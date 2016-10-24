/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengye.share.ui.widget.fab;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.hengye.share.R;
import com.hengye.share.ui.widget.util.DrawableLoader;
import com.hengye.share.util.ViewUtil;


/**
 * A {@link FloatingActionButton} that implements {@link Checkable} to allow display of different
 * icons in it's states.
 */
@CoordinatorLayout.DefaultBehavior(CheckableFab.Behavior.class)
public class CheckableFab extends FloatingActionButton implements Checkable {

    private static final int[] CHECKED = {android.R.attr.state_checked};

    private boolean mIsChecked = true;


    public CheckableFab(Context context) {
        this(context, null);
    }

    public CheckableFab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableFab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(isInEditMode()){
            return;
        }
        setImageDrawable(DrawableLoader.setTintDrawable(R.drawable.btn_fab_follow, R.color.theme_pink_primary_dark));
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(++extraSpace);
        if (mIsChecked) {
            mergeDrawableStates(drawableState, CHECKED);
        }
        return drawableState;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mIsChecked == checked) {
            return;
        }
        mIsChecked = checked;
        refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mIsChecked);
    }


    public static class Behavior extends FloatingActionButton.Behavior {

        private final int MIN_HEIGHT = ViewUtil.dp2px(R.dimen.header_personal_avatar_margin_top);

        private AppBarLayout mAppBarLayout;
        private int mMinimumHeight;

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child,
                                              View dependency) {
            final CoordinatorLayout.LayoutParams lp =
                    (CoordinatorLayout.LayoutParams) child.getLayoutParams();

            if (lp.getAnchorId() == dependency.getId()) {
                updateFab(parent, child);
                return false;
            } else {
                return super.onDependentViewChanged(parent, child, dependency);
            }
        }

        private void updateFab(CoordinatorLayout parent, FloatingActionButton child) {
            if(mAppBarLayout == null){
                mAppBarLayout = (AppBarLayout) parent.findViewById(R.id.appbar);
            }
            if(mAppBarLayout == null){
                return;
            }

            int top = mAppBarLayout.getTop();
            if(top > 0){
                return;
            }
            if(-top  > 2 * mAppBarLayout.getMinimumHeight()){
                child.hide();
            }else{
                child.show();
            }
        }
    }
}
