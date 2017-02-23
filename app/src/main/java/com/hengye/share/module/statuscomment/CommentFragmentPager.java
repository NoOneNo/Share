package com.hengye.share.module.statuscomment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.module.status.StatusPresenter;

import java.util.List;

public class CommentFragmentPager extends FragmentPagerAdapter {

    public CommentFragmentPager(FragmentManager fm, Context context, List<CommentPresenter.CommentGroup> statusGroupGroups) {
        super(fm);
        mContext = context;
        mStatusGroupGroups = statusGroupGroups;
        mFragmentTags = new SparseArray<>();
        mFragments = new SparseArray<>();
    }

    private Context mContext;
    private List<CommentPresenter.CommentGroup> mStatusGroupGroups;
    private SparseArray<String> mFragmentTags;
    SparseArray<CommentFragment> mFragments;

    @Override
    public CharSequence getPageTitle(int position) {
        CommentPresenter.CommentGroup statusGroup = mStatusGroupGroups.get(position);
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
    public CommentFragment getItem(int position) {
        CommentFragment fragment = mFragments.get(position);
        if (fragment == null) {
            fragment = CommentFragment.newInstance(mStatusGroupGroups.get(position));
            mFragments.put(position, fragment);
        }
        return fragment;
    }

    public void refresh(List<CommentPresenter.CommentGroup> data) {
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
        return position;
    }

    @Override
    public int getCount() {
        return mStatusGroupGroups.size();
    }

    public SparseArray<String> getFragmentTags(){
        return mFragmentTags;
    }

}