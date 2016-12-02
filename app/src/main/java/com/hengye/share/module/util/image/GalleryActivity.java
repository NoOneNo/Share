package com.hengye.share.module.util.image;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.hengye.share.model.Topic;
import com.hengye.share.module.util.FragmentActivity;
import com.hengye.share.module.profile.TopicGalleryFragment;
import com.hengye.share.ui.support.AnimationRect;

import java.util.ArrayList;

/**
 * Created by yuhy on 16/8/1.
 */
public class GalleryActivity extends FragmentActivity {

    @Override
    protected boolean setToolBar() {
        return false;
    }

    @Override
    protected boolean setCustomTheme() {
        return false;
    }

    public static void startWithIntent(Context context, String url) {
        ArrayList<String> urls = new ArrayList<>();
        urls.add(url);
        startWithIntent(context, urls, 0);
    }

    public static void startWithIntent(Context context, ArrayList<String> urls,
                                       int index) {
        startWithIntent(context, urls, index, null);
    }

    public static void startWithIntent(Context context, ArrayList<String> urls,
                                       int index, ArrayList<AnimationRect> rectList) {
        Bundle bundle = GalleryFragment.getStartArguments(urls, index, rectList);
        context.startActivity(FragmentActivity.getStartIntent(context, GalleryFragment.class, bundle, GalleryActivity.class));

        if (rectList != null && context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }


    public static void startWithIntent(Context context, ArrayList<String> urls,
                                       int index, ArrayList<AnimationRect> rectList, ArrayList<Topic> topics) {
        Bundle bundle = TopicGalleryFragment.getStartArguments(urls, index, rectList, topics);
        context.startActivity(FragmentActivity.getStartIntent(context, TopicGalleryFragment.class, bundle, GalleryActivity.class));

        if (rectList != null && context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }

    }
}
