package com.hengye.share.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hengye.share.R;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.util.UserUtil;

public class SetTokenActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected String getRequestTag() {
        return super.getRequestTag();
    }

    @Override
    protected boolean setCustomTheme() {
        return super.setCustomTheme();
    }

    @Override
    protected boolean setToolBar() {
        return super.setToolBar();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_set_token;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
    }

    private EditText mTokenEdit;
    private Button mUpdateTokenBtn;

    private void initView(){
        mTokenEdit = (EditText) findViewById(R.id.et_token);
        mUpdateTokenBtn = (Button) findViewById(R.id.btn_update_token);

        mUpdateTokenBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_update_token){
            User user = UserUtil.getCurrentUser();
            user.setToken(mTokenEdit.getText().toString().trim());
            UserUtil.updateCurrentUser();
        }
    }
}
























