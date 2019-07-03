package com.yundepot.adam;

import com.yundepot.oaa.connection.Connection;
import com.yundepot.oaa.connection.DefaultConnectionManager;
import com.yundepot.oaa.connection.Url;
import com.yundepot.oaa.exception.ConnectionException;
import com.yundepot.oaa.invoke.InvokeCallback;
import com.yundepot.oaa.invoke.InvokeFuture;
import com.yundepot.oaa.protocol.Protocol;
import com.yundepot.oaa.protocol.command.Command;
import com.yundepot.oaa.protocol.command.CommandFactory;

import java.util.Map;

/**
 * @author zhaiyanan
 * @date 2019/6/12 13:49
 */
public class AdamClientRemoting extends AdamRemoting {
    private DefaultConnectionManager connectionManager;

    public AdamClientRemoting(DefaultConnectionManager connectionManager, Protocol protocol) {
        super(protocol);
        this.connectionManager = connectionManager;
    }

    @Override
    public void oneway(Url url, Object request, Map<String, String> header) throws Exception {
        final Connection conn = getConnectionAndInitInvokeContext(url);
        this.connectionManager.check(conn);
        this.oneway(conn, request, header);
    }

    @Override
    public Command invokeSync(Url url, Object request, Map<String, String> header, int timeoutMillis) throws Exception{
        final Connection conn = getConnectionAndInitInvokeContext(url);
        this.connectionManager.check(conn);
        return this.invokeSync(conn, request, header, timeoutMillis);
    }

    @Override
    public InvokeFuture invokeWithFuture(Url url, Object request, Map<String, String> header, int timeoutMillis) throws Exception{
        final Connection conn = getConnectionAndInitInvokeContext(url);
        this.connectionManager.check(conn);
        return this.invokeWithFuture(conn, request, header, timeoutMillis);
    }


    @Override
    public void invokeWithCallback(Url url, Object request, Map<String, String> header, InvokeCallback invokeCallback, int timeoutMillis) throws Exception{
        final Connection conn = getConnectionAndInitInvokeContext(url);
        this.connectionManager.check(conn);
        this.invokeWithCallback(conn, request, header, invokeCallback, timeoutMillis);
    }


    protected Connection getConnectionAndInitInvokeContext(Url url) throws ConnectionException {
        Connection conn = this.connectionManager.getAndCreateIfAbsent(url);
        return conn;
    }
}
