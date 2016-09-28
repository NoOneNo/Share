package com.hengye.share.ui.widget.recyclerview;

/**
 * Created by yuhy on 2016/9/28.
 */

public interface ItemTouchHelperAdapter {

    /**
     * @param fromPosition
     * @param toPosition
     * @return 如果返回true, 则可以移动
     */
    boolean moveItem(int fromPosition, int toPosition);

    /**
     * @param position
     * @return 如果返回的object不等于空,则可以移除
     */
    Object removeItem(int position);
}