package com.gdut.myproject.utils;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gdut.myproject.greendao.DaoMaster;
import com.gdut.myproject.greendao.DaoSession;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/5/25.
 */
public class ContextUtils extends Application {
    private static SQLiteDatabase db;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private  static ContextUtils instance;
    private HashMap<Integer,Float> map = new HashMap<>();

    public HashMap<Integer, Float> getMap() {
        return map;
    }

    public void putMap(Integer key, Float value) {
       map.put(key,value);
    }

    public static ContextUtils getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        if(instance == null) instance = this;

    }

    public static  SQLiteDatabase getDb(Context context){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "data.db", null);
            db = helper.getWritableDatabase();
        }
        return db;
    }
    /**
     * 取得DaoMaster
     *
     * @param context        上下文
     * @return               DaoMaster
     */
    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "data.db", null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @param context        上下文
     * @return               DaoSession
     */
    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
