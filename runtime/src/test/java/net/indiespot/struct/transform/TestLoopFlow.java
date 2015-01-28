package net.indiespot.struct.transform;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestLoopFlow {
    @Test
    public void runTest() throws Exception {
        test();
        test2();
        test3();
        test4();
        test5();
        test6();
    }

    public static void test() {
        Vec3 vec = new Vec3();
        for (int i = 0; i < 3; i++) {
            // System.out.println(v);
        }
        // System.out.println(v);
    }

    public static void test2() {
        Vec3 vec = new Vec3();
        while (Math.random() < 0.5) {
            // System.out.println(v);
        }
        // System.out.println(v);
    }

    public static void test3() {
        Vec3 vec = new Vec3();
        do {
            // System.out.println(v);
        } while (Math.random() < 0.5);
        // System.out.println(v);
    }

    public static void test4() {
        for (int i = 0; i < 3; i++) {
            // System.out.println(v);
        }
    }

    public static void test5() {
        while (Math.random() < 0.5) {
            // System.out.println(v);
        }
    }

    public static void test6() {
        do {
            // System.out.println(v);
        } while (Math.random() < 0.5);
    }
}
