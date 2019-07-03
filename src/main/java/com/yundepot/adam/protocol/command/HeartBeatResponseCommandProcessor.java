package com.yundepot.adam.protocol.command;

import com.yundepot.oaa.connection.Connection;
import com.yundepot.oaa.invoke.InvokeContext;
import com.yundepot.oaa.invoke.InvokeFuture;
import com.yundepot.oaa.protocol.command.AbstractCommandProcessor;
import com.yundepot.oaa.util.RemotingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhaiyanan
 * @date 2019/6/12 18:46
 */
public class HeartBeatResponseCommandProcessor extends AbstractCommandProcessor<ResponseCommand> {
    private static final Logger logger = LoggerFactory.getLogger(HeartBeatResponseCommandProcessor.class);

    @Override
    public void doProcess(InvokeContext ctx, ResponseCommand msg) {
        Connection conn = ctx.getChannelHandlerContext().channel().attr(Connection.CONNECTION).get();
        InvokeFuture future = conn.removeInvokeFuture(msg.getId());

        try {
            if (future != null) {
                future.putResponse(msg);
                future.cancelTimeout();
                future.executeInvokeCallback();
            }
        } catch (Throwable e) {
            String address = RemotingUtil.parseRemoteAddress(ctx.getChannelHandlerContext().channel());
            logger.error("Exception caught when heartbeat invoke callback. address {}", address, e);
        }
    }
}
