package com.gdut.myproject.Bean;

import com.gdut.myproject.greendao.DayData;
import com.gdut.myproject.greendao.InstantData;
import com.gdut.myproject.greendao.MonthData;

import java.util.List;

/**
 * Created by Administrator on 2016/11/10.
 */
public class PVBean {

    /**
     * time : 2016-11-04 05-47
     * instantvalue : 6.000
     */

    private List<InstantData> data0;
    /**
     * thisDay : 0
     * thisMonth : -0
     * thisYear : 61.796
     * total : 61.796
     */

    private List<DetailData> data1;
    /**
     * id : 0
     * day : 2016-11-04
     * daytotal : 0
     */

    private List<DayData> data2;
    /**
     * id : 0
     * month : 2016-05
     * monthtotal : 45.195
     */

    private List<MonthData> data3;

    public List<InstantData> getData0() {
        return data0;
    }

    public void setData0(List<InstantData> data0) {
        this.data0 = data0;
    }

    public List<DetailData> getData1() {
        return data1;
    }

    public void setData1(List<DetailData> data1) {
        this.data1 = data1;
    }

    public List<DayData> getData2() {
        return data2;
    }

    public void setData2(List<DayData> data2) {
        this.data2 = data2;
    }

    public List<MonthData> getData3() {
        return data3;
    }

    public void setData3(List<MonthData> data3) {
        this.data3 = data3;
    }

//    public static class InstantData {
//        private String time;
//        private String instantvalue;
//        private String timerange = null;
//        
//        public String getTimerange() {
//    		return timerange;
//    	}
//    	public void setTimerange(String timerange) {
//    		this.timerange = timerange;
//    	}
//    	
//        public String getTime() {
//            return time;
//        }
//
//        public void setTime(String time) {
//            this.time = time;
//        }
//
//        public String getInstantvalue() {
//            return instantvalue;
//        }
//
//        public void setInstantvalue(String instantvalue) {
//            this.instantvalue = instantvalue;
//        }
//    }
//
//    public static class DetailData {
//        private float thisDay;
//        private float thisMonth;
//        private float thisYear;
//        private float total;
//
//        public float getThisDay() {
//            return thisDay;
//        }
//
//        public void setThisDay(float thisDay) {
//            this.thisDay = thisDay;
//        }
//
//        public float getThisMonth() {
//            return thisMonth;
//        }
//
//        public void setThisMonth(float thisMonth) {
//            this.thisMonth = thisMonth;
//        }
//
//        public float getThisYear() {
//            return thisYear;
//        }
//
//        public void setThisYear(float thisYear) {
//            this.thisYear = thisYear;
//        }
//
//        public float getTotal() {
//            return total;
//        }
//
//        public void setTotal(float total) {
//            this.total = total;
//        }
//    }
//
//    public static class DayData {
//        private int id;
//        private String day;
//        private String key;
//        private float daytotal;
//
//        public String getKey() {
//    		return key;
//    	}
//    	public void setKey(String key) {
//    		this.key = key;
//    	}
//    	
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getDay() {
//            return day;
//        }
//
//        public void setDay(String day) {
//            this.day = day;
//        }
//
//        public float getDaytotal() {
//            return daytotal;
//        }
//
//        public void setDaytotal(float daytotal) {
//            this.daytotal = daytotal;
//        }
//    }
//
//    public static class MonthData {
//        private int id;
//        private String month;
//        private float monthtotal;
//        private String key;
//    	
//    	public String getKey() {
//    		return key;
//    	}
//    	public void setKey(String key) {
//    		this.key = key;
//    	}
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getMonth() {
//            return month;
//        }
//
//        public void setMonth(String month) {
//            this.month = month;
//        }
//
//        public float getMonthtotal() {
//            return monthtotal;
//        }
//
//        public void setMonthtotal(float monthtotal) {
//            this.monthtotal = monthtotal;
//        }
//    }
}
