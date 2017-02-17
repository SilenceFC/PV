package com.gdut.myproject.Bean;

/**
 * Created by Administrator on 2016/8/22.
 */
public class NotifyEntity {
    public static int code = 0;
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    private int type ;
    private String title ;
    private  String content ;
    private String extra;



}
