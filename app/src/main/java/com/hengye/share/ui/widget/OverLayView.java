package com.hengye.share.ui.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hengye.share.R;

/**
 * Created by yuhy on 16/8/26.
 */
public class OverLayView extends FrameLayout {

    public OverLayView(Context context) {
        super(context);
        init();
    }

    public OverLayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OverLayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        inflate(getContext(), R.layout.sheetfab_dim_overlay, this);
        setBackgroundColor(getContext().getResources().getColor(R.color.over_lay_background));
    }


    public void show() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 1.0f);
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

        });
        animator.start();
    }

    public void dismiss() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.0f);
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

        });
        animator.start();
    }
}
