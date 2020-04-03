package com.adongs.implement.sightseer;

import org.springframework.context.ApplicationEvent;

/**
 * 忽略的url对象
 * @author yudong
 * @version 1.0
 */
public class SightseerEven extends ApplicationEvent {

    public SightseerEven(Object source) {
        super(source);
    }
}
