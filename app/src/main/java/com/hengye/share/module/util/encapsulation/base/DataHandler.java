package com.hengye.share.module.util.encapsulation.base;

import java.util.List;

/**
 * Created by yuhy on 16/7/28.
 */
public interface DataHandler<T> {
    /**
     * 返回属于哪种类型
     * @param isRefresh
     * @param data
     * @return @link DataType
     */
    int handleData(boolean isRefresh, List<T> data);
}
