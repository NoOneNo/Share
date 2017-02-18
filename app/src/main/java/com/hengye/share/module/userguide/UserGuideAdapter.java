package com.hengye.share.module.userguide;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.ui.widget.ExpandIconView;
import com.hengye.share.ui.widget.util.SelectorLoader;

public class UserGuideAdapter extends CommonAdapter<UserGuide> {

    public UserGuideAdapter(Context context) {
        super(context);
    }

    @Override
    public UserGuideHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new UserGuideHolder(inflate(R.layout.item_user_guide, parent));
    }

    public static class UserGuideHolder extends ItemViewHolder<UserGuide> {

        ExpandIconView expandIcon;
        TextView content;

        public UserGuideHolder(View v) {
            super(v);
            SelectorLoader.getInstance().setTransparentRippleBackground(v);
            expandIcon = (ExpandIconView) findViewById(R.id.expand_icon);
            content = (TextView) findViewById(R.id.tv_content);
        }

        @Override
        public void bindData(Context context, UserGuide userGuide, int position) {

            content.setText(userGuide.getContent());

            if (userGuide.isExpand()) {
                content.setSingleLine(false);
                expandIcon.setState(ExpandIconView.LESS, false);
            } else {
                content.setSingleLine(true);
                expandIcon.setState(ExpandIconView.MORE, false);
            }
        }
    }
}
