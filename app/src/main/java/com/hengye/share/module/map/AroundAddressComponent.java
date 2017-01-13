package com.hengye.share.module.map;

import dagger.Component;

/**
 * Created by yuhy on 2017/1/13.
 */
@Component(modules = AroundAddressPresenterModule.class)
public interface AroundAddressComponent {

    void inject(AroundAddressFragment aroundAddressFragment);
}
