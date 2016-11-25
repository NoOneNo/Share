package com.hengye.share.module.publish;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hengye.share.module.accountmanage.AccountManageActivity;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.model.AtUser;
import com.hengye.share.model.Parent;
import com.hengye.share.model.greenrobot.TopicDraft;
import com.hengye.share.model.greenrobot.TopicDraftHelper;
import com.hengye.share.module.topic.TopicPresenter;
import com.hengye.share.service.TopicPublishService;
import com.hengye.share.ui.support.textspan.SimpleContentSpan;
import com.hengye.share.ui.support.textspan.TopicContentUrlSpan;
import com.hengye.share.ui.widget.TopicEditText;
import com.hengye.share.ui.widget.dialog.DateAndTimePickerDialog;
import com.hengye.share.ui.widget.emoticon.EmoticonPicker;
import com.hengye.share.ui.widget.emoticon.EmoticonPickerUtil;
import com.hengye.share.ui.support.listener.DefaultTextWatcher;
import com.hengye.share.ui.widget.dialog.SimpleTwoBtnDialog;
import com.hengye.share.ui.widget.image.GridGalleryEditorView;
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
import java.util.Calendar;
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

    /**
     * 从草稿箱传过来的草稿, 如果没有则生成一条
     */
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
    private GridGalleryEditorView mGalleryEditor;
    private EmoticonPicker mEmoticonPicker;
    private View mContainer;
    private TextView mContentLength, mGroupVisibleStatus;
    private TopicEditText mContent;
    private ScrollView mScrollView;
    private CheckBox mPublishCB;
    private Dialog mSaveToDraftDialog, mSkipToLoginDialog;
    private boolean mIsWithAtChar;
    private int mCurrentContentLength;

    @SuppressWarnings("ConstantConditions")
    private void initView() {
        mContainer = findViewById(R.id.rl_container);
        mContent = (TopicEditText) findViewById(R.id.et_topic_publish);
        mContentLength = (TextView) findViewById(R.id.tv_content_length);
        mContent.setSelection(0);
        mContent.setOnClickListener(this);
        mGroupVisibleStatus = (TextView) findViewById(R.id.tv_group_visible);
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

                //自动调整EditText的位置
                int currentLineCount = mContent.getLineCount();
                if (currentLineCount > mContent.getMinLines()) {
                    int differCount = lastLineCount - currentLineCount;
                    if (differCount > 0) {
                        mScrollView.scrollBy(0, -mContent.getLineHeight() * differCount);
                    }
                }
                lastLineCount = currentLineCount;

                updateContentLength();

                //输入@的时候自动弹出At用户的界面
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
                    startAtUser(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //避免监听死循环，因为要改动text
//                mContent.removeTextChangedListener(this);
//                SpannableString ss = DataUtil.convertNormalStringToSpannableString(null, s, false);
//                mContent.ensureRange(ss);
//                mContent.setTextKeepState(ss);
//                mContent.addTextChangedListener(this);

                List<SimpleContentSpan> simpleContentSpans = DataUtil.convertNormalStringToSimpleContentUrlSpans(s);
                mContent.ensureRange(simpleContentSpans);
                if(simpleContentSpans != null){
                    mContent.ensureRange(simpleContentSpans);
                    for(SimpleContentSpan span : simpleContentSpans){
                        s.setSpan(span, span.getStart(), span.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        });

        mGalleryEditor = (GridGalleryEditorView) findViewById(R.id.gallery_edit);
        if (mTopicDraft.getUrls() != null) {
            mGalleryEditor.setPaths(mTopicDraft.getUrlList());
        }
        mContent.setFilters(new InputFilter[]{ mEmoticonPicker.getEmoticonInputFilter(), mAtUserInputFilter});
        initSaveToDraftDialog();

        initViewByType();
    }

    /**
     * 直接在afterTextChanged里设置span就好
     */
    private InputFilter mAtUserInputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            return DataUtil.convertNormalStringToSpannableString(null, source, false);
        }
    };

    private void initViewByType() {
        switch (mTopicDraft.getType()) {
            case TopicDraftHelper.PUBLISH_COMMENT:
            case TopicDraftHelper.REPLY_COMMENT:
                mPublishCB.setVisibility(View.VISIBLE);
                mPublishCB.setText(R.string.label_publish_comment_and_repost_to_me);
                mPublishCB.setChecked(mTopicDraft.isCommentOrRepostConcurrently());

                mGroupVisibleStatus.setVisibility(View.GONE);
                mPhotoPickerBtn.setVisibility(View.GONE);
                break;
            case TopicDraftHelper.REPOST_TOPIC:
                mPublishCB.setVisibility(View.VISIBLE);
                mPublishCB.setText(R.string.label_repost_topic_and_commend_to_author);
                mPublishCB.setChecked(mTopicDraft.isCommentOrRepostConcurrently());

                mGroupVisibleStatus.setVisibility(View.GONE);
                mPhotoPickerBtn.setVisibility(View.GONE);
                break;
            case TopicDraftHelper.PUBLISH_TOPIC:
                mPublishCB.setVisibility(View.GONE);

                mGroupVisibleStatus.setVisibility(View.VISIBLE);
                updateGroupVisibleStatus();
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

        if (!mTopicDraft.isSaved() && mTopicDraft.getType() == TopicDraftHelper.REPOST_TOPIC) {
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

    /**
     * @param isWithAtChar 是否由@去打开AtUser，是的话在回调的时候把前面的@去掉
     */
    private void startAtUser(boolean isWithAtChar){
        mIsWithAtChar = isWithAtChar;
        IntentUtil.startActivityForResult(this, AtUserActivity.class, AtUserActivity.REQUEST_AT_USER);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_mention) {
            startAtUser(false);
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
            mGalleryEditor.startPhotoPicker();
        }
    }

    @Override
    public void onBackPressed() {
        if (shouldSaveToDraft()) {
            mSaveToDraftDialog.show();
        } else {
            updateDraftIfNeed();
            super.onBackPressed();
        }
    }

    private boolean shouldSaveToDraft(){
        if(UserUtil.isUserEmpty()){
            return false;
        }
        return hasChangeContent();
    }

    private boolean hasChangeContent() {
        String str = mContent.getText().toString();
        if (CommonUtil.isEquals(mTopicDraftContent, str) && CommonUtil.isEquals(mTopicDraftImages, getPhotoPickerUrls())) {
            return false;
        }
        return true;
    }

    private String getPhotoPickerUrls() {
        return CommonUtil.toSplit(mGalleryEditor.getPaths(), TopicDraft.DELIMITER_URL);
    }

    private TopicDraft generateTopicDraft() {
        TopicDraft td = new TopicDraft();
        td.setUid(UserUtil.getUid());
        td.setContent(mContent.getText().toString());
//        td.setDate(DateUtil.getChinaGMTDateFormat());
        td.setDate(hasChangeContent() ? DateUtil.getChinaGMTDate() : mTopicDraft.getDate());
        td.setType(mTopicDraft.getType());
        td.setParentType(Parent.TYPE_WEIBO);

        td.setTargetTopicId(mTopicDraft.getTargetTopicId());
        td.setTargetTopicJson(mTopicDraft.getTargetTopicJson());
        td.setTargetCommentId(mTopicDraft.getTargetCommentId());
        td.setTargetCommentUserName(mTopicDraft.getTargetCommentUserName());
        td.setTargetCommentContent(mTopicDraft.getTargetCommentContent());
        td.setAssignGroupIdStr(mTopicDraft.getAssignGroupIdStr());
        td.setPublishTiming(mTopicDraft.getPublishTiming());


        if (mPublishCB.isChecked()) {
            td.setIsCommentOrRepostConcurrently(true);
        }
        if (!CommonUtil.isEmpty(mGalleryEditor.getPaths())) {
            if (CommonUtil.isEmpty(td.getContent())) {
                td.setContent(ResUtil.getString(R.string.label_topic_picture_publish_content_empty));
            }
            td.setUrls(getPhotoPickerUrls());
        }

        if (mTopicDraft.isSaved()) {
            //草稿已存在,保存ID
            td.setId(mTopicDraft.getId());
        }

        //插入或更新一条草稿, 标记为发送中
        TopicDraftHelper.saveTopicDraft(td, TopicDraft.NORMAL);
        return td;
    }

    private void publishTopic() {

        if (!checkCanPublishTopic()) {
            return;
        }

        TopicPublishService.publish(this, generateTopicDraft(), UserUtil.getToken());

        if (mTopicDraft.isSaved()) {
            //让草稿箱知道有草稿被发送了, 刷新界面
            setResult(Activity.RESULT_OK);
        }
        finish();
    }

    private void saveToDraft() {
        generateTopicDraft();
        setResult(Activity.RESULT_OK);
        ToastUtil.showToast(R.string.label_save_to_draft_success);
    }

    private void updateDraftIfNeed(){
        if(mTopicDraft.isSaved()) {
            TopicDraftHelper.updateTopicDraft(mTopicDraft);
            setResult(Activity.RESULT_OK);
        }
    }

    private boolean checkCanPublishTopic() {
        boolean result = true;
        if (TextUtils.isEmpty(mContent.getText().toString()) && CommonUtil.isEmpty(mGalleryEditor.getPaths())) {
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
                String userNames = AtUser.getFormatAtUserName(result);
                if(mContent.getText().toString().endsWith("@") && !CommonUtil.isEmpty(userNames)) {
                    Editable editable = mContent.getText();
                    editable.replace(editable.length() - 1, editable.length(), "");
                }
                EmoticonPickerUtil.addContentToEditTextEnd(mContent, userNames);
            }
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
        ((LinearLayout.LayoutParams) mContainer.getLayoutParams()).weight = 1.0F;
    }

    /**
     * 更新还可以输入多少字数
     */
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

    private void updateGroupVisibleStatus() {
        mGroupVisibleStatus.setText(mTopicDraft.getGroupName());
    }

    MenuItem mAllGroupsVisible, mAssignGroupVisible, mPublishTiming, mCancelPublishTiming;
    List<TopicPresenter.TopicGroup> mTopicGroups;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (mTopicDraft.getType() != TopicDraftHelper.PUBLISH_TOPIC) {
            return false;
        }

        getMenuInflater().inflate(R.menu.activity_publish, menu);

        mAllGroupsVisible = menu.findItem(R.id.all_groups_visible);
        mAssignGroupVisible = menu.findItem(R.id.assign_group_visible);
        mPublishTiming = menu.findItem(R.id.publish_timing);
        mCancelPublishTiming = menu.findItem(R.id.publish_timing_cancel);

        mTopicGroups = TopicPresenter.TopicGroup.getTopicGroups();

        if (!CommonUtil.isEmpty(mTopicGroups)) {
            SubMenu subMenu = mAssignGroupVisible.getSubMenu();
//            SubMenu subMenu = menu.addSubMenu(R.id.topic_publish, 1, mAssignGroupVisible.getOrder(), ResUtil.getString(R.string.label_assign_group_visible));
            if (subMenu != null) {
                for (int i = 0; i < mTopicGroups.size(); i++) {
                    TopicPresenter.TopicGroup tg = mTopicGroups.get(i);
                    subMenu.add(1, i, i, tg.getName());
                }
            }
        } else {
            mAllGroupsVisible.setVisible(false);
            mAssignGroupVisible.setVisible(false);
        }

        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.all_groups_visible) {
            //所有人可见
            mTopicDraft.setAssignGroupVisible(false, null);
            updateGroupVisibleStatus();
        } else if (id == R.id.publish_timing) {
            //定时发布
            getPublishTimingDialog().show();
        } else if (id == R.id.publish_timing_cancel) {
            //取消定时发布
            mTopicDraft.cancelTiming();
        } else {
            //指定分组可见
            int groupIndex = item.getItemId();
            if (mTopicGroups != null && groupIndex < mTopicGroups.size()) {
                TopicPresenter.TopicGroup tp = mTopicGroups.get(groupIndex);

                mTopicDraft.setAssignGroupIdStr(tp.getGroupList().getGid());
                updateGroupVisibleStatus();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    DateAndTimePickerDialog mPublishTimingDialog;

    private DateAndTimePickerDialog getPublishTimingDialog(){
        if(mPublishTimingDialog == null){
            long timeInMillis = mTopicDraft.isPublishTiming() ? mTopicDraft.getPublishTiming() : System.currentTimeMillis();
            mPublishTimingDialog = new DateAndTimePickerDialog(this, timeInMillis);
            mPublishTimingDialog.setOnSetListener(new DateAndTimePickerDialog.OnSetListener() {
                @Override
                public void onSet(Calendar calendar, long timeInMillis) {
                    mTopicDraft.setPublishTiming(timeInMillis);
                }
            });
            mPublishTimingDialog.setOnTimeUpdateListener(new DateAndTimePickerDialog.onTimeUpdateListener() {
                @Override
                public boolean onTimeUpdate(boolean isSelectDate, long timeInMillis) {
                    long duration = timeInMillis - System.currentTimeMillis();
                    if(duration < 0){
                        ToastUtil.showToast(R.string.tip_publish_on_time_options_invalid);
                        return false;
                    }

                    return true;
                }
            });
        }
        return mPublishTimingDialog;
    }

}
