package com.hengye.share.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.ui.fragment.encapsulation.TabLayoutFragment;
import com.hengye.share.ui.presenter.TopicPresenter;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.ResUtil;

import java.util.ArrayList;

/**
 * Created by yuhy on 16/7/18.
 */
public class PersonalHomepageFragment extends TabLayoutFragment{

    public static PersonalHomepageFragment newInstance(WBUserInfo wbUserInfo){
        PersonalHomepageFragment fragment = new PersonalHomepageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("wbUserInfo", wbUserInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void handleBundleExtra() {
        super.handleBundleExtra();
        mWbUserInfo = (WBUserInfo)getArguments().getSerializable("wbUserInfo");
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tabs;
    }

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    WBUserInfo mWbUserInfo;
    @Override
    protected ArrayList<TabItem> generateTabs() {
        ArrayList<TabItem> tabItems = new ArrayList<>();
        TabItem tabItem1 = new TabItem(1, ResUtil.getString(R.string.label_tab_about));
        TabItem tabItem2 = new TabItem(2, ResUtil.getString(R.string.label_tab_topic, DataUtil.getCounter(mWbUserInfo.getStatuses_count())));
        TabItem tabItem3 = new TabItem(3, ResUtil.getString(R.string.label_tab_album));

        tabItems.add(tabItem1);
        tabItems.add(tabItem2);
        tabItems.add(tabItem3);

        return tabItems;
    }

    TopicFragment mTopicFragment;
    TopicFragment.LoadDataCallBack mCallBack;
    @Override
    protected Fragment newFragment(TabItem tabItem) {
        Fragment fragment;
        switch (tabItem.getType()){
            case 1:
                fragment = PersonalHomepageAboutFragment.newInstance(mWbUserInfo);
                break;
            case 2:
                fragment = getTopicFragment();
                break;
            case 3:
            default:
                fragment = new TestContentFragment();
                break;

        }
        return fragment;
    }

    public TopicFragment getTopicFragment(){
        if(mTopicFragment == null) {
            mTopicFragment = TopicFragment.newInstance(TopicPresenter.TopicType.HOMEPAGE, mWbUserInfo.getIdstr(), mWbUserInfo.getName());
            mTopicFragment.setLoadDataCallBack(getCallBack());
        }
        return mTopicFragment;
    }

    public TopicFragment.LoadDataCallBack getCallBack() {
        return mCallBack;
    }

    public void setCallBack(TopicFragment.LoadDataCallBack callBack) {
        this.mCallBack = callBack;
    }
}
