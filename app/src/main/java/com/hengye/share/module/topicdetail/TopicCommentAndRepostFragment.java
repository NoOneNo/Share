package com.hengye.share.module.topicdetail;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.module.util.encapsulation.fragment.BaseFragment;
import com.hengye.share.module.topic.StatusFragment;
import com.hengye.share.module.topic.TopicFragment;
import com.hengye.share.module.util.encapsulation.fragment.TabLayoutFragment;
import com.hengye.share.ui.widget.pulltorefresh.PullToRefreshLayout;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.ResUtil;
import com.hengye.swiperefresh.listener.SwipeListener;

import java.util.ArrayList;

/**
 * Created by yuhy on 2016/10/25.
 */

public class TopicCommentAndRepostFragment extends TabLayoutFragment{

    public static Bundle getBundle(Topic topic){
        Bundle bundle = new Bundle();
        bundle.putSerializable("topic", topic);
        return bundle;
    }

    public static TopicCommentAndRepostFragment newInstance(Topic topic) {
        TopicCommentAndRepostFragment fragment = new TopicCommentAndRepostFragment();
        fragment.setArguments(getBundle(topic));
        return fragment;
    }

    Topic mTopic;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_viewpager;
    }

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        mTopic = (Topic) bundle.getSerializable("topic");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mPullToRefresh != null) {
            initPullToRefresh();
        }
    }


    @ColorRes
    public int getTabTextNormalColor(){
        return R.color.text_grey_light;
    }

    @ColorRes
    public int getTabTextSelectColor(){
        return R.color.text_grey;
    }

    @Override
    protected int getOffscreenPageLimit() {
        return super.getOffscreenPageLimit();
    }

    @Override
    protected ArrayList<TabItem> generateTabs() {
        ArrayList<TabItem> tabItems = new ArrayList<>();
        TabItem tabItem1 = new TabItem(0, ResUtil.getString(R.string.label_topic_comment_number, DataUtil.getCounter(mTopic.getCommentsCount())));
        TabItem tabItem2 = new TabItem(1, ResUtil.getString(R.string.label_topic_repost_number, DataUtil.getCounter(mTopic.getRepostsCount())));
        TabItem tabItem3 = new TabItem(2, ResUtil.getString(R.string.label_topic_attitude_number, DataUtil.getCounter(mTopic.getAttitudesCount())));
        tabItems.add(tabItem1);
        tabItems.add(tabItem2);
        tabItems.add(tabItem3);
        return tabItems;
    }

    TopicCommentFragment mCommentFragment, mRepostFragment;
    TopicLikeFragment mLikeFragment;
    PullToRefreshLayout mPullToRefresh;

    @Override
    protected BaseFragment newFragment(TabItem tabItem) {
        BaseFragment fragment = null;
        switch (tabItem.getType()){
            case 0:
                fragment = mCommentFragment = TopicCommentFragment.newInstance(mTopic, true);
                mCommentFragment.setLoadDataCallBack(getLoadDataCallBack(mCommentFragment));
                break;

            case 1:
                fragment = mRepostFragment = TopicCommentFragment.newInstance(mTopic, false);
                mRepostFragment.setLoadDataCallBack(getLoadDataCallBack(mRepostFragment));
                break;
            case 2:
                fragment = mLikeFragment = new TopicLikeFragment();
                mLikeFragment.setLoadDataCallBack(getLoadDataCallBack(mLikeFragment));
        }
        return fragment;
    }

    public void setPullToRefresh(PullToRefreshLayout pullToRefresh) {
        mPullToRefresh = pullToRefresh;
    }

//    public void setOtherTabScrollToTop(){
//        int currentPosition = getCurrentPosition();
//        StatusFragment target = currentPosition == 0 ? mRepostFragment : mCommentFragment;
//
//        if(target != null){
//            target.scrollToTop();
//        }
//    }

    private void initPullToRefresh(){
        getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                if(position == 1 && mTopicFragment != null) {
//                    mPullToRefresh.setScrollUpChild(mTopicFragment.getRecyclerView());
//                }else{
//                    mPullToRefresh.setScrollUpChild(null);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mPullToRefresh.setOnRefreshListener(new SwipeListener.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mCommentFragment != null) {
                    mCommentFragment.onRefresh();
                }
                if(mRepostFragment != null) {
                    mRepostFragment.onRefresh();
                }
            }
        });
    }

    public StatusFragment.LoadDataCallBack getLoadDataCallBack(final BaseFragment baseFragment){
        return new StatusFragment.LoadDataCallBack(){
            @Override
            public void initView() {
                PullToRefreshLayout pullToRefresh = (PullToRefreshLayout) baseFragment.findViewById(R.id.pull_to_refresh);
                if(pullToRefresh != null) {
                    pullToRefresh.setRefreshEnable(false);
                }

            }

            @Override
            public void refresh(boolean isRefreshing) {
                if (isRefreshing) {
//                    mSwipeRefresh.setRefreshing(true, false);
                    if(baseFragment instanceof SwipeListener.OnRefreshListener){
                        ((SwipeListener.OnRefreshListener) baseFragment).onRefresh();
                    }else if(baseFragment instanceof TopicFragment){
                        ((TopicFragment) baseFragment).getPullToRefresh().getOnRefreshListener().onRefresh();
                    }else{
                        mPullToRefresh.getOnRefreshListener().onRefresh();
                    }
                } else {
                    mPullToRefresh.setRefreshing(false);
                }
            }
        };
    }
}
