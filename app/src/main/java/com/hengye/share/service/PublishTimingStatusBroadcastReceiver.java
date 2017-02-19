package com.hengye.share.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hengye.share.model.greenrobot.StatusDraft;
import com.hengye.share.model.greenrobot.StatusDraftHelper;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;

import java.util.List;

/**
 * Created by yuhy on 2016/9/26.
 */

public class PublishTimingStatusBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        long timing = intent.getLongExtra("timing", 0);

        List<StatusDraft> drafts = StatusDraftHelper.getTimingStatusDraft();

        if(!CommonUtil.isEmpty(drafts)){
            for(StatusDraft draft : drafts){
                if(draft.getTiming() == timing){
                    //开始发送
                    L.debug("PublishTimingStatusBroadcastReceiver find timingTask");
                    draft.cancelTiming();
                    StatusDraftHelper.saveStatusDraft(draft, StatusDraft.NORMAL);
                    StatusPublishService.publish(context, draft);
                    break;
                }
            }
        }
    }
}























