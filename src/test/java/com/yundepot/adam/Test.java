package com.yundepot.adam;


import com.yundepot.oaa.common.TimerHolder;
import io.netty.util.Timeout;

import java.util.concurrent.TimeUnit;

/**
 * @author zhaiyanan
 * @date 2019/5/14 17:13
 */
public class Test {
    public static void main(String[] args) throws Exception{
        Timeout timeout = TimerHolder.getTimer().newTimeout(to -> {
            System.out.println(Thread.currentThread().getName());
        }, 1000, TimeUnit.MILLISECONDS);

        TimerHolder.getTimer().newTimeout(to -> {
            System.out.println(Thread.currentThread().getName());
        }, 1000, TimeUnit.MILLISECONDS);

        TimerHolder.getTimer().newTimeout(to -> {
            System.out.println(Thread.currentThread().getName());
        }, 1000, TimeUnit.MILLISECONDS);


        while (true) {
            System.out.println(timeout.isCancelled() + " " + timeout.isExpired());
            Thread.sleep(10000);
        }
    }
}
