package com.hengye.share.ui.fragment;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hengye.share.BaseActivity;

public abstract class BasePreferenceFragment extends PreferenceFragment {

    protected abstract String getTitle();

    protected boolean isHideScrollBar(){
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if(isHideScrollBar() && view != null){
            ListView listView = (ListView) view.findViewById(android.R.id.list);
            if(listView != null){
                listView.setVerticalScrollBarEnabled(false);
            }
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof BaseActivity){
            BaseActivity activity = (BaseActivity) getActivity();
            activity.updateToolbarTitle(getTitle());
        }
    }
}
