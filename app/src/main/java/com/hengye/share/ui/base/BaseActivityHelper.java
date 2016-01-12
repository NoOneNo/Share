package com.hengye.share.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public abstract class BaseActivityHelper{

    public BaseActivityHelper(Activity activity){
        this.mActivity = activity;
    }

    private Activity mActivity;

    protected Activity getActivity() {
        return mActivity;
    }

    public View findViewById(int id) {
        return mActivity.findViewById(id);
    }

    protected abstract void onCreate(Bundle savedInstanceState);

    protected abstract void onPostCreate(Bundle savedInstanceState);

    protected abstract void onResume();
}
