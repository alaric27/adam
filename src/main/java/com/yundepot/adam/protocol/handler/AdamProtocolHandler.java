package com.yundepot.adam.protocol.handler;

import com.yundepot.adam.protocol.command.*;
import com.yundepot.oaa.invoke.InvokeContext;
import com.yundepot.oaa.protocol.command.CommandFactory;
import com.yundepot.oaa.protocol.command.CommandType;
import com.yundepot.oaa.protocol.handler.AbstractProtocolHandler;
import io.netty.channel.ChannelHandler.Sharable;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaiyanan
 * @date 2019/5/14 17:13
 */
@Slf4j
@Sharable
public class AdamProtocolHandler extends AbstractProtocolHandler {

    private CommandFactory commandFactory;

    public AdamProtocolHandler(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
        //注册请求处理器
        registerCommandProcessor(AdamCommandCode.REQUEST.value(), new RequestCommandProcessor(this.commandFactory));
        //注册响应处理器
        registerCommandProcessor(AdamCommandCode.RESPONSE.value(), new ResponseCommandProcessor());
        //注册心跳请求处理器
        registerCommandProcessor(AdamCommandCode.HEARTBEAT.value(), new HeartBeatCommandProcessor());
    }


    @Override
    protected void processCommandException(InvokeContext ctx, Object msg, Throwable t) {
        if (msg instanceof RequestCommand) {
            final RequestCommand cmd = (RequestCommand) msg;
            if (cmd.getCommandType() != CommandType.ONE_WAY.value()) {
                final ResponseCommand response = this.commandFactory.createExceptionResponse(cmd, t, null);
                if (cmd.getCommandCode() == AdamCommandCode.HEARTBEAT.value()) {
                    response.setCommandCode(AdamCommandCode.HEARTBEAT.value());
                }
                ctx.getChannelHandlerContext().writeAndFlush(response).addListener(future -> {
                    if (!future.isSuccess()) {
                        final int id = cmd.getId();
                        log.error("Write back exception response failed, requestId={}", id, future.cause());
                    }
                });
            }
        }
    }
}
