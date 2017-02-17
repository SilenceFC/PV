package com.gdut.myproject.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2016/11/2.
 * 用于确定网络连接、wifi连接情况。
 */
public class NetWorkUtil {
    public static boolean isNetConnect(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        return info!=null && info.isAvailable();

    }

    public static boolean isWifiConnect(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info.getType() == ConnectivityManager.TYPE_WIFI;

    }

}
