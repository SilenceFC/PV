package com.gdut.myproject.service;

import android.app.IntentService;
import android.content.Intent;

import com.gdut.myproject.greendao.AlarmRecord;

import com.gdut.myproject.Bean.NotifyEntity;
import com.gdut.myproject.utils.DbService;
import com.gdut.myproject.utils.GetBundleFromIntent;
import com.gdut.myproject.utils.LogUtils;

/**
 * Created by Administrator on 2016/9/24.
 * 分析推送过来的报警信息并保存报警信息
 */
public class AlarmService extends IntentService {
    private DbService db;
    NotifyEntity notifyEntity;
    AlarmRecord record;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public AlarmService() {
        super("AlarmService");
        db = DbService.getInstance();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        notifyEntity = GetBundleFromIntent.getNotify(intent);
         record = new AlarmRecord();
        record.setDate_alarm(notifyEntity.getContent());
        record.setType(notifyEntity.getExtra());

        db.savaAlarmNote(record);

        LogUtils.Loge("AlarmService","数据已经存储");
    }
}
