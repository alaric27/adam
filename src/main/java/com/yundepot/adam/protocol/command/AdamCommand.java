package com.yundepot.adam.protocol.command;


import com.yundepot.adam.config.HeaderOption;
import com.yundepot.adam.protocol.AdamProtocol;
import com.yundepot.oaa.protocol.ProtocolCode;
import com.yundepot.oaa.protocol.command.Command;
import com.yundepot.oaa.protocol.command.CommandCode;

import java.util.Map;

/**
 * @author zhaiyanan
 * @date 2019/6/14 13:09
 */
public class AdamCommand implements Command {


    private static final long serialVersionUID = -1729979469113766265L;

    /**
     * 协议编码
     */
    private ProtocolCode protocolCode;

    /**
     * command 编码
     */
    private CommandCode commandCode;

    /**
     * 请求id
     */
    private int id;

    /**
     * header长度
     */
    private short headerLen = 0;

    /**
     * 扩展头字段
     */
    private Map<String, String> header;

    /**
     * 内容长度
     */
    private int bodyLen = 0;

    /**
     * 内容
     */
    private Object body;

    public AdamCommand() {

    }

    public AdamCommand(CommandCode commandCode) {
        this.commandCode = commandCode;
    }

    public String getInterest() {
        if (this.header == null) {
            return null;
        }
        return this.header.getOrDefault(HeaderOption.INTEREST.getKey(), HeaderOption.INTEREST.getDefaultValue());
    }

    @Override
    public ProtocolCode getProtocolCode() {
        return this.protocolCode;
    }

    public void setProtocolCode(ProtocolCode protocolCode) {
        this.protocolCode = protocolCode;
    }

    @Override
    public CommandCode getCommandCode() {
        return commandCode;
    }

    public void setCommandCode(CommandCode commandCode) {
        this.commandCode = commandCode;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public short getHeaderLen() {
        return headerLen;
    }

    public void setHeaderLen(short headerLen) {
        this.headerLen = headerLen;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public void setHeader(String key, String value) {
        this.header.put(key, value);
    }

    public String getHeader(String key, String value) {
        if (this.header == null) {
            return null;
        }
        return this.header.getOrDefault(key, value);
    }

    public int getBodyLen() {
        return bodyLen;
    }

    public void setBodyLen(int bodyLen) {
        this.bodyLen = bodyLen;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
