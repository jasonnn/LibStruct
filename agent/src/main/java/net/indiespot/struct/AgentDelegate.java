package net.indiespot.struct;

import java.lang.instrument.Instrumentation;

/**
 * Created by jason on 1/24/15.
 */
public interface AgentDelegate {
    void premain(String args, Instrumentation instrumentation);
}
