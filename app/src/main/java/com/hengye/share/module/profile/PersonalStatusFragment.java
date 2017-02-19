package com.hengye.share.module.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.module.status.StatusFragment;
import com.hengye.share.module.status.StatusPresenter;
import com.hengye.share.util.handler.StatusRefreshIdHandler;

/**
 * Created by yuhy on 2016/10/19.
 */

public class PersonalStatusFragment extends StatusFragment {

    public static PersonalStatusFragment newInstance(StatusPresenter.StatusType statusType, String uid, String name) {
        PersonalStatusFragment fragment = new PersonalStatusFragment();
        fragment.setArguments(getBundle(new StatusPresenter.StatusGroup(statusType), uid, name));
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void setDataHandler() {
        setDataHandler(new StatusRefreshIdHandler<>(getAdapter()));
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_content;
    }

    @Override
    public int getLoadingResId(){
        return R.layout.state_loading_top;
    }

    @Override
    public int getEmptyResId(){
        return R.layout.state_empty_top;
    }

    @Override
    public int getNoNetworkResId(){
        return R.layout.state_no_network_top;
    }

    @Override
    public int getServiceErrorResId() {
        return R.layout.state_service_error_top;
    }
}
