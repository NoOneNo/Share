package com.hengye.share.util.dagger;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuhy on 2017/1/16.
 */

@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

}