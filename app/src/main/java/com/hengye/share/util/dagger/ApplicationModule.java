package com.hengye.share.util.dagger;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuhy on 2017/1/16.
 */

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplicationContext(){
        return application;
    }

    @Provides
    @Singleton
    RefWatcher provideRefWatcher(){
        return LeakCanary.install(application);
    }

}
