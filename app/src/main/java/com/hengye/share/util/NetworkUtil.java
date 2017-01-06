package com.hengye.share.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.provider.Settings;

public class NetworkUtil extends ApplicationUtil{

    public static void startSystemSetting(Context context){
        context.startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    public static boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return false;
        }
//        if (networkInfo.isRoaming()) {
//            return true;
//        }
        return true;
    }

    // 获取当前网络连接的类型信息
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                return networkInfo.getType();
            }
        }
        return -1;
    }

//    // 判断WIFI网络是否可用
//    public static boolean isWifiConnected(Context context) {
//        if (context != null) {
//            ConnectivityManager connectivityManager = (ConnectivityManager) context
//                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo networkInfo = connectivityManager
//                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            if (networkInfo != null) {
//                return networkInfo.isConnected();
//            }
//        }
//        return false;
//    }
//
//    // 判断MOBILE网络是否可用
//    public static boolean isMobileConnected(Context context) {
//        if (context != null) {
//            ConnectivityManager connectivityManager = (ConnectivityManager) context
//                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo networkInfo = connectivityManager
//                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//            if (networkInfo != null) {
//                return networkInfo.isConnected();
//            }
//        }
//        return false;
//    }


    public static boolean isWifiType(){
        return CURRENT_NETWORK_TYPE == ConnectivityManager.TYPE_WIFI;
    }

    private static int CURRENT_NETWORK_TYPE = -1;

    public static class ObserveNetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                CURRENT_NETWORK_TYPE = NetworkUtil.getConnectedType(context);
            }
        }
    }

    public static abstract class NetworkChangeReceiver extends BroadcastReceiver {

        abstract public void onNetworkChange(boolean isConnected);

        @Override
        public void onReceive(final Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                onNetworkChange(NetworkUtil.isConnected());
            }
        }
    }
}
