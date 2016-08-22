package com.hengye.share.ui.widget.image;

import android.content.Context;
import android.util.AttributeSet;

import com.hengye.share.R;

/**
 * Created by yuhy on 16/8/22.
 */
public class AvatarImageView extends SuperImageView{

    public AvatarImageView(Context context) {
        this(context, null);
    }

    public AvatarImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init(){
        setScaleType(ScaleType.CENTER_CROP);
        setAutoClipBitmap(false);
        setImageResource(R.drawable.ic_user_avatar);
        setDefaultImageResId(R.drawable.ic_user_avatar);
    }

}
