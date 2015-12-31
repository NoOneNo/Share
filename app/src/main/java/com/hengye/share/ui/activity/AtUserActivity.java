package com.hengye.share.ui.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.google.gson.reflect.TypeToken;
import com.hengye.share.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.AtUserSearchAdapter;
import com.hengye.share.adapter.recyclerview.AtUserSelectAdapter;
import com.hengye.share.module.AtUser;
import com.hengye.share.module.Topic;
import com.hengye.share.module.UserInfo;
import com.hengye.share.module.sina.WBTopicComments;
import com.hengye.share.module.sina.WBUserInfos;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.ViewUtil;
import com.hengye.share.util.thirdparty.WBUtil;
import com.hengye.swiperefresh.PullToRefreshLayout;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.ArrayList;
import java.util.List;

public class AtUserActivity extends BaseActivity {

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

        setContentView(R.layout.activity_at_user);
        initView();
        initViewSize();
        initClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private PullToRefreshLayout mPullToRefreshLayout;
    private RecyclerView mRVSelectResult, mRVSearchResult;
    private EditText mSearch;
    private View mSearchIcon;

    private AtUserSelectAdapter mAtUserSelectAdapter;

    private AtUserSearchAdapter mAtUserSearchAdapter;
    private List<AtUser> mSelectResultData, mSearchResultData;
    private LinearLayoutManager mSelectResultLayoutManager;

    private Oauth2AccessToken mOauth2AccessToken;

    private void initView() {

        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);

        mRVSelectResult = (RecyclerView) findViewById(R.id.recycler_view_select_result);
        mSelectResultLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRVSelectResult.setLayoutManager(mSelectResultLayoutManager);
        mRVSelectResult.setAdapter(mAtUserSelectAdapter = new AtUserSelectAdapter(this, getSelectResultData()));
        mRVSelectResult.setItemAnimator(new DefaultItemAnimator());

        mRVSearchResult = (RecyclerView) findViewById(R.id.recycler_view_search_result);
        mRVSearchResult.setLayoutManager(new LinearLayoutManager(this));
        mRVSearchResult.setAdapter(mAtUserSearchAdapter = new AtUserSearchAdapter(this, getSearchResultData()));
        mSearch = (EditText) findViewById(R.id.et_username);

        mSearchIcon = findViewById(R.id.ic_search);

        mOauth2AccessToken = SPUtil.getSinaAccessToken();

        Toolbar toolbar = getToolbar();
        toolbar.setTitle("选择用户");
//        toolbar.getMenu().addSubMenu("确定");
//        toolbar.setme
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_at_user_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_confirm);

        int count = mAtUserSelectAdapter.getSelectSize();
        if(count == 0){
            menuItem.setTitle(getResources().getString(R.string.label_confirm));
        }else{
            menuItem.setTitle(String.format(getResources().getString(R.string.label_confirm_count), count));
        }
        return true;
    }


    private void initClick() {
        mAtUserSearchAdapter.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AtUser select = mAtUserSearchAdapter.getItem(position);
                if (select == null) {
                    return;
                }
                updateSelectItems(select);

                mRVSelectResult.post(mRVSelectResultScrollToEnd);
                mAtUserSearchAdapter.notifyItemChanged(position);
            }
        });

        mAtUserSelectAdapter.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AtUser select = mAtUserSelectAdapter.getItem(position);
                if (select == null) {
                    return;
                }
                updateSelectItems(select);

                int index = mAtUserSearchAdapter.getItemPosition(select);
                if (index != -1) {
                    mAtUserSearchAdapter.notifyItemChanged(index);
                }

            }
        });

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAtUserSelectAdapter.setLastItemPrepareDelete(false);
                mAtUserSearchAdapter.showSearchResult(mSearch.getText().toString(), mSearchResultData);
            }
        });

        mSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (!TextUtils.isEmpty(mSearch.getText().toString())) {
                    return false;
                } else {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {

                        AtUser au = mAtUserSelectAdapter.getLastItem();
                        if (au != null) {
                            int position = mAtUserSelectAdapter.getLastItemPosition();
                            if (au.isPrepareDelete()) {
                                mAtUserSelectAdapter.getOnItemClickListener().onItemClick(null, position);
                                mRVSelectResult.scrollToPosition(position - 1);

                                int count = mRVSelectResult.getChildCount();

                                notifySelectResultFirstItem();
                            } else {
                                mRVSelectResult.scrollToPosition(position);
                                mAtUserSelectAdapter.setItemPrepareDelete(true, au, position);
                            }
                        }

                    }
                }
                return false;
            }
        });

        mRVSearchResult.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(RecyclerView.SCROLL_STATE_DRAGGING == newState){
                    ViewUtil.hideKeyBoard(mSearch);
                }
            }
        });

        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestManager.addToRequestQueue(getWBAttentionRequest(mOauth2AccessToken.getToken(), mOauth2AccessToken.getUid()));
            }
        });
        mPullToRefreshLayout.setLoadEnable(false);
//        mPullToRefreshLayout.setRefreshing(true);
    }

    private void notifySelectResultFirstItem(){
        int index = mSelectResultLayoutManager.findFirstVisibleItemPosition();
        mAtUserSelectAdapter.notifyItemChanged(index - 1);
    }

    private Runnable mRVSelectResultScrollToEnd = new Runnable() {
        @Override
        public void run() {
            if (mSelectResultData.size() > 1) {
                mRVSelectResult.scrollToPosition(mSelectResultData.size() - 1);
                notifySelectResultFirstItem();
            }
        }
    };

    private void updateSelectItems(AtUser select) {
        select.setSelected(!select.isSelected());

        long searchMoveTime;
        if (select.isSelected()) {
            searchMoveTime = mRVSelectResult.getItemAnimator().getAddDuration();
            mAtUserSelectAdapter.setLastItemPrepareDelete(false);
            select.setPrepareDelete(false);
            mAtUserSelectAdapter.add(select);
        } else {
            int index = mAtUserSelectAdapter.getItemPosition(select);
            if (index == mAtUserSelectAdapter.getBasicItemCount() - 1) {
                //如果是最后一个就是remove动画，否则是move动画
                searchMoveTime = mRVSelectResult.getItemAnimator().getRemoveDuration();
            } else {
                searchMoveTime = mRVSelectResult.getItemAnimator().getMoveDuration() * 2;
            }
            mAtUserSelectAdapter.remove(select);

        }
        moveSearchTowardLeftOrRight(select.isSelected(), searchMoveTime);
        updateSelectItemCount();
    }

    private void initViewSize() {
        mSearchItemWidth = getResources().getDimensionPixelSize(R.dimen.at_user_avatar) +
                getResources().getDimensionPixelSize(R.dimen.content_margin_3dp) * 2;
        mSearchMarginStart = mSearchMarginStartOriginal = getResources().getDimensionPixelSize(R.dimen.at_user_et_search_margin_start);
    }

    private int mSearchMarginStartOriginal, mSearchItemWidth, mSearchMarginStart;

    private void moveSearchTowardLeftOrRight(boolean isMoveRight, long searchMoveTime) {
        if (isMoveRight) {
            if (!canSearchMoveRight()) {
                return;
            }
        } else {
            if (!canSearchMoveLeft()) {
                return;
            }
        }

        if (mSelectResultData.isEmpty()) {
            mSearchIcon.setVisibility(View.VISIBLE);
        } else if (mSelectResultData.size() == 1) {
            if (isMoveRight) {
                //如果是向右移动，隐藏搜索图标
                mSearchIcon.setVisibility(View.GONE);
            }
        }

        int expectMarginStart;
        if (mSelectResultData.isEmpty()) {
            expectMarginStart = mSearchMarginStartOriginal;
        } else {
            expectMarginStart = mSelectResultData.size() * mSearchItemWidth;
        }

        if (expectMarginStart > mRVSelectResult.getWidth()) {
            mSearchMarginStart = mRVSelectResult.getWidth();
        } else {
            mSearchMarginStart = expectMarginStart;
        }

        int targetX = mSearchMarginStart - mSearchMarginStartOriginal;
        ObjectAnimator oa = ObjectAnimator.ofFloat(mSearch, "translationX", mSearch.getTranslationX(), targetX);
        oa.setDuration(searchMoveTime);
        oa.setInterpolator(new AccelerateInterpolator());
        oa.start();
    }


    private boolean canSearchMoveRight() {
        if (mRVSelectResult.getWidth() > mSearchMarginStart) {
            return true;
        } else {
            return false;
        }
    }

    private boolean canSearchMoveLeft() {
        if (mSearchMarginStart > mSelectResultData.size() * mSearchItemWidth) {
            return true;
        }
        if (mSearchMarginStart >= mRVSelectResult.getWidth()) {
            return true;
        }
        return false;
    }

    private void updateSelectItemCount() {
        invalidateOptionsMenu();
    }

    private List<AtUser> getSelectResultData() {
        mSelectResultData = new ArrayList<>();
        return mSelectResultData;
    }

    private List<AtUser> getSearchResultData() {
        mSearchResultData = SPUtil.getModule(new TypeToken<ArrayList<AtUser>>() {
        }.getType(), AtUser.class.getSimpleName() + SPUtil.getSinaUid());

        if(mSearchResultData == null){
            mSearchResultData = new ArrayList<>();
        }
        return mSearchResultData;
    }

    //https://api.weibo.com/2/friendships/friends.json?access_token=2.00OXW56CiGSdcC7b3b00d3940PueHq&count=200&uid=2207519004

    private GsonRequest getWBAttentionRequest(String token, String uid) {
        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBAttentionUrl());
        ub.addParameter("access_token", token);
        ub.addParameter("uid", uid);
        ub.addParameter("count", 200);
//        ub.addParameter("count", WBUtil.MAX_COUNT_REQUEST);
        return new GsonRequest<>(
                WBUserInfos.class,
                ub.getRequestUrl(),
                new Response.Listener<WBUserInfos>() {
                    @Override
                    public void onResponse(WBUserInfos response) {
                        L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
                        handleData(UserInfo.getUserInfos(response));
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                if (isRefresh) {
                    mPullToRefreshLayout.setRefreshing(false);
//                } else {
                    mPullToRefreshLayout.setLoading(false);
//                }
                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), error);
            }

        });
    }

    private void handleData(List<UserInfo> data){
//        if (isRefresh) {
            mPullToRefreshLayout.setRefreshing(false);
//                } else {
            mPullToRefreshLayout.setLoading(false);
//                }

        mSearchResultData = AtUser.getAtUser(data);

        SPUtil.setModule(mSearchResultData, AtUser.class.getSimpleName() + SPUtil.getSinaUid());
        mAtUserSearchAdapter.refresh(mSearchResultData);
//        mAtUserSearchAdapter.notifyDataSetChanged();
    }
}



