package net.indiespot.struct;

import net.indiespot.struct.api.runtime.StructUnsafe;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

//import org.openjdk.jol.info.ClassLayout;
//import org.openjdk.jol.util.VMSupport;

/**
 * Created by jason on 1/25/15.
 */
@SuppressWarnings("PointlessArithmeticExpression")
public class TestFromPointer {

//    @Test
//    public void testJOL() throws Exception {
//        System.out.println(VMSupport.vmDetails());
//        System.out.println(ClassLayout.parseClass(Vec3.class).toPrintable());
//
//    }

    @Test
    public void testFromPointer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(234);

        long addr = StructUnsafe.getBufferBaseAddress(bb);

        long vec3Size = Struct.sizeof(Vec3.class);

        long v1Ptr = (addr + 0 * vec3Size);
        long v2Ptr = (addr + 1 * vec3Size);

        Vec3 v1 = Struct.fromPointer(v1Ptr);
        Vec3 v2 = Struct.fromPointer(v2Ptr);


        Assert.assertEquals(v1Ptr, Struct.getPointer(v1));
        Assert.assertEquals(v2Ptr, Struct.getPointer(v2));

        Vec3[] vs = Struct.fromPointer(addr, Vec3.class, 2);

        Assert.assertEquals(vs[0], v1);
        Assert.assertEquals(vs[1], v2);

        vs = Struct.fromPointer(addr, 12, 2);
        Assert.assertEquals(v1, vs[0]);
        Assert.assertEquals(v2, vs[1]);

        bb.clear(); // prevent untimely GC
    }
}

/*
StructEnv.rewrite TRANSFORM [net/indiespot/struct/TestFromPointer]:
	method1: <init> ()V
	method2: <init> ()V
		early out for rewrite? [true]
	method1: testFromPointer ()V
	method2: testFromPointer ()V
		early out for rewrite? [false]
			local.set(0, REFERENCE)
		_LABEL <= L416378129
			saving label state [0] <= L416378129
		SIPUSH 234
			stack.push(INT) -> [INT]
		INVOKESTATIC java/nio/ByteBuffer allocateDirect (I)Ljava/nio/ByteBuffer;
			stack.peek(INT) -> [INT]
			stack.peek(INT) -> [INT]
			stack.peek(INT) -> [INT]
			stack.peek(INT) -> [INT]
			stack.pop(INT) -> []
			stack.push(REFERENCE) -> [REFERENCE]
		ASTORE 1
			stack.peek(REFERENCE) -> [REFERENCE]
			stack.pop(REFERENCE) -> []
			local.set(1, REFERENCE)
		_LABEL <= L632172652
			saving label state [1] <= L632172652
		ALOAD 1
			stack.push(REFERENCE) -> [REFERENCE]
		INVOKESTATIC net/indiespot/struct/runtime/StructUnsafe getBufferBaseAddress (Ljava/nio/ByteBuffer;)J
			stack.peek(REFERENCE) -> [REFERENCE]
			stack.peek(REFERENCE) -> [REFERENCE]
			stack.peek(REFERENCE) -> [REFERENCE]
			stack.peek(REFERENCE) -> [REFERENCE]
			stack.pop(REFERENCE) -> []
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
		LSTORE 2
			stack.peek(MISC) -> [MISC,MISC]
			stack.pop(MISC) -> [MISC]
			stack.peek(MISC) -> [MISC]
			stack.pop(MISC) -> []
			local.set(2, MISC)
			local.set(3, MISC)
		_LABEL <= L1324160455
			saving label state [2] <= L1324160455
		SIPUSH 12
			stack.push(INT) -> [INT]
			stack.peek(INT) -> [INT]
			stack.pop(INT) -> []
			stack.push(STRUCT_TYPE) -> [STRUCT_TYPE]
			stack.peek(STRUCT_TYPE) -> [STRUCT_TYPE]
			stack.set(INT, INT)
	1)	I2L
			stack.peek(INT) -> [INT]
			stack.pop(INT) -> []
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
	2)	I2L
		LSTORE 4
			stack.peek(MISC) -> [MISC,MISC]
			stack.pop(MISC) -> [MISC]
			stack.peek(MISC) -> [MISC]
			stack.pop(MISC) -> []
			local.set(4, MISC)
			local.set(5, MISC)
		_LABEL <= L791733324
			saving label state [3] <= L791733324
		LLOAD 2
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
	1)	LCONST_0
			stack.push(MISC) -> [MISC,MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC,MISC]
	2)	LCONST_0
		LLOAD 4
			stack.push(MISC) -> [MISC,MISC,MISC,MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC,MISC,MISC,MISC]
	1)	LMUL
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC,MISC,MISC]
			stack.pop(MISC) -> [MISC,MISC,MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC,MISC]
			stack.pop(MISC) -> [MISC,MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.pop(MISC) -> [MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC]
			stack.pop(MISC) -> [MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC,MISC]
	2)	LMUL
	1)	LADD
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.pop(MISC) -> [MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC]
			stack.pop(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.pop(MISC) -> [MISC]
			stack.peek(MISC) -> [MISC]
			stack.pop(MISC) -> []
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
	2)	LADD
		LSTORE 6
			stack.peek(MISC) -> [MISC,MISC]
			stack.pop(MISC) -> [MISC]
			stack.peek(MISC) -> [MISC]
			stack.pop(MISC) -> []
			local.set(6, MISC)
			local.set(7, MISC)
		_LABEL <= L853299656
			saving label state [4] <= L853299656
		LLOAD 2
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
	1)	LCONST_1
			stack.push(MISC) -> [MISC,MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC,MISC]
	2)	LCONST_1
		LLOAD 4
			stack.push(MISC) -> [MISC,MISC,MISC,MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC,MISC,MISC,MISC]
	1)	LMUL
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC,MISC,MISC]
			stack.pop(MISC) -> [MISC,MISC,MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC,MISC]
			stack.pop(MISC) -> [MISC,MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.pop(MISC) -> [MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC]
			stack.pop(MISC) -> [MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC,MISC]
	2)	LMUL
	1)	LADD
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.pop(MISC) -> [MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC]
			stack.pop(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.pop(MISC) -> [MISC]
			stack.peek(MISC) -> [MISC]
			stack.pop(MISC) -> []
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
	2)	LADD
		LSTORE 8
			stack.peek(MISC) -> [MISC,MISC]
			stack.pop(MISC) -> [MISC]
			stack.peek(MISC) -> [MISC]
			stack.pop(MISC) -> []
			local.set(8, MISC)
			local.set(9, MISC)
		_LABEL <= L511523726
			saving label state [5] <= L511523726
		LLOAD 6
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
		INVOKESTATIC net/indiespot/struct/runtime/StructMemory pointer2handle (J)L$truct;
			stack.peek(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.pop(MISC) -> [MISC]
			stack.peek(MISC) -> [MISC]
			stack.pop(MISC) -> []
			stack.push(STRUCT) -> [STRUCT]
	2)	INVOKESTATIC net/indiespot/struct/runtime/StructMemory pointer2handle (J)I
			stack.peek(STRUCT) -> [STRUCT]
		ASTORE 10
			stack.peek(STRUCT) -> [STRUCT]
			stack.pop(STRUCT) -> []
	2)	ISTORE 10
			local.set(10, STRUCT)
		_LABEL <= L802223941
			saving label state [6] <= L802223941
		LLOAD 8
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
		INVOKESTATIC net/indiespot/struct/runtime/StructMemory pointer2handle (J)L$truct;
			stack.peek(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.pop(MISC) -> [MISC]
			stack.peek(MISC) -> [MISC]
			stack.pop(MISC) -> []
			stack.push(STRUCT) -> [STRUCT]
	2)	INVOKESTATIC net/indiespot/struct/runtime/StructMemory pointer2handle (J)I
			stack.peek(STRUCT) -> [STRUCT]
		ASTORE 11
			stack.peek(STRUCT) -> [STRUCT]
			stack.pop(STRUCT) -> []
	2)	ISTORE 11
			local.set(11, STRUCT)
		_LABEL <= L1075440521
			saving label state [7] <= L1075440521
		LLOAD 6
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
		ALOAD 10
	2)	ILOAD 10
			stack.push(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
		INVOKESTATIC net/indiespot/struct/runtime/StructMemory handle2pointer (L$truct;)J
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.pop(STRUCT) -> [MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC,MISC]
	2)	INVOKESTATIC net/indiespot/struct/runtime/StructMemory handle2pointer (I)J
		INVOKESTATIC org/junit/Assert assertEquals (JJ)V
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.pop(MISC) -> [MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC]
			stack.pop(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.pop(MISC) -> [MISC]
			stack.peek(MISC) -> [MISC]
			stack.pop(MISC) -> []
		_LABEL <= L1706463295
			saving label state [8] <= L1706463295
		LLOAD 8
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
		ALOAD 11
	2)	ILOAD 11
			stack.push(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
		INVOKESTATIC net/indiespot/struct/runtime/StructMemory handle2pointer (L$truct;)J
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.pop(STRUCT) -> [MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC,MISC]
	2)	INVOKESTATIC net/indiespot/struct/runtime/StructMemory handle2pointer (I)J
		INVOKESTATIC org/junit/Assert assertEquals (JJ)V
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC,MISC]
			stack.pop(MISC) -> [MISC,MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC,MISC]
			stack.pop(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.pop(MISC) -> [MISC]
			stack.peek(MISC) -> [MISC]
			stack.pop(MISC) -> []
		_LABEL <= L884493380
			saving label state [9] <= L884493380
		LLOAD 2
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
		SIPUSH 12
			stack.push(INT) -> [MISC,MISC,INT]
			stack.peek(INT) -> [MISC,MISC,INT]
			stack.pop(INT) -> [MISC,MISC]
			stack.push(STRUCT_TYPE) -> [MISC,MISC,STRUCT_TYPE]
	1)	NULL
			stack.push(INT) -> [MISC,MISC,STRUCT_TYPE,INT]
	2)	NULL
			stack.peek(MISC) -> [MISC,MISC,STRUCT_TYPE,INT]
			stack.peek(STRUCT_TYPE) -> [MISC,MISC,STRUCT_TYPE,INT]
			stack.set(INT, INT)
		INVOKESTATIC net/indiespot/struct/runtime/StructMemory pointer2handles (JII)[L$truct;
			stack.peek(INT) -> [MISC,MISC,INT,INT]
			stack.peek(INT) -> [MISC,MISC,INT,INT]
			stack.peek(INT) -> [MISC,MISC,INT,INT]
			stack.peek(INT) -> [MISC,MISC,INT,INT]
			stack.peek(MISC) -> [MISC,MISC,INT,INT]
			stack.peek(MISC) -> [MISC,MISC,INT,INT]
			stack.peek(INT) -> [MISC,MISC,INT,INT]
			stack.peek(INT) -> [MISC,MISC,INT,INT]
			stack.peek(MISC) -> [MISC,MISC,INT,INT]
			stack.peek(INT) -> [MISC,MISC,INT,INT]
			stack.pop(INT) -> [MISC,MISC,INT]
			stack.peek(INT) -> [MISC,MISC,INT]
			stack.pop(INT) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.pop(MISC) -> [MISC]
			stack.peek(MISC) -> [MISC]
			stack.pop(MISC) -> []
			stack.push(STRUCT_ARRAY) -> [STRUCT_ARRAY]
	2)	INVOKESTATIC net/indiespot/struct/runtime/StructMemory pointer2handles (JII)[I
			stack.peek(STRUCT_ARRAY) -> [STRUCT_ARRAY]
			stack.peek(STRUCT_ARRAY) -> [STRUCT_ARRAY]
		ASTORE 12
			stack.peek(STRUCT_ARRAY) -> [STRUCT_ARRAY]
			stack.pop(STRUCT_ARRAY) -> []
			local.set(12, STRUCT_ARRAY)
		_LABEL <= L798433126
			saving label state [10] <= L798433126
		ALOAD 12
			stack.push(STRUCT_ARRAY) -> [STRUCT_ARRAY]
	1)	ICONST_0
			stack.push(INT) -> [STRUCT_ARRAY,INT]
	2)	ICONST_0
	1)	AALOAD
			stack.peek(INT) -> [STRUCT_ARRAY,INT]
			stack.pop(INT) -> [STRUCT_ARRAY]
			stack.pop(STRUCT_ARRAY) -> []
			stack.push(STRUCT) -> [STRUCT]
	2)	IALOAD
		ALOAD 10
	2)	ILOAD 10
			stack.push(STRUCT) -> [STRUCT,STRUCT]
		INVOKESTATIC org/junit/Assert assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
		INVOKESTATIC net/indiespot/struct/runtime/StructMemory toString (L$truct;)Ljava/lang/String;
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.pop(STRUCT) -> [STRUCT]
			stack.push(REFERENCE) -> [STRUCT,REFERENCE]
	2)	INVOKESTATIC net/indiespot/struct/runtime/StructMemory toString (I)Ljava/lang/String;
			stack.peek(STRUCT) -> [STRUCT,REFERENCE]
			stack.peek(STRUCT) -> [STRUCT,REFERENCE]
	1)	SWAP
			stack.pop(REFERENCE) -> [STRUCT]
			stack.pop(STRUCT) -> []
			stack.push(REFERENCE) -> [REFERENCE]
			stack.push(STRUCT) -> [REFERENCE,STRUCT]
	2)	SWAP
		INVOKESTATIC net/indiespot/struct/runtime/StructMemory toString (L$truct;)Ljava/lang/String;
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.pop(STRUCT) -> [REFERENCE]
			stack.push(REFERENCE) -> [REFERENCE,REFERENCE]
	2)	INVOKESTATIC net/indiespot/struct/runtime/StructMemory toString (I)Ljava/lang/String;
	1)	SWAP
			stack.pop(REFERENCE) -> [REFERENCE]
			stack.pop(REFERENCE) -> []
			stack.push(REFERENCE) -> [REFERENCE]
			stack.push(REFERENCE) -> [REFERENCE,REFERENCE]
	2)	SWAP
			stack.peek(REFERENCE) -> [REFERENCE,REFERENCE]
			stack.peek(REFERENCE) -> [REFERENCE,REFERENCE]
			stack.peek(REFERENCE) -> [REFERENCE,REFERENCE]
			stack.pop(REFERENCE) -> [REFERENCE]
			stack.peek(REFERENCE) -> [REFERENCE]
			stack.pop(REFERENCE) -> []
		_LABEL <= L907134805
			saving label state [11] <= L907134805
		ALOAD 12
			stack.push(STRUCT_ARRAY) -> [STRUCT_ARRAY]
	1)	ICONST_1
			stack.push(INT) -> [STRUCT_ARRAY,INT]
	2)	ICONST_1
	1)	AALOAD
			stack.peek(INT) -> [STRUCT_ARRAY,INT]
			stack.pop(INT) -> [STRUCT_ARRAY]
			stack.pop(STRUCT_ARRAY) -> []
			stack.push(STRUCT) -> [STRUCT]
	2)	IALOAD
		ALOAD 11
	2)	ILOAD 11
			stack.push(STRUCT) -> [STRUCT,STRUCT]
		INVOKESTATIC org/junit/Assert assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
		INVOKESTATIC net/indiespot/struct/runtime/StructMemory toString (L$truct;)Ljava/lang/String;
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.pop(STRUCT) -> [STRUCT]
			stack.push(REFERENCE) -> [STRUCT,REFERENCE]
	2)	INVOKESTATIC net/indiespot/struct/runtime/StructMemory toString (I)Ljava/lang/String;
			stack.peek(STRUCT) -> [STRUCT,REFERENCE]
			stack.peek(STRUCT) -> [STRUCT,REFERENCE]
	1)	SWAP
			stack.pop(REFERENCE) -> [STRUCT]
			stack.pop(STRUCT) -> []
			stack.push(REFERENCE) -> [REFERENCE]
			stack.push(STRUCT) -> [REFERENCE,STRUCT]
	2)	SWAP
		INVOKESTATIC net/indiespot/struct/runtime/StructMemory toString (L$truct;)Ljava/lang/String;
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.pop(STRUCT) -> [REFERENCE]
			stack.push(REFERENCE) -> [REFERENCE,REFERENCE]
	2)	INVOKESTATIC net/indiespot/struct/runtime/StructMemory toString (I)Ljava/lang/String;
	1)	SWAP
			stack.pop(REFERENCE) -> [REFERENCE]
			stack.pop(REFERENCE) -> []
			stack.push(REFERENCE) -> [REFERENCE]
			stack.push(REFERENCE) -> [REFERENCE,REFERENCE]
	2)	SWAP
			stack.peek(REFERENCE) -> [REFERENCE,REFERENCE]
			stack.peek(REFERENCE) -> [REFERENCE,REFERENCE]
			stack.peek(REFERENCE) -> [REFERENCE,REFERENCE]
			stack.pop(REFERENCE) -> [REFERENCE]
			stack.peek(REFERENCE) -> [REFERENCE]
			stack.pop(REFERENCE) -> []
		_LABEL <= L1228257582
			saving label state [12] <= L1228257582
		LLOAD 2
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
		BIPUSH 12
			stack.push(INT) -> [MISC,MISC,INT]
	1)	NULL
			stack.push(INT) -> [MISC,MISC,INT,INT]
	2)	NULL
			stack.peek(MISC) -> [MISC,MISC,INT,INT]
		INVOKESTATIC net/indiespot/struct/runtime/StructMemory pointer2handles (JII)[L$truct;
			stack.peek(INT) -> [MISC,MISC,INT,INT]
			stack.peek(INT) -> [MISC,MISC,INT,INT]
			stack.peek(INT) -> [MISC,MISC,INT,INT]
			stack.peek(INT) -> [MISC,MISC,INT,INT]
			stack.peek(MISC) -> [MISC,MISC,INT,INT]
			stack.peek(MISC) -> [MISC,MISC,INT,INT]
			stack.peek(INT) -> [MISC,MISC,INT,INT]
			stack.peek(INT) -> [MISC,MISC,INT,INT]
			stack.peek(MISC) -> [MISC,MISC,INT,INT]
			stack.peek(INT) -> [MISC,MISC,INT,INT]
			stack.pop(INT) -> [MISC,MISC,INT]
			stack.peek(INT) -> [MISC,MISC,INT]
			stack.pop(INT) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.pop(MISC) -> [MISC]
			stack.peek(MISC) -> [MISC]
			stack.pop(MISC) -> []
			stack.push(STRUCT_ARRAY) -> [STRUCT_ARRAY]
	2)	INVOKESTATIC net/indiespot/struct/runtime/StructMemory pointer2handles (JII)[I
			stack.peek(STRUCT_ARRAY) -> [STRUCT_ARRAY]
			stack.peek(STRUCT_ARRAY) -> [STRUCT_ARRAY]
		ASTORE 12
			stack.peek(STRUCT_ARRAY) -> [STRUCT_ARRAY]
			stack.pop(STRUCT_ARRAY) -> []
			local.set(12, STRUCT_ARRAY)
		_LABEL <= L1712365710
			saving label state [13] <= L1712365710
		ALOAD 10
	2)	ILOAD 10
			stack.push(STRUCT) -> [STRUCT]
		ALOAD 12
			stack.push(STRUCT_ARRAY) -> [STRUCT,STRUCT_ARRAY]
	1)	ICONST_0
			stack.push(INT) -> [STRUCT,STRUCT_ARRAY,INT]
	2)	ICONST_0
	1)	AALOAD
			stack.peek(INT) -> [STRUCT,STRUCT_ARRAY,INT]
			stack.pop(INT) -> [STRUCT,STRUCT_ARRAY]
			stack.pop(STRUCT_ARRAY) -> [STRUCT]
			stack.push(STRUCT) -> [STRUCT,STRUCT]
	2)	IALOAD
		INVOKESTATIC org/junit/Assert assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
		INVOKESTATIC net/indiespot/struct/runtime/StructMemory toString (L$truct;)Ljava/lang/String;
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.pop(STRUCT) -> [STRUCT]
			stack.push(REFERENCE) -> [STRUCT,REFERENCE]
	2)	INVOKESTATIC net/indiespot/struct/runtime/StructMemory toString (I)Ljava/lang/String;
			stack.peek(STRUCT) -> [STRUCT,REFERENCE]
			stack.peek(STRUCT) -> [STRUCT,REFERENCE]
	1)	SWAP
			stack.pop(REFERENCE) -> [STRUCT]
			stack.pop(STRUCT) -> []
			stack.push(REFERENCE) -> [REFERENCE]
			stack.push(STRUCT) -> [REFERENCE,STRUCT]
	2)	SWAP
		INVOKESTATIC net/indiespot/struct/runtime/StructMemory toString (L$truct;)Ljava/lang/String;
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.pop(STRUCT) -> [REFERENCE]
			stack.push(REFERENCE) -> [REFERENCE,REFERENCE]
	2)	INVOKESTATIC net/indiespot/struct/runtime/StructMemory toString (I)Ljava/lang/String;
	1)	SWAP
			stack.pop(REFERENCE) -> [REFERENCE]
			stack.pop(REFERENCE) -> []
			stack.push(REFERENCE) -> [REFERENCE]
			stack.push(REFERENCE) -> [REFERENCE,REFERENCE]
	2)	SWAP
			stack.peek(REFERENCE) -> [REFERENCE,REFERENCE]
			stack.peek(REFERENCE) -> [REFERENCE,REFERENCE]
			stack.peek(REFERENCE) -> [REFERENCE,REFERENCE]
			stack.pop(REFERENCE) -> [REFERENCE]
			stack.peek(REFERENCE) -> [REFERENCE]
			stack.pop(REFERENCE) -> []
		_LABEL <= L1302134523
			saving label state [14] <= L1302134523
		ALOAD 11
	2)	ILOAD 11
			stack.push(STRUCT) -> [STRUCT]
		ALOAD 12
			stack.push(STRUCT_ARRAY) -> [STRUCT,STRUCT_ARRAY]
	1)	ICONST_1
			stack.push(INT) -> [STRUCT,STRUCT_ARRAY,INT]
	2)	ICONST_1
	1)	AALOAD
			stack.peek(INT) -> [STRUCT,STRUCT_ARRAY,INT]
			stack.pop(INT) -> [STRUCT,STRUCT_ARRAY]
			stack.pop(STRUCT_ARRAY) -> [STRUCT]
			stack.push(STRUCT) -> [STRUCT,STRUCT]
	2)	IALOAD
		INVOKESTATIC org/junit/Assert assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
		INVOKESTATIC net/indiespot/struct/runtime/StructMemory toString (L$truct;)Ljava/lang/String;
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.pop(STRUCT) -> [STRUCT]
			stack.push(REFERENCE) -> [STRUCT,REFERENCE]
	2)	INVOKESTATIC net/indiespot/struct/runtime/StructMemory toString (I)Ljava/lang/String;
			stack.peek(STRUCT) -> [STRUCT,REFERENCE]
			stack.peek(STRUCT) -> [STRUCT,REFERENCE]
	1)	SWAP
			stack.pop(REFERENCE) -> [STRUCT]
			stack.pop(STRUCT) -> []
			stack.push(REFERENCE) -> [REFERENCE]
			stack.push(STRUCT) -> [REFERENCE,STRUCT]
	2)	SWAP
		INVOKESTATIC net/indiespot/struct/runtime/StructMemory toString (L$truct;)Ljava/lang/String;
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.pop(STRUCT) -> [REFERENCE]
			stack.push(REFERENCE) -> [REFERENCE,REFERENCE]
	2)	INVOKESTATIC net/indiespot/struct/runtime/StructMemory toString (I)Ljava/lang/String;
	1)	SWAP
			stack.pop(REFERENCE) -> [REFERENCE]
			stack.pop(REFERENCE) -> []
			stack.push(REFERENCE) -> [REFERENCE]
			stack.push(REFERENCE) -> [REFERENCE,REFERENCE]
	2)	SWAP
			stack.peek(REFERENCE) -> [REFERENCE,REFERENCE]
			stack.peek(REFERENCE) -> [REFERENCE,REFERENCE]
			stack.peek(REFERENCE) -> [REFERENCE,REFERENCE]
			stack.pop(REFERENCE) -> [REFERENCE]
			stack.peek(REFERENCE) -> [REFERENCE]
			stack.pop(REFERENCE) -> []
		_LABEL <= L2116565131
			saving label state [15] <= L2116565131
		ALOAD 1
			stack.push(REFERENCE) -> [REFERENCE]
		INVOKEVIRTUAL java/nio/ByteBuffer clear ()Ljava/nio/Buffer;
			stack.peek(REFERENCE) -> [REFERENCE]
			stack.pop(REFERENCE) -> []
			stack.push(REFERENCE) -> [REFERENCE]
	1)	POP
			stack.pop(REFERENCE) -> []
	2)	POP
		_LABEL <= L43544162
			saving label state [16] <= L43544162
	1)	RETURN
	2)	RETURN
		_LABEL <= L1702290754
			saving label state [17] <= L1702290754
StructEnv.rewrite OUTPUT [net/indiespot/struct/TestFromPointer]:
// class version 51.0 (51)
// access flags 0x21
public class net/indiespot/struct/TestFromPointer {

  // compiled from: TestFromPointer.java

  // access flags 0x1
  public <init>()V
   L0
    LINENUMBER 15 L0
    ALOAD 0
    INVOKESPECIAL java/lang/Object.<init> ()V
    RETURN
   L1
    LOCALVARIABLE this Lnet/indiespot/struct/TestFromPointer; L0 L1 0
    MAXSTACK = 1
    MAXLOCALS = 1

  // access flags 0x1
  public testFromPointer()V
  @Lorg/junit/Test;()
   L0
    LINENUMBER 18 L0
    SIPUSH 234
    INVOKESTATIC java/nio/ByteBuffer.allocateDirect (I)Ljava/nio/ByteBuffer;
    ASTORE 1
   L1
    LINENUMBER 20 L1
    ALOAD 1
    INVOKESTATIC net/indiespot/struct/runtime/StructUnsafe.getBufferBaseAddress (Ljava/nio/ByteBuffer;)J
    LSTORE 2
   L2
    LINENUMBER 22 L2
    SIPUSH 12
    I2L
    LSTORE 4
   L3
    LINENUMBER 24 L3
    LLOAD 2
    LCONST_0
    LLOAD 4
    LMUL
    LADD
    LSTORE 6
   L4
    LINENUMBER 25 L4
    LLOAD 2
    LCONST_1
    LLOAD 4
    LMUL
    LADD
    LSTORE 8
   L5
    LINENUMBER 27 L5
    LLOAD 6
    INVOKESTATIC net/indiespot/struct/runtime/StructMemory.pointer2handle (J)I
    ISTORE 10
   L6
    LINENUMBER 28 L6
    LLOAD 8
    INVOKESTATIC net/indiespot/struct/runtime/StructMemory.pointer2handle (J)I
    ISTORE 11
   L7
    LINENUMBER 31 L7
    LLOAD 6
    ILOAD 10
    INVOKESTATIC net/indiespot/struct/runtime/StructMemory.handle2pointer (I)J
    INVOKESTATIC org/junit/Assert.assertEquals (JJ)V
   L8
    LINENUMBER 32 L8
    LLOAD 8
    ILOAD 11
    INVOKESTATIC net/indiespot/struct/runtime/StructMemory.handle2pointer (I)J
    INVOKESTATIC org/junit/Assert.assertEquals (JJ)V
   L9
    LINENUMBER 34 L9
    LLOAD 2
    SIPUSH 12
    ICONST_2
    INVOKESTATIC net/indiespot/struct/runtime/StructMemory.pointer2handles (JII)[I
    ASTORE 12
   L10
    LINENUMBER 36 L10
    ALOAD 12
    ICONST_0
    IALOAD
    ILOAD 10
    INVOKESTATIC net/indiespot/struct/runtime/StructMemory.toString (I)Ljava/lang/String;
    SWAP
    INVOKESTATIC net/indiespot/struct/runtime/StructMemory.toString (I)Ljava/lang/String;
    SWAP
    INVOKESTATIC org/junit/Assert.assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
   L11
    LINENUMBER 37 L11
    ALOAD 12
    ICONST_1
    IALOAD
    ILOAD 11
    INVOKESTATIC net/indiespot/struct/runtime/StructMemory.toString (I)Ljava/lang/String;
    SWAP
    INVOKESTATIC net/indiespot/struct/runtime/StructMemory.toString (I)Ljava/lang/String;
    SWAP
    INVOKESTATIC org/junit/Assert.assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
   L12
    LINENUMBER 39 L12
    LLOAD 2
    BIPUSH 12
    ICONST_2
    INVOKESTATIC net/indiespot/struct/runtime/StructMemory.pointer2handles (JII)[I
    ASTORE 12
   L13
    LINENUMBER 40 L13
    ILOAD 10
    ALOAD 12
    ICONST_0
    IALOAD
    INVOKESTATIC net/indiespot/struct/runtime/StructMemory.toString (I)Ljava/lang/String;
    SWAP
    INVOKESTATIC net/indiespot/struct/runtime/StructMemory.toString (I)Ljava/lang/String;
    SWAP
    INVOKESTATIC org/junit/Assert.assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
   L14
    LINENUMBER 41 L14
    ILOAD 11
    ALOAD 12
    ICONST_1
    IALOAD
    INVOKESTATIC net/indiespot/struct/runtime/StructMemory.toString (I)Ljava/lang/String;
    SWAP
    INVOKESTATIC net/indiespot/struct/runtime/StructMemory.toString (I)Ljava/lang/String;
    SWAP
    INVOKESTATIC org/junit/Assert.assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
   L15
    LINENUMBER 43 L15
    ALOAD 1
    INVOKEVIRTUAL java/nio/ByteBuffer.clear ()Ljava/nio/Buffer;
    POP
   L16
    LINENUMBER 44 L16
    RETURN
    MAXSTACK = 6
    MAXLOCALS = 13
}
 */
