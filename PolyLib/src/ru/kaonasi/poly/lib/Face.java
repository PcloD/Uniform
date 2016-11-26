package ru.kaonasi.poly.lib;

import ru.kaonasi.polyhedra.lib.Polyhedron;
import ru.kaonasi.polyhedra.lib.Vector;

public class Face {
	
	private Point3D center;
	private Polygon polygon;
	private int centerPoint;

	public Face(Vector face, Vector vertex, double configuration) {
		double angle = Vector.angle(face, vertex);
		double r = Math.sqrt(face.getX()*face.getX() + face.getY()*face.getY() + face.getZ()*face.getZ());
		double r0 = Math.sqrt(vertex.getX()*vertex.getX() + vertex.getY()*vertex.getY() + vertex.getZ()*vertex.getZ());
		if (Math.abs(r - r0) < Polyhedron.DBL_EPSILON) {
			center = new Point3D(face.getX()*Math.cos(angle), face.getY()*Math.cos(angle), face.getZ()*Math.cos(angle));
		} else {
			center = new Point3D(face.getX()*(r0/r)*Math.cos(angle), face.getY()*(r0/r)*Math.cos(angle), face.getZ()*(r0/r)*Math.cos(angle));
		}
		centerPoint = -1;
		polygon = new Polygon(configuration);
	}
	
	public void setPoint(int p) {
		polygon.setPoint(p);
	}
	
	public String toString(int center, String arrayName, String color) {
		return polygon.toString(center, arrayName, color) + " // " + polygon.getNominator() + (polygon.getDenominator()>1?"/"+polygon.getDenominator():"");
	}
	
	public Point3D getCenter() {
		return center;
	}
	
	public boolean vertexExists(int vertex) {
		return polygon.vertexExists(vertex);
	}

	public int points() {
		return polygon.pointCount();
	}
	
	public int corners() {
		return (int)polygon.getNominator();
	}
	public double getConfiguration() {
		return polygon.getConfiguration();
	}
	
	public long getNominator() {
		return polygon.getNominator();
	}
	
	public long getDenominator() {
		return polygon.getDenominator();
	}

	public int getCenterPoint() {
		return centerPoint;
	}

	public void setCenterPoint(int centerPoint) {
		this.centerPoint = centerPoint;
	}
	
}
