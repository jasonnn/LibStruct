package net.indiespot.struct;

import net.indiespot.struct.testlib.ArrayEmbed;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
@SuppressWarnings("ConstantConditions")
public class TestEmbedArray extends AbstractRuntimeTest {

    @Test
    public void testFloat() {
        ArrayEmbed ae = new ArrayEmbed();


        float[] farr = ae.farr;

        assert farr[0] == 0.0f;
        assert farr[1] == 0.0f;

        farr[0] = 1.2f;
        assert farr[0] == 1.2f;

        farr[1] = 3.4f;
        assert farr[0] == 1.2f;
        assert farr[1] == 3.4f;
    }

    @Test
    public void testInt() {
        ArrayEmbed ae = new ArrayEmbed();

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

    @Test
    public void testDouble() {
        ArrayEmbed ae = new ArrayEmbed();

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
