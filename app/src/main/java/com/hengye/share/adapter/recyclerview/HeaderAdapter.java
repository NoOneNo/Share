package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hengye.share.util.L;

import java.util.ArrayList;

/**
 * Created by yuhy on 2016/10/10.
 */

public abstract class HeaderAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = Integer.MIN_VALUE;
    private static final int TYPE_FOOTER = Integer.MIN_VALUE + 1;
    private static final int TYPE_OFFSET_MIN = TYPE_FOOTER;

    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private ArrayList<View> mFooterViews = new ArrayList<>();
    private LinearLayout mHeaderContainer, mFooterContainer;

    private LinearLayout getContainer() {
        LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        return container;
    }

    private void ensureHeader() {
        if (mHeaderContainer == null) {
            mHeaderContainer = getContainer();
            if (isAddHeaderView()) {
                notifyItemInserted(0);
            }
        }
    }

    private void ensureFooter() {
        if (mFooterContainer == null) {
            mFooterContainer = getContainer();
            if (isAddFooterView()) {
                notifyItemInserted(getItemCount() - 1);
            }
        }
    }

    public void addHeaderView(View view) {
        addHeaderView(view, -1);
    }

    public void addHeaderView(View view, int index) {
        if (index < 0) {
            index = mHeaderViews.size();
        }

        mHeaderViews.add(view);
        ensureHeader();
        mHeaderContainer.addView(view, index);
    }

    public void addFooterView(View view) {
        addFooterView(view, -1);
    }

    public void addFooterView(View view, int index) {
        if (index < 0) {
            index = mFooterViews.size();
        }

        mFooterViews.add(view);
        ensureFooter();
        mFooterContainer.addView(view, index);
    }

    public void removeHeaderView(View view) {
        if (mHeaderContainer != null && removeViewInfo(view, mHeaderViews)) {
            mHeaderContainer.removeView(view);
        }
    }

    public void removeFooterView(View view) {
        if (mFooterContainer != null && removeViewInfo(view, mFooterViews)) {
            mFooterContainer.removeView(view);
        }
    }

    private boolean removeViewInfo(View v, ArrayList<View> where) {
        int len = where.size();
        for (int i = 0; i < len; ++i) {
            View view = where.get(i);
            if (view == v) {
                where.remove(i);
                return true;
            }
        }
        return false;
    }

    public void onBindHeaderView(ContainerViewHolder holder, int position) {
        L.debug("onBindHeaderView invoke()");
    }

    public void onBindFooterView(ContainerViewHolder holder, int position) {
        L.debug("onBindFooterView invoke()");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return onCreateHeaderViewHolder();
        } else if (viewType == TYPE_FOOTER) {
            return onCreateFooterViewHolder();
        }

        VH vh = onCreateBasicItemViewHolder(parent, viewType);

        onBasicItemViewHolderCreate(vh, viewType);
        return vh;
    }

    public abstract void onBasicItemViewHolderCreate(VH vh, int viewType);

    public RecyclerView.ViewHolder onCreateHeaderViewHolder() {
        return new ContainerViewHolder(mHeaderContainer);
    }

    public RecyclerView.ViewHolder onCreateFooterViewHolder() {
        return new ContainerViewHolder(mFooterContainer);
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

    public boolean isAddHeaderView() {
        return !mHeaderViews.isEmpty();
    }

    public boolean isAddFooterView() {
        return !mFooterViews.isEmpty();
    }

    public int getHeaderViewsCount() {
        return mHeaderViews.size();
    }

    public int getFooterViewsCount() {
        return mFooterViews.size();
    }

    public LinearLayout getHeaderContainer() {
        return mHeaderContainer;
    }

    public LinearLayout getFooterContainer() {
        return mFooterContainer;
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

        public LinearLayout container;

        public ContainerViewHolder(LinearLayout container) {
            super(container);
            this.container = container;
        }
    }
}
