package com.gdut.myproject.Async;

import android.os.AsyncTask;


import com.gdut.myproject.greendao.Data;
import com.gdut.myproject.greendao.DayData;
import com.gdut.myproject.greendao.MonthData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gdut.myproject.utils.DbService;
import com.gdut.myproject.utils.LogUtils;

/**
 * 解析完JSON之后将数据插入到数据库中
 * @param
 */
public class MyAsyncTask extends AsyncTask<String,Void,String> {
    private DbService db;
    public MyAsyncTask (DbService dbService){
        this.db = dbService;
    }
    @Override
    protected String doInBackground(String... params) {
        return getJsonString(params[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        parseJson(s);
    }

    private void parseJson(String s) {
        try {
            JSONObject object;
            JSONObject obj = new JSONObject(s);
            JSONArray array1 = obj.getJSONArray("data1");
            JSONArray array2 = obj.getJSONArray("data2");
            JSONArray array3 = obj.getJSONArray("data3");
            List<Data> datas = new ArrayList<>();
            List<DayData> dayDatas = new ArrayList<>();
            List<MonthData> monthDatas = new ArrayList<>();

//            //存在，什么都不做，不然就插入
//            for (int i = 0; i <array1.length(); i++) {
//                Data data = new Data();
//                object = array1.getJSONObject(i);
//                if(db.isDataExist(object.getString("date"))){
//                    LogUtils.Loge("已存在", object.getString("date") + "已经存在");
//                    continue;
//                }else{
//                    if(Float.parseFloat(object.getString("now"))<0){
//                        data.setNow("0");
//                    }else{
//                        data.setNow(object.getString("now"));
//                    }
//                    data.setDate(object.getString("date"));
//                    datas.add(data);
//                }
//            }
//            db.saveNoteLists(datas);

            //存在就更新，不存在就插入
            for (int i = 0; i <array2.length(); i++) {
                DayData data = new DayData();
                object = array2.getJSONObject(i);
                String day = object.getString("day");
                String daytotal = object.getString("daytotal");
                List<Long> ids;
                if(db.isDayDataExist(day)){
                    ids = db.getExistedDayId(day);
                    for (int j = 0; j <ids.size() ; j++) {
                        data.setId(ids.get(j));
                        data.setDay(day);
                        data.setDaytotal(daytotal);
                        //更新daydata;
                        db.updateDayData(data);
                    }
                }else{
                    data.setDay(day);
                    data.setDaytotal(daytotal);
                    dayDatas.add(data);
                }
            }
            db.saveDayNoteLists(dayDatas);

            //存在就更新，不存在就插入
            for (int i = 0; i <array3.length(); i++) {
                MonthData data = new MonthData();
                object = array3.getJSONObject(i);
                String month = object.getString("month");
                String monthtotal = object.getString("monthtotal");
                List<Long> ids;
                if(db.isMonthDataExist(month)){
                    ids = db.getExistedMonthId(month);
                    for (int j = 0; j <ids.size() ; j++) {
                        data.setId(ids.get(j));
                        data.setMonth(month);
                        data.setMonthtotal(monthtotal);
                        //更新daydata;
                        db.updateMonthData(data);
                    }
                }else{
                    data.setMonth(month);
                    data.setMonthtotal(monthtotal);
                    monthDatas.add(data);
                }
            }
            db.saveMonthNoteLists(monthDatas);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getJsonString(String param) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(param).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.connect();

            String jsonString = getStringFromStream(conn.getInputStream());
            LogUtils.Loge("MyAsyncTask:获得JsonString","数据:"+jsonString);
            return jsonString;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getStringFromStream(InputStream is) {
        String content = "";
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
            while ((content=br.readLine())!=null){
                sb.append(content+"\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
