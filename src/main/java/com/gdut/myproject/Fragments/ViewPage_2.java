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
import com.gdut.myproject.greendao.DayData;

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
public class ViewPage_2 extends Fragment {
    LinearLayout ll_view2;
    SharedPreferences sp;
    TextView tv ;
    SimpleDateFormat sdf;
    HashMap<Integer,Float> map ;
    MyHistogramView myHistogramView;
    DbService db;
    List<DayData> list;
    BarChart barChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.pager2,null);
        Log.e("test", "page2的页面");
        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        initView(v);
        initBarChart();
        drawBarChart();
        return v;
    }

    private void drawBarChart() {
        ChartDataUtil util = new ChartDataUtil(API.TYPE_BAR_DAY);
        BarData barData = new BarData(util.getBarXValues(),util.getBarYValues());
        barChart.setData(barData);
        barChart.invalidate();
    }

    private void initBarChart() {
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

        barChart.getAxisRight().setEnabled(false);

        barChart.animateY(1000); // 立即执行的动画,Y轴
    }

    private void initView(View v) {
        ll_view2 = (LinearLayout) v.findViewById(R.id.ll_view2);
        ll_view2.setBackgroundResource(SetBackGround.setLlBackground(sp));

        sdf = new SimpleDateFormat("yyyy-MM");
        tv = (TextView) v.findViewById(R.id.tv_monthElect1);
        //        myHistogramView = (MyHistogramView) v.findViewById(R.id.myHistogramView);
        tv.setText("月发电量"+"\n"+sdf.format(new Date()));
        barChart = (BarChart) v.findViewById(R.id.pager2_barchart);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.e("测试", "ViewPAger2被销毁");
    }
}
