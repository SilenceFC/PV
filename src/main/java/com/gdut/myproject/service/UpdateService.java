package com.gdut.myproject.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.gdut.myproject.utils.ContextUtils;
import com.gdut.myproject.utils.LogUtils;

/**
 * Created by Administrator on 2016/6/1.
 * 用于从服务器获取设备实时数据
 */
public class UpdateService extends Service {

    private String htmltext;
    private String DATA[];

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.Loge("Service服务","已经进入Service");
        // 服务启动时使用
        DATA = new String[11];
        new Thread(){
            public void run() {
                String path = "http://124.16.2.182:8080/MPPT.html";

                try {
                    URL url = new URL(path);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("GET");
                    con.setConnectTimeout(5000);
                    con.setReadTimeout(5000);
                    con.connect();

                    if (con.getResponseCode() == 200) {
                        InputStream is = con.getInputStream();
                        String data = getDataFromStream(is);
                        String[] Data = parse(data);

                        Intent sendintent = new Intent();
                        sendintent.setAction("PVMonitor.DATA_BROADCAST");
                        sendintent.putExtra("data", Data);
                        sendBroadcast(sendintent);
                        Log.e("attentionService", "已获取数据并将数据广播出去");
                    }
                } catch (MalformedURLException e) {
                    Log.e("attenton", "地址异常");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("attenton", "IO链接异常");
                    Toast.makeText(ContextUtils.getInstance(), "网络异常，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            };
        }.start();

        return START_STICKY;
    }

    protected  String[] parse(String data) {

        org.jsoup.nodes.Document doc = Jsoup.parse(data);
        Element infotable = doc.getElementsByTag("table").first();

        Elements tablelineinfo = infotable.select("tr");
        for (Element lineinfo : tablelineinfo) {
            Elements lineinfoContent = lineinfo.select("td");
            for (int i = 0; i < 11; i++) {
                DATA[i] = lineinfoContent.get(i).text().trim();
            }
        }
        return DATA;
    }

    private String getDataFromStream(InputStream is) {
        int buffersize;

        try {
            buffersize = is.available();
            byte[] buffer = new byte[buffersize];
            is.read(buffer);
            String codetext = new String(buffer, "unicode");
            is.close();
            htmltext = new String(codetext.getBytes(), "utf-8");
            Log.e("attention", "html被解析");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return htmltext;
    }

    @Override
    public void onCreate() {
        // 服务创建时初始化壁纸管理器对象
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
