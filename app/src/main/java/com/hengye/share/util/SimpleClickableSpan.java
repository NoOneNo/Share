package com.hengye.share.util;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class SimpleClickableSpan extends ClickableSpan {

    protected int mNormalColor = -1;
    protected int mSelectedColor = -1;
    protected boolean mShowUnderLine = false;
    protected boolean mPress = false;
    protected View.OnClickListener mOnClickListener;

    public SimpleClickableSpan(){

    }

    @Override
    public void onClick(View widget) {
        if(mOnClickListener != null){
            mOnClickListener.onClick(widget);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        if(mPress){
            if (mSelectedColor != -1) {
                ds.setColor(mSelectedColor);
            }
        }else {
            if (mNormalColor != -1) {
                ds.setColor(mNormalColor);
            }
        }
        ds.setUnderlineText(mShowUnderLine);
    }

    public int getNormalColor() {
        return mNormalColor;
    }

    public SimpleClickableSpan setNormalColor(int normalColor) {
        this.mNormalColor = normalColor;
        return this;
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public SimpleClickableSpan setSelectedColor(int selectedColor) {
        this.mSelectedColor = selectedColor;
        return this;
    }

    public boolean isShowUnderLine() {
        return mShowUnderLine;
    }

    public SimpleClickableSpan setShowUnderLine(boolean showUnderLine) {
        this.mShowUnderLine = showUnderLine;
        return this;
    }

    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public SimpleClickableSpan setOnClickListener(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
        return this;
    }

    public boolean isPress() {
        return mPress;
    }

    public void setPress(boolean press) {
        this.mPress = press;
    }

}
