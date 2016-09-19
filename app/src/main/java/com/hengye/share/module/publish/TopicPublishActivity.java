package com.hengye.share.module.publish;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hengye.photopicker.model.Photo;
import com.hengye.photopicker.view.PickPhotoView;
import com.hengye.share.module.accountmanage.AccountManageActivity;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.model.AtUser;
import com.hengye.share.model.Parent;
import com.hengye.share.model.greenrobot.TopicDraft;
import com.hengye.share.model.greenrobot.TopicDraftHelper;
import com.hengye.share.service.TopicPublishService;
import com.hengye.share.ui.widget.emoticon.EmoticonPicker;
import com.hengye.share.ui.widget.emoticon.EmoticonPickerUtil;
import com.hengye.share.ui.support.listener.DefaultTextWatcher;
import com.hengye.share.ui.widget.dialog.SimpleTwoBtnDialog;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.EncodeUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

public class TopicPublishActivity extends BaseActivity implements View.OnClickListener {

    public static Intent getStartIntent(Context context, TopicDraft topicDraft) {
        Intent intent = new Intent(context, TopicPublishActivity.class);
        intent.putExtra("topicDraft", topicDraft);
        return intent;
    }

    public static Intent getStartIntent(Context context, String content) {
        Intent intent = new Intent(context, TopicPublishActivity.class);
        intent.putExtra("topicDraft", TopicDraftHelper.getWBTopicDraftByTopicPublish(content));
        return intent;
    }

    public static Intent getAtTaStartIntent(Context context, String name) {
        return getStartIntent(context, "@" + name + " ");
    }

    private TopicDraft mTopicDraft;
    private String mTopicDraftContent, mTopicDraftImages;
    private final static int DEFAULT_TYPE = TopicDraftHelper.PUBLISH_TOPIC;

    @Override
    protected void handleBundleExtra(Intent intent) {
        mTopicDraft = (TopicDraft) intent.getSerializableExtra("topicDraft");
        if (mTopicDraft != null) {
            mTopicDraftContent = mTopicDraft.getContent();
            mTopicDraftImages = mTopicDraft.getUrls();
        } else {
            mTopicDraft = new TopicDraft();
            mTopicDraft.setType(DEFAULT_TYPE);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_topic_publish;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private final int MAX_CHINESE_CONTENT_LENGTH = 140;

    private ImageButton mPhotoPickerBtn, mMentionBtn, mEmoticonBtn, mPublishBtn;
    private PickPhotoView mPhotoPicker;
    private EmoticonPicker mEmoticonPicker;
    private View mContainer;
    private TextView mContentLength;
    private EditText mContent;
    private ScrollView mScrollView;
    private CheckBox mPublishCB;
    private Dialog mSaveToDraftDialog, mSkipToLoginDialog;

    private int mCurrentContentLength;

    @SuppressWarnings("ConstantConditions")
    private void initView() {
        mContainer = findViewById(R.id.rl_container);
        mContent = (EditText) findViewById(R.id.et_topic_publish);
        mContentLength = (TextView) findViewById(R.id.tv_content_length);
        mContent.setSelection(0);
        mContent.setOnClickListener(this);
        mPhotoPickerBtn = (ImageButton) findViewById(R.id.btn_camera);
        mPhotoPickerBtn.setOnClickListener(this);
        mMentionBtn = (ImageButton) findViewById(R.id.btn_mention);
        mMentionBtn.setOnClickListener(this);
        mEmoticonBtn = (ImageButton) findViewById(R.id.btn_emoticon);
        mEmoticonBtn.setOnClickListener(this);
        mPublishBtn = (ImageButton) findViewById(R.id.btn_publish);
        mPublishBtn.setOnClickListener(this);
        mEmoticonPicker = (EmoticonPicker) findViewById(R.id.emoticon_picker);
        mEmoticonPicker.setEditText(this, ((LinearLayout) findViewById(R.id.ll_root)),
                mContent);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mPublishCB = (CheckBox) findViewById(R.id.checkbox_publish);
        mContent.addTextChangedListener(new DefaultTextWatcher() {

            int lastLineCount = mContent.getLineCount();

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int currentLineCount = mContent.getLineCount();
                if (currentLineCount > mContent.getMinLines()) {
                    int differCount = lastLineCount - currentLineCount;
                    if (differCount > 0) {
                        mScrollView.scrollBy(0, -mContent.getLineHeight() * differCount);
                    }
                }
                lastLineCount = currentLineCount;

                updateContentLength();

                boolean findAt = false;
                if (count == 1) {
                    String str = s.toString();
                    String end = str.substring(str.length() - 1);
                    if ("@".equals(end)) {
                        if (start == 0) {
                            findAt = true;
                        } else if (str.length() > 1) {
                            String lastChar = str.substring(str.length() - 2, str.length() - 1);
                            if (" ".equals(lastChar) || "\n".equals(lastChar)) {
                                findAt = true;
                            }
                        }
                    }
                }
                if (findAt) {
                    L.debug("find at action");
                }
            }
        });

        mPhotoPicker = (PickPhotoView) findViewById(R.id.pick_photo);
        mPhotoPicker.setOnDeletePhotoListener(new PickPhotoView.onDeletePhotoListener() {
            @Override
            public void onDeletePhoto(View view, Photo photo) {
                if (mPhotoPicker.getPhotos().isEmpty()) {
                    mPhotoPicker.setVisibility(View.INVISIBLE);
                }
            }
        });
        if (mTopicDraft.getUrls() != null) {
            ArrayList<Photo> photos = new ArrayList<>();
            List<String> urls = CommonUtil.split(mTopicDraft.getUrls(), ",");
            for (String url : urls) {
                Photo photo = new Photo();
                photo.setDataPath(url);
                photos.add(photo);
            }
            mPhotoPicker.setAddPhotos(photos);
        } else {
            mPhotoPicker.setVisibility(View.INVISIBLE);
        }
        mContent.setFilters(new InputFilter[]{mAtUserInputFilter, mEmoticonPicker.getEmoticonInputFilter()});
        initSaveToDraftDialog();

        initViewByType();
    }

    private void initViewByType() {
        switch (mTopicDraft.getType()) {
            case TopicDraftHelper.PUBLISH_COMMENT:
            case TopicDraftHelper.REPLY_COMMENT:
                mPublishCB.setVisibility(View.VISIBLE);
                mPublishCB.setText(R.string.label_publish_comment_and_repost_to_me);
                mPublishCB.setChecked(mTopicDraft.isCommentOrRepostConcurrently());

                mPhotoPickerBtn.setVisibility(View.GONE);
                break;
            case TopicDraftHelper.REPOST_TOPIC:
                mPublishCB.setVisibility(View.VISIBLE);
                mPublishCB.setText(R.string.label_repost_topic_and_commend_to_author);
                mPublishCB.setChecked(mTopicDraft.isCommentOrRepostConcurrently());

                mPhotoPickerBtn.setVisibility(View.GONE);
                break;
            case TopicDraftHelper.PUBLISH_TOPIC:
                mPublishCB.setVisibility(View.GONE);
                mPhotoPickerBtn.setVisibility(View.VISIBLE);
            default:
                break;
        }
    }

    public void initSaveToDraftDialog() {
        SimpleTwoBtnDialog builder = new SimpleTwoBtnDialog();
        if (mTopicDraft != null && mTopicDraft.getId() != null) {
            builder.setContent(getString(R.string.label_save_to_draft_override));
        } else {
            builder.setContent(getString(R.string.label_save_to_draft));
        }
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

    private void initData() {
        changeTitleStyle(mTopicDraft.getType());

        if (mTopicDraft == null || TextUtils.isEmpty(mTopicDraft.getContent())) {
            return;
        }

        if (mTopicDraft.getId() == null && mTopicDraft.getType() == TopicDraftHelper.REPOST_TOPIC) {
            mTopicDraftContent = "//" + mTopicDraft.getContent();
            mContent.setText(mTopicDraftContent);
            mContent.setSelection(0);
        } else {
            mContent.setText(mTopicDraft.getContent());
            mContent.setSelection(mTopicDraft.getContent().length());
        }
        updateContentLength();
    }

    private void changeTitleStyle(int publishType) {
        int titleResId;
        switch (publishType) {
            case TopicDraftHelper.PUBLISH_COMMENT:
                titleResId = R.string.title_publish_comment;
                break;
            case TopicDraftHelper.REPLY_COMMENT:
                titleResId = R.string.title_reply_comment;
                break;
            case TopicDraftHelper.REPOST_TOPIC:
                titleResId = R.string.title_repost_topic;
                break;
            case TopicDraftHelper.PUBLISH_TOPIC:
            default:
                titleResId = R.string.title_publish_topic;
                break;
        }
        updateToolbarTitle(titleResId);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_mention) {
            IntentUtil.startActivityForResult(this, AtUserActivity.class, AtUserActivity.REQUEST_AT_USER);
        } else if (id == R.id.btn_emoticon) {
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
        } else if (id == R.id.btn_camera) {
            mPhotoPicker.performAddPhotoClick();
        }
    }

    @Override
    public void onBackPressed() {
        if (hasChangeContent()) {
            mSaveToDraftDialog.show();
        } else {
            super.onBackPressed();
        }
    }

    private boolean hasChangeContent() {
        String str = mContent.getText().toString();
        if (CommonUtil.isEquals(mTopicDraftContent, str) && CommonUtil.isEquals(mTopicDraftImages, getPhotoPickerUrls())) {
            return false;
        }
        return true;
    }

    private String getPhotoPickerUrls() {
        List<Photo> photos = mPhotoPicker.getPhotos();
        if (photos == null) {
            return null;
        }
        List<String> urls = new ArrayList<>();
        for (Photo photo : photos) {
            urls.add(photo.getDataPath());
        }
        return CommonUtil.toSplit(urls, ",");
    }

    private TopicDraft generateTopicDraft() {
        TopicDraft td = new TopicDraft();
        td.setUid(UserUtil.getUid());
        td.setContent(mContent.getText().toString());
//        td.setDate(DateUtil.getChinaGMTDateFormat());
        td.setDate(hasChangeContent() ? DateUtil.getChinaGMTDate() : mTopicDraft.getDate());
        td.setType(mTopicDraft.getType());
        td.setParentType(Parent.TYPE_WEIBO);
        if (mTopicDraft != null) {
            td.setId(mTopicDraft.getId());
            td.setTargetTopicId(mTopicDraft.getTargetTopicId());
            td.setTargetTopicJson(mTopicDraft.getTargetTopicJson());
            td.setTargetCommentId(mTopicDraft.getTargetCommentId());
            td.setTargetCommentUserName(mTopicDraft.getTargetCommentUserName());
            td.setTargetCommentContent(mTopicDraft.getTargetCommentContent());
        }
        if(mPublishCB.isChecked()){
            td.setIsCommentOrRepostConcurrently(true);
        }
        if (!CommonUtil.isEmpty(mPhotoPicker.getPhotos())) {

            if (CommonUtil.isEmpty(td.getContent())) {
                td.setContent(ResUtil.getString(R.string.label_topic_picture_publish_content_empty));
            }

            td.setUrls(getPhotoPickerUrls());
//            td.setUrls(mPhotoPicker.getPhotos().get(0).getDataPath());
        }
        return td;
    }

    private void publishTopic() {

        if (!checkCanPublishTopic()) {
            return;
        }

        TopicPublishService.publish(this, generateTopicDraft(), UserUtil.getToken());
        finish();
    }

    private void saveToDraft() {
        TopicDraftHelper.saveTopicDraft(generateTopicDraft());
        setResult(Activity.RESULT_OK);
        ToastUtil.showToast(R.string.label_save_to_draft_success);
    }

    private boolean checkCanPublishTopic() {
        boolean result = true;
        if (TextUtils.isEmpty(mContent.getText().toString()) && CommonUtil.isEmpty(mPhotoPicker.getPhotos())) {
            result = false;
            ToastUtil.showToast(R.string.label_topic_publish_field_empty);
        } else if (mCurrentContentLength > MAX_CHINESE_CONTENT_LENGTH) {
            result = false;
            ToastUtil.showToast(R.string.tip_topic_publish_content_length_error);
        } else if (TextUtils.isEmpty(UserUtil.getToken())) {
            result = false;
            showSkipToLoginDialog();
        }

        return result;
    }

    private void showSkipToLoginDialog() {
        if (mSkipToLoginDialog == null) {
            mSkipToLoginDialog = AccountManageActivity.getLoginDialog(this);
        }
        mSkipToLoginDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == AtUserActivity.REQUEST_AT_USER) {
            if (data != null) {
                ArrayList<String> result = (ArrayList<String>) data.getSerializableExtra("atUser");
                EmoticonPickerUtil.addContentToEditTextEnd(mContent, AtUser.getFormatAtUserName(result));
            }
        } else if (mPhotoPicker.handleResult(requestCode, resultCode, data)) {
            mPhotoPicker.setVisibility(View.VISIBLE);
        }
    }

    private InputFilter mAtUserInputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            return DataUtil.convertNormalStringToSpannableString(String.valueOf(source), false);
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

    private void updateContentLength() {
        mCurrentContentLength = EncodeUtil.getChineseLength(mContent.getText().toString());
        int differLength = Math.abs(MAX_CHINESE_CONTENT_LENGTH - mCurrentContentLength);
        if (mCurrentContentLength <= MAX_CHINESE_CONTENT_LENGTH) {
            mContentLength.setTextColor(ResUtil.getColor(R.color.font_grey));
            mContentLength.setText(ResUtil.getString(R.string.label_topic_publish_content_length_less, differLength));
        } else {
            mContentLength.setTextColor(ResUtil.getColor(R.color.font_red_warn));
            mContentLength.setText(ResUtil.getString(R.string.label_topic_publish_content_length_more, differLength));
        }
    }
}
