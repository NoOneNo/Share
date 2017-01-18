package com.hengye.share.module.map;

import com.hengye.share.model.Address;
import com.hengye.share.module.util.encapsulation.base.Pager;
import com.hengye.share.module.util.encapsulation.mvp.ListDataMvpView;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface AroundAddressContract {

    interface View extends ListDataMvpView<Address> {
    }

    interface Presenter extends MvpPresenter<View>{
        void loadAroundAddress(boolean isRefresh);

        void setLocation(float longitude, float latitude);

        void setKeywords(String keywords);

        void setPager(Pager pager);
    }
}
