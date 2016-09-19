package com.hengye.share.module.topic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.hengye.share.R;
import com.hengye.share.module.base.BaseActivity;

public class TopicThemeActivity extends BaseActivity{

    @Override
    protected void handleBundleExtra(Intent intent) {
        mTopicTheme = intent.getStringExtra("topicTheme");

        if(mTopicTheme == null) {
            Uri data = intent.getData();
            if (data != null && data.toString() != null) {
                String value = data.toString();
                int start = value.indexOf("#");
                int end = value.lastIndexOf("#");
                if(start != -1 && end != -1) {
                    mTopicTheme = value.substring(start + 1, end);
                }
            }
        }
    }

    public static Intent getStartIntent(Context context, String topicTheme){
        Intent intent = new Intent(context, TopicThemeActivity.class);
        intent.putExtra("topicTheme", topicTheme);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_topic_theme;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        if(mTopicTheme == null){
            finish();
        }else{
            initView();
        }
    }

    //话题
    private String mTopicTheme;

    private void initView(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, TopicPageFragment.newInstance(TopicPagePresenter.TopicType.THEME, mTopicTheme))
                .commit();
    }


}

















