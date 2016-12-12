package com.hengye.share.module.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.hengye.share.R;
import com.hengye.share.model.Address;
import com.hengye.share.module.groupmanage.AroundAddressAdapter;
import com.hengye.share.module.test.TestLocationActivity;
import com.hengye.share.module.util.FragmentActivity;
import com.hengye.share.module.util.encapsulation.base.DefaultDataHandler;
import com.hengye.share.module.util.encapsulation.base.NumberPager;
import com.hengye.share.module.util.encapsulation.fragment.RecyclerRefreshFragment;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.util.L;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.ViewUtil;
import com.hengye.share.util.thirdparty.AMapUtil;

import java.util.ArrayList;

/**
 * Created by yuhy on 2016/12/6.
 */

public class AroundAddressFragment extends RecyclerRefreshFragment<Address> implements AroundAddressMvpView, View.OnClickListener {

    public static void start(Activity activity, int requestCode) {
        activity.startActivityForResult(FragmentActivity.getStartIntent(activity, AroundAddressFragment.class), requestCode);
    }

    public static final String EXTRA_ADDRESS = "address";

    @Override
    public String getTitle() {
        return ResUtil.getString(R.string.title_activity_around_address);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_around_address;
    }

    @Override
    public int getContentResId() {
        return R.layout.fragment_recycler_refresh_vertical;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mKeywordsTxt = (EditText) findViewById(R.id.et_keywords);
        mCurrentLocationTxt = (TextView) findViewById(R.id.tv_current_address);
        mCurrentLocation = findViewById(R.id.layout_current_location);
        mCurrentLocation.setOnClickListener(this);
        mSearchBtn = findViewById(R.id.ic_search);
        mSearchBtn.setOnClickListener(this);

        mKeywordsTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchAroundAddress();
                }
                return false;
            }
        });

        setAdapter(mAdapter = new AroundAddressAdapter(getContext(), new ArrayList<Address>()));
        setPager(mNumberPager = new NumberPager());
        setDataHandler(new DefaultDataHandler<>(mAdapter));
        addPresenter(mPresenter = new AroundAddressPresenter(this));
        mPresenter.setPager(mNumberPager);
        ViewUtil.hideKeyBoardOnScroll(getRecyclerView(), mKeywordsTxt);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Address address = mAdapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(EXTRA_ADDRESS, address);
                setResult(Activity.RESULT_OK, intent);
                finish();
                L.debug("onClick Address : {}", address);
            }
        });

        showLoading();
        mCurrentLocationTxt.setText(R.string.tip_locating);
        initLocation();
    }

    AroundAddressPresenter mPresenter;
    EditText mKeywordsTxt;
    TextView mCurrentLocationTxt;
    View mSearchBtn, mCurrentLocation;
    AroundAddressAdapter mAdapter;
    NumberPager mNumberPager;

    AMapLocation mLocation;
    AMapLocationClient mAMapLocationClient;

    private void initLocation() {
        mAMapLocationClient = AMapUtil.initLocationClient(getActivity(), new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation loc) {
                if (null != loc && loc.getErrorCode() == AMapLocation.LOCATION_SUCCESS) {
                    mLocation = loc;
                    mPresenter.setLocation((float) loc.getLongitude(), (float) loc.getLatitude());
                    mCurrentLocationTxt.setText(loc.getAddress());
                    mPresenter.loadAroundAddress(true);
                    //解析定位结果
                    String result = TestLocationActivity.Utils.getLocationStr(loc);
                    L.debug("定位成功 : {}", result);
                } else {
                    mCurrentLocationTxt.setText(R.string.tip_location_fail);
                    L.debug("定位失败");
                    if(loc != null){
                        L.debug("错误信息 : {}", loc);
                    }
                }
            }
        });
    }

    private void searchAroundAddress() {
        mPresenter.setKeywords(mKeywordsTxt.getText().toString());
        onRefresh();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.layout_current_location) {
            if (mLocation == null && mAMapLocationClient != null) {
                mCurrentLocationTxt.setText(R.string.tip_locating);
                mAMapLocationClient.startLocation();
            }
        } else if (id == R.id.ic_search) {
            searchAroundAddress();
        }
    }

    @Override
    public void onRefresh() {
        if (isLocationSuccess()) {
            mPresenter.loadAroundAddress(true);
        } else {
            setRefreshing(false);
        }
    }

    @Override
    public void onLoad() {
        mPresenter.loadAroundAddress(false);
    }

    private boolean isLocationSuccess() {
        if (mLocation == null) {
            ToastUtil.showToast(R.string.tip_locating);
            return false;
        }
        return true;
    }

    @Override
    public void onTaskComplete(boolean isRefresh, int taskState) {
        super.onTaskComplete(isRefresh, taskState);
        ViewUtil.hideKeyBoard(mKeywordsTxt);
    }
}














