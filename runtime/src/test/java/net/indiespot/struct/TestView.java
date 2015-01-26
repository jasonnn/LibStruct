package net.indiespot.struct;

import net.indiespot.struct.testlib.PosVelRef;
import net.indiespot.struct.testlib.PosVelView;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestView {

    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        PosVelRef ref = new PosVelRef();
        ref.pos = new Vec3();
        ref.vel = new Vec3();
        ref.pos.set(13, 14, 15);
        ref.vel.set(16, 17, 18);

        PosVelView embed = new PosVelView();
        embed.pos().set(19, 20, 21);
        embed.vel().set(22, 23, 24);
    }
}
