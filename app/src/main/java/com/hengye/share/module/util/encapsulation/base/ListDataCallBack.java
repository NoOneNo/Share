package com.hengye.share.module.util.encapsulation.base;

import java.util.List;

/**
 * Created by yuhy on 2016/10/20.
 */

public interface ListDataCallBack<T> extends ListTaskCallBack {

    void onLoadListData(boolean isRefresh, List<T> data);
}
