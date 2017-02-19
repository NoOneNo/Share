package com.hengye.share.model;

import android.support.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hengye.share.model.sina.WBShortUrl;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.GsonUtil;
import com.hengye.share.util.thirdparty.WBUtil;

import java.io.Serializable;

/**
 * Created by yuhy on 2016/11/10.
 */

public class StatusUrl implements Serializable {

    public static final int COMMON = 0;
    public static final int PHOTO = 1;
    public static final int MUSIC = 2;
    public static final int VIDEO = 3;
    public static final int ARTICLE = 4;
    public static final int LOCATION= 5;

    public static int getUrlType(WBShortUrl wbShortUrl) {
        if (wbShortUrl != null && !CommonUtil.isEmpty(wbShortUrl.getAnnotations())) {
            WBShortUrl.AnnotationsBean annotationsBean = wbShortUrl.getAnnotations().get(0);
            String type = annotationsBean.getObject_type();
            if(type != null) {
                switch (type) {
                    case WBShortUrl.TYPE_VIDEO:
                        return VIDEO;
                    case WBShortUrl.TYPE_COLLECTION:
                        return PHOTO;
                    case WBShortUrl.TYPE_PLACE:
                        return LOCATION;
                }
            }
        }
        return COMMON;
    }

    public static void setupStatusUrl(StatusUrl statusUrl, WBShortUrl wbShortUrl){
        if (wbShortUrl != null && !CommonUtil.isEmpty(wbShortUrl.getAnnotations())) {
            WBShortUrl.AnnotationsBean annotationsBean = wbShortUrl.getAnnotations().get(0);
            try {
                JsonObject jsonObject = GsonUtil.toJsonObject(annotationsBean.getObject());
//                String json = GsonUtil.toJson(annotationsBean.getObject());
//                JSONObject jsonObject = new JSONObject(json);
                String displayName = jsonObject.get("display_name").getAsString();
                statusUrl.setDisplayName(displayName);

                if(statusUrl.getType() == PHOTO){
                    JsonArray jsonArray = jsonObject.get("pic_ids").getAsJsonArray();
                    String picId = jsonArray.get(0).getAsString();
                    if(picId != null){
                        statusUrl.setAnnotation(WBUtil.getWBImgUrlById(picId));
                    }
                }else if(statusUrl.getType() == LOCATION){
                    //经纬度:23.33441 113.38426，空格隔开
                    String position = jsonObject.get("position").getAsString();
                    statusUrl.setAnnotation(position);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public static StatusUrl create(String statusId, @NonNull WBShortUrl wbShortUrl) {
        StatusUrl statusUrl = new StatusUrl(statusId, wbShortUrl.getUrl_long(), getUrlType(wbShortUrl));
        setupStatusUrl(statusUrl, wbShortUrl);
        return statusUrl;
    }

    private static final long serialVersionUID = -5816652159213898692L;

    public StatusUrl(String statusId, String url, int type) {
        this.statusId = statusId;
        this.url = url;
        this.type = type;
    }

    String statusId;//微博Id
    String url;//长链地址
//    String picUrl;//图片地址，可能跟长链地址不一样
    String displayName;//url类型显示的名字
    int type;//url类型

    /**
     *  url的详情数据
     *  如果是{@link #PHOTO} 这是一个图片的id地址，需要拼接
     *
     *
     *
     */
    Object annotation;

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Object annotation) {
        this.annotation = annotation;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TopicUrl{" +
                "statusId='" + statusId + '\'' +
                ", url='" + url + '\'' +
                ", displayName='" + displayName + '\'' +
                ", type=" + type +
                ", annotation=" + annotation +
                '}';
    }
}
