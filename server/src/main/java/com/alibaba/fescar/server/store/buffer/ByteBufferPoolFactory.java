package com.alibaba.fescar.server.store.buffer;


import com.alibaba.fescar.config.Configuration;
import com.alibaba.fescar.config.ConfigurationFactory;

import java.nio.ByteBuffer;

/**
 * Created by lz on 2019/3/30.
 */
public final class ByteBufferPoolFactory {

    public static final String STORE_FILEKEY_PREFIX = "store.";

    public static final int DEFAULT_BUFFER_SIZE = 4096;

    public static final ByteBufferPool THREAD_BYTEBUFFER_POOL;

    static {
        Configuration configuration = ConfigurationFactory.getInstance();
        int maxThreadDirectByteBufferSize = configuration.getInt(STORE_FILEKEY_PREFIX + "max-thread-direct-bytebuffer-size", DEFAULT_BUFFER_SIZE);
        int maxThreadHeapByteBufferSize = configuration.getInt(STORE_FILEKEY_PREFIX + "max-thread-heap-bytebuffer-size", DEFAULT_BUFFER_SIZE);
        THREAD_BYTEBUFFER_POOL = new ThreadByteBufferPool(maxThreadDirectByteBufferSize, maxThreadHeapByteBufferSize);
    }
}
