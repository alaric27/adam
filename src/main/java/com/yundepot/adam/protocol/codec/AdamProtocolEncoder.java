package com.yundepot.adam.protocol.codec;

import com.yundepot.adam.config.HeaderOption;
import com.yundepot.adam.protocol.CrcSwitch;
import com.yundepot.adam.protocol.command.AdamCommand;
import com.yundepot.oaa.protocol.codec.ProtocolEncoder;
import com.yundepot.oaa.serialize.SerializerManager;
import com.yundepot.oaa.serialize.StringMapSerializer;
import com.yundepot.oaa.util.CrcUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

/**
 * @author zhaiyanan
 * @date 2019/5/31 09:45
 */
public class AdamProtocolEncoder implements ProtocolEncoder {

    /**
     * 编码，异常不做处理，由发送方addListener处理
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    public void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
        if (!(msg instanceof AdamCommand)) {
            return;
        }

        AdamCommand command = (AdamCommand) msg;

        // header
        byte[] headerBytes = StringMapSerializer.serialize(command.getHeader());
        int headerLen = 0;
        if (headerBytes != null) {
            headerLen = headerBytes.length;
        }

        // body
        byte serializeCode = Byte.valueOf(command.getHeader(HeaderOption.SERIALIZATION.getKey(), HeaderOption.SERIALIZATION.getDefaultValue()));
        byte[] bodyBytes = SerializerManager.getSerializer(serializeCode).serialize(command.getBody());
        int bodyLen = 0;
        if (bodyBytes != null) {
            bodyLen = bodyBytes.length;
        }

        int index = out.writerIndex();
        out.writeByte(command.getProtocolCode().getCode());
        out.writeByte(command.getProtocolCode().getVersion());
        out.writeShort(command.getCommandCode().value());
        out.writeInt(command.getId());
        out.writeShort(headerLen);
        out.writeInt(bodyLen);

        if (headerLen > 0) {
            out.writeBytes(headerBytes);
        }

        if (bodyLen > 0) {
            out.writeBytes(bodyBytes);
        }

        byte crcSwitch = Byte.valueOf(command.getHeader(HeaderOption.CRC_SWITCH.getKey(), HeaderOption.CRC_SWITCH.getDefaultValue()));
        if (crcSwitch == CrcSwitch.ON.getCode()) {
            byte[] frame = new byte[out.readableBytes()];
            out.getBytes(index, frame);
            out.writeInt(CrcUtil.crc32(frame));
        }

    }
}
