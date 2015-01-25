package net.indiespot.struct;

import org.junit.ClassRule;
import org.junit.rules.TestRule;

/**
 * Created by jason on 1/24/15.
 */
public class TestBase {
    @ClassRule
    public static TestRule gc = new GCListenerRule();
}
