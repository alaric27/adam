package com.yundepot.adam.string;


import com.yundepot.adam.AdamClient;
import com.yundepot.adam.config.HeaderOption;
import com.yundepot.adam.protocol.command.ResponseCommand;
import com.yundepot.adam.quickstart.MyRequest;
import com.yundepot.adam.quickstart.MyResponse;

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
        Map<String, String> header = new HashMap<>();
        header.put("a", "b");
        header.put(HeaderOption.PROCESSOR.getKey(), "/rpc");
        ResponseCommand response = (ResponseCommand) client.invokeSync("127.0.0.1:8087", "hello world", header, 30 * 1000);
        String myResponse = (String) response.getBody();
        System.out.println(myResponse);
    }

}
