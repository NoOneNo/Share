package com.hengye.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hengye.share.util.SPUtil;
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
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        replaceCustomThemeIfNeeded();
        setToolBarIfNeeded();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        cancelPendingRequestsIfNeeded();
        super.onDestroy();
    }

    protected void cancelPendingRequestsIfNeeded(){
        if(getRequestTag() != null){
            RequestManager.cancelPendingRequests(getRequestTag());
        }
    }

    protected void setCustomThemeIfNeeded(Bundle savedInstanceState){
        if(setCustomTheme()){
            if (savedInstanceState == null) {
                mThemeResId = SPUtil.getAppThemeResId();
            } else {
                mThemeResId = savedInstanceState.getInt("theme");
            }
            setTheme(mThemeResId);
        }

    }

    protected void replaceCustomThemeIfNeeded(){
        if (setCustomTheme() && mThemeResId != SPUtil.getAppThemeResId()) {
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

    protected void setToolBarIfNeeded(){
        if(setToolBar()) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                toolbar.setTitle(getTitle());
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
    }
}
