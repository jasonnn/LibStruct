package net.indiespot.struct;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestCalloc {

    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        Vec3 a = Struct.malloc(Vec3.class);
        Vec3 b = Struct.calloc(Vec3.class);

        Assert.assertNotEquals(0.0f, a.x);
        Assert.assertTrue(a.y != 0.0f);
        Assert.assertTrue(a.z != 0.0f);

        Assert.assertTrue(b.x == 0.0f);
        Assert.assertTrue(b.y == 0.0f);
        Assert.assertTrue(b.z == 0.0f);

        Struct.free(a);
        Struct.free(b);
    }
}
