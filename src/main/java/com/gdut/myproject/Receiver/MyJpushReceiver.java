package com.gdut.myproject.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;



import com.gdut.myproject.PVMonitor.HistoryActivity;
import com.gdut.myproject.PVMonitor.R;
import com.gdut.myproject.service.AlarmService;

import com.gdut.myproject.Bean.NotifyEntity;
import cn.jpush.android.api.JPushInterface;
import com.gdut.myproject.utils.GetBundleFromIntent;
import com.gdut.myproject.utils.LogUtils;

/**
 * Created by Administrator on 2016/9/21.
 */
public class MyJpushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotifyEntity entity = GetBundleFromIntent.getNotify(intent);
        LogUtils.Loge("title", "->"+entity.getTitle());
        LogUtils.Loge("content","->"+entity.getContent());
        LogUtils.Loge("extra","->"+entity.getExtra());
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            if((entity.getType()>=2&&entity.getType()<=5)||entity.getType()==7||entity.getType()==9){
                //出现报警信息才会弹出通知;
                showNotification(context, entity);
                saveNotification(context, GetBundleFromIntent.getBundle(intent));
                LogUtils.Loge("极光广播", "过载/过压/欠压/短路情况");
            }else {
                //改变app主页逆变器，光伏控制器的工作状态
                Intent broadcast = new Intent();
                broadcast.putExtra("type",entity.getType());
                broadcast.setAction("PVMonitor.refresh.DATA_BROADCAST");
                context.sendBroadcast(broadcast);
                LogUtils.Loge("极光广播", "收到推送消息，工作状态改变");
            }

        }
    }

    private void saveNotification(Context context, Bundle bundle) {
        LogUtils.Loge("极光广播", "保存报警信息");
        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    private void showNotification(Context context, NotifyEntity entity) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, HistoryActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context,getAutoAddNum(),intent,PendingIntent.FLAG_ONE_SHOT);
        Notification notification = null;
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.JELLY_BEAN) {//版本小于4.0
            Notification.Builder builder = new Notification.Builder(context)
                    .setAutoCancel(true)
                    .setContentTitle(entity.getTitle())
                    .setContentText(entity.getExtra()+",请及时处理")
                    .setSmallIcon(R.mipmap.sun)
                    .setContentIntent(pi)
                    .setWhen(System.currentTimeMillis());
            notification = builder.getNotification();
        }else {
            notification = new Notification.Builder(context)
                    .setAutoCancel(true)
                    .setContentTitle(entity.getTitle())
                    .setContentText(entity.getExtra()+",请及时处理")
                    .setSmallIcon(R.mipmap.sun)
                    .setContentIntent(pi)
                    .setWhen(System.currentTimeMillis())
                    .build();
        }

        notification.defaults = Notification.DEFAULT_ALL;
        notificationManager.notify(getAutoAddNum(),notification);
    }


    public static final int MAX = Integer.MAX_VALUE;
    public static final int MIN = (int) MAX / 2;

    /**
     * 给推送内容设置不同的id,当多个推送通知发送过来时、会依次显示而不会被覆盖
     * @return
     */
    private int getAutoAddNum() {
        return (int) (MIN + Math.random() * (MAX - MIN));
    }
}
