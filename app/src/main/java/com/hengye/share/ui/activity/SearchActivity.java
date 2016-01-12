package com.hengye.share.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.SearchUserAdapter;
import com.hengye.share.model.sina.WBTopic;
import com.hengye.share.util.L;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected String getRequestTag() {
        return super.getRequestTag();
    }

    @Override
    protected boolean setCustomTheme() {
        return super.setCustomTheme();
    }

    @Override
    protected boolean setToolBar() {
        return super.setToolBar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        initView();
    }

    private ImageButton mBackBtn, mDeleteBtn;
    private EditText mContent;
    private RecyclerView mUserRV, mTopicRV;

    private SearchUserAdapter mUserAdapter;
    private ArrayList<String> mUserData;

    private void initView(){
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mDeleteBtn = (ImageButton) findViewById(R.id.btn_delete);
        mContent = (EditText) findViewById(R.id.et_search);
        mUserRV = (RecyclerView) findViewById(R.id.recycler_view_user);
        mUserRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mUserRV.setAdapter(mUserAdapter = new SearchUserAdapter(this, getUserData()));
        mUserRV.setItemAnimator(new DefaultItemAnimator());
        mTopicRV = (RecyclerView) findViewById(R.id.recycler_view_topic);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_back){
            onBackPressed();
        }else if(id == R.id.btn_delete){

        }
    }

    private ArrayList<String> getUserData(){
        if(mUserData == null){
            mUserData = new ArrayList<>();
            mUserData.add("yu");
            mUserData.add("yu");
            mUserData.add("yu");
            mUserData.add("yu");
            mUserData.add("yu");
            mUserData.add("yu");
            mUserData.add("yu");
            mUserData.add("yu");
            mUserData.add("yu");
            mUserData.add("yu");
            mUserData.add("yu");
            mUserData.add("yu");
        }
        return mUserData;
    }

    private GsonRequest getWBSearchUserRequest(String token, String content) {

        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBSearchUserUrl());
        ub.addParameter("access_token", token);
        ub.addParameter("q", content);
        return new GsonRequest<>(Request.Method.POST,
                WBTopic.class,
                ub.getRequestUrl()
                , new Response.Listener<WBTopic>() {
            @Override
            public void onResponse(WBTopic response) {
                L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
                if(response != null){
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), volleyError);
            }
        });
    }
}
