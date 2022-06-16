package com.yundepot.adam.quickstart;


import com.yundepot.adam.AdamClient;
import com.yundepot.adam.protocol.command.ResponseCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaiyanan
 * @date 2019/5/10 16:15
 */
public class MyClient {

    public static void main(String[] args) throws Exception{
        AdamClient client = new AdamClient();
        client.start();
        MyRequest request = new MyRequest();
        request.setReq("hello, adam-server");
        Map<String, String> header = new HashMap<>();
        header.put("a", "b");
        long start = System.currentTimeMillis();
        ResponseCommand response = (ResponseCommand) client.invokeSync("127.0.0.1:8087",request, header, 3000 * 1000);
        System.out.println(System.currentTimeMillis() - start);
        MyResponse myResponse = (MyResponse) response.getBody();
        System.out.println(myResponse.getResp());
//        Thread.sleep(100000);
    }

}
