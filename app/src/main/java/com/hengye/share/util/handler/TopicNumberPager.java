package com.hengye.share.util.handler;

import com.hengye.share.module.util.encapsulation.base.NumberPager;
import com.hengye.share.util.thirdparty.WBUtil;

/**
 * Created by yuhy on 16/7/28.
 */
public class TopicNumberPager extends NumberPager {

    public TopicNumberPager() {
        this(1);
    }

    public TopicNumberPager(int firstNumber) {
        super(firstNumber, WBUtil.getWBTopicRequestCount());
    }
}
