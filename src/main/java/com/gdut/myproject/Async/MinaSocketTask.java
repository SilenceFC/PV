package com.gdut.myproject.Async;

import android.os.AsyncTask;

import com.gdut.myproject.utils.LogUtils;
import com.gdut.myproject.utils.MinaSocketClient;

/**
 * Created by Administrator on 2016/11/14.
 */
public class MinaSocketTask extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... params) {
        String content = null;
        content = MinaSocketClient.getInstance().openMinaSocket(params[0]);
        LogUtils.Loge("异步任务","获取数据内容："+content);
        return content;
    }

}
