package net.indiespot.struct.testlib;

import net.indiespot.struct.api.annotations.CopyStruct;
import net.indiespot.struct.api.annotations.StructField;
import net.indiespot.struct.api.annotations.StructType;
import net.indiespot.struct.api.annotations.TakeStruct;

/**
 * Created by jason on 1/24/15.
 */
@StructType
public class Vec3 {
    @StructField
    public float x;
    @StructField
    public float y;
    @StructField
    public float z;
    public static int aaaaaaah;

    public Vec3() {
        this(0.0f, 0.0f, 0.0f);
    }

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(float xyz) {
        this(xyz, xyz, xyz);
    }

    @TakeStruct
    public Vec3 add(Vec3 that) {
        this.x += that.x;
        this.y += that.y;
        this.z += that.z;
        return this;
    }

    @TakeStruct
    public Vec3 mul(Vec3 that) {
        this.x *= that.x;
        this.y *= that.y;
        this.z *= that.z;
        return this;
    }

    @TakeStruct
    public Vec3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    @CopyStruct
    public Vec3 copy() {
        return this;
    }

    public static void noob() {
        // System.out.println("n00b!");
    }

    @Override
    public String toString() {
        return "Vec3[" + x + ", " + y + ", " + z + "]";
    }
}
