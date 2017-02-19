package com.hengye.share.module.status;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.hengye.share.model.greenrobot.GroupList;

import java.util.List;

public class StatusFragmentPager extends FragmentPagerAdapter {

    public StatusFragmentPager(FragmentManager fm, Context context, List<StatusPresenter.StatusGroup> statusGroupGroups) {
        super(fm);
        mContext = context;
        mStatusGroupGroups = statusGroupGroups;
        mFragmentTags = new SparseArray<>();
        mFragments = new SparseArray<>();
    }

    private Context mContext;
    private List<StatusPresenter.StatusGroup> mStatusGroupGroups;
    private SparseArray<String> mFragmentTags;
    SparseArray<StatusFragment> mFragments;

    @Override
    public CharSequence getPageTitle(int position) {
        StatusPresenter.StatusGroup statusGroup = mStatusGroupGroups.get(position);
        return statusGroup.getName();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        Fragment fragment = (Fragment) obj;
        if(fragment != null){
            mFragmentTags.put(position, fragment.getTag());
        }
        return obj;
    }

    @Override
    public StatusFragment getItem(int position) {
        StatusFragment fragment = mFragments.get(position);
        if (fragment == null) {
            fragment = StatusFragment.newInstance(mStatusGroupGroups.get(position));
            mFragments.put(position, fragment);
        }
        return fragment;
    }

    public void refresh(List<StatusPresenter.StatusGroup> data) {
        if (data == null) {
            this.mStatusGroupGroups.clear();
        } else {
            this.mStatusGroupGroups = data;
        }
        mFragments.clear();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        long itemId;
        try {
            GroupList gl = mStatusGroupGroups.get(position).getGroupList();
            itemId = gl == null ? position : Long.valueOf(gl.getGid());
        }catch (Exception e){
            e.printStackTrace();
            itemId = position;
        }
        return itemId;
    }

    @Override
    public int getCount() {
        return mStatusGroupGroups.size();
    }

    public SparseArray<String> getFragmentTags(){
        return mFragmentTags;
    }

}