package com.hengye.share.ui.widget.theme;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.hengye.share.R;
import com.hengye.share.ui.widget.util.SelectorLoader;

/**
 * Created by yuhy on 16/8/18.
 */
public class CustomThemeImageButton extends ImageButton {

    public CustomThemeImageButton(Context context) {
        this(context, null);
    }

    public CustomThemeImageButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomThemeImageButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CustomThemeImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);

        if (isInEditMode()) {
            return;
        }
        init();
    }

    private void init() {

    }
}
