package com.hengye.share.module.util.encapsulation.view.recyclerview;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yuhy on 2016/10/12.
 */
public class ItemViewHolder<T> extends RecyclerView.ViewHolder {

    public ItemViewHolder(View itemView) {
        super(itemView);
        itemView.setTag(this);
    }

    public View findViewById(@IdRes int id) {
        return itemView.findViewById(id);
    }

    public void bindData(Context context, T t, int position) {

    }

    public void registerOnClickListener(View v) {
        v.setOnClickListener(getOnClickWrapListener());
    }

    public void registerOnLongClickListener(View v) {
        v.setOnLongClickListener(getOnLongClickWrapListener());
    }

    private View.OnClickListener getOnClickWrapListener(){
        if(mOnClickWrapListener == null){
            mOnClickWrapListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnClickListener != null){
                        mOnClickListener.onClick(v);
                    }
                }
            };
        }
        return mOnClickWrapListener;
    }

    private View.OnLongClickListener getOnLongClickWrapListener(){
        if(mOnLongClickWrapListener == null){
            mOnLongClickWrapListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(mOnLongClickListener != null){
                        return mOnLongClickListener.onLongClick(v);
                    }
                    return false;
                }
            };
        }
        return mOnLongClickWrapListener;
    }

    /**
     * 用于提供给内部调用的OnClick;
     */
    View.OnClickListener mOnClickListener, mOnClickWrapListener;

    /**
     * 用于提供给内部调用的OnLongClick;
     */
    View.OnLongClickListener mOnLongClickListener, mOnLongClickWrapListener;

    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
        itemView.setOnClickListener(mOnClickListener);
    }

    public View.OnLongClickListener getOnLongClickListener() {
        return mOnLongClickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.mOnLongClickListener = onLongClickListener;
        itemView.setOnLongClickListener(mOnLongClickListener);
    }
}
