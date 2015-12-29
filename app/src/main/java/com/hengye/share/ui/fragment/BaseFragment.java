package com.hengye.share.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hengye.share.util.RequestManager;


public class BaseFragment extends Fragment {

    public final static String BASE_FRAGMENT = "BaseFragment";

    public String getTitle(){
        return null;
    }

    protected String getRequestTag(){
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleBundleExtra();
    }

    protected void handleBundleExtra(){

    }

    @Override
    public void onDestroy() {
        cancelPendingRequestsIfNeeded();
        super.onDestroy();
    }

    protected void cancelPendingRequestsIfNeeded(){
        if(getRequestTag() != null){
            RequestManager.cancelPendingRequests(getRequestTag());
        }
    }
}
