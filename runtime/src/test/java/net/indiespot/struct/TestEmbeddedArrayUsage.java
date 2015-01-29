package net.indiespot.struct;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
@Ignore("nothing tested")
public class TestEmbeddedArrayUsage extends AbstractRuntimeTest {
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        // ArrayEmbed ae = new ArrayEmbed();
        // paramValue(ae.iarr); // this will fail
    }

    public static void paramValue(int[] arr) {
        // ok
    }

    public static int[] returnValue() {
        // ArrayEmbed ae = new ArrayEmbed();
        // return ae.iarr; // this will fail
        return null;
    }

    public static void reassign() {
        // ArrayEmbed ae = new ArrayEmbed();
        // ae.iarr = new int[4]; // this will fail
    }
}
