package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.view.NetworkImageViewPlus;
import com.hengye.share.R;

import org.w3c.dom.Text;

import java.util.List;

public class SearchUserAdapter extends CommonAdapter<String, SearchUserAdapter.MainViewHolder> {

    public SearchUserAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_search_user, parent, false));
    }

    public static class MainViewHolder extends CommonAdapter.ItemViewHolder<String> {

        NetworkImageViewPlus avatar;
        TextView username;

        public MainViewHolder(View v) {
            super(v);

            avatar = (NetworkImageViewPlus) v.findViewById(R.id.iv_avatar);
            username = (TextView) v.findViewById(R.id.tv_username);
        }

        @Override
        public void bindData(Context context, String string) {
            username.setText(string);
        }
    }
}