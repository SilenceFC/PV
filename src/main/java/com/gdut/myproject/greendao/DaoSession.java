package com.gdut.myproject.greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig instantDataDaoConfig;
    private final DaoConfig dayDataDaoConfig;
    private final DaoConfig monthDataDaoConfig;
    private final DaoConfig alarmRecordDaoConfig;

    private final InstantDataDao instantDataDao;
    private final DayDataDao dayDataDao;
    private final MonthDataDao monthDataDao;
    private final AlarmRecordDao alarmRecordDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        instantDataDaoConfig = daoConfigMap.get(InstantDataDao.class).clone();
        instantDataDaoConfig.initIdentityScope(type);

        dayDataDaoConfig = daoConfigMap.get(DayDataDao.class).clone();
        dayDataDaoConfig.initIdentityScope(type);

        monthDataDaoConfig = daoConfigMap.get(MonthDataDao.class).clone();
        monthDataDaoConfig.initIdentityScope(type);

        alarmRecordDaoConfig = daoConfigMap.get(AlarmRecordDao.class).clone();
        alarmRecordDaoConfig.initIdentityScope(type);

        instantDataDao = new InstantDataDao(instantDataDaoConfig, this);
        dayDataDao = new DayDataDao(dayDataDaoConfig, this);
        monthDataDao = new MonthDataDao(monthDataDaoConfig, this);
        alarmRecordDao = new AlarmRecordDao(alarmRecordDaoConfig, this);

        registerDao(InstantData.class, instantDataDao);
        registerDao(DayData.class, dayDataDao);
        registerDao(MonthData.class, monthDataDao);
        registerDao(AlarmRecord.class, alarmRecordDao);
    }
    
    public void clear() {
        instantDataDaoConfig.getIdentityScope().clear();
        dayDataDaoConfig.getIdentityScope().clear();
        monthDataDaoConfig.getIdentityScope().clear();
        alarmRecordDaoConfig.getIdentityScope().clear();
    }

    public InstantDataDao getInstantDataDao() {
        return instantDataDao;
    }

    public DayDataDao getDayDataDao() {
        return dayDataDao;
    }

    public MonthDataDao getMonthDataDao() {
        return monthDataDao;
    }

    public AlarmRecordDao getAlarmRecordDao() {
        return alarmRecordDao;
    }

}
