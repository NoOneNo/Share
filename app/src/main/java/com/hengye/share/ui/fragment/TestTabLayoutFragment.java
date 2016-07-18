package com.hengye.share.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.hengye.share.ui.fragment.encapsulation.TabLayoutFragment;

import java.util.ArrayList;

/**
 * Created by yuhy on 16/7/18.
 */
public class TestTabLayoutFragment extends TabLayoutFragment {

    @Override
    public String getTitle() {
        return "test";
    }

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected ArrayList<TabItem> generateTabs() {

        ArrayList<TabItem> tabItems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TabItem tabItem = new TabItem();
            tabItem.setTitle("test" + (i + 1));
            tabItems.add(tabItem);
        }
        return tabItems;
    }

    @Override
    protected Fragment newFragment(TabItem tabItem) {
        return new TestContentFragment();
    }
}