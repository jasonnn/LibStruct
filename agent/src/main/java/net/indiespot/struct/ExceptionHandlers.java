package net.indiespot.struct;

/**
 * Created by jason on 1/28/15.
 */
public enum ExceptionHandlers implements Thread.UncaughtExceptionHandler {

    PRINT_STACK_TRACE {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            e.printStackTrace(System.err);

        }
    },
    RETHROW {
        @Override
        public void uncaughtException(Thread t, Throwable e) {

            if (e instanceof RuntimeException) throw ((RuntimeException) e);
            else throw new RuntimeException(e);

        }
    }
}
