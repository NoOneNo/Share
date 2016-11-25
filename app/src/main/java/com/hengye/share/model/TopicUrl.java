package com.hengye.share.model;

import android.support.annotation.NonNull;

import com.hengye.share.model.sina.WBShortUrl;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.GsonUtil;
import com.hengye.share.util.L;

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
                }
            }
        }
        return COMMON;
    }

    public static String getDisplayName(WBShortUrl wbShortUrl){
        if (wbShortUrl != null && !CommonUtil.isEmpty(wbShortUrl.getAnnotations())) {
            WBShortUrl.AnnotationsBean annotationsBean = wbShortUrl.getAnnotations().get(0);
            String json = GsonUtil.toJson(annotationsBean.getObject());
            try {
                JSONObject jsonObject = new JSONObject(json);
                String displayName = jsonObject.getString("display_name");
                L.debug("url : {}, displayName : {}", wbShortUrl.getUrl_long(), displayName);
                return displayName;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public static TopicUrl getTopicUrl(String topicId, @NonNull WBShortUrl wbShortUrl) {
        TopicUrl topicUrl = new TopicUrl(topicId, wbShortUrl.getUrl_long(), getUrlType(wbShortUrl));
        topicUrl.setDisplayName(getDisplayName(wbShortUrl));
        return topicUrl;
    }

    private static final long serialVersionUID = -5816652159213898692L;

    public TopicUrl(String topicId, String url, int type) {
        this.topicId = topicId;
        this.url = url;
        this.type = type;
    }

    String topicId;//微博Id
    String url;//url
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

    public String getLinkUrl(){
        return DataUtil.WEB_SCHEME + url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
