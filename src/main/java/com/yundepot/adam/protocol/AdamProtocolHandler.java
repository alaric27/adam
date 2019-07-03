package com.yundepot.adam.protocol;

import com.yundepot.adam.protocol.command.*;
import com.yundepot.oaa.invoke.InvokeContext;
import com.yundepot.oaa.protocol.AbstractProtocolHandler;
import com.yundepot.oaa.protocol.command.CommandFactory;
import io.netty.channel.ChannelHandler.Sharable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhaiyanan
 * @date 2019/5/14 17:13
 */
@Sharable
public class AdamProtocolHandler extends AbstractProtocolHandler {

    private static final Logger logger = LoggerFactory.getLogger(AdamProtocolHandler.class);

    private CommandFactory commandFactory;

    public AdamProtocolHandler(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
        //注册请求处理器
        registerCommandProcessor(AdamCommandCode.REQUEST, new RequestCommandProcessor(this.commandFactory));
        //注册响应处理器
        registerCommandProcessor(AdamCommandCode.RESPONSE, new ResponseCommandProcessor());
        //注册心跳请求处理器
        registerCommandProcessor(AdamCommandCode.HEARTBEAT_REQUEST, new HeartBeatRequestCommandProcessor());
        //注册心跳响应处理器
        registerCommandProcessor(AdamCommandCode.HEARTBEAT_RESPONSE, new HeartBeatResponseCommandProcessor());
        //注册one way处理器
        registerCommandProcessor(AdamCommandCode.ONE_WAY, new OneWayCommandProcessor());
    }


    @Override
    protected void processCommandException(InvokeContext ctx, Object msg, Throwable t) {
        if (msg instanceof RequestCommand) {
            final RequestCommand cmd = (RequestCommand) msg;
            if (cmd.getCommandCode().value() != AdamCommandCode.ONE_WAY.value()) {
                final ResponseCommand response = this.commandFactory.createExceptionResponse(cmd.getId(), t, null);
                ctx.getChannelHandlerContext().writeAndFlush(response).addListener(future -> {
                    if (!future.isSuccess()) {
                        final int id = cmd.getId();
                        logger.error("Write back exception response failed, requestId={}", id, future.cause());
                    }
                });
            }
        }
    }
}
