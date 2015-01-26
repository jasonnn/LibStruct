package net.indiespot.struct;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestSetter {
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        Vec3 v = new Vec3();
        assert v.x == 0;
        assert v.y == 0;
        assert v.z == 0;
        Vec3 ref = v.set(4567.8f, 4.5f, 3);
        if (v != ref)
            throw new IllegalStateException();
        assert ref.x == 4567.8f;
        assert ref.y == 4.5f;
        assert ref.z == 3f;
    }
}
