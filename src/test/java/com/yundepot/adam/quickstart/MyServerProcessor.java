package com.yundepot.adam.quickstart;


import com.yundepot.adam.processor.SyncProcessor;
import com.yundepot.oaa.invoke.InvokeContext;

import java.util.Map;
import java.util.Optional;

/**
 * @author zhaiyanan
 * @date 2019/5/10 16:10
 */
public class MyServerProcessor extends SyncProcessor<MyRequest> {

    @Override
    public Object handleRequest(InvokeContext invokeContext, MyRequest myRequest) throws Exception {
        Map<String, String> map = invokeContext.getAttachment();
        MyResponse response = new MyResponse();
        if (myRequest != null) {
            System.out.println(myRequest);
            response.setResp("from server -> " + myRequest.getReq());
        }
        return response;
    }

    @Override
    public String interest() {
        return MyRequest.class.getName();
    }
}
