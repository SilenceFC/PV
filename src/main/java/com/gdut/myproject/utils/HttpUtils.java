package com.gdut.myproject.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.gdut.myproject.Bean.DetailData;


/**
 * Created by Administrator on 2016/5/19.
 */
public class HttpUtils {
    private static final String path = "http://124.16.2.182:8080/MyPVMonitorServlet/servlet/WebServlet?";
    private  BufferedReader br;
    private  StringBuilder sb;
    private  HttpURLConnection conn;
    private  URL url;
    private  String jsonStr;
    private DetailData specialDD;
    private Context mContext;
    private android.os.Handler handler ;

    public HttpUtils(Context context, android.os.Handler handler){
        this.mContext = context;
        this.handler = handler;
        specialDD = new DetailData();
        specialDD.setIsExist(false);
    }

    public HttpUtils(Context context){
        this.mContext = context;
        specialDD = new DetailData();
        specialDD.setIsExist(false);
    }

    public   DetailData getJsonFromHttp(String query,String city) {
        try {
            String add = "date="+query+"&city="+ URLEncoder.encode(city,"utf-8");

            url = new URL(path+add);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.connect();
            LogUtils.Loge("httpUtils", (conn.getInputStream() == null) + "");
            jsonStr = getJsonFromStream(conn.getInputStream());
            LogUtils.Loge("httpUtils", "JSONsTR"+jsonStr);
        } catch (MalformedURLException e) {
            LogUtils.Loge("httpUtils", "url异常");
            e.printStackTrace();
            return specialDD;
        } catch (IOException e) {
            LogUtils.Loge("httpUtils", "IO异常");
            e.printStackTrace();
            return specialDD;
        }finally {
            conn.disconnect();
        }
        return parseJson(jsonStr);
    }

    private  DetailData parseJson(String jsonStr) {
        Gson gson = new Gson();
        DetailData datas= gson.fromJson(jsonStr,new TypeToken<DetailData>(){}.getType());
        datas.setIsExist(true);
        return datas;
    }

    private  String getJsonFromStream(InputStream inputStream) throws IOException {
        try {
            br = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
            sb = new StringBuilder();
            String content = "";
            while ((content = br.readLine()) != null) {
                sb.append(content+"\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            br.close();
        }
        return sb.toString();
    }
}
