package com.yundepot.adam.protocol.command;


import com.yundepot.adam.protocol.CrcSwitch;
import com.yundepot.adam.protocol.AdamProtocol;
import com.yundepot.oaa.exception.SerializationException;
import com.yundepot.oaa.protocol.ProtocolCode;
import com.yundepot.oaa.protocol.command.Command;
import com.yundepot.oaa.protocol.command.CommandCode;
import com.yundepot.oaa.serialize.SerializerManager;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author zhaiyanan
 * @date 2019/6/14 13:09
 */
public class AdamCommand implements Command {


    private static final long serialVersionUID = -1729979469113766265L;

    /**
     * command 编码
     */
    private CommandCode commandCode;

    /**
     * 序列化器
     */
    private byte serializer = SerializerManager.HESSIAN;

    /**
     * 协议开关
     */
    private CrcSwitch crcSwitch = CrcSwitch.ON;

    /**
     * 请求id
     */
    private int id;

    /**
     * class长度
     */
    private short classNameLen = 0;

    /**
     * header长度
     */
    private short headerLen = 0;

    /**
     * 内容长度
     */
    private int contentLen = 0;

    /**
     * 类名字节
     */
    private byte[] clazzBytes;

    /**
     * 类名
     */
    private String className;

    /**
     * header字节
     */
    private byte[] headerBytes;

    /**
     * 扩展头字段
     */
    private Map<String, String> header;

    /**
     * 内容字节
     */
    private byte[] contentBytes;

    /**
     * 内容
     */
    private Object content;

    public AdamCommand(CommandCode commandCode) {
        this.commandCode = commandCode;
    }

    /**
     * 序列化
     *
     * @throws SerializationException
     */
    @Override
    public void serialize() throws SerializationException {
        this.serializeClazz();
        this.serializeHeader();
        this.serializeContent();
    }

    /**
     * 反序列化
     *
     * @throws SerializationException
     */
    @Override
    public void deserialize() throws SerializationException {
        this.deserializeClazz();
        this.deserializeHeader();
        this.deserializeContent();
    }


    public void serializeClazz() {
        if (this.className != null) {
            byte[] clz = this.className.getBytes(StandardCharsets.UTF_8);
            this.setClazzBytes(clz);
        }
    }

    public void deserializeClazz() {
        if (this.clazzBytes != null && this.getClassName() == null) {
            this.setClassName(new String(this.getClazzBytes(), StandardCharsets.UTF_8));
        }
    }

    public void serializeContent() throws SerializationException {
        if (this.content != null) {
            try {
                this.setContentBytes(SerializerManager.getSerializer(this.getSerializer()).serialize(this.content));
            } catch (SerializationException e) {
                throw e;
            } catch (Exception e) {
                throw new SerializationException("Exception caught when serialize content of response command!", e);
            }
        }
    }

    public void deserializeContent() throws SerializationException {
        if (this.content == null && this.contentBytes != null) {
            try {
                this.setContent(SerializerManager.getSerializer(this.getSerializer()).deserialize(this.getContentBytes(), this.className));
            } catch (SerializationException e) {
                throw e;
            } catch (Exception e) {
                throw new SerializationException("Exception caught when deserialize content of response command!", e);
            }
        }

    }

    public void serializeHeader() throws SerializationException {
        if (this.header != null) {
            try {
                this.setHeaderBytes(SerializerManager.getSerializer(this.getSerializer()).serialize(this.header));
            } catch (SerializationException e) {
                throw e;
            } catch (Exception e) {
                throw new SerializationException("Exception caught when serialize content of response command!", e);
            }
        }
    }

    public void deserializeHeader() throws SerializationException {
        if (this.header == null && this.getHeaderBytes() != null) {
            try {
                this.setHeader(SerializerManager.getSerializer(this.getSerializer()).deserialize(this.getHeaderBytes(), Map.class.getName()));

            } catch (SerializationException e) {
                throw e;
            } catch (Exception e) {
                throw new SerializationException("Exception caught when deserialize content of response command!", e);
            }
        }
    }

    @Override
    public ProtocolCode getProtocolCode() {
        return ProtocolCode.getProtocolCode(AdamProtocol.PROTOCOL_CODE, AdamProtocol.PROTOCOL_VERSION);
    }

    @Override
    public CommandCode getCommandCode() {
        return commandCode;
    }

    public void setCommandCode(CommandCode commandCode) {
        this.commandCode = commandCode;
    }

    @Override
    public byte getSerializer() {
        return serializer;
    }

    public void setSerializer(byte serializer) {
        this.serializer = serializer;
    }

    public CrcSwitch getCrcSwitch() {
        return crcSwitch;
    }

    public void setCrcSwitch(CrcSwitch crcSwitch) {
        this.crcSwitch = crcSwitch;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public byte[] getContentBytes() {
        return contentBytes;
    }

    public void setContentBytes(byte[] contentBytes) {
        if (contentBytes != null) {
            this.contentBytes = contentBytes;
            this.contentLen = contentBytes.length;
        }
    }

    public short getHeaderLen() {
        return headerLen;
    }

    public int getContentLen() {
        return contentLen;
    }

    public short getClassNameLen() {
        return classNameLen;
    }

    public byte[] getClazzBytes() {
        return clazzBytes;
    }

    public void setClazzBytes(byte[] clazzBytes) {
        if (clazzBytes != null) {
            this.clazzBytes = clazzBytes;
            this.classNameLen = (short) clazzBytes.length;
        }
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }
}
