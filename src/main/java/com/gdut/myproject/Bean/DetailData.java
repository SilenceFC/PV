package com.gdut.myproject.Bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/23.
 * 存放天气信息
 */
public class DetailData implements Serializable {
    private Float thisDay;
    private Float thisMonth;
    private Float thisYear;

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Float getThisDay() {
        return thisDay;
    }

    public void setThisDay(Float thisDay) {
        this.thisDay = thisDay;
    }

    public Float getThisMonth() {
        return thisMonth;
    }

    public void setThisMonth(Float thisMonth) {
        this.thisMonth = thisMonth;
    }

    public Float getThisYear() {
        return thisYear;
    }

    public void setThisYear(Float thisYear) {
        this.thisYear = thisYear;
    }

    private Float total;

    private boolean isExist;

    public boolean isExist() {
        return isExist;
    }

    public void setIsExist(boolean isExist) {
        this.isExist = isExist;
    }

}
