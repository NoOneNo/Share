package com.hengye.share.module.hottopic;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.HotTopic;
import com.hengye.share.module.base.ShareRecyclerFragment;
import com.hengye.share.module.topic.TopicThemeActivity;
import com.hengye.share.module.util.FragmentActivity;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.util.ResUtil;

import java.util.ArrayList;

public class HotTopicFragment extends ShareRecyclerFragment {

    public static void start(Context context, ArrayList<HotTopic> hotTopics) {
        context.startActivity(FragmentActivity.
                getStartIntent(context, HotTopicFragment.class, getStartBundle(hotTopics)));
    }

    public static Bundle getStartBundle(ArrayList<HotTopic> hotTopics){
        Bundle bundle = new Bundle();
        bundle.putSerializable("hotTopics", hotTopics);
        return bundle;
    }

    HotTopicAdapter mAdapter;
    ArrayList<HotTopic> mHotTopics;

    @Override
    public String getTitle() {
        return ResUtil.getString(R.string.title_activity_hot_topic);
    }

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        super.handleBundleExtra(bundle);
        mHotTopics = (ArrayList<HotTopic>) bundle.getSerializable("hotTopics");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mHotTopics == null){
            finish();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRefreshEnable(false);
        setAdapter(mAdapter = new HotTopicAdapter(getContext(), mHotTopics));

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HotTopic hotTopic = mAdapter.getItem(position);
                startActivity(TopicThemeActivity.getStartIntent(getContext(), hotTopic.getTopic()));
            }
        });
    }
}
