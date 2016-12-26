package com.gdut.myproject.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import com.gdut.myproject.Bean.LogoEvent;
import com.gdut.myproject.Bean.WeatherTotal;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

import com.gdut.myproject.PVMonitor.R;
import com.gdut.myproject.utils.LogUtils;

import com.gdut.myproject.utils.NetWorkUtil;
import com.gdut.myproject.utils.OkUtils;
import com.gdut.myproject.utils.SPUtils;
import com.gdut.myproject.utils.SetBackGround;

/**
 * Created by Administrator on 2016/5/9.
 *
 */
public class Fragment2 extends Fragment {
    private static final String CITY = "广州";
    private static final String HTTPURLHEAD = "http://apis.baidu.com/apistore/weatherservice/cityname";
    private static final int TYPE_CONNECT = 1;
    private static final int TYPE_DISCONNECT = 0;
    private String url;
    private ProgressDialog pd;
    private Context mContext;
//    private MyAsyncTask task;
    private SharedPreferences sp;

    private RelativeLayout ll_fragment2;
    private TextView tv_cityName, tv_weather, tv_ltemp, tv_htemp, tv_WD, tv_WS, tv_sunrise, tv_sunset, tv_updateTime;
    private ImageView iv_weather, iv_num1, iv_num2;

    private int[] resID = {
            R.mipmap.num0, R.mipmap.num1, R.mipmap.num2, R.mipmap.num3, R.mipmap.num4,
            R.mipmap.num5, R.mipmap.num6, R.mipmap.num7, R.mipmap.num8, R.mipmap.num9,
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                setTextWithoutUpdate(sp);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.fragment2, null);
        mContext = getActivity();
        pd = ProgressDialog.show(getActivity(), "加载中", "请稍等。。。");
        sp = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        initView(v);
        return v;
    }

    private void initView(View v) {
        ll_fragment2 = (RelativeLayout) v.findViewById(R.id.ll_fragment2);

        tv_cityName = (TextView) v.findViewById(R.id.tv_cityName);
        tv_weather = (TextView) v.findViewById(R.id.tv_weather);
        tv_ltemp = (TextView) v.findViewById(R.id.tv_ltemp);
        tv_htemp = (TextView) v.findViewById(R.id.tv_htemp);
        tv_WD = (TextView) v.findViewById(R.id.tv_WD);
        tv_WS = (TextView) v.findViewById(R.id.tv_WS);
        tv_sunrise = (TextView) v.findViewById(R.id.tv_sunrise);
        tv_sunset = (TextView) v.findViewById(R.id.tv_sunset);
        tv_updateTime = (TextView) v.findViewById(R.id.tv_updateTime);

        iv_weather = (ImageView) v.findViewById(R.id.iv_weather);
        iv_num1 = (ImageView) v.findViewById(R.id.iv_num1);
        iv_num2 = (ImageView) v.findViewById(R.id.iv_num2);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //        try {
        //            url = HTTPURLHEAD + "?" + "cityname=" + URLEncoder.encode(CITY, "UTF-8");
        //        } catch (UnsupportedEncodingException e) {
        //            pd.dismiss();
        //            Toast.makeText(getActivity(), "编码异常，无详细天气信息", Toast.LENGTH_SHORT).show();
        //            e.printStackTrace();
        //        }
        //
        //        if(SPUtils.judgeTime(sp)){
        //            setTextWithoutUpdate(sp);
        //            pd.dismiss();
        //        }else{
        //            task = new MyAsyncTask();
        //            task.execute(url);
        //        }
        try {
            url = "?" + "cityname=" + URLEncoder.encode(CITY, "utf-8");

        } catch (UnsupportedEncodingException e) {
            pd.dismiss();
            Toast.makeText(getActivity(), "编码异常，无详细天气信息", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        if (SPUtils.judgeTime(sp)) {
            setTextWithoutUpdate(sp);
            pd.dismiss();
        } else {
            if(NetWorkUtil.isNetConnect(mContext)){
                LogUtils.Loge("检查网络连接","网络已经链接");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkUtils.getInstance().okhttpSunRise(com.gdut.myproject.Bean.API.CITY_TEST, new OkUtils.JObjectCallback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                EventBus.getDefault().post(new LogoEvent("IO异常，检查网络设置"));
                            }

                            @Override
                            public void onResponse(Call call, Object o) {
                                SPUtils.updateWeatherSP(sp,(WeatherTotal)o,System.currentTimeMillis());
                                LogUtils.Loge("获取心知天气","成功");
                                Message msg = Message.obtain();
                                msg.what = TYPE_CONNECT;
                                handler.sendMessage(msg);
                                pd.dismiss();
                            }
                        });
                    }
                }).start();

            }else{
                Message msg = Message.obtain();
                msg.what = TYPE_DISCONNECT;
                handler.sendMessage(msg);
                pd.dismiss();
            }
        }
    }

    private void setTextWithoutUpdate(SharedPreferences sp) {
        tv_cityName.setText(sp.getString("city","未知"));
        tv_weather.setText(sp.getString("weather","晴"));
        tv_ltemp.setText(sp.getString("l_tmp","25") + "℃");
        tv_htemp.setText(sp.getString("h_tmp","25") + "℃");
        tv_WD.setText("风向：" + sp.getString("WD","未知"));
        tv_WS.setText("风力：" + sp.getString("WS","未知")+"km/h");
        tv_sunrise.setText("日出：" + sp.getString("sunrise","00-00"));
        tv_sunset.setText("日落：" + sp.getString("sunset","00-00"));
        tv_updateTime.setText("更新时间:"+sp.getString("updatetime","00-00").replace("null",""));

        ll_fragment2.setBackgroundResource(SetBackGround.setLlBackground(sp));
        iv_weather.setBackgroundResource(setIvWeatherBackground(sp.getString("weather","晴")));
        int[] num = getNumFromTemp(sp.getString("temp","25"));
        iv_num1.setBackgroundResource(resID[num[0]]);
        iv_num2.setBackgroundResource(resID[num[1]]);
        pd.dismiss();
    }

    private int[] getNumFromTemp(String temp) {
        int[] nums = new int[2];
        int temps = Integer.parseInt(temp);
        nums[0] = temps / 10;
        nums[1] = temps % 10;
        LogUtils.Loge("温度", "num[0]:" + nums[0] + " num[1]:" + nums[1]);
        return nums;
    }

    private int setIvWeatherBackground(String weather) {
        int resId = R.mipmap.w_1;
        if (weather.indexOf("多云") != -1 || weather.indexOf("晴") != -1) {//多云转晴，以下类同 indexOf:包含字串
            resId = R.mipmap.w_2;
        } else if (weather.indexOf("多云") != -1 && weather.indexOf("阴") != -1) {
            resId = R.mipmap.w_4;
        } else if (weather.indexOf("阴") != -1 && weather.indexOf("雨") != -1) {
            resId = R.mipmap.w_5;
        } else if (weather.indexOf("晴") != -1 && weather.indexOf("雨") != -1) {
            resId = R.mipmap.w_2;
        } else if (weather.indexOf("晴") != -1 && weather.indexOf("雾") != -1) {
            resId = R.mipmap.w_3;
        } else if (weather.indexOf("晴") != -1) {
            resId = R.mipmap.w_1;
        } else if (weather.indexOf("多云") != -1) {
            resId = R.mipmap.w_4;
        } else if (weather.indexOf("阵雨") != -1 && weather.indexOf("雷") == -1) {
            resId = R.mipmap.w_5;
        } else if (weather.indexOf("阵雨") != -1 && weather.indexOf("中雨") != -1) {
            resId = R.mipmap.w_6;
        } else if (weather.indexOf("阵雨") != -1 && weather.indexOf("大雨") != -1) {
            resId = R.mipmap.w_7;
        } else if (weather.indexOf("阵雨") != -1 && weather.indexOf("暴雨") != -1) {
            resId = R.mipmap.w_8;
        } else if (weather.indexOf("小雨") != -1) {
            resId = R.mipmap.w_5;
        } else if (weather.indexOf("中雨") != -1) {
            resId = R.mipmap.w_6;
        } else if (weather.indexOf("大雨") != -1) {
            resId = R.mipmap.w_7;
        } else if (weather.indexOf("暴雨") != -1) {
            resId = R.mipmap.w_8;
        } else if (weather.indexOf("冰雹") != -1) {
            resId = R.mipmap.w_9;
        } else if (weather.indexOf("雷阵雨") != -1) {
            resId = R.mipmap.w_12;
        } else if (weather.indexOf("小雪") != -1) {
            resId = R.mipmap.w_9;
        } else if (weather.indexOf("中雪") != -1) {
            resId = R.mipmap.w_10;
        } else if (weather.indexOf("大雪") != -1) {
            resId = R.mipmap.w_11;
        } else if (weather.indexOf("暴雪") != -1) {
            resId = R.mipmap.w_11;
        } else if (weather.indexOf("扬沙") != -1) {
            resId = R.mipmap.w_3;
        } else if (weather.indexOf("沙尘") != -1) {
            resId = R.mipmap.w_3;
        } else if (weather.indexOf("雾") != -1) {
            resId = R.mipmap.w_3;
        }
        return resId;

    }


    public void onDestroy() {
        super.onDestroy();
        LogUtils.Loge("测试", "fragment2被销毁");
//        Log.e("测试", "fragment2被销毁");
    }
}