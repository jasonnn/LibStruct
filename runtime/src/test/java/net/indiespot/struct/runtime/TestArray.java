package net.indiespot.struct.runtime;

import net.indiespot.struct.AbstractRuntimeTest;
import net.indiespot.struct.Struct;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
@SuppressWarnings("ALL")
public class TestArray extends AbstractRuntimeTest {

    @Test
    public void test() {
        Vec3[] arr = new Vec3[10];
        arr[arr.length - 1].x = 4.5f;
        arr[arr.length - 1].set(5.6f, 6.7f, 7.8f);

        long p1 = Struct.getPointer(arr[0]);
        long p2 = Struct.getPointer(arr[1]);
        if (p2 - p1 != 12)
            throw new IllegalStateException();

        Vec3 a = arr[0];
        Vec3 b = arr[1];

        a.x = 3.7f;
        b.x = 4.8f;
        assert a.x == arr[0].x;
        assert b.x == arr[1].x;
    }
}
