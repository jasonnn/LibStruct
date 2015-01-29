package net.indiespot.struct;

import net.indiespot.struct.api.annotations.CopyStruct;
import net.indiespot.struct.api.annotations.TakeStruct;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestStructReturnType extends AbstractRuntimeTest {

    @Test
    public void testReturnSelf() throws Exception {
        Vec3 vec1 = new Vec3();
        vec1.x = 13.37f;
        Vec3 vec2 = returnSelf(vec1);

        assert vec1 == vec2;
    }

    @Test
    public void testReturnCopy() throws Exception {
        Vec3 vec1 = new Vec3();
        vec1.x = 13.37f;
        Vec3 vec3 = returnCopy(vec1);

        assert vec1 != vec3;
    }

    @TakeStruct
    public static Vec3 returnSelf(Vec3 vec) {
        return vec;
    }

    @CopyStruct
    public static Vec3 returnCopy(Vec3 vec) {
        return vec;
    }
}
