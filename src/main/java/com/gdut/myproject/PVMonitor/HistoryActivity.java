package com.gdut.myproject.PVMonitor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.gdut.myproject.greendao.AlarmRecordDao;

import com.gdut.myproject.Adapter.AlarmCursorAdapter;
import cn.jpush.android.api.JPushInterface;
import com.gdut.myproject.utils.ContextUtils;
import com.gdut.myproject.utils.SetBackGround;

/**
 * Created by Administrator on 2016/9/19.
 */
public class HistoryActivity extends Activity {
    private SQLiteDatabase db;
    private ListView list_alarm;
    private SharedPreferences sp;
    private LinearLayout ll_activity_history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();
        String orderBy = AlarmRecordDao.Properties.Id.columnName+" DESC";
        Cursor cursor = db.query(ContextUtils.getDaoSession(this).getAlarmRecordDao().getTablename(),
                ContextUtils.getDaoSession(this).getAlarmRecordDao().getAllColumns(),null,null,null,null,orderBy);
       list_alarm.setAdapter(new AlarmCursorAdapter(this,cursor,true));

    }

    private void initView() {
        db = ContextUtils.getDb(this);
        list_alarm = (ListView) findViewById(R.id.list_alarm);
        ll_activity_history = (LinearLayout) findViewById(R.id.ll_activity_history);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        ll_activity_history.setBackgroundResource(SetBackGround.setLlBackground(sp));
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

}
