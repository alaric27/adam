package com.yundepot.adam.string;


import com.yundepot.adam.processor.SyncProcessor;
import com.yundepot.adam.quickstart.MyRequest;
import com.yundepot.adam.quickstart.MyResponse;
import com.yundepot.oaa.invoke.InvokeContext;

import java.util.Map;

/**
 * @author zhaiyanan
 * @date 2019/5/10 16:10
 */
public class MyStringProcessor extends SyncProcessor<String> {

    @Override
    public Object handleRequest(InvokeContext invokeContext, String myRequest) throws Exception {
        System.out.println(myRequest);
        return "hhhhhh";
    }

    @Override
    public String interest() {
        return "/rpc";
    }
}
