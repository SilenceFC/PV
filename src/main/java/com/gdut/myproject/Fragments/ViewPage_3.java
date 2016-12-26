package com.gdut.myproject.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdut.myproject.PVMonitor.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.gdut.myproject.greendao.MonthData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.gdut.myproject.Bean.API;
import com.gdut.myproject.CustomUI.MyHistogramView;
import com.gdut.myproject.utils.ChartDataUtil;
import com.gdut.myproject.utils.DbService;
import com.gdut.myproject.utils.SetBackGround;

/**
 * Created by Administrator on 2016/5/9.
 */
public class ViewPage_3 extends Fragment {
    LinearLayout ll_view3;
    BarChart barChart;
    TextView tv ;
    
    SharedPreferences sp;
    SimpleDateFormat sdf;
    DbService db;
    
    HashMap<Integer,Float> map;
    MyHistogramView myHistogramView;
    
    List<MonthData> list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.pager3,null);
        Log.e("test", "page3的页面");
        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        initView(v);
        initChart();
        drawChart();
        return v;
    }

    private void initChart() {
        barChart.setDescription("");
        barChart.setNoDataTextDescription("暂无可用数据");
        barChart.setDragEnabled(false);
        barChart.setTouchEnabled(true);
        barChart.setDrawBorders(false);
        barChart.setBackgroundColor(Color.TRANSPARENT);
        barChart.setGridBackgroundColor(Color.TRANSPARENT);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        barChart.getAxisRight().setEnabled(false);//右侧不显示Y轴
        barChart.getAxisLeft().setAxisMinValue(0.0f);//不设置的话下方会有空格
        barChart.animateY(1000); // 立即执行的动画,Y轴
    }

    private void drawChart() {
        ChartDataUtil util = new ChartDataUtil(API.TYPE_BAR_MONTH);
        BarData barData = new BarData(util.getBarXValues(),util.getBarYValues());
        barChart.setData(barData);
        barChart.invalidate();
    }

    private void initView(View v) {
        ll_view3 = (LinearLayout) v.findViewById(R.id.ll_view3);
        ll_view3.setBackgroundResource(SetBackGround.setLlBackground(sp));

        sdf = new SimpleDateFormat("yyyy-MM");
        tv = (TextView) v.findViewById(R.id.tv_yearElect1);
        tv.setText("年发电量" + "\n" + sdf.format(new Date()));
        barChart = (BarChart) v.findViewById(R.id.pager3_barchart);
        //        myHistogramView = (MyHistogramView) v.findViewById(R.id.myHistogramView1);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        db = DbService.getInstance();
//        list =  db.getSpecifiedMonthData();
//
//        map = new HashMap<>();
//
//        int key;
//        float value;
//
//        for (int i = 0; i < list.size(); i++) {
//            key = Integer.parseInt(list.get(i).getMonth().substring(5)) ;
//            LogUtils.Loge("year---key的值", "key:" + key);
//            value = Float.parseFloat(list.get(i).getMonthtotal());
//            map.put(key,value);
//        }
//        map = new HashMap<>();
//        map.put(1, (float) 25);
//        map.put(2,(float)37);
//        map.put(3,(float)58);
//        map.put(4,(float)98);
//        map.put(5,(float)100);
//        map.put(6,(float)32);
//        map.put(7, (float) 66);
//        map.put(8, (float) 80);
//        map.put(9,(float)55);
//        map.put(10,(float)25);
//        map.put(11,(float)25);
//        map.put(12,(float)37);

//        myHistogramView.setUnit("Mwh");
//        myHistogramView.setMap(map);
//        myHistogramView.postInvalidate();
    }
    public void onDestroy() {
        super.onDestroy();
        Log.e("测试", "ViewPAger3被销毁");
    }
}
