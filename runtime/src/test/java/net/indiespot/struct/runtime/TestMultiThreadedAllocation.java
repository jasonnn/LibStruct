package net.indiespot.struct.runtime;

import net.indiespot.struct.TheAgentD;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestMultiThreadedAllocation {
    @Ignore("this takes a long time & lots of cpu to run")
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        Thread[] ts = new Thread[8];
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    TheAgentD.main(new String[0]);
                }
            });
        }

        try {
            for (int i = 0; i < ts.length; i++)
                ts[i].start();
            for (int i = 0; i < ts.length; i++)
                ts[i].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
