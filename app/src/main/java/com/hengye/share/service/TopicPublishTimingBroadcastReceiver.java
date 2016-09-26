package com.hengye.share.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hengye.share.model.greenrobot.TopicDraft;
import com.hengye.share.model.greenrobot.TopicDraftHelper;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;

import java.util.List;

/**
 * Created by yuhy on 2016/9/26.
 */

public class TopicPublishTimingBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        long timing = intent.getLongExtra("timing", 0);

        List<TopicDraft> drafts = TopicDraftHelper.getTimingTopicDraft();

        if(!CommonUtil.isEmpty(drafts)){
            for(TopicDraft draft : drafts){
                if(draft.getTiming() == timing){
                    //开始发送
                    L.debug("TopicPublishTimingBroadcastReceiver find timingTask");
                    draft.cancelTiming();
                    TopicDraftHelper.saveTopicDraft(draft, TopicDraft.NORMAL);
                    TopicPublishService.publish(context, draft);
                    break;
                }
            }
        }
    }
}























