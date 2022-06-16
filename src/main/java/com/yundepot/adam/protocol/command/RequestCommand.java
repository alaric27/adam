package com.yundepot.adam.protocol.command;

import com.yundepot.oaa.protocol.command.CommandType;
import com.yundepot.oaa.util.IdGenerator;

/**
 * @author zhaiyanan
 * @date 2019/6/14 13:14
 */
public class RequestCommand extends AdamCommand {

    private static final long serialVersionUID = -5227506879608157098L;
    private transient long arriveTime = System.currentTimeMillis();

    public RequestCommand() {
        this(AdamCommandCode.REQUEST);
    }

    public RequestCommand(byte commandType, short commandCode) {
        this(commandType, commandCode, null);
    }

    public RequestCommand(short commandCode, Object body) {
        this(CommandType.REQUEST.value(), commandCode, body);
    }

    public RequestCommand(Object body) {
        this(CommandType.REQUEST.value(), AdamCommandCode.REQUEST.value(), body);
    }

    public RequestCommand(byte commandType, short commandCode, Object body) {
        super.setCommandCode(commandCode);
        super.setBody(body);
        super.setId(IdGenerator.nextId());
    }

    public long getArriveTime() {
        return arriveTime;
    }
}
