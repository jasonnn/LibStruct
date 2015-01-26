package net.indiespot.struct;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */

public class TestStructField {
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        new TestStructField().testInstance();
        TestStructField.testStatic();
        TestStructField.testStatic2();
    }

    public Vec3 vec1;

    public void testInstance() {
        vec1 = Struct.calloc(Vec3.class);
        vec1.x = 43.21f;
        Vec3 that = vec1;
        vec1 = that;
        assert (vec1.x == 43.21f);
        assert (that.x == 43.21f);
        Struct.free(vec1);
    }

    public static Vec3 vec2;
    public static Vec3[] arr;

    public static void testStatic() {
        vec2 = Struct.calloc(Vec3.class);

        vec2.x = 12.34f;
        Vec3 that = vec2;
        vec2 = that;
        assert (vec2.x == 12.34f);
        assert (that.x == 12.34f);

        arr = Struct.mallocArray(Vec3.class, 13);
    }

    public static void testStatic2() {
        vec2.x = 4;
        Struct.free(vec2);

        arr[0].x = 5;

        for (Vec3 item : arr)
            Struct.free(item);
    }
}
