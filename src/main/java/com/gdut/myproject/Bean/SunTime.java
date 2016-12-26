package com.gdut.myproject.Bean;

/**
 * Created by Administrator on 2016/11/2.
 */
public class SunTime {
    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String date = null;
    public String sunrise=null;
    public String sunset = null;
}
