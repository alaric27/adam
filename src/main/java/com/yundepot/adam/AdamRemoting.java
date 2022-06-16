package com.yundepot.adam;

import com.yundepot.adam.config.HeaderOption;
import com.yundepot.adam.protocol.command.AdamCommandCode;
import com.yundepot.adam.protocol.command.RequestCommand;
import com.yundepot.oaa.BaseRemoting;
import com.yundepot.oaa.connection.Connection;
import com.yundepot.oaa.connection.Url;
import com.yundepot.oaa.invoke.DefaultInvokeFuture;
import com.yundepot.oaa.invoke.InvokeCallback;
import com.yundepot.oaa.invoke.InvokeFuture;
import com.yundepot.oaa.protocol.Protocol;
import com.yundepot.oaa.protocol.command.Command;
import com.yundepot.oaa.protocol.command.CommandCode;
import com.yundepot.oaa.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaiyanan
 * @date 2019/6/12 13:49
 */
public abstract class AdamRemoting extends BaseRemoting {
    private Protocol protocol;

    public AdamRemoting(Protocol protocol) {
        super(protocol.getCommandFactory());
        this.protocol = protocol;
    }

    public void oneway(final String address, final Object request, Map<String, String> header) throws Exception{
        Url url = Url.parse(address);
        this.oneway(url, request, header);
    }

    public abstract void oneway(final Url url, final Object request, Map<String, String> header) throws Exception;

    public void oneway(final Connection conn, final Object request, Map<String, String> header) throws Exception{
        RequestCommand requestCommand = createRequestCommand(request, header, AdamCommandCode.ONE_WAY.value());
        super.oneway(conn, requestCommand);
    }

    public Command invokeSync(final String address, final Object request, Map<String, String> header, final int timeoutMillis) throws Exception {
        Url url = Url.parse(address);
        return this.invokeSync(url, request, header, timeoutMillis, AdamCommandCode.REQUEST.value());
    }

    public Command invokeSync(String address, Object request, Map<String, String> header, int timeout, short commandCode) throws Exception {
        Url url = Url.parse(address);
        return this.invokeSync(url, request, header, timeout, commandCode);
    }

    public abstract Command invokeSync(final Url url, final Object request, Map<String, String> header, final int timeoutMillis) throws Exception;

    public abstract Command invokeSync(Url url, Object request, Map<String, String> header, int timeout, short commandCode) throws Exception;

    public Command invokeSync(final Connection conn, final Object request, Map<String, String> header, final int timeoutMillis) throws Exception {
        Command requestCommand = createRequestCommand(request, header);
        return super.invokeSync(conn, requestCommand, timeoutMillis);
    }

    public Command invokeSync(final Connection conn, final Object request, Map<String, String> header, final int timeout, short commandCode) throws Exception {
        Command requestCommand = createRequestCommand(request, header, commandCode);
        return super.invokeSync(conn, requestCommand, timeout);
    }

    public InvokeFuture invokeWithFuture(final String addr, final Object request, Map<String, String> header, int timeoutMillis) throws Exception{
        Url url = Url.parse(addr);
        return this.invokeWithFuture(url, request, header, timeoutMillis);
    }

    public abstract InvokeFuture invokeWithFuture(final Url url, final Object request, Map<String, String> header, final int timeoutMillis) throws Exception;

    public InvokeFuture invokeWithFuture(final Connection conn, final Object request, Map<String, String> header, final int timeoutMillis) {
        Command requestCommand = createRequestCommand(request, header);
        InvokeFuture future = super.invokeWithFuture(conn, requestCommand, timeoutMillis);
        return future;
    }

    public void invokeWithCallback(String addr, Object request, Map<String, String> header, InvokeCallback invokeCallback, int timeoutMillis) throws Exception{
        Url url = Url.parse(addr);
        this.invokeWithCallback(url, request, header, invokeCallback, timeoutMillis);
    }

    public abstract void invokeWithCallback(final Url url, final Object request, Map<String, String> header, final InvokeCallback invokeCallback,
                                            final int timeoutMillis) throws Exception;

    public void invokeWithCallback(final Connection conn, final Object request, Map<String, String> header, final InvokeCallback invokeCallback, final int timeoutMillis) {
        Command requestCommand = createRequestCommand(request, header);
        super.invokeWithCallback(conn, requestCommand, invokeCallback, timeoutMillis);
    }

    /**
     * 构建请求对象
     * @param request
     * @param header
     * @return
     */
    protected RequestCommand createRequestCommand(Object request, Map<String, String> header) {
        return createRequestCommand(request, header, AdamCommandCode.REQUEST.value());
    }

    protected RequestCommand createRequestCommand(Object request, Map<String, String> header, short commandCode) {
        RequestCommand command = this.getCommandFactory().createRequest(commandCode, request);
        command.setProtocolCode(protocol.getProtocolCode());
        header = header == null ? new HashMap<>() : header;

        String uri = header.get(HeaderOption.PROCESSOR.getKey());
        if (StringUtils.isEmpty(uri)) {
            header.put(HeaderOption.PROCESSOR.getKey(), request.getClass().getName());
        }
        command.setHeader(header);
        return command;
    }


    @Override
    protected InvokeFuture createInvokeFuture(Command request) {
        return new DefaultInvokeFuture(request.getId(), null, protocol, this.getCommandFactory());
    }

    @Override
    protected InvokeFuture createInvokeFuture(Connection conn, Command request, InvokeCallback invokeCallback) {
        return new DefaultInvokeFuture(request.getId(), invokeCallback, protocol, this.getCommandFactory());
    }
}
