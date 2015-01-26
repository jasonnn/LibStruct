package net.indiespot.struct;

import net.indiespot.struct.api.runtime.StructMemory;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Created by jason on 1/25/15.
 */
public class TestInterleavedMapping {
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
        Vec3[] mapped1 = Struct.map(Vec3.class, bb, 24, 0);
        Vec3[] mapped2 = Struct.map(Vec3.class, bb, 24, 12);
        {
            long p1 = Struct.getPointer(mapped1[0]);
            long p2 = Struct.getPointer(mapped1[1]);
            if (p2 - p1 != 24)
                throw new IllegalStateException();
        }
        {
            long p1 = Struct.getPointer(mapped2[0]);
            long p2 = Struct.getPointer(mapped2[1]);
            if (p2 - p1 != 24)
                throw new IllegalStateException();
        }
        {
            long p1 = Struct.getPointer(mapped1[0]);
            long p2 = Struct.getPointer(mapped2[0]);
            if (p2 - p1 != 12)
                throw new IllegalStateException();
        }
    }
}
