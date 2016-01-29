package com.hengye.share.adapter.viewpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.ui.fragment.TopicFragment;
import com.hengye.share.ui.presenter.TopicPresenter;

import java.util.List;

public class TopicFragmentPager extends FragmentPagerAdapter {

    public TopicFragmentPager(FragmentManager fm, Context context, List<TopicPresenter.TopicGroup> topicGroupGroups) {
        super(fm);
        mContext = context;
        mTopicGroupGroups = topicGroupGroups;
    }

    private Context mContext;
    private List<TopicPresenter.TopicGroup> mTopicGroupGroups;

    @Override
    public CharSequence getPageTitle(int position) {
        TopicPresenter.TopicGroup topicGroup = mTopicGroupGroups.get(position);
        return TopicPresenter.TopicGroup.getName(topicGroup, mContext.getResources());
    }

    @Override
    public Fragment getItem(int position) {
        return TopicFragment.newInstance(mTopicGroupGroups.get(position));
    }

    public void refresh(List<TopicPresenter.TopicGroup> data) {
        if (data == null) {
            this.mTopicGroupGroups.clear();
        } else {
            this.mTopicGroupGroups = data;
        }
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        long itemId;
        try {
            GroupList gl = mTopicGroupGroups.get(position).getGroupList();
            itemId = Long.valueOf(gl.getGid());
        }catch (Exception e){
            e.printStackTrace();
            itemId = position;
        }
        return itemId;
    }

    @Override
    public int getCount() {
        return mTopicGroupGroups.size();
    }
}