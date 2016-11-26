package 	ru.kaonasi.polyhedra.lib;

public class Kaleido {

	public static void main(String[] args) {
		
		Polyhedron P = new Polyhedron();
		try {
			P.kaleido("#12", true, true, false, false);
			for (double x: P.p) {
				System.out.println(x);
			}
			System.out.println("polyform = " + P.polyform);
			System.out.println("Euler ch = " + P.chi);
			System.out.println("Symmetry group = " + P.K);
			System.out.println("Number of faces types = " + P.N);
			System.out.println("Vertex valency = " + P.M);
			System.out.println("Name = " + P.name);
			System.out.println("Dual name = " + P.dual_name);
			System.out.println("Vertex configuration = " + P.config);
			System.out.println("Vertices = " + P.V);
			System.out.println("Faces = " + P.F);
		} catch (PolyhedronException e) {
			System.out.println("Kaleido: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
