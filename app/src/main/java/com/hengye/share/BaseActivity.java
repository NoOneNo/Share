package com.hengye.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hengye.share.util.RequestManager;
import com.hengye.share.util.SettingHelper;

public class BaseActivity extends AppCompatActivity {

    protected String getRequestTag(){
        return "BaseActivity";
    }

    /**
     * @return 如果为true, 则使用APP自定义的主题
     */
    protected boolean setCustomTheme(){
        return true;
    }

    protected boolean setToolBar(){
        return true;
    }
    private int mThemeResId = 0;

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
        replaceCustomThemeIfNeeded();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        cancelPendingRequestsIfNeeded();
        super.onDestroy();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options){
        super.startActivityForResult(intent, requestCode, options);
    }

    @Override
    public void finish(){
        super.finish();
    }

    protected void handleBundleExtra(){

    }

    protected void cancelPendingRequestsIfNeeded(){
        if(getRequestTag() != null){
            RequestManager.cancelPendingRequests(getRequestTag());
        }
    }

    protected void setCustomThemeIfNeeded(Bundle savedInstanceState){
        if(setCustomTheme()){
            if (savedInstanceState == null) {
                mThemeResId = SettingHelper.getAppThemeResId();
            } else {
                mThemeResId = savedInstanceState.getInt("theme");
            }
            setTheme(mThemeResId);
        }

    }

    protected void replaceCustomThemeIfNeeded(){
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

    protected void setToolBarIfNeeded(View view){
        if(!setToolBar()) {
            super.setContentView(view);
        }else{
            super.setContentView(R.layout.activity_base);
            LinearLayout rootLayout = (LinearLayout) findViewById(R.id.layout_root);
            if (rootLayout == null) return;
            rootLayout.addView(view,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            initToolbar();
        }
    }

    protected void initToolbar(){
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

    public Toolbar getToolbar(){
        return mToolbar;
    }

    public void updateToolbarTitle(String title){
        mToolbar.setTitle(title);
    }

    public void updateToolbarTitle(@StringRes int resId){
        mToolbar.setTitle(getString(resId));
    }

    protected CharSequence getToolbarTitle() {
        return getTitle();
    }
}
