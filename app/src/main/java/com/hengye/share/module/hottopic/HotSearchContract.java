package com.hengye.share.module.hottopic;

import com.hengye.share.model.HotSearch;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.module.util.encapsulation.mvp.TaskMvpView;

import java.util.List;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface HotSearchContract {

    interface View extends TaskMvpView {
        void onLoadHotSearchSuccess(List<HotSearch> hotSearches);
    }

    interface Presenter extends MvpPresenter<View> {

        void loadHotSearch();
    }
}
