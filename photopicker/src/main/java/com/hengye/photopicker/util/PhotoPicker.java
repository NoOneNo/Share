package com.hengye.photopicker.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hengye.photopicker.activity.PhotoPickerActivity;
import com.hengye.photopicker.model.Photo;
import com.hengye.photopicker.util.theme.BaseTheme;
import com.hengye.photopicker.util.theme.GreenTheme;

import java.util.ArrayList;

/**
 * Created by yuhy on 16/4/14.
 */
public class PhotoPicker {

    public static void startPhotoPicker(Activity activity){
        Intent intent = new Intent(activity, PhotoPickerActivity.class);
        intent.putExtra(BaseTheme.KEY_CUSTOM_THEME, new GreenTheme());
        activity.startActivityForResult(intent, PhotoPickerActivity.REQUEST_PICK_PHOTO);
    }

    public static void startPhotoPicker(Activity activity, BaseTheme theme){
        Intent intent = new Intent(activity, PhotoPickerActivity.class);
        intent.putExtra(BaseTheme.KEY_CUSTOM_THEME, theme);
        activity.startActivityForResult(intent, PhotoPickerActivity.REQUEST_PICK_PHOTO);
    }

    public static ArrayList<Photo> resolvePhotoPicker(int requestCode, int resultCode, Intent data){
        if(requestCode == PhotoPickerActivity.REQUEST_PICK_PHOTO && resultCode == Activity.RESULT_OK){
            return (ArrayList<Photo>)data.getSerializableExtra(PhotoPickerActivity.PICK_PHOTO_DATA);//得到选择的照片
        }
        return null;
    }

    public static Photo getSinglePhotoPickerResult(Intent intent){
        if(intent == null){
            return null;
        }
        return (Photo)intent.getSerializableExtra(BaseTheme.KEY_SINGLE_PHOTO);
    }

    public static void startSinglePhotoPickerTarget(Context context, String targetName, Photo photo){
        Intent intent = new Intent();
        intent.setClassName(context, targetName);
        intent.putExtra(BaseTheme.KEY_SINGLE_PHOTO, photo);
        context.startActivity(intent);
    }

    public static Photo resolveSinglePhotoPicker(int requestCode, int resultCode, Intent data){
        if(requestCode == PhotoPickerActivity.REQUEST_PICK_PHOTO && resultCode == Activity.RESULT_OK){
            return (Photo)data.getSerializableExtra(BaseTheme.KEY_SINGLE_PHOTO);
        }
        return null;
    }
}
