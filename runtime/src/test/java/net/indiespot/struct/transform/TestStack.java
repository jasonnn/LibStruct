package net.indiespot.struct.transform;

import net.indiespot.struct.AbstractRuntimeTest;
import net.indiespot.struct.api.annotations.CopyStruct;
import net.indiespot.struct.api.annotations.TakeStruct;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestStack extends AbstractRuntimeTest {


    @Test
    public void testCopyTakeSemantics() {
        Vec3 vec = new Vec3();
        vec.x = 1;

        vec = self(vec);
        vec.x = 3;
        if (vec.y != 13)
            throw new IllegalStateException();

        vec = copy();
        vec.x = 4;
        if (vec.y != 14)
            throw new IllegalStateException();

        vec = pass();
        vec.x = 5; // must crash, as the struct is on the part of the stack
        // that was popped
        if (vec.y != 15)
            throw new IllegalStateException();
    }

    @TakeStruct
    private static Vec3 self(Vec3 v) {
        v.y = 13;
        return v;
    }

    @CopyStruct
    private static Vec3 copy() {
        Vec3 v = new Vec3();
        v.y = 14;
        return v;
    }

    @TakeStruct
    private static Vec3 pass() {
        Vec3 v = new Vec3();
        v.y = 15;
        return v;
    }
}
