package com.hengye.share.module.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.util.encapsulation.fragment.RecyclerRefreshFragment;
import com.hengye.share.module.util.encapsulation.view.listener.OnDoubleTapListener;
import com.hengye.share.ui.widget.common.CommonToolBar;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.List;

/**
 * Created by yuhy on 16/7/27.
 */
public abstract class ShareRecyclerFragment<T> extends RecyclerRefreshFragment<T> {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addDoubleTabListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeDoubleTabListener();
    }

    @Override
    public void onToolbarDoubleClick(Toolbar toolbar) {
        onScrollToTop(false);
    }

    OnDoubleTapListener mOnDoubleTapListener;

    private void addDoubleTabListener() {
        CommonToolBar toolbar = getShareToolbar();
        if (toolbar != null) {
            toolbar.addOnDoubleTapListener(mOnDoubleTapListener = new OnDoubleTapListener() {
                @Override
                public void onDoubleTap(View view) {
                    onToolbarDoubleClick((Toolbar) view);
                }
            });
        }
    }

    private void removeDoubleTabListener() {
        if (mOnDoubleTapListener != null) {
            CommonToolBar toolbar = getShareToolbar();
            if (toolbar != null) {
                toolbar.removeOnDoubleTapListener(mOnDoubleTapListener);
                mOnDoubleTapListener = null;
            }
        }
    }

    private CommonToolBar getShareToolbar() {
        if (getActivity() instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) getActivity();
            if (baseActivity.getToolbar() != null) {
                return baseActivity.getToolbar();
            }
        }
        return null;
    }


    @Override
    public boolean canLoadMore(List<T> data, int type) {
        int pageSize;
        if (getPager() != null) {
            pageSize = getPager().getPageSize();
        } else {
            pageSize = WBUtil.getWBStatusRequestCount();
        }

        if (data != null && data.size() < pageSize / 2) {
            return false;
        } else {
            return super.canLoadMore(data, type);
        }
    }
}
















