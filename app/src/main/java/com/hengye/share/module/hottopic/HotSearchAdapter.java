package com.hengye.share.module.hottopic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.HotSearch;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.ResUtil;

public class HotSearchAdapter extends CommonAdapter<HotSearch> {

    public HotSearchAdapter(Context context) {
        super(context);
    }

    @Override
    public HotSearchViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new HotSearchViewHolder(inflate(R.layout.item_hot_search, parent));
    }

    public static class HotSearchViewHolder extends ItemViewHolder<HotSearch> {

        TextView title, count;
        public HotSearchViewHolder(View v) {
            super(v);
            title = (TextView) findViewById(R.id.tv_title);
            count = (TextView) findViewById(R.id.tv_count);
        }

        @Override
        public void bindData(Context context, HotSearch hotSearch, int position) {

            title.setText(hotSearch.getContent());
            count.setText(ResUtil.getString(R.string.label_hot_search_count, DataUtil.getCounter(hotSearch.getCount())));
        }
    }
}
