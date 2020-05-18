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

    public void oneway(final String addr, final Object request, Map<String, String> header) throws Exception{
        Url url = Url.parse(addr);
        this.oneway(url, request, header);
    }

    public abstract void oneway(final Url url, final Object request, Map<String, String> header) throws Exception;

    public void oneway(final Connection conn, final Object request, Map<String, String> header) throws Exception{
        RequestCommand requestCommand = toRemotingCommand(request, header, -1);
        requestCommand.setCommandCode(AdamCommandCode.ONE_WAY);
        super.oneway(conn, requestCommand);
    }

    public Command invokeSync(final String addr, final Object request, Map<String, String> header, final int timeoutMillis) throws Exception {
        Url url = Url.parse(addr);
        return this.invokeSync(url, request, header, timeoutMillis);
    }

    public abstract Command invokeSync(final Url url, final Object request, Map<String, String> header, final int timeoutMillis) throws Exception;

    public Command invokeSync(final Connection conn, final Object request, Map<String, String> header, final int timeoutMillis) throws Exception {
        Command requestCommand = toRemotingCommand(request, header, timeoutMillis);
        return super.invokeSync(conn, requestCommand, timeoutMillis);
    }

    public InvokeFuture invokeWithFuture(final String addr, final Object request, Map<String, String> header, int timeoutMillis) throws Exception{
        Url url = Url.parse(addr);
        return this.invokeWithFuture(url, request, header, timeoutMillis);
    }

    public abstract InvokeFuture invokeWithFuture(final Url url, final Object request, Map<String, String> header, final int timeoutMillis) throws Exception;

    public InvokeFuture invokeWithFuture(final Connection conn, final Object request, Map<String, String> header, final int timeoutMillis) {
        Command requestCommand = toRemotingCommand(request, header, timeoutMillis);
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
        Command requestCommand = toRemotingCommand(request, header, timeoutMillis);
        super.invokeWithCallback(conn, requestCommand, invokeCallback, timeoutMillis);
    }

    /**
     * 构建请求对象
     * @param request
     * @param header
     * @param timeoutMillis
     * @return
     */
    protected RequestCommand toRemotingCommand(Object request, Map<String, String> header, int timeoutMillis) {
        RequestCommand command = this.getCommandFactory().createRequest(request);
        command.setProtocolCode(protocol.getProtocolCode());
        if (header == null) {
            header = new HashMap<>();
        }
        command.setHeader(header);
        command.setHeader(HeaderOption.REQUEST_TIMEOUT.getKey(), String.valueOf(timeoutMillis));
        command.setHeader(HeaderOption.REQUEST_TIMEOUT.getKey(), header.getOrDefault(HeaderOption.REQUEST_TIMEOUT.getKey(), HeaderOption.REQUEST_TIMEOUT.getDefaultValue()));
        command.setHeader(HeaderOption.INTEREST.getKey(), header.getOrDefault(HeaderOption.INTEREST.getKey(), request.getClass().getName()));
        command.setHeader(HeaderOption.CRC_SWITCH.getKey(), header.getOrDefault(HeaderOption.CRC_SWITCH.getKey(), HeaderOption.CRC_SWITCH.getDefaultValue()));
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
