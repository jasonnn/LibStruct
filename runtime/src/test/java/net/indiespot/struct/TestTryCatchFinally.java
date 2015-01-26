package net.indiespot.struct;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestTryCatchFinally {
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        Vec3 a = new Vec3();

        try {
            a.x = 13;
        } catch (Throwable t) {

        } finally {
            a.y = 14;
        }

        try {
            a.x = 13;
        } catch (Throwable t) {
            System.out.println(t);
        } finally {
            a.y = 14;
        }

        try {
            a.x = 13;
        } catch (Throwable t) {
            throw t;
        } finally {
            a.y = 14;
        }

        try {
            a.x = 13;
        } catch (Throwable t) {
            System.out.println(t);
            throw new IllegalStateException("doh!");
        } finally {
            a.y = 14;
        }
    }
}
