package com.gdut.myproject.Async;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.gdut.myproject.utils.ContextUtils;
import com.gdut.myproject.Bean.DetailData;
import com.gdut.myproject.utils.HttpUtils;
import com.gdut.myproject.utils.LogUtils;
import com.gdut.myproject.utils.MinaSocketClient;
import com.gdut.myproject.utils.SPUtils;

/**
 * Created by Administrator on 2016/6/2.
 */
public class RefreshAsync extends AsyncTask<String, Void, String> {
    private SharedPreferences sp;
    private Context mContext;
    private ProgressDialog pd;
    public RefreshAsync(SharedPreferences sp,Context context,ProgressDialog pd){
        this.sp = sp;
        this.mContext = context;
        this.pd = pd;
    }
    @Override
    protected String doInBackground(String... params) {
        String content = null;
        content = MinaSocketClient.getInstance().openMinaSocket(params[0]);
        LogUtils.Loge("1小时异步任务","获取数据内容："+content);
        return content;
    }

    @Override
    protected void onPostExecute(String s) {
        LogUtils.Loge("AsyncTaskUI线程任务","获取数据内容："+s);
        if ("null".equals(s)) {
            pd.dismiss();
            Toast.makeText(ContextUtils.getInstance(), "无法连接到服务器，请稍后再试", Toast.LENGTH_LONG).show();
            LogUtils.Loge("有问题", "网络异常");

        } else {
            Intent sendintent = new Intent();
            sendintent.putExtra("type",11);
            sendintent.setAction("PVMonitor.refresh.DATA_BROADCAST");
            mContext.sendBroadcast(sendintent);
            pd.dismiss();
            LogUtils.Loge("广播","刷新广播");
        }
    }
}
