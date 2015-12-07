package com.hengye.share.module;

import java.io.Serializable;

public class Parent implements Serializable{

    private static final long serialVersionUID = -3450948658862989295L;

    public static final int TYPE_WEIBO= 1;

    public static final int TYPE_QQ = 2;

    public static final int TYPE_WECHAT= 3;

    public Parent(Object target, int type) {
        this.target = target;
        this.type = type;
    }

    private Object target;
    private int type;

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isWeiBo(){
        return getType() == TYPE_WEIBO;
    }

    public boolean isQQ(){
        return getType() == TYPE_QQ;
    }

    public boolean isWechat(){
        return getType() == TYPE_WECHAT;
    }
}
