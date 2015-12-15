package com.hengye.share.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by sebnapi on 08.11.14.
 * <p/>
 * If you extend this Adapter you are able to add a Header, a Footer or both
 * by a similar ViewHolder pattern as in RecyclerView.
 * <p/>
 * If you want to omit changes to your class hierarchy you can try the Plug-and-Play
 * approach HeaderRecyclerViewAdapterV1.
 * <p/>
 * Don't override (Be careful while overriding)
 * - onCreateViewHolder
 * - onBindViewHolder
 * - getItemCount
 * - getItemViewType
 * <p/>
 * You need to override the abstract methods introduced by this class. This class
 * is not using generics as RecyclerView.Adapter make yourself sure to cast right.
 * <p/>
 * TOTALLY UNTESTED - USE WITH CARE - HAVE FUN :)
 */
public abstract class RecyclerViewHeaderAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = Integer.MIN_VALUE;
    private static final int TYPE_FOOTER = Integer.MIN_VALUE + 1;
    private static final int TYPE_ADAPTEE_OFFSET = 2;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return onCreateHeaderViewHolder(parent, viewType);
        } else if (viewType == TYPE_FOOTER) {
            return onCreateFooterViewHolder(parent, viewType);
        }
        return onCreateBasicItemViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0 && holder.getItemViewType() == TYPE_HEADER) {
            onBindHeaderView(holder, position);
        } else if (position == getItemCount() - 1&& holder.getItemViewType() == TYPE_FOOTER) {
            onBindFooterView(holder, position);
        } else {
            onBindBasicItemView((VH)holder, getBasicItemVirtualPosition(position));
        }
    }


    /**
     * 得到item的虚拟位置，此位置的索引不包含header, footer；
     * @param position 在RecyclerView实际中的position
     * @return
     */
    public int getBasicItemVirtualPosition(int position){
        return getBasicItemVirtualPosition(position, isAddHeaderView());
    }

    /**
     * 得到item的虚拟位置，此位置的索引不包含header, footer；
     * @param position 在RecyclerView实际中的position
     * @param isAddHeaderView 是否使用header
     * @return
     */
    public static int getBasicItemVirtualPosition(int position, boolean isAddHeaderView){
        return position - (isAddHeaderView ? 1 : 0);
    }

    @Override
    public int getItemCount() {
        int itemCount = getBasicItemCount();
        if (isAddHeaderView()) {
            itemCount += 1;
        }
        if (isAddFooterView()) {
            itemCount += 1;
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && isAddHeaderView()) {
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1 && isAddFooterView()) {
            return TYPE_FOOTER;
        }
        if (getBasicItemType(position) >= Integer.MAX_VALUE - TYPE_ADAPTEE_OFFSET) {
            throw new IllegalStateException("HeaderRecyclerViewAdapter offsets your BasicItemType by " + TYPE_ADAPTEE_OFFSET + ".");
        }
        return getBasicItemType(position);
    }

    public boolean isAddHeaderView(){
        return false;
    }

    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType){
        return null;
    }

    public void onBindHeaderView(RecyclerView.ViewHolder holder, int position){}

    public boolean isAddFooterView(){
        return false;
    }

    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType){
        return null;
    }

    public void onBindFooterView(RecyclerView.ViewHolder holder, int position){}

    public abstract VH onCreateBasicItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindBasicItemView(VH holder, int position);

    public abstract int getBasicItemCount();

    /**
     * make sure you don't use [Integer.MAX_VALUE-1, Integer.MAX_VALUE] as BasicItemViewType
     *
     * @param position
     * @return
     */
    public abstract int getBasicItemType(int position);

}