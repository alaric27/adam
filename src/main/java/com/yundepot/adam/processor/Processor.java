package com.yundepot.adam.processor;

import com.yundepot.oaa.invoke.InvokeContext;
import java.util.concurrent.Executor;

/**
 * @author zhaiyanan
 * @date 2019/5/23 21:15
 */
public interface Processor<T> {

    /**
     * 异步处理
     * @param invokeContext
     * @param asyncCtx
     * @param request
     */
    void handleRequest(InvokeContext invokeContext, AsyncContext asyncCtx, T request);

    /**
     * 同步处理
     * @param invokeContext
     * @param request
     * @return
     * @throws Exception
     */
    Object handleRequest(InvokeContext invokeContext, T request) throws Exception;

    /**
     * 感兴趣的事件
     * @return
     */
    String interest();

    /**
     * 获取线程执行器
     * @return
     */
    Executor getExecutor();

    /**
     * 是否超时丢弃
     * @return
     */
    boolean timeoutDiscard();
}
