package net.indiespot.struct.api.runtime;

public abstract class FastThreadLocal<T> {
    public static final int MAX_SUPPORTED_THREADS = 100_000;
    //TODO investigate SuspiciousArrayCast
    private final T[] threadid2value = (T[]) new Object[MAX_SUPPORTED_THREADS];

    public int size() {
        int size = 0;
        for (T aThreadid2value : threadid2value)
            if (aThreadid2value != null)
                size++;
        return size;
    }

    public interface Visitor<T> {
        void visit(int threadId, T item);
    }

    public void visit(Visitor<T> visitor) {
        for (int i = 0; i < threadid2value.length; i++) {
            T value = threadid2value[i];
            if (value != null)
                visitor.visit(i, value);
        }
    }

    public T[] copy() {
        return threadid2value.clone();
    }

    public final void set(T value) {
        final int id = (int) Thread.currentThread().getId();
        T old = threadid2value[id];
        threadid2value[id] = null;
        if (old != null)
            this.onRelease(old);
        threadid2value[id] = value;
    }

    public final T get() {
        final int id = (int) Thread.currentThread().getId();
        T value = threadid2value[id];
        if (value == null)
            threadid2value[id] = value = initialValue();
        return value;
    }

    public abstract T initialValue();

    public abstract void onRelease(T value);

    {
        ThreadMonitor.addListener(new ThreadMonitor.ThreadListener() {
            @Override
            public void onThreadStart(long threadId) {

            }

            @Override
            public void onThreadDeath(long threadId) {
                T value = threadid2value[(int) threadId];
                if (value == null)
                    return;
                threadid2value[(int) threadId] = null;
                FastThreadLocal.this.onRelease(value);
            }
        });
    }
}
