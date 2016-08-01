package com.hengye.share.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.hengye.share.R;
import com.hengye.share.ui.base.BaseFragment;
import com.hengye.share.util.AnimationUtil;

import java.io.File;

public class ImageWebViewFragment extends BaseFragment {

    public static ImageWebViewFragment newInstance(String path, boolean animationIn) {
        ImageWebViewFragment fragment = new ImageWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putBoolean("animationIn", animationIn);
        fragment.setArguments(bundle);
        return fragment;
    }

    String mPath;
    boolean mAnimateIn;

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        mPath = bundle.getString("path");
        mAnimateIn = bundle.getBoolean("animationIn");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_webview, null);

        mWebView = (WebView) view.findViewById(R.id.web_view);

        mWebView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mWebView.setVisibility(View.INVISIBLE);
        mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);

//        LongClickListener longClickListener = ((ContainerFragment) getParentFragment())
//                .getLongClickListener();
//        large.setOnLongClickListener(longClickListener);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);


        if (mAnimateIn) {
            showContent(mPath, mWebView);
        } else {
            /**
             * webview will influence other imageview animation performance
             */
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    showContent(mPath, mWebView);
                }
            }, AnimationUtil.GALLERY_WEBVIEW_ANIMATION_DURATION);
        }
        return view;
    }

    private void showContent(String path, WebView large) {
        File file = new File(path);

        String str1 = "file://" + file.getAbsolutePath()
                .replace("/mnt/sdcard/", "/sdcard/");
        String str2 =
                "<html>\n<head>\n     <style>\n          html,body{background:transparent;margin:0;padding:0;}          *{-webkit-tap-highlight-color:rgba(0, 0, 0, 0);}\n     </style>\n     <script type=\"text/javascript\">\n     var imgUrl = \""
                        + str1 + "\";" + "     var objImage = new Image();\n"
                        + "     var realWidth = 0;\n" + "     var realHeight = 0;\n" + "\n"
                        + "     function onLoad() {\n"
                        + "          objImage.onload = function() {\n"
                        + "               realWidth = objImage.width;\n"
                        + "               realHeight = objImage.height;\n" + "\n"
                        + "               document.gagImg.src = imgUrl;\n"
                        + "               onResize();\n" + "          }\n"
                        + "          objImage.src = imgUrl;\n" + "     }\n" + "\n"
                        + "     function onResize() {\n" + "          var scale = 1;\n"
                        + "          var newWidth = document.gagImg.width;\n"
                        + "          if (realWidth > newWidth) {\n"
                        + "               scale = realWidth / newWidth;\n"
                        + "          } else {\n"
                        + "               scale = newWidth / realWidth;\n" + "          }\n"
                        + "\n"
                        + "          hiddenHeight = Math.ceil(30 * scale);\n"
                        + "          document.getElementById('hiddenBar').style.height = hiddenHeight + \"px\";\n"
                        + "          document.getElementById('hiddenBar').style.marginTop = -hiddenHeight + \"px\";\n"
                        + "     }\n" + "     </script>\n" + "</head>\n"
                        + "<body onload=\"onLoad()\" onresize=\"onResize()\" onclick=\"Android.toggleOverlayDisplay();\">\n"
                        + "     <table style=\"width: 100%;height:100%;\">\n"
                        + "          <tr style=\"width: 100%;\">\n"
                        + "               <td valign=\"middle\" align=\"center\" style=\"width: 100%;\">\n"
                        + "                    <div style=\"display:block\">\n"
                        + "                         <img name=\"gagImg\" src=\"\" width=\"100%\" style=\"\" />\n"
                        + "                    </div>\n"
                        + "                    <div id=\"hiddenBar\" style=\"position:absolute; width: 100%; background: transparent;\"></div>\n"
                        + "               </td>\n" + "          </tr>\n" + "     </table>\n"
                        + "</body>\n" + "</html>";
        large.loadDataWithBaseURL("file:///android_asset/", str2, "text/html", "utf-8",
                null);
        large.setVisibility(View.VISIBLE);
    }
}
