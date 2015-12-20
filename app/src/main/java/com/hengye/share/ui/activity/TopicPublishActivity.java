package com.hengye.share.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hengye.share.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.ui.emoticon.EmoticonPicker;
import com.hengye.share.ui.emoticon.EmoticonPickerUtil;

public class TopicPublishActivity extends BaseActivity implements View.OnClickListener{

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_topic_publish);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private ImageButton mEmoticonBtn;
    private EmoticonPicker mEmoticonPicker;
    private RelativeLayout mContainer;
    private EditText mContent;

    private void initView(){
        mContainer = (RelativeLayout) findViewById(R.id.rl_container);
        mContent = (EditText) findViewById(R.id.et_topic_publish);
        mContent.setOnClickListener(this);
        mEmoticonBtn = (ImageButton) findViewById(R.id.btn_emoticon);
        mEmoticonBtn.setOnClickListener(this);
        mEmoticonPicker = (EmoticonPicker) findViewById(R.id.emoticon_picker);
        mEmoticonPicker.setEditText(this, ((LinearLayout) findViewById(R.id.ll_root)),
                mContent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_emoticon){
            if (mEmoticonPicker.isShown()) {
                hideEmoticonPicker(true);
            } else {
                showEmoticonPicker(
                        EmoticonPickerUtil.isKeyBoardShow(this));
            }
        }else if(id == R.id.et_topic_publish){
            hideEmoticonPicker(true);
        }
    }

    private void showEmoticonPicker(boolean showAnimation) {
        mEmoticonPicker.show(showAnimation);
        lockContainerHeight(EmoticonPickerUtil.getAppContentHeight(this));
    }

    public void hideEmoticonPicker(boolean showKeyBoard) {
        if (mEmoticonPicker.isShown()) {
            if (showKeyBoard) {
                //this time softkeyboard is hidden
                lockContainerHeight(mEmoticonPicker.getTop());
                mEmoticonPicker.hide();
                EmoticonPickerUtil.showKeyBoard(mContent);
                mContent.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        unlockContainerHeight();
                    }
                }, 200L);
            } else {
                mEmoticonPicker.hide();
                unlockContainerHeight();
            }
        }
    }

    private void lockContainerHeight(int height) {
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) mContainer
                .getLayoutParams();
        localLayoutParams.height = height;
        localLayoutParams.weight = 0.0F;
    }

    public void unlockContainerHeight() {
        ((LinearLayout.LayoutParams) mContainer.getLayoutParams()).weight
                = 1.0F;
    }
}
