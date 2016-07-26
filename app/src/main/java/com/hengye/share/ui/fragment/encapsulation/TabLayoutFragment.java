package com.hengye.share.ui.fragment.encapsulation;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.hengye.share.R;

/**
 * Created by yuhy on 16/7/18.
 */
public abstract class TabLayoutFragment extends TabsFragment {

    @Override
    public int getContentResId() {
        return R.layout.fragment_tablayout;
    }

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpTabLayout(savedInstanceState);
    }

    TabLayout mTabLayout;

    protected TabLayout findTabLayout(){
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        if(tabLayout == null){
            tabLayout = (TabLayout)getActivity().findViewById(R.id.tab);
        }
        return tabLayout;
    }

    public TabLayout getTabLayout(){
        return mTabLayout;
    }

    protected void setUpTabLayout(Bundle savedInstanceState){
        mTabLayout = findTabLayout();
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabTextColors(getResources().getColor(getTabTextNormalColor()), getResources().getColor(getTabTextSelectColor()));
        mTabLayout.setupWithViewPager(getViewPager());
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                mTabLayout.setScrollPosition(TabLayoutFragment.this.mCurrentPosition, 0.0F, true);
            }
        }, 150L);
    }

    @ColorRes
    public int getTabTextNormalColor(){
        return R.color.white;
    }

    @ColorRes
    public int getTabTextSelectColor(){
        return R.color.white;
    }
}
