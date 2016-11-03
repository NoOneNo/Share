package com.hengye.share.module.util.encapsulation.view.recyclerview;

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

    /**
     * @param position
     * @return 该位置的item是否支持滑动删除
     */
    boolean isItemSwipeEnabled(int position);

    /**
     * @param position
     * @return 该位置的item是否支持长按拖曳
     */
    boolean isItemDragEnabled(int position);

    /**
     * @param actualPosition 在RecyclerView实际中的position
     * @return 得到item的虚拟位置，此位置的索引不包含header, footer
     */
    int getBasicItemPosition(int actualPosition);
}