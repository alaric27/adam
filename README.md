# adam
定义了adam协议，是对oaa的一个具体实现。

# quick start
1、定义请求，及该请求对应的处理器

```java
public class MyRequest implements Serializable {
    private String req;

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }
}
```

  ```java
  public class MyResponse implements Serializable {

    private String resp;

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }
}
  ```


```java
public class MyServerProcessor extends SyncProcessor<MyRequest> {

    @Override
    public Object handleRequest(InvokeContext invokeContext, MyRequest myRequest) throws Exception {
        Map<String, String> map = invokeContext.getAttachment();
        MyResponse response = new MyResponse();
        if (myRequest != null) {
            System.out.println(myRequest);
            response.setResp("from server -> " + myRequest.getReq());
        }
        return response;
    }

    @Override
    public String interest() {
        return MyRequest.class.getName();
    }
}

```

2、注册该请求及该请求对应的处理器到服务端，并启动服务
 ```java
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
 
 ```

3、启动客户端
```java
public class MyClient {

    public static void main(String[] args) throws Exception{
        AdamClient client = new AdamClient();
        client.start();
        MyRequest request = new MyRequest();
        request.setReq("hello, adam-server");
        Map<String, String> header = new HashMap<>();
        header.put("a", "b");
        ResponseCommand response = (ResponseCommand) client.invokeSync("127.0.0.1:8087",request, header, 30 * 1000);
        MyResponse myResponse = (MyResponse) response.getContent();
        System.out.println(myResponse.getResp());
    }
}
```

