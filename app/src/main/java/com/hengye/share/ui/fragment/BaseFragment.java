package com.hengye.share.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    public final static String BASE_FRAGMENT = "BaseFragment";

    public String getTitle(){
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleBundleExtra();
    }

    protected void handleBundleExtra(){

    }
}
