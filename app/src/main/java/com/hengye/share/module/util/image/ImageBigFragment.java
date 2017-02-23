package com.hengye.share.module.util.image;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.hengye.share.R;
import com.hengye.share.module.setting.SettingHelper;

/**
 * Created by yuhy on 2017/2/22.
 */

public class ImageBigFragment extends ImageBaseFragment {

    public static ImageBigFragment newInstance(String path) {
        ImageBigFragment fragment = new ImageBigFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        fragment.setArguments(bundle);
        return fragment;
    }

    String mPath;

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        mPath = bundle.getString("path");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private SubsamplingScaleImageView mPhotoView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_big, container, false);

        mPhotoView = (SubsamplingScaleImageView) view.findViewById(R.id.imageView);

        if(mPath != null){
            mPhotoView.setOnImageEventListener(new CustomImageEventListener(mPhotoView));
            mPhotoView.setImage(ImageSource.uri(mPath));
        }
        mPhotoView.setOnLongClickListener(this);

        if(SettingHelper.isClickToCloseGallery()) {
            mPhotoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }

        return view;
    }

    public class CustomImageEventListener implements SubsamplingScaleImageView.OnImageEventListener {
        private final SubsamplingScaleImageView iv;

        public CustomImageEventListener(SubsamplingScaleImageView subsamplingScaleImageView) {
            this.iv = subsamplingScaleImageView;
        }

        public void onReady() {
            float f = 0.5f;
            int sWidth = this.iv.getSWidth();
            int sHeight = this.iv.getSHeight();
            int width = this.iv.getWidth();
            int height = this.iv.getHeight();
            int i = 0;
            if (sWidth == 0 || sHeight == 0 || width == 0 || height == 0) {
                i = 1;
            }
            if (i == 0) {
                if (sWidth <= sHeight) {
                    f = ((float) width) / ((float) sWidth);
                } else {
                    f = ((float) height) / ((float) sHeight);
                }
            }
            if (i == 0 && ((float) sHeight) / ((float) sWidth) > 2.0f) {
                this.iv.animateScaleAndCenter(f, new PointF((float) (sWidth / 2), 0.0f)).withEasing(1).start();
            }
            if (Math.abs(((double) f) - 0.1d) < 0.20000000298023224d) {
                f += 0.2f;
            }
            this.iv.setDoubleTapZoomScale(f);
        }

        public void onImageLoaded() {
        }

        public void onPreviewLoadError(Exception exception) {
        }

        public void onImageLoadError(Exception exception) {
        }

        public void onTileLoadError(Exception exception) {
        }

        public void onPreviewReleased() {
        }
    }
}
