package net.indiespot.struct;

import net.indiespot.struct.testlib.Ship;
import net.indiespot.struct.testlib.Vec3;

/**
 * Created by jason on 1/25/15.
 */
public class TestNull2 {


    public static void test() {
        Ship ship = new Ship();
        // ship = null;
        // ship = Struct.typedNull(Ship.class);
        ship.id = 0;
        ship.pos = new Vec3();
    }
}
