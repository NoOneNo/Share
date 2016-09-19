package com.hengye.share.module.test;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.ContentFragment;

/**
 * Created by yuhy on 16/7/18.
 */
public class TestContentFragment extends ContentFragment {

    @Override
    public String getTitle() {
        return "test";
    }

    @Override
    public int getContentResId() {
        return R.layout.item_search;
    }

    @Override
    public int getEmptyResId() {
        return R.layout.activity_login;
    }

    @Override
    public void initContent(@Nullable Bundle savedInstanceState) {
//        showContent();
//        showLoading();
        showEmpty();
    }
}
