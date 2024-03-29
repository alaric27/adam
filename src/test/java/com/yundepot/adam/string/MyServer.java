package com.yundepot.adam.string;


import com.yundepot.adam.AdamServer;
import com.yundepot.adam.processor.Processor;
import com.yundepot.adam.processor.ProcessorManager;
import com.yundepot.adam.quickstart.MyServerProcessor;

/**
 * @author zhaiyanan
 * @date 2019/5/10 16:13
 */
public class MyServer {


    public static void start() {
        AdamServer server = new AdamServer(8087);
        ProcessorManager.registerProcessor(new MyStringProcessor());
        server.start();
    }


    public static void main(String[] args) {
        MyServer.start();
    }

}
