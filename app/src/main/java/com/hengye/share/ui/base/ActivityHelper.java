package com.hengye.share.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class ActivityHelper {

    private ArrayList<ActivityLifecycleListener> mActivityLifecycleListener =
            new ArrayList<>();

    public void clear(){
        mActivityLifecycleListener.clear();
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

    private Object[] collectActivityLifecycleListener() {
        Object[] callbacks = null;
        synchronized (mActivityLifecycleListener) {
            if (mActivityLifecycleListener.size() > 0) {
                callbacks = mActivityLifecycleListener.toArray();
            }
        }
        return callbacks;
    }

    interface ActivityLifecycleListener {

        void onActivityCreated(Activity activity, Bundle savedInstanceState);
        void onActivityStarted(Activity activity);
        void onActivityResumed(Activity activity);
        void onActivityPaused(Activity activity);
        void onActivityStopped(Activity activity);
        void onActivitySaveInstanceState(Activity activity, Bundle outState);
        void onActivityDestroyed(Activity activity);
        void onActivityResulted(Activity activity, int requestCode, int resultCode, Intent data);
    }
}
