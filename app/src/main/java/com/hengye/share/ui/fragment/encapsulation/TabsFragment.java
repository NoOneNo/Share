package com.hengye.share.ui.fragment.encapsulation;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.ui.base.BaseFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuhy on 16/7/18.
 */
public abstract class TabsFragment extends ViewPagerFragment{

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    public FragmentPagerAdapter getAdapter(){
        return mAdapter;
    }

    FragmentPagerAdapter mAdapter;
    ArrayList<TabItem> mItems;

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("items", mItems);
    }

    protected void setUpTabs(Bundle savedInstanceState) {

        mItems = savedInstanceState == null ? generateTabs() : (ArrayList<TabItem>) savedInstanceState.getSerializable("items");

        getViewPager().setAdapter(mAdapter = new TabsAdapter(getFragmentManager()));
    }

    protected void updateViewPager() {
        mItems = generateTabs();
        mAdapter.notifyDataSetChanged();
    }

    protected void destroyFragments() {
        if (getActivity() != null) {
            if (getActivity().isDestroyed()) {
                return;
            }

            try {
//                FragmentTransaction e1 = getFragmentManager().beginTransaction();
//                Set keySet = mFragments.keySet();
//                Iterator i$ = keySet.iterator();
//
//                while (i$.hasNext()) {
//                    String key = (String) i$.next();
//                    if (mFragments.get(key) != null) {
//                        e1.remove(mFragments.get(key));
//                        L.debug("AFragment-Tabs", "remove fragment , key = " + key);
//                    }
//                }
//
//                e1.commit();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

    }

    protected abstract ArrayList<TabItem> generateTabs();

    protected abstract BaseFragment newFragment(TabItem tabItem);

    protected int delayTabInit() {
        return 0;
    }

    public void onDestroy() {
        try {
            destroyFragments();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    public Fragment getCurrentFragment() {
        return getFragment(getCurrentPosition());
    }

    public Fragment getFragment(int position) {
        return mAdapter.getItem(position);
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
            }

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

        public BaseFragment getFragment(String tabTitle) {
            if (mFragments != null && !TextUtils.isEmpty(tabTitle)) {
                for (int i = 0; i < mItems.size(); ++i) {
                    if (tabTitle.equals(((TabItem) mItems.get(i)).getTitle())) {
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
