package com.hengye.share.ui.widget.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hengye.share.R;
import com.hengye.share.util.L;

import java.util.ArrayList;
import java.util.List;

public class StretchLoadingView extends LinearLayout {

    private final int DEFAULT_TARGET_COUNT = 5;

    private float scaleS = 1.0F, scaleT = 1.2F, scaleE = 1.4F;
    private List<ImageView> mTargets = new ArrayList<>();

    private int currentWidth = -1;
    private boolean reset = false;
    private int targetResId;
    private int targetCount;

    public StretchLoadingView(Context context) {
        this(context, null);
    }

    public StretchLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);

        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StretchLoadingView);
        setTargetResId(ta.getResourceId(R.styleable.StretchLoadingView_stretch_target, android.R.color.white));
        setTargetCount(ta.getInt(R.styleable.StretchLoadingView_stretch_targetCount, DEFAULT_TARGET_COUNT));

        String gravity = ta.getString(R.styleable.StretchLoadingView_stretch_gravity);

        int centerH = Gravity.CENTER_HORIZONTAL;

        if("top".equalsIgnoreCase(gravity)){
            setGravity(Gravity.TOP | centerH);
        }else if("bottom".equalsIgnoreCase(gravity)){
            setGravity(Gravity.BOTTOM | centerH);
        }else{
            setGravity(Gravity.CENTER_VERTICAL | centerH);
        }
        ta.recycle();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = getMeasuredWidth();
        if(getMeasuredWidth() != currentWidth){
            reset = true;
            currentWidth = measuredWidth;
            initView(measuredWidth);
            setWillNotDraw(false);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        start();
    }


    public void start(){
        View middleView = getMiddleView();
        if (reset && middleView != null && middleView.getX() != 0.0f) {
            reset = false;
            startAnim();
            setWillNotDraw(true);
        }
    }

    public void setTargetResId(int resId){
        this.targetResId = resId;

        if(!mTargets.isEmpty()){
            for(ImageView iv : mTargets){
                iv.setImageDrawable(getContext().getResources().getDrawable(targetResId));
            }
        }
    }

    public void setTargetCount(int count){
        this.targetCount = getValidTargetCount(count);
    }

    public int getTargetCount(){
        return this.targetCount;
    }

    /**
     * targetCount can not be an even number.
     * @param expectCount
     * @return
     */
    private int getValidTargetCount(int expectCount){
        if(expectCount <= 0){
            return DEFAULT_TARGET_COUNT;
        }else if(expectCount / 2 == 0) {
            return expectCount + 1;
        }
        return expectCount;
    }

    private void initView(int width) {
        removeAllViewsInLayout();

        int imageSize = (int)(width / (targetCount + (targetCount - 1) * 0.8));
//        int margin = width / (targetCount * targetCount);
        int margin = (int)(imageSize * 0.4);

        L.debug("imageSize is {}", imageSize);

        mTargets.clear();
        int middleIndex = targetCount / 2;
        for(int i = 0; i < targetCount; i++){
            ImageView iv = new ImageView(getContext());
            LayoutParams lp = new LayoutParams(imageSize, imageSize);

            if(i < middleIndex){
                lp.setMarginEnd(margin);
            }else if(i > middleIndex){
                lp.setMarginStart(margin);
            }

            iv.setLayoutParams(lp);
            iv.setImageDrawable(getContext().getResources().getDrawable(targetResId));
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            this.addView(iv);
            mTargets.add(iv);
        }

        ViewGroup.LayoutParams lp = getLayoutParams();
        int currentHeight = lp.height;

        if(currentHeight == ViewGroup.LayoutParams.WRAP_CONTENT){
            lp.height = (int)Math.ceil(imageSize * scaleE);

            requestLayout();
            L.debug("change height : {}", lp.height);
        }
    }

    /**
     * @return a view in the middle of views;
     */
    private View getMiddleView(){
        if(!mTargets.isEmpty()) {
            return mTargets.get(mTargets.size() / 2);
        }
        return null;
    }

    private void playAnimatorList(AnimatorSet animatorSet, List<Animator>  animators, long delay){
        AnimatorSet.Builder builder = null;
        for(int i = 0; i < animators.size(); i++){
            Animator animator = animators.get(i);
            if(i == 0){
                builder = animatorSet.play(animator);
            }else{
                builder.with(animator);
            }
        }
        if(builder != null) {
            builder.after(delay);
        }
    }

    private List<Animator> getScaleStartAndTransitionAnimators(boolean isStretch, long duration) {
        return getScaleAnimators(isStretch ? scaleT : scaleS, isStretch ? scaleS : scaleT, duration);
    }

    private List<Animator> getScaleStartAndEndAnimators(boolean isStretch, long duration) {
        return getScaleAnimators(isStretch ? scaleE : scaleS, isStretch ? scaleS : scaleE, duration);
    }

    private List<Animator> getScaleTransitionAndEndAnimators(boolean isStretch, long duration) {
        return getScaleAnimators(isStretch ? scaleE : scaleT, isStretch ? scaleT : scaleE, duration);
    }

    private List<Animator> getScaleAnimators(float fromScale, float toScale, long duration) {
        List<Animator> animators = new ArrayList<>();

        for (View target : mTargets) {
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(target, "scaleX", fromScale, toScale).setDuration(duration);
            ObjectAnimator scaleYAnimator= ObjectAnimator.ofFloat(target, "scaleY", fromScale, toScale).setDuration(duration);
            animators.add(scaleXAnimator);
            animators.add(scaleYAnimator);
        }
        return animators;
    }

    private List<Animator> getDefaultPositionStretchAnimators(boolean isStretch) {
        return getPositionStretchAnimators(isStretch, 200);
    }

    private List<Animator> getPositionStretchAnimators(boolean isStretch, long duration) {
        List<Animator> animators = new ArrayList<>();
        View middleView = getMiddleView();
        float middleX = middleView == null ? 0 : middleView.getX();

        L.debug("middlex : {}", middleX);
        for (View target : mTargets) {
            float fromX = isStretch ? middleX :target.getX();
            float toX = isStretch ? target.getX() : middleX;
            animators.add(ObjectAnimator.ofFloat(target, "x", fromX, toX).setDuration(duration));
        }
        return animators;
    }

    public void startAnim() {
        final AnimatorSet as1 = new AnimatorSet();

        playAnimatorList(as1, getScaleStartAndTransitionAnimators(false, 50), 100);
        playAnimatorList(as1, getDefaultPositionStretchAnimators(false), 100);
        playAnimatorList(as1, getScaleStartAndEndAnimators(false, 200), 250);


        final AnimatorSet as2 = new AnimatorSet();

        playAnimatorList(as2, getScaleTransitionAndEndAnimators(true, 50), 100);
        playAnimatorList(as2, getDefaultPositionStretchAnimators(true), 100);
        playAnimatorList(as2, getScaleStartAndTransitionAnimators(true, 200), 250);

        as1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                as2.start();
            }
        });

        as2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                as1.start();
            }
        });

        as1.start();
    }
}
