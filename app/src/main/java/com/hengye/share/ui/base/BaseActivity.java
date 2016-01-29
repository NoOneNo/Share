package com.hengye.share.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hengye.share.R;
import com.hengye.share.ui.presenter.BasePresenter;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.SettingHelper;

public class BaseActivity extends AppCompatActivity{

    protected String getRequestTag() {
        return "BaseActivity";
    }

    /**
     * @return 如果为true, 则使用APP自定义的主题
     */
    protected boolean setCustomTheme() {
        return true;
    }

    protected boolean setToolBar() {
        return true;
    }

    protected boolean canSwipeBack() {
        return true;
    }

    protected boolean setFinishPendingTransition() {
        return true;
    }

    private int mThemeResId = 0;

    private SwipeBackHelper mSwipeHelper;

    private BasePresenter mPresenter;

    protected boolean mFirstClick = true;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme", mThemeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomThemeIfNeeded(savedInstanceState);
        super.onCreate(savedInstanceState);
        handleBundleExtra();
        setupActivityHelper();

        if (mSwipeHelper != null) {
            mSwipeHelper.onCreate();
        }

        afterCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mSwipeHelper != null) {
            mSwipeHelper.onPostCreate();
        }
    }

    protected void afterCreate(Bundle savedInstanceState) {
        if(getLayoutResId() != 0){
            setContentView(getLayoutResId());
        }
    }

    protected @LayoutRes int getLayoutResId(){
        return 0;
    }

    @Override
    public void setContentView(int layoutId) {
        setToolBarIfNeeded(View.inflate(this, layoutId, null));
    }

    @Override
    public void setContentView(View view) {
        setToolBarIfNeeded(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetFirstClick();
        replaceCustomThemeIfNeeded();
        if (mSwipeHelper != null) {
            mSwipeHelper.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelPendingRequestsIfNeeded();
        if(mPresenter != null){
            mPresenter.detachView();
        }
    }

    public void startActivity(Class<?> cls){
        startActivity(new Intent(this, cls));
    }

    public void startActivityForResult(Class<?> cls, int requestCode){
        startActivityForResult(new Intent(this, cls), requestCode);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (mFirstClick) {
            mFirstClick = false;
            super.startActivityForResult(intent, requestCode, options);
            overridePendingTransitionOnStart();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
        if (setFinishPendingTransition()) {
            overridePendingTransitionOnFinish();
        }
    }

    protected void overridePendingTransitionOnStart(){
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    protected void overridePendingTransitionOnFinish(){
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    protected void handleBundleExtra() {

    }

    protected void cancelPendingRequestsIfNeeded() {
        if (getRequestTag() != null) {
            RequestManager.cancelPendingRequests(getRequestTag());
        }
    }

    protected void setCustomThemeIfNeeded(Bundle savedInstanceState) {
        if (setCustomTheme()) {
            if (savedInstanceState == null) {
                mThemeResId = SettingHelper.getAppThemeResId();
            } else {
                mThemeResId = savedInstanceState.getInt("theme");
            }
            setTheme(mThemeResId);
        }

    }

    protected void replaceCustomThemeIfNeeded() {
        if (setCustomTheme() && mThemeResId != SettingHelper.getAppThemeResId()) {
            reStartActivity();
        }
    }

    protected void reStartActivity() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    protected void setToolBarIfNeeded(View view) {
        if (!setToolBar()) {
            super.setContentView(view);
        } else {
            super.setContentView(R.layout.activity_base);
            LinearLayout rootLayout = (LinearLayout) findViewById(R.id.layout_root);
            if (rootLayout == null) return;
            rootLayout.addView(view,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            initToolbar();
        }
    }

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setTitle(getToolbarTitle());
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    private Toolbar mToolbar;

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void updateToolbarTitle(String title) {
        mToolbar.setTitle(title);
    }

    public void updateToolbarTitle(@StringRes int resId) {
        mToolbar.setTitle(getString(resId));
    }

    protected CharSequence getToolbarTitle() {
        return getTitle();
    }

    protected void setupActivityHelper() {
        if (mSwipeHelper == null) {
            if(canSwipeBack() && SettingHelper.isSwipeBack()){
                mSwipeHelper = new SwipeBackHelper(this);
            }
        }
    }

    public void resetFirstClick() {
        mFirstClick = true;
    }

    public void setupPresenter(BasePresenter presenter) {
        mPresenter = presenter;
    }

}
