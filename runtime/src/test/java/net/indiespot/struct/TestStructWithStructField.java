package net.indiespot.struct;

import net.indiespot.struct.testlib.Ship;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestStructWithStructField {
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        Ship ship = new Ship();
        assert (ship.id == 100001);
        ship.id++;
        assert (ship.id == 100002);
        assert (ship.pos == null);

        ship.pos = new Vec3();
        assert (ship.pos != null);
    }
}
