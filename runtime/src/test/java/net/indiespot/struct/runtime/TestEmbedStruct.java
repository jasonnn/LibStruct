package net.indiespot.struct.runtime;

import net.indiespot.struct.Struct;
import net.indiespot.struct.testlib.StructEmbed;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestEmbedStruct {
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        StructEmbed em1 = new StructEmbed();
        StructEmbed em2 = new StructEmbed();
        assert (Struct.getPointer(em2) - Struct.getPointer(em1)) == Struct.sizeof(StructEmbed.class);

        assert (Struct.getPointer(em1) - Struct.getPointer(em1.pos)) == 0;
        assert (Struct.getPointer(em1.vel) - Struct.getPointer(em1.pos)) == Struct.sizeof(Vec3.class);

        assert (Struct.getPointer(em2) - Struct.getPointer(em2.pos)) == 0;
        assert (Struct.getPointer(em2.vel) - Struct.getPointer(em2.pos)) == Struct.sizeof(Vec3.class);
    }
}
