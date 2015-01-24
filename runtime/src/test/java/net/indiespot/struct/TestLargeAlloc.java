package net.indiespot.struct;

import net.indiespot.struct.api.Struct;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.logging.Logger;

/**
 * Created by jason on 1/24/15.
 */
public class TestLargeAlloc {
    private static final Logger log = Logger.getLogger(TestLargeAlloc.class.getName());

    @ClassRule
    public static GCListenerRule gc = new GCListenerRule();

    @Test
    public void testLargeAlloc() {
        int count = Integer.MAX_VALUE / (Struct.sizeof(StructTest.Vec3.class) - 2);
        log.info("count=" + count);
        log.info("sizeof=" + Struct.sizeof(StructTest.Vec3.class));
        StructTest.Vec3 base = Struct.callocArrayBase(StructTest.Vec3.class, count);
        for (int i = 1; i < count; i++) {
            StructTest.Vec3 v1 = Struct.index(base, StructTest.Vec3.class, i - 1);
            StructTest.Vec3 v2 = Struct.index(base, StructTest.Vec3.class, i - 0);
            v2.mul(v1);
            v1.mul(v2);
            long p1 = Struct.getPointer(v1);
            long p2 = Struct.getPointer(v2);
            assert (p2 - p1) == Struct.sizeof(StructTest.Vec3.class);
        }
        Struct.free(base);
    }
}
