package com.hengye.share.adapter.recyclerview;


import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.handler.data.base.DataAdapter;
import com.hengye.share.ui.widget.listener.OnItemClickListener;
import com.hengye.share.ui.widget.listener.OnItemLongClickListener;

import java.util.List;

public class CommonAdapter<T, VH extends CommonAdapter.ItemViewHolder> extends HeaderAdapter<VH> implements DataAdapter<T> {

    private Context mContext;
    private List<T> mData;
    private OnItemClickListener mOnItemClickListener, mOnChildViewItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener, mOnChildViewItemLongClickListener;

    public CommonAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public VH onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindBasicItemView(VH holder, int position) {
        holder.setOnItemClickListener(getOnItemClickListener());
        holder.setOnChildViewItemClickListener(getOnChildViewItemClickListener());
        holder.setOnItemLongClickListener(getOnItemLongClickListener());
        holder.setOnChildViewItemLongClickListener(getOnChildViewItemLongClickListener());
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

    public static class ItemViewHolder<T> extends RecyclerView.ViewHolder{

        public ItemViewHolder(View itemView) {
            this(itemView, null);
        }

        public ItemViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            this(itemView, onItemClickListener, false);
        }

        public ItemViewHolder(View itemView, boolean isAddHeaderView) {
            this(itemView, null, isAddHeaderView);
        }

        public ItemViewHolder(View itemView, OnItemClickListener onItemClickListener, boolean isAddHeaderView) {
            super(itemView);
            mOnItemClickListener = onItemClickListener;
            itemView.setOnClickListener(mOnClickForItemListener);
            itemView.setOnLongClickListener(mOnLongClickForItemListener);
            mIsAddHeaderView = isAddHeaderView;
        }

        public View findViewById(@IdRes int id){
            return itemView.findViewById(id);
        }

        public void bindData(Context context, T t, int position){

        }

        public void registerChildViewItemClick(View v){
            v.setOnClickListener(mOnClickForChildViewListener);
        }

        public void registerChildViewItemLongClick(View v){
            v.setOnLongClickListener(mOnLongClickForChildViewListener);
        }

        boolean mIsAddHeaderView;

        public int getItemVirtualPosition(){
            return HeaderAdapter.getBasicItemVirtualPosition(getAdapterPosition(), mIsAddHeaderView);
        }

        /**
         * 用于提供给外部调用的ChildViewClick;
         */
        View.OnClickListener mOnClickForChildViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnChildViewItemClickListener != null) {
                    mOnChildViewItemClickListener.onItemClick(v, getItemVirtualPosition());
                }
            }
        };
        /**
         * 用于提供给内部调用的ItemClick;
         */
        View.OnClickListener mOnClickForItemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, getItemVirtualPosition());
                }
            }
        };

        /**
         * 用于提供给外部调用的ChildViewClick;
         */
        View.OnLongClickListener mOnLongClickForChildViewListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnChildViewItemLongClickListener != null) {
                    return mOnChildViewItemLongClickListener.onItemLongClick(v, getItemVirtualPosition());
                }
                return false;
            }
        };
        /**
         * 用于提供给内部调用的ItemClick;
         */
        View.OnLongClickListener mOnLongClickForItemListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    return mOnItemLongClickListener.onItemLongClick(v, getItemVirtualPosition());
                }
                return false;
            }
        };

        OnItemClickListener mOnItemClickListener, mOnChildViewItemClickListener;
        OnItemLongClickListener mOnItemLongClickListener, mOnChildViewItemLongClickListener;

        public OnItemClickListener getOnItemClickListener() {
            return mOnItemClickListener;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public OnItemClickListener getOnChildViewItemClickListener() {
            return mOnChildViewItemClickListener;
        }

        public void setOnChildViewItemClickListener(OnItemClickListener onChildViewItemClickListener) {
            this.mOnChildViewItemClickListener = onChildViewItemClickListener;
        }

        public OnItemLongClickListener getOnItemLongClickListener() {
            return mOnItemLongClickListener;
        }

        public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
            this.mOnItemLongClickListener = onItemLongClickListener;
        }

        public OnItemLongClickListener getOnChildViewItemLongClickListener() {
            return mOnChildViewItemLongClickListener;
        }

        public void setOnChildViewItemLongClickListener(OnItemLongClickListener onChildViewItemLongClickListener) {
            this.mOnChildViewItemLongClickListener = onChildViewItemLongClickListener;
        }

        //        public View.OnClickListener getOnClickForItemListener() {
//            return mOnClickForItemListener;
//        }
    }

    public boolean isEmpty() {
        return mData.isEmpty();
    }

    public T getItem(int position){
        if(0 <= position && position < mData.size()){
            return mData.get(position);
        }
        return null;
    }

    public int getItemPosition(T item){
        return mData.indexOf(item);
    }

    public T getLastItem(){
        if(mData.isEmpty()){
            return null;
        }
        return mData.get(mData.size() - 1);
    }

    public int getLastItemPosition(){
        return mData.size() - 1;
    }

    public void addItem(int position, T item) {
        mData.add(position, item);
        notifyItemInserted(getBasicItemVirtualPosition(position));
    }

    public void addItem(T item) {
        mData.add(item);
        notifyItemInserted(getBasicItemVirtualPosition(mData.size() - 1));
    }

    public void updateItem(int position, T item){
        mData.set(position, item);
        notifyItemChanged(getBasicItemVirtualPosition(position));
    }

    public T removeItem(int position) {
        T t = mData.remove(position);
        notifyItemRemoved(getBasicItemVirtualPosition(position));
        return t;
    }

    public T removeItem(T item) {
        int index = mData.indexOf(item);
        if(index != -1){
            return removeItem(index);
        }
        return null;
    }

    public void addAll(List<T> data) {
        if(data == null){
            return;
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addAll(int position, List<T> data) {
        if(data == null){
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

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public OnItemClickListener getOnChildViewItemClickListener() {
        return mOnChildViewItemClickListener;
    }

    public void setOnChildViewItemClickListener(OnItemClickListener onChildViewItemClickListener) {
        this.mOnChildViewItemClickListener = onChildViewItemClickListener;
    }

    public OnItemLongClickListener getOnChildViewItemLongClickListener() {
        return mOnChildViewItemLongClickListener;
    }

    public void setOnChildViewItemLongClickListener(OnItemLongClickListener onChildViewItemLongClickListener) {
        this.mOnChildViewItemLongClickListener = onChildViewItemLongClickListener;
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

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }
}
