package net.indiespot.struct;

import net.indiespot.struct.api.runtime.ThreadMonitor;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class ThreadMonitorTest {
    @Rule
    public TestRule timeout = new Timeout(10000);

    @Ignore("not really a test")
    @Test
    public void testUsage() throws Exception {
        ThreadMonitor.setPollInterval(100);

        final CountDownLatch latch = new CountDownLatch(100);

        ThreadMonitor.addListener(new ThreadMonitor.ThreadListener() {
            @Override
            public void onThreadStart(long threadId) {
                System.out.println("onThreadStart:" + threadId);
            }

            @Override
            public void onThreadDeath(long threadId) {
                latch.countDown();
                System.out.println("onThreadDeath:" + threadId);
            }
        });

        Random rndm = new Random();
        for(int i = 0; i < 100; i++) {
            spawnWorker(rndm.nextInt(1000));
        }

        latch.await();

    }



	private static void spawnWorker(final long delay) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(delay);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
