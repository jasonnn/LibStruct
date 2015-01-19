package test.net.indiespot.demo.softlylit.structs.support;

import test.net.indiespot.demo.softlylit.structs.Point;

import net.indiespot.struct.cp.Struct;
import net.indiespot.struct.cp.TakeStruct;
import net.indiespot.struct.transform.StructEnv;

public class PointBlock {
	private final int length;
	private final Point base;

	private int size;

	public PointBlock(int length) {
		this.length = length;
		this.base = Struct.malloc(Point.class, length)[0];
		this.size = 0;
	}

	public void clear() {
		size = 0;
	}

	public int size() {
		return size;
	}

	@TakeStruct
	public Point add() {
		return this.get(size++);
	}

	@TakeStruct
	public Point add(Point src) {
		Point dst = this.add();
		Struct.copy(Point.class, src, dst);
		return dst;
	}

	public void addAll(PointBlock src) {
		if (StructEnv.SAFETY_FIRST)
			if (src.size > this.length - this.size)
				throw new IllegalStateException();
		Struct.copy(Point.class, src.base, this.get(this.size), src.size);
		this.size += src.size;
	}

	@TakeStruct
	public Point get(int index) {
		if (StructEnv.SAFETY_FIRST)
			if (index < 0 || index >= size)
				throw new IllegalStateException();
		return Struct.sibling(base, Point.class, index);
	}

	public void set(int index, Point value) {
		Struct.copy(Point.class, value, this.get(index));
	}

	public void free() {
		Point[] arr = Struct.emptyArray(Point.class, length);
		for (int i = 0; i < length; i++)
			arr[i] = this.get(i);
		Struct.free(arr);
	}
}