package com.yundepot.adam.protocol.codec;

import com.yundepot.adam.config.HeaderOption;
import com.yundepot.adam.protocol.CrcSwitch;
import com.yundepot.adam.protocol.command.AdamCommand;
import com.yundepot.adam.protocol.command.ResponseCommand;
import com.yundepot.oaa.protocol.codec.ProtocolEncoder;
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
        command.serialize();

        int index = out.writerIndex();
        out.writeByte(command.getProtocolCode().getCode());
        out.writeByte(command.getProtocolCode().getVersion());
        out.writeShort(command.getCommandCode().value());
        out.writeInt(command.getId());
        out.writeByte(command.getSerializer());

        if (command instanceof ResponseCommand) {
            ResponseCommand responseCommand = (ResponseCommand) command;
            out.writeShort(responseCommand.getResponseStatus().getValue());
        }
        out.writeShort(command.getHeaderLen());
        out.writeInt(command.getBodyLen());

        if (command.getHeaderLen() > 0) {
            out.writeBytes(command.getHeaderBytes());
        }

        if (command.getBodyLen() > 0) {
            out.writeBytes(command.getBodyBytes());
        }

        byte crcSwitch = Byte.valueOf(command.getHeader(HeaderOption.CRC_SWITCH.getKey(), HeaderOption.CRC_SWITCH.getDefaultValue()));
        if (crcSwitch == CrcSwitch.ON.getCode()) {
            byte[] frame = new byte[out.readableBytes()];
            out.getBytes(index, frame);
            out.writeInt(CrcUtil.crc32(frame));
        }
    }
}
