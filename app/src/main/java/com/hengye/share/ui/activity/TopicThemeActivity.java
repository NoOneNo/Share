package com.hengye.share.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.hengye.share.R;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.fragment.TopicPageFragment;
import com.hengye.share.ui.presenter.TopicPagePresenter;

public class TopicThemeActivity extends BaseActivity{

    @Override
    protected void handleBundleExtra() {
        mTopicTheme = getIntent().getStringExtra("topicTheme");

        if(mTopicTheme == null) {
            Uri data = getIntent().getData();
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
    protected int getLayoutResId() {
        return R.layout.activity_topic_theme;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        if(mTopicTheme == null){
            TopicThemeActivity.this.finish();
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

















