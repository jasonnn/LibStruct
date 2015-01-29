package net.indiespot.struct.runtime;

import net.indiespot.struct.AbstractRuntimeTest;
import net.indiespot.struct.Struct;
import net.indiespot.struct.api.annotations.TakeStruct;
import net.indiespot.struct.api.runtime.StructGC;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestMalloc extends AbstractRuntimeTest {
    @Test
    public static void test() {
        for (int i = 0; i < 4; i++) {
            Vec3 vec1 = Struct.malloc(Vec3.class);
            Vec3 vec2 = Struct.malloc(Vec3.class);

            Struct.free(vec1);
            Struct.free(vec2);
        }

        Vec3[] vecs = Struct.mallocArray(Vec3.class, 7);
        for (Vec3 vec : vecs) {
            Struct.free(vec);
        }

        vecs = Struct.mallocArray(Vec3.class, 100_000);
        for (Vec3 vec : vecs) {
            Struct.free(vec);
        }

        Struct.free(Struct.mallocArray(Vec3.class, 100_000));
    }

    private static class Vec3BlockingQueue {
        private Vec3[] queue;
        private int size;

        public Vec3BlockingQueue(int cap) {
            queue = Struct.nullArray(Vec3.class, cap);
        }

        public synchronized void push(Vec3 vec) {
            while (size == queue.length) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    // ignore
                }
            }
            queue[size++] = vec;
            this.notify();
            // System.out.println("pushed");
        }

        @TakeStruct
        public synchronized Vec3 pop() {
            while (size == 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    // ignore
                }
            }
            Vec3 vec = queue[--size];
            this.notify();
            // System.out.println("popped");
            return vec;
        }

        @TakeStruct
        public synchronized Vec3 poll(long timeout) {
            final long started = System.currentTimeMillis();
            while (size == 0) {
                try {
                    this.wait(timeout);
                } catch (InterruptedException e) {
                    // ignore
                }

                if (size == 0) {
                    if (System.currentTimeMillis() - started > timeout) {
                        return Struct.nullStruct(Vec3.class);
                    }
                }
            }
            Vec3 vec = queue[--size];
            this.notify();
            // System.out.println("polled: " + vec);
            return vec;
        }
    }

    @Test
    public void testBlockingQueueProducerConsumer() {

        int start = StructGC.getHandleCount();
        if (start != 0)
            throw new IllegalStateException("StructGC.getHandleCount=" + start);

        final int itemCountPerProducer = 1_000_000;
        final long pollTimeout = 5_000;
        final int queueCount = 4;
        for (int q = 0; q < queueCount; q++) {
            Vec3BlockingQueue queue = new Vec3BlockingQueue(100_000);

            for (int i = 0; i < 8; i++)
                createProducer(queue, itemCountPerProducer);

            for (int i = 0; i < 32; i++)
                createConsumer(queue, pollTimeout);
        }

        StructGC.discardThreadLocal();

        StructGC.addListener(new StructGC.GCListener() {
            @Override
            public void onGC(int freedHandles, int remainingHandles, int gcHeaps, int emptyHeaps, long tookNanos) {
                System.out.println("StructGC: handles freed: " + (freedHandles / 1024) + "K/" + (freedHandles + remainingHandles) / 1024 + "K, empty heaps: " + emptyHeaps + "/" + (emptyHeaps + gcHeaps) + ", collection took: " + (tookNanos / 1_000) + "us");
            }

            @Override
            public void onStress() {

            }

            @Override
            public void onPanic() {

            }
        });

        try {
            Thread.sleep(pollTimeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (StructGC.getHandleCount() > start);
    }

    private static void createProducer(final Vec3BlockingQueue queue, final int items) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < items; i++) {
                    queue.push(Struct.malloc(Vec3.class));
                }

                StructGC.discardThreadLocal();
            }
        }).start();
    }

    private static void createConsumer(final Vec3BlockingQueue queue, final long pollTimeout) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Vec3 item = queue.poll(pollTimeout);
                    if (item == null)
                        break;

                    Struct.free(item);
                }
            }
        }).start();
    }
}
