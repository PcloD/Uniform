package ru.kaonasi.polyhedra.lib;

public class Vector {
	
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

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector(Vector a) {
		this.x = a.x;
		this.y = a.y;
		this.z = a.z;
	}

	public double dot(Vector b)	{
	    return x * b.x + y * b.y + z * b.z;
	}

	public Vector rotate(Vector axis, double angle) {
		Vector a, b, c;
		a = axis.scale(dot(axis));
		b = diff(a).scale(Math.cos(angle));
		c = axis.cross(this).scale(Math.sin(angle));
		return a.sum3(b, c);
	};
	
	public Vector sum3(Vector b, Vector c) { return new Vector(x + b.x + c.x, y + b.y + c.y, z + b.z + c.z); };
	public Vector scale(double k) { return new Vector(x * k, y * k, z * k); };
	public Vector sum(Vector b) { return new Vector(x + b.x, y + b.y, z + b.z); }
	public Vector diff(Vector b) { return new Vector(x - b.x, y - b.y, z - b.z); };
	public Vector cross(Vector b) { return new Vector(y * b.z - z * b.y, z * b.x - x * b.z, x * b.y - y * b.x); };

	public boolean same(Vector b, double epsilon) {
	    return Math.abs(x - b.x) < epsilon && Math.abs(y - b.y) < epsilon && Math.abs(z - b.z) < epsilon;
	}

	public static Vector rotate(Vector vertex, Vector axis, double angle) {
		Vector temp = new Vector(vertex);
		return temp.rotate(axis, angle);
	}
	
	public double angle(Vector b) {
		return Math.acos((x*b.x + y*b.y + z*b.z)/(Math.sqrt(x*x + y*y + z*z)*Math.sqrt(b.x*b.x + b.y*b.y + b.z*b.z))); 
	}
	
	public static double angle(Vector a, Vector b) {
		return a.angle(b); 
	}
}
