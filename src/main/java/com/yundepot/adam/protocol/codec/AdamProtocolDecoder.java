package com.yundepot.adam.protocol.codec;

import com.yundepot.adam.config.HeaderOption;
import com.yundepot.adam.protocol.command.AdamCommand;
import com.yundepot.adam.protocol.command.AdamCommandCode;
import com.yundepot.adam.protocol.CrcSwitch;
import com.yundepot.adam.protocol.AdamProtocol;
import com.yundepot.adam.protocol.command.RequestCommand;
import com.yundepot.adam.protocol.command.ResponseCommand;
import com.yundepot.oaa.common.ResponseStatus;
import com.yundepot.oaa.exception.CodecException;
import com.yundepot.oaa.protocol.ProtocolCode;
import com.yundepot.oaa.protocol.codec.ProtocolDecoder;
import com.yundepot.oaa.util.CrcUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author zhaiyanan
 * @date 2019/6/9 14:26
 */
public class AdamProtocolDecoder implements ProtocolDecoder {

    private static final Logger logger = LoggerFactory.getLogger(AdamProtocolDecoder.class);

    private int lessLen = AdamProtocol.getResponseHeaderLength() < AdamProtocol.getRequestHeaderLength() ?
            AdamProtocol.getResponseHeaderLength() : AdamProtocol.getRequestHeaderLength();

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //如果字节不够则不做处理
        if (in.readableBytes() < lessLen) {
            return;
        }
        // 检查协议
        checkProtocol(in);
        // 检查 command code
        checkCommandCode(in);

        int startIndex = in.readerIndex();
        in.markReaderIndex();
        byte protocolCode = in.readByte();
        byte version = in.readByte();
        short cmdCode = in.readShort();
        int requestId = in.readInt();
        byte serializer = in.readByte();

        AdamCommand command = null;
        if (cmdCode == AdamCommandCode.REQUEST.value() || cmdCode == AdamCommandCode.ONE_WAY.value()
                || cmdCode == AdamCommandCode.HEARTBEAT_REQUEST.value()) {
            int timeout = in.readInt();
            command = new RequestCommand();
            RequestCommand cmd = (RequestCommand) command;
            cmd.setTimeout(timeout);
        } else if (cmdCode == AdamCommandCode.RESPONSE.value() || cmdCode == AdamCommandCode.HEARTBEAT_RESPONSE.value()) {
            short status = in.readShort();
            command = new ResponseCommand();
            ResponseCommand cmd = (ResponseCommand) command;
            cmd.setResponseHost((InetSocketAddress) ctx.channel().remoteAddress());
            cmd.setResponseStatus(ResponseStatus.valueOf(status));
        }

        short nriLen = in.readShort();
        short headerLen = in.readShort();
        int bodyLen = in.readInt();
        byte[] nriBytes = null;
        byte[] headerBytes = null;
        byte[] bodyBytes = null;

        // 校验数据是否足够
        int lengthAtLeast = nriLen + headerLen + bodyLen;
        if (in.readableBytes() < lengthAtLeast) {
            in.resetReaderIndex();
            return;
        }

        if (nriLen > 0) {
            nriBytes = new byte[nriLen];
            in.readBytes(nriBytes);
        }

        if (headerLen > 0) {
            headerBytes = new byte[headerLen];
            in.readBytes(headerBytes);
        }
        if (bodyLen > 0) {
            bodyBytes = new byte[bodyLen];
            in.readBytes(bodyBytes);
        }

        command.setProtocolCode(ProtocolCode.getProtocolCode(protocolCode, version));
        command.setCommandCode(AdamCommandCode.valueOf(cmdCode));
        command.setId(requestId);
        command.setSerializer(serializer);
        command.setNriBytes(nriBytes);
        command.setHeaderBytes(headerBytes);
        command.setBodyBytes(bodyBytes);
        command.deserialize();

        byte crcSwitch = Byte.valueOf(command.getHeader(HeaderOption.CRC_SWITCH.getKey(), HeaderOption.CRC_SWITCH.getDefaultValue()));
        if (CrcSwitch.ON.getCode() == crcSwitch) {
            if (in.readableBytes() < 4) {
                in.resetReaderIndex();
                return;
            }
            checkCRC(in, startIndex);
        }
        out.add(command);
    }

    /**
     * 检查command code
     * @param in
     */
    private short checkCommandCode(ByteBuf in) throws CodecException{
        in.markReaderIndex();
        in.readByte();
        in.readByte();
        short cmdCode = in.readShort();
        in.resetReaderIndex();
        if (cmdCode != AdamCommandCode.REQUEST.value() && cmdCode != AdamCommandCode.ONE_WAY.value()
                && cmdCode != AdamCommandCode.RESPONSE.value() && cmdCode != AdamCommandCode.HEARTBEAT_REQUEST.value()
                && cmdCode != AdamCommandCode.HEARTBEAT_RESPONSE.value()) {
            String msg = "Unknown command code: " + cmdCode;
            logger.error(msg);
            throw new CodecException(msg);
        }
        return cmdCode;
    }

    /**
     * 检查协议
     * @param in
     */
    private void checkProtocol(ByteBuf in) throws CodecException{
        in.markReaderIndex();
        byte protocol = in.readByte();
        in.resetReaderIndex();
        if (protocol != AdamProtocol.PROTOCOL_CODE) {
            String msg = "Unknown protocol: " + protocol;
            logger.error(msg);
            throw new CodecException(msg);
        }
    }

    private void checkCRC(ByteBuf in, int startIndex) throws CodecException{
        int endIndex = in.readerIndex();
        int expectedCrc = in.readInt();
        byte[] frame = new byte[endIndex - startIndex];
        in.getBytes(startIndex, frame, 0, endIndex - startIndex);
        int actualCrc = CrcUtil.crc32(frame);
        if (expectedCrc != actualCrc) {
            String err = "CRC check failed!";
            logger.error(err);
            throw new CodecException(err);
        }
    }
}
