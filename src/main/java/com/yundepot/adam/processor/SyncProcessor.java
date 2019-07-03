package com.yundepot.adam.processor;

import com.yundepot.oaa.invoke.InvokeContext;

/**
 * @author zhaiyanan
 * @date 2019/5/23 21:14
 */
public abstract class SyncProcessor<T> extends AbstractProcessor<T> {

    @Override
    public void handleRequest(InvokeContext invokeContext, AsyncContext asyncCtx, T request) {
        throw new UnsupportedOperationException("async request is unsupported in SyncProcessor!");
    }
}
