package net.indiespot.struct;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestNull {
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        new Vec3();
        Vec3 vec = null;
        vec = new Vec3();
        vec = null;
        vec = new Vec3();

        // if(Math.random() < 0.5)
        // vec = new Vec3();
        // else
        // vec = null;
        // test(vec);
    }

    // private static void test(Vec3 nullStruct) {
    // System.out.println(nullStruct);
    // }
}
