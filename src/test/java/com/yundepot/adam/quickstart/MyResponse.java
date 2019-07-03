package com.yundepot.adam.quickstart;

import java.io.Serializable;

/**
 * @author zhaiyanan
 * @date 2019/5/10 16:09
 */
public class MyResponse implements Serializable {

    private String resp;

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }
}
