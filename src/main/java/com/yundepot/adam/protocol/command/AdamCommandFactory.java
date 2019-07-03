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
    public ResponseCommand createResponse(final Command request, final Object responseObject) {
        RequestCommand requestCmd = (RequestCommand) request;
        ResponseCommand response = new ResponseCommand(requestCmd.getId(), responseObject);
        if (null != responseObject) {
            response.setClassName(responseObject.getClass().getName());
        }
        response.setSerializer(requestCmd.getSerializer());
        response.setCrcSwitch(requestCmd.getCrcSwitch());
        response.setResponseStatus(ResponseStatus.SUCCESS);
        return response;
    }


    @Override
    public ResponseCommand createExceptionResponse(int id, final Throwable t, String errMsg) {
        ResponseCommand response = null;
        if (null == t) {
            response = new ResponseCommand(id, createServerException(errMsg));
        } else {
            response = new ResponseCommand(id, createServerException(t, errMsg));
        }
        response.setClassName(ServerException.class.getName());
        response.setResponseStatus(ResponseStatus.SERVER_EXCEPTION);
        return response;
    }

    @Override
    public ResponseCommand createTimeoutResponse(InetSocketAddress address) {
        ResponseCommand responseCommand = new ResponseCommand();
        responseCommand.setResponseStatus(ResponseStatus.TIMEOUT);
        responseCommand.setResponseTimeMillis(System.currentTimeMillis());
        responseCommand.setResponseHost(address);
        return responseCommand;
    }

    @Override
    public ResponseCommand createSendFailedResponse(final InetSocketAddress address, Throwable throwable) {
        ResponseCommand responseCommand = new ResponseCommand();
        responseCommand.setResponseStatus(ResponseStatus.CLIENT_SEND_EXCEPTION);
        responseCommand.setResponseTimeMillis(System.currentTimeMillis());
        responseCommand.setResponseHost(address);
        responseCommand.setCause(throwable);
        return responseCommand;
    }

    @Override
    public ResponseCommand createConnectionClosedResponse(InetSocketAddress address) {
        ResponseCommand responseCommand = new ResponseCommand();
        responseCommand.setResponseStatus(ResponseStatus.CONNECTION_CLOSED);
        responseCommand.setResponseTimeMillis(System.currentTimeMillis());
        responseCommand.setResponseHost(address);
        return responseCommand;
    }

    private ServerException createServerException(String errMsg) {
        return new ServerException(errMsg);
    }

    private ServerException createServerException(Throwable t, String errMsg) {
        String formattedErrMsg = String.format("[Server]origin error message: %s: %s. describe message: %s", t.getClass().getName(), t.getMessage(), errMsg);
        ServerException e = new ServerException(formattedErrMsg);
        e.setStackTrace(t.getStackTrace());
        return e;
    }
}