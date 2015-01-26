package net.indiespot.struct.softlylit.structs;


import net.indiespot.struct.api.annotations.StructField;
import net.indiespot.struct.api.annotations.StructType;
import net.indiespot.struct.api.annotations.TakeStruct;

@StructType
public class Triangle {
	@StructField(embed = true)
	public Point a;

	@StructField(embed = true)
	public Point b;

	@StructField(embed = true)
	public Point c;
/*
	@StructField(embed = true)
	public Line ab;

	@StructField(embed = true)
	public Line bc;

	@StructField(embed = true)
	public Line ca;
*/
	private Triangle() {
		// hide
	}

	@TakeStruct
	public Triangle load(Point a, Point b, Point c) {
		this.a.load(a);
		this.b.load(b);
		this.c.load(c);
		return this;
	}
/*
	public void sync() {
		ab.load(a, b);
		bc.load(b, c);
		ca.load(c, a);
	}
	*/
}
