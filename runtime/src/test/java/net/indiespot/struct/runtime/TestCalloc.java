package net.indiespot.struct.runtime;

import net.indiespot.struct.AbstractRuntimeTest;
import net.indiespot.struct.Struct;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jason on 1/25/15.
 */
public class TestCalloc extends AbstractRuntimeTest {
    @Test
    public void test() {
        Vec3 a = Struct.malloc(Vec3.class);
        Vec3 b = Struct.calloc(Vec3.class);

        Assert.assertNotEquals(0.0f, a.x);
        Assert.assertTrue(a.y != 0.0f);
        Assert.assertTrue(a.z != 0.0f);

        assertEquals(0.0f, b.x, 0.0);
        assertEquals(0.0f, b.y, 0.0);
        assertEquals(0.0f, b.z, 0.0);

        Struct.free(a);
        Struct.free(b);
    }
}
