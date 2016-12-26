package com.gdut.myproject.utils;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.gdut.myproject.Bean.API;
import com.gdut.myproject.Bean.DetailData;
import com.gdut.myproject.Bean.HotWeather;
import com.gdut.myproject.Bean.UpdateInfo;
import com.gdut.myproject.Bean.WeatherTotal;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Administrator on 2016/9/12.
 * 此类封装了okhttp的一些方法，传入相应的参数即可
 */
public class OkUtils {
    private static OkUtils instance = null;
    private Call mCall;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private Gson mGson;
    private StringBuffer sb;
    private BufferedReader br;
    private HttpURLConnection conn;

    //单例模式,外界无法实例化OkUtils
    private OkUtils() {
        mOkHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());//获取主UI线程的looper，意思是将handler操作放在UI线程中
        mGson = new Gson();
    }

    public static synchronized OkUtils getInstance() {
        if (instance == null) {
            instance = new OkUtils();
        }
        return instance;
    }

    /**
     * 用于获取APP更新信息。请求成功，回调callback的onResponse方法，将获得的json数据返回
     *
     * @param url      下载地址
     * @param callback 回调方法
     */
    public void okhttpUpdateInfo(String url, final JObjectCallback callback) {
        final Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                UpdateInfo updateInfo = mGson.fromJson(response.body().string(), UpdateInfo.class);
                callback.onResponse(call, updateInfo);
            }
        });
    }

    public void okhttpSunRise(String city,final JObjectCallback callback){
        try {

            final Request request = new Request.Builder().url(API.PATH_SUN+URLEncoder.encode(city,"utf-8")+"&key=1dc105b2d5634cfaaa67350514981121").build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onFailure(call,e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonstr = response.body().string().replace(" ","").replace("3.0","");
                    LogUtils.Loge("和风天气","json:"+jsonstr);
                    HotWeather hw = mGson.fromJson(jsonstr,HotWeather.class);
                    HotWeather.HeWeatherdataserviceBean hwb = hw.getHeWeatherdataservice().get(0);

                    WeatherTotal wt = createWeatherTotal(hwb);

                    callback.onResponse(call,wt);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private WeatherTotal createWeatherTotal( HotWeather.HeWeatherdataserviceBean hwb){
        WeatherTotal wt = new WeatherTotal();
        HotWeather.HeWeatherdataserviceBean.BasicBean bb = hwb.getBasic();
       HotWeather.HeWeatherdataserviceBean.DailyForecastBean dailybean = hwb.getDaily_forecast().get(0);
        HotWeather.HeWeatherdataserviceBean.NowBean NB = hwb.getNow();
        wt.setCity(bb.getCity());
        wt.setUpdatetime(bb.getUpdate().getLoc());
        wt.setSunrise(dailybean.getAstro().getSr());
        wt.setSunset(dailybean.getAstro().getSs());
        wt.setTemp_h(dailybean.getTmp().getMax());
        wt.setTemp_l(dailybean.getTmp().getMin());
        wt.setWeather(NB.getCond().getTxt());
        wt.setTemp(NB.getTmp());
        wt.setWind(NB.getWind().getDir());
        wt.setWindspeed(NB.getWind().getSpd());
        wt.setVis(NB.getVis());
        wt.setPm25(hwb.getAqi().getCity().getPm25());

        return wt;
    }

    public void okhttpInstantData(final JObjectCallback callback){
        final Request request = new Request.Builder().url(API.PATH_INSTANT).build();
        Call call = mOkHttpClient.newCall(request);
        LogUtils.Loge("获取瞬时json","执行方法");
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.Loge("获取瞬时json","失败，IO异常");
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.Loge("获取瞬时json","：：："+response.body().string());
                callback.onResponse(call, response);
            }
        });
    }
    /**
     * 用于获取天气情况 json格式
     *
     * @param url
     * @param callback
     */
    public void okhttpRefreshData(String url, final JObjectCallback callback) {
        final Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                DetailData detailData = mGson.fromJson(response.body().string(), new TypeToken<DetailData>() {
                }.getType());
                detailData.setIsExist(true);
                callback.onResponse(call, detailData);
            }
        });
    }

    public interface JObjectCallback {
        void onFailure(Call call, IOException e);

        void onResponse(Call call, Object o);
    }

    public interface JObjectCallbackForWeather {
        void onResponse(Object o);
    }

    public void okhttpCurrentData(String url, final JsoupCallback jsoupCallback) {
        final Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                jsoupCallback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                jsoupCallback.onResponse(call, parse(getDataFromStream(response.body().byteStream())));
            }
        });
    }

    protected List<String> parse(String data) {
        List<String> datas = new ArrayList<>();
        org.jsoup.nodes.Document doc = Jsoup.parse(data);
        Element infotable = doc.getElementsByTag("table").first();
        StringBuffer sb = new StringBuffer();
        Elements tablelineinfo = infotable.select("tr");
        for (Element lineinfo : tablelineinfo) {
            Elements lineinfoContent = lineinfo.select("td");

            for (int i = 0; i < lineinfoContent.size(); i++) {
                datas.add(lineinfoContent.get(i).text().trim());
                sb.append(lineinfoContent.get(i).text().trim() + "\n");
            }
        }
        return datas;
    }

    private String getDataFromStream(InputStream is) {
        try {
            byte[] buffer = new byte[1024 * 5];
            is.read(buffer);
            String codetext = new String(buffer, "unicode");
            is.close();
            codetext = new String(codetext.getBytes(), "utf-8");
            Log.e("attention", "html被解析");
            return codetext;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    public interface JsoupCallback {
        void onFailure(Call call, IOException e);

        void onResponse(Call call, List<String> list);
    }

    /**
     * 用于下载文件，目前有BUG
     *
     * @param tv
     * @param url
     * @param version
     * @param fileCallback
     */
    public void xutilsDownloadFile(final TextView tv, String url, final String version, final FileCallback fileCallback) {
        HttpUtils http = new HttpUtils();
        http.download(HttpRequest.HttpMethod.GET, url, Environment.getExternalStorageDirectory() + "PVMonitor" + version + ".apk",
                null, new RequestCallBack<File>() {
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        tv.setVisibility(View.VISIBLE);
                        tv.setText("下载进度：" + (current / total) * 100 + "%");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        tv.setVisibility(View.GONE);
                        fileCallback.onResponse(responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                });
    }

    public interface FileCallback {
        void onFailure(IOException e);

        void onResponse(File file);
    }

    /**
     * 取消线程中的下载任务
     *
     * @param instance
     */
    public void cancelAll(OkUtils instance) {
        if (instance != null && mCall != null && !mCall.isCanceled()) {
            instance.mCall.cancel();
        }
    }
}
