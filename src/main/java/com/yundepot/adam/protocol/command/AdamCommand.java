package com.yundepot.adam.protocol.command;


import com.yundepot.adam.config.HeaderOption;
import com.yundepot.oaa.exception.DeserializationException;
import com.yundepot.oaa.exception.SerializationException;
import com.yundepot.oaa.protocol.ProtocolCode;
import com.yundepot.oaa.protocol.command.Command;
import com.yundepot.oaa.serialize.SerializerManager;
import com.yundepot.oaa.serialize.StringMapSerializer;

import java.util.HashMap;
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
    private short commandCode;

    /**
     * 请求id
     */
    private int id;

    /**
     * 序列化编码
     */
    private byte serializer = SerializerManager.HESSIAN;

    /**
     * header长度
     */
    private short headerLen = 0;

    /**
     * 扩展头字段
     */
    private Map<String, String> header = new HashMap<>();

    /**
     * 扩展头字节
     */
    private byte[] headerBytes;

    /**
     * 内容长度
     */
    private int bodyLen = 0;

    /**
     * 内容
     */
    private Object body;

    /**
     * 内容字节
     */
    private byte[] bodyBytes;

    public AdamCommand() {

    }

    public AdamCommand(short commandCode) {
        this.commandCode = commandCode;
    }

    @Override
    public ProtocolCode getProtocolCode() {
        return this.protocolCode;
    }

    public void setProtocolCode(ProtocolCode protocolCode) {
        this.protocolCode = protocolCode;
    }

    @Override
    public short getCommandCode() {
        return commandCode;
    }

    public void setCommandCode(short commandCode) {
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

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public void setHeader(String key, String value) {
        if (header == null) {
            header = new HashMap<>();
        }
        this.header.put(key, value);
    }

    public String getHeader(String key, String value) {
        if (this.header == null) {
            return value;
        }
        return this.header.getOrDefault(key, value);
    }

    public String getHeader(String key) {
        return getHeader(key, null);
    }

    public int getBodyLen() {
        return bodyLen;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public byte getSerializer() {
        return serializer;
    }

    @Override
    public void serialize() throws SerializationException {
        this.serializeHeader();
        this.serializeBody();
    }

    private void serializeBody() throws SerializationException{
        if (this.body != null) {
            setBodyBytes(SerializerManager.getSerializer(this.serializer).serialize(this.body));
        }
    }

    private void serializeHeader() {
        if (null != this.header || !this.header.isEmpty()) {
            setHeaderBytes(StringMapSerializer.serialize(this.header));
        }
    }

    @Override
    public void deserialize() throws DeserializationException {
        this.deserializeHeader();
        this.deserializeBody();
    }

    private void deserializeBody() throws DeserializationException{
        if (this.bodyBytes != null) {
            setBody(SerializerManager.getSerializer(serializer).deserialize(bodyBytes, getHeader(HeaderOption.SERIALIZE_HINT.getKey())));
        }
    }

    private void deserializeHeader() {
        if (this.headerBytes != null) {
            setHeader(StringMapSerializer.deserialize(headerBytes));
        }
    }

    public void setSerializer(byte serializer) {
        this.serializer = serializer;
    }

    public byte[] getHeaderBytes() {
        return headerBytes;
    }

    public void setHeaderBytes(byte[] headerBytes) {
        if (headerBytes != null) {
            this.headerBytes = headerBytes;
            this.headerLen = (short) headerBytes.length;
        }
    }

    public byte[] getBodyBytes() {
        return bodyBytes;
    }

    public void setBodyBytes(byte[] bodyBytes) {
        if (bodyBytes != null) {
            this.bodyBytes = bodyBytes;
            this.bodyLen = bodyBytes.length;
        }
    }
}
