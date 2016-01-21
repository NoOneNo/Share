package com.hengye.share.ui.widget.util;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;

public class ShapeLoader {

    private float DENSITY;

    /**
     * 用于代码设置控件
     */
    private ShapeLoader(){
        DENSITY = Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * singleton
     */
    private static ShapeLoader shapeLoader;
    public static ShapeLoader getInstance(){
        if(shapeLoader == null){
            shapeLoader = new ShapeLoader();
        }
        return shapeLoader;
    }


    /**
     * 设置单色
     * @param view
     * @param color
     */
    public void setRectBackground(View view, int color){
        setBackground(view, getRectBackground(color));
    }

    /**
     * 设置带圆角
     * @param view
     * @param color
     * @param radius
     */
    public void setRectConnerBackground(View view, int color, float radius){
        setBackground(view, getRectConnerBackground(color, radius));
    }

    /**
     * 设置矩形带不同圆角
     * @param view
     * @param color     颜色
     * @param radius    1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
     */
    public void setRectConnerBackground(View view, int color, float[] radius){
        setBackground(view, getRectConnerBackground(color, radius));
    }

    /**
     * 设置带描边的
     * @param view
     * @param soildColor    主体颜色
     * @param stokenWidth   描边宽度
     * @param stokenColor   描边颜色
     */
    public void setRectStokenBackground(View view, int soildColor, int stokenWidth, int stokenColor){
        setBackground(view, getRectStokenBackground(soildColor, stokenWidth, stokenColor));
    }

    /**
     * 设置带描边的
     * @param view
     * @param soildColor    主体颜色
     * @param stokenWidth   描边宽度
     * @param stokenColor   描边颜色
     * @param dashWidth     描边虚线宽度
     * @param dashGap       虚线描边间的间隔
     */
    public void setRectStokenBackground(View view, int soildColor, int stokenWidth, int stokenColor, float dashWidth, float dashGap){
        setBackground(view, getGradientDrawable(soildColor, stokenWidth, stokenColor, dashWidth, dashGap));
    }

    /**
     * 设置带圆角描边的
     * @param view
     * @param soildColor    主体颜色
     * @param stokenWidth   描边宽度
     * @param stokenColor   描边颜色
     * @param radius        圆角半径
     */
    public void setRectConnerStokenBackground(View view, int soildColor, int stokenWidth, int stokenColor, float radius){
        setBackground(view, getRectConnerStokenBackground(soildColor, stokenWidth, stokenColor, radius));
    }

    /**
     * 设置带圆角描边的
     * @param view
     * @param soildColor    主体颜色
     * @param stokenWidth   描边宽度
     * @param stokenColor   描边颜色
     * @param radius        圆角半径
     * @param dashWidth     描边虚线宽度
     * @param dashGap       虚线描边间的间隔
     */
    public void setRectConnerStokenBackground(View view, int soildColor, int stokenWidth, int stokenColor, float radius, float dashWidth, float dashGap){
        setBackground(view, getRectConnerStokenBackground(soildColor, stokenWidth, stokenColor, radius, dashWidth, dashGap));
    }

    /**
     * 设置带不同圆角描边的
     * @param view
     * @param soildColor    主体颜色
     * @param stokenWidth   描边宽度
     * @param stokenColor   描边颜色
     * @param radius        1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
     */
    public void setRectConnerStokenBackground(View view, int soildColor, int stokenWidth, int stokenColor, float[] radius){
        setBackground(view, getRectConnerStokenBackground(soildColor, stokenWidth, stokenColor, radius));
    }

    /**
     * 设置带不同圆角描边的
     * @param view
     * @param soildColor    主体颜色
     * @param stokenWidth   描边宽度
     * @param stokenColor   描边颜色
     * @param radius        1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
     * @param dashWidth     描边虚线宽度
     * @param dashGap       虚线描边间的间隔
     */
    public void setRectConnerStokenBackground(View view, int soildColor, int stokenWidth, int stokenColor, float[] radius, float dashWidth, float dashGap){
        setBackground(view, getRectConnerStokenBackground(soildColor, stokenWidth, stokenColor, radius, dashWidth, dashGap));
    }

    /**
     * 设置椭圆单色
     * @param view
     * @param color
     */
    public void setOvalBackground(View view, int color){
        setBackground(view, getOvalBackground(color));
    }

    /**
     * 设置椭圆带描边
     * @param view
     * @param soildColor    主体颜色
     * @param stokenWidth   描边宽度
     * @param stokenColor   描边颜色
     */
    public void setOvalStokenBackground(View view, int soildColor, int stokenWidth, int stokenColor){
        setBackground(view, getOvalStokenBackground(soildColor, stokenWidth, stokenColor));
    }

    /**
     * 设置椭圆带虚线描边
     * @param view
     * @param soildColor    主体颜色
     * @param stokenWidth   描边宽度
     * @param stokenColor   描边颜色
     * @param dashWidth     描边虚线宽度
     * @param dashGap       虚线描边间的间隔
     */
    public void setOvalStokenBackground(View view, int soildColor, int stokenWidth, int stokenColor, float dashWidth, float dashGap){
        setBackground(view, getOvalStokenBackground(soildColor, stokenWidth, stokenColor, dashWidth, dashGap));
    }

    private void setBackground(View view, Drawable background){
        if(view != null){
            if(Build.VERSION.SDK_INT >= 16){
                view.setBackground(background);
            }
            else{
                view.setBackgroundDrawable(background);
            }
        }
    }

    /**
     *
     * 获取单色
     * @param color
     */
    public Drawable getRectBackground(int color){
        return getRectConnerStokenBackground(color, 0, color, 0f, 0f, 0f);
    }

    /**
     *
     * 获取带圆角
     * @param color
     * @param radius
     * @return
     */
    public Drawable getRectConnerBackground(int color, float radius){
        return getRectConnerStokenBackground(color, 0, color, radius, 0f, 0f);
    }

    /**
     * 获取矩形
     * @param color     颜色
     * @param radius    1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
     */
    public Drawable getRectConnerBackground(int color, float[] radius){
        return getRectConnerStokenBackground(color, 0, color, radius, 0f, 0f);
    }

    /**
     *
     * 获取带描边的
     * @param soildColor    主体颜色
     * @param stokenWidth   描边宽度
     * @param stokenColor   描边颜色
     * @return
     */
    public Drawable getRectStokenBackground(int soildColor, int stokenWidth, int stokenColor){
        return getRectConnerStokenBackground(soildColor, stokenWidth, stokenColor, 0f, 0f, 0f);
    }

    /**
     * 获取带描边的
     * @param soildColor    主体颜色
     * @param stokenWidth   描边宽度
     * @param stokenColor   描边颜色
     * @param dashWidth     描边虚线宽度
     * @param dashGap       虚线描边间的间隔
     * @return
     */
    public Drawable getRectStokenBackground(int soildColor, int stokenWidth, int stokenColor, float dashWidth, float dashGap){
        return getGradientDrawable(soildColor, stokenWidth, stokenColor, dashWidth, dashGap);
    }

    /**
     *
     * 获取带描边的
     * @param soildColor    主体颜色
     * @param stokenWidth   描边宽度
     * @param stokenColor   描边颜色
     * @param radius        半径
     * @return
     */
    public Drawable getRectConnerStokenBackground(int soildColor, int stokenWidth, int stokenColor, float radius){
        return getRectConnerStokenBackground(soildColor, stokenWidth, stokenColor, radius, 0f, 0f);
    }

    /**
     *
     * 获取带圆角描边的
     * @param soildColor    主体颜色
     * @param stokenWidth   描边宽度
     * @param stokenColor   描边颜色
     * @param radius        圆角半径
     * @param dashWidth     描边虚线宽度
     * @param dashGap       虚线描边间的间隔
     * @return
     */
    public Drawable getRectConnerStokenBackground(int soildColor, int stokenWidth, int stokenColor, float radius, float dashWidth, float dashGap){
        GradientDrawable gradientDrawable = getGradientDrawable(soildColor, stokenWidth, stokenColor, dashWidth, dashGap);
        gradientDrawable.setCornerRadius(radius);
        return gradientDrawable;
    }

    private GradientDrawable getGradientDrawable(int soildColor, int stokenWidth, int stokenColor, float dashWidth, float dashGap) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(soildColor);
        gradientDrawable.setStroke(stokenWidth, stokenColor, dashWidth, dashGap);
        return gradientDrawable;
    }

    /**
     *
     * 获取带圆角描边的
     * @param soildColor    主体颜色
     * @param stokenWidth   描边宽度
     * @param stokenColor   描边颜色
     * @param radius        圆角半径
     * @return
     */
    public Drawable getRectConnerStokenBackground(int soildColor, int stokenWidth, int stokenColor, float[] radius){
        return getRectConnerStokenBackground(soildColor, stokenWidth, stokenColor, radius, 0f, 0f);
    }

    /**
     *
     * 获取带圆角描边的
     * @param soildColor    主体颜色
     * @param stokenWidth   描边宽度
     * @param stokenColor   描边颜色
     * @param radius        圆角半径
     * @param dashWidth     描边虚线宽度
     * @param dashGap       虚线描边间的间隔
     * @return
     */
    public Drawable getRectConnerStokenBackground(int soildColor, int stokenWidth, int stokenColor, float[] radius, float dashWidth, float dashGap){
        GradientDrawable gradientDrawable = getGradientDrawable(soildColor, stokenWidth, stokenColor, dashWidth, dashGap);
        gradientDrawable.setCornerRadii(radius);
        return gradientDrawable;
    }

    /**
     * 获取椭圆
     * @param color
     * @return
     */
    public Drawable getOvalBackground(int color){
        return getOvalStokenBackground(color, 0, color, 0f, 0f);
    }

    /**
     * 获取椭圆带描边
     * @param soildColor    主体颜色
     * @param stokenWidth   描边宽度
     * @param stokenColor   描边颜色
     * @return
     */
    public Drawable getOvalStokenBackground(int soildColor, int stokenWidth, int stokenColor){
        return getOvalStokenBackground(soildColor, stokenWidth, stokenColor, 0f, 0f);
    }

    /**
     * 设置带圆角描边的
     * @param soildColor    主体颜色
     * @param stokenWidth   描边宽度
     * @param stokenColor   描边颜色
     * @param dashWidth     描边虚线宽度
     * @param dashGap       虚线描边间的间隔
     * @return
     */
    public Drawable getOvalStokenBackground(int soildColor, int stokenWidth, int stokenColor, float dashWidth, float dashGap){
        GradientDrawable gradientDrawable = getGradientDrawable(soildColor, stokenWidth, stokenColor, dashWidth, dashGap);
        gradientDrawable.setShape(GradientDrawable.OVAL);
        return gradientDrawable;
    }

    public float dp2Px(float dp){
        return Math.round(dp * DENSITY);
    }
}
