package com.gdut.myproject.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;


    public MyPagerAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
        Log.e("测试", "初始化适配器" );
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("测试", "当前position"+position );
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
