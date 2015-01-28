package net.indiespot.struct.runtime;

import net.indiespot.struct.Struct;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestCopy {

    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        Vec3 a = Struct.malloc(Vec3.class);
        a.x = 12.34f;
        a.y = 23.45f;
        a.z = 34.56f;

        Vec3 b = Struct.malloc(Vec3.class);
        b.x = 00.00f;
        b.y = 00.00f;
        b.z = 00.00f;

        assert (a.x == 12.34f);
        assert (b.x == 00.00f);
        Struct.copy(Vec3.class, a, b);
        assert (a.x == 12.34f);
        assert (b.x == 12.34f);

        Struct.free(a);
        Struct.free(b);
    }
}
