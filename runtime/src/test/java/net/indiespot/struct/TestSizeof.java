package net.indiespot.struct;

import net.indiespot.struct.testlib.Ship;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestSizeof {
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        int sizeofVec3 = Struct.sizeof(Vec3.class);
        int sizeofShip = Struct.sizeof(Ship.class);

        assert (sizeofVec3 == 12);
        assert (sizeofShip == 8);
    }
}
