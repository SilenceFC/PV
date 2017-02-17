package com.gdut.myproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gdut.myproject.greendao.DayData;
import com.gdut.myproject.greendao.InstantData;
import com.gdut.myproject.greendao.MonthData;

import java.util.List;

import com.gdut.myproject.Bean.DetailData;
import com.gdut.myproject.Bean.PVBean;

/**
 * 用于将光伏数据解析出来并分别存储在对应的SharedPrefence、SQLite中
 * 涉及到数据库操作，建议另开线程进行处理。此类在minaIoHandler中被用到
 */
public class ParseUtil {
    private static Gson mGson;
    private static boolean isTrue;

    private static List<InstantData> list0 ;
    private static List<DetailData> list1 ;
    private static List<DayData> list2 ;
    private static List<MonthData> list3 ;
    private static PVBean pv;

    private ParseUtil(){
        mGson = new Gson();
        isTrue = false;
    }

    private static class ParseHolder{
        private static final ParseUtil INSTANCE = new ParseUtil();
    }
    public static ParseUtil getInstance(){
        return ParseHolder.INSTANCE;
    }

    /**
     * 传入获取的光伏数据，
     * @param message
     * @return 解析完成返回true,不然返回false
     */
    public boolean  parseData(Context context, String message){
        if(message!=null && !"".equals(message)){
            isTrue = true;
            pv = mGson.fromJson(message,new TypeToken<PVBean>(){}.getType());
            List<InstantData> list0 = pv.getData0();
            List<DetailData> list1 = pv.getData1();
            List<DayData> list2 = pv.getData2();
            List<MonthData> list3 = pv.getData3();

            saveDataInSP(context,list1.get(0));

            saveDataInSQL(context,list0,list2,list3);

            LogUtils.Loge("ParseData","解析完成");
        }

        return isTrue;
    }

    /**
     * 将DetailData数据存放在sp中，存放完成返回true
     * @param context 上下文
     * @param data  DetailData
     * @return
     */
    private static boolean saveDataInSP(Context context,DetailData data){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        SPUtils.updatePVSP(sp,data,System.currentTimeMillis());
        return true;
    }

    /**
     * 存入数据，无视数据个数
     * @param context
     * @param instantlist
     * @param dayList
     * @param monthList
     */
    private static void saveDataInSQL(Context context,List<InstantData> instantlist,List<DayData> dayList,List<MonthData> monthList){
           DbService instance = DbService.getInstance();
           instance.saveInstantNoteLists(instantlist);
           instance.saveDayNoteLists(dayList);
           instance.saveMonthNoteLists(monthList);
    }
}
