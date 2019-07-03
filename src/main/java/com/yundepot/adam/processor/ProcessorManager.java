package com.yundepot.adam.processor;

import com.yundepot.oaa.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 处理器管理类
 * @author zhaiyanan
 * @date 2019/5/23 14:20
 */
public class ProcessorManager {

    private static final ConcurrentHashMap<String, Processor<?>> processors = new ConcurrentHashMap<>();

    /**
     * 注册处理器
     * @param processor
     */
    public static void registerProcessor(Processor<?> processor) {

        if (null == processor) {
            throw new RuntimeException("processor should not be null!");
        }

        if (StringUtils.isBlank(processor.interest())) {
            throw new RuntimeException("Processor interest should not be blank!");
        }
        Processor<?> preProcessor = processors.putIfAbsent(processor.interest(), processor);
        if (preProcessor != null) {
            throw new RuntimeException("interest key " +  processor.interest() + " has already been registered");
        }
    }

    /**
     * 获取处理器
     * @param interest
     * @return
     */
    public static Processor<?> getProcessor(String interest) {
        return StringUtils.isBlank(interest) ? null : processors.get(interest);
    }


}
