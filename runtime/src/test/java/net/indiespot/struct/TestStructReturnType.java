package net.indiespot.struct;

import net.indiespot.struct.api.annotations.CopyStruct;
import net.indiespot.struct.api.annotations.TakeStruct;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestStructReturnType {
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        Vec3 vec1 = new Vec3();
        vec1.x = 13.37f;
        Vec3 vec2 = returnSelf(vec1);
        Vec3 vec3 = returnCopy(vec1);

        if (vec1 != vec2)
            throw new IllegalStateException("vec1 != vec2");
        if (vec1 == vec3)
            throw new IllegalStateException("vec1 == vec3");

        vec1.x = 73.31f;
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
