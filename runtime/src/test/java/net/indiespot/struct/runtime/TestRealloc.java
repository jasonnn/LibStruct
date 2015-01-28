package net.indiespot.struct.runtime;

import net.indiespot.struct.Struct;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestRealloc {

    @Test
    public void runTest() throws Exception {
        test();
    }

    public void test() {
        Vec3[] arr = Struct.mallocArray(Vec3.class, 13);
        assert arr.length == 13;

        arr[4].x = 13.14f;
        arr[7].y = 17.13f;

        arr = Struct.reallocArray(Vec3.class, arr, 13);
        assert arr.length == 13;
        assert arr[4].x == 13.14f;
        assert arr[7].y == 17.13f;

        arr = Struct.reallocArray(Vec3.class, arr, 5);
        assert arr.length == 5;
        assert arr[4].x == 13.14f;

        arr = Struct.reallocArray(Vec3.class, arr, 8);
        assert arr.length == 8;
        assert arr[4].x == 13.14f;

        Struct.free(arr);
    }
}
