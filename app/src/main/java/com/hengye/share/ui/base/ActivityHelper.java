package com.hengye.share.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class ActivityHelper {

    private final ArrayList<ActivityLifecycleListener> mActivityLifecycleListener =
            new ArrayList<>();

    private ActivityActionInterceptListener mActivityActionInterceptListener;

    public void clean(){
        mActivityLifecycleListener.clear();
        unregisterActivityActionInterceptListener();
    }

    public void registerActivityActionInterceptListener(ActivityActionInterceptListener listener) {
        mActivityActionInterceptListener = listener;
    }

    public void unregisterActivityActionInterceptListener() {
        mActivityActionInterceptListener = null;
    }

    public void registerActivityLifecycleListener(ActivityLifecycleListener listener) {
        synchronized (mActivityLifecycleListener) {
            mActivityLifecycleListener.add(listener);
        }
    }

    public void unregisterActivityLifecycleListener(ActivityLifecycleListener listener) {
        synchronized (mActivityLifecycleListener) {
            mActivityLifecycleListener.remove(listener);
        }
    }

    /* package */ void dispatchActivityCreated(Activity activity, Bundle savedInstanceState) {
        Object[] callbacks = collectActivityLifecycleListener();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleListener)callbacks[i]).onActivityCreated(activity,
                        savedInstanceState);
            }
        }
    }

    /* package */ void dispatchActivityStarted(Activity activity) {
        Object[] callbacks = collectActivityLifecycleListener();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleListener)callbacks[i]).onActivityStarted(activity);
            }
        }
    }

    /* package */ void dispatchActivityResumed(Activity activity) {
        Object[] callbacks = collectActivityLifecycleListener();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleListener)callbacks[i]).onActivityResumed(activity);
            }
        }
    }

    /* package */ void dispatchActivityPaused(Activity activity) {
        Object[] callbacks = collectActivityLifecycleListener();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleListener)callbacks[i]).onActivityPaused(activity);
            }
        }
    }

    /* package */ void dispatchActivityStopped(Activity activity) {
        Object[] callbacks = collectActivityLifecycleListener();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleListener)callbacks[i]).onActivityStopped(activity);
            }
        }
    }

    /* package */ void dispatchActivitySaveInstanceState(Activity activity, Bundle outState) {
        Object[] callbacks = collectActivityLifecycleListener();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleListener)callbacks[i]).onActivitySaveInstanceState(activity,
                        outState);
            }
        }
    }

    /* package */ void dispatchActivityDestroyed(Activity activity) {
        Object[] callbacks = collectActivityLifecycleListener();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleListener)callbacks[i]).onActivityDestroyed(activity);
            }
        }
    }

    /* package */ void dispatchActivityResulted(Activity activity, int requestCode, int resultCode, Intent data) {
        Object[] callbacks = collectActivityLifecycleListener();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleListener)callbacks[i]).onActivityResulted(activity, requestCode, resultCode, data);
            }
        }
    }

    /* package */ boolean onBackPressed() {
        if(mActivityActionInterceptListener != null){
            return mActivityActionInterceptListener.onBackPressed();
        }
        return false;
    }

    private Object[] collectActivityLifecycleListener() {
        Object[] callbacks = null;
        synchronized (mActivityLifecycleListener) {
            if (mActivityLifecycleListener.size() > 0) {
                callbacks = mActivityLifecycleListener.toArray();
            }
        }
        return callbacks;
    }

    public interface ActivityLifecycleListener {

        void onActivityCreated(Activity activity, Bundle savedInstanceState);
        void onActivityStarted(Activity activity);
        void onActivityResumed(Activity activity);
        void onActivityPaused(Activity activity);
        void onActivityStopped(Activity activity);
        void onActivitySaveInstanceState(Activity activity, Bundle outState);
        void onActivityDestroyed(Activity activity);
        void onActivityResulted(Activity activity, int requestCode, int resultCode, Intent data);
    }

    public interface ActivityActionInterceptListener{
        boolean onBackPressed();//是否拦截, 如果返回true则拦截, 不调用Activity的onBackPressed;
    }

    public static class DefaultActivityLifecycleListener implements ActivityLifecycleListener{
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }

        @Override
        public void onActivityResulted(Activity activity, int requestCode, int resultCode, Intent data) {

        }
    }

    public static class DefaultActivityActionInterceptListener implements ActivityActionInterceptListener{
        @Override
        public boolean onBackPressed() {
            return false;
        }
    }
}
