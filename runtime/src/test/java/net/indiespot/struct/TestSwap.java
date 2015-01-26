package net.indiespot.struct;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestSwap {
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        Vec3 a = new Vec3(1.20f, 2.30f, 3.40f);
        Vec3 b = new Vec3(1.02f, 2.03f, 3.04f);

        String A = "Vec3[1.2, 2.3, 3.4]";
        String B = "Vec3[1.02, 2.03, 3.04]";

        Assert.assertEquals(A, a.toString());
        Assert.assertEquals(B, b.toString());


        Struct.swap(Vec3.class, a, b);

        Assert.assertEquals(B, a.toString());
        Assert.assertEquals(A, b.toString());
    }
}
