package com.alibaba.fescar.server.store.buffer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Use ThreadLocal as our buffer pool
 * @author 563868273@qq.com
 * @date 2019 /03/30
 */
public class ThreadByteBufferPool implements ByteBufferPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadByteBufferPool.class);

    private int maxDirectBufferSize;

    private int maxHeapBufferSize;

    private ThreadLocal<ThreadByteBuffer> directByteBuffer;

    private ThreadLocal<ThreadByteBuffer> heapByteBuffer;

    public ThreadByteBufferPool(int maxDirectBufferSize, int maxHeapBufferSize) {
        this.maxDirectBufferSize = maxDirectBufferSize;
        this.maxHeapBufferSize = maxHeapBufferSize;

        directByteBuffer = ThreadLocal.withInitial(() -> new ThreadByteBuffer(ByteBuffer.allocateDirect(maxDirectBufferSize), false));

        heapByteBuffer = ThreadLocal.withInitial(() -> new ThreadByteBuffer(ByteBuffer.allocateDirect(maxHeapBufferSize), false));
    }

    @Override
    public ByteBuffer borrowDirectBuffer(int bufferSize) {
        if (bufferSize > maxDirectBufferSize) {
            LOGGER.error("direct buffer size exceeded, max direct buffer size : " + maxDirectBufferSize + " borrow size : " + bufferSize);
            throw new RuntimeException("direct buffer size exceeded");
        }
        ThreadByteBuffer threadByteBuffer = directByteBuffer.get();
        if (threadByteBuffer.inUsing){
            throw new RuntimeException("in the same thread, cannot borrow direct buffer which is in using");
        }

        return threadByteBuffer.useByteBuffer();
    }

    @Override
    public ByteBuffer borrowHeapBuffer(int bufferSize) {
        if (bufferSize > maxHeapBufferSize) {
            LOGGER.error("heap buffer size exceeded, max heap buffer size : " + maxHeapBufferSize + " borrow size : " + bufferSize);
            throw new RuntimeException("direct buffer size exceeded");
        }
        ThreadByteBuffer threadByteBuffer = heapByteBuffer.get();
        if (threadByteBuffer.inUsing){
            throw new RuntimeException("in the same thread, cannot borrow heap buffer which is in using");
        }

        return threadByteBuffer.useByteBuffer();
    }

    @Override
    public void returnDirectBuffer(ByteBuffer byteBuffer) {
        ThreadByteBuffer threadByteBuffer = directByteBuffer.get();
        if (!threadByteBuffer.byteBuffer.equals(byteBuffer)){
            throw new RuntimeException("cannot return a direct buffer that does not belong to the threadLocal");
        }
        threadByteBuffer.recycle();
    }

    @Override
    public void returnHeapBuffer(ByteBuffer byteBuffer) {
        ThreadByteBuffer threadByteBuffer = heapByteBuffer.get();
        if (!threadByteBuffer.byteBuffer.equals(byteBuffer)){
            throw new RuntimeException("cannot return a heap buffer that does not belong to the threadLocal");
        }
        threadByteBuffer.recycle();
    }

    class ThreadByteBuffer {

        private ByteBuffer byteBuffer;

        private boolean inUsing;

        public ThreadByteBuffer(ByteBuffer byteBuffer, boolean inUsing) {
            this.byteBuffer = byteBuffer;
            this.inUsing = inUsing;
        }

        public ByteBuffer useByteBuffer() {
            inUsing = true;
            return byteBuffer;
        }

        public void recycle() {
            byteBuffer.clear();
            inUsing = false;
        }
    }
}
