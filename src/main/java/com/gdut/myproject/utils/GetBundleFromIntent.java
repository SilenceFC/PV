package com.gdut.myproject.utils;

import android.content.Intent;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import com.gdut.myproject.Bean.NotifyEntity;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/9/24.
 * 解析推送内容，按照标题、内容、类型来存储推送消息
 */
public class GetBundleFromIntent {
    private static  Bundle mBundle;
    private static  NotifyEntity notify ;

    /**
     * 解析推送内容，按照标题、内容、类型来存储推送消息
     * @param intent
     * @return
     */
    public static NotifyEntity getNotify(Intent intent){
        notify = new NotifyEntity();
        mBundle = getBundle(intent);
        notify.setTitle(mBundle.getString(JPushInterface.EXTRA_TITLE));
        notify.setContent(mBundle.getString(JPushInterface.EXTRA_MESSAGE));
        notify.setType(parseJsonType(mBundle.getString(JPushInterface.EXTRA_EXTRA)));
        notify.setExtra(parseJsonExtra(mBundle.getString(JPushInterface.EXTRA_EXTRA)));
        return notify;
    }

    public static Bundle getBundle(Intent intent){
        mBundle = intent.getExtras();
        return mBundle;
    }

   private static String parseJsonExtra(String s){
       try {
           JSONObject obj;
           String extra1;
           if(s!=null&& !"".equals(s)){
               obj = new JSONObject(s);
               extra1 = obj.getString("type").substring(1);
           }else {
               extra1 = "无此类型";
           }
           return extra1;
       } catch (JSONException e) {
           e.printStackTrace();
           LogUtils.Loge("从intent中获取Extra数据","json解析错误");
           return "";
       }
    }

    private static int parseJsonType(String s){
        try {
            JSONObject obj;
            int type;
            if(s!=null&& !"".equals(s)){
                obj = new JSONObject(s);
                type = Integer.parseInt(obj.getString("type").substring(0,1));
            }else {
                type = 10;
            }
            return type;
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.Loge("从intent中获取Type数据","json解析错误");
            return 10;
        }
    }
}
