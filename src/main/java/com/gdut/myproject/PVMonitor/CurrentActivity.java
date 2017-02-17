package com.gdut.myproject.PVMonitor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.gdut.myproject.Bean.API;
import com.gdut.myproject.Bean.CurrentInfo;
import com.gdut.myproject.Bean.MainEvent;
import de.greenrobot.event.EventBus;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import com.gdut.myproject.utils.LogUtils;
import com.gdut.myproject.utils.MinaSocketClient;
import com.gdut.myproject.utils.OkUtils;
import com.gdut.myproject.utils.SetBackGround;

/**
 * Created by Administrator on 2016/9/19.
 * 1.判断是否wifi下，是就直接获取数据
 * 2.不是则弹出dialog，dialog确定加载数据，否定就退出界面返回上一页
 */
public class CurrentActivity extends Activity implements View.OnClickListener{
    LinearLayout ll_fragment4,ll_interver,ll_mppt;
    SharedPreferences sp;
    Timer timer;
    Gson mGson;

    Disposable disposable;

    private TextView tv_inI,tv_inU,tv_inP,tv_outI,tv_outU,tv_outP,tv_temp;
    private TextView tv_DCI,tv_DCU,tv_out_P,tv_AoutU,tv_BoutU;
    TextView tv_mppt,tv_interver;

    boolean isMpptGone = true;
    boolean isInterverGone = true;

    Drawable img_close,img_open;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);
        EventBus.getDefault().register(this);
        initView();

    }

    private void test(){
       disposable =  Observable.interval(0,2000,TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .map(new Function<Long, String>() {
                    @Override
                    public String apply(Long s) throws Exception {
                        LogUtils.Loge("rxjava","s:"+s);
                        return MinaSocketClient.getInstance().openMinaSocket("Current");
                    }
                })
                .map(new Function<String, List<String>>() {
                    @Override
                    public List<String> apply(String s) throws Exception {
                        return mGson.fromJson(s,new TypeToken<List<String>>(){}.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> list) throws Exception {
                        LogUtils.Loge("Rxjava obsever","list:"+list.toString());
                        giveValue2View(list);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkWiFi(this);
    }

    private void initView() {
        mGson = new Gson();
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        ll_interver = (LinearLayout) findViewById(R.id.ll_interver);
        ll_mppt = (LinearLayout) findViewById(R.id.ll_mppt);

        ll_fragment4 = (LinearLayout) findViewById(R.id.ll_fragment4);
        ll_fragment4.setBackgroundResource(SetBackGround.setLlBackground(sp));

        tv_interver = (TextView) findViewById(R.id.tv_interver);
        tv_mppt = (TextView) findViewById(R.id.tv_mppt);

        tv_interver.setOnClickListener(this);
        tv_mppt.setOnClickListener(this);

        img_close = getResources().getDrawable(R.mipmap.more);
        img_open = getResources().getDrawable(R.mipmap.more_unfold);

        img_close.setBounds(0, 0, img_close.getMinimumWidth(), img_close.getMinimumHeight());
        img_open.setBounds(0, 0, img_open.getMinimumWidth(), img_open.getMinimumHeight());

        tv_inI = (TextView) findViewById(R.id.tv_inI);
        tv_inU = (TextView) findViewById(R.id.tv_inU);
        tv_inP = (TextView) findViewById(R.id.tv_inP);
        tv_outI = (TextView) findViewById(R.id.tv_outI);
        tv_outU = (TextView) findViewById(R.id.tv_outU);
        tv_outP = (TextView) findViewById(R.id.tv_outP);
        tv_temp = (TextView) findViewById(R.id.tv_temp);

        tv_DCI = (TextView) findViewById(R.id.tv_DCI);
        tv_DCU = (TextView) findViewById(R.id.tv_DCU);
        tv_out_P = (TextView) findViewById(R.id.tv_out_P);
        tv_AoutU = (TextView) findViewById(R.id.tv_AoutU);
        tv_BoutU = (TextView) findViewById(R.id.tv_BoutU);
    }

    private void checkWiFi(Context context) {
        if(!isWifiConnected(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("检测到wifi未连接，继续查看会产生流量消耗")
                    .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CurrentActivity.this.finish();
                        }
                    }).setPositiveButton("继续", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    test();
                }
            }).setCancelable(false).show();
        }else {
            LogUtils.Loge("wifi打开","： 111");
//            getCurrentDataFromNet();
            test();
        }
    }

public void onEventMainThread(CurrentInfo info){
    List<String> list= info.getCurrentList();
    giveValue2View(list);
}

    private void giveValue2View(List<String> list) {
        int temp = (int)(Float.parseFloat(list.get(0)));

        int inI = (int)(Float.parseFloat(list.get(1)));
        int inU = (int)(Float.parseFloat(list.get(2)));

        int inP = inI*inU;

        int outI = (int)(Float.parseFloat(list.get(3)));
        int outU = (int)(Float.parseFloat(list.get(4)));

        int outP = outI*outU;

        int DCI = (int)(Float.parseFloat(list.get(5)));
        int DCU = (int)(Float.parseFloat(list.get(6)));
        int out_P = (int)(Float.parseFloat(list.get(7)));
        int AoutU = (int)(Float.parseFloat(list.get(8)));
        int BoutU = (int)(Float.parseFloat(list.get(9)));

        tv_inI.setText(inI+" A");
        tv_inU.setText(inU+" V");
        tv_inP.setText(inP+" W");

        tv_outI.setText(+outI+" A");
        tv_outU.setText(+outU+" V");
        tv_outP.setText(+outP+" W");
        tv_temp.setText(+temp+" ℃");

        tv_DCI.setText(DCI+" A");
        tv_DCU.setText(DCU+" V");
        tv_out_P.setText(out_P+" W");
        tv_AoutU.setText(AoutU+" V");
        tv_BoutU.setText(BoutU+" V");
    }

    /**
     * 开始获取网络数据
     */
    private void getCurrentDataFromNet() {
        LogUtils.Loge("current1123：", "获取数据");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                OkUtils.getInstance().okhttpCurrentData(API.PATH_CURRENT, new OkUtils.JsoupCallback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        EventBus.getDefault().post(new MainEvent("获取数据失败，请检查网络"));
                        CurrentActivity.this.finish();
                    }

                    @Override
                    public void onResponse(Call call, List<String> list) {
                        LogUtils.Loge("current1123：", list.toString());
                        EventBus.getDefault().post(new CurrentInfo(list));
                    }
                });
            }
        },0,2000);
    }

    private boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        LogUtils.Loge("current",networkInfo.isConnected()+"");
        return networkInfo.isConnected();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_interver:
                if(isInterverGone){
                    ll_interver.setVisibility(View.VISIBLE);
                    tv_interver.setCompoundDrawables(img_open, null, null, null);
                    isInterverGone = false;
                }else{
                    ll_interver.setVisibility(View.GONE);
                    tv_interver.setCompoundDrawables(img_close,null,null,null);
                    isInterverGone = true;
                }
                break;
            case R.id.tv_mppt:
                if(isMpptGone){
                    ll_mppt.setVisibility(View.VISIBLE);
                    tv_mppt.setCompoundDrawables(img_open, null, null, null);
                    isMpptGone = false;
                }else{
                    ll_mppt.setVisibility(View.GONE);
                    tv_mppt.setCompoundDrawables(img_close,null,null,null);
                    isMpptGone = true;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(disposable!=null&&!disposable.isDisposed()){
            disposable.dispose();
        }
    }

    public interface MinaCallBack{
        void onResponse(Object msg);
        void onError(Throwable throwable);
    }
}
