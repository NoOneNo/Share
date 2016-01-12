package com.hengye.share.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.ui.fragment.BaseFragment;

/**
 * This is a Activity to show a Fragment extends BaseFragment.
 */
public class FragmentActivity extends BaseActivity {

    protected BaseFragment mFragment;

    public final static String FRAGMENT_CLASS = "fragment_class";

    public static <T extends BaseFragment> Intent getStartIntent(Context context, Class<T> clazz) {
        return getStartIntent(context, clazz, null);
    }

    public static <T extends BaseFragment> Intent getStartIntent(Context context, Class<T> clazz, Bundle extras) {
        Intent intent = new Intent(context, FragmentActivity.class);
        intent.putExtra(FRAGMENT_CLASS, clazz);
        if (extras != null) {
            intent.putExtras(extras);
        }
        return intent;
    }

    @Override
    protected CharSequence getToolbarTitle() {
        ensureFragment();
        return mFragment.getTitle();
    }

    protected void ensureFragment(){
        if (mFragment == null) {
            try {
                mFragment = getBaseFragmentClass().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected Class<? extends BaseFragment> getBaseFragmentClass() {
        try {
            Class clazz = (Class) getIntent().getSerializableExtra(FRAGMENT_CLASS);
            if (clazz.getSuperclass().getSimpleName().equals(BaseFragment.BASE_FRAGMENT)) {
                return clazz;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ensureFragment();

        setContentView(R.layout.activity_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, mFragment)
                    .commit();
        }
    }
}
