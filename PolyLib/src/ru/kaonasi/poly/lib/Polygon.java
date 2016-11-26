package ru.kaonasi.poly.lib;

import java.util.ArrayList;
import java.util.List;

import ru.kaonasi.polyhedra.lib.Fraction;

public class Polygon {

	private List<Integer> points;
	private Fraction frax;
	private double configuration;

	public Polygon(double configuration) {
		this.configuration = configuration;
		frax = new Fraction(configuration);
		this.points = new ArrayList<Integer>();
	}

	public void setPoint(int p) {
		points.add(p);
	}
	
	public String toString(int center, String arrayName, String color) {
		String ret = "";
		if (frax.getD() == 1 || frax.getD() == (frax.getN() - 1)) {
			// выпуклый многоугольник
			if (frax.getN() == 3) {
				// треугольник: triangle{p01[0],p01[1],p01[2] pigment {color Red}}
				ret += "triangle{";
				for (int p: points) {
					ret += arrayName + "[" + String.format("%2d", p) + "],";
				}
				ret = ret.substring(0, ret.length() - 1);
				ret += " pigment {color " + (color==null||color.isEmpty()?"White":color) +"}}";
			} else {
				// многоугольник
				ret += "polygon{" + frax.getN() + ",";
				for (int p: points) {
					ret += arrayName + "[" + String.format("%2d", p) + "],";
				}
				ret = ret.substring(0, ret.length() - 1);
				ret += " pigment {color " + (color==null||color.isEmpty()?"White":color) +"}}";
			}
		} else {
			// невыпуклые многоугольник
			ret += "union {\n";
			int previous = -1;
			for (int p: points) {
				if (previous < 0) {
					previous = p;
				} else {
					ret += "  triangle{";
					ret += arrayName + "[" + String.format("%2d", previous) + "],";
					ret += arrayName + "[" + String.format("%2d", center) + "],";
					ret += arrayName + "[" + String.format("%2d", p) + "]}\n";
					previous = p;
				}
			}
			ret += "  triangle{";
			ret += arrayName + "[" + String.format("%2d", previous) + "],";
			ret += arrayName + "[" + String.format("%2d", center) + "],";
			ret += arrayName + "[" + String.format("%2d", points.get(0)) + "]}\n";
			ret += "  pigment {color " + (color==null||color.isEmpty()?"White":color) +"}\n}";
		}
		return ret;
	}

	public long getNominator() {
		return frax.getN();
	}
	
	public long getDenominator() {
		return frax.getD();
	}
	
	public boolean vertexExists(int vertex) {
		for (int i: points) {
			if (i == vertex) {
				return true;
			}
		}
		return false;
	}
	
	public int pointCount() {
		return points.size();
	}

	public double getConfiguration() {
		return configuration;
	}
	
}
