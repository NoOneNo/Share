package com.hengye.share.ui.widget.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

import com.hengye.share.R;
import com.hengye.share.util.ThemeUtil;

/**
 * Created by yuhy on 16/8/16.
 */
public class CommonTabLayout extends TabLayout {

    public CommonTabLayout(Context context) {
        this(context, null);
    }

    public CommonTabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommonTabLayout);
        setAutoTintThemeColor(ta.getBoolean(R.styleable.CommonTabLayout_autoTintThemeColor, false));
        ta.recycle();

        init();
    }

    private boolean mAutoTintThemeColor = false;

    void init(){
        if(isAutoTintThemeColor()){
            setBackgroundColor(ThemeUtil.getColor());
            setTabTextColors(ThemeUtil.getTintLightColor(), ThemeUtil.getTintDarkColor());
        }
    }

    public boolean isAutoTintThemeColor() {
        return mAutoTintThemeColor;
    }

    public void setAutoTintThemeColor(boolean autoTintThemeColor) {
        this.mAutoTintThemeColor = autoTintThemeColor;
    }
}
