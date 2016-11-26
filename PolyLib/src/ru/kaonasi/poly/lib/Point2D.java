package ru.kaonasi.poly.lib;

public class Point2D {

	private double x, y;

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
	
	public Point2D() {
		this.x = 0;
		this.y = 0;
	}

	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point2D(Point2D p) {
		this.x = p.x;
		this.y = p.y;
	}

	public boolean same(Point2D p) {
		return (x == p.x && y == p.y);
	}
}
