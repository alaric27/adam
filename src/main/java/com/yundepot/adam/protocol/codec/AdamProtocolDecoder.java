package com.yundepot.adam.protocol.codec;

import com.yundepot.adam.protocol.command.AdamCommandCode;
import com.yundepot.adam.protocol.CrcSwitch;
import com.yundepot.adam.protocol.AdamProtocol;
import com.yundepot.adam.protocol.command.RequestCommand;
import com.yundepot.adam.protocol.command.ResponseCommand;
import com.yundepot.oaa.common.ResponseStatus;
import com.yundepot.oaa.exception.CodecException;
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
        short cmdCode = checkCommandCode(in);
        if (cmdCode == AdamCommandCode.REQUEST.value() || cmdCode == AdamCommandCode.ONE_WAY.value()
                || cmdCode == AdamCommandCode.HEARTBEAT_REQUEST.value()) {
            decodeRequest(in, out);
        } else if (cmdCode == AdamCommandCode.RESPONSE.value() || cmdCode == AdamCommandCode.HEARTBEAT_RESPONSE.value()) {
            decodeResponse(in, ctx, out);
        }
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

    /**
     * 解码响应
     * @param in
     * @param out
     */
    private void decodeResponse(ByteBuf in, ChannelHandlerContext ctx, List<Object> out) throws Exception {
        int startIndex = in.readerIndex();
        in.markReaderIndex();
        if (in.readableBytes() < AdamProtocol.getResponseHeaderLength()) {
            in.resetReaderIndex();
            return;
        }

        in.readByte();
        in.readByte();
        short cmdCode = in.readShort();
        int requestId = in.readInt();
        byte serializer = in.readByte();
        byte crcSwitchValue = in.readByte();
        short status = in.readShort();
        short classLen = in.readShort();
        short headerLen = in.readShort();
        int contentLen = in.readInt();
        byte[] clazz = null;
        byte[] header = null;
        byte[] content = null;

        boolean crcSwitchOn = CrcSwitch.ON.getCode() == crcSwitchValue;
        int lengthAtLeast = classLen + headerLen + contentLen;
        if (crcSwitchOn) {
            lengthAtLeast += 4;
        }

        // 校验数据是否足够
        if (in.readableBytes() < lengthAtLeast) {
            in.resetReaderIndex();
            return;
        }

        if (classLen > 0) {
            clazz = new byte[classLen];
            in.readBytes(clazz);
        }
        if (headerLen > 0) {
            header = new byte[headerLen];
            in.readBytes(header);
        }
        if (contentLen > 0) {
            content = new byte[contentLen];
            in.readBytes(content);
        }
        if (crcSwitchOn) {
            checkCRC(in, startIndex);
        }

        ResponseCommand command = createResponseCommand(cmdCode);
        command.setId(requestId);
        command.setSerializer(serializer);
        command.setCrcSwitch(CrcSwitch.valueOf(crcSwitchValue));
        command.setResponseStatus(ResponseStatus.valueOf(status));
        command.setClazzBytes(clazz);
        command.setHeaderBytes(header);
        command.setContentBytes(content);
        command.setResponseTimeMillis(System.currentTimeMillis());
        command.setResponseHost((InetSocketAddress) ctx.channel().remoteAddress());
        command.deserialize();
        out.add(command);
    }

    /**
     * 解码请求
     * @param in
     * @param out
     */
    private void decodeRequest(ByteBuf in, List<Object> out) throws Exception {
        int startIndex = in.readerIndex();
        in.markReaderIndex();

        if (in.readableBytes() < AdamProtocol.getRequestHeaderLength()) {
            in.resetReaderIndex();
            return;
        }

        in.readByte();
        in.readByte();
        short cmdCode = in.readShort();

        int requestId = in.readInt();
        byte serializer = in.readByte();
        byte crcSwitchValue = in.readByte();
        int timeout = in.readInt();
        short classLen = in.readShort();
        short headerLen = in.readShort();
        int contentLen = in.readInt();
        byte[] clazz = null;
        byte[] header = null;
        byte[] content = null;

        boolean crcSwitchOn = CrcSwitch.ON.getCode() == crcSwitchValue;
        int lengthAtLeast = classLen + headerLen + contentLen;
        if (crcSwitchOn) {
            lengthAtLeast += 4;
        }

        // 校验数据是否足够
        if (in.readableBytes() < lengthAtLeast) {
            in.resetReaderIndex();
            return;
        }

        if (classLen > 0) {
            clazz = new byte[classLen];
            in.readBytes(clazz);
        }
        if (headerLen > 0) {
            header = new byte[headerLen];
            in.readBytes(header);
        }
        if (contentLen > 0) {
            content = new byte[contentLen];
            in.readBytes(content);
        }
        if (crcSwitchOn) {
            checkCRC(in, startIndex);
        }

        RequestCommand command = createRequestCommand(cmdCode);
        command.setId(requestId);
        command.setSerializer(serializer);
        command.setCrcSwitch(CrcSwitch.valueOf(crcSwitchValue));
        command.setTimeout(timeout);
        command.setClazzBytes(clazz);
        command.setHeaderBytes(header);
        command.setContentBytes(content);
        command.deserialize();
        out.add(command);
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

    private ResponseCommand createResponseCommand(short cmdCode) {
        ResponseCommand command = new ResponseCommand();
        command.setCommandCode(AdamCommandCode.valueOf(cmdCode));
        return command;
    }

    private RequestCommand createRequestCommand(short cmdCode) {
        RequestCommand command = new RequestCommand();
        command.setCommandCode(AdamCommandCode.valueOf(cmdCode));
        command.setArriveTime(System.currentTimeMillis());
        return command;
    }
}
