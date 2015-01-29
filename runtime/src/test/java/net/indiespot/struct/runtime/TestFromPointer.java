package net.indiespot.struct.runtime;

import net.indiespot.struct.AbstractRuntimeTest;
import net.indiespot.struct.Struct;
import net.indiespot.struct.api.runtime.StructUnsafe;
import net.indiespot.struct.testlib.Ship;
import net.indiespot.struct.testlib.Vec3;
import org.junit.Assert;
import org.junit.Test;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;


/**
 * Created by jason on 1/25/15.
 */
@SuppressWarnings("PointlessArithmeticExpression")
public class TestFromPointer extends AbstractRuntimeTest {

    @Test
    public  void sizeOfTest() {
        int sizeofVec3 = Struct.sizeof(Vec3.class);
        int sizeofShip = Struct.sizeof(Ship.class);

        assert (sizeofVec3 == 12);
        assert (sizeofShip == 8);
    }



    @Test
    public void testFromPointer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(234);

        long addr = StructUnsafe.getBufferBaseAddress(bb);
        long addr2 = ((DirectBuffer) bb).address();

        Assert.assertEquals(addr, addr2);


        long vec3Size = Struct.sizeof(Vec3.class);

        long v1Ptr = (addr + 0 * 24);
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
analyzing class: net/indiespot/struct/runtime/TestFromPointer
	checking method for rewrite: <init>()V
	checking method for rewrite: sizeOfTest()V
		flagged for rewrite
	checking method for rewrite: testJOL()V
	checking method for rewrite: testFromPointer()V
		flagged for rewrite
	checking method for rewrite: <clinit>()V
StructEnv.rewrite INPUT [net/indiespot/struct/runtime/TestFromPointer]:
// class version 52.0 (52)
// access flags 0x21
public class net/indiespot/struct/runtime/TestFromPointer {

  // compiled from: TestFromPointer.java

  // access flags 0x1018
  final static synthetic Z $assertionsDisabled

  // access flags 0x1
  public <init>()V
   L0
    LINENUMBER 21 L0
    ALOAD 0
    INVOKESPECIAL java/lang/Object.<init> ()V
    RETURN
   L1
    LOCALVARIABLE this Lnet/indiespot/struct/runtime/TestFromPointer; L0 L1 0
    MAXSTACK = 1
    MAXLOCALS = 1

  // access flags 0x1
  public sizeOfTest()V
  @Lorg/junit/Test;()
   L0
    LINENUMBER 25 L0
    LDC Lnet/indiespot/struct/testlib/Vec3;.class
    INVOKESTATIC net/indiespot/struct/Struct.sizeof (Ljava/lang/Class;)I
    ISTORE 1
   L1
    LINENUMBER 26 L1
    LDC Lnet/indiespot/struct/testlib/Ship;.class
    INVOKESTATIC net/indiespot/struct/Struct.sizeof (Ljava/lang/Class;)I
    ISTORE 2
   L2
    LINENUMBER 28 L2
    GETSTATIC net/indiespot/struct/runtime/TestFromPointer.$assertionsDisabled : Z
    IFNE L3
    ILOAD 1
    BIPUSH 12
    IF_ICMPEQ L3
    NEW java/lang/AssertionError
    DUP
    INVOKESPECIAL java/lang/AssertionError.<init> ()V
    ATHROW
   L3
    LINENUMBER 29 L3
   FRAME APPEND [I I]
    GETSTATIC net/indiespot/struct/runtime/TestFromPointer.$assertionsDisabled : Z
    IFNE L4
    ILOAD 2
    BIPUSH 8
    IF_ICMPEQ L4
    NEW java/lang/AssertionError
    DUP
    INVOKESPECIAL java/lang/AssertionError.<init> ()V
    ATHROW
   L4
    LINENUMBER 30 L4
   FRAME SAME
    RETURN
   L5
    LOCALVARIABLE this Lnet/indiespot/struct/runtime/TestFromPointer; L0 L5 0
    LOCALVARIABLE sizeofVec3 I L1 L5 1
    LOCALVARIABLE sizeofShip I L2 L5 2
    MAXSTACK = 2
    MAXLOCALS = 3

  // access flags 0x1
  public testJOL()V throws java/lang/Exception
  @Lorg/junit/Test;()
  @Lnet/indiespot/struct/transform/SkipTransformation;() // invisible
   L0
    LINENUMBER 35 L0
    GETSTATIC java/lang/System.err : Ljava/io/PrintStream;
    INVOKESTATIC org/openjdk/jol/util/VMSupport.vmDetails ()Ljava/lang/String;
    INVOKEVIRTUAL java/io/PrintStream.println (Ljava/lang/String;)V
   L1
    LINENUMBER 36 L1
    GETSTATIC java/lang/System.err : Ljava/io/PrintStream;
    LDC Lnet/indiespot/struct/testlib/Vec3;.class
    INVOKESTATIC org/openjdk/jol/info/ClassLayout.parseClass (Ljava/lang/Class;)Lorg/openjdk/jol/info/ClassLayout;
    INVOKEVIRTUAL org/openjdk/jol/info/ClassLayout.toPrintable ()Ljava/lang/String;
    INVOKEVIRTUAL java/io/PrintStream.println (Ljava/lang/String;)V
   L2
    LINENUMBER 58 L2
    RETURN
   L3
    LOCALVARIABLE this Lnet/indiespot/struct/runtime/TestFromPointer; L0 L3 0
    MAXSTACK = 2
    MAXLOCALS = 1

  // access flags 0x1
  public testFromPointer()V
  @Lorg/junit/Test;()
   L0
    LINENUMBER 62 L0
    SIPUSH 234
    INVOKESTATIC java/nio/ByteBuffer.allocateDirect (I)Ljava/nio/ByteBuffer;
    ASTORE 1
   L1
    LINENUMBER 64 L1
    ALOAD 1
    INVOKESTATIC net/indiespot/struct/api/runtime/StructUnsafe.getBufferBaseAddress (Ljava/nio/ByteBuffer;)J
    LSTORE 2
   L2
    LINENUMBER 65 L2
    ALOAD 1
    CHECKCAST sun/nio/ch/DirectBuffer
    INVOKEINTERFACE sun/nio/ch/DirectBuffer.address ()J
    LSTORE 4
   L3
    LINENUMBER 67 L3
    LLOAD 2
    LLOAD 4
    INVOKESTATIC org/junit/Assert.assertEquals (JJ)V
   L4
    LINENUMBER 70 L4
    LDC Lnet/indiespot/struct/testlib/Vec3;.class
    INVOKESTATIC net/indiespot/struct/Struct.sizeof (Ljava/lang/Class;)I
    I2L
    LSTORE 6
   L5
    LINENUMBER 72 L5
    LLOAD 2
    LCONST_0
    LADD
    LSTORE 8
   L6
    LINENUMBER 73 L6
    LLOAD 2
    LCONST_1
    LLOAD 6
    LMUL
    LADD
    LSTORE 10
   L7
    LINENUMBER 75 L7
    LLOAD 8
    INVOKESTATIC net/indiespot/struct/Struct.fromPointer (J)Ljava/lang/Object;
    CHECKCAST net/indiespot/struct/testlib/Vec3
    ASTORE 12
   L8
    LINENUMBER 76 L8
    LLOAD 10
    INVOKESTATIC net/indiespot/struct/Struct.fromPointer (J)Ljava/lang/Object;
    CHECKCAST net/indiespot/struct/testlib/Vec3
    ASTORE 13
   L9
    LINENUMBER 79 L9
    LLOAD 8
    ALOAD 12
    INVOKESTATIC net/indiespot/struct/Struct.getPointer (Ljava/lang/Object;)J
    INVOKESTATIC org/junit/Assert.assertEquals (JJ)V
   L10
    LINENUMBER 80 L10
    LLOAD 10
    ALOAD 13
    INVOKESTATIC net/indiespot/struct/Struct.getPointer (Ljava/lang/Object;)J
    INVOKESTATIC org/junit/Assert.assertEquals (JJ)V
   L11
    LINENUMBER 82 L11
    LLOAD 2
    LDC Lnet/indiespot/struct/testlib/Vec3;.class
    ICONST_2
    INVOKESTATIC net/indiespot/struct/Struct.fromPointer (JLjava/lang/Class;I)[Ljava/lang/Object;
    CHECKCAST [Lnet/indiespot/struct/testlib/Vec3;
    ASTORE 14
   L12
    LINENUMBER 84 L12
    ALOAD 14
    ICONST_0
    AALOAD
    ALOAD 12
    INVOKESTATIC org/junit/Assert.assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
   L13
    LINENUMBER 85 L13
    ALOAD 14
    ICONST_1
    AALOAD
    ALOAD 13
    INVOKESTATIC org/junit/Assert.assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
   L14
    LINENUMBER 87 L14
    LLOAD 2
    BIPUSH 12
    ICONST_2
    INVOKESTATIC net/indiespot/struct/Struct.fromPointer (JII)[Ljava/lang/Object;
    CHECKCAST [Lnet/indiespot/struct/testlib/Vec3;
    ASTORE 14
   L15
    LINENUMBER 88 L15
    ALOAD 12
    ALOAD 14
    ICONST_0
    AALOAD
    INVOKESTATIC org/junit/Assert.assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
   L16
    LINENUMBER 89 L16
    ALOAD 13
    ALOAD 14
    ICONST_1
    AALOAD
    INVOKESTATIC org/junit/Assert.assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
   L17
    LINENUMBER 91 L17
    ALOAD 1
    INVOKEVIRTUAL java/nio/ByteBuffer.clear ()Ljava/nio/Buffer;
    POP
   L18
    LINENUMBER 92 L18
    RETURN
   L19
    LOCALVARIABLE this Lnet/indiespot/struct/runtime/TestFromPointer; L0 L19 0
    LOCALVARIABLE bb Ljava/nio/ByteBuffer; L1 L19 1
    LOCALVARIABLE addr J L2 L19 2
    LOCALVARIABLE addr2 J L3 L19 4
    LOCALVARIABLE vec3Size J L5 L19 6
    LOCALVARIABLE v1Ptr J L6 L19 8
    LOCALVARIABLE v2Ptr J L7 L19 10
    LOCALVARIABLE v1 Lnet/indiespot/struct/testlib/Vec3; L8 L19 12
    LOCALVARIABLE v2 Lnet/indiespot/struct/testlib/Vec3; L9 L19 13
    LOCALVARIABLE vs [Lnet/indiespot/struct/testlib/Vec3; L12 L19 14
    MAXSTACK = 6
    MAXLOCALS = 15

  // access flags 0x8
  static <clinit>()V
   L0
    LINENUMBER 20 L0
    LDC Lnet/indiespot/struct/runtime/TestFromPointer;.class
    INVOKEVIRTUAL java/lang/Class.desiredAssertionStatus ()Z
    IFNE L1
    ICONST_1
    GOTO L2
   L1
   FRAME SAME
    ICONST_0
   L2
   FRAME SAME1 I
    PUTSTATIC net/indiespot/struct/runtime/TestFromPointer.$assertionsDisabled : Z
    RETURN
    MAXSTACK = 1
    MAXLOCALS = 0
}
StructEnv.rewrite TRANSFORM [net/indiespot/struct/runtime/TestFromPointer]:
	field1: $assertionsDisabled Z
	field2: $assertionsDisabled Z
	method1: <init> ()V
	method2: <init> ()V
		early out for rewrite? [true]
	method1: sizeOfTest ()V
	method2: sizeOfTest ()V
		early out for rewrite? [false]
			local.set(0, REFERENCE)
		_LABEL <= L610998173
			saving label state [0] <= L610998173
		SIPUSH 12
			stack.push(INT) -> [INT]
			stack.peek(INT) -> [INT]
			stack.pop(INT) -> []
			stack.push(STRUCT_TYPE) -> [STRUCT_TYPE]
			stack.peek(STRUCT_TYPE) -> [STRUCT_TYPE]
			stack.set(INT, INT)
		ISTORE 1
			stack.peek(INT) -> [INT]
			stack.pop(INT) -> []
			local.set(1, INT)
		_LABEL <= L1029991479
			saving label state [1] <= L1029991479
		SIPUSH 8
			stack.push(INT) -> [INT]
			stack.peek(INT) -> [INT]
			stack.pop(INT) -> []
			stack.push(STRUCT_TYPE) -> [STRUCT_TYPE]
			stack.peek(STRUCT_TYPE) -> [STRUCT_TYPE]
			stack.set(INT, INT)
		ISTORE 2
			stack.peek(INT) -> [INT]
			stack.pop(INT) -> []
			local.set(2, INT)
		_LABEL <= L1104106489
			saving label state [2] <= L1104106489
		GETSTATIC net/indiespot/struct/runtime/TestFromPointer $assertionsDisabled Z
			stack.push(INT) -> [INT]
		IFNE
			stack.pop(INT) -> []
				IFNE label[-1] jump to L94438417
		ILOAD 1
			stack.push(INT) -> [INT]
		BIPUSH 12
			stack.push(INT) -> [INT,INT]
		IF_ICMPEQ
			stack.peek(INT) -> [INT,INT]
			stack.pop(INT) -> [INT]
			stack.peek(INT) -> [INT]
			stack.pop(INT) -> []
				IF_ICMPEQ label[3] jump to L94438417
		NEW java/lang/AssertionError
			stack.push(REFERENCE) -> [REFERENCE]
	1)	DUP
			stack.peek(REFERENCE) -> [REFERENCE]
			stack.push(REFERENCE) -> [REFERENCE,REFERENCE]
	2)	DUP
		INVOKESPECIAL java/lang/AssertionError <init> ()V
			stack.peek(REFERENCE) -> [REFERENCE,REFERENCE]
			stack.pop(REFERENCE) -> [REFERENCE]
	1)	ATHROW
			stack.peek(REFERENCE) -> [REFERENCE]
			stack.pop(REFERENCE) -> []
	2)	ATHROW
		_LABEL <= L94438417
			restored label state [3] <= L94438417
		GETSTATIC net/indiespot/struct/runtime/TestFromPointer $assertionsDisabled Z
			stack.push(INT) -> [INT]
		IFNE
			stack.pop(INT) -> []
				IFNE label[-1] jump to L193064360
		ILOAD 2
			stack.push(INT) -> [INT]
		BIPUSH 8
			stack.push(INT) -> [INT,INT]
		IF_ICMPEQ
			stack.peek(INT) -> [INT,INT]
			stack.pop(INT) -> [INT]
			stack.peek(INT) -> [INT]
			stack.pop(INT) -> []
				IF_ICMPEQ label[4] jump to L193064360
		NEW java/lang/AssertionError
			stack.push(REFERENCE) -> [REFERENCE]
	1)	DUP
			stack.peek(REFERENCE) -> [REFERENCE]
			stack.push(REFERENCE) -> [REFERENCE,REFERENCE]
	2)	DUP
		INVOKESPECIAL java/lang/AssertionError <init> ()V
			stack.peek(REFERENCE) -> [REFERENCE,REFERENCE]
			stack.pop(REFERENCE) -> [REFERENCE]
	1)	ATHROW
			stack.peek(REFERENCE) -> [REFERENCE]
			stack.pop(REFERENCE) -> []
	2)	ATHROW
		_LABEL <= L193064360
			restored label state [4] <= L193064360
	1)	RETURN
	2)	RETURN
		_LABEL <= L109961541
			saving label state [5] <= L109961541
	method1: testJOL ()V
	method2: testJOL ()V
		early out for rewrite? [true]
	method1: testFromPointer ()V
	method2: testFromPointer ()V
		early out for rewrite? [false]
			local.set(0, REFERENCE)
		_LABEL <= L670700378
			saving label state [0] <= L670700378
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
		_LABEL <= L1109371569
			saving label state [1] <= L1109371569
		ALOAD 1
			stack.push(REFERENCE) -> [REFERENCE]
		INVOKESTATIC net/indiespot/struct/api/runtime/StructUnsafe getBufferBaseAddress (Ljava/nio/ByteBuffer;)J
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
		_LABEL <= L728890494
			saving label state [2] <= L728890494
		ALOAD 1
			stack.push(REFERENCE) -> [REFERENCE]
			stack.peek(REFERENCE) -> [REFERENCE]
			stack.peek(REFERENCE) -> [REFERENCE]
		CHECKCAST sun/nio/ch/DirectBuffer
		INVOKEINTERFACE sun/nio/ch/DirectBuffer address ()J
			stack.peek(REFERENCE) -> [REFERENCE]
			stack.pop(REFERENCE) -> []
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
		LSTORE 4
			stack.peek(MISC) -> [MISC,MISC]
			stack.pop(MISC) -> [MISC]
			stack.peek(MISC) -> [MISC]
			stack.pop(MISC) -> []
			local.set(4, MISC)
			local.set(5, MISC)
		_LABEL <= L1558600329
			saving label state [3] <= L1558600329
		LLOAD 2
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
		LLOAD 4
			stack.push(MISC) -> [MISC,MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC,MISC]
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
		_LABEL <= L636718812
			saving label state [4] <= L636718812
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
		LSTORE 6
			stack.peek(MISC) -> [MISC,MISC]
			stack.pop(MISC) -> [MISC]
			stack.peek(MISC) -> [MISC]
			stack.pop(MISC) -> []
			local.set(6, MISC)
			local.set(7, MISC)
		_LABEL <= L445051633
			saving label state [5] <= L445051633
		LLOAD 2
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
	1)	LCONST_0
			stack.push(MISC) -> [MISC,MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC,MISC]
	2)	LCONST_0
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
		_LABEL <= L1051754451
			saving label state [6] <= L1051754451
		LLOAD 2
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
	1)	LCONST_1
			stack.push(MISC) -> [MISC,MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC,MISC]
	2)	LCONST_1
		LLOAD 6
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
		LSTORE 10
			stack.peek(MISC) -> [MISC,MISC]
			stack.pop(MISC) -> [MISC]
			stack.peek(MISC) -> [MISC]
			stack.pop(MISC) -> []
			local.set(10, MISC)
			local.set(11, MISC)
		_LABEL <= L1349277854
			saving label state [7] <= L1349277854
		LLOAD 8
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
		INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory pointer2handle (J)L$truct;
			stack.peek(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.pop(MISC) -> [MISC]
			stack.peek(MISC) -> [MISC]
			stack.pop(MISC) -> []
			stack.push(STRUCT) -> [STRUCT]
	2)	INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory pointer2handle (J)I
			stack.peek(STRUCT) -> [STRUCT]
		ASTORE 12
			stack.peek(STRUCT) -> [STRUCT]
			stack.pop(STRUCT) -> []
	2)	ISTORE 12
			local.set(12, STRUCT)
		_LABEL <= L1775282465
			saving label state [8] <= L1775282465
		LLOAD 10
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
		INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory pointer2handle (J)L$truct;
			stack.peek(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.peek(MISC) -> [MISC,MISC]
			stack.pop(MISC) -> [MISC]
			stack.peek(MISC) -> [MISC]
			stack.pop(MISC) -> []
			stack.push(STRUCT) -> [STRUCT]
	2)	INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory pointer2handle (J)I
			stack.peek(STRUCT) -> [STRUCT]
		ASTORE 13
			stack.peek(STRUCT) -> [STRUCT]
			stack.pop(STRUCT) -> []
	2)	ISTORE 13
			local.set(13, STRUCT)
		_LABEL <= L1147985808
			saving label state [9] <= L1147985808
		LLOAD 8
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
		ALOAD 12
	2)	ILOAD 12
			stack.push(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
		INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory handle2pointer (L$truct;)J
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.pop(STRUCT) -> [MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC,MISC]
	2)	INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory handle2pointer (I)J
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
		_LABEL <= L2040495657
			saving label state [10] <= L2040495657
		LLOAD 10
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
		ALOAD 13
	2)	ILOAD 13
			stack.push(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
		INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory handle2pointer (L$truct;)J
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.peek(STRUCT) -> [MISC,MISC,STRUCT]
			stack.pop(STRUCT) -> [MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC]
			stack.push(MISC) -> [MISC,MISC,MISC,MISC]
	2)	INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory handle2pointer (I)J
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
		_LABEL <= L1267032364
			saving label state [11] <= L1267032364
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
		INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory pointer2handles (JII)[L$truct;
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
	2)	INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory pointer2handles (JII)[I
			stack.peek(STRUCT_ARRAY) -> [STRUCT_ARRAY]
			stack.peek(STRUCT_ARRAY) -> [STRUCT_ARRAY]
		ASTORE 14
			stack.peek(STRUCT_ARRAY) -> [STRUCT_ARRAY]
			stack.pop(STRUCT_ARRAY) -> []
			local.set(14, STRUCT_ARRAY)
		_LABEL <= L661672156
			saving label state [12] <= L661672156
		ALOAD 14
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
		ALOAD 12
	2)	ILOAD 12
			stack.push(STRUCT) -> [STRUCT,STRUCT]
		INVOKESTATIC org/junit/Assert assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
		INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory toString (L$truct;)Ljava/lang/String;
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.pop(STRUCT) -> [STRUCT]
			stack.push(REFERENCE) -> [STRUCT,REFERENCE]
	2)	INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory toString (I)Ljava/lang/String;
			stack.peek(STRUCT) -> [STRUCT,REFERENCE]
			stack.peek(STRUCT) -> [STRUCT,REFERENCE]
	1)	SWAP
			stack.pop(REFERENCE) -> [STRUCT]
			stack.pop(STRUCT) -> []
			stack.push(REFERENCE) -> [REFERENCE]
			stack.push(STRUCT) -> [REFERENCE,STRUCT]
	2)	SWAP
		INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory toString (L$truct;)Ljava/lang/String;
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.pop(STRUCT) -> [REFERENCE]
			stack.push(REFERENCE) -> [REFERENCE,REFERENCE]
	2)	INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory toString (I)Ljava/lang/String;
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
		_LABEL <= L96639997
			saving label state [13] <= L96639997
		ALOAD 14
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
		ALOAD 13
	2)	ILOAD 13
			stack.push(STRUCT) -> [STRUCT,STRUCT]
		INVOKESTATIC org/junit/Assert assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
		INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory toString (L$truct;)Ljava/lang/String;
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.pop(STRUCT) -> [STRUCT]
			stack.push(REFERENCE) -> [STRUCT,REFERENCE]
	2)	INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory toString (I)Ljava/lang/String;
			stack.peek(STRUCT) -> [STRUCT,REFERENCE]
			stack.peek(STRUCT) -> [STRUCT,REFERENCE]
	1)	SWAP
			stack.pop(REFERENCE) -> [STRUCT]
			stack.pop(STRUCT) -> []
			stack.push(REFERENCE) -> [REFERENCE]
			stack.push(STRUCT) -> [REFERENCE,STRUCT]
	2)	SWAP
		INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory toString (L$truct;)Ljava/lang/String;
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.pop(STRUCT) -> [REFERENCE]
			stack.push(REFERENCE) -> [REFERENCE,REFERENCE]
	2)	INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory toString (I)Ljava/lang/String;
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
		_LABEL <= L128893786
			saving label state [14] <= L128893786
		LLOAD 2
			stack.push(MISC) -> [MISC]
			stack.push(MISC) -> [MISC,MISC]
		BIPUSH 12
			stack.push(INT) -> [MISC,MISC,INT]
	1)	NULL
			stack.push(INT) -> [MISC,MISC,INT,INT]
	2)	NULL
			stack.peek(MISC) -> [MISC,MISC,INT,INT]
		INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory pointer2handles (JII)[L$truct;
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
	2)	INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory pointer2handles (JII)[I
			stack.peek(STRUCT_ARRAY) -> [STRUCT_ARRAY]
			stack.peek(STRUCT_ARRAY) -> [STRUCT_ARRAY]
		ASTORE 14
			stack.peek(STRUCT_ARRAY) -> [STRUCT_ARRAY]
			stack.pop(STRUCT_ARRAY) -> []
			local.set(14, STRUCT_ARRAY)
		_LABEL <= L1732398722
			saving label state [15] <= L1732398722
		ALOAD 12
	2)	ILOAD 12
			stack.push(STRUCT) -> [STRUCT]
		ALOAD 14
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
		INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory toString (L$truct;)Ljava/lang/String;
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.pop(STRUCT) -> [STRUCT]
			stack.push(REFERENCE) -> [STRUCT,REFERENCE]
	2)	INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory toString (I)Ljava/lang/String;
			stack.peek(STRUCT) -> [STRUCT,REFERENCE]
			stack.peek(STRUCT) -> [STRUCT,REFERENCE]
	1)	SWAP
			stack.pop(REFERENCE) -> [STRUCT]
			stack.pop(STRUCT) -> []
			stack.push(REFERENCE) -> [REFERENCE]
			stack.push(STRUCT) -> [REFERENCE,STRUCT]
	2)	SWAP
		INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory toString (L$truct;)Ljava/lang/String;
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.pop(STRUCT) -> [REFERENCE]
			stack.push(REFERENCE) -> [REFERENCE,REFERENCE]
	2)	INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory toString (I)Ljava/lang/String;
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
		_LABEL <= L1108411398
			saving label state [16] <= L1108411398
		ALOAD 13
	2)	ILOAD 13
			stack.push(STRUCT) -> [STRUCT]
		ALOAD 14
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
		INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory toString (L$truct;)Ljava/lang/String;
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.peek(STRUCT) -> [STRUCT,STRUCT]
			stack.pop(STRUCT) -> [STRUCT]
			stack.push(REFERENCE) -> [STRUCT,REFERENCE]
	2)	INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory toString (I)Ljava/lang/String;
			stack.peek(STRUCT) -> [STRUCT,REFERENCE]
			stack.peek(STRUCT) -> [STRUCT,REFERENCE]
	1)	SWAP
			stack.pop(REFERENCE) -> [STRUCT]
			stack.pop(STRUCT) -> []
			stack.push(REFERENCE) -> [REFERENCE]
			stack.push(STRUCT) -> [REFERENCE,STRUCT]
	2)	SWAP
		INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory toString (L$truct;)Ljava/lang/String;
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.peek(STRUCT) -> [REFERENCE,STRUCT]
			stack.pop(STRUCT) -> [REFERENCE]
			stack.push(REFERENCE) -> [REFERENCE,REFERENCE]
	2)	INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory toString (I)Ljava/lang/String;
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
		_LABEL <= L1394438858
			saving label state [17] <= L1394438858
		ALOAD 1
			stack.push(REFERENCE) -> [REFERENCE]
		INVOKEVIRTUAL java/nio/ByteBuffer clear ()Ljava/nio/Buffer;
			stack.peek(REFERENCE) -> [REFERENCE]
			stack.pop(REFERENCE) -> []
			stack.push(REFERENCE) -> [REFERENCE]
	1)	POP
			stack.pop(REFERENCE) -> []
	2)	POP
		_LABEL <= L584634336
			saving label state [18] <= L584634336
	1)	RETURN
	2)	RETURN
		_LABEL <= L1469821799
			saving label state [19] <= L1469821799
	method1: <clinit> ()V
	method2: <clinit> ()V
		early out for rewrite? [true]
StructEnv.rewrite OUTPUT [net/indiespot/struct/runtime/TestFromPointer]:
// class version 52.0 (52)
// access flags 0x21
public class net/indiespot/struct/runtime/TestFromPointer {

  // compiled from: TestFromPointer.java

  // access flags 0x1018
  final static synthetic Z $assertionsDisabled

  // access flags 0x1
  public <init>()V
   L0
    LINENUMBER 21 L0
    ALOAD 0
    INVOKESPECIAL java/lang/Object.<init> ()V
    RETURN
   L1
    LOCALVARIABLE this Lnet/indiespot/struct/runtime/TestFromPointer; L0 L1 0
    MAXSTACK = 1
    MAXLOCALS = 1

  // access flags 0x1
  public sizeOfTest()V
  @Lorg/junit/Test;()
   L0
    LINENUMBER 25 L0
    SIPUSH 12
    ISTORE 1
   L1
    LINENUMBER 26 L1
    SIPUSH 8
    ISTORE 2
   L2
    LINENUMBER 28 L2
    GETSTATIC net/indiespot/struct/runtime/TestFromPointer.$assertionsDisabled : Z
    IFNE L3
    ILOAD 1
    BIPUSH 12
    IF_ICMPEQ L3
    NEW java/lang/AssertionError
    DUP
    INVOKESPECIAL java/lang/AssertionError.<init> ()V
    ATHROW
   L3
    LINENUMBER 29 L3
   FRAME APPEND [I I]
    GETSTATIC net/indiespot/struct/runtime/TestFromPointer.$assertionsDisabled : Z
    IFNE L4
    ILOAD 2
    BIPUSH 8
    IF_ICMPEQ L4
    NEW java/lang/AssertionError
    DUP
    INVOKESPECIAL java/lang/AssertionError.<init> ()V
    ATHROW
   L4
    LINENUMBER 30 L4
   FRAME SAME
    RETURN
   L5
    LOCALVARIABLE this Lnet/indiespot/struct/runtime/TestFromPointer; L0 L5 0
    LOCALVARIABLE sizeofVec3 I L1 L5 1
    LOCALVARIABLE sizeofShip I L2 L5 2
    MAXSTACK = 2
    MAXLOCALS = 3

  // access flags 0x1
  public testJOL()V throws java/lang/Exception
  @Lorg/junit/Test;()
  @Lnet/indiespot/struct/transform/SkipTransformation;() // invisible
   L0
    LINENUMBER 35 L0
    GETSTATIC java/lang/System.err : Ljava/io/PrintStream;
    INVOKESTATIC org/openjdk/jol/util/VMSupport.vmDetails ()Ljava/lang/String;
    INVOKEVIRTUAL java/io/PrintStream.println (Ljava/lang/String;)V
   L1
    LINENUMBER 36 L1
    GETSTATIC java/lang/System.err : Ljava/io/PrintStream;
    LDC Lnet/indiespot/struct/testlib/Vec3;.class
    INVOKESTATIC org/openjdk/jol/info/ClassLayout.parseClass (Ljava/lang/Class;)Lorg/openjdk/jol/info/ClassLayout;
    INVOKEVIRTUAL org/openjdk/jol/info/ClassLayout.toPrintable ()Ljava/lang/String;
    INVOKEVIRTUAL java/io/PrintStream.println (Ljava/lang/String;)V
   L2
    LINENUMBER 58 L2
    RETURN
   L3
    LOCALVARIABLE this Lnet/indiespot/struct/runtime/TestFromPointer; L0 L3 0
    MAXSTACK = 2
    MAXLOCALS = 1

  // access flags 0x1
  public testFromPointer()V
  @Lorg/junit/Test;()
   L0
    LINENUMBER 62 L0
    SIPUSH 234
    INVOKESTATIC java/nio/ByteBuffer.allocateDirect (I)Ljava/nio/ByteBuffer;
    ASTORE 1
   L1
    LINENUMBER 64 L1
    ALOAD 1
    INVOKESTATIC net/indiespot/struct/api/runtime/StructUnsafe.getBufferBaseAddress (Ljava/nio/ByteBuffer;)J
    LSTORE 2
   L2
    LINENUMBER 65 L2
    ALOAD 1
    CHECKCAST sun/nio/ch/DirectBuffer
    INVOKEINTERFACE sun/nio/ch/DirectBuffer.address ()J
    LSTORE 4
   L3
    LINENUMBER 67 L3
    LLOAD 2
    LLOAD 4
    INVOKESTATIC org/junit/Assert.assertEquals (JJ)V
   L4
    LINENUMBER 70 L4
    SIPUSH 12
    I2L
    LSTORE 6
   L5
    LINENUMBER 72 L5
    LLOAD 2
    LCONST_0
    LADD
    LSTORE 8
   L6
    LINENUMBER 73 L6
    LLOAD 2
    LCONST_1
    LLOAD 6
    LMUL
    LADD
    LSTORE 10
   L7
    LINENUMBER 75 L7
    LLOAD 8
    INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory.pointer2handle (J)I
    ISTORE 12
   L8
    LINENUMBER 76 L8
    LLOAD 10
    INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory.pointer2handle (J)I
    ISTORE 13
   L9
    LINENUMBER 79 L9
    LLOAD 8
    ILOAD 12
    INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory.handle2pointer (I)J
    INVOKESTATIC org/junit/Assert.assertEquals (JJ)V
   L10
    LINENUMBER 80 L10
    LLOAD 10
    ILOAD 13
    INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory.handle2pointer (I)J
    INVOKESTATIC org/junit/Assert.assertEquals (JJ)V
   L11
    LINENUMBER 82 L11
    LLOAD 2
    SIPUSH 12
    ICONST_2
    INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory.pointer2handles (JII)[I
    ASTORE 14
   L12
    LINENUMBER 84 L12
    ALOAD 14
    ICONST_0
    IALOAD
    ILOAD 12
    INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory.toString (I)Ljava/lang/String;
    SWAP
    INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory.toString (I)Ljava/lang/String;
    SWAP
    INVOKESTATIC org/junit/Assert.assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
   L13
    LINENUMBER 85 L13
    ALOAD 14
    ICONST_1
    IALOAD
    ILOAD 13
    INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory.toString (I)Ljava/lang/String;
    SWAP
    INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory.toString (I)Ljava/lang/String;
    SWAP
    INVOKESTATIC org/junit/Assert.assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
   L14
    LINENUMBER 87 L14
    LLOAD 2
    BIPUSH 12
    ICONST_2
    INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory.pointer2handles (JII)[I
    ASTORE 14
   L15
    LINENUMBER 88 L15
    ILOAD 12
    ALOAD 14
    ICONST_0
    IALOAD
    INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory.toString (I)Ljava/lang/String;
    SWAP
    INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory.toString (I)Ljava/lang/String;
    SWAP
    INVOKESTATIC org/junit/Assert.assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
   L16
    LINENUMBER 89 L16
    ILOAD 13
    ALOAD 14
    ICONST_1
    IALOAD
    INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory.toString (I)Ljava/lang/String;
    SWAP
    INVOKESTATIC net/indiespot/struct/api/runtime/StructMemory.toString (I)Ljava/lang/String;
    SWAP
    INVOKESTATIC org/junit/Assert.assertEquals (Ljava/lang/Object;Ljava/lang/Object;)V
   L17
    LINENUMBER 91 L17
    ALOAD 1
    INVOKEVIRTUAL java/nio/ByteBuffer.clear ()Ljava/nio/Buffer;
    POP
   L18
    LINENUMBER 92 L18
    RETURN
   L19
    LOCALVARIABLE this Lnet/indiespot/struct/runtime/TestFromPointer; L0 L19 0
    LOCALVARIABLE bb Ljava/nio/ByteBuffer; L1 L19 1
    LOCALVARIABLE addr J L2 L19 2
    LOCALVARIABLE addr2 J L3 L19 4
    LOCALVARIABLE vec3Size J L5 L19 6
    LOCALVARIABLE v1Ptr J L6 L19 8
    LOCALVARIABLE v2Ptr J L7 L19 10
    LOCALVARIABLE v1 Lnet/indiespot/struct/testlib/Vec3; L8 L19 12
    LOCALVARIABLE v2 Lnet/indiespot/struct/testlib/Vec3; L9 L19 13
    LOCALVARIABLE vs [Lnet/indiespot/struct/testlib/Vec3; L12 L19 14
    MAXSTACK = 6
    MAXLOCALS = 15

  // access flags 0x8
  static <clinit>()V
   L0
    LINENUMBER 20 L0
    LDC Lnet/indiespot/struct/runtime/TestFromPointer;.class
    INVOKEVIRTUAL java/lang/Class.desiredAssertionStatus ()Z
    IFNE L1
    ICONST_1
    GOTO L2
   L1
   FRAME SAME
    ICONST_0
   L2
   FRAME SAME1 I
    PUTSTATIC net/indiespot/struct/runtime/TestFromPointer.$assertionsDisabled : Z
    RETURN
    MAXSTACK = 1
    MAXLOCALS = 0
}
}
 */
