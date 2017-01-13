package com.hengye.share.module.map;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuhy on 2017/1/13.
 */

@Module
public class AroundAddressPresenterModule {

    private AroundAddressMvpView aroundAddressMvpView;

    public AroundAddressPresenterModule(AroundAddressMvpView aroundAddressMvpView){
        this.aroundAddressMvpView = aroundAddressMvpView;
    }

    @Provides
    AroundAddressMvpView provideAroundAddressMvpView(){
        return aroundAddressMvpView;
    }

    @Provides
    AroundAddressPresenter provideAroundAddressPresenter(){
        return new AroundAddressPresenter(aroundAddressMvpView);
    }
}
