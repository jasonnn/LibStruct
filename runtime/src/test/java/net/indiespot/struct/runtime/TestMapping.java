package net.indiespot.struct.runtime;

import net.indiespot.struct.Struct;
import net.indiespot.struct.api.runtime.StructMemory;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Created by jason on 1/25/15.
 */
public class TestMapping {

    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        int alignMargin = 4 - 1;
        int sizeof = 3 << 2;
        int count = 10;
        ByteBuffer bb = ByteBuffer.allocateDirect(count * sizeof + alignMargin);
        StructMemory.alignBufferToWord(bb);
        Vec3[] mapped = Struct.map(Vec3.class, bb);
        long p1 = Struct.getPointer(mapped[0]);
        long p2 = Struct.getPointer(mapped[1]);
        if (p2 - p1 != 12)
            throw new IllegalStateException();
    }
}
