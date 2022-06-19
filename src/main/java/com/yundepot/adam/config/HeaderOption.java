package com.yundepot.adam.config;

import com.yundepot.adam.protocol.CrcSwitch;
import com.yundepot.oaa.config.ConfigOption;

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
}
