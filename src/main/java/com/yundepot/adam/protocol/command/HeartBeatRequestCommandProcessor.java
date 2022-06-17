package com.yundepot.adam.protocol.command;

import com.yundepot.oaa.common.ResponseStatus;
import com.yundepot.oaa.invoke.InvokeContext;
import com.yundepot.oaa.protocol.command.AbstractCommandProcessor;
import com.yundepot.oaa.util.RemotingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhaiyanan
 * @date 2019/6/12 18:42
 */
public class HeartBeatRequestCommandProcessor extends AbstractCommandProcessor<RequestCommand> {
    private static final Logger logger = LoggerFactory.getLogger(HeartBeatRequestCommandProcessor.class);

    @Override
    public void doProcess(InvokeContext ctx, RequestCommand msg) {
        final int id = msg.getId();
        ResponseCommand ack = new ResponseCommand();
        ack.setCommandCode(AdamCommandCode.HEARTBEAT_RESPONSE.value());
        ack.setId(id);
        ack.setResponseStatus(ResponseStatus.SUCCESS);
        ack.setProtocolCode(msg.getProtocolCode());

        ctx.writeAndFlush(ack).addListener(future -> {
            if (!future.isSuccess()) {
                logger.error("Send heartbeat ack failed! Id={}, to remoteAddr={}", id,
                        RemotingUtil.parseRemoteAddress(ctx.getChannelHandlerContext().channel()));
            }
        });
    }
}
