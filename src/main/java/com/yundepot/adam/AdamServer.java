package com.yundepot.adam;

import com.yundepot.adam.processor.Processor;
import com.yundepot.adam.processor.ProcessorManager;
import com.yundepot.adam.protocol.AdamProtocol;
import com.yundepot.oaa.OaaServer;

import java.net.InetSocketAddress;

/**
 * @author zhaiyanan
 * @date 2019/5/14 17:13
 */
public class AdamServer extends OaaServer {

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
        registerProtocol(new AdamProtocol());
    }

    /**
     * 注册处理器
     * @param processor
     */
    public void registerProcessor(Processor<?> processor) {
        ProcessorManager.registerProcessor(processor);
    }

}
