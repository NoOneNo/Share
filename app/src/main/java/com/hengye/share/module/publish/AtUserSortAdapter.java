package com.hengye.share.module.publish;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.AtUser;
import com.hengye.share.model.UserInfo;
import com.hengye.share.ui.widget.image.AvatarImageView;
import com.hengye.share.ui.widget.lettersort.recyclerview.GroupAdapter;
import com.hengye.share.ui.widget.util.SelectorLoader;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.ResUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AtUserSortAdapter extends GroupAdapter<AtUserSortAdapter.Letter, AtUser> {

    public AtUserSortAdapter(Context context){
        super(context, new ArrayList<>());
    }

    public AtUserSortAdapter(Context context, Map<Letter, List<AtUser>> map) {
        super(context, map);
    }

    public void showSearchResult(String str, List<Object> totalData, List<AtUser> atUsers) {
        if (TextUtils.isEmpty(str)) {
            refresh(totalData);
        } else {
            List<Object> objects = new ArrayList<>();
            objects.add(new Letter(ResUtil.getString(R.string.title_activity_attention_list)));
            objects.addAll(AtUser.search(atUsers, str));
            refresh(objects);
        }
    }

    @Override
    public ItemViewHolder onCreateGroupViewHolder(ViewGroup parent) {
        return new GroupHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_at_user_title, parent, false));
    }

    @Override
    public ItemViewHolder onCreateChildViewHolder(ViewGroup parent) {
        return new ChildHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_at_user_search_result, parent, false));
    }

    public static class GroupHolder extends ItemViewHolder<Letter> {

        TextView mTitle;

        public GroupHolder(View v) {
            super(v);
            mTitle = (TextView) findViewById(R.id.tv_text);
        }

        @Override
        public void bindData(Context context, Letter letter, int position) {
            mTitle.setText(letter.getLetter());
        }
    }

    public static class ChildHolder extends ItemViewHolder<AtUser> {

        ImageButton mCheckBox;
        TextView mUsername;
        AvatarImageView mAvatar;

        public ChildHolder(View v) {
            super(v);

            mCheckBox = (ImageButton) findViewById(R.id.btn_check);
            mUsername = (TextView) findViewById(R.id.tv_username);
            mAvatar = (AvatarImageView) findViewById(R.id.iv_avatar);

            SelectorLoader
                    .getInstance()
                    .setDefaultRippleWhiteBackground(v);
        }

        @Override
        public void bindData(Context context, AtUser atUser, int position) {
            mCheckBox.setTag(position);
            mCheckBox.setImageResource(atUser.isSelected() ? R.drawable.ic_check_select : 0);

            UserInfo userInfo = atUser.getUserInfo();
            if (userInfo != null) {
                mUsername.setText(userInfo.getName());
                mAvatar.setImageUrl(userInfo.getAvatar(), RequestManager.getImageLoader());
            } else {
                mAvatar.setImageResource(0);
            }
        }
    }

    public static class Letter implements GroupAdapter.SortKey {
        String letter;

        public Letter(){}

        public Letter(String letter) {
            this.letter = letter;
        }

        public String getLetter() {
            return letter;
        }

        public void setLetter(String letter) {
            this.letter = letter;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Letter letter1 = (Letter) o;

            return letter != null ? letter.equals(letter1.letter) : letter1.letter == null;

        }

        @Override
        public int hashCode() {
            return letter != null ? letter.hashCode() : 0;
        }
    }
}
