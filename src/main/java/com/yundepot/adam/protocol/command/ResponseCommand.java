package com.yundepot.adam.protocol.command;

import com.yundepot.oaa.protocol.command.CommandType;

import java.net.InetSocketAddress;

/**
 * @author zhaiyanan
 * @date 2019/6/14 13:16
 */
public class ResponseCommand extends AdamCommand {
    private static final long serialVersionUID = -5194754228565292441L;
    private short status;

    // 以下为客户端信息
    private long responseTime;
    private InetSocketAddress responseHost;
    private Throwable cause;
    private String errorMsg;

    public ResponseCommand() {
        setCommandCode(AdamCommandCode.RESPONSE.value());
        setCommandType(CommandType.RESPONSE.value());
    }

    public ResponseCommand(int id, Object body) {
        this();
        setId(id);
        setBody(body);
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
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
