package com.hengye.share.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hengye.share.R;

public class ToastUtil extends ApplicationUtil{

    public static void showSnackBar(View view, CharSequence text){
        getSnackBar(view, text, null, null).show();
    }

    public static void showSnackBar(View view, @StringRes int resId){
        getSnackBar(view, getString(resId), null, null).show();
    }

    public static void showSnackBar(View view, @StringRes int textResId, @StringRes int actionResId, View.OnClickListener listener){
        getSnackBar(view, getString(textResId), getString(actionResId), listener).show();
    }

    public static void showSnackBar(View view, CharSequence text, CharSequence action, View.OnClickListener listener){
        getSnackBar(view, text, action, listener).show();
    }

    private static Snackbar getSnackBar(View view, CharSequence text, CharSequence action, View.OnClickListener listener){
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        if(action != null){
            snackbar.setAction(action, listener);
        }
        return snackbar;
    }

    private static String getString(@StringRes int resId){
        return ResUtil.getString(resId);
    }

    private static Toast mToast;

    private static Toast getToast(){
        if(mToast == null){
            mToast = createCustomToast(getContext());
        }
        return mToast;
    }

    public static void showLoadErrorToast(){
        showToast(R.string.tip_load_error);
    }

    public static void showNetWorkErrorToast(){
        showToastWarning(R.string.tip_no_network);
    }

    public static void showToBeAchievedToast(){
        showToast("该功能暂未实现 >_<");
    }

    public static void showToast(CharSequence text){
//        Toast toast = getToast();
//        toast.setText(text);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.show();
        showToastInfo(text);
    }

    public static void showToast(@StringRes int resId){
        showToast(ResUtil.getString(resId));
    }

    public static void showToastNormal(@StringRes int resId){
        showToastNormal(ResUtil.getString(resId));
    }

    public static void showToastNormal(CharSequence text){
        showCustomToast(text, 0, 0);
    }

    public static void showToastInfo(@StringRes int resId){
        showToastInfo(ResUtil.getString(resId));
    }

    public static void showToastInfo(CharSequence text){
        showCustomToast(text, R.drawable.ic_info_outline_white_48dp, INFO_COLOR);
    }

    public static void showToastError(@StringRes int resId){
        showToastError(ResUtil.getString(resId));
    }

    public static void showToastError(CharSequence text){
        showCustomToast(text, R.drawable.ic_clear_white_48dp, ERROR_COLOR);
    }

    public static void showToastSuccess(@StringRes int resId){
        showToastSuccess(ResUtil.getString(resId));
    }

    public static void showToastSuccess(CharSequence text){
        showCustomToast(text, R.drawable.ic_check_white_48dp, SUCCESS_COLOR);
    }

    public static void showToastWarning(@StringRes int resId){
        showToastWarning(ResUtil.getString(resId));
    }

    public static void showToastWarning(CharSequence text){
        showCustomToast(text, R.drawable.ic_error_outline_white_48dp, WARNING_COLOR);
    }


    private static Toast createCustomToast(Context context){
        Toast currentToast = new Toast(context);
        View toastLayout = View.inflate(context, R.layout.toast_layout, null);
        currentToast.setView(toastLayout);
        return currentToast;
    }

    private static final @ColorInt int DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");

    private static final @ColorInt int ERROR_COLOR = Color.parseColor("#D50000");
    private static final @ColorInt int INFO_COLOR = Color.parseColor("#3F51B5");
    private static final @ColorInt int SUCCESS_COLOR = Color.parseColor("#50BBA7");
    private static final @ColorInt int WARNING_COLOR = Color.parseColor("#FFA900");

    private static final String TOAST_TYPEFACE = "sans-serif-condensed";

    private static void showCustomToast(CharSequence text, @DrawableRes int iconResId, @ColorInt int tintColor){
        showCustomToast(getContext(), text, iconResId,
                DEFAULT_TEXT_COLOR, tintColor, Toast.LENGTH_SHORT);
    }

    private static void showCustomToast(@NonNull Context context, CharSequence text, @DrawableRes int iconResId,
                                        @ColorInt int textColor, @ColorInt int tintColor, int duration) {
        initCustomToast(context, text, iconResId, textColor, tintColor, duration).show();
    }

    private static Toast initCustomToast(@NonNull Context context, CharSequence text, @DrawableRes int iconResId,
                                         @ColorInt int textColor, @ColorInt int tintColor, int duration) {
        Toast currentToast = getToast();
        View toastLayout = currentToast.getView();
        ImageView toastIcon = (ImageView) toastLayout.findViewById(R.id.toast_icon);
        TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
        Drawable drawableFrame = context.getDrawable(R.drawable.toast_frame);
        if (tintColor != 0 && drawableFrame != null) {
            drawableFrame.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        }
        toastLayout.setBackground(drawableFrame);

        if(toastIcon != null) {
            if (iconResId != 0) {
                toastIcon.setBackgroundResource(iconResId);
            } else {
                toastIcon.setVisibility(View.GONE);
            }
        }

        if(toastTextView != null) {
            toastTextView.setTextColor(textColor);
            toastTextView.setText(text);
            toastTextView.setTypeface(Typeface.create(TOAST_TYPEFACE, Typeface.NORMAL));
        }

        currentToast.setDuration(duration);
        return currentToast;
    }
}
