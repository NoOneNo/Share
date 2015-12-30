package com.hengye.share.ui.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;

import com.hengye.share.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.AtUserSearchAdapter;
import com.hengye.share.adapter.recyclerview.AtUserSelectAdapter;
import com.hengye.share.module.AtUser;
import com.hengye.share.util.ViewUtil;
import com.hengye.swiperefresh.PullToRefreshLayout;

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


    private void initView() {

        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);

        mRVSelectResult = (RecyclerView) findViewById(R.id.recycler_view_select_result);
        mRVSelectResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRVSelectResult.setAdapter(mAtUserSelectAdapter = new AtUserSelectAdapter(this, getSelectResultData()));
        mRVSelectResult.setItemAnimator(new DefaultItemAnimator());

        mRVSearchResult = (RecyclerView) findViewById(R.id.recycler_view_search_result);
        mRVSearchResult.setLayoutManager(new LinearLayoutManager(this));
        mRVSearchResult.setAdapter(mAtUserSearchAdapter = new AtUserSearchAdapter(this, getSearchResultData()));

        mSearch = (EditText) findViewById(R.id.et_username);

        mSearchIcon = findViewById(R.id.ic_search);

    }

    private void initClick() {
        mAtUserSearchAdapter.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AtUser select = mAtUserSearchAdapter.getItem(position);
                if (select == null) {
                    return;
                }
                select.setSelected(!select.isSelected());
                updateSelectItems(select);

                mRVSelectResult.post(mRVSelectResultScrollToEnd);

                setCheckBoxSelect(position, select.isSelected());
            }
        });

        mAtUserSelectAdapter.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AtUser select = mAtUserSelectAdapter.getItem(position);
                if (select == null) {
                    return;
                }
                select.setSelected(!select.isSelected());
                updateSelectItems(select);

                mRVSelectResult.post(mRVSelectResultScrollToEnd);

                int index = mAtUserSearchAdapter.getItemPosition(select);
                if (index != -1) {
                    setCheckBoxSelect(index, select.isSelected());
                }

//                mAtUserSearchAdapter.notifyDataSetChanged();
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
                String str = mSearch.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    mAtUserSearchAdapter.refresh(mSearchResultData);
                } else {
                    mAtUserSearchAdapter.refresh(AtUser.search(mSearchResultData, str));
                }
            }
        });
    }

    private Runnable mRVSelectResultScrollToEnd = new Runnable() {
        @Override
        public void run() {
            if (mSelectResultData.size() > 1) {
                mRVSelectResult.scrollToPosition(mSelectResultData.size() - 1);
            }
        }
    };

    private void setCheckBoxSelect(int position, boolean isSelected) {
        ImageButton checkBox = (ImageButton) mRVSearchResult.findViewWithTag(position);
        if (checkBox != null) {
            checkBox.setImageResource(isSelected ? R.drawable.ic_check_select : 0);
        }
    }

    private void updateSelectItems(AtUser select) {
        long searchMoveTime;
        if (select.isSelected()) {
            searchMoveTime = mRVSelectResult.getItemAnimator().getAddDuration();
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
        mSearchItemWidth = getResources().getDimensionPixelSize(R.dimen.at_user_avatar);
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
//        if(mSelectItems == null || mSelectItems.size() == 0){
//            mNext.setText("确定");
//        }else{
//            mNext.setText("确定(" + mSelectItems.size() + ")");
//        }
    }

    private List<AtUser> getSelectResultData() {
        mSelectResultData = new ArrayList<>();
        return mSelectResultData;
    }

    private List<AtUser> getSearchResultData() {
        mSearchResultData = new ArrayList<>();
        int i = 0;
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));
        mSearchResultData.add(new AtUser("user" + i++ + i));

        return mSearchResultData;
    }
}
