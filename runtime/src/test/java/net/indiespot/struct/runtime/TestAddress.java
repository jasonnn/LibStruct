package net.indiespot.struct.runtime;

import net.indiespot.struct.AbstractRuntimeTest;
import net.indiespot.struct.Struct;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestAddress extends AbstractRuntimeTest {

    @Test
    public void test() {
        Vec3 vec = new Vec3();
        Object obj = new Object();
        //TODO not really a test
        System.out.println("addr=" + Struct.getPointer(vec));
        System.out.println("addr=" + Struct.getPointer(obj));
    }
}
