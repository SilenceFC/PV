package com.gdut.myproject.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



import java.text.SimpleDateFormat;
import java.util.Date;

import com.gdut.myproject.PVMonitor.R;
import com.gdut.myproject.utils.ContextUtils;
import com.gdut.myproject.Bean.DetailData;
import com.gdut.myproject.utils.HttpUtils;
import com.gdut.myproject.utils.LogUtils;
import com.gdut.myproject.utils.SPUtils;
import com.gdut.myproject.utils.SetBackGround;

/**
 * Created by Administrator on 2016/5/9.
 * 主要用于显示数据，同时判断时间时候过去1小时，是就刷新数据
 */
public class Fragment1 extends Fragment {
   private LinearLayout ll_fragment1,ll_state,ll_month,ll_year,ll_total,ll_volume,ll_today,ll_picture;
    private ImageView iv_weather;
    private TextView tv_monthData,tv_yearData,tv_totalData,tv_todayData,tv_updatetime1;
    private SharedPreferences sp;
    Fragment1Receiver fr1;
    private static final String CITY="广州";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment1,container,false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        ll_fragment1 = (LinearLayout) v.findViewById(R.id.ll_fragment1);
        iv_weather = (ImageView) v.findViewById(R.id.iv_weather);
        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);

        tv_todayData = (TextView) v.findViewById(R.id.tv_todayData);
        tv_totalData = (TextView) v.findViewById(R.id.tv_totalData);
        tv_yearData = (TextView) v.findViewById(R.id.tv_yearData);
        tv_monthData = (TextView) v.findViewById(R.id.tv_monthData);
        tv_updatetime1 = (TextView) v.findViewById(R.id.tv_updatetime1);

        setView();
    }

    private void setView(){
        LogUtils.Loge("View","刷新View");
        ll_fragment1.setBackgroundResource(SetBackGround.setLlBackground(sp));
        iv_weather.setBackgroundResource(SetBackGround.setImageBackground(sp));

        tv_todayData.setText(sp.getFloat("thisDay", (float) 0) + "kWh");
        tv_monthData.setText(sp.getFloat("thisMonth", (float) 0)+"kWh");
        tv_yearData.setText(sp.getFloat("thisYear", (float) 0)+"kWh");
        tv_totalData.setText(sp.getFloat("total", (float) 0) + "kWh");
        tv_updatetime1.setText(sp.getString("date", "0000-00-00"));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!SPUtils.judgeTime(sp)){
            String add = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            MyAsyncTask task = new MyAsyncTask();
            task.execute(add);
        }

        fr1= new Fragment1Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("PVMonitor.refresh.DATA_BROADCAST");
        getActivity().registerReceiver(fr1, filter);

        LogUtils.Loge("广播接收者", "fragment1中的广播接收者已经注册成功");
    }

    public class MyAsyncTask extends AsyncTask<String,Void, DetailData> {

        @Override
        protected DetailData doInBackground(String... params) {
            HttpUtils hu = new HttpUtils(getContext());
            return hu.getJsonFromHttp(params[0],CITY);
        }

        @Override
        protected void onPostExecute(DetailData detailData) {
            if(!detailData.isExist()){
                Toast.makeText(ContextUtils.getInstance(), "无法连接到服务器，请稍后再试", Toast.LENGTH_LONG).show();
                LogUtils.Loge("有问题", "网络异常");
                setView();
            }else{
                SPUtils.updatePVSP(sp, detailData, System.currentTimeMillis());
                setView();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(fr1);
    }

    private class Fragment1Receiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            setView();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        Log.e("测试", "fragment1被销毁");

    }
}
