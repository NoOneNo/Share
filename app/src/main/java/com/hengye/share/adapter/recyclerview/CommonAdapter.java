package com.hengye.share.adapter.recyclerview;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.handler.data.base.DataAdapter;
import com.hengye.share.ui.widget.listener.OnItemClickListener;
import com.hengye.share.ui.widget.listener.OnItemLongClickListener;
import com.hengye.share.ui.widget.recyclerview.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CommonAdapter<T> extends HeaderAdapter<ItemViewHolder> implements DataAdapter<T>, ItemTouchHelperAdapter {

    private Context mContext;
    private List<T> mData;
    private int mSelectPosition = -1;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public CommonAdapter() {
    }

    public CommonAdapter(Context context) {
        this(context, new ArrayList<T>());
    }

    public CommonAdapter(Context context, List<T> data) {
        init(context, data);
    }

    public void init(Context context, List<T> data) {
        this.mContext = context;
        this.mData = data;
    }

    public View inflate(@LayoutRes int layoutResId){
        return inflate(layoutResId, null);
    }

    public View inflate(@LayoutRes int layoutResId, ViewGroup parent){
        return LayoutInflater.from(getContext()).inflate(layoutResId, parent, false);
    }

    @Override
    public void onBasicItemViewHolderCreate(final ItemViewHolder holder, int viewType) {

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getOnItemClickListener() != null){
                    getOnItemClickListener().onItemClick(v, getBasicItemPosition(holder));
                }
            }
        });

        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(getOnItemLongClickListener() != null){
                    return getOnItemLongClickListener().onItemLongClick(v, getBasicItemPosition(holder));
                }
                return false;
            }
        });

    }

    @Override
    public void onBindBasicItemView(ItemViewHolder holder, int position) {
        holder.bindData(getContext(), getItem(position), position);
    }

    @Override
    public int getBasicItemCount() {
        return mData.size();
    }

    @Override
    public int getBasicItemType(int position) {
        return 0;
    }

    public boolean isEmpty() {
        return mData.isEmpty();
    }

    public T getItem(int position) {
        if (!isIndexOutOfBounds(position)) {
            return mData.get(position);
        }
        return null;
    }

    public int getItemPosition(T item) {
        return mData.indexOf(item);
    }

    @Override
    public int getBasicItemPosition(int actualPosition) {
        return super.getBasicItemPosition(actualPosition);
    }

    /**
     *
     * @param basicItemPosition 在{@link #mData}中的位置
     * @return 返回在RecyclerView实际中的position
     */
    public int getActualItemPosition(int basicItemPosition){
        return basicItemPosition + (isAddHeaderView() ? 1 : 0);
    }

    public T getLastItem() {
        if (mData.isEmpty()) {
            return null;
        }
        return mData.get(mData.size() - 1);
    }

    public int getLastItemPosition() {
        return mData.size() - 1;
    }

    public void addItem(int position, T item) {
        mData.add(position, item);
        notifyItemInserted(getActualItemPosition(position));
    }

    public void addItem(T item) {
        mData.add(item);
        notifyItemInserted(getActualItemPosition(mData.size() - 1));
    }

    public void updateItem(int position, T item) {
        mData.set(position, item);
        notifyItemChanged(getActualItemPosition(position));
    }

    public T removeItem(int position) {
        T t = mData.remove(position);
        notifyItemRemoved(getActualItemPosition(position));
        return t;
    }

    public T removeItem(T item) {
        int index = mData.indexOf(item);
        if (index != -1) {
            return removeItem(index);
        }
        return null;
    }

    /**
     * @param from
     * @param to
     * @return 如果可以移动, 返回true;
     */
    public boolean moveItem(int from, int to) {
        final int size = mData.size();
        if (to < 0 || from >= size || to < 0 || to >= size || from == to) {
            return false;
        }
        Collections.swap(mData, from, to);
        notifyItemMoved(getActualItemPosition(from), getActualItemPosition(to));
        return true;
    }

    public void addAll(List<T> data) {
        if (data == null) {
            return;
        }
        int sizeBeforeAdd = mData.size();
        mData.addAll(data);
//        notifyDataSetChanged();
        notifyItemChanged(getActualItemPosition(sizeBeforeAdd));
//        notifyItemRangeInserted(getActualItemPosition(sizeBeforeAdd), data.size() - 1);
    }

    public void addAll(int position, List<T> data) {
        if (data == null) {
            return;
        }
        mData.addAll(position, data);
        notifyDataSetChanged();
    }

    public void refresh(List<T> data) {
        if (data == null) {
            mData.clear();
        } else {
            mData = data;
        }
        notifyDataSetChanged();
    }

    @Override
    public boolean isItemSwipeEnabled(int position){
        return !isIndexOutOfBounds(position);
    }

    @Override
    public boolean isItemDragEnabled(int position){
        return !isIndexOutOfBounds(position);
    }

    public boolean isIndexOutOfBounds(int position){
        return 0 > position || position >= mData.size();
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public int getSelectPosition() {
        return mSelectPosition;
    }

    public boolean isSelectPosition(int position){
        return mSelectPosition == position;
    }

    /**
     * @param selectPosition
     * @return if selectPosition equals to mSelectPosition, return false;
     */
    public boolean setSelectPosition(int selectPosition) {
        if (this.mSelectPosition == selectPosition) {
            return false;
        }
        int lastSelectPosition = mSelectPosition;
        this.mSelectPosition = selectPosition;
        notifyItemChanged(lastSelectPosition);
        notifyItemChanged(selectPosition);
        return true;
    }
}
