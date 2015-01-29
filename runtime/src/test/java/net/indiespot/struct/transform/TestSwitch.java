package net.indiespot.struct.transform;

import net.indiespot.struct.AbstractRuntimeTest;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestSwitch extends AbstractRuntimeTest {


    @Test
    public void testTryCatch() {
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

    @Test
    public void testTryCatchFinally() {
        Vec3 a = new Vec3();

        try {
            a.x = 13;
        } catch (Throwable t) {

        } finally {
            a.y = 14;
        }

        try {
            a.x = 13;
        } catch (Throwable t) {
            System.out.println(t);
        } finally {
            a.y = 14;
        }

        try {
            a.x = 13;
        } catch (Throwable t) {
            throw t;
        } finally {
            a.y = 14;
        }

        try {
            a.x = 13;
        } catch (Throwable t) {
            System.out.println(t);
            throw new IllegalStateException("doh!");
        } finally {
            a.y = 14;
        }
    }
}
