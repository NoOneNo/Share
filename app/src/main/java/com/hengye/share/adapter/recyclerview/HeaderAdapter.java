package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
    private ViewGroup mHeaderContainer, mFooterContainer;
    private RecyclerView mRecyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    public void setHeader(View header){
        setHeader(header, false);
    }

    public void setHeader(View header, boolean isSelected){
        if(mHeader == header){
            return;
        }

        int firstPosition = getHeaderPosition();

        View headerBefore = mHeader;
        mHeader = header;

        if(header == null){
            //移除header
            notifyItemRemoved(firstPosition);
        }else{
            if(headerBefore == null){
                //新增header
                notifyItemInserted(firstPosition);
            }else{
                //改变header
                notifyItemChanged(firstPosition);
            }
        }

        if(isSelected && mRecyclerView != null){
            mRecyclerView.scrollToPosition(getHeaderPosition());
        }
    }

    public void setFooter(View footer){
        setFooter(footer, false);
    }

    public void setFooter(View footer, boolean isSelected){
        if(mFooter == footer){
            return;
        }

        //footer变换前的最后位置
        int lastPosition = getFooterPosition();

        View footerBefore = mFooter;
        mFooter = footer;

        if(footer == null){
            //移除footer
            notifyItemRemoved(lastPosition);
        }else{
            if(footerBefore == null){
                //新增footer
                notifyItemInserted(lastPosition + 1);
            }else{
                //改变header
                notifyItemChanged(lastPosition);
            }
        }

        if(isSelected && mRecyclerView != null){
            mRecyclerView.scrollToPosition(getFooterPosition());
        }
    }

    public void removeHeader(){
        setHeader(null, false);
    }

    public void removeFooter(){
        setFooter(null, false);
    }

    private ViewGroup getContainer(){
        FrameLayout view = new FrameLayout(getContext());
        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return view;
    }

    private ViewGroup getHeaderContainer(){
        if(mHeaderContainer == null){
            mHeaderContainer = getContainer();
        }
        return mHeaderContainer;
    }

    private ViewGroup getFooterContainer(){
        if(mHeaderContainer == null){
            mFooterContainer = getContainer();
        }
        return mFooterContainer;
    }

    private void onBindContainer(ContainerViewHolder holder, View target){
        if(holder.container.getChildCount() != 0){
            View child = holder.container.getChildAt(0);
            if(child != mHeader){
                holder.container.removeView(child);
                holder.container.addView(target);
            }
        }else{
            holder.container.addView(target);
        }
    }

    public void onBindHeaderView(ContainerViewHolder holder, int position) {
        L.debug("onBindHeaderView invoke()");
        onBindContainer(holder, mHeader);
    }

    public void onBindFooterView(ContainerViewHolder holder, int position) {
        L.debug("onBindFooterView invoke()");
        onBindContainer(holder, mFooter);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if(holder instanceof ContainerViewHolder){
            ContainerViewHolder containerViewHolder = (ContainerViewHolder)holder;
            containerViewHolder.container.removeAllViews();
        }
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
        return new ContainerViewHolder(getContainer());
    }

    public RecyclerView.ViewHolder onCreateFooterViewHolder() {
        return new ContainerViewHolder(getContainer());
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

        public ViewGroup container;

        public ContainerViewHolder(ViewGroup container) {
            super(container);
            this.container = container;
//            setIsRecyclable(false);
        }
    }
}
