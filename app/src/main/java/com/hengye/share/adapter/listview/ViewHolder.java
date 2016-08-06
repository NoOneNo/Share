package com.hengye.share.adapter.listview;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;

import com.hengye.share.ui.view.listener.OnItemClickListener;
import com.hengye.share.ui.view.listener.OnItemLongClickListener;

public abstract class ViewHolder<T> {

    private View v;

    public ViewHolder(View v) {
        v.setTag(this);
        this.v = v;
    }

    public View getParent(){
        return v;
    }

    public View findViewById(@IdRes int id){
        return v.findViewById(id);
    }

    abstract void bindData(Context context, T t, int position);

    public void bindItemPosition(int position){
        v.setTag(View.NO_ID, position);
    }

    public int getItemPosition(){
        return (int)v.getTag(View.NO_ID);
    }

    public void registerChildViewItemClick(View v){
        v.setOnClickListener(mOnClickForChildViewListener);
    }

    public void registerChildViewItemLongClick(View v){
        v.setOnLongClickListener(mOnLongClickForChildViewListener);
    }

    /**
     * 用于提供给外部调用的ChildViewClick;
     */
    View.OnClickListener mOnClickForChildViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getOnChildViewItemClickListener() != null) {
                getOnChildViewItemClickListener().onItemClick(v, getItemPosition());
            }
        }
    };

    /**
     * 用于提供给外部调用的ChildViewClick;
     */
    View.OnLongClickListener mOnLongClickForChildViewListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (getOnChildViewItemLongClickListener() != null) {
                return getOnChildViewItemLongClickListener().onItemLongClick(v, getItemPosition());
            }
            return false;
        }
    };

    OnItemClickListener mOnChildViewItemClickListener;
    OnItemLongClickListener mOnChildViewItemLongClickListener;

    public OnItemLongClickListener getOnChildViewItemLongClickListener() {
        return mOnChildViewItemLongClickListener;
    }

    public void setOnChildViewItemLongClickListener(OnItemLongClickListener onChildViewItemLongClickListener) {
        this.mOnChildViewItemLongClickListener = onChildViewItemLongClickListener;
    }

    public OnItemClickListener getOnChildViewItemClickListener() {
        return mOnChildViewItemClickListener;
    }

    public void setOnChildViewItemClickListener(OnItemClickListener onChildViewItemClickListener) {
        this.mOnChildViewItemClickListener = onChildViewItemClickListener;
    }
}
