package com.gdut.myproject.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 日志管理工具，用于手动控制代码中的LOG
 */
public class LogUtils {
   static  boolean isOpen = true;

    public static void Loge(String TAG,String content){
        if(isOpen){
            Log.e(TAG, content);
        }
    }

    public static void Logi(String TAG,String content){
        if(isOpen){
            Log.i(TAG, content);
        }
    }

    public static void showToast(Context context,String content,int time){
        int duration = (time==0)?Toast.LENGTH_SHORT:Toast.LENGTH_LONG;
        Toast.makeText(context,content,duration).show();
    }
}
