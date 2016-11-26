package ru.kaonasi.poly.lib;

import ru.kaonasi.polyhedra.lib.PolyhedronException;

public class Line2D {
	private double tg; // тангенс угла наклона
	private double x; // пересечение с осью x
	private double y; // пересечение с осью y

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

	public Line2D(Line2D l) {
		this.x = l.x;
		this.y = l.y;
	}

	public boolean same (Line2D p) {
		return (x == p.x && y == p.y);
	}

	public boolean parallel(Line2D l) {
		if ((Double.isNaN(x) && Double.isNaN(l.x)) || (Double.isNaN(y) && Double.isNaN(l.y))) {
			return true;
		} else {
			return (tg == l.tg);
		}
	}
	public Line2D(Point2D A1, Point2D A2) throws PolyhedronException {
		if (A1.equals(A2)) {
			throw new PolyhedronException("Невозможно построить прямую через одну точку");
		}
		if (A1.getX() == A2.getX()) {
			// прямая параллельна оси Y
			x = A1.getX();
			y = Double.NaN;
			tg = Double.NaN;
		} else if (A1.getY() == A2.getY()) {
			// прямая параллельна оси X
			x = Double.NaN;
			y = A1.getY();
			tg = 0;
		} else {
			double a;
			a = (A1.getY() - A2.getY())/(A1.getX() - A2.getX());
			y = A1.getY() - a*A1.getX();
			tg = a;
			x = -y/a;
		}
	}
	
	public Point2D intercept(Line2D l) throws PolyhedronException{
		if (parallel(l)) {
			throw new PolyhedronException("Параллельные прямые не пересекаются");
		}
		if (Double.isNaN(x)) {
			// прямая параллельна x
			if (Double.isNaN(l.y)) {
				// вторая прямая паралельна y
				return new Point2D(l.x, y);
			} else {
				return new Point2D((y - l.y)/l.tg ,y);
			}
		} else if (Double.isNaN(y)) {
			// прямая параллельна y
			if (Double.isNaN(l.x)) {
				// вторая прямая параллельна y
				return new Point2D(x, l.y);
			} else {
				return new Point2D(x, l.tg*x + l.y);
			}
		} else {
			// прямая не параллельна осям координат
			if (l.x == Double.NaN || l.y == Double.NaN) {
				// вторая прямая параллельна одной из осей
				return l.intercept(this);
			} else {
				// обе прямые не паралельны осям
				double y0 = (l.y - tg*l.tg*y)/(1 - tg*l.tg);
				return new Point2D((tg*y0 + l.y), y0);
			}
		}
	}
	
	public static Point2D intercept(Line2D l1, Line2D l2) throws PolyhedronException {
		return l1.intercept(l2);
	}
	
	public double getX(double Y) {
		return (Y - y)/tg;
	}

	public double getY(double X) {
		return tg*X + y;
	}
}
