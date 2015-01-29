package net.indiespot.struct.transform;

import net.indiespot.struct.AbstractRuntimeTest;
import net.indiespot.struct.Struct;
import net.indiespot.struct.testlib.Ship;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestSizeof extends AbstractRuntimeTest {

    @Test
    public void test() {
        int sizeofVec3 = Struct.sizeof(Vec3.class);
        int sizeofShip = Struct.sizeof(Ship.class);

        assert (sizeofVec3 == 12);
        assert (sizeofShip == 8);
    }
}
