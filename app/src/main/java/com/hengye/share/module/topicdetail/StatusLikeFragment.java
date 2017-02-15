package com.hengye.share.module.topicdetail;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.model.UserInfo;
import com.hengye.share.module.topic.ShareLoadDataCallbackFragment;

public class StatusLikeFragment extends ShareLoadDataCallbackFragment<UserInfo> {

    @Override
    public int getLayoutResId() {
        return super.getLayoutResId();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showEmpty();
    }
}
