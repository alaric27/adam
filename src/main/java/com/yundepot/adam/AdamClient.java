package com.yundepot.adam;

import com.yundepot.adam.protocol.AdamProtocol;
import com.yundepot.oaa.OaaClient;
import com.yundepot.oaa.config.GenericOption;
import com.yundepot.oaa.connection.*;
import com.yundepot.oaa.invoke.InvokeCallback;
import com.yundepot.oaa.invoke.InvokeFuture;
import com.yundepot.oaa.protocol.Protocol;
import com.yundepot.oaa.protocol.command.Command;

import java.util.Map;

/**
 *
 *  客户端
 * @author zhaiyanan
 * @date 2019/5/10 16:15
 */
public class AdamClient extends OaaClient {

    private DefaultConnectionManager connectionManager;
    protected AdamRemoting adamRemoting;
    private ConnectionScanner taskScanner = new ConnectionScanner();
    private Protocol protocol;

    public AdamClient() {
        this.protocol = new AdamProtocol();
        ConnectionFactory connectionFactory = new DefaultConnectionFactory(connectionEventHandler, configManager, protocol);
        connectionManager = new DefaultConnectionManager(connectionFactory, configManager);
        connectionEventHandler.setConnectionManager(connectionManager);
    }

    @Override
    public void start() {
        this.connectionManager.start();
        this.adamRemoting = new AdamClientRemoting(this.connectionManager, this.protocol);
        this.taskScanner.add(this.connectionManager);
        this.taskScanner.start();
    }

    @Override
    public void shutdown() {
        this.connectionManager.shutdown();
        this.taskScanner.shutdown();
    }

    public void oneway(final String addr, final Object request, Map<String, String> header) throws Exception{
        this.adamRemoting.oneway(addr, request, header);
    }

    public void oneway(final Url url, final Object request, Map<String, String> header) throws Exception{
        this.adamRemoting.oneway(url, request, header);
    }

    public void oneway(final Connection conn, final Object request, Map<String, String> header) throws Exception{
        this.adamRemoting.oneway(conn, request, header);
    }

    public Command invokeSync(final String addr, final Object request, Map<String, String> header, final int timeoutMillis) throws Exception {
        return this.adamRemoting.invokeSync(addr, request, header, timeoutMillis);
    }

    public Command invokeSync(final Url url, final Object request, Map<String, String> header, final int timeoutMillis) throws Exception{
        return this.adamRemoting.invokeSync(url, request, header, timeoutMillis);
    }

    public Command invokeSync(final Connection conn, final Object request, Map<String, String> header, final int timeoutMillis) throws Exception {
        return this.adamRemoting.invokeSync(conn, request, header, timeoutMillis);
    }

    public InvokeFuture invokeWithFuture(final String addr, final Object request, Map<String, String> header, final int timeoutMillis) throws Exception {
        return this.adamRemoting.invokeWithFuture(addr, request, header, timeoutMillis);
    }

    public InvokeFuture invokeWithFuture(final Url url, final Object request, Map<String, String> header, final int timeoutMillis) throws Exception {
        return this.adamRemoting.invokeWithFuture(url, request, header, timeoutMillis);
    }

    public InvokeFuture invokeWithFuture(final Connection conn, final Object request, Map<String, String> header, int timeoutMillis) {
        return this.adamRemoting.invokeWithFuture(conn, request, header, timeoutMillis);
    }

    public void invokeWithCallback(final String addr, final Object request, Map<String, String> header, final InvokeCallback invokeCallback,
                                   final int timeoutMillis) throws Exception {
        this.adamRemoting.invokeWithCallback(addr, request, header, invokeCallback, timeoutMillis);
    }

    public void invokeWithCallback(final Url url, final Object request, Map<String, String> header, final InvokeCallback invokeCallback,
                                   final int timeoutMillis) throws Exception {
        this.adamRemoting.invokeWithCallback(url, request, header, invokeCallback, timeoutMillis);
    }

    public void invokeWithCallback(final Connection conn, final Object request, Map<String, String> header, final InvokeCallback invokeCallback, final int timeoutMillis) {
        this.adamRemoting.invokeWithCallback(conn, request, header, invokeCallback, timeoutMillis);
    }

    public Connection getConnection(String addr, int connectTimeout) throws Exception {
        Url url = Url.parse(addr);
        return this.getConnection(url, connectTimeout);
    }

    public Connection getConnection(Url url, int connectTimeout) throws Exception {
        this.configManager.option(GenericOption.CREATE_CONNECTION_TIMEOUT, connectTimeout);
        return this.connectionManager.getAndCreateIfAbsent(url);
    }

    @Override
    public Protocol getProtocol() {
        return this.protocol;
    }
}
