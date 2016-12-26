package com.gdut.myproject.utils;

import android.content.Intent;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import com.gdut.myproject.Bean.NotifyEntity;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/9/24.
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
        notify.setExtra(parseJson(mBundle.getString(JPushInterface.EXTRA_EXTRA)));
        return notify;
    }

    public static Bundle getBundle(Intent intent){
        mBundle = intent.getExtras();
        return mBundle;
    }

   private static String parseJson(String s){
       try {
           JSONObject obj;
           String type;
           if(s!=null&& !"".equals(s)){
               obj = new JSONObject(s);
               type = obj.getString("type");
           }else {
               type = "无此类型";
           }
           return type;
       } catch (JSONException e) {
           e.printStackTrace();
           LogUtils.Loge("从intent中获取数据","json解析错误");
           return "";
       }

    }
}
