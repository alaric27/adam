package com.yundepot.adam.processor;

import com.yundepot.oaa.invoke.InvokeContext;

/**
 * @author zhaiyanan
 * @date 2019/5/23 21:13
 */
public abstract class AsyncProcessor<T> extends AbstractProcessor<T> {

    @Override
    public Object handleRequest(InvokeContext invokeContext, T request) throws Exception {
        throw new UnsupportedOperationException("sync request is unsupported in AsyncProcessor!");
    }
}
