package com.hengye.share.model.sina;

import java.io.Serializable;

/**
 * Created by yuhy on 16/7/29.
 */
public class WBUploadPicture implements Serializable{

    private static final long serialVersionUID = 3806073561257634554L;

    private String pic_id;

    private String thumbnail_pic;

    private String bmiddle_pic;

    private String original_pic;

    public String getPic_id() {
        return pic_id;
    }

    public void setPic_id(String pic_id) {
        this.pic_id = pic_id;
    }

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    public String getBmiddle_pic() {
        return bmiddle_pic;
    }

    public void setBmiddle_pic(String bmiddle_pic) {
        this.bmiddle_pic = bmiddle_pic;
    }

    public String getOriginal_pic() {
        return original_pic;
    }

    public void setOriginal_pic(String original_pic) {
        this.original_pic = original_pic;
    }
}
