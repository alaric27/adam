package com.yundepot.adam.customize;

import com.yundepot.adam.AdamClient;
import com.yundepot.adam.protocol.command.ResponseCommand;
import com.yundepot.adam.quickstart.MyRequest;

/**
 * @author zhaiyanan
 * @date 2022/6/16  11:21
 */
public class MyClient {

    public static void main(String[] args) throws Exception{
        AdamClient client = new AdamClient();
        client.start();
        MyRequest request = new MyRequest();
        request.setReq("hello, adam-server");
        ResponseCommand response = (ResponseCommand) client.invokeSync("127.0.0.1:8087",request, null, 6000, (short) 7);
        MyRequest fromServer = (MyRequest) response.getBody();

        System.out.println(fromServer.getReq());
    }
}
