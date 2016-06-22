package com.hengye.share.ui.support.textspan;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class SimpleClickableSpan2 extends ClickableSpan {

    protected int mNormalColor = -1;
    protected int mSelectedColor = -1;
    protected boolean mShowUnderLine = false;
    protected boolean mPress = false;
    protected View.OnClickListener mOnClickListener;
    protected View.OnLongClickListener mOnLongClickListener;

    public SimpleClickableSpan2(){

    }

    @Override
    public void onClick(View widget) {
        if(mOnClickListener != null){
            mOnClickListener.onClick(widget);
        }
    }

    public void onLongClick(View widget) {
        if(mOnLongClickListener != null){
            mOnLongClickListener.onLongClick(widget);
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

    public SimpleClickableSpan2 setNormalColor(int normalColor) {
        this.mNormalColor = normalColor;
        return this;
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public SimpleClickableSpan2 setSelectedColor(int selectedColor) {
        this.mSelectedColor = selectedColor;
        return this;
    }

    public boolean isShowUnderLine() {
        return mShowUnderLine;
    }

    public SimpleClickableSpan2 setShowUnderLine(boolean showUnderLine) {
        this.mShowUnderLine = showUnderLine;
        return this;
    }

    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public SimpleClickableSpan2 setOnClickListener(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
        return this;
    }

    public View.OnLongClickListener getOnLongClickListener() {
        return mOnLongClickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.mOnLongClickListener = onLongClickListener;
    }

    public boolean isPress() {
        return mPress;
    }

    public void setPress(boolean press) {
        this.mPress = press;
    }

}
