package net.indiespot.struct.transform;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestStructAsObjectParam {
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        Vec3 vec = new Vec3();

        {
            test(null);
            test(vec);

            test(null, null);
            test(null, vec);
            test(vec, null);
            test(vec, vec);

            test(null, null, null);
            test(null, null, vec);
            test(null, vec, null);
            test(null, vec, vec);
            test(vec, null, null);
            test(vec, null, vec);
            test(vec, vec, null);
            test(vec, vec, vec);
        }

        {
            test("v");
            test(vec);

            test("v", "v");
            test("v", vec);
            test(vec, "v");
            test(vec, vec);

            test("v", "v", "v");
            test("v", "v", vec);
            test("v", vec, "v");
            test("v", vec, vec);
            test(vec, "v", "v");
            test(vec, "v", vec);
            test(vec, vec, "v");
            test(vec, vec, vec);
        }

        {
            Vec3 vc3 = new Vec3();
            test(vc3, vec, vec);
            test(vc3, vec, vc3);
            test(vec, vec, vc3);

            vc3 = null;
            test(vc3, vec, vec);
            test(vc3, vec, vc3);
            test(vec, vec, vc3);
        }

        {
            // stringify support for last 3 struct params
            test("v", "v", "v", "v");
            test("v", "v", "v", vec);
            test("v", "v", vec, "v");
            test("v", vec, "v", "v");
            // test(vec, "v", "v", "v"); // will bark!

            // deterministically null-structs are not stringified
            vec = null;
            test("v", "v", "v", "v");
            test("v", "v", "v", vec);
            test("v", "v", vec, "v");
            test("v", vec, "v", "v");
        }

    }

    private static void test(Object a) {
        //
    }

    private static void test(Object a, Object b) {
        //
    }

    private static void test(Object a, Object b, Object c) {
        //
    }

    private static void test(Object a, Object b, Object c, Object d) {
        //
    }
}
