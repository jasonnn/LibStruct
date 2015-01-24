package net.indiespot.struct;

import net.indiespot.struct.runtime.StructGC;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * Created by jason on 1/24/15.
 */
public class GCListenerRule extends TestWatcher implements StructGC.GcInfo {
    @Override
    protected void starting(Description description) {
        StructGC.addListener(this);
        super.starting(description);
    }

    @Override
    protected void finished(Description description) {
        StructGC.removeListener(this);
        super.finished(description);
    }

    @Override
    public void onGC(int freedHandles, int remainingHandles, int gcHeaps, int emptyHeaps, long tookNanos) {
        System.out.println("LibStruct GC: freed=" + freedHandles / 1024 + "K, remaining=" + remainingHandles / 1024 + "K, took: " + tookNanos / 1000 / 1000 + "ms");
    }

    @Override
    public void onStress() {
        System.out.println("LibStruct GC: stress!");
    }

    @Override
    public void onPanic() {
        System.out.println("LibStruct GC: panic!");

    }
}
