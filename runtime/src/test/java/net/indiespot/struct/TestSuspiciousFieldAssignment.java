package net.indiespot.struct;

import net.indiespot.struct.api.runtime.SuspiciousFieldAssignmentError;
import net.indiespot.struct.testlib.Ship;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestSuspiciousFieldAssignment extends AbstractRuntimeTest {

    public static Vec3 field;

    @Test
    public void test() {
        try {
            field = new Vec3();
            assert false;
        } catch (SuspiciousFieldAssignmentError err) {
            assert true;
        }

        field = Struct.malloc(Vec3.class);

        Ship ship = new Ship();
        ship.pos = field;
        ship.pos = new Vec3();

        ship = Struct.calloc(Ship.class);
        ship.pos = field;
        try {
            ship.pos = new Vec3();
            assert false;
        } catch (SuspiciousFieldAssignmentError err) {
            assert true;
        }

        Struct.free(field);
        Struct.free(ship);
    }
}
