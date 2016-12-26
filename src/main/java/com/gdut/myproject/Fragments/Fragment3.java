package com.gdut.myproject.Fragments;



import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdut.myproject.PVMonitor.R;

import java.util.ArrayList;
import java.util.List;


import com.gdut.myproject.Adapter.MyPagerAdapter;
import com.gdut.myproject.utils.LogUtils;

/**
 * Created by Administrator on 2016/5/9.
 */
public class Fragment3 extends Fragment {
    private TextView tv_power, tv_monthElect, tv_yearElect;
    private ImageView tabline;
    private ViewPager pager;
    private List<Fragment> fragmentLists;
    private int currentPage = 0;
    private int length = 0;

    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.fragment3, null);
        initView(v);
        initTabline();

        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager(), fragmentLists);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        pager.setOffscreenPageLimit(0);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0);
                Log.e("tv", "点击第一个TV");
            }
        });
        tv_monthElect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);
                Log.e("tv", "点击第二个TV");
            }
        });
        tv_yearElect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(2);
                Log.e("tv", "点击第三个TV");
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //position 0-->1,1-->2;positionoffset 0-->0.9999>0
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabline.getLayoutParams();
                LogUtils.Loge("pager","currentpage"+currentPage+" position:"+position+" positionoffset:"+positionOffset);
                if (currentPage == 0 && position == 0) {//1-->2
                    lp.leftMargin = (int) (0 * length + positionOffset * length);
                } else if (currentPage == 1 && position == 1) {//2-->3
                    lp.leftMargin = (int) (1 * length + positionOffset * length);
                } else if (currentPage == 2 && position == 1) {//3-->2
                    lp.leftMargin = (int) (2 * length  - (1-positionOffset) * length);
                } else if (currentPage == 1 && position == 0) {//2-->1
                    lp.leftMargin = (int) (1 * length  - (1-positionOffset) * length);
                }
                tabline.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                tv_power.setTextColor(Color.GRAY);
                tv_monthElect.setTextColor(Color.GRAY);
                tv_yearElect.setTextColor(Color.GRAY);
                switch (position) {
                    case 0:
                        tv_power.setTextColor(Color.GREEN);
                        break;
                    case 1:
                        tv_monthElect.setTextColor(Color.GREEN);
                        break;
                    case 2:
                        tv_yearElect.setTextColor(Color.GREEN);
                        break;
                }
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.Loge("onPause","调用onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pager.setCurrentItem(0);
        fragmentLists.clear();
        Log.e("测试", "fragment3被销毁");
    }

    /**
     * 获取屏幕的宽度，设置滑动条的宽度为屏幕宽度的1/3
     */
    private void initTabline() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        length = metrics.widthPixels / 3;
        ViewGroup.LayoutParams lp = tabline.getLayoutParams();
        lp.width = length;
        tabline.setLayoutParams(lp);
    }

    private void initView(View view) {
        tv_power = (TextView) view.findViewById(R.id.tv_power);
        tv_monthElect = (TextView) view.findViewById(R.id.tv_monthElect);
        tv_yearElect = (TextView) view.findViewById(R.id.tv_yearElect);

        tabline = (ImageView) view.findViewById(R.id.tabline);
        pager = (ViewPager) view.findViewById(R.id.pager);

        fragmentLists = new ArrayList<>();
        fragmentLists.add(new ViewPage_1());
        fragmentLists.add(new ViewPage_2());
        fragmentLists.add(new ViewPage_3());

        Log.e("测试", "fragmentlist添加完毕");
    }
}
