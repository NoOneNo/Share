package com.hengye.share.module.status;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.hengye.share.R;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.util.ResUtil;

/**
 * 微博话题
 */
public class StatusTopicActivity extends BaseActivity{

    @Override
    protected void handleBundleExtra(Intent intent) {
        mStatusTopic = intent.getStringExtra("statusTopic");

        if(mStatusTopic == null) {
            Uri data = intent.getData();
            if (data != null && data.toString() != null) {
                String value = data.toString();
                int start = value.indexOf("#");
                int end = value.lastIndexOf("#");
                if(start != -1 && end != -1) {
                    mStatusTopic = value.substring(start + 1, end);
                }
            }
        }
    }

    public static Intent getStartIntent(Context context, String statusTheme){
        Intent intent = new Intent(context, StatusTopicActivity.class);
        intent.putExtra("statusTopic", statusTheme);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(mStatusTopic == null){
            finish();
        }else{
            initView();
        }
    }

    //话题
    private String mStatusTopic;

    private void initView(){

        updateToolbarTitle(ResUtil.getString(R.string.label_hot_topic_modifier, mStatusTopic));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, StatusPageFragment.newInstance(StatusPagePresenter.StatusType.THEME, mStatusTopic))
                .commit();
    }


}

















