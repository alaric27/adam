package com.yundepot.adam.protocol.command;

import com.yundepot.oaa.common.ResponseStatus;
import com.yundepot.oaa.exception.ServerException;
import com.yundepot.oaa.protocol.command.Command;
import com.yundepot.oaa.protocol.command.CommandFactory;

import java.net.InetSocketAddress;

/**
 * @author zhaiyanan
 * @date 2019/6/9 14:26
 */
public class AdamCommandFactory implements CommandFactory {
    @Override
    public RequestCommand createRequest(Object requestObject) {
        return new RequestCommand(requestObject);
    }

    @Override
    public RequestCommand createRequest(short commandCode, final Object request){
        RequestCommand command = new RequestCommand(request);
        command.setCommandCode(commandCode);
        return command;
    }

    @Override
    public ResponseCommand createResponse(final Command request, final Object responseObject) {
        RequestCommand requestCmd = (RequestCommand) request;
        ResponseCommand response = new ResponseCommand(requestCmd.getId(), responseObject);
        response.setProtocolCode(request.getProtocolCode());
        response.setResponseStatus(ResponseStatus.SUCCESS);
        response.setSerializer(requestCmd.getSerializer());
        return response;
    }

    @Override
    public ResponseCommand createExceptionResponse(final Command request, final Throwable t, String errMsg) {
        ServerException exception = new ServerException(errMsg);
        exception.setStackTrace(t.getStackTrace());
        ResponseCommand response = new ResponseCommand(request.getId(), exception );
        RequestCommand requestCmd = (RequestCommand) request;
        response.setProtocolCode(request.getProtocolCode());
        response.setResponseStatus(ResponseStatus.SERVER_EXCEPTION);
        response.setSerializer(requestCmd.getSerializer());
        return response;
    }

    // 以下为客户端返回响应
    @Override
    public ResponseCommand createTimeoutResponse(InetSocketAddress address) {
        ResponseCommand responseCommand = new ResponseCommand();
        responseCommand.setResponseStatus(ResponseStatus.TIMEOUT);
        responseCommand.setResponseTime(System.currentTimeMillis());
        responseCommand.setResponseHost(address);
        return responseCommand;
    }

    @Override
    public ResponseCommand createSendFailedResponse(final InetSocketAddress address, Throwable throwable) {
        ResponseCommand responseCommand = new ResponseCommand();
        responseCommand.setResponseStatus(ResponseStatus.CLIENT_SEND_EXCEPTION);
        responseCommand.setResponseTime(System.currentTimeMillis());
        responseCommand.setResponseHost(address);
        responseCommand.setCause(throwable);
        return responseCommand;
    }

    @Override
    public ResponseCommand createConnectionClosedResponse(InetSocketAddress address) {
        ResponseCommand responseCommand = new ResponseCommand();
        responseCommand.setResponseStatus(ResponseStatus.CONNECTION_CLOSED);
        responseCommand.setResponseTime(System.currentTimeMillis());
        responseCommand.setResponseHost(address);
        return responseCommand;
    }
}