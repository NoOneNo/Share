package com.hengye.photopicker.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PhotoAlbum implements Serializable {

    private static final long serialVersionUID = 3124572393016817968L;

    private String bucketId;//相册的唯一id
    private String bucketDisplayName;//相册名字
    private String dataPath;//相册文件夹地址
    private Photo coverPhoto;//相册封面照片
    private ArrayList<Photo> images;

    public PhotoAlbum() {
        images = new ArrayList<Photo>();
    }

    public PhotoAlbum(String bucketId, String bucketDisplayName,
                      String dataPath, Photo coverPhoto) {
        this();
        this.bucketId = bucketId;
        this.bucketDisplayName = bucketDisplayName;
        this.dataPath = dataPath;
        this.coverPhoto = coverPhoto;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketDisplayName() {
        return bucketDisplayName;
    }

    public void setBucketDisplayName(String bucketDisplayName) {
        this.bucketDisplayName = bucketDisplayName;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public Photo getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(Photo coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public ArrayList<Photo> getImages() {
        return images;
    }

    public int getImagesSize() {
        if (getImages() != null && getImages().size() != 0) {
            if (getImages().get(0).isNull()) {
                return getImages().size() - 1;
            } else {
                return getImages().size();
            }
        } else {
            return 0;
        }
    }

    public void setImages(ArrayList<Photo> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "PhotoAlbum [bucketId=" + bucketId + ", bucketDisplayName="
                + bucketDisplayName + ", dataPath=" + dataPath
                + ", images=" + images + "]";
    }

}
