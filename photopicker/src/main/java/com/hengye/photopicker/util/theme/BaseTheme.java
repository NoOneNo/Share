package com.hengye.photopicker.util.theme;

import android.view.View;

import com.hengye.photopicker.model.Photo;

import java.io.Serializable;

public abstract class BaseTheme implements Serializable{

    private static final long serialVersionUID = -8854619149674430435L;


    public final static String KEY_CUSTOM_THEME = "custom_theme";
    public final static String KEY_SINGLE_PHOTO = "single_photo";

    public BaseTheme(){}

    public final static int TYPE_PHOTO = 1;//图片和视频
    public final static int TYPE_IMAGE = 2;//图片
    public final static int TYPE_VIDEO = 3;//视频

    boolean mIsCanTakePhoto = true;
    boolean mIsPickSinglePhoto = false;//如果是点击单张图片模式, 就不显示下面工具条, 和图片的圆圈选择, 选择图片事件可自定义;

//    OnSinglePhotoClick mOnSinglePhotoClick;
    String mSinglePhotoStartActivityName;
    int mContentType = TYPE_PHOTO;
    int mMaxSelectImageSize = DEFAULT_MAX_PICK_IMAGE_COUNT;
    int mMaxSelectVideoSize = DEFAULT_MAX_PICK_VIDEO_COUNT;
    public int getContentType(){
        return mContentType;
    }

    public final static int DEFAULT_MAX_PICK_IMAGE_COUNT = 9;//默认可选择图片数量的最大值
    public final static int DEFAULT_MAX_PICK_VIDEO_COUNT = 1;//默认可选择视频数量的最大值
    public int getMaxSelectImageSize(){
        return mMaxSelectImageSize;
    }

    public int getMaxSelectVideoSize(){
        return mMaxSelectVideoSize;
    }

    public void setContentType(int contentType) {
        this.mContentType = contentType;
    }

    public void setMaxSelectImageSize(int maxSelectImageSize) {
        this.mMaxSelectImageSize = maxSelectImageSize;
    }

    public void setMaxSelectVideoSize(int maxSelectVideoSize) {
        this.mMaxSelectVideoSize = maxSelectVideoSize;
    }


    //是否支持拍照
    public boolean isCanTakePhoto(){
        return mIsCanTakePhoto;
    }

    public void setIsCanTakePhoto(boolean isCanTakePhoto) {
        this.mIsCanTakePhoto = isCanTakePhoto;
    }

    public boolean isPickSinglePhoto(){
        return mIsPickSinglePhoto;
    }

    public void setIsPickSinglePhoto(boolean isPickSinglePhoto){
        this.mIsPickSinglePhoto = isPickSinglePhoto;
    }

    public String getSinglePhotoStartActivityName() {
        return mSinglePhotoStartActivityName;
    }

    public void setSinglePhotoStartActivityName(String singlePhotoStartActivityName) {
        this.mSinglePhotoStartActivityName = singlePhotoStartActivityName;
    }

    //    public OnSinglePhotoClick getOnSinglePhotoClick() {
//        return mOnSinglePhotoClick;
//    }
//
//    public void setOnSinglePhotoClick(OnSinglePhotoClick onSinglePhotoClick) {
//        this.mOnSinglePhotoClick = onSinglePhotoClick;
//    }

    //标题栏背景色
    public abstract int getTitleBackground();

    //标题栏后退按钮
    public abstract int getTitleBackIcon();

    //标题栏字体颜色
    public abstract int getTitleTextFontColor();

    //当前相册的名字(标题栏中间位置)的右边按钮(选中状态)
    public abstract int getTitleNavigationSelectedIcon();

    //当前相册的名字(标题栏中间位置)的右边按钮(未选中状态)
    public abstract int getTitleNavigationUnSelectedIcon();

    //相册列表listview的selector
    public abstract int getAlbumListViewSelector();

    //当前选中的相册颜色
    public abstract int getAlbumListItemSelected();

    //相册列表如果存在选中的相册显示圆点
    public abstract int getAlbumPointSelected();

    //照片选中状态的按钮
    public abstract int getPhotoSelected();

    //下一步按钮样式
    public abstract int getNextBtn();


//    public interface OnSinglePhotoClick{
//        void onClick(View v, Photo photo);
//    }

}
