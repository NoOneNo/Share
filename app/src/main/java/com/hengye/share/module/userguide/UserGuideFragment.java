package com.hengye.share.module.userguide;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.base.DefaultDataHandler;
import com.hengye.share.module.util.encapsulation.fragment.RecyclerFragment;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.ui.widget.recyclerview.DividerItemDecoration;
import com.hengye.share.util.ResUtil;

public class UserGuideFragment extends RecyclerFragment<UserGuide>
        implements UserGuideContract.View{

    private UserGuideAdapter mAdapter;
    private UserGuideContract.Presenter mPresenter;

    @Override
    public String getTitle() {
        return ResUtil.getString(R.string.title_activity_user_guide);
    }

    @Override
    protected boolean isShowScrollbars() {
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAdapter(mAdapter = new UserGuideAdapter(getContext()));
        setDataHandler(new DefaultDataHandler<>(mAdapter));
        mPresenter = new UserGuidePresenter(this);
        mPresenter.listUserGuide();

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                UserGuideAdapter.UserGuideHolder userGuideHolder = (UserGuideAdapter.UserGuideHolder)mAdapter.findViewHolderForLayoutPosition(position);
                userGuideHolder.expandIcon.switchState();
                UserGuide userGuide = mAdapter.getItem(position);
                userGuide.setExpand(!userGuide.isExpand());
                mAdapter.updateItem(position);
            }
        });
    }



}
