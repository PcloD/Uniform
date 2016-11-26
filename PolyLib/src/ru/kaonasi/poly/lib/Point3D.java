package ru.kaonasi.poly.lib;

import ru.kaonasi.polyhedra.lib.Vector;

public class Point3D {
	private double x, y, z;

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public Point3D() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3D(Point3D p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}

	public Point3D(Vector vector) {
		this.x = vector.getX();
		this.y = vector.getY();
		this.z = vector.getZ();
	}
	
}
