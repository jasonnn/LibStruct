package net.indiespot.struct.transform;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
@SuppressWarnings("UnnecessaryReturnStatement")
public class TestIfFlow {
    @Test
    public void runTest() throws Exception {
        test();
        test2();
        test3();
    }

    public static void test() {
        Vec3 vec = new Vec3();

        if (Math.random() < 0.5)
            return;
        // System.out.println(v);
    }

    public static void test2() {
        Vec3 vec = new Vec3();

        if (Math.random() < 0.5) {
// System.out.println(v);
        }
        return;
    }

    public static void test3() {
        Vec3 vec = new Vec3();

        if (Math.random() < 0.5) {
// System.out.println(v);
        } else {
// System.out.println(v);
        }
    }
}
