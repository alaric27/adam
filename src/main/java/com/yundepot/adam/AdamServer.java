package com.yundepot.adam;

import com.yundepot.adam.protocol.AdamProtocol;
import com.yundepot.oaa.OaaServer;
import com.yundepot.oaa.protocol.Protocol;

import java.net.InetSocketAddress;

/**
 * @author zhaiyanan
 * @date 2019/5/14 17:13
 */
public class AdamServer extends OaaServer {


    private Protocol protocol;

    /**
     * 构造方法，只绑定端口
     * @param port
     */
    public AdamServer(int port) {
        this(new InetSocketAddress(port).getAddress().getHostAddress(), port);
    }

    /**
     * 构造方法，绑定IP和端口
     * @param ip
     * @param port
     */
    public AdamServer(String ip, int port) {
        super(ip, port);
        protocol = new AdamProtocol();
        registerProtocol(protocol);
    }

    public Protocol getProtocol() {
        return protocol;
    }
}
