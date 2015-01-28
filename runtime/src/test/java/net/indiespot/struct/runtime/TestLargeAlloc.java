package net.indiespot.struct.runtime;

import net.indiespot.struct.GCListenerRule;
import net.indiespot.struct.Struct;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import java.util.logging.Logger;

/**
 * Created by jason on 1/24/15.
 */
@SuppressWarnings({"PointlessArithmeticExpression", "ConstantConditions"})
@Ignore("slow")
public class TestLargeAlloc {
    private static final Logger log = Logger.getLogger(TestLargeAlloc.class.getName());

    @ClassRule
    public static GCListenerRule gc = new GCListenerRule();

    @Test
    public void testLargeAlloc() {
        int count = Integer.MAX_VALUE / (Struct.sizeof(Vec3.class) - 2);
        log.info("count=" + count);
        log.info("sizeof=" + Struct.sizeof(Vec3.class));
        Vec3 base = Struct.callocArrayBase(Vec3.class, count);
        for (int i = 1; i < count; i++) {
            Vec3 v1 = Struct.index(base, Vec3.class, i - 1);
            Vec3 v2 = Struct.index(base, Vec3.class, i - 0);
            v2.mul(v1);
            v1.mul(v2);
            long p1 = Struct.getPointer(v1);
            long p2 = Struct.getPointer(v2);
            Assert.assertEquals(p2 - p1, Struct.sizeof(Vec3.class));
        }
        Struct.free(base);
    }
}
