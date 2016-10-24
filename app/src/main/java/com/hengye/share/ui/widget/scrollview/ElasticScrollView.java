package com.hengye.share.ui.widget.scrollview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by yuhy on 16/7/29.
 */
public class ElasticScrollView extends ScrollView {

    public ElasticScrollView(Context context) {
        this(context, null);
    }

    public ElasticScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ElasticScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // 这个值控制可以把ScrollView包裹的控件拉出偏离顶部或底部的距离。
    private static final int MAX_OVERSCROLL_Y = 200;

    private Context mContext;
    private int newMaxOverScrollY;

    /*
     * public ZhangPhilListView(Context context, AttributeSet attrs, int
     * defStyle) { super(context, attrs, defStyle); this.mContext = context;
     * init(); }
     */

    @SuppressLint("NewApi")
    private void init(Context context) {

        this.mContext = context;

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        float density = metrics.density;
        newMaxOverScrollY = (int) (density * MAX_OVERSCROLL_Y);

        //false:隐藏ScrollView的滚动条。
        this.setVerticalScrollBarEnabled(false);

        //不管装载的控件填充的数据是否满屏，都允许橡皮筋一样的弹性回弹。
        this.setOverScrollMode(ScrollView.OVER_SCROLL_ALWAYS);
    }

    private int delY, preY;
    private boolean action_up;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action_up = false;
                preY = (int)ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int currentY = (int) ev.getY();
                delY = (preY - currentY);
                preY = currentY;
                break;
            case MotionEvent.ACTION_UP:
                action_up = true;
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        this.scrollBy(0, delY / 2);
//        if (action_up) {
//            this.scrollTo(0, 0);
//        }
    }

    // 最关键的地方。
    //支持到SDK8需要增加@SuppressLint("NewApi")。
    @SuppressLint("NewApi")
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, maxOverScrollX, newMaxOverScrollY,
                isTouchEvent);
    }
}
