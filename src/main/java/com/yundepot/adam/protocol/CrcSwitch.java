package com.yundepot.adam.protocol;

/**
 * @author zhaiyanan
 * @date 2019/7/1 14:27
 */
public enum CrcSwitch {
    OFF((byte) 0),
    ON((byte) 1),
    ;

    // todo 该类不应该放在这里
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
                return OFF;
            case 1:
                return ON;
                default:
                    throw new IllegalArgumentException("Unknown status value ," + value);
        }
    }
}
