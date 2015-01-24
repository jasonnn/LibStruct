package net.indiespot.struct;

import net.indiespot.struct.api.Struct;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TestRule;

/**
 * Created by jason on 1/24/15.
 */
@SuppressWarnings("PointlessBooleanExpression")
public class TestStructEnv {
    @ClassRule
    public static TestRule gc = new GCListenerRule();

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
        assert Struct.isReachable(new StructTest.Vec3()) == true;
    }

    @Test
    public void test3() {
        assert Struct.getPointer(new StructTest.Vec3()) > 0L;
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
