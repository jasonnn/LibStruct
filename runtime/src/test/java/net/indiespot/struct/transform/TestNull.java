package net.indiespot.struct.transform;

import net.indiespot.struct.AbstractRuntimeTest;
import net.indiespot.struct.testlib.Ship;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestNull extends AbstractRuntimeTest {

    @Test
    public void test() {
        new Vec3();
        Vec3 vec = null;
        vec = new Vec3();
        vec = null;
        vec = new Vec3();

        // if(Math.random() < 0.5)
        // vec = new Vec3();
        // else
        // vec = null;
        // test(vec);
    }

    // private static void test(Vec3 nullStruct) {
    // System.out.println(nullStruct);
    // }

    @Test
    public void test2() {
        Ship ship = new Ship();
        // ship = null;
        // ship = Struct.typedNull(Ship.class);
        ship.id = 0;
        ship.pos = new Vec3();
    }
}
