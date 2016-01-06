package com.hengye.share.model;

import java.io.Serializable;

public class Parent implements Serializable{

    private static final long serialVersionUID = -3450948658862989295L;

    public static final int TYPE_WEIBO= 1;

    public static final int TYPE_QQ = 2;

    public static final int TYPE_WECHAT= 3;

    public static Parent getWBParent(){
        return new Parent(null, TYPE_WEIBO);
    }

    public Parent(){}

    public Parent(String json, int type) {
        this.json = json;
        this.type = type;
    }

    private String json;
    private int type;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
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
