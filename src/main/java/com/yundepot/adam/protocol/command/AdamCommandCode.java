package com.yundepot.adam.protocol.command;

import com.yundepot.oaa.protocol.command.CommandCode;

/**
 * @author zhaiyanan
 * @date 2019/5/29 18:34
 */
public enum AdamCommandCode implements CommandCode {

    HEARTBEAT_REQUEST((short)1),
    HEARTBEAT_RESPONSE((short)2),
    REQUEST((short) 3),
    RESPONSE((short) 4),
    ;

    private short value;

    AdamCommandCode(short value) {
        this.value = value;
    }

    @Override
    public short value() {
        return this.value;
    }

    public static AdamCommandCode valueOf(short value) {
        switch (value) {
            case 1:
                return HEARTBEAT_REQUEST;
            case 2:
                return HEARTBEAT_RESPONSE;
            case 3:
                return REQUEST;
            case 4:
                return RESPONSE;
                default:
                    throw new IllegalArgumentException("Unknown command code value: " + value);
        }
    }

}
