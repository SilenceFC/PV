package com.gdut.myproject.PVMonitor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gdut.myproject.Async.MinaSocketTask;
import com.gdut.myproject.Bean.API;
import com.gdut.myproject.Bean.LogoEvent;
import com.gdut.myproject.Bean.UpdateInfo;
import com.gdut.myproject.Bean.WeatherTotal;
import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;
import okhttp3.Call;
import com.gdut.myproject.utils.LogUtils;
import com.gdut.myproject.utils.OkUtils;
import com.gdut.myproject.utils.SPUtils;


/**
 * 获取版本信息并提示更新
 * Created by Administrator on 2016/5/6.
 */
public class LogoActivity extends Activity {
    private static final int ENTER_MAIN = 0;
    private static final int ERROR_URL = 1;
    private static final int ERROR_JSON = 2;
    private static final int ERROR_IO = 3;
    private static final int SHOWDIALOG = 4;

    private static final String TAG = "LogoActivity";
    private Gson mGson;

    private TextView tv_version;
    private TextView tv_updateprogress;

    private RequestQueue requestQueue;
    private SharedPreferences sp;

    private String version;
    private String description;
    private String apkurl;
    private String uuid;
    private long start, end, dTime;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ENTER_MAIN:
                    enterMainActivity();
                    break;
                case ERROR_URL:
                    Toast.makeText(LogoActivity.this, "URL异常，请刷新重试", Toast.LENGTH_SHORT).show();
                    enterMainActivity();
                    break;
                case ERROR_JSON:
                    Toast.makeText(LogoActivity.this, "Json异常，请刷新重试", Toast.LENGTH_SHORT).show();
                    enterMainActivity();
                    break;
                case ERROR_IO:
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("state",API.ERROR_NET);
                    editor.commit();
                    Toast.makeText(LogoActivity.this, "网络异常，请检查网络后重试", Toast.LENGTH_SHORT).show();
                    enterMainActivity();
                    break;
                case SHOWDIALOG:
//                    showUpdateDialog();
                    break;
                default:
                    enterMainActivity();
                    break;

            }
        }
    };

//    private void showUpdateDialog() {
//        LogUtils.Loge("显示dialog", "进入dialog");
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("更新信息")
//                .setMessage(description)
//                .setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        dialog.dismiss();
//                        enterMainActivity();
//                    }
//                }).setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                enterMainActivity();
//            }
//        }).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    OkUtils.getInstance().xutilsDownloadFile(tv_updateprogress, apkurl, version, new OkUtils.FileCallback() {
//                        @Override
//                        public void onFailure(IOException e) {
//                            Toast.makeText(LogoActivity.this, "下载失败", Toast.LENGTH_SHORT);
//                        }
//
//                        @Override
//                        public void onResponse(File file) {
//                            installAPK(file);
//                        }
//                    });
//                }
//            }
//        }).show();
//    }

    private void installAPK(File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        setContentView(R.layout.activity_logo);

        initView();

        MinaSocketTask task = new MinaSocketTask();
        //传入欲发送到服务器的数据
        task.execute(sp.getString("UUID",""));

        if (SPUtils.judgeTime(sp)) {//未到1小时
            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enterMainActivity();
                    LogUtils.Loge("com/gdut/myproject/handler", "未到时间，无需刷新");
                }
            }, 2000);
        } else {
            String add = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            start = System.currentTimeMillis();

//            MinaSocketTask task = new MinaSocketTask();
//            //传入欲发送到服务器的数据
//            task.execute("");


            //            MyAsyncTask task = new MyAsyncTask();
            //            task.execute(add);
            getRefreshData(add);
        }

        ScaleAnimation sa = new ScaleAnimation(0.5f,1.0f,0.5f,1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        sa.setDuration(2000);
        sa.setFillAfter(true);
        findViewById(R.id.app_back).setAnimation(sa);
    }

    public void onEventMainThread(LogoEvent event){
        Toast.makeText(this,event.getMsg(),Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化控件，获取app版本号，初始化volley请求队列
     */
    private void initView() {
        mGson = new Gson();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_updateprogress = (TextView) findViewById(R.id.tv_updateprogress);
        requestQueue = Volley.newRequestQueue(LogoActivity.this);

        tv_version.setText("版本号：" + getVersionName());

        SPUtils.updateUUIDSP(sp);
        Log.e(TAG, "LOGO版本号: " + getVersionName());

        EventBus.getDefault().register(this);
    }

    private void getRefreshData(String time) {
        OkUtils.getInstance().okhttpSunRise(API.CITY_TEST, new OkUtils.JObjectCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = Message.obtain();
                msg.what = ERROR_IO;
                mhandler.sendMessage(msg);
                EventBus.getDefault().post(new LogoEvent("IO异常，检查网络设置"));
            }

            @Override
            public void onResponse(Call call, Object o) {
                SPUtils.updateWeatherSP(sp,(WeatherTotal)o,System.currentTimeMillis());
                LogUtils.Loge("获取心知天气","成功");
                Message msg = Message.obtain();
                msg.what = ENTER_MAIN;
                mhandler.sendMessage(msg);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("state",API.SUCCESS_NET);
                editor.commit();
            }
        });

//        OkUtils.getInstance().okhttpInstantData(new OkUtils.JObjectCallback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                EventBus.getDefault().post(new LogoEvent("IO异常，检查网络设置"));
//            }
//
//            @Override
//            public void onResponse(Call call, Object o) {
//
//                LogUtils.Loge("获取功率瞬时值","瞬时功率:");
//            }
//        });
//
//        try {
//            String add = "date=" + time + "&city=" + URLEncoder.encode(API.CITY_TEST, "utf-8");
//            OkUtils.getInstance().okhttpRefreshData(API.PATH_DATAFIRST + add, new OkUtils.JObjectCallback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    EventBus.getDefault().post(new LogoEvent("IO异常，检查网络设置"));
//                    enterMainActivity();
//                }
//
//                @Override
//                public void onResponse(Call call, Object o){
//                    SPUtils.updatePVSP(sp, (DetailData) o, System.currentTimeMillis());
//                    getdTime();
//                    enterMainActivity();
//                }
//            });
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
////            getdTime();
//            LogUtils.showToast(LogoActivity.this, "城市拼写错误，请重试", 0);
//            enterMainActivity();
//        }

    }

    /**
     * 进入时间未超过2秒，则凑足两秒后进入
     */
    private void getdTime() {
        LogUtils.Loge(TAG + "判断时间", "时间过了几秒");
        end = System.currentTimeMillis();
        dTime = end - start;
        if (dTime < 2000) {
            SystemClock.sleep(2000 - dTime);
        }
    }

    private void enterMainActivity() {
        Intent intent = new Intent(LogoActivity.this, FirstActivity.class);
        startActivity(intent);
        finish();
    }

    private String getVersionName() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packInfo = pm.getPackageInfo("com.gdut.myproject.PVMonitor", 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void checkVersion() {
        final Message msg = Message.obtain();
        OkUtils.getInstance().okhttpUpdateInfo(API.PATH_UPDATE, new OkUtils.JObjectCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                msg.what = ERROR_IO;
            }

            @Override
            public void onResponse(Call call, Object o) {
                version = ((UpdateInfo) o).getVersion();
                description = ((UpdateInfo) o).getDescription();
                apkurl = ((UpdateInfo) o).getApkurl();
                if (version != getVersionName()) {
                    msg.what = SHOWDIALOG;
                }
            }
        });

        mhandler.sendMessage(msg);
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
