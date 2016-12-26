package com.gdut.myproject.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "MONTH_DATA".
 */
public class MonthData {

    private Long id;
    private String key;
    /** Not-null value. */
    private String month;
    private String monthtotal;

    public MonthData() {
    }

    public MonthData(Long id) {
        this.id = id;
    }

    public MonthData(Long id, String key, String month, String monthtotal) {
        this.id = id;
        this.key = key;
        this.month = month;
        this.monthtotal = monthtotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /** Not-null value. */
    public String getMonth() {
        return month;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonthtotal() {
        return monthtotal;
    }

    public void setMonthtotal(String monthtotal) {
        this.monthtotal = monthtotal;
    }

}