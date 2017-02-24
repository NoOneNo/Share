package com.hengye.share.module.util.image;

import android.view.View;

import com.hengye.share.module.util.encapsulation.fragment.BaseFragment;

/**
 * Created by yuhy on 2016/11/23.
 */

public class ImageBaseFragment extends BaseFragment
        implements View.OnLongClickListener, View.OnClickListener {

    @Override
    public boolean onLongClick(View v) {
        if(getParentFragment() != null && getParentFragment() instanceof ImageFragment){
            return ((ImageFragment)getParentFragment()).onLongClick(v);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if(getActivity() != null){
            getActivity().onBackPressed();
        }
    }
}
