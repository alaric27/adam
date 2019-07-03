package com.yundepot.adam.protocol.command;

import com.yundepot.oaa.protocol.command.CommandCode;
import com.yundepot.oaa.util.IdGenerator;

/**
 * @author zhaiyanan
 * @date 2019/6/14 13:14
 */
public class RequestCommand extends AdamCommand {

    private static final long serialVersionUID = -5227506879608157098L;
    private transient long arriveTime = -1;

    private int timeout = -1;

    public RequestCommand() {
        this(AdamCommandCode.REQUEST);
    }

    public RequestCommand(CommandCode code) {
        super(code);
        this.setId(IdGenerator.nextId());
    }

    public RequestCommand(Object content) {
        this();
        this.setContent(content);
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public long getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(long arriveTime) {
        this.arriveTime = arriveTime;
    }
}
