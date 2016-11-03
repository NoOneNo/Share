package com.hengye.share.module.test;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.fragment.BaseFragment;
import com.hengye.share.module.util.encapsulation.base.StateLayoutManager;

/**
 * Created by yuhy on 16/7/18.
 */
public class TestStateFragment extends BaseFragment implements View.OnClickListener{

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_test_state;
    }


    StateLayoutManager mStateLayoutManager;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStateLayoutManager = new StateLayoutManager(getContext(), (ViewGroup)findViewById(R.id.content_layout));

        mStateLayoutManager.setViewResourceId(StateLayoutManager.STATE_CONTENT, R.layout.footer_load_fail);
        mStateLayoutManager.setViewResourceId(StateLayoutManager.STATE_EMPTY, R.layout.state_empty);
        mStateLayoutManager.setViewResourceId(StateLayoutManager.STATE_LOADING, R.layout.state_loading);
        mStateLayoutManager.setViewResourceId(StateLayoutManager.STATE_NO_NETWORK, R.layout.state_no_network);

        findViewById(R.id.btn_show_content).setOnClickListener(this);
        findViewById(R.id.btn_show_empty).setOnClickListener(this);
        findViewById(R.id.btn_show_loading).setOnClickListener(this);
        findViewById(R.id.btn_show_no_network).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_show_content){
            showContent();
        }else if(id == R.id.btn_show_empty){
            showEmpty();
        }else if(id == R.id.btn_show_loading){
            showLoading();
        }else if(id == R.id.btn_show_no_network){
            showNoNetwork();
        }
    }

    public void showContent(){
        mStateLayoutManager.showState(StateLayoutManager.STATE_CONTENT);
    }

    public void showEmpty(){
        mStateLayoutManager.showState(StateLayoutManager.STATE_EMPTY);
    }

    public void showLoading(){
        mStateLayoutManager.showState(StateLayoutManager.STATE_LOADING);
    }

    public void showNoNetwork(){
        mStateLayoutManager.showState(StateLayoutManager.STATE_NO_NETWORK);
    }
}
