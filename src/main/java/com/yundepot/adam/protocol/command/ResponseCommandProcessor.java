package com.yundepot.adam.protocol.command;

import com.yundepot.oaa.connection.Connection;
import com.yundepot.oaa.invoke.InvokeContext;
import com.yundepot.oaa.invoke.InvokeFuture;
import com.yundepot.oaa.protocol.command.AbstractCommandProcessor;
import com.yundepot.oaa.protocol.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhaiyanan
 * @date 2019/6/27 09:13
 */
public class ResponseCommandProcessor extends AbstractCommandProcessor<Command> {

    private static final Logger logger = LoggerFactory.getLogger(ResponseCommandProcessor.class);

    @Override
    public void doProcess(InvokeContext ctx, Command cmd) {
        Connection conn = ctx.getChannelHandlerContext().channel().attr(Connection.CONNECTION).get();
        InvokeFuture future = conn.removeInvokeFuture(cmd.getId());
        try {
            if (future != null) {
                future.putResponse(cmd);
                future.cancelTimeout();
                future.executeInvokeCallback();
            }
        } catch (Throwable e) {
            logger.error("Exception when invoke callback, id={}", cmd.getId(), e);
        }

    }

}
