package com.yundepot.adam.protocol.command;

import com.yundepot.adam.config.HeaderOption;
import com.yundepot.adam.processor.Processor;
import com.yundepot.adam.processor.ProcessorManager;
import com.yundepot.oaa.invoke.InvokeContext;
import com.yundepot.oaa.protocol.command.AbstractCommandProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhaiyanan
 * @date 2019/6/12 13:49
 */
public class OneWayCommandProcessor extends AbstractCommandProcessor<RequestCommand> {

    private static final Logger logger = LoggerFactory.getLogger(OneWayCommandProcessor.class);

    @Override
    public void doProcess(InvokeContext ctx, RequestCommand cmd) {
        Processor processor = ProcessorManager.getProcessor(cmd.getNri());
        if (processor == null) {
            logger.error("No processor found for request: " + cmd.getNri());
            return;
        }

        ctx.setTimeoutDiscard(processor.timeoutDiscard());
        ctx.setArriveTimestamp(cmd.getArriveTime());
        ctx.setTimeout(Integer.valueOf(cmd.getHeader(HeaderOption.REQUEST_TIMEOUT.getKey(), HeaderOption.REQUEST_TIMEOUT.getDefaultValue())));
        if (ctx.isTimeoutDiscard() && ctx.isRequestTimeout()) {
            logger.debug("request id [{}] time out discard, cost [{}]", cmd.getId(), System.currentTimeMillis() - cmd.getArriveTime());
            return;
        }
        // 分发到用户处理器
        dispatchToUserProcessor(ctx, cmd);
    }

    private void dispatchToUserProcessor(InvokeContext ctx, RequestCommand cmd) {
        final int id = cmd.getId();
        try {
            Processor processor = ProcessorManager.getProcessor(cmd.getNri());
            processor.handleRequest(ctx, cmd.getBody());
        } catch (Throwable t) {
            String errMsg = "one way process request failed in RequestCommandProcessor, id=" + id;
            logger.error(errMsg, t);
        }
    }
}
