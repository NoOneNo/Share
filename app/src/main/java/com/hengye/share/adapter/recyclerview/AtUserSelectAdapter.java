package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.view.NetworkImageView;
import com.hengye.share.R;
import com.hengye.share.module.AtUser;
import com.hengye.share.module.UserInfo;
import com.hengye.share.util.RequestManager;

import java.util.List;

public class AtUserSelectAdapter extends CommonAdapter<AtUser, AtUserSelectAdapter.MainViewHolder> {

    public AtUserSelectAdapter(Context context, List<AtUser> data) {
        super(context, data);
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_at_user_select_result, parent, false));
    }

    @Override
    public void onBindBasicItemView(MainViewHolder holder, int position) {
        super.onBindBasicItemView(holder, position);

    }

    public void setLastItemPrepareDelete(boolean isPrepareDelete) {
        AtUser au = getLastItem();
        if (au != null) {
            setItemPrepareDelete(isPrepareDelete, au, getLastItemPosition());
        }
    }

    public void setItemPrepareDelete(boolean isPrepareDelete, AtUser au, int position) {
        if(au.isPrepareDelete() != isPrepareDelete) {
            au.setPrepareDelete(isPrepareDelete);
            notifyItemChanged(position);
        }
    }

    public int getSelectSize(){
        return getBasicItemCount();
    }

    public static class MainViewHolder extends CommonAdapter.ItemViewHolder<AtUser> {

        NetworkImageView mAvatar;
        View mAvatarMask;

        public MainViewHolder(View v) {
            super(v);

            mAvatar = (NetworkImageView) v.findViewById(R.id.iv_avatar);
            mAvatarMask = v.findViewById(R.id.iv_avatar_mask);
        }

        @Override
        public void bindData(Context context, AtUser atUser, int position) {
            if (atUser.isPrepareDelete()) {
                mAvatarMask.setVisibility(View.VISIBLE);
            } else {
                mAvatarMask.setVisibility(View.GONE);
            }

            UserInfo userInfo = atUser.getUserInfo();
            if(userInfo != null){
                mAvatar.setImageUrl(userInfo.getAvatar(), RequestManager.getImageLoader());
            }
        }
    }
}
