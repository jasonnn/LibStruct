package net.indiespot.struct.transform;

import net.indiespot.struct.AbstractRuntimeTest;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
@SuppressWarnings("StatementWithEmptyBody")
public class TestLoopFlow extends AbstractRuntimeTest {

    @Test
    public void testFori() {
        Vec3 vec = new Vec3();
        for (int i = 0; i < 3; i++) {
            // System.out.println(v);
        }
        // System.out.println(v);
    }

    @Test
    public void testWhile() {
        Vec3 vec = new Vec3();
        while (Math.random() < 0.5) {
            // System.out.println(v);
        }
        // System.out.println(v);
    }

    @Test
    public void testDoWhile() {
        Vec3 vec = new Vec3();
        do {
            // System.out.println(v);
        } while (Math.random() < 0.5);
        // System.out.println(v);
    }

    @Test
    public void testFori_2() {
        for (int i = 0; i < 3; i++) {
            // System.out.println(v);
        }
    }

    @Test
    public void testWhile_2() {
        while (Math.random() < 0.5) {
            // System.out.println(v);
        }
    }

    @Test
    public void testDoWhile_2() {
        do {
            // System.out.println(v);
        } while (Math.random() < 0.5);
    }
}
