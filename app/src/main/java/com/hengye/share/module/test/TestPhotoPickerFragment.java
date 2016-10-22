package com.hengye.share.module.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.StateFragment;
import com.hengye.share.ui.widget.image.GridGalleryEditorView;

/**
 * Created by yuhy on 16/7/18.
 */
public class TestPhotoPickerFragment extends StateFragment {

    @Override
    public String getTitle() {
        return "test";
    }

    @Override
    public int getContentResId() {
        return R.layout.test_photo_picker;
    }

    @Override
    public void initContent(@Nullable Bundle savedInstanceState) {

        mGGEV = (GridGalleryEditorView) findViewById(R.id.gallery_edit);

        findViewById(R.id.pick_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGGEV.startPhotoPicker();

            }
        });
    }

    GridGalleryEditorView mGGEV;
}
