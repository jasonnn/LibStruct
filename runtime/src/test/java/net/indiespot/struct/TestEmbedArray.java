package net.indiespot.struct;

import net.indiespot.struct.testlib.ArrayEmbed;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestEmbedArray {
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        ArrayEmbed ae = new ArrayEmbed();

        // test float[]
        {
            float[] farr = ae.farr;

            assert farr[0] == 0.0f;
            assert farr[1] == 0.0f;

            farr[0] = 1.2f;
            assert farr[0] == 1.2f;

            farr[1] = 3.4f;
            assert farr[0] == 1.2f;
            assert farr[1] == 3.4f;
        }

        // test int[]
        {
            int[] iarr = ae.iarr;

            assert iarr[0] == 0;
            assert iarr[1] == 0;

            iarr[0] = 0;
            assert iarr[0] == 0;

            iarr[1] = 3;
            assert iarr[0] == 0;
            assert iarr[1] == 3;

            iarr[2] = 5;
            assert iarr[0] == 0;
            assert iarr[1] == 3;
            assert iarr[2] == 5;
        }

        // test double[]
        {
            double[] darr = ae.darr;

            assert darr[0] == 0.0;
            assert darr[1] == 0.0;

            darr[0] = 1.2;
            assert darr[0] == 1.2;

            darr[1] = 3.4;
            assert darr[0] == 1.2;
            assert darr[1] == 3.4;
        }
    }
}
