package com.hengye.share.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.adapter.viewpager.TopicFragmentPager;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.presenter.TopicPresenter;

import java.util.ArrayList;
import java.util.List;

public class TopicCommentActivity extends BaseActivity{

    @Override
    protected boolean setToolBar() {
        return false;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_topic_comment;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private void initView(){

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new TopicFragmentPager(getSupportFragmentManager(), this, getTopicGroups()));
//        mViewPager.setAdapter(new TopicNotifyFragmentPager());
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private List<TopicPresenter.TopicGroup> getTopicGroups(){
        ArrayList<TopicPresenter.TopicGroup> topicGroupGroups = new ArrayList<>();
        topicGroupGroups.add(new TopicPresenter.TopicGroup(TopicPresenter.TopicType.COMMENT_TO_ME));
        topicGroupGroups.add(new TopicPresenter.TopicGroup(TopicPresenter.TopicType.COMMENT_BY_ME));
        return topicGroupGroups;
    }

}
