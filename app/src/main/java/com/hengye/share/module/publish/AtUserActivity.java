package com.hengye.share.module.publish;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.AtUser;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.ShareJson;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.profile.UserListContract;
import com.hengye.share.module.profile.UserListPresenter;
import com.hengye.share.module.publish.AtUserSortAdapter.Letter;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.ui.widget.lettersort.SideBar;
import com.hengye.share.ui.widget.recyclerview.DividerItemDecoration;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ThemeUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.ViewUtil;
import com.hengye.swiperefresh.PullToRefreshLayout;
import com.hengye.swiperefresh.listener.SwipeListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.hengye.share.module.util.encapsulation.base.TaskState.isSuccess;

public class AtUserActivity extends BaseActivity implements UserListContract.View  {

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
    public int getLayoutResId() {
        return R.layout.activity_at_user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new UserListPresenter(this, UserUtil.getUid());
        initView();
        initViewSize();
        initListener();
    }

    private PullToRefreshLayout mPullToRefreshLayout;
    private RecyclerView mRVSelectResult, mRVSearchResult;
    private EditText mSearch;
    private SideBar mSideBar;
    private View mSearchIcon, mLetter;
    private TextView mLetterTV;

    private AtUserSelectAdapter mAtUserSelectAdapter;

    private AtUserSortAdapter mAtUserSearchAdapter;
    private ArrayList<AtUser> mSelectResultData, mSearchResultData;
    private List<Object> mSearchResultTotalData;
    private LinearLayoutManager mSelectResultLayoutManager, mSearchResultLayoutManager;

    private UserListPresenter mPresenter;

    private void initView() {

        mSideBar = (SideBar) findViewById(R.id.side_bar);
        mSideBar.setLetterNormalColor(ThemeUtil.getDarkColor());
        mSideBar.setLetterPressedColor(ThemeUtil.getColor());
        mSideBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                L.debug("sidebar onTouch invoke()!");
                return false;
            }
        });

        mLetterTV = (TextView) findViewById(R.id.tv_letter);
        mLetterTV.setTextColor(ThemeUtil.getTextColor());
        mLetter = findViewById(R.id.fl_letter);
        mLetter.setBackgroundColor(ThemeUtil.getColor());
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);

        mRVSelectResult = (RecyclerView) findViewById(R.id.recycler_view_select_result);
        mSelectResultLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRVSelectResult.setLayoutManager(mSelectResultLayoutManager);

        mRVSelectResult.setAdapter(mAtUserSelectAdapter = new AtUserSelectAdapter(this, mSelectResultData = mPresenter.getSelectResultData()));
        mRVSelectResult.setItemAnimator(new DefaultItemAnimator());

        mRVSearchResult = (RecyclerView) findViewById(R.id.recycler_view_search_result);
        mRVSearchResult.addItemDecoration(new DividerItemDecoration(this));
        mRVSearchResult.setLayoutManager(mSearchResultLayoutManager = new LinearLayoutManager(this));
//        mRVSearchResult.setAdapter(mAtUserSearchAdapter = new AtUserSortAdapter(this, mSearchResultData = mPresenter.getSearchResultData()));
        mRVSearchResult.setAdapter(mAtUserSearchAdapter = new AtUserSortAdapter(this));
        mSearchResultData = mPresenter.getSearchResultData();
        mAtUserSearchAdapter.refresh(convertUserList(mSearchResultData));
        mSearch = (EditText) findViewById(R.id.et_username);
        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    search();
                }
                return false;
            }
        });
        mSearchIcon = findViewById(R.id.ic_search);

        if (UserUtil.isUserEmpty()) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_at_user_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_confirm);

        int count = mAtUserSelectAdapter.getSelectSize();
        if (count == 0) {
            menuItem.setTitle(getResources().getString(R.string.label_confirm));
        } else {
            menuItem.setTitle(getResources().getString(R.string.label_confirm_count, count + ""));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_confirm) {

            if (handleAtUser()) {
                finish();
            } else {
                ToastUtil.showToast(R.string.label_select_at_user_null);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean handleAtUser() {
        if (CommonUtil.isEmpty(mSelectResultData)) {
            return false;
        }

        Intent intent = new Intent();
        intent.putExtra("atUser", AtUser.getAtUserName(mSelectResultData));
        setResult(Activity.RESULT_OK, intent);
        return true;
    }


    private void initListener() {
        mAtUserSearchAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                AtUser select = mAtUserSearchAdapter.getItem(position);
                Object obj = mAtUserSearchAdapter.getItem(position);
                if(!(obj instanceof AtUser)){
                    return;
                }

                AtUser select = (AtUser) obj;
                updateSelectItems(select);

                mRVSelectResult.post(mRVSelectResultScrollToEnd);
                mAtUserSearchAdapter.notifyItemChanged(position);
            }
        });

        mAtUserSelectAdapter.setOnItemClickListener(new OnItemClickListener() {
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
                search();
            }
        });

        mSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (!TextUtils.isEmpty(mSearch.getText().toString())) {
                    return false;
                }
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {

                    AtUser au = mAtUserSelectAdapter.getLastItem();
                    if (au != null) {
                        int position = mAtUserSelectAdapter.getLastItemPosition();
                        if (au.isPrepareDelete()) {
                            mAtUserSelectAdapter.getOnItemClickListener().onItemClick(null, position);
                            mRVSelectResult.scrollToPosition(position - 1);

                            notifySelectResultFirstItem();
                        } else {
                            mRVSelectResult.scrollToPosition(position);
                            mAtUserSelectAdapter.setItemPrepareDelete(true, au, position);
                        }
                    }

                }
                return false;
            }
        });

        ViewUtil.hideKeyBoardOnScroll(mRVSearchResult, mSearch);

        mPullToRefreshLayout.setOnRefreshListener(new SwipeListener.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadWBAttentions(true, true);
            }
        });
        mPullToRefreshLayout.setLoadEnable(false);

        if (CommonUtil.isEmpty(mSearchResultData)) {
            mPullToRefreshLayout.setRefreshing(true);
        }

        mSideBar.setOnTouchLetterListener(new SideBar.OnTouchLetterListener() {
            @Override
            public void onTouchLetter(String s) {
                mLetter.setVisibility(View.VISIBLE);
                mLetterTV.setText(s);
                int selection = mAtUserSearchAdapter.getKeyIndex(new Letter(s));
                if (selection != -1) {
                    mSearchResultLayoutManager.scrollToPositionWithOffset(selection, 0);
                }
//                L.debug("s : {}, index : {}", s, selection);
            }

            @Override
            public void onTouchOutside() {
                mLetter.setVisibility(View.GONE);
            }
        });
    }

    private void search(){
        mAtUserSelectAdapter.setLastItemPrepareDelete(false);
        mAtUserSearchAdapter.showSearchResult(mSearch.getText().toString(), mSearchResultTotalData, mSearchResultData);
    }

    private void notifySelectResultFirstItem() {
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
            mAtUserSelectAdapter.addItem(select);
        } else {
            int index = mAtUserSelectAdapter.getItemPosition(select);
            if (index == mAtUserSelectAdapter.getBasicItemCount() - 1) {
                //如果是最后一个就是remove动画，否则是move动画
                searchMoveTime = mRVSelectResult.getItemAnimator().getRemoveDuration();
            } else {
                searchMoveTime = mRVSelectResult.getItemAnimator().getMoveDuration() * 2;
            }
            mAtUserSelectAdapter.removeItem(select);

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
        return mRVSelectResult.getWidth() > mSearchMarginStart;
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

    @Override
    public void onLoadListData(boolean isRefresh, List<UserInfo> data) {
        mSearchResultData = AtUser.getAtUser(data);

        ShareJson.saveListData(AtUser.class.getSimpleName() + UserUtil.getUid(), mSearchResultData, false);
        mAtUserSearchAdapter.refresh(convertUserList(mSearchResultData));
        if(!isRefresh && CommonUtil.isEmpty(data)){
            mPullToRefreshLayout.setLoadEnable(false);
        }
    }

    @Override
    public void onTaskStart() {}

    @Override
    public void onTaskComplete(boolean isRefresh, int taskState) {
        mPullToRefreshLayout.setTaskComplete(isSuccess(taskState));
        if(!TaskState.isSuccess(taskState)) {
            TaskState.toastState(taskState);
        }
    }

    public List<Object> convertUserList(List<AtUser> userInfos) {

        Map<Letter, List<AtUser>> sortUsers = new LinkedHashMap<>();
        try {

            List<String> data = mSideBar.getData().subList(1, mSideBar.getData().size());

            for (AtUser atUser : userInfos) {
                Letter letter = new Letter();
                String key = "#";
                UserInfo userInfo = atUser.getUserInfo();
                if (!TextUtils.isEmpty(userInfo.getSpell()) && data.contains(userInfo.getSpell())) {
                    key = userInfo.getSpell();
                }
                letter.setLetter(key);

                List<AtUser> temp = sortUsers.get(letter);
                if (temp == null) {
                    temp = new ArrayList<>();
                    sortUsers.put(letter, temp);
                }
                temp.add(atUser);
            }

            if(!sortUsers.isEmpty()){
                Map<Letter, List<AtUser>> result = new LinkedHashMap<>();
                Letter letter;
                for(String str : data){
                    letter = new Letter(str);
                    if(sortUsers.containsKey(letter)){
                        result.put(letter, sortUsers.get(letter));
                    }
                }
                sortUsers.clear();
                sortUsers = result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mSearchResultTotalData = mAtUserSearchAdapter.convertMapToList(sortUsers);
    }
}




