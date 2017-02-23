package com.hengye.share.module.status;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.Status;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.profile.PersonalHomepageActivity;
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.ui.widget.image.AvatarImageView;

/**
 * Created by yuhy on 2016/11/29.
 */
public class StatusTitleViewHolder {

    public AvatarImageView mAvatar;
    public TextView mUsername, mDescription;
    public View mTitle;

    public StatusTitleViewHolder() {
    }

    public StatusTitleViewHolder(View v) {
        mTitle = v.findViewById(R.id.layout_status_title);
        mAvatar = (AvatarImageView) v.findViewById(R.id.iv_status_avatar);
        mUsername = (TextView) v.findViewById(R.id.tv_status_username);
        mDescription = (TextView) v.findViewById(R.id.tv_status_description);

//            mTitle.setTag(false);
    }

    public void initStatusTitle(final Context context, Status status) {
        if (TextUtils.isEmpty(status.getChannel())) {
            mDescription.setText(status.getFormatDate());
        } else {
            String str = String.format(context.getString(R.string.label_time_and_from), status.getFormatDate(), Html.fromHtml(status.getChannel()));
            mDescription.setText(str);
        }

        UserInfo userInfo = status.getUserInfo();
        if (userInfo != null) {
            mUsername.setText(userInfo.getName());
            if (SettingHelper.isShowStatusAvatar()) {
                mAvatar.setImageUrl(userInfo.getAvatar());
            } else {
                mAvatar.setImageResource(R.drawable.ic_user_avatar);
            }
        }
    }

    public static boolean isClickStatusTitle(int id){
        return id == R.id.iv_status_avatar || id == R.id.tv_status_username || id == R.id.tv_status_description;
    }

    public static boolean onClickStatusTitle(Context context, CommonAdapter adapter, View view, int position, UserInfo userInfo) {
        int id = view.getId();
        if (isClickStatusTitle(id)) {
            View startView = null;
            if (id == R.id.iv_status_avatar) {
                //如果点击的是头像
                startView = view;
            } else {
                RecyclerView.ViewHolder viewHolder = adapter.findViewHolderForLayoutPosition(position);
                if (viewHolder != null && viewHolder instanceof StatusTitle) {
                    StatusTitle statusTitle = (StatusTitle) viewHolder;
                    startView = statusTitle.getStatusTitleViewHolder().mAvatar;
                }
            }
            PersonalHomepageActivity.start(context, startView, userInfo);
            return true;
        }
        return false;
    }

    /**
     * Created by yuhy on 2016/11/29.
     */
    public interface StatusTitle {
        StatusTitleViewHolder getStatusTitleViewHolder();
    }
}
