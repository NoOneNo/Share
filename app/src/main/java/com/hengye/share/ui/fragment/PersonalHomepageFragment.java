package com.hengye.share.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.ui.base.BaseFragment;
import com.hengye.share.ui.fragment.encapsulation.TabLayoutFragment;
import com.hengye.share.ui.presenter.TopicPresenter;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.ResUtil;
import com.hengye.swiperefresh.PullToRefreshLayout;
import com.hengye.swiperefresh.SwipeRefreshLayout;
import com.hengye.swiperefresh.listener.SwipeListener;

import java.util.ArrayList;

/**
 * Created by yuhy on 16/7/18.
 */
public class PersonalHomepageFragment extends TabLayoutFragment{

    public static PersonalHomepageFragment newInstance(WBUserInfo wbUserInfo){
        PersonalHomepageFragment fragment = new PersonalHomepageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("wbUserInfo", wbUserInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        mWbUserInfo = (WBUserInfo)bundle.getSerializable("wbUserInfo");
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_viewpager;
    }

    @Override
    public boolean isLayoutInflateMode() {
        return false;
    }

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViewPager().setOffscreenPageLimit(3);
        if(mSwipeRefresh != null) {
            initSwipeRefresh();
        }
    }

    @Override
    protected int getOffscreenPageLimit() {
        return 3;
    }

    @Override
    protected int getDefaultSelectPosition() {
        return 1;
    }

    WBUserInfo mWbUserInfo;
    @Override
    protected ArrayList<TabItem> generateTabs() {
        ArrayList<TabItem> tabItems = new ArrayList<>();
        TabItem tabItem1 = new TabItem(0, ResUtil.getString(R.string.label_tab_about));
        TabItem tabItem2 = new TabItem(1, ResUtil.getString(R.string.label_tab_topic, DataUtil.getCounter(mWbUserInfo.getStatuses_count())));
        TabItem tabItem3 = new TabItem(2, ResUtil.getString(R.string.label_tab_album));

        tabItems.add(tabItem1);
        tabItems.add(tabItem2);
        tabItems.add(tabItem3);

        return tabItems;
    }

    PersonalHomepageAboutFragment mPersonalHomepageAboutFragment;
    TopicFragment mTopicFragment;
    TopicAlbumFragment mTopicAlbumFragment;

    SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected BaseFragment newFragment(TabItem tabItem) {
        BaseFragment fragment;
        switch (tabItem.getType()){
            case 0:
                mPersonalHomepageAboutFragment = PersonalHomepageAboutFragment.newInstance(mWbUserInfo);
                fragment = mPersonalHomepageAboutFragment;
                break;
            case 1:
                mTopicFragment = TopicFragment.newInstance(TopicPresenter.TopicType.HOMEPAGE, mWbUserInfo.getIdstr(), mWbUserInfo.getName());
                mTopicFragment.setLoadDataCallBack(getLoadDataCallBack(mTopicFragment));
                fragment = mTopicFragment;
                break;
            case 2:
            default:
                mTopicAlbumFragment = TopicAlbumFragment.newInstance(mWbUserInfo.getIdstr(), mWbUserInfo.getName());
                mTopicAlbumFragment.setLoadDataCallBack(getLoadDataCallBack(mTopicAlbumFragment));
                fragment = mTopicAlbumFragment;
                break;

        }
        return fragment;
    }

    public void setSwipeRefresh(SwipeRefreshLayout swipeRefresh) {
        mSwipeRefresh = swipeRefresh;
    }

    @Override
    public boolean onToolbarDoubleClick(Toolbar toolbar) {
        if(getCurrentFragment() != null){
            return ((BaseFragment)getCurrentFragment()).onToolbarDoubleClick(toolbar);
        }
        return super.onToolbarDoubleClick(toolbar);
    }

    private void initSwipeRefresh(){
        getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mSwipeRefresh.setEnabled(state != ViewPager.SCROLL_STATE_DRAGGING);
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (getCurrentPosition()){
                    case 0:
                        mSwipeRefresh.setRefreshing(false);
//                        mPersonalHomepageAboutFragment
                        break;
                    case 1:
                        if(mTopicFragment != null) {
                            mTopicFragment.getPullToRefresh().getOnRefreshListener().onRefresh();
                        }else{
                            mSwipeRefresh.setRefreshing(false);
                        }
                        break;
                    case 2:
                        if(mTopicAlbumFragment != null) {
                            mTopicAlbumFragment.onRefresh();
                        }else{
                            mSwipeRefresh.setRefreshing(false);
                        }
                    default:
                        mSwipeRefresh.setRefreshing(false);
                        break;
                }
            }
        });
    }

    public LoadDataCallBack getLoadDataCallBack(final BaseFragment baseFragment){
        return new LoadDataCallBack(){
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
                    mSwipeRefresh.setRefreshing(true, false);
                    if(baseFragment instanceof SwipeListener.OnRefreshListener){
                        ((SwipeListener.OnRefreshListener) baseFragment).onRefresh();
                    }else if(baseFragment instanceof TopicFragment){
                        ((TopicFragment) baseFragment).getPullToRefresh().getOnRefreshListener().onRefresh();
                    }else{
                        mSwipeRefresh.getOnRefreshListener().onRefresh();
                    }
                } else {
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        };
    }

    public interface LoadDataCallBack {

        void initView();

        void refresh(boolean isRefreshing);
    }
}
