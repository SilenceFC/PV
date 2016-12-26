package com.gdut.myproject.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdut.myproject.PVMonitor.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.gdut.myproject.greendao.Data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.gdut.myproject.Bean.API;

import com.gdut.myproject.utils.ChartDataUtil;
import com.gdut.myproject.utils.DbService;

import com.gdut.myproject.utils.SetBackGround;


/**
 * Created by Administrator on 2016/5/9.
 */
public class ViewPage_1 extends Fragment implements View.OnClickListener {
    Context mContext;
    LinearLayout ll_view1;
//    MyChartView myChartView;
    LineChart pager1_linechart;

    TextView tv_titlePower;
    SimpleDateFormat sdf;
    HashMap<Integer, Float> map;
    SharedPreferences sp;
    DbService db;
    List<Data> list;
    Calendar cal_now,cal_select;
    int mYear,mMonth,mDay;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.pager1, null);
        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
//        myChartView = (MyChartView) v.findViewById(R.id.myChartView);
        initView(v,sp);
        initChart(pager1_linechart);
        drawChart(pager1_linechart);
        return v;
    }

    private void drawChart(LineChart mChart) {
        //为折线图添加数据
        ChartDataUtil util = new ChartDataUtil(API.TYPE_LINE);
        LineData data = new LineData(util.getLineXValues(), util.getLineYValues());
        mChart.setData(data);
        mChart.invalidate();
    }

    private void initChart(LineChart mChart) {
        mChart.setDescription("");
        mChart.setDescriptionColor(Color.RED);

        mChart.setDrawGridBackground(false);
        mChart.getAxisRight().setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        //: 设置标签字符间的空隙，默认characters间隔是4
        // setSpaceBetweenLabels(int characters)
        //避免减掉第一个或者最后一个标签值
        xAxis.setLabelsToSkip(2);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setStartAtZero(true);
        yAxis.setSpaceTop(20f);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setLabelCount(12,false);
    }

    /**
     * 初始化界面，包括MPAndroid控件，线性布局背景，页面标题
     * @param v
     * @param sp
     */
    private void initView(View v, SharedPreferences sp) {
        pager1_linechart = (LineChart) v.findViewById(R.id.pager1_linechart);

        ll_view1 = (LinearLayout) v.findViewById(R.id.ll_view1);
        ll_view1.setBackgroundResource(SetBackGround.setLlBackground(sp));
        Log.e("test", "page1的页面");
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        tv_titlePower = (TextView) v.findViewById(R.id.tv_titlePower);
        tv_titlePower.setText("功率" + "\n" + sdf.format(new Date()));
        mContext = getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        startActivityForResult(new Intent(),2);
//        db = DbService.getInstance(getContext());

        tv_titlePower.setOnClickListener(this);

//        list = db.getSpecifiedData();
//        int sunrise =Integer.parseInt(sp.getString("sunrise", "05:00").substring(0,2)) ;
//        int sunset =Integer.parseInt(sp.getString("sunset","21:00").substring(0,2));
//        map = new HashMap<>();
//
//        int key = 0;
//        float value = 0;
//        if (list.size() == 0) {
//            map.put(key, value);
//        } else {
//            for (int i = 0; i < list.size(); i++) {
//                key = Integer.parseInt(list.get(i).getDate().substring(11, 13));
//                LogUtils.Loge("key的值", "key:" + key);
//                value = Float.parseFloat(list.get(i).getNow());
//                if(key>=sunrise&&key<(sunset+1)){
//                    map.put(key, value);
//                }
//            }
//        }
//        myChartView.setMax(15);
//        myChartView.setMap(map);
//        myChartView.postInvalidate();
    }

    public void onDestroy() {
        super.onDestroy();
        Log.e("测试", "ViewPAger1被销毁");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_titlePower:
              cal_now = Calendar.getInstance();
                cal_select = Calendar.getInstance();
                mYear =  cal_now.get(Calendar.YEAR);
                mMonth =  cal_now.get(Calendar.MONTH);
                mDay =  cal_now.get(Calendar.DAY_OF_MONTH);
                cal_now.set(mYear,mMonth,mDay);
                new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        cal_select.set(year,monthOfYear,dayOfMonth);
                        if(judgeDataIsExist(cal_now,cal_select)){
                            Toast.makeText(mContext,"存在数据",Toast.LENGTH_SHORT).show();
                        }

                        //根据时间查询相应的光伏数据
                    }
                }, cal_now.get(Calendar.YEAR), cal_now.get(Calendar.MONTH), cal_now.get(Calendar.DAY_OF_MONTH)).show();
            break;
        }
    }

    private boolean judgeDataIsExist(Calendar now, Calendar select) {
       if (now.compareTo(select)!=-1) return true;
        return false;
    }

}
