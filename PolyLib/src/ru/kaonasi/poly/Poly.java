package ru.kaonasi.poly;

import java.util.ArrayList;
import java.util.List;


//import ru.kaonasi.poly.lib.Line2D;
//import ru.kaonasi.poly.lib.Point2D;
import ru.kaonasi.poly.lib.Face;
import ru.kaonasi.polyhedra.lib.Fraction;
import ru.kaonasi.polyhedra.lib.Polyhedron;
import ru.kaonasi.polyhedra.lib.PolyhedronException;
import ru.kaonasi.polyhedra.lib.Vector;

public class Poly {
	
	private static Polyhedron P;

	public static void main(String[] args) {
		calculate();
	}
	
//	private static void testLine() {
//		Point2D p1 = new Point2D(-1,1);
//		Point2D p2 = new Point2D(-3,1);
//		Point2D p3 = new Point2D(-1,1);
//		Point2D p4 = new Point2D(-3,3);
//		try {
//			Line2D l1 = new Line2D(p1, p2);
//			Line2D l2 = new Line2D(p3, p4);
//			Point2D p = Line2D.intercept(l1, l2);
//			System.out.println(p.getX() + " " + p.getY());
////			System.out.println(l.getX() + " " + l.getY());
//		} catch (PolyhedronException e) {
//			System.out.println(e.getMessage());
//		}
//	}
	
	private static void calculate() {
		int p = 72;
		P = new Polyhedron();
		try {
			P.kaleido("#" + (p + 5), true, true, false, false);
			System.out.println("//polyform = " + P.getPolyform());
			System.out.println("//Euler ch = " + P.getChi());
			System.out.println("//Symmetry group = " + P.getK());
			System.out.println("//Number of faces types = " + P.getNFacesTypes());
			System.out.println("//Vertex valency = " + P.getVertesValency());
			System.out.println("//Name = " + P.getName());
			System.out.println("//Dual name = " + P.getDual_name());
			System.out.println("//Vertex configuration = " + P.getConfig());
			System.out.println("//Vertices = " + P.getNVertex());
			System.out.println("//Faces = " + P.getNFaces());
			System.out.println("//Faces array count = " + P.getF().length);
			
/*			System.out.printf("Edges:\n");
			for (int[] v: P.getE()) {
				for (int x: v) {
					System.out.printf("%3d", x);
				}
				System.out.printf("\n");
			}

			System.out.printf("Incid:\n");
			for (int[] v: P.getIncid()) {
				for (int x: v) {
					System.out.printf("%3d", x);
				}
				System.out.printf("\n");
			}
			
			System.out.printf("Vertex-vertex adjacency:\n");
			for (int[] v: P.getAdj()) {
				for (int x: v) {
					System.out.printf("%3d", x);
				}
				System.out.printf("\n");
			}
*/
//			Vector[] vs = P.getF();
			List<Face> faces = new ArrayList<Face>();
			// обойти все грани
			for (int faceCount = 0; faceCount < P.getNFaces(); faceCount++) {
				// найти вершину, содержащую грань
				int v1 = seekVertex(faceCount);
//				System.out.println("v1="+v1+" faceCount="+faceCount);
				Face face = new Face(P.getF()[faceCount], P.getV()[v1], P.getN()[P.getFtype()[faceCount]]);
				// найти все вершины при грани
				face.setPoint(v1);
				boolean end = false;
				while (!end) {
					// найти соседа вершины v1, содержащего грань faceCount
					int v2 = -1;
					// пройти по столбцу [v1], проверяя каждую вершину на содержание грани faceCount
					for (int i = 0; i < P.getVertesValency(); i++) {
						v2 = P.getAdj()[i][v1];
						// проверим, содержит ли v2 грань faceCount, пройдёмся по incid
						boolean found = false;
						for (int j = 0; j < P.getVertesValency(); j++) {
							if (P.getIncid()[j][v2] == faceCount) {
								// v2 содержит грань faceCount, надо проверить не принадлежит ли уже эта вершина грани faceCount
								if (face.vertexExists(v2)) {
									continue;
								} else {
									v1 = v2;
									found = true;
									break;
								}
							}
						}
						if (found) {
							face.setPoint(v2);
						}
						if (face.corners() == face.points()) {
							end = true;
						}
					}
				}
				faces.add(face);
			}

			// vertices
			String vertices = "#declare p" + p + " = array[%2d] {\n";
			int i;
			for (i = 0; i < P.getV().length; i++) {
				Vector v = P.getV()[i];
				vertices += String.format("%s", String.format("  <%17.14f; %17.14f; %17.14f>", v.getX(), v.getY(), v.getZ()).replace(",", ".").replace(";", ","));
				vertices += ",\n";
			}
			boolean auxiliaryNeeded = false;
			for (double c: P.getN()) {
				Fraction frax = new Fraction(c);
				if (frax.getD() > 1 && frax.getD() != (frax.getN() - 1)) {
					auxiliaryNeeded = true;
				}
			}
			if (auxiliaryNeeded) {
				vertices += "\n// auxiliary vertices\n";
				for (Face f: faces) {
					if (f.getDenominator() > 1 && f.getDenominator() != (f.getNominator() - 1)) {
						vertices += String.format("%s", String.format("  <%17.14f; %17.14f; %17.14f>", f.getCenter().getX(), f.getCenter().getY(), f.getCenter().getZ()).replace(",", ".").replace(";", ","));
						vertices += ",\n";
						f.setCenterPoint(i++);
					}
				}
			}
			vertices += "  <0,0,0>\n}\n";
			System.out.printf(vertices, i + 1);//P.getV().length);

			System.out.println("#declare FACES" + p + " = union {");
			for (i = 0; i < P.getNFacesTypes(); i++) {
				for (Face f: faces) {
					if (f.getConfiguration() == P.getN()[i]) {
						System.out.println(f.toString(f.getCenterPoint(), "p" + p, null));
					}
				}
			}
			System.out.println("} // " + faces.size());

		} catch (PolyhedronException e) {
			System.out.println("Kaleido: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static int seekVertex(int face) throws PolyhedronException {
//		System.out.printf("поиск первой вершины грани:");
		for (int vc = 0; vc < P.getNVertex(); vc++) {
			for (int vv = 0; vv < P.getVertesValency(); vv++) {
				int incid = P.getIncid()[vv][vc]; 
//				System.out.printf("%4d", incid);
				if (incid == face) {
//					System.out.printf(" найдена вершина %4d\n", vc);
					return vc;
				}
			}
		}
		throw new PolyhedronException("Не найдена ни одна вершина, принадлежащая грани " + face);
	}

}
