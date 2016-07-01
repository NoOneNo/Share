package com.hengye.share.adapter.listview;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;

import com.hengye.share.util.ViewUtil;

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

    ViewUtil.OnItemClickListener mOnChildViewItemClickListener;
    ViewUtil.OnItemLongClickListener mOnChildViewItemLongClickListener;

    public ViewUtil.OnItemLongClickListener getOnChildViewItemLongClickListener() {
        return mOnChildViewItemLongClickListener;
    }

    public void setOnChildViewItemLongClickListener(ViewUtil.OnItemLongClickListener onChildViewItemLongClickListener) {
        this.mOnChildViewItemLongClickListener = onChildViewItemLongClickListener;
    }

    public ViewUtil.OnItemClickListener getOnChildViewItemClickListener() {
        return mOnChildViewItemClickListener;
    }

    public void setOnChildViewItemClickListener(ViewUtil.OnItemClickListener onChildViewItemClickListener) {
        this.mOnChildViewItemClickListener = onChildViewItemClickListener;
    }
}
