package com.gdut.myproject.CustomUI;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Administrator on 2016/5/17.
 */
public class MyHistogram {
    private int width;//柱形条的宽度
    private int height;//柱形条的高度
    private int originX = 60;
    private int originY = 0;
    private int i ;
    private int s ;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public MyHistogram(int s,int width,int originX,int originY){
        this.s = s;
        this.width = width;
        this.originX = originX;
        this.originY = originY;
    }
    /**
     * 绘制矩形条
     */
    public void drawHistogram(Canvas canvas,Paint paint){
        canvas.drawRect(originX+i*(s+width),originY-height,originX+width+i*(s+width),originY,paint);
    }
}
