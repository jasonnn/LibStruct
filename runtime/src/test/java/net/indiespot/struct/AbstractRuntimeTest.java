package net.indiespot.struct;

import org.junit.ClassRule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * Created by jason on 1/24/15.
 */
public class AbstractRuntimeTest {


    @ClassRule
    public static TestRule gc = new GCListenerRule();
    @ClassRule
    public static TestRule exceptionSetup = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            StructAgentDelegate.setExceptionHandler(ExceptionHandlers.PRINT_STACK_TRACE);
            super.starting(description);
        }
    };
}
