package net.indiespot.struct;

public class StructTest extends AbstractRuntimeTest {


    //TODO
//	public static class TestCustomStack {
//		public static void test() {
//			StructAllocationStack stack = Struct.createStructAllocationStack(1024);
//
//			stack.save();
//
//			Vec3 v1 = Struct.stackAlloc(stack, Vec3.class);
//			Vec3 v2 = Struct.stackAlloc(stack, Vec3.class);
//
//			stack.restore();
//
//			Vec3 v1b = Struct.stackAlloc(stack, Vec3.class);
//			Vec3 v2b = Struct.stackAlloc(stack, Vec3.class);
//
//			assert (v1 == v1b);
//			assert (v2 == v2b);
//
//			Struct.discardStructAllocationStack(stack);
//		}
//	}

//	public static class TestAllocPerformance {
//		public static void test() {
//			final int allocCount = 10_000_000;
//
//			StructAllocationStack sas = Struct.createStructAllocationStack(allocCount * Struct.sizeof(Vec3.class) + 100);
//
//			for (int k = 0; k < 10; k++) {
//				long tm2 = System.nanoTime();
//				for (int i = 0; i < allocCount; i++)
//					instance();
//				long tm1 = System.nanoTime();
//				sas.save();
//				for (int i = 0; i < allocCount; i++)
//					stackAlloc(sas);
//				sas.restore();
//				long t0 = System.nanoTime();
//				for (int i = 0; i < allocCount; i++)
//					stackAlloc1();
//				long t1 = System.nanoTime();
//				for (int i = 0; i < allocCount; i += 100)
//					stackAlloc1N(100);
//				long t2 = System.nanoTime();
//				for (int i = 0; i < allocCount; i += 100)
//					stackAlloc10N(10);
//				long t3 = System.nanoTime();
//				for (int i = 0; i < allocCount; i += 100)
//					stackAllocArray(100);
//				long t4 = System.nanoTime();
//				for (int i = 0; i < allocCount; i++)
//					memoryAlloc();
//				long t5 = System.nanoTime();
//				for (int i = 0; i < allocCount; i += 100)
//					memoryAllocArray(100);
//				long t6 = System.nanoTime();
//				for (int i = 0; i < allocCount; i += 100)
//					memoryAllocArrayBulkFree(100);
//				long t7 = System.nanoTime();
//
//				long tInstance1 = (tm1 - tm2) / 1000L;
//				long tStackAllocS = (t0 - tm1) / 1000L;
//				long tStackAlloc1 = (t1 - t0) / 1000L;
//				long tStackAlloc1N = (t2 - t1) / 1000L;
//				long tStackAlloc10N = (t3 - t2) / 1000L;
//				long tStackAllocArr = (t4 - t3) / 1000L;
//				long tMemoryAllocAndFree = (t5 - t4) / 1000L;
//				long tMemoryAllocAndFreeArr = (t6 - t5) / 1000L;
//				long tMemoryAllocAndFree2Arr = (t7 - t6) / 1000L;
//
//				System.out.println();
//				System.out.println("tInstance1      \t" + tInstance1 / 1000 + "ms \t" + (int) (allocCount / (double) tInstance1) + "M/s");
//				System.out.println("tStackAllocS    \t" + tStackAllocS / 1000 + "ms \t" + (int) (allocCount / (double) tStackAllocS) + "M/s");
//				System.out.println("tStackAlloc1    \t" + tStackAlloc1 / 1000 + "ms \t" + (int) (allocCount / (double) tStackAlloc1) + "M/s");
//				System.out.println("tStackAlloc1N   \t" + tStackAlloc1N / 1000 + "ms   \t" + (int) (allocCount / (double) tStackAlloc1N) + "M/s");
//				System.out.println("tStackAlloc10N  \t" + tStackAlloc10N / 1000 + "ms  \t" + (int) (allocCount / (double) tStackAlloc10N) + "M/s");
//				System.out.println("tStackAllocArr  \t" + tStackAllocArr / 1000 + "ms  \t" + (int) (allocCount / (double) tStackAllocArr) + "M/s");
//				System.out.println("tMemoryAllocFree     \t" + tMemoryAllocAndFree / 1000 + "ms    \t" + (int) (allocCount / (double) tMemoryAllocAndFree) + "M/s");
//				System.out.println("tMemoryAllocFreeArr  \t" + tMemoryAllocAndFreeArr / 1000 + "ms \t" + (int) (allocCount / (double) tMemoryAllocAndFreeArr) + "M/s");
//				System.out.println("tMemoryAllocFreeArr2 \t" + tMemoryAllocAndFree2Arr / 1000 + "ms \t" + (int) (allocCount / (double) tMemoryAllocAndFree2Arr) + "M/s");
//			}
//
//			Struct.discardStructAllocationStack(sas);
//		}
//
//		private static void instance() {
//			new NormalVec3(); // HotSpot might remove this
//		}
//
//		private static void stackAlloc(StructAllocationStack stack) {
//			Struct.stackAlloc(stack, Vec3.class).set(0.0f, 0.0f, 0.0f);
//		}
//
//		@SuppressWarnings("unused")
//		private static void stackAlloc1() {
//			Vec3 vec = new Vec3(); // HotSpot will _not_ remove this
//		}
//
//		@SuppressWarnings("unused")
//		private static void stackAlloc1N(int n) {
//			for (int i = 0; i < n; i++) {
//				Vec3 vec = new Vec3(); // HotSpot will _not_ remove this
//			}
//		}
//
//		private static void stackAlloc10N(int n) {
//			for (int i = 0; i < n; i++) {
//				new Vec3(); // HotSpot will _not_ remove this
//				new Vec3();
//				new Vec3();
//				new Vec3();
//				new Vec3();
//				new Vec3();
//				new Vec3();
//				new Vec3();
//				new Vec3();
//				new Vec3();
//			}
//		}
//
//		@SuppressWarnings("unused")
//		private static void stackAllocArray(int n) {
//			Vec3[] arr = new Vec3[n]; // HotSpot will _not_ remove this
//		}
//
//		private static void memoryAlloc() {
//			Struct.free(Struct.malloc(Vec3.class));
//		}
//
//		private static void memoryAllocArray(int n) {
//			for (Vec3 vec : Struct.mallocArray(Vec3.class, n))
//				Struct.free(vec);
//		}
//
//		private static void memoryAllocArrayBulkFree(int n) {
//			Struct.free(Struct.mallocArray(Vec3.class, n));
//		}
//	}


    // ----------------

    // ----------------


}
