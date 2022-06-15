package com.yundepot.adam.protocol.command;

import com.yundepot.adam.config.HeaderOption;
import com.yundepot.adam.protocol.CrcSwitch;
import com.yundepot.oaa.common.ResponseStatus;
import com.yundepot.oaa.exception.ServerException;
import com.yundepot.oaa.protocol.command.Command;
import com.yundepot.oaa.protocol.command.CommandCode;
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
    public RequestCommand createRequest(CommandCode commandCode, final Object request){
        return new RequestCommand(commandCode, request);
    }

    @Override
    public ResponseCommand createResponse(final Command request, final Object responseObject) {
        RequestCommand requestCmd = (RequestCommand) request;
        ResponseCommand response = new ResponseCommand(requestCmd.getId(), responseObject);
        response.setProtocolCode(request.getProtocolCode());
        response.setResponseStatus(ResponseStatus.SUCCESS);
        response.setSerializer(requestCmd.getSerializer());

        byte crcSwitch = Byte.valueOf(requestCmd.getHeader(HeaderOption.CRC_SWITCH.getKey(), HeaderOption.CRC_SWITCH.getDefaultValue()));
        if (CrcSwitch.ON.getCode() == crcSwitch) {
            response.setHeader(HeaderOption.CRC_SWITCH.getKey(), String.valueOf(CrcSwitch.ON.getCode()));
        }
        return response;
    }

    @Override
    public ResponseCommand createExceptionResponse(final Command request, final Throwable t, String errMsg) {
        ResponseCommand response = null;
        if (null == t) {
            response = new ResponseCommand(request.getId(), createServerException(errMsg));
        } else {
            response = new ResponseCommand(request.getId(), createServerException(t, errMsg));
        }

        RequestCommand requestCmd = (RequestCommand) request;
        response.setProtocolCode(request.getProtocolCode());
        response.setResponseStatus(ResponseStatus.SERVER_EXCEPTION);
        response.setSerializer(requestCmd.getSerializer());
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

    @Override
    public RequestCommand createHeartBeatRequest() {
        return new RequestCommand(AdamCommandCode.HEARTBEAT_REQUEST);
    }

    @Override
    public boolean checkResponse(Command command) {
        ResponseCommand response = (ResponseCommand) command;
        if (response == null || !response.getResponseStatus().equals(ResponseStatus.SUCCESS)) {
            return false;
        }
        return true;
    }
}