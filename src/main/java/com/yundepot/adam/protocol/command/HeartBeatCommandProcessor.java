package com.yundepot.adam.protocol.command;

import com.yundepot.adam.common.ResponseStatus;
import com.yundepot.oaa.connection.Connection;
import com.yundepot.oaa.invoke.InvokeContext;
import com.yundepot.oaa.invoke.InvokeFuture;
import com.yundepot.oaa.protocol.command.AbstractCommandProcessor;
import com.yundepot.oaa.protocol.command.Command;
import com.yundepot.oaa.protocol.command.CommandType;
import com.yundepot.oaa.util.RemotingUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaiyanan
 * @date 2019/6/12 18:42
 */
@Slf4j
public class HeartBeatCommandProcessor extends AbstractCommandProcessor<Command> {

    @Override
    public void doProcess(InvokeContext ctx, Command command) {
        if (command.getCommandType() == CommandType.REQUEST.value()) {
            final int id = command.getId();
            ResponseCommand ack = new ResponseCommand();
            ack.setCommandCode(AdamCommandCode.HEARTBEAT.value());
            ack.setId(id);
            ack.setStatus(ResponseStatus.SUCCESS.value());
            ack.setProtocolCode(command.getProtocolCode());
            ctx.writeAndFlush(ack).addListener(future -> {
                if (!future.isSuccess()) {
                    log.error("Send heartbeat ack failed! Id={}, to remoteAddr={}", id,
                            RemotingUtil.parseRemoteAddress(ctx.getChannelHandlerContext().channel()));
                }
            });
        } else if (command.getCommandType() == CommandType.RESPONSE.value()) {
            Connection conn = ctx.getChannelHandlerContext().channel().attr(Connection.CONNECTION).get();
            InvokeFuture future = conn.removeInvokeFuture(command.getId());
            try {
                if (future != null) {
                    future.putResponse(command);
                    future.cancelTimeout();
                    future.executeInvokeCallback();
                }
            } catch (Throwable e) {
                String address = RemotingUtil.parseRemoteAddress(ctx.getChannelHandlerContext().channel());
                log.error("Exception caught when heartbeat invoke callback. address {}", address, e);
            }
        } else {
            throw new RuntimeException("Cannot process command: " + command.getClass().getName());
        }
    }
}
