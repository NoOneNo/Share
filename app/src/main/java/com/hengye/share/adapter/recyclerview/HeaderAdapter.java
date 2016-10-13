package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hengye.share.util.L;

/**
 * Created by yuhy on 2016/10/10.
 */

public abstract class HeaderAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = Integer.MIN_VALUE;
    private static final int TYPE_FOOTER = Integer.MIN_VALUE + 1;
    private static final int TYPE_OFFSET_MIN = TYPE_FOOTER;

    private View mHeader, mFooter;
    private RecyclerView mRecyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    public void setHeader(View header){
        if(mHeader == header){
            return;
        }

        int firstPosition = getHeaderPosition();
        View headerBefore = mHeader;
        mHeader = header;

        if(headerBefore == null){
            //footer属于新增
            notifyItemInserted(firstPosition);
        }else{
            if(header != null){
                //footer变了
                notifyItemChanged(firstPosition);
            }else {
                //移除footer，移除后item总数少1，
                notifyItemRemoved(firstPosition);
            }

            if(mRecyclerView != null){
                mRecyclerView.removeView(headerBefore);
            }
        }
    }

    public void setFooter(View footer){
        if(mFooter == footer){
            return;
        }

        int lastPosition = getFooterPosition();
        View footerBefore = mFooter;
        mFooter = footer;

        if(footerBefore == null){
            //footer属于新增
            notifyItemInserted(lastPosition + 1);
        }else{
            if(footer != null){
                //footer变了
                notifyItemChanged(lastPosition);
            }else {
                //移除footer，移除后item总数少1，
                notifyItemRemoved(lastPosition);
            }

            if(mRecyclerView != null){
                mRecyclerView.removeView(footerBefore);
            }
        }


//        if(isAddFooterView()){
//            notifyItemChanged(getItemCount() - 1);
//            if(mRecyclerView != null && beforView != null){
//                mRecyclerView.removeView(beforView);
//            }
//        }else{
//            notifyItemRemoved(getItemCount() - 1);
//        }
    }

    public void removeHeader(){
        setHeader(null);
    }

    public void removeFooter(){
        setFooter(null);
    }

    public void onBindHeaderView(ContainerViewHolder holder, int position) {
        L.debug("onBindHeaderView invoke()");
    }

    public void onBindFooterView(ContainerViewHolder holder, int position) {
        L.debug("onBindFooterView invoke()");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isAddHeaderView() && viewType == TYPE_HEADER) {
            return onCreateHeaderViewHolder();
        } else if (isAddFooterView() && viewType == TYPE_FOOTER) {
            return onCreateFooterViewHolder();
        }

        VH vh = onCreateBasicItemViewHolder(parent, viewType);

        onBasicItemViewHolderCreate(vh, viewType);
        return vh;
    }

    public abstract void onBasicItemViewHolderCreate(VH vh, int viewType);

    public RecyclerView.ViewHolder onCreateHeaderViewHolder() {
        return new ContainerViewHolder(mHeader);
    }

    public RecyclerView.ViewHolder onCreateFooterViewHolder() {
        return new ContainerViewHolder(mFooter);
    }

    public abstract VH onCreateBasicItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindBasicItemView(VH holder, int position);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0 && holder.getItemViewType() == TYPE_HEADER) {
            onBindHeaderView((ContainerViewHolder) holder, position);
        } else if (position == getItemCount() - 1 && holder.getItemViewType() == TYPE_FOOTER) {
            onBindFooterView((ContainerViewHolder) holder, position);
        } else {
            onBindBasicItemViewByType(holder, position);
        }
    }

    protected void onBindBasicItemViewByType(RecyclerView.ViewHolder holder, int position) {
        onBindBasicItemView((VH) holder, getBasicItemPosition(position));
    }

    /**
     * @param actualPosition 在RecyclerView实际中的position
     * @return 得到item的虚拟位置，此位置的索引不包含header, footer
     */
    public int getBasicItemPosition(int actualPosition) {
        return actualPosition - (isAddHeaderView() ? 1 : 0);
    }

    public int getBasicItemPosition(RecyclerView.ViewHolder viewHolder){
        return getBasicItemPosition(viewHolder.getAdapterPosition());
    }

    /**
     * 得到item的虚拟位置，此位置的索引不包含header, footer；
     *
     * @param position        在RecyclerView实际中的position
     * @param isAddHeaderView 是否使用header
     * @return
     */
    public static int getBasicItemVirtualPosition(int position, boolean isAddHeaderView) {
        return position - (isAddHeaderView ? 1 : 0);
    }

    /**
     * 如果要获取的数量不包括头和尾的，使用{@link HeaderAdapter#getBasicItemCount()}
     *
     * @return
     */
    @Deprecated
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

        int type = getBasicItemType(getBasicItemPosition(position));
        if (type <= TYPE_OFFSET_MIN) {
            throw new IllegalStateException("HeaderRecyclerViewAdapter offsets your BasicItemType by " + TYPE_OFFSET_MIN + ".");
        }

        return type;
    }

    public int getHeaderPosition(){
        return 0;
    }

    public int getFooterPosition(){
        return getItemCount() - 1;
    }

    public boolean isAddHeaderView() {
//        return mHeaderContainer != null;
        return mHeader != null;
    }

    public boolean isAddFooterView() {
        return mFooter != null;
    }

    /**
     * make sure you don't use [Integer.MIN_VALUE, Integer.MIN_VALUE + 1] as BasicItemViewType
     *
     * @param position
     * @return
     */
    public abstract int getBasicItemType(int position);

    public abstract int getBasicItemCount();

    public abstract Context getContext();

    public static class ContainerViewHolder extends RecyclerView.ViewHolder {

        public View container;

        public ContainerViewHolder(View container) {
            super(container);
            this.container = container;
//            setIsRecyclable(false);
        }
    }
}
