package com.hengye.share.module.publish;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.model.AtUser;
import com.hengye.share.model.UserInfo;
import com.hengye.share.ui.widget.image.AvatarImageView;
import com.hengye.share.util.RequestManager;

import java.util.List;

public class AtUserSelectAdapter extends CommonAdapter<AtUser> {

    public AtUserSelectAdapter(Context context, List<AtUser> data) {
        super(context, data);
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_at_user_select_result, parent, false));
    }

    public void setLastItemPrepareDelete(boolean isPrepareDelete) {
        AtUser au = getLastItem();
        if (au != null) {
            setItemPrepareDelete(isPrepareDelete, au, getLastItemPosition());
        }
    }

    public void setItemPrepareDelete(boolean isPrepareDelete, AtUser au, int position) {
        if (au.isPrepareDelete() != isPrepareDelete) {
            au.setPrepareDelete(isPrepareDelete);
            notifyItemChanged(position);
        }
    }

    public int getSelectSize() {
        return getBasicItemCount();
    }

    public static class MainViewHolder extends ItemViewHolder<AtUser> {

        AvatarImageView mAvatar;
        View mAvatarMask;

        public MainViewHolder(View v) {
            super(v);

            mAvatar = (AvatarImageView) findViewById(R.id.iv_avatar);
            mAvatarMask = findViewById(R.id.iv_avatar_mask);
        }

        @Override
        public void bindData(Context context, AtUser atUser, int position) {
            if (atUser.isPrepareDelete()) {
                mAvatarMask.setVisibility(View.VISIBLE);
            } else {
                mAvatarMask.setVisibility(View.GONE);
            }

            UserInfo userInfo = atUser.getUserInfo();
            if (userInfo != null) {
                mAvatar.setImageUrl(userInfo.getAvatar(), RequestManager.getImageLoader());
            }
        }
    }
}
