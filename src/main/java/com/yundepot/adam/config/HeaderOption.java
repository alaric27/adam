package com.yundepot.adam.config;

import com.yundepot.adam.common.CrcSwitch;
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
     * 处理器
     */
    public static final ConfigOption<String> PROCESSOR = ConfigOption.valueOf("processor", null);


    /**
     * 序列化选择，非传输参数
     */
    public static final ConfigOption<Byte> SERIALIZER = ConfigOption.valueOf("adam._serializer_", SerializerManager.HESSIAN);
}
