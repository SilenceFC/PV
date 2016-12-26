package com.gdut.myproject.Bean;

/**
 * Created by Administrator on 2016/9/12.
 */
public class API {
    public static final int TYPE_LINE = 0;
    public static final int TYPE_BAR_DAY = 1;
    public static final int TYPE_BAR_MONTH = 2;
    public static final String ADDRESS = "124.16.2.182";
    public static final String ADDRESS_WIFI = "192.168.115.182";
    public static final int PORT = 30000;
    public static final String CITY_TEST ="广州";
    public static final String CITY_WORK ="博罗";
    public static final String PATH_UPDATE ="http://124.16.2.182:8080/pvupdate.html";
    public static final String PATH_DATAFIRST = "http://124.16.2.182:8080/MyPVMonitorServlet/servlet/WebServlet?";
    public static final String PATH_PVDATA = "http://124.16.2.182:8080/MyPVMonitorServlet/servlet/Data_Statics?date=";
    public static final String PATH_INSTANT = "http://124.16.2.182/MyPVMonitorServlet/servlet/InstantPower";
    public static final String PATH_CURRENT = "http://124.16.2.182:8080/MPPT.html";
    public static final String PATH_SUN = "https://api.heweather.com/x3/weather?city=";
}
