package com.yundepot.adam.protocol;

import com.yundepot.adam.protocol.codec.AdamProtocolDecoder;
import com.yundepot.adam.protocol.codec.AdamProtocolEncoder;
import com.yundepot.adam.protocol.command.AdamCommandFactory;
import com.yundepot.oaa.protocol.*;
import com.yundepot.oaa.protocol.codec.ProtocolDecoder;
import com.yundepot.oaa.protocol.codec.ProtocolEncoder;
import com.yundepot.oaa.protocol.command.CommandFactory;

/**
 *
 * 请求协议
 *   0     1     2           4                      8     9      10          12         14           16          18                     22                                   n                     n+4
 *   +-----+-----+-----+----------+-----+-----+-----+-----+------+-----+-----+-----+-----+-----------+-----------+-----+-----+-----+-----+-----------+-----------+-----+-----+-----+-----+-----+-----+
 *   |proto| ver | cmdcode   |    requestId         |codec|switch|   timeout             |  classLen | headerLen |   contentLen          | className header content  bytes   | CRC32(optional)       |
 *   +-----------------+----------------+-----------+------------+-----------+-----------+-----------+-----------+-----+-----+-----+-----+-----------+-----------+-----+-----+-----+-----+-----+-----+
 *
 *
 * 响应协议
 *
 *   0     1     2           4           6           8     9      10          12         14           16                      20                                   n                     n+4
 *   +-----+-----+-----+-----+-----+-----+-----+-----+-----+------+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----------+-----------+-----+-----+-----+-----+-----+-----+
 *   |proto| ver | cmdcode   |     requestId         |codec|switch|respstatus |  classLen | headerLen |   contentLen          | className header content  bytes   | CRC32(optional)       |
 *   +-----------------+-----------------+-----------+------------+-----------+-----------+-----+-----+-----+-----+-----+-----+-----------+-----------+-----+-----+-----+-----+-----+-----+
 *
 *  proto: 协议编码
 *  ver: 协议版本
 *  cmdcode: Command的编码
 *  requestId:请求id
 *  codec: 编码解码器的代号
 *  switch: crc的开关
 *  classLen: class类的长度
 *  headerLen:header的长度
 *  contentLen:内容的长度
 *  CRC32:校验和
 *
 * @author zhaiyanan
 * @date 2019/5/15 15:48
 */
public class AdamProtocol implements Protocol {

    public static final byte PROTOCOL_CODE = (byte) 1;
    public static final byte PROTOCOL_VERSION = (byte) 1;


    private static final int REQUEST_HEADER_LEN = 22;
    private static final int RESPONSE_HEADER_LEN = 20;

    private ProtocolEncoder encoder;
    private ProtocolDecoder decoder;
    private HeartbeatTrigger heartbeatTrigger;
    private ProtocolHandler protocolHandler;
    private CommandFactory commandFactory;


    public AdamProtocol() {
        this.encoder = new AdamProtocolEncoder();
        this.decoder = new AdamProtocolDecoder();
        this.commandFactory = new AdamCommandFactory();
        this.heartbeatTrigger = new ProtocolHeartbeatTrigger(this.commandFactory, this);
        this.protocolHandler = new AdamProtocolHandler(this.commandFactory);
    }

    public static int getRequestHeaderLength() {
        return REQUEST_HEADER_LEN;
    }

    public static int getResponseHeaderLength() {
        return RESPONSE_HEADER_LEN;
    }


    @Override
    public ProtocolEncoder getEncoder() {
        return this.encoder;
    }

    @Override
    public ProtocolDecoder getDecoder() {
        return this.decoder;
    }

    @Override
    public HeartbeatTrigger getHeartbeatTrigger() {
        return this.heartbeatTrigger;
    }

    @Override
    public ProtocolHandler getProtocolHandler() {
        return this.protocolHandler;
    }

    @Override
    public CommandFactory getCommandFactory() {
        return this.commandFactory;
    }

    @Override
    public ProtocolCode getProtocolCode() {
        return ProtocolCode.getProtocolCode(PROTOCOL_CODE, PROTOCOL_VERSION);
    }
}
