package com.hengye.share.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.util.ViewUtil;

import java.util.List;

public class RecyclerViewBaseAdapter<T, VH extends RecyclerViewBaseAdapter.ItemViewHolder> extends RecyclerViewHeaderAdapter<VH> {

    private Context mContext;
    private List<T> mData;
    private ViewUtil.OnItemClickListener mOnItemClickListener, mOnChildViewItemClickListener;

    public RecyclerViewBaseAdapter(Context context, List<T> data) {
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
        holder.bindData(getContext(), getItem(position));
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

        public ItemViewHolder(View itemView, ViewUtil.OnItemClickListener onItemClickListener) {
            this(itemView, null, false);
        }

        public ItemViewHolder(View itemView, boolean isAddHeaderView) {
            this(itemView, null, isAddHeaderView);
        }

        public ItemViewHolder(View itemView, ViewUtil.OnItemClickListener onItemClickListener, boolean isAddHeaderView) {
            super(itemView);
            mOnItemClickListener = onItemClickListener;
            itemView.setOnClickListener(mOnClickForItemListener);
            mIsAddHeaderView = isAddHeaderView;
        }

        public void bindData(Context context, T t){

        }

        public void registerChildViewItemClick(View v){
            v.setOnClickListener(mOnClickForChildViewListener);
        }

        public void registerItemClick(View v){
            v.setOnClickListener(mOnClickForItemListener);
        }

        boolean mIsAddHeaderView;

        public int getItemVirtualPosition(){
            return RecyclerViewHeaderAdapter.getBasicItemVirtualPosition(getAdapterPosition(), mIsAddHeaderView);
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

        ViewUtil.OnItemClickListener mOnItemClickListener, mOnChildViewItemClickListener;

        public ViewUtil.OnItemClickListener getOnItemClickListener() {
            return mOnItemClickListener;
        }

        public void setOnItemClickListener(ViewUtil.OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public View.OnClickListener getOnClickForItemListener() {
            return mOnClickForItemListener;
        }

        public ViewUtil.OnItemClickListener getOnChildViewItemClickListener() {
            return mOnChildViewItemClickListener;
        }

        public void setOnChildViewItemClickListener(ViewUtil.OnItemClickListener onChildViewItemClickListener) {
            this.mOnChildViewItemClickListener = onChildViewItemClickListener;
        }
    }

    public T getItem(int position){
        return mData.get(position);
    }

    public void add(int position, T item) {
        mData.add(position, item);
        notifyItemInserted(getBasicItemVirtualPosition(position));
    }

    public void add(T item) {
        mData.add(item);
        notifyItemInserted(getBasicItemVirtualPosition(mData.size() - 1));
    }

    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(getBasicItemVirtualPosition(position));
    }

    public void refresh(List<T> data) {
        if (data == null) {
            mData.clear();
        } else {
            mData = data;
        }
        notifyDataSetChanged();
    }

    public void addAll(List<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addAll(int position, List<T> data) {
        mData.addAll(position, data);
        notifyDataSetChanged();
    }

    public ViewUtil.OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(ViewUtil.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public ViewUtil.OnItemClickListener getOnChildViewItemClickListener() {
        return mOnChildViewItemClickListener;
    }

    public void setOnChildViewItemClickListener(ViewUtil.OnItemClickListener onChildViewItemClickListener) {
        this.mOnChildViewItemClickListener = onChildViewItemClickListener;
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        refresh(data);
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }
}
