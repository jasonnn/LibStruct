package net.indiespot.struct;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
@SuppressWarnings("MismatchedReadAndWriteOfArray")
public class TestIndex extends AbstractRuntimeTest {

    @Test
    public void test() {
        Vec3[] arr = new Vec3[123];

        arr[0].set(1.20f, 2.30f, 3.40f);
        arr[1].set(1.02f, 2.03f, 3.04f);

        Assert.assertEquals(arr[0], Struct.index(arr[1], Vec3.class, -1));
        Assert.assertEquals(arr[2], Struct.index(arr[1], Vec3.class, +1));

        /*
         * Vec3 base = arr[0]; for(int m = 0; m < 256; m++) { long t0 =
         * System.nanoTime(); float siblingSum = 0.0f; for(int k = 0; k < 1024;
         * k++) { for(int i = 0; i < 123; i++) { Vec3 at = Struct.sibling(base,
         * Vec3.class, i); siblingSum += at.x; } } long t1 = System.nanoTime();
         * float arrayElemSum = 0.0f; for(int k = 0; k < 1024; k++) { for(int i
         * = 0; i < 123; i++) { Vec3 at = arr[i]; arrayElemSum += at.x; } }
         * long t2 = System.nanoTime();
         *
         * System.out.println("siblingSum=" + siblingSum + " (took: " + (t1 -
         * t0) / 1000 + "us)"); System.out.println("arrayElemSum=" +
         * arrayElemSum + " (took: " + (t2 - t1) / 1000 + "us)");
         * System.out.println(); }
         */
    }
}
