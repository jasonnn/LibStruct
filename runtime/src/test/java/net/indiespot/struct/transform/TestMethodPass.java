package net.indiespot.struct.transform;

import net.indiespot.struct.AbstractRuntimeTest;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */

public class TestMethodPass extends AbstractRuntimeTest {

    @Test
    public void test() {
        Vec3 vec = new Vec3();
        vec.x = 3.34f;
        vec.y = 4.46f;
        vec.z = 5.56f;

        test(vec);
    }

    public static void test(Vec3 vec) {
        assert vec.x == 3.34f;
        assert vec.y == 4.46f;
        assert vec.z == 5.56f;
    }
}
