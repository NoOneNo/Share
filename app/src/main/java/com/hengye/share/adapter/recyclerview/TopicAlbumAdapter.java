package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.R;
import com.hengye.share.ui.widget.SuperImageView;

import java.util.List;

public class TopicAlbumAdapter extends CommonAdapter<String, TopicAlbumAdapter.MainViewHolder> {

    public TopicAlbumAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_topic_album, parent, false));
    }

    public static class MainViewHolder extends CommonAdapter.ItemViewHolder<String> {

        SuperImageView superImageView;

        public MainViewHolder(View v) {
            super(v);
            superImageView = (SuperImageView) findViewById(R.id.image);
        }

        @Override
        public void bindData(Context context, String string, int position) {
            superImageView.setImageUrl(string);
        }
    }
}
