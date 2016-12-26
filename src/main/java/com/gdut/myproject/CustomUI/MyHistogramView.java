package com.gdut.myproject.CustomUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gdut.myproject.utils.LogUtils;
import com.gdut.myproject.utils.MyComparator;

/**
 * Created by Administrator on 2016/5/17.
 */
public class MyHistogramView extends View {
    private static final String TYPE = "宋体";
    private boolean isFirst = true;
    private float max = 0;
    private String unit = "kWh";
    private int originX = 50;//原点x坐标为60

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    private int originY = 0;
    private int height = 0;
    private int width = 0;
    private int num = 5;
    private int x1 = 10;//y轴与第一个柱形条的距离
    private int x2 = 20;//最后一个柱形条与箭头的距离
    private int x3 = 10;//箭头与屏幕右侧距离
    private int[] widths;//根据数据大小动态存放 柱形条宽度和柱形条之间的宽度
    private int w = 0;//柱形条宽度
    private int s = 0;//柱形条间距

    private Paint p,paint;
    private SharedPreferences sp;
    private MyHistogram myHistogram;
    private HashMap<Integer, Float> map = null;
    private HashMap<Float, Float> map1 = null;
    private List<Integer> keys;
    private List<Float> values;

    public HashMap<Integer, Float> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer, Float> map) {
        this.map = map;
    }

    @Override

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
            init();
            paint = getPaint(Paint.ANTI_ALIAS_FLAG, Color.GREEN, 2, Paint.Style.STROKE);
            drawAxisXY(canvas, paint);
            if (map != null) {
                getValueFromMap(map);
                widths = getHistogramWidth(keys.size());
                drawXY(canvas, paint);
                drawHistogram(canvas);
            }
    }

    private void drawHistogram(Canvas canvas) {
        int trueHeight;
        float scale = ((originY - 45) / max);

        myHistogram = new MyHistogram(widths[0], widths[1], originX + x1, originY);
        for (int i = 0; i < values.size(); i++) {
            trueHeight = (int) (values.get(i) * scale);
            if(values.get(i)==(float)(100.0)){
            }
            myHistogram.setI(i);
            myHistogram.setHeight(trueHeight);
            myHistogram.drawHistogram(canvas, p);
        }
    }

    private int[] getHistogramWidth(int size) {
        int[] a = new int[2];
        if (size > 20) {
            s = 5;
            w = (int) (((width - originX - x1 - x2 - x3) - (size - 1) * s) / size);
        } else if (size > 10 && size <= 20) {
            s = 10;
            w = (int) (((width - originX - x1 - x2 - x3) - (size - 1) * s) / size);
        } else if (size > 0 && size <= 10) {
            s = 15;
            w = 30;
        }
        a[0] = s;
        a[1] = w;
        return a;
    }

    /**
     * 画XY轴的刻度
     */
    private void drawXY(Canvas canvas, Paint paint) {

        int a = width - x3 - x2 - x1 - originX;//x轴可用长度 10:x轴与左边的距离，15：一个数据与箭头的距离  20：y轴与第一个柱形条间的距离
        int b = 0 ;
       if(keys.size()!=1){
           b = (int) (a / (keys.size() - 1));
       }
        //每个刻度之间的距离
        int scale = (int) (originY - 45) / num;

        //画X轴的刻度
        for (int i = 0; i < keys.size(); i++) {
            canvas.drawLine(originX + x1 + widths[1] / 2 + i * (widths[0] + widths[1]), originY, originX + x1 + widths[1] / 2 + i * (widths[0] + widths[1]), originY - 5, paint);
            drawText(keys.get(i) + "", originX + x1 + widths[1] / 2 + i * (widths[0] + widths[1]), originY + originX / 2, canvas);
        }
        //画y轴刻度
        for (int j = 1; j <= num; j++) {
            canvas.drawLine(originX, originY - j * scale, originX + 10, originY - j * scale, paint);
            drawText((max / 5) * j + "", originX - 25, originY - j * scale, canvas);
        }

    }

    private void getValueFromMap(HashMap<Integer, Float> map) {
        keys = new ArrayList<>();
        values = new ArrayList<>();
        MyComparator comparator = new MyComparator();
        for (Map.Entry<Integer, Float> mapEntry : map.entrySet()) {
            keys.add(mapEntry.getKey());
        }
        Collections.sort(keys, comparator);
        for (int i = 0; i < keys.size(); i++) {
            values.add(map.get(keys.get(i)));
        }
        for (Float data : values) {
            max = max > data ? max : data;
        }
        if(isFirst){
            max = maxChanged(max);
            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat("max",max);
            editor.commit();
            isFirst = false;
        }else {
            isFirst = true;
            max = sp.getFloat("max",0);
        }
    }

    private float maxChanged(float max) {
        String dataString = String.valueOf(max);
        String dataInt = dataString.substring(0, dataString.lastIndexOf("."));
        int length = dataInt.length();
        switch (length) {
            case 1:
                max = 10;
                break;
            case 2:
                max = ((int) (max / 10) + 1) * 10;
                break;
            case 3:
                if( (((int) (max / 10)) % 10)==0){
                    LogUtils.Loge("max = ",max+"");
                }else {
                    max = (int) (max / 100) * 100 + (((int) (max / 10)) % 10 + 1) * 10;
                }
                break;
        }
        return max;
    }

    /**
     * 画出X，Y轴，标出Y轴的单位
     *
     * @param canvas
     * @param paint
     */
    private void drawAxisXY(Canvas canvas, Paint paint) {

        //画Y轴及箭头
        canvas.drawLine(originX, originY, originX, 10, paint);
        canvas.drawLine(originX, 10, originX - 5, 15, paint);
        canvas.drawLine(originX, 10, originX + 5, 15, paint);

        drawText("(" + unit + ")", originX + 25, 20, canvas);

        //画X轴及箭头
        canvas.drawLine(originX, originY, width - 10, originY, paint);
        canvas.drawLine(width - 10, originY, width - 15, originY - 5, paint);
        canvas.drawLine(width - 10, originY, width - 15, originY + 5, paint);
    }

    private void drawText(String content, int x, int y, Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.BLUE);
        p.setTextSize(18);
//        p.setTextSkewX((float)(2.0)); 倾斜因子
        Typeface font = Typeface.create(TYPE, Typeface.NORMAL);
        p.setTypeface(font);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(content, x, y, p);
    }

    private Paint getPaint(int flag, int color, int width, Paint.Style style) {
        Paint p = new Paint(flag);
        p.setColor(color);
        p.setStrokeWidth(width);
        p.setStyle(style);
        return p;
    }

    /**
     * 获得视图的高度和宽度，实例化MyHistogram;
     */
    private void init() {
        p= getPaint(Paint.ANTI_ALIAS_FLAG, Color.GREEN, 2, Paint.Style.FILL_AND_STROKE);
        sp = getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        height = getHeight();
        width = getWidth();
        originY = height - originX;
        widths = new int[2];
    }

    public MyHistogramView(Context context) {
        super(context);
    }

    public MyHistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHistogramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
