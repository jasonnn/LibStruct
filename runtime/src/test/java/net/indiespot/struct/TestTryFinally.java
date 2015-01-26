package net.indiespot.struct;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestTryFinally {
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        Vec3 a = new Vec3();

        try {
            a.x = 5;
        } finally {
            a.y = 6;
        }
    }
}
