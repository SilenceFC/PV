package com.gdut.myproject.Bean;

/**
 * Created by Administrator on 2016/9/12.
 */
public class UpdateInfo {
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApkurl() {
        return apkurl;
    }

    public void setApkurl(String apkurl) {
        this.apkurl = apkurl;
    }

    public String toString(){
       return "["+"version:"+version+" description:"+description+" apkurl:"+apkurl+"]";
   }
    private String version;
    private String description;
    private String apkurl;


}
