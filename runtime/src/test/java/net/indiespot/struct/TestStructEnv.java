package net.indiespot.struct;

import net.indiespot.struct.api.Struct;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/24/15.
 */
@SuppressWarnings("PointlessBooleanExpression")
public class TestStructEnv extends TestBase {


    public static void test() {
        new TestStructEnv().testAll();
    }

    public void testAll() {
        test0();
        test1();
        test2();
        test3();
        test4();
    }

    @Test
    public void test0() {
        try {
            assert false;

            throw new IllegalStateException("asserts must be enabled");
        } catch (AssertionError err) {
            System.out.println("StructTest: asserts are enabled.");
        }
    }

    @Test
    public void test1() {
        assert Struct.isReachable(null) == false;
    }

    @Test
    public void test2() {
        assert Struct.isReachable(new Vec3()) == true;
    }

    @Test
    public void test3() {
        assert Struct.getPointer(new Vec3()) > 0L;
    }

    @Test
    public void test4() {
        Class<?> typ1 = String.class;
        // Class<?> typ2 = Vec3.class;

        System.out.println(typ1);
        // System.out.println("x="+typ2);
        // System.exit(-1);
    }
}
