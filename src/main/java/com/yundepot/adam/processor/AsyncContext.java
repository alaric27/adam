package com.yundepot.adam.processor;

import java.util.Map;

/**
 * @author zhaiyanan
 * @date 2019/5/23 21:11
 */
public interface AsyncContext {

    /**
     * 发送响应
     * @param response
     */
    void sendResponse(Object response, Map<String, String> header);
}
