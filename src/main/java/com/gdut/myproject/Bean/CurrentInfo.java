package com.gdut.myproject.Bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class CurrentInfo {
    private List<String> mList;
    public CurrentInfo(List<String> list){
        mList = list;
    }
public List<String> getCurrentList(){
        return mList;
    }
}
