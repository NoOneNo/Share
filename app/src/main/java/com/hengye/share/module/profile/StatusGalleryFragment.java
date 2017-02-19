package com.hengye.share.module.profile;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.Status;
import com.hengye.share.module.statusdetail.StatusDetail2Activity;
import com.hengye.share.module.util.image.GalleryFragment;
import com.hengye.share.ui.support.AnimationRect;

import java.util.ArrayList;

/**
 * Created by yuhy on 16/8/1.
 */
public class StatusGalleryFragment extends GalleryFragment {

    public static Bundle getStartArguments(ArrayList<String> urls,
                                           int index, ArrayList<AnimationRect> rectList, ArrayList<Status> topics) {
        Bundle bundle = getStartArguments(urls, index, rectList);
        bundle.putSerializable("statuses", topics);
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
        return R.layout.fragment_status_gallery;
    }

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        super.handleBundleExtra(bundle);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //父类更新内容会调用updatePage(), 需要先findViewById().
        mStatusContent = (TextView) findViewById(R.id.tv_text);
        super.onViewCreated(view, savedInstanceState);
        mStatusContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsDragging) {
                    return;
                }
                Status topic = getStatus(getCurrentPosition());
                if (topic != null) {
                    startActivity(StatusDetail2Activity.getStartIntent(getContext(), topic, false));
                }
            }
        });

        mStatusContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getViewPager().onTouchEvent(event);
                return false;
            }
        });
    }

    final ArrayList<String> urls = new ArrayList<>(StatusAlbumFragment.urls);
    final ArrayList<Status> statuses = new ArrayList<>(StatusAlbumFragment.statuses);
    TextView mStatusContent;
    boolean mIsDragging;

    @Override
    public void updatePage(int pageNo) {
        super.updatePage(pageNo);
        mStatusContent.setText(getStatusContent(pageNo));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        super.onPageScrollStateChanged(state);
        mIsDragging = state != ViewPager.SCROLL_STATE_IDLE;
    }

    protected Status getStatus(int position) {
        if (0 <= position && position < statuses.size()) {
            return statuses.get(position);
        }
        return null;
    }

    protected String getStatusContent(int position) {
        Status status = getStatus(position);
        if (status != null) {
            return status.getContent();
        }
        return null;
    }
}
