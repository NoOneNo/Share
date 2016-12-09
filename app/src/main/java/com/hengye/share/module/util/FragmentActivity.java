package com.hengye.share.module.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.fragment.BaseFragment;

/**
 * This is a Activity to show a Fragment extends BaseFragment.
 */
public class FragmentActivity extends BaseActivity {

    protected BaseFragment mFragment;

    public final static String FRAGMENT_CLASS = "fragment_class";

    public static <T extends BaseFragment> Intent getStartIntent(Context context, Class<T> fragmentClazz) {
        return getStartIntent(context, fragmentClazz, null);
    }

    public static <T extends BaseFragment> Intent getStartIntent(Context context, Class<T> fragmentClazz, Bundle extras) {
        return getStartIntent(context, fragmentClazz, extras, FragmentActivity.class);
    }

//
    public static <T extends BaseFragment, A extends FragmentActivity> Intent getStartIntent(Context context, Class<T> fragmentClazz, Bundle extras, Class<A> activityClazz) {
        Intent intent = new Intent(context, activityClazz);
        intent.putExtra(FRAGMENT_CLASS, fragmentClazz);
        if (extras != null) {
            intent.putExtras(extras);
        }
        return intent;
    }

    @Override
    protected boolean setToolBar() {
        return mFragment.setToolBar();
    }

    @Override
    protected CharSequence getToolbarTitle() {
        return mFragment.getTitle();
    }

    protected void ensureFragment(){
        if (mFragment == null) {
            try {
                mFragment = getBaseFragmentClass().newInstance();
                mFragment.setArguments(getIntent().getExtras());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected Class<? extends BaseFragment> getBaseFragmentClass() {
        try {
            return (Class) getIntent().getSerializableExtra(FRAGMENT_CLASS);
//            if (clazz.getSuperclass().getSimpleName().equals(BaseFragment.BASE_FRAGMENT)) {
//                return clazz;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ensureFragment();
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, mFragment)
                    .commit();
        }
    }
}
