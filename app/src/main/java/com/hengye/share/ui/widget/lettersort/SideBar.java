package com.hengye.share.ui.widget.lettersort;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.hengye.share.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yuhy on 16/4/22.
 */
public class SideBar extends View {

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    // 触摸事件
    private OnTouchLetterListener mOnTouchLetterListener;
    // 26个字母
    private int mPosition = -1;// 选中
    private Paint mPaint = new Paint();

    List<String> mData;

    private int mBackgroundNormalColor, mBackgroundPressedColor;
    private int mLetterNormalColor, mLetterPressedColor;

    public static String[] DEFAULT_LETTER = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#" };

    private void init(Context context, AttributeSet attrs) {
        if(attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SideBar);
            mBackgroundNormalColor = ta.getColor(R.styleable.SideBar_backgroundNormalColor, getDefaultBackgroundNormalColor());
            mBackgroundPressedColor = ta.getColor(R.styleable.SideBar_backgroundPressedColor, getDefaultBackgroundPressedColor());
            mLetterNormalColor = ta.getColor(R.styleable.SideBar_letterNormalColor, getDefaultLetterNormalColor());
            mLetterPressedColor = ta.getColor(R.styleable.SideBar_letterPressedColor, getDefaultLetterPressedColor());
            ta.recycle();
        }else{
            mBackgroundNormalColor = getDefaultBackgroundNormalColor();
            mBackgroundPressedColor = getDefaultBackgroundPressedColor();
            mLetterNormalColor = getDefaultLetterNormalColor();
            mLetterPressedColor = getDefaultLetterPressedColor();
        }
    }

    public List<String> getData(){
        if(mData == null){
            mData = Arrays.asList(DEFAULT_LETTER);
        }
        return mData;
    }

    public void setData(List<String> data){
        mData = data;
    }

    public int getCurrentPosition(){
        return mPosition;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / getData().size();// 获取每一个字母的高度

        for (int i = 0; i < getData().size(); i++) {
            mPaint.setColor(getLetterColor());
            // mPaint.setColor(Color.WHITE);
            mPaint.setTypeface(Typeface.DEFAULT);
            mPaint.setAntiAlias(true);
            mPaint.setTextSize(singleHeight);
            // 选中的状态
            if (i == mPosition) {
                mPaint.setColor(getLetterPressedColor());
                mPaint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - mPaint.measureText(getData().get(i)) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(getData().get(i), xPos, yPos, mPaint);
            mPaint.reset();// 重置画笔
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int lastPosition = mPosition;
        final int position = (int) (y / getHeight() * getData().size());// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundColor(getBackgroundNormalColor());
                mPosition = -1;//
                invalidate();

                if (getOnTouchLetterListener() != null) {
                    getOnTouchLetterListener().onTouchOutside();
                }

                break;

            default:
                setBackgroundColor(getBackgroundPressedColor());
                if (lastPosition != position) {
                    if (position >= 0 && position < getData().size()) {
                        if (getOnTouchLetterListener() != null) {
                            getOnTouchLetterListener().onTouchLetter(getData().get(position));
                        }
                        mPosition = position;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }

    public int getLetterColor(){
        return mLetterNormalColor;
    }

    public int getLetterPressedColor(){
        return mLetterPressedColor;
    }

    public int getBackgroundPressedColor(){
        return mBackgroundPressedColor;
    }

    public int getBackgroundNormalColor(){
        return mBackgroundNormalColor;
    }

    public void setBackgroundNormalColor(int backgroundNormalColor) {
        this.mBackgroundNormalColor = backgroundNormalColor;
    }

    public void setBackgroundPressedColor(int backgroundPressedColor) {
        this.mBackgroundPressedColor = backgroundPressedColor;
    }

    public void setLetterNormalColor(int letterNormalColor) {
        this.mLetterNormalColor = letterNormalColor;
    }

    public void setLetterPressedColor(int letterPressedColor) {
        this.mLetterPressedColor = letterPressedColor;
    }

    public int getDefaultLetterNormalColor(){
        return Color.parseColor("#336666");
    }

    public int getDefaultLetterPressedColor(){
        return Color.parseColor("#3399ff");
    }

    public int getDefaultBackgroundPressedColor(){
        return Color.parseColor("#e0e0e0");
    }

    public int getDefaultBackgroundNormalColor(){
        return getResources().getColor(android.R.color.transparent);
    }

    /**
     * 向外公开的方法
     *
     * @param onTouchLetterListener
     */
    public void setOnTouchLetterListener(
            OnTouchLetterListener onTouchLetterListener) {
        this.mOnTouchLetterListener = onTouchLetterListener;
    }

    public OnTouchLetterListener getOnTouchLetterListener() {
        return mOnTouchLetterListener;
    }

    /**
     * 接口
     *
     * @author coder
     *
     */
    public interface OnTouchLetterListener {

        /**
         * 当接触到某个key的时候会调用;
         * @param s
         */
        void onTouchLetter(String s);

        /**
         * 当离开控件可触摸区域时会调用;
         */
        void onTouchOutside();
    }

}














