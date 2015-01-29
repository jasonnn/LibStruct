package net.indiespot.struct;

import net.indiespot.struct.testlib.NormalVec3;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

/**
 * Created by jason on 1/25/15.
 */
@Ignore("benchmark")
public class TestPerformance extends AbstractRuntimeTest {
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {

        Random rndm = new Random(12153);
        NormalVec3 nv = new NormalVec3();
        Vec3 sv = new Vec3();

        NormalVec3[] arr2 = new NormalVec3[1024];
        Vec3[] arr3 = new Vec3[1024];
        Vec3[] arr4 = new Vec3[1024];

        for (int k = 0; k < 16; k++) {
            System.out.println();
            float p = 0;
            sv.x = nv.x = rndm.nextFloat();
            sv.y = nv.y = rndm.nextFloat();
            sv.z = nv.z = rndm.nextFloat();

            long[] tA = new long[32];
            long[] tB = new long[32];

            // ---

            for (int i = 0; i < tA.length; i++) {
                long t0 = System.nanoTime();
                benchInstanceNew(arr2);
                long t1 = System.nanoTime();
                tA[i] = t1 - t0;
            }

            for (int i = 0; i < tB.length; i++) {
                long t0 = System.nanoTime();
                benchStructNew(arr3);
                long t1 = System.nanoTime();
                tB[i] = t1 - t0;
            }

            System.out.println("instance creation:       " + tA[tA.length / 2] / 1000 + "us [" + p + "]");
            System.out.println("struct creation:         " + tB[tB.length / 2] / 1000 + "us [" + p + "]");

            // ---

            for (int i = 0; i < tA.length; i++) {
                long t0 = System.nanoTime();
                p += benchInstanceAccess(nv);
                long t1 = System.nanoTime();
                tA[i] = t1 - t0;
            }

            for (int i = 0; i < tB.length; i++) {
                long t0 = System.nanoTime();
                p += benchStructAccess(sv);
                long t1 = System.nanoTime();
                tB[i] = t1 - t0;
            }

            System.out.println("instances access:        " + tA[tA.length / 2] / 1000 + "us [" + p + "]");
            System.out.println("struct access:           " + tB[tB.length / 2] / 1000 + "us [" + p + "]");

            // ---

            for (int i = 0; i < tA.length; i++) {
                long t0 = System.nanoTime();
                p += benchInstanceArray(arr2);
                long t1 = System.nanoTime();
                tA[i] = t1 - t0;
            }

            for (int i = 0; i < tB.length; i++) {
                long t0 = System.nanoTime();
                p += benchStructArray(arr4);
                long t1 = System.nanoTime();
                tB[i] = t1 - t0;
            }

            System.out.println("instance array access:   " + tA[tA.length / 2] / 1000 + "us [" + p + "]");
            System.out.println("struct array access:     " + tB[tB.length / 2] / 1000 + "us [" + p + "]");
        }
    }

    private static void benchInstanceNew(NormalVec3[] arr) {
        for (int i = 0; i < 128; i++) {
            benchInstanceNew2(arr);
        }
    }

    private static void benchInstanceNew2(NormalVec3[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new NormalVec3(1, 2, 3);
        }
    }

    private static void benchStructNew(Vec3[] arr) {
        for (int i = 0; i < 128; i++) {
            benchStructNew2(arr);
        }
    }

    private static void benchStructNew2(Vec3[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Vec3(1, 2, 3);
        }
    }

    private static float benchInstanceAccess(NormalVec3 nv) {
        float p = 0;
        for (int i = 0; i < 1024 * 1024; i++) {
            p += nv.x * nv.y + nv.z;
            p *= nv.y * nv.z + nv.x;
            p -= nv.z * nv.x + nv.y;
        }
        return p;
    }

    private static float benchStructAccess(Vec3 nv) {
        float p = 0;
        for (int i = 0; i < 1024 * 1024; i++) {
            p += nv.x * nv.y + nv.z;
            p *= nv.y * nv.z + nv.x;
            p -= nv.z * nv.x + nv.y;
        }
        return p;
    }

    private static float benchInstanceArray(NormalVec3[] arr) {
        float p = 0;
        for (int k = 0; k < 64; k++) {
            for (int i = 0, len = arr.length - 2; i < len; i++) {
                p += arr[i + 0].x * arr[i + 0].y + arr[i + 0].z;
                p *= arr[i + 1].y * arr[i + 1].z + arr[i + 1].x;
                p -= arr[i + 2].z * arr[i + 2].x + arr[i + 2].y;
            }
        }
        return p;
    }

    private static float benchStructArray(Vec3[] arr) {
        float p = 0;
        for (int k = 0; k < 64; k++) {
            for (int i = 0, len = arr.length - 2; i < len; i++) {
                p += arr[i + 0].x * arr[i + 0].y + arr[i + 0].z;
                p *= arr[i + 1].y * arr[i + 1].z + arr[i + 1].x;
                p -= arr[i + 2].z * arr[i + 2].x + arr[i + 2].y;
            }
        }
        return p;
    }
}
