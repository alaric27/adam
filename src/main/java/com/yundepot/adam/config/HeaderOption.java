package com.yundepot.adam.config;

import com.yundepot.adam.protocol.CrcSwitch;
import com.yundepot.oaa.common.ResponseStatus;
import com.yundepot.oaa.config.ConfigOption;
import com.yundepot.oaa.serialize.SerializerManager;

/**
 * @author zhaiyanan
 * @date 2020/5/18  13:01
 */
public class HeaderOption {

    /**
     * crc开关, 默认为关
     */
    public static final ConfigOption<String> CRC_SWITCH = ConfigOption.valueOf("crcSwitch", String.valueOf(CrcSwitch.OFF.getCode()));

    /**
     * 序列化方式，默认为hessian
     */
    public static final ConfigOption<String> SERIALIZATION = ConfigOption.valueOf("serialization", String.valueOf(SerializerManager.HESSIAN));

    /**
     * 请求超时时间，默认为-1
     */
    public static final ConfigOption<String> REQUEST_TIMEOUT = ConfigOption.valueOf("timeout", String.valueOf(SerializerManager.HESSIAN));

    /**
     * 响应状态码， 默认成功
     */
    public static final ConfigOption<String> RESPONSE_STATUS = ConfigOption.valueOf("status", String.valueOf(ResponseStatus.SUCCESS.getValue()));

    /**
     * 感兴趣的事件
     */
    public static final ConfigOption<String> INTEREST = ConfigOption.valueOf("interest", null);
}
