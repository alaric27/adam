package com.yundepot.adam.quickstart;

import java.io.Serializable;

/**
 * @author zhaiyanan
 * @date 2019/5/10 16:08
 */
public class MyRequest implements Serializable {
    private String req;

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }
}
