package net.indiespot.struct;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestSum extends AbstractRuntimeTest {
    @Test
    public void testSum() {
        Vec3 vec1 = new Vec3();
        vec1.x = 13.34f;
        vec1.y = 14.46f;
        vec1.z = 15.58f;

        Vec3 vec2 = new Vec3();
        vec2.x = 1 + vec1.x;
        vec2.y = 2 + vec1.y;
        vec2.z = 3 + vec1.z;

        assert vec2.x == 14.34f;
        assert vec2.y == 16.46f;
        assert vec2.z == 18.58f;
    }
}
