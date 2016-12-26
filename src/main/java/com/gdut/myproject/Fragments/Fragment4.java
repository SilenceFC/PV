package com.gdut.myproject.Fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdut.myproject.PVMonitor.R;

import java.util.List;


import com.gdut.myproject.service.UpdateService;

import com.gdut.myproject.utils.LogUtils;
import com.gdut.myproject.utils.SetBackGround;

/**
 * Created by Administrator on 2016/5/9.
 */
public class Fragment4 extends Fragment {
    private List<Float> datas;
    LinearLayout ll_fragment4,ll_interver,ll_mppt;
    SharedPreferences sp;
    Intent intent;
    PendingIntent pi;
    FragmentReceiver fr;
    private TextView tv_inI,tv_inU,tv_inP,tv_outI,tv_outU,tv_outP,tv_temp;
    private TextView tv_DCI,tv_DCU,tv_out_P,tv_AoutU,tv_BoutU,tv_mainU;
    TextView tv_mppt,tv_interver;
    boolean isMpptGone = true;
    boolean isInterverGone = true;
    Drawable img_close,img_open;

    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.activity_current, null);
        initView(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        tv_mppt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMpptGone){
                    ll_mppt.setVisibility(View.VISIBLE);
                    tv_mppt.setCompoundDrawables(img_open,null,null,null);
                    isMpptGone = false;
                }else{
                    ll_mppt.setVisibility(View.GONE);
                    tv_mppt.setCompoundDrawables(img_close, null, null, null);
                    isMpptGone = true;
                }
            }
        });
        tv_interver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInterverGone){
                    ll_interver.setVisibility(View.VISIBLE);
                    tv_interver.setCompoundDrawables(img_open, null, null, null);
                    isInterverGone = false;

                } else{
                    ll_interver.setVisibility(View.GONE);
                    tv_interver.setCompoundDrawables(img_close,null,null,null);
                    isInterverGone = true;
                }
            }
        });

        intent = new Intent(getActivity(), UpdateService.class);
        pi = PendingIntent.getService(getActivity(),0,intent,0);

        fr = new FragmentReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("PVMonitor.DATA_BROADCAST");
        getActivity().registerReceiver(fr, filter);

        LogUtils.Loge("广播接收者", "fragment4中的广播接收者已经注册成功");

        AlarmManager am1 = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        am1.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, 2000, pi);

        LogUtils.Loge("闹钟", "闹钟已经启动，adapter已配置");
    }

    //接受服务发来的广播，取出广播中的数据
    private class FragmentReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String[] data =  intent.getExtras().getStringArray("data");

           setTextContent(data);

            LogUtils.Loge("onReveiver", "接收到广播");
        }
    }

    private void setTextContent(String[] obj) {
        int temp = (int)(Float.parseFloat(obj[0]));

        int inI = (int)(Float.parseFloat(obj[1]));
        int inU = (int)(Float.parseFloat(obj[2]));

        int inP = inI*inU;

        int outI = (int)(Float.parseFloat(obj[3]));
        int outU = (int)(Float.parseFloat(obj[4]));

        int outP = outI*outU;

        int DCI = (int)(Float.parseFloat(obj[5]));
        int DCU = (int)(Float.parseFloat(obj[6]));
        int out_P = (int)(Float.parseFloat(obj[7]));
        int AoutU = (int)(Float.parseFloat(obj[8]));
        int BoutU = (int)(Float.parseFloat(obj[9]));
        int mainU = (int)(Float.parseFloat(obj[10]));


        tv_inI.setText("输入电流:"+inI+"A");
        tv_inU.setText("输入电压:"+inU+"V");
        tv_inP.setText("输入功率:"+inP+""+"W");

        tv_outI.setText("输出电流:"+outI+"A");
        tv_outU.setText("输出电压:"+outU+"V");
        tv_outP.setText("输出功率:"+outP+""+"W");
        tv_temp.setText("散热器温度:"+temp+"℃");

        tv_DCI.setText("直流电流:"+DCI+"A");
        tv_DCU.setText("直流电压:"+DCU+"V");
        tv_out_P.setText("输出功率:"+out_P+"kW");
        tv_AoutU.setText("A相电压:"+AoutU+"V");
        tv_BoutU.setText("B相电压:"+BoutU+"V");
        tv_mainU.setText("市电电压:"+mainU+"V");
    }

    @Override
    public void onStop() {
        super.onStop();
        AlarmManager am2 = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        am2.cancel(pi);
        getActivity().unregisterReceiver(fr);
        LogUtils.Loge("停止状态", "service被终止");
    }

    public void onDestroy() {
        super.onDestroy();
        LogUtils.Loge("测试", "fragment4被销毁");

    }

    private void initView(View v) {
        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        ll_interver = (LinearLayout) v.findViewById(R.id.ll_interver);
        ll_mppt = (LinearLayout) v.findViewById(R.id.ll_mppt);
        ll_fragment4 = (LinearLayout) v.findViewById(R.id.ll_fragment4);
        ll_fragment4.setBackgroundResource(SetBackGround.setLlBackground(sp));

        tv_interver = (TextView) v.findViewById(R.id.tv_interver);
        tv_mppt = (TextView) v.findViewById(R.id.tv_mppt);

        img_close = getResources().getDrawable(R.mipmap.a_close);
        img_open = getResources().getDrawable(R.mipmap.a_open);

        img_close.setBounds(0, 0, img_close.getMinimumWidth(), img_close.getMinimumHeight());
        img_open.setBounds(0, 0, img_open.getMinimumWidth(), img_open.getMinimumHeight());

        tv_inI = (TextView) v.findViewById(R.id.tv_inI);
        tv_inU = (TextView) v.findViewById(R.id.tv_inU);
        tv_inP = (TextView) v.findViewById(R.id.tv_inP);
        tv_outI = (TextView) v.findViewById(R.id.tv_outI);
        tv_outU = (TextView) v.findViewById(R.id.tv_outU);
        tv_outP = (TextView) v.findViewById(R.id.tv_outP);
        tv_temp = (TextView) v.findViewById(R.id.tv_temp);

        tv_DCI = (TextView) v.findViewById(R.id.tv_DCI);
        tv_DCU = (TextView) v.findViewById(R.id.tv_DCU);
        tv_out_P = (TextView) v.findViewById(R.id.tv_out_P);
        tv_AoutU = (TextView) v.findViewById(R.id.tv_AoutU);
        tv_BoutU = (TextView) v.findViewById(R.id.tv_BoutU);

    }
}
