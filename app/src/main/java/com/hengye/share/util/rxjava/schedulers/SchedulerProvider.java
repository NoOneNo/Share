package com.hengye.share.util.rxjava.schedulers;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Provides different types of schedulers.
 */
public class SchedulerProvider{

    @NonNull
    public static Scheduler computation() {
        return Schedulers.computation();
    }

    @NonNull
    public static Scheduler io() {
        return Schedulers.io();
    }

    @NonNull
    public static Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
