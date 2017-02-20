package com.hengye.share.module.util;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hengye.share.module.base.BaseActivity;

public abstract class BasePreferenceFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener{

    protected abstract String getTitle();

    protected boolean isHideScrollBar(){
        return true;
    }

    protected boolean isRegisterOnSharedPreferenceChangeListener(){
        return false;
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

        if(isRegisterOnSharedPreferenceChangeListener()){
            PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .registerOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(isRegisterOnSharedPreferenceChangeListener()) {
            PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
