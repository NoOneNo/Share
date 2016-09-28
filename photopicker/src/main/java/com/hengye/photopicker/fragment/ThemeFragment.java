package com.hengye.photopicker.fragment;

import android.support.v4.app.Fragment;

import com.hengye.photopicker.activity.PhotoPickerActivity;
import com.hengye.photopicker.activity.ThemeActivity;
import com.hengye.photopicker.model.Photo;
import com.hengye.photopicker.util.theme.BaseTheme;
import com.hengye.photopicker.util.theme.GreenTheme;

import java.util.ArrayList;

public class ThemeFragment extends Fragment{

    public BaseTheme mTheme;

    public BaseTheme getCustomTheme(){
        if(mTheme != null){
            return mTheme;
        }

        if(getActivity() == null){
            throw new IllegalStateException("activity is null");
        }

        if(getActivity() instanceof PhotoPickerActivity){
            mTheme = ((ThemeActivity)getActivity()).mTheme;
        }

        if(mTheme == null){
            mTheme = new GreenTheme();
        }

        return mTheme;
    }

    public ArrayList<Photo> getSelectPhotos() {
        return ((ThemeActivity)getActivity()).getSelectPhotos();
//        return mSelectPhotos;
    }
}
