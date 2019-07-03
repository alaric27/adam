package com.yundepot.adam.processor;


import java.util.concurrent.Executor;

/**
 *
 * @author zhaiyanan
 * @date 2019/5/10 16:15
 */
public abstract class AbstractProcessor<T> implements Processor<T> {

    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public boolean timeoutDiscard() {
        return true;
    }
}