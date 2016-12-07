package com.hengye.share.module.profile;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.module.topicdetail.TopicDetail2Activity;
import com.hengye.share.module.util.image.GalleryFragment;
import com.hengye.share.ui.support.AnimationRect;

import java.util.ArrayList;

/**
 * Created by yuhy on 16/8/1.
 */
public class TopicGalleryFragment extends GalleryFragment {

    public static Bundle getStartArguments(ArrayList<String> urls,
                                           int index, ArrayList<AnimationRect> rectList, ArrayList<Topic> topics) {
        Bundle bundle = getStartArguments(urls, index, rectList);
        bundle.putSerializable("topics", topics);
        return bundle;
    }

    @Override
    protected int getImageSize() {
        return urls.size();
    }

    @Override
    protected String getImageUrl(int position) {
        if (0 <= position && position < urls.size()) {
            return urls.get(position);
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
        //父类更新内容会调用updatePage(), 需要先findViewById().
        mTopicContent = (TextView) findViewById(R.id.tv_text);
        super.onViewCreated(view, savedInstanceState);
        mTopicContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsDragging) {
                    return;
                }
                Topic topic = getTopic(getCurrentPosition());
                if (topic != null) {
                    startActivity(TopicDetail2Activity.getStartIntent(getContext(), topic, false));
                }
            }
        });

        mTopicContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getViewPager().onTouchEvent(event);
                return false;
            }
        });
    }

    final ArrayList<String> urls = new ArrayList<>(TopicAlbumFragment.urls);
    final ArrayList<Topic> topics = new ArrayList<>(TopicAlbumFragment.topics);
    TextView mTopicContent;
    boolean mIsDragging;

    @Override
    public void updatePage(int pageNo) {
        super.updatePage(pageNo);
        mTopicContent.setText(getTopicContent(pageNo));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        super.onPageScrollStateChanged(state);
        mIsDragging = state != ViewPager.SCROLL_STATE_IDLE;
    }

    protected Topic getTopic(int position) {
        if (0 <= position && position < topics.size()) {
            return topics.get(position);
        }
        return null;
    }

    protected String getTopicContent(int position) {
        Topic topic = getTopic(position);
        if (topic != null) {
            return topic.getContent();
        }
        return null;
    }
}
