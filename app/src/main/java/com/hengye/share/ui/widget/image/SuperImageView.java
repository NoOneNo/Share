package com.hengye.share.ui.widget.image;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;

import com.android.volley.view.NetworkImageViewPlus;
import com.hengye.share.util.RequestManager;

/**
 * Created by yuhy on 16/7/22.
 */
public class SuperImageView extends NetworkImageViewPlus {

    public SuperImageView(Context context) {
        this(context, null);
    }

    public SuperImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Sets URL of the image that should be loaded into this view. Note that calling this will
     * immediately either set the cached image (if available) or the default image specified by
     * {@link SuperImageView#setDefaultImageResId(int)} on the view.
     */
    public void setImageUrl(String url){
        super.setImageUrl(url, RequestManager.getImageLoader());
    }

    /**
     * Sets the default image resource ID to be used for this view until the attempt to load it
     * completes.
     */
    public void setDefaultImageResId(int defaultImage) {
        super.setDefaultImageResId(defaultImage);
        ViewCompat.postInvalidateOnAnimation(this);
    }


}
