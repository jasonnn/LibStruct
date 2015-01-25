package net.indiespot.struct.testlib;

import net.indiespot.struct.api.StructField;
import net.indiespot.struct.api.StructType;

/**
 * Created by jason on 1/24/15.
 */
@StructType
public class ArrayEmbed {
    @StructField(length = 2)
    public float[] farr;
    @StructField(length = 15)
    public int[] iarr;
    @StructField(length = 2)
    public double[] darr;

    @Override
    public String toString() {
        return "ArrayEmbed[]";
    }
}
