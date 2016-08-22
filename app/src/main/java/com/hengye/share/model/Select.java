package com.hengye.share.model;

import com.hengye.share.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class Select<T>{

    public Select(T target){
        this.selected = false;
        this.target = target;
    }

    private boolean selected;

    private T target;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public T getTarget() {
        return target;
    }

    public void setTarget(T target) {
        this.target = target;
    }

    public static <T> List<Select<T>> get(List<T> targets){
        if(CommonUtil.isEmpty(targets)){
            return null;
        }
        List<Select<T>> list = new ArrayList<>();
        for(T t : targets){
            list.add(new Select<>(t));
        }
        return list;
    }
}
