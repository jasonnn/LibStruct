package net.indiespot.struct;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestAddress {
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        Vec3 vec = new Vec3();
        Object obj = new Object();

        // System.out.println("addr=" + Struct.getPointer(vec));
        // System.out.println("addr=" + Struct.getPointer(obj));
    }
}
