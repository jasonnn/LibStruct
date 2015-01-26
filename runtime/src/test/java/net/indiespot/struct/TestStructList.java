package net.indiespot.struct;

import net.indiespot.struct.testlib.Vec3;
import org.junit.Test;

/**
 * Created by jason on 1/25/15.
 */
public class TestStructList {
    @Test
    public void runTest() throws Exception {
        test();
    }

    public static void test() {
        VecList list = new VecList(10);
        for (int i = 0; i < 100; i++)
            list.add(new Vec3());
    }

    public static class VecList {
        private Vec3[] arr;
        private int size, cap;

        public VecList() {
            this(10);
        }

        public VecList(int cap) {
            this.cap = cap;
            arr = Struct.nullArray(Vec3.class, cap);
            size = 0;
        }

        public void add(Vec3 vec) {
            if (size == cap)
                this.expand(-1);
            arr[size++] = vec;
        }

        public void expand(int minSize) {
            Vec3[] arr2 = Struct.nullArray(Vec3.class, Math.max(minSize, cap * 2));
            for (int i = 0; i < size; i++)
                arr2[i] = arr[i];
            arr = arr2;
            cap = arr.length;
        }

        public void free() {
            for (int i = 0; i < size; i++)
                Struct.free(arr[i]);
        }
    }
}
