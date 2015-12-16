package com.hengye.share.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.hengye.share.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.ui.fragment.TopicNotifyFragment;

public class TopicNotifyActivity extends BaseActivity{

    @Override
    protected String getRequestTag() {
        return "TopicNotifyActivity";
    }

    @Override
    protected boolean setCustomTheme() {
        return super.setCustomTheme();
    }

    @Override
    protected boolean setToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_topic_notify);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private void initView(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new TopicNotifyFragmentPager());
        mTabLayout.setupWithViewPager(mViewPager);
    }


    class TopicNotifyFragmentPager extends FragmentPagerAdapter{

        public TopicNotifyFragmentPager(){
            super(getSupportFragmentManager());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "评论";
            }else{
                return "提及";
            }
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            if(position == 0){
                fragment = TopicNotifyFragment.newInstance(TopicNotifyFragment.NOTIFY_COMMENT);
            }else{
                fragment = TopicNotifyFragment.newInstance(TopicNotifyFragment.NOTIFY_MENTION);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
