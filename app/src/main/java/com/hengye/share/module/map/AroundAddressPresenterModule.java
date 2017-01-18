package com.hengye.share.module.map;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuhy on 2017/1/13.
 */

@Module
public class AroundAddressPresenterModule {

    private AroundAddressContract.View aroundAddressContractView;

    public AroundAddressPresenterModule(AroundAddressContract.View aroundAddressContractView){
        this.aroundAddressContractView = aroundAddressContractView;
    }

    @Provides
    AroundAddressContract.Presenter providePresenter(){
        return new AroundAddressPresenter(aroundAddressContractView);
    }
}
