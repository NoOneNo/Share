package com.hengye.photopicker.model;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Photo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5416304942167044170L;

    private String dataPath;//图片路径
    private long size;//图片大小，单位为byte
    private String displayName;//图片名字
    private String mimeType;//图片文件类型
    private String bucketId;//相册的唯一id
    private String orientation;//图片旋转角度
    private long dateModified;//图片修改日期

    private boolean isVideo = false;//是否是视频
    private String thumbnail;//视频的缩略图
    private long duration;//视频时长

    private boolean isSelected = false;//保存图片是否被选择的状态, 默认没有被选中

    private boolean isNull = false;

    public Photo() {

    }

    public Photo(boolean isNull) {
        this.isNull = isNull;
    }

    public Photo(String dataPath, long size, String displayName, String mimeType, String bucketId, String orientation, long dateModified, boolean isVideo) {
        this();
        this.dataPath = dataPath;
        this.size = size;
        this.displayName = displayName;
        this.mimeType = mimeType;
        this.bucketId = bucketId;
        this.orientation = orientation;
        this.dateModified = dateModified;
        this.isVideo = isVideo;
    }

    public Photo(String dataPath, long size, String displayName, String mimeType, String bucketId, String orientation, long dateModified) {
        this(dataPath, size, displayName, mimeType, bucketId, orientation, dateModified, false);
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public long getDateModified() {
        return dateModified;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setIsNull(boolean isNull) {
        this.isNull = isNull;
    }

    @Override
    public String toString() {
        return "Photo [dataPath=" + dataPath + ", size=" + size + ", displayName="
                + displayName + ", mimeType=" + mimeType + ", bucketId=" + bucketId + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataPath == null) ? 0 : dataPath.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Photo other = (Photo) obj;
        if (dataPath == null) {
            if (other.dataPath != null)
                return false;
        } else if (!dataPath.equals(other.dataPath))
            return false;
        return true;
    }

    private String formatDuration;
    public String formatVideoDuration(){
        if(!TextUtils.isEmpty(formatDuration)){
            return formatDuration;
        }

        long day = duration / (24 * 60 * 60 * 1000);
        long hour = (duration / (60 * 60 * 1000) - day * 24);
        long minute = ((duration / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long second = (duration / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);

        GregorianCalendar gc = new GregorianCalendar(0, 0, 0, (int)hour, (int)minute, (int)second);
        Date date = gc.getTime();
        new SimpleDateFormat("HH:mm", Locale.US).format(date);
        if(day > 0){
            formatDuration = "超过一天";
        }else if(hour > 0){
            formatDuration = new SimpleDateFormat("HH:mm", Locale.US).format(date);
        }else if(minute > 0){
            formatDuration = new SimpleDateFormat("mm:ss", Locale.US).format(date);
        }else if(second >= 0){
            formatDuration = new SimpleDateFormat("mm:ss", Locale.US).format(date);
        }else{
            formatDuration = "时长未知";
        }
        return formatDuration;
    }

    public String formatSize(Context context){
        return Formatter.formatFileSize(context, getSize());
    }

    public static long getTotalPhotoSize(List<Photo> photos){
        Long sizeSum = 0L;
        for(Photo p : photos){
            sizeSum += p.getSize();
        }
        return sizeSum;
    }

    public static long getTotalImageSize(List<Photo> photos){
        Long sizeSum = 0L;
        for(Photo p : photos){
            if(p.isVideo){
                continue;
            }
            sizeSum += p.getSize();
        }
        return sizeSum;
    }

    public static String formatTotalImageSize(Context context, List<Photo> photos){
        return Formatter.formatFileSize(context, getTotalImageSize(photos));
    }

    public static String formatTotalPhotoSize(Context context, List<Photo> photos){
        return Formatter.formatFileSize(context, getTotalPhotoSize(photos));
    }

    public static int getExistImageCount(List<Photo> photos){
        int count = 0;
        for(Photo photo : photos){
            if(!photo.isVideo()){
                count++;
            }
        }
        return count;
    }

    public static int getExistVideoCount(List<Photo> photos){
        int count = 0;
        for(Photo photo : photos){
            if(photo.isVideo()){
                count++;
            }
        }
        return count;
    }

}
