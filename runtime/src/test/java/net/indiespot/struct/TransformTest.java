package net.indiespot.struct;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
@SuppressWarnings("ConstantConditions")
public class TransformTest {
    @Test
    public void testOneInstance() throws Exception {
        new Vec3();

    }

    @Test
    public void testOneInstanceNull() throws Exception {

        Object obj = new Object();
        Vec3 vec = new Vec3();
        assert !(vec == null);
        assert !(obj == null);
        assert (vec != null);
        assert (obj != null);
        assert (vec == vec);
        assert (obj == obj);
    }

    @Test
    public void testOneInstanceNullRef() throws Exception {

        // Vec3 vec = new Vec3();
        // vec.x = 5.6f;
        // echo(vec.x);
        // vec = null; FIXME: very hard to fix
        // echo(vec.x);
    }

    @Test
    public void testOneInstanceInstanceof() {

        Object obj = new Object();
        Vec3 vec = new Vec3();
        assert !(vec instanceof Vec3);
        assert (obj instanceof Object);

    }


    @Test
    public void TestTwoInstances() {

        Vec3 vec1 = new Vec3();
        Vec3 vec2 = new Vec3();
        assert vec1 != vec2;

    }
}
