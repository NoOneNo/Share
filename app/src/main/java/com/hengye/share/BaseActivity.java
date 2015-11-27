package com.hengye.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hengye.share.util.SPUtil;
import com.hengye.share.util.SettingHelper;
import com.hengye.volleyplus.toolbox.RequestManager;

public class BaseActivity extends AppCompatActivity {

    protected String getRequestTag(){
        return null;
    }

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
        getBundleExtra();
        super.onCreate(savedInstanceState);
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

    protected void getBundleExtra(){

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(getToolbarTitle());
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    public void updateToolbarTitle(String title){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected CharSequence getToolbarTitle() {
        return getTitle();
    }
}
