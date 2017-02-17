package com.gdut.myproject.utils;

import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.gdut.myproject.Bean.DetailData;
import com.gdut.myproject.Bean.WeatherTotal;

/**
 * Created by Administrator on 2016/5/24.
 * 用于插入数据到SharedPreferences,判断时间是否超过1小时
 */
public class SPUtils {
    public static final String CITY="广州";
    public static void updateUUIDSP(SharedPreferences sp){

        String result = sp.getString("UUID","");
        LogUtils.Loge("sputils","uuid:"+result);
        if("".equals(result)){
            SharedPreferences.Editor editor = sp.edit();
            String uuid = UUID.randomUUID().toString().replace("-","");
            editor.putString("UUID",uuid);
            editor.commit();
        }
    }
    /**
     * 将获取到的光伏数据保存在SharedPreferences中
     * @param sp SharedPreferences
     * @param wBean 保存数据的bean类型
     * @param time 数据更新的时间
     */
    public static void updatePVSP(SharedPreferences sp, DetailData wBean , long time){

        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean("isFirstWeather", false);

        editor.putLong("天气获取时间", time);

        editor.putString("date",new SimpleDateFormat("HH:mm").format(new Date(time)));

        editor.putFloat("thisDay", wBean.getThisDay());
        editor.putFloat("thisMonth",wBean.getThisMonth() );
        editor.putFloat("thisYear", wBean.getThisYear());
        editor.putFloat("total", wBean.getTotal());

        editor.commit();

        LogUtils.Logi("updateSharePreference","已经提交");
    }

    /**
     * 将获取到的天气数据保存在SharedPreferences中
     * @param sp SharedPreferences
     * @param wBean 保存数据的bean类型
     * @param time 数据更新的时间
     */
    public static void updateWeatherSP(SharedPreferences sp, WeatherTotal wBean , long time){

        SharedPreferences.Editor editor = sp.edit();
        String updatetime = wBean.getUpdatetime();
        StringBuffer sb = new StringBuffer(updatetime);
        sb.insert(10," ");
        updatetime = sb.toString();
        editor.putBoolean("isFirstWeather", false);

        editor.putLong("天气获取时间", time);

        editor.putString("date",new SimpleDateFormat("HH:mm").format(new Date(time)));

        editor.putString("city", wBean.getCity());
        editor.putString("updatetime",updatetime);
        editor.putString("weather", wBean.getWeather());
        editor.putString("temp", wBean.getTemp());
        editor.putString("l_tmp", wBean.getTemp_l());
        editor.putString("h_tmp", wBean.getTemp_h());
        editor.putString("WD", wBean.getWind());
        editor.putString("WS", wBean.getWindspeed());
        editor.putString("pm25", wBean.getPm25());
        editor.putString("vis", wBean.getVis());
        editor.putString("sunrise", wBean.getSunrise());
        editor.putString("sunset",wBean.getSunset());

        editor.commit();

        LogUtils.Logi("updateSharePreference","已经提交");
    }

    /**
     * 判断时间是否过去一小时
     * @param sp
     * @return true:未到1小时，false:超过1小时
     */
    public static boolean judgeTime(SharedPreferences sp) {
        long now = System.currentTimeMillis();
        long last = sp.getLong("天气获取时间",now-3700*1000);
        LogUtils.Loge("SPUtil存储时间",new SimpleDateFormat("hh-mm-ss").format(new Date()));
        LogUtils.Loge("SPUtil存储时间",new SimpleDateFormat("hh-mm-ss").format(new Date(last)));
        long dtime = now - last;
        LogUtils.Loge("存储时间",dtime+"");
        if(dtime<60*60*1000){
            LogUtils.Loge("SPUtil布尔值","true");
            return true;
        }else{
            LogUtils.Loge("SPUtil布尔值","false");
            return false;
        }
    }


}
