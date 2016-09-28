package com.hengye.photopicker.activity;

import android.os.Bundle;

import com.hengye.photopicker.R;
import com.hengye.photopicker.fragment.GalleryFragment;


public class PhotoPickerActivity extends ThemeActivity{

    private GalleryFragment mGalleryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_pick);
    }

    @Override
    public void onBackPressed() {
        if (mGalleryFragment != null && mGalleryFragment.isVisible()) {
            mGalleryFragment.runExitAnimation(new Runnable() {
                public void run() {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    }
                }
            });
        } else {
            super.onBackPressed();
        }
    }

    public void addGalleryFragment(GalleryFragment galleryFragment) {
        this.mGalleryFragment = galleryFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.photo_pick_fragment_container, this.mGalleryFragment)
                .addToBackStack(null)
                .commit();
    }

}



