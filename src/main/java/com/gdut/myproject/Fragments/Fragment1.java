package com.gdut.myproject.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

import com.gdut.myproject.Async.MinaSocketTask;
import com.gdut.myproject.Bean.API;
import com.gdut.myproject.PVMonitor.R;
import com.gdut.myproject.utils.ContextUtils;
import com.gdut.myproject.Bean.DetailData;
import com.gdut.myproject.utils.HttpUtils;
import com.gdut.myproject.utils.LogUtils;
import com.gdut.myproject.utils.MinaSocketClient;
import com.gdut.myproject.utils.SPUtils;
import com.gdut.myproject.utils.SetBackGround;

/**
 * Created by Administrator on 2016/5/9.
 * 主要用于显示数据，同时判断时间时候过去1小时，是就刷新数据
 */
public class Fragment1 extends Fragment {
    private LinearLayout ll_fragment1, ll_state, ll_month, ll_year, ll_total, ll_volume, ll_today, ll_picture;
    private ImageView iv_weather, iv_mpptstate, iv_pvstate;
    private TextView tv_monthData, tv_yearData, tv_totalData, tv_todayData, tv_updatetime1;
    private SharedPreferences sp;
    Fragment1Receiver fr1;


    private static final String CITY = "广州";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment1, container, false);
        initView(v);
        isConnected(getActivity());
        return v;
    }

    private void isConnected(FragmentActivity context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if(manager!=null){
            NetworkInfo info = manager.getActiveNetworkInfo();
            if(info!=null&& info.isConnected()){
                if(info.getState() == NetworkInfo.State.CONNECTED){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("state", API.SUCCESS_NET);
                    editor.commit();
                }else{
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("state", API.ERROR_NET);
                    editor.commit();
                }
            }
        }
    }

    private void initView(View v) {
        ll_fragment1 = (LinearLayout) v.findViewById(R.id.ll_fragment1);
        ll_state = (LinearLayout) v.findViewById(R.id.ll_state);
        iv_weather = (ImageView) v.findViewById(R.id.iv_weather);
        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);

        iv_mpptstate = (ImageView) v.findViewById(R.id.iv_mpptstate);
        iv_pvstate = (ImageView) v.findViewById(R.id.iv_pvstate);

        tv_todayData = (TextView) v.findViewById(R.id.tv_todayData);
        tv_totalData = (TextView) v.findViewById(R.id.tv_totalData);
        tv_yearData = (TextView) v.findViewById(R.id.tv_yearData);
        tv_monthData = (TextView) v.findViewById(R.id.tv_monthData);
        tv_updatetime1 = (TextView) v.findViewById(R.id.tv_updatetime1);

        setView();
    }

    private void setView() {
        LogUtils.Loge("View", "刷新View");
        ll_fragment1.setBackgroundResource(SetBackGround.setLlBackground(sp));
        iv_weather.setBackgroundResource(R.mipmap.back_house);

        iv_mpptstate.setBackgroundResource(SetBackGround.setDeviceStateBackground(sp,0));
        iv_pvstate.setBackgroundResource(SetBackGround.setDeviceStateBackground(sp,1));

        tv_todayData.setText(sp.getFloat("thisDay", (float) 0) + "kWh");
        tv_monthData.setText(sp.getFloat("thisMonth", (float) 0) + "kWh");
        tv_yearData.setText(sp.getFloat("thisYear", (float) 0) + "kWh");
        tv_totalData.setText(sp.getFloat("total", (float) 0) + "kWh");
        tv_updatetime1.setText(sp.getString("date", "0000-00-00"));

        ll_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = sp.getInt("state", API.ERROR_NET);
                switch (state){
                    case API.ERROR_NET:
                        Toast.makeText(getActivity(),"网络未连接，请检查网络设置后刷新。。。",Toast.LENGTH_LONG).show();
                        break;
                    case API.ERROR_SERVER:
                        Toast.makeText(getActivity(),"光伏监测服务器未上线，详情请联系工作人员。。。",Toast.LENGTH_LONG).show();
                        break;
                    case API.ERROR_SELF:
                        Toast.makeText(getActivity(),"设备关闭/工作异常，详情请联系工作人员。。。",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!SPUtils.judgeTime(sp)) {
            //本页面自动刷新  需要做出改进
            String uuid = sp.getString("UUID","");
            MyAsyncTask task = new MyAsyncTask();
            task.execute(uuid);
        }

        fr1 = new Fragment1Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("PVMonitor.refresh.DATA_BROADCAST");
        getActivity().registerReceiver(fr1, filter);

        LogUtils.Loge("广播接收者", "fragment1中的广播接收者已经注册成功");
    }


    public class MyAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            String content = null;
            content = MinaSocketClient.getInstance().openMinaSocket(params[0]);
            LogUtils.Loge("1小时异步任务","获取数据内容："+content);
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals(null)){
                Toast.makeText(getActivity(),"网络连接异常，请稍后再试。。。",Toast.LENGTH_LONG).show();
            }
            setView();
        }
    }
//    public class MyAsyncTask extends AsyncTask<String, Void, DetailData> {
//
//        @Override
//        protected DetailData doInBackground(String... params) {
//            HttpUtils hu = new HttpUtils(getContext());
//            return hu.getJsonFromHttp(params[0], CITY);
//        }
//
//        @Override
//        protected void onPostExecute(DetailData detailData) {
//            if (!detailData.isExist()) {
//                Toast.makeText(ContextUtils.getInstance(), "无法连接到服务器，请稍后再试", Toast.LENGTH_LONG).show();
//                LogUtils.Loge("有问题", "网络异常");
//                setView();
//            } else {
//                SPUtils.updatePVSP(sp, detailData, System.currentTimeMillis());
//                setView();
//            }
//        }
//    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(fr1);
    }

    private class Fragment1Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getExtras().getInt("type");
            SharedPreferences.Editor editor = sp.edit();
//            setView();
            switch (type){
                case 0:
                    //所有设备均下线
                    editor.putInt("state",API.ERROR_SERVER);
                    editor.commit();
                    iv_mpptstate.setBackgroundResource(R.mipmap.main2);
                    iv_pvstate.setBackgroundResource(R.mipmap.main2);
                    break;
                case 1:
                    //所有设备未下线
                    editor.putInt("state",API.SUCCESS_SERVER);
                    editor.commit();
                    break;
                case 6:
                    //逆变器启动
                    editor.putInt("InterverState",1);
                    editor.commit();
                    iv_pvstate.setBackgroundResource(R.mipmap.main1);
                    break;
                case 7:
                    //逆变器关闭
                    editor.putInt("InterverState",0);
                    editor.commit();
                    iv_pvstate.setBackgroundResource(R.mipmap.main2);
                    break;
                case 8:
                    //控制器启动
                    editor.putInt("MpptState",1);
                    editor.commit();
                    iv_mpptstate.setBackgroundResource(R.mipmap.main1);
                    break;
                case 9:
                    //控制器关闭
                    editor.putInt("MpptState",0);
                    editor.commit();
                    iv_mpptstate.setBackgroundResource(R.mipmap.main2);
                    break;
                case 11:
                    setView();
                    break;
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        Log.e("测试", "fragment1被销毁");

    }
}
