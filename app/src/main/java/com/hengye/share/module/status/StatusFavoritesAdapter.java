package com.hengye.share.module.status;

import android.content.Context;
import android.view.ViewGroup;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.model.StatusFavorites;

import java.util.List;

public class StatusFavoritesAdapter extends CommonAdapter<StatusFavorites.StatusFavorite> {

    public StatusFavoritesAdapter(Context context, List<StatusFavorites.StatusFavorite> data) {
        super(context, data);
    }

    @Override
    public StatusAdapter.StatusDefaultViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new StatusAdapter.StatusDefaultViewHolder(inflate(R.layout.item_status_total, parent));
    }

    @Override
    public void onBindBasicItemView(ItemViewHolder holder, int position) {
        StatusAdapter.StatusDefaultViewHolder topicHolder = (StatusAdapter.StatusDefaultViewHolder) holder;
        topicHolder.bindData(getContext(), getItem(position).getStatus(), position);

    }
}
