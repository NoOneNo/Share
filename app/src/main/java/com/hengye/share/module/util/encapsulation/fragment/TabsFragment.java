package com.hengye.share.module.util.encapsulation.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.View;

import com.hengye.share.module.util.encapsulation.view.listener.OnScrollToTopAndBottomListener;
import com.hengye.share.util.L;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuhy on 16/7/18.
 */
public abstract class TabsFragment extends ViewPagerFragment {

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        L.debug("onViewCreated invoke()");
        if (delayTabInit() == 0) {
            setUpTabs(savedInstanceState);
        } else {
            (new Handler()).postDelayed(new Runnable() {
                public void run() {
                    setUpTabs(savedInstanceState);
                }
            }, (long) delayTabInit());
        }
    }

    @Override
    public TabsAdapter getAdapter() {
        return mAdapter;
    }

    TabsAdapter mAdapter;
    ArrayList<TabItem> mItems;

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("items", mItems);

        L.debug("onSaveInstanceState invoke()");
    }

    protected void setUpTabs(Bundle savedInstanceState) {

        L.debug("setUpTabs invoke()");

        mItems = savedInstanceState == null ? generateTabs() : (ArrayList<TabItem>) savedInstanceState.getSerializable("items");

        // 初始化的时候，移除一下Fragment
        if (savedInstanceState != null) {
            List<Fragment> fragmentList = getFragmentManager().getFragments();
            if(fragmentList != null){
                for(Fragment fragment : fragmentList){
                    if (fragment != null) {
                        getFragmentManager().beginTransaction()
                                .remove(fragment).commit();
                    }
                }
            }
        }
        getViewPager().setAdapter(mAdapter = new TabsAdapter(getFragmentManager()));
        int currentPosition = savedInstanceState == null ? getDefaultSelectPosition() : savedInstanceState.getInt("position");
        L.debug("currentPosition : {}, savedInstanceState == null is : {}", currentPosition, savedInstanceState == null);
        getViewPager().setCurrentItem(currentPosition);
    }

    protected void updateViewPager() {
        mItems = generateTabs();
        mAdapter.notifyDataSetChanged();
    }


    protected void destroyFragments() {
        L.debug("destroyFragments invoke()");
    }

    protected abstract ArrayList<TabItem> generateTabs();

    protected abstract BaseFragment newFragment(TabItem tabItem);

    protected int delayTabInit() {
        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        try {
            destroyFragments();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public BaseFragment getCurrentFragment() {
        return getFragment(getCurrentPosition());
    }

    public BaseFragment getFragment(int position) {
        return mAdapter.getItem(position);
    }


    public void setOtherTabScrollToTop(){
        if(mAdapter == null){
            return;
        }
        for(int i = 0; i < mAdapter.getCount(); i++){
            if(i == getCurrentPosition()){
                continue;
            }
            BaseFragment fragment = getFragment(i);
            if(fragment instanceof OnScrollToTopAndBottomListener){
                ((OnScrollToTopAndBottomListener) fragment).onScrollToTop(false);
            }
        }
    }

    class TabsAdapter extends FragmentPagerAdapter {

        Map<String, BaseFragment> mFragments;

        public TabsAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new HashMap<>();
        }

        @Override
        public BaseFragment getItem(int position) {
            BaseFragment fragment = mFragments.get(this.makeFragmentName(position));
            if (fragment == null) {
                fragment = newFragment(mItems.get(position));
                mFragments.put(this.makeFragmentName(position), fragment);
//                L.debug("position is null :{}", position);
            }
//            L.debug("position is not null : {}", position);

            return fragment;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        public CharSequence getPageTitle(int position) {
            return (mItems.get(position)).getTitle();
        }

        protected String makeFragmentName(int position) {
            return mItems.get(position).getTitle();
        }

        public Map<String, BaseFragment> getFragments() {
            return mFragments;
        }

        public void clear() {
            mFragments.clear();
        }

        public BaseFragment getFragment(String tabTitle) {
            if (mFragments != null && !TextUtils.isEmpty(tabTitle)) {
                for (int i = 0; i < mItems.size(); ++i) {
                    if (tabTitle.equals((mItems.get(i)).getTitle())) {
                        return mFragments.get(makeFragmentName(i));
                    }
                }
                return null;
            } else {
                return null;
            }
        }
    }

    public static class TabItem implements Serializable {
        private static final long serialVersionUID = -1162756298239591517L;
        private int type;
        private String title;
        private Serializable tag;

        public TabItem() {
        }

        public TabItem(int type, String title) {
            this.type = type;
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Serializable getTag() {
            return tag;
        }

        public void setTag(Serializable tag) {
            this.tag = tag;
        }
    }

}
