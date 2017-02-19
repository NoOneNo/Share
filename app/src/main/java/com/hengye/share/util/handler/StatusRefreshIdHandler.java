package com.hengye.share.util.handler;

import com.hengye.share.module.util.encapsulation.base.DataAdapter;
import com.hengye.share.module.util.encapsulation.base.DataHandler;

import java.util.List;

import static com.hengye.share.module.util.encapsulation.base.DataType.LOAD_DATA_SIZE_EQUAL;
import static com.hengye.share.module.util.encapsulation.base.DataType.LOAD_NO_MORE_DATA;
import static com.hengye.share.module.util.encapsulation.base.DataType.REFRESH_DATA_SIZE_EQUAL;
import static com.hengye.share.module.util.encapsulation.base.DataType.REFRESH_DATA_SIZE_LESS;
import static com.hengye.share.module.util.encapsulation.base.DataType.getType;

/**
 * Created by yuhy on 16/7/28.
 */
public class StatusRefreshIdHandler<T> implements DataHandler<T> {

    DataAdapter<T> mAdapter;

    public StatusRefreshIdHandler(DataAdapter<T> adapter) {
        mAdapter = adapter;
    }

    @Override
    public int handleData(boolean isRefresh, List<T> data) {
        int type = getType(isRefresh, data, mAdapter.getData());
        handleAdapter(type, data, mAdapter);
        return type;
    }

    public static <T> void handleAdapter(int type, List<T> data, DataAdapter<T> adapter) {
        switch (type) {
            case REFRESH_DATA_SIZE_LESS:
            case REFRESH_DATA_SIZE_EQUAL:
                adapter.refresh(data);
                break;
            case LOAD_NO_MORE_DATA:
            case LOAD_DATA_SIZE_EQUAL:
                adapter.addAll(data);
                break;
        }
    }
}
