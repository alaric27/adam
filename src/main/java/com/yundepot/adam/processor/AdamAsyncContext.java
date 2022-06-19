package com.yundepot.adam.processor;


import com.yundepot.adam.protocol.command.RequestCommand;
import com.yundepot.adam.protocol.command.RequestCommandProcessor;
import com.yundepot.adam.protocol.command.ResponseCommand;
import com.yundepot.oaa.invoke.InvokeContext;

import java.util.Map;

/**
 * @author zhaiyanan
 * @date 2019/5/23 21:14
 */
public class AdamAsyncContext implements AsyncContext {

    private InvokeContext ctx;
    private RequestCommand cmd;
    private RequestCommandProcessor processor;

    public AdamAsyncContext(final InvokeContext ctx, final RequestCommand cmd, final RequestCommandProcessor processor) {
        this.ctx = ctx;
        this.cmd = cmd;
        this.processor = processor;
    }

    @Override
    public void sendResponse(Object responseObject, Map<String, String> header) {
        ResponseCommand response = processor.getCommandFactory().createResponse(cmd, responseObject);
        response.setHeader(header);
        processor.sendResponse(this.ctx, response);
    }
}