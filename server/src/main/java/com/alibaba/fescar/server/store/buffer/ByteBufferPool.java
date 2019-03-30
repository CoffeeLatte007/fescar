/*
 *  Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.alibaba.fescar.server.store.buffer;

import java.nio.ByteBuffer;

/**
 * @author 563868273@qq.com
 * @date 2019 /03/30
 */
public interface ByteBufferPool {

    /**
     *  borrow DirectByteBuffer from the poll
     * @param bufferSize
     * @return the ByteBuffer
     */
    ByteBuffer borrowDirectBuffer(int bufferSize);

    /**
     *  borrow HeapByteBuffer from the poll
     * @param bufferSize
     * @return the ByteBuffer
     */
    ByteBuffer borrowHeapBuffer(int bufferSize);

    void returnDirectBuffer(ByteBuffer byteBuffer);

    void returnHeapBuffer(ByteBuffer byteBuffer);
}
