package com.gdut.myproject.utils;

import java.util.Comparator;

/**
 * Created by Administrator on 2016/5/16.
 * 按照升序排列
 */
public class MyComparator implements Comparator<Integer> {
    @Override
    public int compare(Integer o1, Integer o2) {
        if(o1 < o2){
            return -1;
        }else if(o1 == o2){
            return 0;
        }else{
            return 1;
        }
    }
}
