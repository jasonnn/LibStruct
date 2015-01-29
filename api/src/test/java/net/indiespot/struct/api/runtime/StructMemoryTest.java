package net.indiespot.struct.api.runtime;

import org.junit.Test;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;

public class StructMemoryTest {
    @Test
    public void testThreadLocalStack() throws Exception {
        StructAllocationStack stack = StructThreadLocalStack.saveStack();
        int vecHandle = StructMemory.allocate(12, stack);

        float x = StructMemory.fget(vecHandle, 0);
        float y = StructMemory.fget(vecHandle, 4);
        float z = StructMemory.fget(vecHandle, 8);

        assertEquals(0, x, 0);
        assertEquals(0, y, 0);
        assertEquals(0, z, 0);

        stack.restore();

    }

    @Test
    public void testPointer2Handle() throws Exception {
        ByteBuffer bb = ByteBuffer.allocateDirect(234).order(ByteOrder.nativeOrder());

        long addr = StructUnsafe.getBufferBaseAddress(bb);
        long addr2 = ((DirectBuffer) bb).address();

        assertEquals(addr, addr2);


        int handle = StructMemory.pointer2handle(addr);

        long ptr = StructMemory.handle2pointer(handle);

        assertEquals(addr, ptr);

    }


}
