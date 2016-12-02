package com.hengye.share.model;

import android.support.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hengye.share.model.sina.WBShortUrl;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.GsonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.thirdparty.WBUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by yuhy on 2016/11/10.
 */

public class TopicUrl implements Serializable {

    public static final int COMMON = 0;
    public static final int PHOTO = 1;
    public static final int MUSIC = 2;
    public static final int VIDEO = 3;
    public static final int ARTICLE = 4;

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
                }
            }
        }
        return COMMON;
    }

    public static void setupTopicUrl(TopicUrl topicUrl, WBShortUrl wbShortUrl){
        if (wbShortUrl != null && !CommonUtil.isEmpty(wbShortUrl.getAnnotations())) {
            WBShortUrl.AnnotationsBean annotationsBean = wbShortUrl.getAnnotations().get(0);
            try {
                JsonObject jsonObject = GsonUtil.toJsonObject(annotationsBean.getObject());
//                String json = GsonUtil.toJson(annotationsBean.getObject());
//                JSONObject jsonObject = new JSONObject(json);
                String displayName = jsonObject.get("display_name").getAsString();
                topicUrl.setDisplayName(displayName);


                if(topicUrl.getType() == PHOTO){
                    JsonArray jsonArray = jsonObject.get("pic_ids").getAsJsonArray();
                    String picId = jsonArray.get(0).getAsString();
                    if(picId != null){
                        topicUrl.setPicUrl(WBUtil.getWBImgUrlById(picId));
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public static TopicUrl create(String topicId, @NonNull WBShortUrl wbShortUrl) {
        TopicUrl topicUrl = new TopicUrl(topicId, wbShortUrl.getUrl_long(), getUrlType(wbShortUrl));
        setupTopicUrl(topicUrl, wbShortUrl);
        return topicUrl;
    }

    private static final long serialVersionUID = -5816652159213898692L;

    public TopicUrl(String topicId, String url, int type) {
        this.topicId = topicId;
        this.url = url;
        this.type = type;
    }

    String topicId;//微博Id
    String url;//长链地址
    String picUrl;//图片地址，可能跟长链地址不一样
    String displayName;//url类型显示的名字
    int type;//url类型

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
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
                "topicId='" + topicId + '\'' +
                ", url='" + url + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", displayName='" + displayName + '\'' +
                ", type=" + type +
                '}';
    }
}
