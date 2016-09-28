package com.hengye.photopicker.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.Settings;
import android.util.Log;

import com.hengye.photopicker.model.Photo;
import com.hengye.photopicker.model.PhotoAlbum;

public class PhotosUtil {
    private static final String TAG = "PhotosUtil";
    //单例
    private static volatile PhotosUtil instance;

//    public static final String ALL_PHOTO_ALBUM = "mAllPhotoAlbum";
    public static final String ALL_IMAGES_ALBUM = "mAllImagesAlbum";
    public static final String ALL_VIDEO_ALBUM = "mAllVideoAlbum";

    private Uri mImagesUri, mVideoUri, mVideoThumbnailUri;
    private ContentResolver mContentResolver;

    private PhotosUtil(ContentResolver contentResolver) {
        this.mContentResolver = contentResolver;
        this.mImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        this.mVideoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        this.mVideoThumbnailUri = MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI;
    }

    public static PhotosUtil getInstance(ContentResolver mContentResolver) {
        if (instance == null) {
            synchronized (PhotosUtil.class) {
                if (instance == null) {
                    instance = new PhotosUtil(mContentResolver);
                }
            }
        }
        return instance;
    }

    //    private static final String[] ACCEPTABLE_IMAGE_TYPES =
//            new String[] { "image/jpeg", "image/jpg", "image/png", "image/gif" };
//    private static final String WHERE_CLAUSE =
//            "(" + Media.MIME_TYPE + " in (?, ?, ?, ?))";

    private static final int INDEX_ID = 0;//图片存放路径
    private static final int INDEX_DATA_PATH = 1;//图片存放路径
    private static final int INDEX_SIZE = 2;//图片对应的文件大小
    private static final int INDEX_DISPLAY_NAME = 3;//图片对应的文件类型
    private static final int INDEX_MIME_TYPE = 4;//图片对应的文件类型
    private static final int INDEX_DATE_MODIFIED = 5;//图片的修改日期
    private static final int INDEX_BUCKET_ID = 6;//图片对应文件夹的id
    private static final int INDEX_BUCKET_DISPLAY_NAME = 7;//图片对应文件夹的名称
    private static final int INDEX_ORIENTATION = 8;//图片对应旋转角度
    private static final int INDEX_DURATION = 8;//视频对应时长

    //包括视频和图片
    public List<PhotoAlbum> getPhotoAlbums() {
        Long startTime = System.currentTimeMillis();

        List<PhotoAlbum> images = getImageAlbums();
        List<PhotoAlbum> videos = getVideoAlbums();

        boolean isImagesEmpty = false;
        boolean isVideosEmpty = false;
        if(images == null || images.isEmpty()){
            isImagesEmpty = true;
        }
        if(videos == null || videos.isEmpty()){
            isVideosEmpty = true;
        }

        List<PhotoAlbum> result;
        if(isImagesEmpty && isVideosEmpty){
            result =  null;
        }else if(isImagesEmpty){
            result =  videos;
        }else if(isVideosEmpty){
            result =  images;
        }else{
            PhotoAlbum videoAlbum = videos.get(0);
            videos.remove(0);//去掉所有视频相册

//            images.addAll(videos);
            for(int i = 0; i < videos.size(); i++){
                int index = isExistSameBucketId(images, videos.get(i).getBucketId());
                if(index != -1){
                    images.get(index).getImages().addAll(videos.get(i).getImages());
                    Collections.sort(images.get(index).getImages(), new PhotoComparator());
                }else{
                    images.add(videos.get(i));
                }
            }

            Collections.sort(images, new PhotoAlbumComparator());
            images.add(1, videoAlbum);//排序后把所有视频相册插进第二个位置

            List<Photo> totalVideosPhoto = videoAlbum.getImages();
//            int targetVideoPhotoIndex = 0;
//            Photo targetVideoPhoto = totalVideosPhoto.get(0);

            List<Photo> totalImagesPhoto = images.get(0).getImages();
            totalImagesPhoto.addAll(totalVideosPhoto);
            Collections.sort(totalImagesPhoto, new PhotoComparator());

            result = images;
//            int totalImagesSize = totalImagesPhoto.size();
//            for(int i = 0; i < totalImagesSize; i++){
//                Photo photo = totalImagesPhoto.get(i);
//                if(photo)
//            }
        }

        Long finishTime = System.currentTimeMillis();
        Log.i(TAG, "getPhotoImageAlbums cost : " + (finishTime - startTime));
        return result;
    }

    private int isExistSameBucketId(List<PhotoAlbum> list, String bucketId){
        for(int i = 0; i < list.size(); i++){
            if(bucketId.equals(list.get(i).getBucketId())){
                return i;
            }
        }
        return -1;
    }

    public List<PhotoAlbum> getImageAlbums() {

        Long startTime = System.currentTimeMillis();

        Uri uri = mImagesUri.buildUpon().appendQueryParameter("distinct", "true").build();

        Cursor cursor = MediaStore.Images.Media.query(
                mContentResolver, uri,
                new String[]{
                        Media._ID,
                        Media.DATA,
                        Media.SIZE,
                        Media.DISPLAY_NAME,
                        Media.MIME_TYPE,
                        Media.DATE_MODIFIED,
                        Media.BUCKET_ID,
                        Media.BUCKET_DISPLAY_NAME,
                        Media.ORIENTATION},
                null, null, Media.DATE_MODIFIED + " desc");//按修改日期时间从大到小排序
        if(cursor == null){
            return null;
        }
        List<PhotoAlbum> mPhotoAlbumList = new ArrayList<PhotoAlbum>();
        Log.i(TAG, "count : " + cursor.getCount());
        try {
            //用来存储所有图片作为第一个相册;
            PhotoAlbum allPA = new PhotoAlbum(ALL_IMAGES_ALBUM, null, null, null);
            //利用HashMap和bucket_id来区分不同相册
            HashMap<String, PhotoAlbum> mPhotoAlbumMap = new HashMap<String, PhotoAlbum>();
            while (cursor.moveToNext()) {
                String data = cursor.getString(INDEX_DATA_PATH);
                long size = cursor.getInt(INDEX_SIZE);
                String display_name = cursor.getString(INDEX_DISPLAY_NAME);
                String mime_type = cursor.getString(INDEX_MIME_TYPE);
                long date_modified = cursor.getLong(INDEX_DATE_MODIFIED);
                String orientation = cursor.getString(INDEX_ORIENTATION);
                String bucket_id = cursor.getString(INDEX_BUCKET_ID);
                String bucket_display_name = cursor.getString(INDEX_BUCKET_DISPLAY_NAME);

                //判断文件路径是否有效
                File file = new File(data).getParentFile();
                if (file == null) {
                    continue;
                }
                Photo photo = new Photo(data, size, display_name, mime_type, bucket_id, orientation, date_modified);
                allPA.getImages().add(photo);
                //如果map里面已经存在相同的相册
                if (mPhotoAlbumMap.containsKey(bucket_id)) {
                    PhotoAlbum pa = mPhotoAlbumMap.get(bucket_id);
                    pa.getImages().add(photo);
                } else {
                    //新建相册里的第一张图片作为相册封面
                    PhotoAlbum pa = new PhotoAlbum(bucket_id, bucket_display_name, file.getAbsolutePath(), photo);
                    pa.getImages().add(photo);
                    mPhotoAlbumMap.put(bucket_id, pa);
                }
            }
//            allPA.setmCoverPath(allpa)
            if (allPA.getImages().size() < 1) {
                return null;
            }
            //把得到的第一张图片作为所有相册的封面
            allPA.setCoverPhoto(allPA.getImages().get(0));
            mPhotoAlbumList.add(allPA);

            //根据相册相片的数量从大到小排序
            ArrayList<PhotoAlbum> pas = new ArrayList<PhotoAlbum>(mPhotoAlbumMap.values());
            Collections.sort(pas, new PhotoAlbumComparator());

            mPhotoAlbumList.addAll(pas);
            mPhotoAlbumMap.clear();
            mPhotoAlbumMap = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        Long finishTime = System.currentTimeMillis();
        Log.i(TAG, "getImageAlbums cost : " + (finishTime - startTime));
        Log.i(TAG, "images list size : " + mPhotoAlbumList.size());
        return mPhotoAlbumList;
    }


    public List<PhotoAlbum> getVideoAlbums() {

        Long startTime = System.currentTimeMillis();

        Uri uri = mVideoUri.buildUpon().appendQueryParameter("distinct", "true").build();

        Cursor cursor = MediaStore.Images.Media.query(
                mContentResolver, uri,
                new String[]{
                        Media._ID,
                        Media.DATA,
                        Media.SIZE,
                        Media.DISPLAY_NAME,
                        Media.MIME_TYPE,
                        Media.DATE_MODIFIED,
                        Media.BUCKET_ID,
                        Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Video.Media.DURATION},
                null, null, Media.DATE_MODIFIED + " desc");//按修改日期时间从大到小排序
        if(cursor == null){
            return null;
        }
        List<PhotoAlbum> mPhotoAlbumList = new ArrayList<PhotoAlbum>();
        Log.i(TAG, "count : " + cursor.getCount());
        try {
            //用来存储所有视频作为第一个相册;
            PhotoAlbum allPA = new PhotoAlbum(ALL_VIDEO_ALBUM, null, null, null);
            //利用HashMap和bucket_id来区分不同相册
            HashMap<String, PhotoAlbum> mPhotoAlbumMap = new HashMap<String, PhotoAlbum>();
            while (cursor.moveToNext()) {
                String id = cursor.getString(INDEX_ID);
                String data = cursor.getString(INDEX_DATA_PATH);
                long size = cursor.getInt(INDEX_SIZE);
                String display_name = cursor.getString(INDEX_DISPLAY_NAME);
                String mime_type = cursor.getString(INDEX_MIME_TYPE);
                long date_modified = cursor.getLong(INDEX_DATE_MODIFIED);
                String bucket_id = cursor.getString(INDEX_BUCKET_ID);
                String bucket_display_name = cursor.getString(INDEX_BUCKET_DISPLAY_NAME);
                long duration = cursor.getLong(INDEX_DURATION);

                //判断文件路径是否有效
                File file = new File(data).getParentFile();
                if (file == null) {
                    continue;
                }
//                Log.i(TAG, "video : path : " + data);
                String thumbnail = getVideoThumbnail(id);
                Photo photo = new Photo(data, size, display_name, mime_type, bucket_id, null, date_modified, true);
                photo.setDuration(duration);
                photo.setThumbnail(thumbnail);
                allPA.getImages().add(photo);
                //如果map里面已经存在相同的相册
                if (mPhotoAlbumMap.containsKey(bucket_id)) {
                    PhotoAlbum pa = mPhotoAlbumMap.get(bucket_id);
                    pa.getImages().add(photo);
                } else {
                    //新建相册里的第一张图片作为相册封面
                    PhotoAlbum pa = new PhotoAlbum(bucket_id, bucket_display_name, file.getAbsolutePath(), photo);
                    pa.getImages().add(photo);
                    mPhotoAlbumMap.put(bucket_id, pa);
                }
            }
//            allPA.setmCoverPath(allpa)
            if (allPA.getImages().size() < 1) {
                return null;
            }
            //把得到的第一张图片作为所有相册的封面
            allPA.setCoverPhoto(allPA.getImages().get(0));
            mPhotoAlbumList.add(allPA);

            //根据相册相片的数量从大到小排序
            ArrayList<PhotoAlbum> pas = new ArrayList<PhotoAlbum>(mPhotoAlbumMap.values());
            Collections.sort(pas, new PhotoAlbumComparator());

            mPhotoAlbumList.addAll(pas);
            mPhotoAlbumMap.clear();
            mPhotoAlbumMap = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        Long finishTime = System.currentTimeMillis();
        Log.i(TAG, "getVideoAlbums cost : " + (finishTime - startTime));
        Log.i(TAG, "video list size : " + mPhotoAlbumList.size());
        return mPhotoAlbumList;
    }

    private static final int INDEX_THUMBNAIL_DATA_PATH = 0;//存放路径
    private static final int INDEX_THUMBNAIL_VIDEO_ID = 1;//对应视频的ID
    private static final String COLUMN_THUMBNAIL_VIDEO_ID = "video_id";//对应视频的ID列名

    private String getVideoThumbnail(String id) {
        Uri uri = mVideoThumbnailUri.buildUpon().appendQueryParameter("distinct", "true").build();

        Cursor cursor = mContentResolver.query(
                uri,
                new String[]{
                        Media.DATA,
                        COLUMN_THUMBNAIL_VIDEO_ID},
                COLUMN_THUMBNAIL_VIDEO_ID + " = ?", new String[]{id}, COLUMN_THUMBNAIL_VIDEO_ID);
        try {
            while (cursor.moveToNext()) {
                String data = cursor.getString(INDEX_THUMBNAIL_DATA_PATH);
                String video_id = cursor.getString(INDEX_THUMBNAIL_VIDEO_ID);
//                Log.i(TAG, "video thumbnail video id : " + video_id + ", id : " + id);
                if(cursor.isLast()){
//                    Log.i(TAG, "video thumbnail path : " + data);
                    return data;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return null;
    }

    class PhotoComparator implements Comparator<Photo>{
        @Override
        public int compare(Photo lhs, Photo rhs) {
            if (lhs.getDateModified() >= rhs.getDateModified()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    class PhotoAlbumComparator implements Comparator<PhotoAlbum>{
        @Override
        public int compare(PhotoAlbum lhs, PhotoAlbum rhs) {
            if (lhs.getImages().size() >= rhs.getImages().size()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        if(!storageDir.exists()){
            storageDir.mkdirs();
        }
//        Environment.getExternalStorageDirectory().
//        File image = new File(storageDir, imageFileName + ".jpg");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

}
































