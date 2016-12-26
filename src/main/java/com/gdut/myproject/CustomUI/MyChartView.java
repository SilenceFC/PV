package com.gdut.myproject.CustomUI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gdut.myproject.utils.LogUtils;
import com.gdut.myproject.utils.MyComparator;

/**
 * Created by Administrator on 2016/5/16.
 */
public class MyChartView extends View {

    private static final String TYPE = "宋体";
    private int height = 0;
    private int width = 0;
    private int originX = 50;//距离屏幕左边和底部均为30；
    private int unit = 3;
    private int pjValue;
    private int scale = 0;
    private int max;

    private Paint paint;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    private List<Integer> keys = null;
    private List<Integer> xList = null;
    private HashMap<Integer, Float> map;
    private Point[] mPoints = null;

    public HashMap<Integer, Float> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer, Float> map) {
        this.map = map;
    }
    @Override

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = getPaint(Paint.ANTI_ALIAS_FLAG, Color.GREEN, 2, Paint.Style.STROKE);

        initView(paint, canvas);

        if (map != null) {
            drawXScale(paint, canvas);
        }


    }

    private Paint getPaint(int flag,int color,int width,Paint.Style style){
        Paint p = new Paint(flag);
        p.setColor(color);
        p.setStrokeWidth(width);
        p.setStyle(style);
        return p;
    }

    /**
     * 画出X轴的刻度，并将X轴的坐标放入List中,然后有给定点画出折线图
     */

    private void drawXScale(Paint paint, Canvas canvas) {
        scale = pjValue*3/max;
        xList = new ArrayList<>();
        keys = getKeyFromMap(map);

        if(keys ==null){
            return;
        }

        for (int i = 0; i < keys.size(); i++) {
            LogUtils.Loge("个数","keys:"+keys.size());
            int x = originX+(int)(i*(width-originX-25)/keys.size());
            xList.add(x);
            canvas.drawLine(x,height-originX,x,height-originX-10,paint);
            drawText(String.valueOf(keys.get(i)),x,height-10,canvas);
        }
        mPoints = getPoint(keys,xList,map);
        drawChart(mPoints, canvas);
    }

    /**
     * 根据给定点画折线
     * @param mPoints
     * @param canvas
     */
    private void drawChart(Point[] mPoints, Canvas canvas) {
        Paint p =  getPaint(Paint.ANTI_ALIAS_FLAG, Color.YELLOW, 2, Paint.Style.STROKE);

        Path path = new Path();
        Point startP;
        Point endP;
        path.moveTo(mPoints[0].x,height - originX);
        for (int i = 0; i < mPoints.length-1; i++) {
            path.lineTo(mPoints[i].x,mPoints[i].y);
//            startP = mPoints[i];
//            endP = mPoints[i + 1];
//
//            int aver = (startP.x + endP.x) / 2;
//            path.moveTo(startP.x, startP.y);
//            path.cubicTo(aver, startP.y, aver, endP.y, endP.x, endP.y);
        }
        path.lineTo(mPoints[mPoints.length-1].x,height - originX);
        p.setStyle(Paint.Style.FILL);
        canvas.drawPath(path,p);
    }

    /**
     * 按照map中的key-value，获得各个点的坐标
     * @param keys
     * @param xList
     * @param map
     * @return
     */
    private Point[] getPoint(List<Integer> keys, List<Integer> xList, HashMap<Integer, Float> map) {
        Point[] points = new Point[keys.size()];
        for (int i = 0; i < keys.size(); i++) {
            int y = height-originX-(int) (map.get(keys.get(i))*scale);
            points[i] = new Point(xList.get(i),y);
        }
            return points;
    }

    /**
     * 将map中的key取出来
     * @param map
     * @return
     */
    private List<Integer> getKeyFromMap(HashMap<Integer, Float> map) {
        List<Integer> mKeys = new ArrayList<Integer>();
        if (map == null)
            return null;

        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            mKeys.add((Integer) mapEntry.getKey());
        }

        MyComparator comparator = new MyComparator();
        Collections.sort(mKeys,comparator);
        return mKeys;
    }

    /**
     * 初始化折线图，画出折线图的x,y轴
     *
     * @param paint
     * @param canvas
     */
    private void initView(Paint paint, Canvas canvas) {
        height = getHeight();
        width = getWidth();
        pjValue = (height - originX - 45) / 3;

        //y轴及箭头
        canvas.drawLine(originX, height - originX, originX, 10, paint);
        canvas.drawLine(originX, 10, originX - 5, 15, paint);
        canvas.drawLine(originX, 10, originX + 5, 15, paint);

        for (int i = 1; i <= unit; i++) {
            canvas.drawLine(originX, height - originX - i * pjValue, originX + 10, height - originX - i * pjValue, paint);
            drawText(i*5+"",originX-25,height - originX - i * pjValue,canvas);
        }
        drawText("(kW)", originX + 25, 20, canvas);

        //X轴及箭头
        canvas.drawLine(originX, height - originX, width - 10, height - originX, paint);
        canvas.drawLine(width - 10, height - originX, width - 15, height - originX + 5, paint);
        canvas.drawLine(width - 10, height - originX, width - 15, height - originX - 5, paint);
    }

    /**
     * @param content 要写的文本内容;
     * @param x       文本内容起始点
     * @param y
     * @param canvas
     */
    private void drawText(String content, int x, int y, Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.GREEN);
        p.setTextSize(18);
        Typeface font = Typeface.create(TYPE, Typeface.NORMAL);
        p.setTypeface(font);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(content, x, y, p);
    }

    public MyChartView(Context context) {
        super(context);
    }

    public MyChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
