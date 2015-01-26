package net.indiespot.struct.api.runtime;

import net.indiespot.struct.api.StructConfig;
import net.indiespot.struct.api.runtime.StructGC.IntList;

import java.nio.ByteBuffer;

public class StructHeap {

    final ByteBuffer buffer;

    private final StructAllocationBlock block;
    private int allocCount, freeCount;
    private IntList activeHandles;

    public StructHeap(ByteBuffer buffer) {
        long addr = StructMemory.alignBufferToWord(buffer);
        int handleOffset = StructMemory.pointer2handle(addr);

        this.buffer = buffer;
        this.block = new StructAllocationBlock(handleOffset, buffer.remaining());

        if (StructConfig.SAFETY_FIRST) {
            this.activeHandles = new IntList();
        }
    }

    public int malloc(int sizeof) {
        if (block.canAllocate(sizeof)) {
            int handle = block.allocate(sizeof);
            if (StructConfig.SAFETY_FIRST) {
                if (activeHandles.contains(handle))
                    throw new IllegalStateException();
                activeHandles.add(handle);
            }
            allocCount++;
            return handle;
        }
        return 0;
    }

    public int malloc(int sizeof, int length) {
        if (block.canAllocate(sizeof * length)) {
            int offset = block.allocate(sizeof * length);
            if (StructConfig.SAFETY_FIRST) {
                int words = StructMemory.bytes2words(sizeof);
                for (int i = 0; i < length; i++) {
                    int handle = offset + i * words;
                    if (activeHandles.contains(handle))
                        throw new IllegalStateException();
                    activeHandles.add(handle);
                }
            }
            allocCount += length;
            return offset;
        }
        return 0;
    }

    public boolean freeHandle(int handle) {
        if (this.isOnHeap(handle)) {
            if (StructConfig.SAFETY_FIRST) {
                if (activeHandles.keepValue(handle))
                    throw new IllegalStateException();
            }
            if (++freeCount == allocCount) {
                allocCount = 0;
                freeCount = 0;
                block.wordsAllocated = 0;
            }
            return true;
        }
        return false;
    }

    public boolean isOnHeap(int handle) {
        return block.isOnBlock(handle);
    }

    public boolean isEmpty() {
        boolean isEmpty = (allocCount == freeCount);
        if (StructConfig.SAFETY_FIRST)
            if (isEmpty != activeHandles.isEmpty())
                throw new IllegalStateException();
        return isEmpty;
    }

    public int getHandleCount() {
        int count = (allocCount - freeCount);
        if (StructConfig.SAFETY_FIRST)
            if (count != activeHandles.size())
                throw new IllegalStateException();
        return count;
    }

    @Override
    public String toString() {
        return StructHeap.class.getSimpleName() + "[alloc=" + allocCount + ", free=" + freeCount + ", buffer=" + buffer + ']';
    }
}
