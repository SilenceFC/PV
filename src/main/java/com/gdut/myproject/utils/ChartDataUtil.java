package com.gdut.myproject.utils;

import android.graphics.Color;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.gdut.myproject.greendao.DayData;
import com.gdut.myproject.greendao.InstantData;
import com.gdut.myproject.greendao.MonthData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */
public class ChartDataUtil {
    private DbService db;
    private int dataType;

    private List<InstantData> datas;
    private List<DayData> dayDatas;
    private List<MonthData> monthDatas;


    private ArrayList<String> xLabel;
    private ArrayList<LineDataSet> lineSets;
    private ArrayList<BarDataSet> barSets;
    private ArrayList<Entry> entries;
    private ArrayList<BarEntry> barEntries;

    public ChartDataUtil(int type){
        db = DbService.getInstance();
        dataType = type;
        switch (dataType){
            case 0:
                datas = db.getSpecifiedData();
                break;
            case 1:
                dayDatas = db.getSpecifiedDayData();
                break;
            case 2:
                monthDatas = db.getSpecifiedMonthData();
                break;
        }
    }


    public  ArrayList<String> getLineXValues(){
        ArrayList<String> xLabel = new ArrayList<>();

        for (int i = 0;i<datas.size();i++){
            String x = datas.get(i).getTime().substring(11).replace("-",":");
            xLabel.add(x);
        }
        return xLabel;
    }

    public  ArrayList<LineDataSet> getLineYValues(){
        lineSets = new ArrayList<>();
        entries = new ArrayList<>();
        for (int i = 0;i<datas.size();i++){
            float value = Float.parseFloat(datas.get(i).getInstantvalue());
            Entry entry = new Entry(value,i);
            entries.add(entry);
        }
        LineDataSet lineDataSet = new LineDataSet(entries,"实时功率");
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setColor(Color.YELLOW);
        lineDataSet.setHighLightColor(Color.RED);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        // 设置填充
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawCubic(true);
        lineDataSet.setCubicIntensity(0.1f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.YELLOW);


        lineSets.add(lineDataSet);
        return lineSets;
    }

    public  ArrayList<String> getBarXValues() {
        ArrayList<String> xLabel = new ArrayList<>();
        if (dataType == 1) {
            for (int i = 0; i < dayDatas.size(); i++) {
                String x = dayDatas.get(i).getDay().substring(5).replace("-", ".");
                xLabel.add(x);
            }
        } else {
            for (int i = 0; i < monthDatas.size(); i++) {
                String x = monthDatas.get(i).getMonth().substring(5);
                xLabel.add(x);
            }
        }
        return xLabel;
    }

        public ArrayList<BarDataSet> getBarYValues (){
            barSets = new ArrayList<>();
            barEntries = new ArrayList<>();
            BarDataSet barDataSet;
            if (dataType == 1) {
                for (int i = 0; i < dayDatas.size(); i++) {
                    float value = Float.parseFloat(dayDatas.get(i).getDaytotal());
                    BarEntry entry = new BarEntry(value, i);
                    barEntries.add(entry);
                }
                barDataSet = new BarDataSet(barEntries, "日发电量");
            }else{
                for (int i = 0; i < monthDatas.size(); i++) {
                    float value = Float.parseFloat(monthDatas.get(i).getMonthtotal());
                    BarEntry entry = new BarEntry(value, i);
                    barEntries.add(entry);
                }
                barDataSet = new BarDataSet(barEntries, "月发电量");
            }

            barDataSet.setHighlightEnabled(true);
            barDataSet.setColor(Color.YELLOW);
            barDataSet.setHighLightColor(Color.RED);
            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            barDataSet.setDrawValues(true);

            barSets.add(barDataSet);
            return barSets;
        }

}
