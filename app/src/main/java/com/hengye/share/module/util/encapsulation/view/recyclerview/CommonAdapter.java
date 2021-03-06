package com.hengye.share.module.util.encapsulation.view.recyclerview;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.module.util.encapsulation.base.DataAdapter;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemLongClickListener;
import com.hengye.share.module.util.encapsulation.base.DiffUtil;
import com.hengye.share.util.L;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CommonAdapter<T> extends HeaderAdapter<ItemViewHolder>
        implements DataAdapter<T>, ItemTouchHelperAdapter {

    private Context mContext;
    private List<T> mData;
    private int mSelectPosition = -1;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private boolean mIsCheckDiffMode = false;

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
        if(data == null){
            data = new ArrayList<>();
        }
        this.mData = data;
    }

    public View inflate(@LayoutRes int layoutResId) {
        return inflate(layoutResId, null);
    }

    public View inflate(@LayoutRes int layoutResId, ViewGroup parent) {
        return LayoutInflater.from(getContext()).inflate(layoutResId, parent, false);
    }

    @Override
    public ItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBasicItemViewHolderCreate(final ItemViewHolder holder, int viewType) {

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnItemClickListener() != null) {
                    getOnItemClickListener().onItemClick(v, getBasicItemPosition(holder));
                }
            }
        });

        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (getOnItemLongClickListener() != null) {
                    return getOnItemLongClickListener().onItemLongClick(v, getBasicItemPosition(holder));
                }
                return false;
            }
        });

    }

    @SuppressWarnings("unchecked")
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
//        position = getActualItemPosition(position);
        if (!isIndexOutOfBounds(position)) {
            return mData.get(position);
        }
        return null;
    }

    public int getItemPosition(T item) {
        return mData.indexOf(item);
    }

    public boolean contains(T item){
        if(item == null){
            return false;
        }
        return mData.contains(item);
    }

    public int getFirstPosition(){
        return getActualItemPosition(0);
    }

    public int getLastPosition(){
        return mData.size() - 1;
    }

    /**
     * @param basicItemPosition 在{@link #mData}中的位置
     * @return 返回在RecyclerView实际中的position
     */
    public int getActualItemPosition(int basicItemPosition) {
        return basicItemPosition + (isAddHeaderView() ? 1 : 0);
    }

    /**
     * @param basicItemPosition 在{@link #mData}中的位置
     * @return 返回在RecyclerView里的RecyclerView.ViewHolder
     */
    public RecyclerView.ViewHolder findViewHolderForLayoutPosition(int basicItemPosition) {
        if(getRecyclerView() != null) {
            return getRecyclerView().findViewHolderForLayoutPosition(getActualItemPosition(basicItemPosition));
        }
        return null;
    }

    public void notifyItemChanged(T item){
        int index = mData.indexOf(item);
        if (index != -1) {
            notifyItemChanged(index);
        }
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

    public void updateItem(int position) {
        notifyItemChanged(getActualItemPosition(position));
    }

    public void updateItem(int position, T item) {
        mData.set(position, item);
        notifyItemChanged(getActualItemPosition(position));
    }

    public void updateItem(T item) {
        int index = mData.indexOf(item);
        if (index != -1) {
            updateItem(index, item);
        }
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
        notifyItemRangeInserted(getActualItemPosition(sizeBeforeAdd), data.size() - 1);
    }

    public void addAll(int position, List<T> data) {
        if (data == null) {
            return;
        }
        mData.addAll(position, data);
//        notifyItemRangeInserted(position, data.size());
        notifyDataSetChanged();
    }

    public void refresh(List<T> data) {
        if (!mIsCheckDiffMode) {
            if (data == null) {
                mData.clear();
            } else {
                mData = data;
            }
            notifyDataSetChanged();
        } else {
            if (data == null && !mData.isEmpty()) {
                mData.clear();
                notifyDataSetChanged();
            } else if (data != null && mData.isEmpty()) {
                mData = data;
                notifyDataSetChanged();
            } else if(data != null && !mData.isEmpty()){
                int position = DiffUtil.checkEquals(data, mData);
                if(position == -1){
                    mData.clear();
                    mData = data;
                    notifyDataSetChanged();
                    L.debug("not find equals");
                }else if(position > 0){
                    mData.addAll(0, data.subList(0, position - 1));
                    notifyItemRangeInserted(getActualItemPosition(0), position);
//                    scrollToPosition(getActualItemPosition(0));
                    L.debug("notifyItemRangeInserted from : %s to : %s", 0, position);
                }else{
                    L.debug("find equals at 0 not change");
                }
            }
        }
    }

    @Override
    public boolean isItemSwipeEnabled(int position) {
        return !isIndexOutOfBounds(position);
    }

    @Override
    public boolean isItemDragEnabled(int position) {
        return !isIndexOutOfBounds(position);
    }

    public boolean isIndexOutOfBounds(int position) {
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

    public boolean isSelectPosition(int position) {
        return mSelectPosition == position;
    }

    public boolean isCheckDiffMode() {
        return mIsCheckDiffMode;
    }

    public void setCheckDiffMode(boolean checkDiffMode) {
        mIsCheckDiffMode = checkDiffMode;
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
