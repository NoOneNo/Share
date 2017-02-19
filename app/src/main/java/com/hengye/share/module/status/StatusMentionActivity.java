package com.hengye.share.module.status;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.util.ResUtil;

import java.util.ArrayList;
import java.util.List;

public class StatusMentionActivity extends BaseActivity{

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
        return R.layout.activity_status_mention;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private StatusNotifyFragmentPager mAdapter;

    private void initView(){

        initToolbar();
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mAdapter = new StatusNotifyFragmentPager(getSupportFragmentManager(), this, getStatusGroups()));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private List<StatusPresenter.StatusGroup> getStatusGroups(){
        ArrayList<StatusPresenter.StatusGroup> statusGroupGroups = new ArrayList<>();

        statusGroupGroups.add(new StatusPresenter.StatusGroup(StatusPresenter.StatusType.TOPIC_AT_ME));
        statusGroupGroups.add(new StatusPresenter.StatusGroup(StatusPresenter.StatusType.COMMENT_AT_ME));
        return statusGroupGroups;
    }
}
