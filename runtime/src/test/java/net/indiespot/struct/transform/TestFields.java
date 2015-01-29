package net.indiespot.struct.transform;

import net.indiespot.struct.AbstractRuntimeTest;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
@SuppressWarnings("ConstantConditions")
public class TestFields extends AbstractRuntimeTest {


    @Test
    public void testGetSet() {
        Vec3 vec = new Vec3();
        vec.x = 13.34f;
        vec.y = 14.46f;
        vec.z = 15.56f;
        assert vec.x == 13.34f;
        assert vec.y == 14.46f;
        assert vec.z == 15.56f;
    }
}
