package com.hengye.share.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.ui.activity.TopicDetailActivity;
import com.hengye.share.ui.support.AnimationRect;

import java.util.ArrayList;

/**
 * Created by yuhy on 16/8/1.
 */
public class TopicGalleryFragment extends GalleryFragment{

    public static Bundle getStartArguments(ArrayList<String> urls,
                                           int index, ArrayList<AnimationRect> rectList, ArrayList<Topic> topics) {
        Bundle bundle = getStartArguments(urls, index, rectList);
        bundle.putSerializable("topics", topics);
        return bundle;
    }

    @Override
    protected int getImageSize() {
        if(TopicAlbumFragment.urls != null){
            return TopicAlbumFragment.urls.size();
        }
        return 0;
    }

    @Override
    protected String getImageUrl(int position) {
        if(TopicAlbumFragment.urls != null && 0 <= position && position < TopicAlbumFragment.urls.size()){
            return TopicAlbumFragment.urls.get(position);
        }
        return null;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_topic_gallery;
    }

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        super.handleBundleExtra(bundle);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mTopicContent = (TextView) findViewById(R.id.tv_text);
        super.onViewCreated(view, savedInstanceState);
        mTopicContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Topic topic = getTopic(getCurrentPosition());
                if(topic != null) {
                    startActivity(TopicDetailActivity.getStartIntent(getContext(), topic, false));
                }
            }
        });
    }

    TextView mTopicContent;

    @Override
    public void updatePage(int pageNo) {
        super.updatePage(pageNo);
        mTopicContent.setText(getTopicContent(pageNo));
    }

    protected Topic getTopic(int position){
        if(TopicAlbumFragment.topics != null && 0 <= position && position < TopicAlbumFragment.topics.size()){
            return TopicAlbumFragment.topics.get(position);
        }
        return null;
    }

    protected String getTopicContent(int position){
        Topic topic = getTopic(position);
        if(topic != null){
            return topic.getContent();
        }
        return null;
    }
}
