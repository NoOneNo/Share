package com.hengye.photopicker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;


import com.hengye.photopicker.model.Photo;
import com.hengye.photopicker.util.theme.BaseTheme;
import com.hengye.photopicker.util.theme.GreenTheme;

import java.util.ArrayList;

public class ThemeActivity extends FragmentActivity{

    public BaseTheme mTheme;

    private ArrayList<Photo> mSelectPhotos = new ArrayList<Photo>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCustomTheme();
    }

    private void initCustomTheme(){
        Object obj = getIntent().getSerializableExtra(BaseTheme.KEY_CUSTOM_THEME);
        if(obj instanceof BaseTheme){
            mTheme = (BaseTheme) obj;
        }

        if(mTheme == null){
            mTheme = new GreenTheme();
        }
    }

    public ArrayList<Photo> getSelectPhotos() {
        return mSelectPhotos;
    }

    public void setSelectPhotos(ArrayList<Photo> selectPhotos) {
        this.mSelectPhotos = selectPhotos;
    }

    public static final int REQUEST_PICK_PHOTO = 6;
    public final static String PICK_PHOTO_DATA = "pickPhotoData";
    public final static String PICK_PHOTO_PRIMITIVE = "pickPhotoPrimitive";
    public void finishAtyWithData(ArrayList<Photo> data, boolean isPrimitive) {
        Intent intent = new Intent();
        intent.putExtra(PICK_PHOTO_DATA, data);
        intent.putExtra(PICK_PHOTO_PRIMITIVE, isPrimitive);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
