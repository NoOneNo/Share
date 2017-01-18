package com.hengye.share.util.dagger;

import android.content.Context;
import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuhy on 2017/1/16.
 */

@Module
public class FragmentModule {

    private final Context mContext;

    FragmentModule(Fragment fragment) {
        mContext = fragment.getContext();
    }

    @Provides
    Context provideContext() {
        return mContext;
    }
}
