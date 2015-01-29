package net.indiespot.struct.transform;

import net.indiespot.struct.AbstractRuntimeTest;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
@SuppressWarnings("ALL")
public class TestIfFlow extends AbstractRuntimeTest {

    @Test
    public void testEarlyReturn() {
        Vec3 vec = new Vec3();

        if (Math.random() < 0.5)
            return;
        // System.out.println(v);
    }

    @Test
    public void testSimpleIfStmnt() {
        Vec3 vec = new Vec3();

        if (Math.random() < 0.5) {
// System.out.println(v);
        }
        return;
    }

    @Test
    public void testIfElse() {
        Vec3 vec = new Vec3();

        if (Math.random() < 0.5) {
// System.out.println(v);
        } else {
// System.out.println(v);
        }
    }
}
