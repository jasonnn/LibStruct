package net.indiespot.struct.transform;

import net.indiespot.struct.AbstractRuntimeTest;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestInstanceMethod extends AbstractRuntimeTest {
    @Test
    public void test() {
        Vec3 a = new Vec3();
        a.x = 5;

        Vec3 b = new Vec3();
        b.x = 6;

        Vec3 vec = new Vec3();
        vec.add(a);
        vec.add(b);

        // System.out.println(v);

        Vec3 copy = vec.copy();
        // System.out.println(v);

        copy.x = 15;
        // System.out.println(v);
        // System.out.println(v);
    }
}
