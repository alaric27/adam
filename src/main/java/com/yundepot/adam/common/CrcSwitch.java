package com.yundepot.adam.common;

/**
 * @author zhaiyanan
 * @date 2019/7/1 14:27
 */
public enum CrcSwitch {
    OFF((byte) 0),
    ON((byte) 1),
    ;

    private byte code;

    CrcSwitch(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
