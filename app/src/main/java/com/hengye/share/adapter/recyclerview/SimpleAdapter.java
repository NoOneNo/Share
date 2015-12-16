package com.hengye.share.adapter.recyclerview;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.util.ViewUtil;

import java.util.List;

public class SimpleAdapter<T, VH extends SimpleAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {

    private Context mContext;
    private List<T> mData;
    private ViewUtil.OnItemClickListener mOnItemClickListener, mOnChildViewItemClickListener;

    public SimpleAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.setOnItemClickListener(getOnItemClickListener());
        holder.setOnChildViewItemClickListener(getOnChildViewItemClickListener());
        holder.bindData(getContext(), getItem(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder<T> extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public ViewHolder(View itemView, ViewUtil.OnItemClickListener onItemClickListener) {
            super(itemView);
            mOnItemClickListener = onItemClickListener;
            itemView.setOnClickListener(mOnClickForItemListener);
        }

        public void bindData(Context context, T t){

        }

        ViewUtil.OnItemClickListener mOnItemClickListener, mOnChildViewItemClickListener;

        public void registerChildViewItemClick(View v){
            v.setOnClickListener(mOnClickForChildViewListener);
        }


        /**
         * 用于提供给外部调用的ChildViewClick;
         */
        View.OnClickListener mOnClickForChildViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnChildViewItemClickListener != null) {
                    mOnChildViewItemClickListener.onItemClick(v, getAdapterPosition());
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
                    mOnItemClickListener.onItemClick(v, getAdapterPosition());
                }
            }
        };

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
        notifyItemInserted(position);
    }

    public void add(T item) {
        mData.add(item);
        notifyItemInserted(mData.size() - 1);
    }

    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
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
