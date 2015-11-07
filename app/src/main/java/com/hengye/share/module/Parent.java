package com.hengye.share.module;

public class Parent {

    public static final int TYPE_WEIBO= 1;

    public static final int TYPE_QQ = 2;

    public static final int TYPE_WECHAT= 3;

    private Object parent;
    private int parentType;

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public int getParentType() {
        return parentType;
    }

    public void setParentType(int parentType) {
        this.parentType = parentType;
    }
}
