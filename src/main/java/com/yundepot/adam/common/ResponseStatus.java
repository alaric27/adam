package com.yundepot.adam.common;

/**
 * @author zhaiyanan
 * @date 2019/5/23 14:46
 */
public enum ResponseStatus {
    SUCCESS((short) 0),
    SERVER_EXCEPTION((short) 1),
    TIMEOUT((short) 2),
    CLIENT_SEND_EXCEPTION((short) 3),
    CONNECTION_CLOSED((short) 4),
    ;

    private short code;

    ResponseStatus(short code) {
        this.code = code;
    }

    public short value() {
        return code;
    }
}
