package com.hengye.share.module.util.encapsulation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.module.base.ActivityHelper;
import com.hengye.share.module.util.encapsulation.mvp.BasePresenter;
import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;

import java.util.HashSet;
import java.util.Set;


public class BaseFragment extends Fragment implements ActivityHelper.ActivityActionInterceptListener {

    public final static String BASE_FRAGMENT = "BaseFragment";

    public boolean setToolBar() {
        return getTitle() != null;
    }

    public String getTitle(){
        return null;
    }

    protected String getRequestTag(){
        return null;
    }

    private View parent;

//    private BasePresenter mPresenter;
    private Set<BasePresenter> mPresenters;

    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleBundleExtra(getArguments());
    }

    protected void handleBundleExtra(Bundle bundle){}

    public @LayoutRes int getLayoutResId(){
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        if(getLayoutResId() == 0){
            return null;
        }

        if(parent == null){
            parent = inflater.inflate(getLayoutResId(), container, false);
        }
//        if(parent == null){
//            parent = inflater.inflate(getLayoutResId(), container, false);
//        }else{
//            // 不再重新绘制UI
//            // 缓存的rootView需要判断是否已经被加过parent，
//            // 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
//            ViewGroup parent = (ViewGroup) this.parent.getParent();
//            if(parent != null){
//                parent.removeView(this.parent);
//            }
//            return this.parent;
//        }
//        initLayout(inflater, container, savedInstanceState);

        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        cancelPendingRequestsIfNeeded();
        detachMvpView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        onLazyLoadIfNeed();
    }

    private boolean mPrepared = false;
    private boolean mLazyLoaded = false;
    private void onLazyLoadIfNeed(){
        if(!mLazyLoaded && mPrepared && getUserVisibleHint()){
            mLazyLoaded = true;
            onLazyLoad();
        }
    }

    /**
     * 当真的可见的时候并且没有懒加载过的时候会调用；
     */
    protected void onLazyLoad(){

    }

    public void markLazyLoadPrepared(){
        mPrepared = true;
    }

    public void markLazyLoadPreparedAndLazyLoadIfCan(){
        markLazyLoadPrepared();
        onLazyLoadIfNeed();
    }

    protected void cancelPendingRequestsIfNeeded(){
        if(getRequestTag() != null){
            RequestManager.cancelPendingRequests(getRequestTag());
        }
    }

    public View getParent() {
        return parent;
    }

    public View findViewById(@IdRes int id){
        return parent.findViewById(id);
    }

    public void addPresenter(BasePresenter presenter) {
        if(mPresenters == null){
            mPresenters = new HashSet<>();
        }
        mPresenters.add(presenter);
    }

    protected void detachMvpView(){
        if(mPresenters != null){
            for(BasePresenter presenter : mPresenters){
                presenter.detachView();
            }
        }
    }

    public void onToolbarDoubleClick(Toolbar toolbar){
    }

    public final void setResult(int resultCode) {
        getActivity().setResult(resultCode);
    }

    public final void setResult(int resultCode, Intent data) {
        getActivity().setResult(resultCode, data);
    }

    public void finish() {
        getActivity().finish();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }
}
