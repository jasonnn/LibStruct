package net.indiespot.struct.transform;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestConstructor {

    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        Vec3 v;

        v = new Vec3();
        assert v.x == 0.0f;
        assert v.y == 0.0f;
        assert v.z == 0.0f;

        v = new Vec3(1, 2, 3);
        assert v.x == 1;
        assert v.y == 2;
        assert v.z == 3;

        v = new Vec3(1.2f, 3.4f, 5.6f);
        assert v.x == 1.2f;
        assert v.y == 3.4f;
        assert v.z == 5.6f;

        v = new Vec3(1337f);
        assert v.x == 1337f;
        assert v.y == 1337f;
        assert v.z == 1337f;
    }
}
