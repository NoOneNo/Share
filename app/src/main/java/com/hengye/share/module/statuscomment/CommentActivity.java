package com.hengye.share.module.statuscomment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.hengye.share.R;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.util.ResUtil;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends BaseActivity{

    @Override
    protected boolean setToolBar() {
        return false;
    }

    @Override
    protected CharSequence getToolbarTitle() {
        return ResUtil.getString(R.string.title_page_comment);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_status_comment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private CommentFragmentPager mAdapter;

    private void initView(){
        initToolbar();
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mAdapter = new CommentFragmentPager(getSupportFragmentManager(), this, getCommentGroups()));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private List<CommentPresenter.CommentGroup> getCommentGroups(){
        ArrayList<CommentPresenter.CommentGroup> statusGroupGroups = new ArrayList<>();
        statusGroupGroups.add(new CommentPresenter.CommentGroup(CommentPresenter.CommentType.COMMENT_TO_ME));
        statusGroupGroups.add(new CommentPresenter.CommentGroup(CommentPresenter.CommentType.COMMENT_BY_ME));
        return statusGroupGroups;
    }
}
