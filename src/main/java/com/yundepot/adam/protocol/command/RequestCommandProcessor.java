package com.yundepot.adam.protocol.command;

import com.yundepot.adam.processor.AdamAsyncContext;
import com.yundepot.adam.processor.AsyncProcessor;
import com.yundepot.adam.processor.Processor;
import com.yundepot.adam.processor.ProcessorManager;
import com.yundepot.oaa.invoke.InvokeContext;
import com.yundepot.oaa.protocol.command.AbstractCommandProcessor;
import com.yundepot.oaa.protocol.command.Command;
import com.yundepot.oaa.protocol.command.CommandFactory;
import com.yundepot.oaa.util.RemotingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * @author zhaiyanan
 * @date 2019/5/10 16:10
 */
public class RequestCommandProcessor extends AbstractCommandProcessor<RequestCommand> {

    private static final Logger logger = LoggerFactory.getLogger(RequestCommandProcessor.class);

    public RequestCommandProcessor(CommandFactory commandFactory) {
        super(commandFactory);
    }

    @Override
    public void doProcess(final InvokeContext ctx, RequestCommand cmd) {
        Processor processor = ProcessorManager.getProcessor(cmd.getClassName());
        if (processor == null) {
            String errMsg = "No processor found for request: " + cmd.getClassName();
            logger.error(errMsg);
            sendResponse(ctx, this.getCommandFactory().createExceptionResponse(cmd.getId(), null, errMsg));
            return;
        }

        ctx.setTimeoutDiscard(processor.timeoutDiscard());
        ctx.setArriveTimestamp(cmd.getArriveTime());
        ctx.setTimeout(cmd.getTimeout());
        if (ctx.isTimeoutDiscard() && ctx.isRequestTimeout()) {
            logger.debug("request id [{}] time out discard, cost [{}]", cmd.getId(), System.currentTimeMillis() - cmd.getArriveTime());
            return;
        }

        // 如果Processor设置了线程池则使用自己的线程池，如果没有设置，则在CommandProcessor的线程中执行
        Executor executor = processor.getExecutor();
        if (executor != null) {
            executor.execute(() -> dispatchToUserProcessor(ctx, cmd));
        } else {
            dispatchToUserProcessor(ctx, cmd);
        }
    }

    @Override
    protected void processException(InvokeContext ctx, RequestCommand command, Throwable e) {
        final ResponseCommand response = getCommandFactory().createExceptionResponse(command.getId(), e, null);
        ctx.getChannelHandlerContext().writeAndFlush(response).addListener(future -> {
            if (!future.isSuccess()) {
                final int id = command.getId();
                logger.error("Write back exception response failed, requestId={}", id, future.cause());
            }
        });
    }

    /**
     * 发送响应
     * @param ctx
     * @param response
     */
    public void sendResponse(final InvokeContext ctx, final Command response) {
        final int id = response.getId();
        ctx.writeAndFlush(response).addListener(future -> {
            if (!future.isSuccess()) {
                logger.error("response send failed,id={},address={}", id,
                        RemotingUtil.parseRemoteAddress(ctx.getChannelHandlerContext().channel()), future.cause());
            }
        });
    }


    /**
     * 分发到用户处理器
     * @param ctx
     * @param cmd
     */
    private void dispatchToUserProcessor(InvokeContext ctx, RequestCommand cmd) {
        final int id = cmd.getId();
        ctx.setAttachment(cmd.getHeader());
        Processor processor = ProcessorManager.getProcessor(cmd.getClassName());
        if (processor instanceof AsyncProcessor) {
            // 在业务线程中处理，在业务线程中发送响应结果
            try {
                processor.handleRequest(ctx, new AdamAsyncContext(ctx, cmd, this), cmd.getContent());
            } catch (Throwable t) {
                String errMsg = "ASYNC process request failed in RequestCommandProcessor, id=" + id;
                logger.error(errMsg, t);
                sendResponse(ctx, this.getCommandFactory().createExceptionResponse(id, t, errMsg));
            }
        } else {
            // 在当前Processor线程中处理
            try {
                Object responseObject = processor.handleRequest(ctx, cmd.getContent());
                sendResponse(ctx, this.getCommandFactory().createResponse(cmd, responseObject));
            } catch (Throwable t) {
                String errMsg = "SYNC process request failed in RequestCommandProcessor, id=" + id;
                logger.error(errMsg, t);
                sendResponse(ctx, this.getCommandFactory().createExceptionResponse(id, t, errMsg));
            }
        }
    }
}
