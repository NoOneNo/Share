package com.hengye.share.adapter.listview;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;

public abstract class ViewHolder<T> {

    private View v;

    public ViewHolder(View v) {
        v.setTag(this);
        this.v = v;
    }

    public View findViewById(@IdRes int id){
        return v.findViewById(id);
    }

    abstract public void bindData(Context context, T t);

}
