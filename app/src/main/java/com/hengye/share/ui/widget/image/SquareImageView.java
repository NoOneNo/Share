package com.hengye.share.ui.widget.image;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by yuhy on 16/7/25.
 */
public class SquareImageView extends SuperImageView {

    public SquareImageView(Context context) {
        this(context, null);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public ViewGroup.LayoutParams getLayoutParams() {
        ViewGroup.LayoutParams lp = super.getLayoutParams();
        lp.height = lp.width;
        return lp;
    }

}
