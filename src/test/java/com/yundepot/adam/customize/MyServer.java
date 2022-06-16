package com.yundepot.adam.customize;

import com.yundepot.adam.AdamServer;
import com.yundepot.oaa.protocol.Protocol;
import com.yundepot.oaa.protocol.handler.ProtocolHandler;

/**
 * 自定义commandCode 示例
 * @author zhaiyanan
 * @date 2022/6/16  10:25
 */
public class MyServer {

    public static void main(String[] args) {
        AdamServer server = new AdamServer(8087);
        Protocol protocol = server.getProtocol();
        ProtocolHandler protocolHandler = protocol.getProtocolHandler();
        protocolHandler.registerCommandProcessor((short) 7, new EchoCommandProcessor());
        server.start();
    }
}
