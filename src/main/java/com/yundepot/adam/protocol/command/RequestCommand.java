package com.yundepot.adam.protocol.command;

import com.yundepot.oaa.protocol.command.CommandCode;
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

    public RequestCommand(CommandCode code) {
        super(code);
        this.setId(IdGenerator.nextId());
    }

    public RequestCommand(Object content) {
        this();
        this.setBody(content);
    }

    public long getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(long arriveTime) {
        this.arriveTime = arriveTime;
    }
}
