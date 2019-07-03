package com.yundepot.adam.protocol;

/**
 * @author zhaiyanan
 * @date 2019/7/1 14:27
 */
public enum CrcSwitch {
    ON((byte) 0),
    OFF((byte) 1),
    ;

    private byte code;

    CrcSwitch(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public static CrcSwitch valueOf(byte value) {
        switch (value) {
            case 0:
                return ON;
            case 1:
                return OFF;
                default:
                    throw new IllegalArgumentException("Unknown status value ," + value);
        }
    }
}
