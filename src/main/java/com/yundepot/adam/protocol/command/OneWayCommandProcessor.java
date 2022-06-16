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
        String processorId = cmd.getHeader(HeaderOption.PROCESSOR.getKey());
        Processor processor = ProcessorManager.getProcessor(processorId);
        if (processor == null) {
            logger.error("No processor found for request: " + processorId);
            return;
        }
        ctx.setArriveTimestamp(cmd.getArriveTime());
        // 分发到用户处理器
        dispatchToUserProcessor(ctx, cmd);
    }

    private void dispatchToUserProcessor(InvokeContext ctx, RequestCommand cmd) {
        final int id = cmd.getId();
        try {
            String processorId = cmd.getHeader(HeaderOption.PROCESSOR.getKey());
            Processor processor = ProcessorManager.getProcessor(processorId);
            processor.handleRequest(ctx, cmd.getBody());
        } catch (Throwable t) {
            String errMsg = "one way process request failed in RequestCommandProcessor, id=" + id;
            logger.error(errMsg, t);
        }
    }
}
