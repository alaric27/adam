package com.yundepot.adam.util;

import com.yundepot.adam.protocol.command.ResponseCommand;
import com.yundepot.oaa.common.ResponseStatus;
import com.yundepot.oaa.exception.ConnectionException;
import com.yundepot.oaa.exception.InvokeException;
import com.yundepot.oaa.exception.ServerException;
import com.yundepot.oaa.util.StringUtils;
import io.netty.handler.codec.CodecException;

/**
 * @author zhaiyanan
 * @date 2019/5/31 09:45
 */
public class ResponseResolver {

    public static Object resolveResponseObject(ResponseCommand response, String addr) throws Exception {
        preProcess(response, addr);
        return response.getContent();
    }

    private static void preProcess(ResponseCommand response, String addr) throws Exception {
        String msg;
        if (response == null) {
            msg = String.format("invocation timeout[response null]! the address is %s", addr);
            throw new InvokeException(msg);
        }

        if (ResponseStatus.SUCCESS == response.getResponseStatus()) {
            return;
        }

        switch (response.getResponseStatus()) {
            case SUCCESS:
                break;
            case SERVER_EXCEPTION:
                msg = String.format("Server exception! Please check the server log, the address is %s, id=%s", addr, response.getId());
                throw new ServerException(detailErrorMsg(msg, response), toThrowable(response));
            case TIMEOUT:
                msg = String.format(" invocation timeout[response TIMEOUT]! the address is %s", addr);
                throw new InvokeException(msg);
            case CONNECTION_CLOSED:
                msg = String.format("Connection closed! the address is %s", addr);
                throw new ConnectionException(msg);
            case CLIENT_SEND_EXCEPTION:
                msg = String.format(" invocation send failed! the address is %s", addr);
                throw  new InvokeException(msg, response.getCause());
            case CODEC_EXCEPTION:
                msg = String.format("Codec exception! the address is %s, id=%s", addr, response.getId());
                throw  new CodecException(msg);
                default:
                    throw new InvokeException("Unknown exception");
        }

    }

    private static Throwable toThrowable(ResponseCommand response) {
        Object ex = response.getContent();
        if (ex != null && ex instanceof Throwable) {
            return (Throwable) ex;
        }
        return null;
    }

    private static String detailErrorMsg(String clientErrMsg, ResponseCommand response) {
        if (StringUtils.isNotBlank(response.getErrorMsg())) {
            return String.format("%s, ServerErrorMsg:%s", clientErrMsg, response.getErrorMsg());
        } else {
            return String.format("%s, ServerErrorMsg:null", clientErrMsg);
        }
    }
}
