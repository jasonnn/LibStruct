package net.indiespot.struct;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestSwitch {
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        new Vec3();

        switch (4) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }

        switch (4) {
            case 1003:
                break;
            case 2003:
                break;
            case 3003:
                break;
        }
    }
}
