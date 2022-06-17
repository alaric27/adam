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
        setCommandCode(AdamCommandCode.REQUEST.value());
        setCommandType(CommandType.REQUEST.value());
        setId(IdGenerator.nextId());
    }

    public RequestCommand(Object body) {
        this();
        setBody(body);
    }

    public long getArriveTime() {
        return arriveTime;
    }
}
