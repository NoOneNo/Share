package com.hengye.share.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import com.hengye.share.R;
import com.hengye.share.module.setting.SettingHelper;

public class IntentUtil extends ApplicationUtil {

    private IntentUtil() {

    }

    /**
     * 检测系统是否可以打开相应的intent;
     *
     * @param intent
     * @return
     */
    public static boolean resolveActivity(Intent intent) {
        return intent != null && intent.resolveActivity(getContext().getPackageManager()) != null;
    }

    public static void startActivity(Context context, Class clazz) {
        startActivity(context, new Intent(context, clazz));
    }

    public static void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity activity, Class clazz, int requestCode) {
        startActivityForResult(activity, new Intent(activity, clazz), requestCode);
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }

    public static void launchUrl(Context context, Uri uri) {
        if(SettingHelper.isUseCustomTabsUI()){
            launchUrlUseCustomTabsUI(context, uri);
        }else{
            launchUrlUseBrowserUI(context, uri);
        }
    }

    private static void launchUrlUseCustomTabsUI(Context context, Uri uri) {

        // create an intent builder
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

//        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_arrow_back_white_48dp);
//        if(drawable instanceof BitmapDrawable){
//            intentBuilder.setCloseButtonIcon(((BitmapDrawable)drawable).getBitmap());
//        }
        // Begin customizing
        // set toolbar colors
        intentBuilder
                .setToolbarColor(ThemeUtil.getColor())
                .setSecondaryToolbarColor(ThemeUtil.getColor())
                .setInstantAppsEnabled(true)
                .enableUrlBarHiding()
                .setShowTitle(true)
                .setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
                .setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right);

        // build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();

        try {
            // launch the url
            customTabsIntent.launchUrl(context, uri);
        } catch (ActivityNotFoundException anfe) {
            anfe.printStackTrace();
            ToastUtil.showToastError(R.string.label_resolve_url_activity_fail);
        }
    }

    private static void launchUrlUseBrowserUI(Context context, Uri uri){
        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (IntentUtil.resolveActivity(intent)) {
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException anfe) {
                anfe.printStackTrace();
                ToastUtil.showToastError(R.string.label_resolve_url_activity_fail);
            }
        } else {
            ToastUtil.showToastError(R.string.label_resolve_url_activity_fail);
        }
    }
}
