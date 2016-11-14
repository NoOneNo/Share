package com.hengye.share.module.topic;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.util.ResUtil;

import java.util.ArrayList;
import java.util.List;

public class TopicMentionActivity extends BaseActivity{

    @Override
    protected boolean setToolBar() {
        return false;
    }

    @Override
    protected CharSequence getToolbarTitle() {
        return ResUtil.getString(R.string.title_page_mention);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_topic_mention;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
    }

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TopicFragmentPager mAdapter;

    private void initView(){

        initToolbar();
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mAdapter = new TopicFragmentPager(getSupportFragmentManager(), this, getTopicGroups()));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private List<TopicPresenter.TopicGroup> getTopicGroups(){
        ArrayList<TopicPresenter.TopicGroup> topicGroupGroups = new ArrayList<>();

        topicGroupGroups.add(new TopicPresenter.TopicGroup(TopicPresenter.TopicType.TOPIC_AT_ME));
        topicGroupGroups.add(new TopicPresenter.TopicGroup(TopicPresenter.TopicType.COMMENT_AT_ME));
        return topicGroupGroups;
    }

//    @Override
//    public void onToolbarDoubleClick(Toolbar toolbar) {
//        mAdapter.getItem(mViewPager.getCurrentItem()).onToolbarDoubleClick(toolbar);
//    }
}
