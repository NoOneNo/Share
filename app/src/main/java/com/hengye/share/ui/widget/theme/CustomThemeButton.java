package com.hengye.share.ui.widget.theme;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageButton;

import com.hengye.share.R;

/**
 * Created by yuhy on 16/8/18.
 */
public class CustomThemeButton extends Button {

    public CustomThemeButton(Context context) {
        this(context, null);
    }

    public CustomThemeButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomThemeButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CustomThemeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);

        if (isInEditMode()) {
            return;
        }
        init();
    }

    private void init() {
    }
}
