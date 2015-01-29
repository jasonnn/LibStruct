package net.indiespot.struct;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by jason on 1/24/15.
 */
@SuppressWarnings("PointlessBooleanExpression")
public class TestStructEnv extends AbstractRuntimeTest {


    @Test(expected = AssertionError.class)
    public void makeSureAssertionsAreEnabled() {
            assert false;
    }

    @Test
    public void testNullReachability() {
        assert Struct.isReachable(null) == false;
    }

    @Test
    public void testStructReachability() {
        assert Struct.isReachable(new Vec3()) == true;
    }

    @Test
    public void testPointerIsProbablyValid() {
        assert Struct.getPointer(new Vec3()) > 0L;
    }

    @Ignore("nothing tested")
    @Test
    public void test4() {
        Class<?> typ1 = String.class;
        // Class<?> typ2 = Vec3.class;

        System.out.println(typ1);
        // System.out.println("x="+typ2);
        // System.exit(-1);
    }
}
