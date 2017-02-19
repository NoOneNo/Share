package com.hengye.share.util.handler;

import com.hengye.share.module.util.encapsulation.base.NumberPager;
import com.hengye.share.util.thirdparty.WBUtil;

/**
 * Created by yuhy on 16/7/28.
 */
public class StatusNumberPager extends NumberPager {

    public StatusNumberPager() {
        this(1);
    }

    public StatusNumberPager(int firstNumber) {
        this(firstNumber, WBUtil.getWBStatusRequestCount());
    }

    public StatusNumberPager(int firstNumber, int pageSize){
        super(firstNumber, pageSize);
    }
}
