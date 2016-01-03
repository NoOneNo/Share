package com.hengye.share.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.util.Linkify;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.hengye.share.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.module.AtUser;
import com.hengye.share.module.Topic;
import com.hengye.share.module.UserInfo;
import com.hengye.share.service.TopicPublishService;
import com.hengye.share.ui.emoticon.EmoticonPicker;
import com.hengye.share.ui.emoticon.EmoticonPickerUtil;
import com.hengye.share.ui.support.LongClickableLinkMovementMethod;
import com.hengye.share.ui.widget.dialog.SimpleTwoBtnDialog;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.ToastUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TopicPublishActivity extends BaseActivity implements View.OnClickListener {

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

    private ImageButton mMentionBtn, mEmoticonBtn, mPublishBtn;
    private EmoticonPicker mEmoticonPicker;
    private RelativeLayout mContainer;
    private EditText mContent;
    private Dialog mSaveToDraftDialog;

    private void initView() {
        mContainer = (RelativeLayout) findViewById(R.id.rl_container);
        mContent = (EditText) findViewById(R.id.et_topic_publish);
        mContent.setMovementMethod(LongClickableLinkMovementMethod.getInstance());
        mContent.setOnClickListener(this);
        mMentionBtn = (ImageButton) findViewById(R.id.btn_mention);
        mMentionBtn.setOnClickListener(this);
        mEmoticonBtn = (ImageButton) findViewById(R.id.btn_emoticon);
        mEmoticonBtn.setOnClickListener(this);
        mPublishBtn = (ImageButton) findViewById(R.id.btn_publish);
        mPublishBtn.setOnClickListener(this);
        mEmoticonPicker = (EmoticonPicker) findViewById(R.id.emoticon_picker);
        mEmoticonPicker.setEditText(this, ((LinearLayout) findViewById(R.id.ll_root)),
                mContent);
        mContent.setFilters(new InputFilter[]{mAtUserInputFilter, mEmoticonPicker.getEmoticonInputFilter()});
        initSaveToDraftDialog();
    }

    public void initSaveToDraftDialog() {
        SimpleTwoBtnDialog builder = new SimpleTwoBtnDialog();
        builder.setContent(getString(R.string.label_save_to_draft));
        builder.setNegativeButtonClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setPositiveButtonClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveToDraft();
                dialog.dismiss();
                finish();
            }
        });
        mSaveToDraftDialog = builder.create(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_mention){
            IntentUtil.startActivityForResult(this, AtUserActivity.class, AtUserActivity.REQUEST_AT_USER);
        }else if (id == R.id.btn_emoticon) {
            if (mEmoticonPicker.isShown()) {
                hideEmoticonPicker(true);
            } else {
                showEmoticonPicker(
                        EmoticonPickerUtil.isKeyBoardShow(this));
            }
        } else if (id == R.id.et_topic_publish) {
            hideEmoticonPicker(true);
        } else if (id == R.id.btn_publish) {
            publishTopic();
        }
    }

    @Override
    public void onBackPressed() {
        mSaveToDraftDialog.show();
    }

    private void publishTopic() {
        String content = mContent.getText().toString();
        Topic topic = new Topic();
        topic.setContent(content);
        TopicPublishService.publish(this, topic, SPUtil.getSinaToken());
        finish();
    }

    private void saveToDraft() {
        ArrayList<Topic> temp = SPUtil.getModule(new TypeToken<ArrayList<Topic>>() {
        }.getType(), TopicDraftActivity.class.getSimpleName() + SPUtil.getSinaUid());

        if(CommonUtil.isEmptyCollection(temp)){
            temp = new ArrayList<>();
        }

        String content = mContent.getText().toString();
        Topic topic = new Topic();
        topic.setContent(content);

        temp.add(topic);
        SPUtil.setModule(temp, TopicDraftActivity.class.getSimpleName() + SPUtil.getSinaUid());

        ToastUtil.showToast(R.string.label_save_to_draft_success);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == AtUserActivity.REQUEST_AT_USER) {
            if (data != null) {
                ArrayList<String> result = (ArrayList<String>) data.getSerializableExtra("atUser");
                if(!CommonUtil.isEmptyCollection(result)){
                    Editable editAble = mContent.getEditableText();
                    int start = mContent.getSelectionStart();
                    if(start == -1){
                        start = 0;
                    }
                    editAble.insert(start, AtUser.getFormatAtUserName(result));
                }
            }
        }
    }

    private InputFilter mAtUserInputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            return DataUtil.convertNormalStringToSpannableString(TopicPublishActivity.this, String.valueOf(source));
        }
    };

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
        ((LinearLayout.LayoutParams) mContainer.getLayoutParams()).weight = 1.0F;
    }
}
