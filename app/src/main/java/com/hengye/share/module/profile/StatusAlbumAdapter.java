package com.hengye.share.module.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.ui.widget.image.SuperImageView;

import java.util.List;

public class StatusAlbumAdapter extends CommonAdapter<String> {

    public StatusAlbumAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public StatusAlbumViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new StatusAlbumViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_status_album, parent, false));
    }

    public static class StatusAlbumViewHolder extends ItemViewHolder<String> {

        SuperImageView superImageView;

        public StatusAlbumViewHolder(View v) {
            super(v);
            superImageView = (SuperImageView) findViewById(R.id.image);
        }

        @Override
        public void bindData(Context context, String string, int position) {
            superImageView.setImageUrl(string);
        }
    }
}
