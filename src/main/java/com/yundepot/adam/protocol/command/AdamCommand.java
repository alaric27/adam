package com.yundepot.adam.protocol.command;


import com.yundepot.oaa.exception.DeserializationException;
import com.yundepot.oaa.exception.SerializationException;
import com.yundepot.oaa.protocol.ProtocolCode;
import com.yundepot.oaa.protocol.command.Command;
import com.yundepot.oaa.protocol.command.CommandCode;
import com.yundepot.oaa.serialize.SerializerManager;
import com.yundepot.oaa.serialize.StringMapSerializer;
import com.yundepot.oaa.util.StringUtils;

import java.nio.charset.StandardCharsets;
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
    private CommandCode commandCode;

    /**
     * 请求id
     */
    private int id;

    /**
     * 序列化编码
     */
    private byte serializer = SerializerManager.HESSIAN;

    /**
     * 资源定位符长度
     */
    private short nriLen = 0;

    /**
     * 资源定位符
     */
    private String nri;

    /**
     * 资源定位字节
     */
    private byte[] nriBytes;

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

    public AdamCommand(CommandCode commandCode) {
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
            return value;
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

    @Override
    public byte getSerializer() {
        return serializer;
    }

    @Override
    public void serialize() throws SerializationException {
        this.serializeNri();
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

    private void serializeNri() {
        if (StringUtils.isNotEmpty(this.nri)) {
            setNriBytes(this.nri.getBytes(StandardCharsets.UTF_8));
        }
    }


    @Override
    public void deserialize() throws DeserializationException {
        this.deserializeNri();
        this.deserializeHeader();
        this.deserializeBody();
    }

    private void deserializeBody() throws DeserializationException{
        if (this.bodyBytes != null) {
            setBody(SerializerManager.getSerializer(serializer).deserialize(bodyBytes, getNri()));
        }
    }

    private void deserializeHeader() {
        if (this.headerBytes != null) {
            setHeader(StringMapSerializer.deserialize(headerBytes));
        }
    }

    private void deserializeNri() {
        if (this.nriBytes != null) {
            this.setNri(new String(this.nriBytes, StandardCharsets.UTF_8));
        }
    }

    public void setSerializer(byte serializer) {
        this.serializer = serializer;
    }

    public short getNriLen() {
        return nriLen;
    }

    public void setNriLen(short nriLen) {
        this.nriLen = nriLen;
    }

    public String getNri() {
        return nri;
    }

    public void setNri(String nri) {
        this.nri = nri;
    }

    public byte[] getNriBytes() {
        return nriBytes;
    }

    public void setNriBytes(byte[] nriBytes) {
        if (nriBytes != null) {
            this.nriBytes = nriBytes;
            this.nriLen = (short) nriBytes.length;
        }
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
