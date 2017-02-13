package com.hengye.photopicker.activity;

import android.os.Bundle;

import com.hengye.photopicker.R;
import com.hengye.photopicker.fragment.GalleryFragment;
import com.hengye.photopicker.model.Photo;

import java.util.ArrayList;

public class GalleryActivity extends ThemeActivity {

    GalleryFragment mGalleryFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_gallery);

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, generateGalleryFragment())
                    .commit();
        }
    }

    public GalleryFragment generateGalleryFragment(){
        ArrayList<Photo> totalPhotos = (ArrayList<Photo>) getIntent().getSerializableExtra(GalleryFragment.IMG_TOTAL_PHOTO);
        setSelectPhotos(totalPhotos);
        ArrayList<Photo> temp = new ArrayList<Photo>();
        if(totalPhotos != null) {
            temp.addAll(totalPhotos);
        }
        int indexStart = getIntent().getIntExtra(GalleryFragment.IMG_INDEX, 0);
        int[] imgLocationOnScreen = getIntent().getIntArrayExtra(GalleryFragment.IMG_LOCATION);
        int imgWidth = getIntent().getIntExtra(GalleryFragment.IMG_WIDTH, 0);
        boolean isTakePhoto = getIntent().getBooleanExtra(GalleryFragment.IMG_TAKE_PHOTO, false);
        boolean isPrimitive = getIntent().getBooleanExtra(PhotoPickerActivity.PICK_PHOTO_PRIMITIVE, false);
        return GalleryFragment.newInstance(temp, indexStart, imgLocationOnScreen, imgWidth, isTakePhoto, isPrimitive);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mGalleryFragment != null && mGalleryFragment.isVisible()) {
            mGalleryFragment.runExitAnimation(new Runnable() {
                public void run() {
                    finish();
                }
            });
        }
    }
}
