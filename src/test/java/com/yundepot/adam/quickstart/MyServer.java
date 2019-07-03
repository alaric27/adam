package com.yundepot.adam.quickstart;


import com.yundepot.adam.AdamServer;

/**
 * @author zhaiyanan
 * @date 2019/5/10 16:13
 */
public class MyServer {


    public static void start() {
        AdamServer server = new AdamServer(8087);
        server.registerProcessor(new MyServerProcessor());
        server.start();
    }


    public static void main(String[] args) {
        MyServer.start();
    }

}
