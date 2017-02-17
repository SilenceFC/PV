package com.gdut.myproject.utils;

import android.content.SharedPreferences;

import com.gdut.myproject.Bean.API;
import com.gdut.myproject.PVMonitor.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/24.
 * 设置页面背景,主页图片,设备工作情况,比较时间是否在同一范围内
 */
public class SetBackGround {
    /**
     * 页面背景固定为蓝色晴天背景
     * @param sp
     * @return
     */
    public static int setLlBackground(SharedPreferences sp) {
        int resId = R.mipmap.static_bg_sunny;
//        String weather = sp.getString("weather","晴");
//        String sunrise = sp.getString("sunrise","00:00");
//        String sunset = sp.getString("sunset","00:00");
//        if (weather.indexOf("晴") != -1) {
//            if (timeCompare(sunrise, sunset)) {
//                resId = R.mipmap.static_bg_sunny;
//            } else {
//                resId = R.mipmap.static_bg_sunny_night;
//            }
//        } else if (weather.indexOf("多云") != -1) {
//            if (timeCompare(sunrise, sunset)) {
//                resId = R.mipmap.static_bg_cloudy;
//            } else {
//                resId = R.mipmap.static_bg_cloudy_night;
//            }
//        } else if (weather.indexOf("雨") != -1) {
//            if (timeCompare(sunrise, sunset)) {
//                resId = R.mipmap.static_bg_rain;
//            } else {
//                resId = R.mipmap.static_bg_rain_night;
//            }
//        } else if (weather.indexOf("雪") != -1) {
//            if (timeCompare(sunrise, sunset)) {
//                resId = R.mipmap.static_bg_snow;
//            } else {
//                resId = R.mipmap.static_bg_snow_night;
//            }
//        }
        return resId;
    }

    public static int setImageBackground(SharedPreferences sp) {
        int resId = R.mipmap.weather_sunny;
        String weather = sp.getString("weather","晴");

        if (weather.indexOf("晴") != -1) {

                resId = R.mipmap.weather_sunny;

        } else if (weather.indexOf("多云") != -1) {
                resId = R.mipmap.weather_cloud;
        } else if (weather.indexOf("雨") != -1) {
                resId = R.mipmap.weather_rain;
        } else if (weather.indexOf("雪") != -1) {
            resId = R.mipmap.weather_snow;
        }
        return resId;
    }

    public static int setDeviceStateBackground(SharedPreferences sp,int type){
        int resId = R.mipmap.main2;
        LogUtils.Loge("设置状态背景", "type:"+type);
        switch (type){
            case 0:
                int state = sp.getInt("state", API.ERROR_NET);
                LogUtils.Loge("设置状态背景", "state:"+state);
                if(state == API.ERROR_NET || state == API.ERROR_SERVER){
                }else{
                    int mpptstate = sp.getInt("MpptState",0);
                    if(mpptstate == 1){
                        resId = R.mipmap.main1;
                    }
                }
            break;
            case 1:
                int state1 = sp.getInt("state", API.ERROR_NET);
                LogUtils.Loge("设置状态背景", "state1:"+state1);
                if(state1 == API.ERROR_NET || state1 == API.ERROR_SERVER){
                }else{
                    int interverstate = sp.getInt("InterverState",0);
                    if(interverstate == 1){
                        resId = R.mipmap.main1;
                    }
                }
                break;
        }

        return resId;
    }

    /**
     * 比较时间是否在两个传入值范围内
     * @param sunrise
     * @param sunset
     * @return true:在时间范围内，
     *          false:在实践范围外
     */
    private static boolean timeCompare(String sunrise, String sunset) {
        boolean isEarly = false;
        SimpleDateFormat sdf = new SimpleDateFormat("HH-mm");
        String nowTime = sdf.format(new Date()).replace("-", ":");
        LogUtils.Loge("时间比对", "nowTime:" + nowTime + " sunrise:" + sunrise+" sunset:"+sunset);

        if(sunrise.equals("00:00")&&sunset.equals("00:00")){
            return true;
        }

        if (nowTime.compareTo(sunrise) > 0 && nowTime.compareTo(sunset) < 0 ) {
            isEarly = true;
            LogUtils.Loge("测试1", "nowTime:" + nowTime + " isEarly" + isEarly);
        }
        return isEarly;
    }
}
