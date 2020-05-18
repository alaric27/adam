package com.yundepot.adam.protocol.command;

import com.yundepot.oaa.common.ResponseStatus;
import com.yundepot.oaa.protocol.command.CommandCode;

import java.net.InetSocketAddress;

/**
 * @author zhaiyanan
 * @date 2019/6/14 13:16
 */
public class ResponseCommand extends AdamCommand {
    private static final long serialVersionUID = -5194754228565292441L;
    private ResponseStatus responseStatus;
    private long responseTimeMillis;
    private InetSocketAddress responseHost;
    private Throwable cause;
    private String errorMsg;

    public ResponseCommand() {
        this(AdamCommandCode.RESPONSE);
    }

    public ResponseCommand(CommandCode code) {
        super(code);
    }

    public ResponseCommand(CommandCode code, int id) {
        super(code);
        this.setId(id);
    }

    public ResponseCommand(int id, Object content) {
        this();
        this.setId(id);
        this.setBody(content);
    }

    public ResponseCommand(CommandCode code, int id, Object response) {
        super(code);
        this.setId(id);
        this.setBody(response);
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public long getResponseTimeMillis() {
        return responseTimeMillis;
    }

    public void setResponseTimeMillis(long responseTimeMillis) {
        this.responseTimeMillis = responseTimeMillis;
    }

    public InetSocketAddress getResponseHost() {
        return responseHost;
    }

    public void setResponseHost(InetSocketAddress responseHost) {
        this.responseHost = responseHost;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
