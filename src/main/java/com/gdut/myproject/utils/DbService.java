package com.gdut.myproject.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.gdut.myproject.greendao.AlarmRecord;
import com.gdut.myproject.greendao.AlarmRecordDao;
import com.gdut.myproject.greendao.DaoSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gdut.myproject.greendao.DayData;
import com.gdut.myproject.greendao.DayDataDao;
import com.gdut.myproject.greendao.InstantData;
import com.gdut.myproject.greendao.InstantDataDao;
import com.gdut.myproject.greendao.MonthData;
import com.gdut.myproject.greendao.MonthDataDao;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Administrator on 2016/5/27.
 * 由GreenDao提供的操作数据库API
 */
public class DbService {
    private static final String TAG = DbService.class.getSimpleName();
    private static DbService instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private InstantDataDao instantDao;
    private MonthDataDao monthDataDao;
    private DayDataDao dayDataDao;
    private AlarmRecordDao alarmRecordDao;

    private DbService() {
    }

    /**
     * 采用单例模式
     * @return dbservice
     */
    public static DbService getInstance() {
        if (instance == null) {
            instance = new DbService();
            if (appContext == null) {
                appContext = ContextUtils.getInstance().getApplicationContext();
            }
            instance.mDaoSession = ContextUtils.getDaoSession(appContext);
            instance.instantDao = instance.mDaoSession.getInstantDataDao();
            instance.dayDataDao = instance.mDaoSession.getDayDataDao();
            instance.monthDataDao = instance.mDaoSession.getMonthDataDao();
            instance.alarmRecordDao = instance.mDaoSession.getAlarmRecordDao();
        }
        return instance;
    }

    /**
     * 根据用户id,取出用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    public InstantData loadInstantNote(long id) {
        if (!TextUtils.isEmpty(id + "")) {
            return instantDao.load(id);
        }
        return null;
    }

    public DayData loadDayNote(long id) {
        if (!TextUtils.isEmpty(id + "")) {
            return dayDataDao.load(id);
        }
        return null;
    }

    public MonthData loadMonthNote(long id) {
        if (!TextUtils.isEmpty(id + "")) {
            return monthDataDao.load(id);
        }
        return null;
    }

    public AlarmRecord loadAlarmNote(long id) {
        if (!TextUtils.isEmpty(id + "")) {
            return alarmRecordDao.load(id);
        }
        return null;
    }

    /**
     * 取出所有数据
     *
     * @return 所有数据信息
     */
    public List<InstantData> loadInstantAllNote() {
        return instantDao.loadAll();
    }

    public List<DayData> loadDayAllNote() {
        return dayDataDao.loadAll();
    }

    public List<MonthData> loadMonthAllNote() {
        return monthDataDao.loadAll();
    }

    public List<AlarmRecord> loadAlarmAllNote() {
        return alarmRecordDao.loadAll();
    }

    /**
     * 根据查询条件,返回数据列表
     *
     * @param where  条件
     * @param params 参数
     * @return 数据列表
     */
    public List<InstantData> queryInstantNote(String where, String... params) {
        return instantDao.queryRaw(where, params);
    }


    /**
     * 根据用户信息,插入或修改信息
     *
     * @param data 用户信息
     * @return 插入或修改的用户id
     */
    public long saveInstantNote(InstantData data) {
        return instantDao.insertOrReplace(data);
    }

    public long saveDayNote(DayData data) {
        return dayDataDao.insertOrReplace(data);
    }

    public long saveMonthNote(MonthData data) {
        return monthDataDao.insertOrReplace(data);
    }

    public long savaAlarmNote(AlarmRecord data){ return alarmRecordDao.insertOrReplace(data);}

    /**
     * 批量插入或修改用户信息
     *
     * @param list 用户信息列表
     */
    public void saveInstantNoteLists(final List<InstantData> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        instantDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    InstantData data = list.get(i);
                    instantDao.insertOrReplace(data);
                }
            }
        });

    }

    public void saveDayNoteLists(final List<DayData> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        dayDataDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    DayData data = list.get(i);
                    dayDataDao.insertOrReplace(data);
                }
            }
        });

    }

    public void saveMonthNoteLists(final List<MonthData> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        monthDataDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    MonthData data = list.get(i);
                    LogUtils.Loge("monthdata个数",data.getMonth());
                    monthDataDao.insertOrReplace(data);
                }
            }
        });

    }

    public void saveAlarmNoteLists(final List<AlarmRecord> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        alarmRecordDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    AlarmRecord data = list.get(i);
                    alarmRecordDao.insertOrReplace(data);
                }

            }
        });

    }

    /**
     *
     * @param time 查询条件
     * @return
     */
    public boolean isDataExist(String time){
        QueryBuilder qb = instantDao.queryBuilder();
        qb.where(InstantDataDao.Properties.Time.like(time));
        List<InstantData> list = qb.list();
        Log.e(TAG, "data有" + list.size() + "条数据");
        if(list.size()==0){
            return false;
        }else{
            return true;
        }
    }

    public boolean isDayDataExist(String day){
        QueryBuilder qb = dayDataDao.queryBuilder();
        day="%"+day+"%";
        qb.where(DayDataDao.Properties.Day.like(day));
        List<DayData> list = qb.list();
        Log.e(TAG, "DayData BOOLEAN 有"+list.size()+"条数据" );
        for (int i = 0; i <list.size(); i++) {
            Log.e(TAG, "day:" + list.get(i).getDay() + "  daytotal:" + list.get(i).getDaytotal());
        }
        if(list.size()==0){
            return false;
        }else{
            return true;
        }
    }

    public List<Long> getExistedDayId(String day){
        QueryBuilder qb = dayDataDao.queryBuilder();
        day="%"+day+"%";
        qb.where(DayDataDao.Properties.Day.like(day));
        List<DayData> list = qb.list();
        List<Long> list1 = new ArrayList<>();
        Log.e(TAG, "DayData有"+list.size()+"条数据" );
        for (int i = 0; i <list.size(); i++) {
            Log.e(TAG, "id = " + list.get(i).getId());
           list1.add(list.get(i).getId());
        }

        return list1;
    }

    public List<Long> getExistedMonthId(String month){
        QueryBuilder qb = monthDataDao.queryBuilder();
        month = "%"+month+"%";
        qb.where(MonthDataDao.Properties.Month.like(month));
        List<MonthData> list = qb.list();
        List<Long> list1 = new ArrayList<>();
        Log.e(TAG, "MonthData有"+list.size()+"条数据" );
        for (int i = 0; i <list.size(); i++) {
            Log.e(TAG, "id = " + list.get(i).getId());
            list1.add(list.get(i).getId());
        }
        return list1;
    }

    public boolean isMonthDataExist(String month){
        QueryBuilder qb = monthDataDao.queryBuilder();
        month = "%"+month+"%";
        qb.where(MonthDataDao.Properties.Month.like(month));
        List<MonthData> list = qb.list();
        Log.e(TAG, "monthdata Boolean有"+list.size()+"条数据" );
        if(list.size()==0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 按照输入条件获取每时的功率数据
     */
    public List<InstantData> getSpecifiedData(){
        QueryBuilder qb = instantDao.queryBuilder();
        String day = "%"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"%";
        LogUtils.Loge("<--获取日期-->","day:"+day);
        qb.where(InstantDataDao.Properties.Time.like(day));
        List<InstantData> list = qb.list();
        return list;
    }

    /**
     * 按照输入条件获取每日数据
     */
    public List<DayData> getSpecifiedDayData(){
        QueryBuilder qb = dayDataDao.queryBuilder();
        String day = "%"+new SimpleDateFormat("yyyy-MM").format(new Date())+"%";
        LogUtils.Loge("<--获取日期-->","day:"+day);
        qb.where(DayDataDao.Properties.Day.like(day));
        List<DayData> list = qb.list();
//        for (int i = 0; i < list.size(); i++) {
//            LogUtils.Loge("<--获取数据-->","day:"+list.get(i).getDay()+"  daytotal:"+list.get(i).getDaytotal());
//        }
        return list;
    }

    public List<AlarmRecord> getSpecifiedAlarmData(){
       List<AlarmRecord> list = alarmRecordDao.loadAll();
        return list;
    }

    /**
     * 按照输入条件获取每月数据，只取12个数据
     */
    public List<MonthData> getSpecifiedMonthData(){
        List<MonthData> list = monthDataDao.loadAll();
        List<MonthData> list1;
        int size = list.size();
        if(size<=12){
            return list;
        }else {
            list1 = new ArrayList<>();
            for (int i = size-12; i <size; i++) {
                list1.add(list.get(i));
            }
            return list1;
        }
//        for (int i = 0; i < list.size(); i++) {
//            LogUtils.Loge("<--获取数据-->","day:"+list.get(i).getDay()+"  daytotal:"+list.get(i).getDaytotal());
//        }
    }



    /**
     * 删除所有数据
     */
    public void deleteInstantAllNote() {
        instantDao.deleteAll();
    }

    public void deleteDayAllNote() {
        dayDataDao.deleteAll();
    }

    public void deleteMonthAllNote() {
        monthDataDao.deleteAll();
    }
    public void deleteAlarmAllNote(){alarmRecordDao.deleteAll();}


    /**
     * 根据id,删除数据
     *
     * @param id 用户id
     */
    public void deleteNote(long id) {
        instantDao.deleteByKey(id);
        Log.i(TAG, "delete");
    }

    /**
     * 根据用户类,删除信息
     *
     * @param data 用户信息类
     */
    public void deleteNote(InstantData data) {
        instantDao.delete(data);
    }

    public void updateData(InstantData data){
        instantDao.update(data);
    }
    public void updateDayData(DayData data){
        dayDataDao.update(data);
    }
    public void updateMonthData(MonthData data){
        monthDataDao.update(data);
    }
}

