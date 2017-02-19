package com.hengye.share.model;

import java.util.HashMap;
/**
 * Created by yuhy on 2016/11/10.
 */

public interface StatusShortUrl {

    /**
     * 当前微博id
     * @return
     */
    String getId();

    /**
     * 当前微博内容
     * @return
     */
    String getContent();

    /**
     * 设置短链
     * @param urlMap
     */
    void setUrlMap(HashMap<String, StatusUrl> urlMap);

    /**
     * 得到短链
     * @return
     */
    HashMap<String, StatusUrl> getUrlMap();
}
