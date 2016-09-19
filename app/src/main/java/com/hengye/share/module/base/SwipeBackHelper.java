package com.hengye.share.module.base;

import android.app.Activity;

import com.hengye.share.module.setting.SettingHelper;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

public class SwipeBackHelper implements SwipeBackActivityBase {

    private Activity mActivity;

    private SwipeBackActivityHelper mHelper;

    public SwipeBackHelper(Activity activity) {
        this.mActivity = activity;
    }

    protected void onCreate() {

        mHelper = new SwipeBackActivityHelper(getActivity());
        mHelper.onActivityCreate();

        mHelper.getSwipeBackLayout().addSwipeListener(new SwipeBackLayout.SwipeListener() {

            @Override
            public void onScrollStateChange(int state, float scrollPercent) {

            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
            }

            @Override
            public void onScrollOverThreshold() {
            }

        });
    }

    protected void onPostCreate() {
        mHelper.onPostCreate();
    }

    public void onResume() {
        setSwipeBackEdgeMode();
        setScreenOrientation();
    }

    protected Activity getActivity() {
        return mActivity;
    }


    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(getActivity());
        getSwipeBackLayout().scrollToFinishActivity();
    }

    public void setSwipeBackEdgeMode() {
        int mode = 1;
        String s = SettingHelper.getSwipeBack();
        try {
            if(s != null){
                mode = Integer.valueOf(SettingHelper.getSwipeBack());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        switch (mode) {
            case 1:
                getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
                break;
            case 2:
                getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_RIGHT);
                break;
            case 3:
            default:
                getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT | SwipeBackLayout.EDGE_RIGHT);
                break;
        }
    }

    private void setScreenOrientation() {
        // 开启屏幕旋转
//        if (AppSettings.isScreenRotate()) {
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//        }
//        else {
//            if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR)
//                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
    }
}
