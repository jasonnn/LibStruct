package net.indiespot.struct;

import net.indiespot.struct.api.runtime.StructGC;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.logging.Logger;

/**
 * Created by jason on 1/24/15.
 */
public class GCListenerRule extends TestWatcher implements StructGC.GCListener {
    private static final Logger log = Logger.getLogger(GCListenerRule.class.getName());

    @Override
    protected void starting(Description description) {
        StructGC.addListener(this);
        super.starting(description);
    }

    @Override
    protected void finished(Description description) {
        StructGC.removeListener(this);
        super.finished(description);
        log.info("awaiting gc...");
        while (StructGC.getHandleCount() != 0) {
            Thread.yield();
        }

        log.info("done");
    }

    @Override
    public void onGC(int freedHandles, int remainingHandles, int gcHeaps, int emptyHeaps, long tookNanos) {
        log.info("LibStruct GC: freed=" + freedHandles / 1024 + "K, remaining=" + remainingHandles / 1024 + "K, took: " + tookNanos / 1000 / 1000 + "ms");
    }

    @Override
    public void onStress() {
        log.info("LibStruct GC: stress!");
    }

    @Override
    public void onPanic() {
        log.info("LibStruct GC: panic!");

    }
}
