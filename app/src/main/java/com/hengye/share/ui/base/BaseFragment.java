package com.hengye.share.ui.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.ui.presenter.BasePresenter;
import com.hengye.share.util.RequestManager;

import java.util.HashSet;
import java.util.Set;


public class BaseFragment extends Fragment {

    public final static String BASE_FRAGMENT = "BaseFragment";

    public boolean setToolBar() {
        if(getTitle() == null){
            return false;
        }
        return true;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleBundleExtra();
    }

    protected void handleBundleExtra(){}

    protected @LayoutRes int getLayoutResId(){
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
        cancelPendingRequestsIfNeeded();
        detachMvpView();
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
}
