package com.hengye.share.module.hottopic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.HotTopic;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.ui.widget.image.SuperImageView;
import com.hengye.share.util.ResUtil;

import java.util.List;

public class HotTopicAdapter extends CommonAdapter<HotTopic> {

    public HotTopicAdapter(Context context, List<HotTopic> data) {
        super(context, data);
    }

    @Override
    public HotTopicViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new HotTopicViewHolder(inflate(R.layout.item_hot_topic, parent));
    }

    public static class HotTopicViewHolder extends ItemViewHolder<HotTopic> {

        SuperImageView cover;
        TextView title, subTitle, desc;

        public HotTopicViewHolder(View v) {
            super(v);

            cover = (SuperImageView) findViewById(R.id.iv_cover);
            cover.setDefaultImageResId(R.drawable.ic_user_avatar);
            title = (TextView) findViewById(R.id.tv_title);
            subTitle = (TextView) findViewById(R.id.tv_subtitle);
            desc = (TextView) findViewById(R.id.tv_description);
            registerOnClickListener(v);
        }

        @Override
        public void bindData(Context context, HotTopic hotTopic, int position) {

            cover.setImageUrl(hotTopic.getCover());

            title.setText(ResUtil.getString(R.string.label_hot_topic_modifier, hotTopic.getTopic()));
            subTitle.setText(hotTopic.getDescription());
            desc.setText(hotTopic.getReadingInfo());
        }
    }
}
