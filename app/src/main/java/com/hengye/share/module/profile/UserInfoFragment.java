package com.hengye.share.module.profile;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.module.publish.TopicPublishActivity;
import com.hengye.share.module.util.encapsulation.fragment.BaseFragment;
import com.hengye.share.module.util.encapsulation.view.listener.OnScrollToTopAndBottomListener;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.ToastUtil;

/**
 * Created by yuhy on 16/7/19.
 */
public class UserInfoFragment extends BaseFragment
        implements View.OnClickListener, OnScrollToTopAndBottomListener {

    public static UserInfoFragment newInstance(WBUserInfo wbUserInfo){
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("wbUserInfo", wbUserInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        mWbUserInfo = (WBUserInfo)bundle.getSerializable("wbUserInfo");
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_personalhomepage_about;
    }

    @Override
    public void onToolbarDoubleClick(Toolbar toolbar) {
        onScrollToTop(false);
    }

    @Override
    public void onScrollToTop(boolean isSmooth) {
        if(isSmooth){
            mScrollView.smoothScrollTo(0, 0);
        }else {
            mScrollView.fullScroll(View.FOCUS_UP);
//            mScrollView.scrollTo(0, 0);
        }

    }

    @Override
    public void onScrollToBottom(boolean isSmooth) {
        mScrollView.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRemark = findViewById(R.id.item_remark);
        mGroup = findViewById(R.id.item_group);
        mWBAuth = findViewById(R.id.item_wb_auth);
        mLocation = findViewById(R.id.item_location);
        mSign = findViewById(R.id.item_sign);

        mScrollView = (NestedScrollView) findViewById(R.id.scrollView);

        findViewById(R.id.btn_at_ta).setOnClickListener(this);
        findViewById(R.id.btn_private_message).setOnClickListener(this);
        findViewById(R.id.btn_detail_info).setOnClickListener(this);
        updateUserInfo();
    }

    WBUserInfo mWbUserInfo;
    View mRemark, mGroup, mWBAuth, mLocation, mSign;
    NestedScrollView mScrollView;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_at_ta){
            startActivity(TopicPublishActivity.getAtTaStartIntent(getContext(), mWbUserInfo.getScreen_name()));
        }else{
            ToastUtil.showToBeAchievedToast();
        }
    }

    public void updateUserInfo(WBUserInfo wbUserInfo){
        mWbUserInfo = wbUserInfo;
        updateUserInfo();
    }

    public void updateUserInfo(){
        if(mWbUserInfo == null){
            return;
        }

        mGroup.setVisibility(View.GONE);
        setItemValue(mRemark, "备注", CommonUtil.isEmpty(mWbUserInfo.getRemark()) ? "未设置" : mWbUserInfo.getRemark());
        if(CommonUtil.isEmpty(mWbUserInfo.getVerified_reason())){
            mWBAuth.setVisibility(View.GONE);
        }else{
            setItemValue(mWBAuth, "微博认证", mWbUserInfo.getVerified_reason());
        }

        setItemValue(mLocation, "所在地", mWbUserInfo.getLocation());

        setItemValue(mSign, "签名", mWbUserInfo.getDescription());
    }

    private TextView setItemValue(int itemParentId, String itemKey, String itemValue) {
        return setItemValue(findViewById(itemParentId), itemKey, itemValue);
    }

    private TextView setItemValue(View parent, String itemKey, String itemValue) {
        if (parent != null) {
            TextView key = (TextView) parent.findViewById(R.id.tv_key);
            key.setText(itemKey);
            TextView value = (TextView) parent.findViewById(R.id.tv_value);
            value.setText(itemValue);
            return value;
        }
        return null;
    }
}
