package com.yundepot.adam.customize;

import com.yundepot.adam.common.ResponseStatus;
import com.yundepot.adam.protocol.command.RequestCommand;
import com.yundepot.adam.protocol.command.ResponseCommand;
import com.yundepot.adam.quickstart.MyRequest;
import com.yundepot.oaa.invoke.InvokeContext;
import com.yundepot.oaa.protocol.command.AbstractCommandProcessor;

/**
 * @author zhaiyanan
 * @date 2022/6/16  10:22
 */
public class EchoCommandProcessor extends AbstractCommandProcessor<RequestCommand> {
    @Override
    protected void doProcess(InvokeContext ctx, RequestCommand msg) {
        MyRequest request = (MyRequest) msg.getBody();
        ResponseCommand response = new ResponseCommand(msg.getId(), request);
        response.setProtocolCode(msg.getProtocolCode());
        response.setStatus(ResponseStatus.SUCCESS.value());
        response.setSerializer(msg.getSerializer());
        ctx.writeAndFlush(response).addListener(future -> {
            if (!future.isSuccess()) {
                future.cause().printStackTrace();
            }
        });
    }
}
