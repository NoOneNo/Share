package com.hengye.share.ui.widget.dialog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectorLoader2 {

    /**
     * 用于代码设置控件selector
     */
    private SelectorLoader2(){
    }

    /**
     * singleton
     */
    private static SelectorLoader2 selectorLoader;
    public static SelectorLoader2 getInstance(){
        if(selectorLoader == null){
            selectorLoader = new SelectorLoader2();
        }
        return selectorLoader;
    }

    /**
     * 设置View背景的selector，imageView也是设置background的selector，若需要设置src的selector，使用setImageSelector（）
     * @param view      设置的View
     * @param normalId  一般情况下资源Id
     * @param pressedId 点击情况下资源Id
     * @param context
     */
    public void setBackgroundSelector(View view, int normalId, int pressedId, Context context){
        setBackgroundSelector(view, getPressStateSelectorDrawable(context, normalId, pressedId));
    }

    public void setBackgroundSelector(View view, Drawable normalDrawable, Drawable pressedDrawable){
        setBackgroundSelector(view, getPressStateSelectorDrawable(normalDrawable, pressedDrawable));
    }

    public void setBackgroundSelector(View view, Bitmap normalBitmap, Bitmap pressedBitmap, Context context){
        setBackgroundSelector(view, getPressStateSelectorDrawable(context, normalBitmap, pressedBitmap));
    }

    public void setBackgroundSelector(View view, String normalPath, String pressedPath){
        setBackgroundSelector(view, getPressStateSelectorDrawable(normalPath, pressedPath));
    }

    /**
     * 设置imageView的src的selector
     * @param context
     * @param imageView 需要设置的ImageView
     * @param normalId  一般情况下资源Id
     * @param pressedId 点击情况下资源Id
     */
    public void setImageSelector(Context context, ImageView imageView, int normalId, int pressedId){
        imageView.setImageDrawable(getPressStateSelectorDrawable(context, normalId, pressedId));
    }

    public void setImageSelector(ImageView imageView, Drawable normalDrawable, Drawable pressedDrawable){
        imageView.setImageDrawable(getPressStateSelectorDrawable(normalDrawable, pressedDrawable));
    }

    public void setImageSelector(ImageView imageView, Bitmap normalBitmap, Bitmap pressedBitmap, Context context){
        imageView.setImageDrawable(getPressStateSelectorDrawable(context, normalBitmap, pressedBitmap));
    }

    public void setImageSelector(ImageView imageView, String normalPath, String pressedPath){
        imageView.setImageDrawable(getPressStateSelectorDrawable(normalPath, pressedPath));
    }

    /**
     * 设置TextView字体颜色的Selector
     * @param textView      需要设置的textView
     * @param normalColor   正常颜色
     * @param pressColor    点击颜色
     */
    public void setTextColorSelector(TextView textView, int normalColor, int pressColor){
        textView.setTextColor(getColorStateListSelector(normalColor, pressColor));
    }

    /**
     * 设置checkbox，radiobutton的背景selector，若需要设置勾选的selector，使用setCheckButtonSelector（）
     * @param view      需要设置的radioButton或者checkbox
     * @param normalId  一般情况下资源Id
     * @param pressedId 点击情况下资源Id
     * @param context
     */
    public void setCheckBackgroundSelector(CompoundButton view, int normalId, int pressedId, Context context){
        setBackgroundSelector(view, getCheckStateSelectorDrawable(context, normalId, pressedId));
    }

    public void setCheckBackgroundSelector(CompoundButton view, Drawable normalDrawable, Drawable pressedDrawable){
        setBackgroundSelector(view, getCheckStateSelectorDrawable(normalDrawable, pressedDrawable));
    }

    public void setCheckBackgroundSelector(CompoundButton view, Bitmap normalBitmap, Bitmap pressedBitmap, Context context){
        setBackgroundSelector(view, getCheckStateSelectorDrawable(context, normalBitmap, pressedBitmap));
    }

    public void setCheckBackgroundSelector(CompoundButton view, String normalPath, String pressedPath){
        setBackgroundSelector(view, getCheckStateSelectorDrawable(normalPath, pressedPath));
    }

    /**
     * 隐藏checkbox，radiobutton中的选择按钮
     * @param view
     */
    public void setCheckButtonNullDrawable(CompoundButton view){
        view.setButtonDrawable(android.R.color.transparent);
    }

    /**
     * 设置checkbox，radiobutton的背景buttonselector，即设置勾选的selector
     * @param view      需要设置的radioButton或者checkbox
     * @param normalId  一般情况下资源Id
     * @param pressedId 点击情况下资源Id
     * @param context
     */
    public void setCheckButtonSelector(CompoundButton view, int normalId, int pressedId, Context context){
        view.setButtonDrawable(getCheckStateSelectorDrawable(context, normalId, pressedId));
    }

    public void setCheckButtonSelector(CompoundButton view, Drawable normalDrawable, Drawable pressedDrawable){
        view.setButtonDrawable(getCheckStateSelectorDrawable(normalDrawable, pressedDrawable));
    }

    public void setCheckButtonSelector(CompoundButton view, Bitmap normalBitmap, Bitmap pressedBitmap, Context context){
        view.setButtonDrawable(getCheckStateSelectorDrawable(context, normalBitmap, pressedBitmap));
    }

    public void setCheckButtonSelector(CompoundButton view, String normalPath, String pressedPath){
        view.setButtonDrawable(getCheckStateSelectorDrawable(normalPath, pressedPath));
    }

    private void setBackgroundSelector(View view, Drawable selector){
        if(view != null){
            if(Build.VERSION.SDK_INT >= 16){
                view.setBackground(selector);
            }
            else{
                view.setBackgroundDrawable(selector);
            }
        }
    }

    public Drawable getDrawableByPath(String path){
        return Drawable.createFromPath(path);
    }

    public Drawable getDrawableByBitmap(Context context, Bitmap bitmap){
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public Drawable getDrawableById(Context context, int id){
        return context.getResources().getDrawable(id);
    }

    /**
     * 获取点击的selector
     * @param normalPath
     * @param pressedPath
     * @return
     */
    public Drawable getPressStateSelectorDrawable(String normalPath, String pressedPath){
        Drawable normalDrawable = getDrawableByPath(normalPath);
        Drawable pressedDrawable = getDrawableByPath(pressedPath);
        return getPressStateSelectorDrawable(normalDrawable, pressedDrawable);
    }

    public Drawable getPressStateSelectorDrawable(Context context, Bitmap normalBitmap, Bitmap pressedBitmap){
        Drawable normalDrawable = getDrawableByBitmap(context, normalBitmap);
        Drawable pressedDrawable = getDrawableByBitmap(context, pressedBitmap);
        return getPressStateSelectorDrawable(normalDrawable, pressedDrawable);
    }

    public Drawable getPressStateSelectorDrawable(Context context, int normalId, int pressedId){
        Drawable normalDrawable = getDrawableById(context, normalId);
        Drawable pressedDrawable = getDrawableById(context, pressedId);
        return getPressStateSelectorDrawable(normalDrawable, pressedDrawable);
    }

    public Drawable getPressStateSelectorDrawable(Drawable normalDrawable, Drawable pressedDrawable){
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed},  pressedDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_selected},  pressedDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_focused},  pressedDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);
        return stateListDrawable;
    }

    /**
     * 获取关于check状态的selector
     * @param normalPath
     * @param pressedPath
     * @return
     */
    public Drawable getCheckStateSelectorDrawable(String normalPath, String pressedPath){
        Drawable normalDrawable = getDrawableByPath(normalPath);
        Drawable pressedDrawable = getDrawableByPath(pressedPath);
        return getCheckStateSelectorDrawable(normalDrawable, pressedDrawable);
    }

    public Drawable getCheckStateSelectorDrawable(Context context, Bitmap normalBitmap, Bitmap pressedBitmap){
        Drawable normalDrawable = getDrawableByBitmap(context, normalBitmap);
        Drawable pressedDrawable = getDrawableByBitmap(context, pressedBitmap);
        return getCheckStateSelectorDrawable(normalDrawable, pressedDrawable);
    }

    public Drawable getCheckStateSelectorDrawable(Context context, int normalId, int pressedId){
        Drawable normalDrawable = getDrawableById(context, normalId);
        Drawable pressedDrawable = getDrawableById(context, pressedId);
        return getCheckStateSelectorDrawable(normalDrawable, pressedDrawable);
    }

    public Drawable getCheckStateSelectorDrawable(Drawable normalDrawable, Drawable checkedDrawable){
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{-android.R.attr.state_checked}, normalDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, checkedDrawable);
        return stateListDrawable;
    }

    /**
     * 获取textview 的 textcolor的selector
     * @param normalColor   正常颜色
     * @param pressColor    点击颜色
     * @return
     */
    public ColorStateList getTextColorSelector(int normalColor, int pressColor){
        return getColorStateListSelector(normalColor, pressColor);
    }

    public ColorStateList getColorStateListSelector(int normalColor, int pressColor) {
        int[] colors = new int[] {pressColor, pressColor, pressColor, normalColor};
        int[][] states = new int[4][];
        states[0] = new int[] { android.R.attr.state_pressed };
        states[1] = new int[] { android.R.attr.state_selected };
        states[2] = new int[] { android.R.attr.state_focused };
        states[3] = new int[] {};
        return new ColorStateList(states, colors);
    }

}
