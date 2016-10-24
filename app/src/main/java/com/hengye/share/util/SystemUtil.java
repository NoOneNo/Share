package com.hengye.share.util;

/**
 * Created by yuhy on 16/8/17.
 */
public class SystemUtil extends ApplicationUtil{


    public static String getPackageName() {
        return getContext().getPackageName();
//
//        try {
//            PackageManager packageManager = getContext().getPackageManager();
//            PackageInfo packageInfo = packageManager.getPackageInfo(getContext().getPackageName(), 0);
//            return packageInfo.packageName;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
    }
}
