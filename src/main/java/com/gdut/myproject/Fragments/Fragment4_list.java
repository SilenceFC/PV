package com.gdut.myproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.gdut.myproject.PVMonitor.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gdut.myproject.PVMonitor.CurrentActivity;
import com.gdut.myproject.PVMonitor.HistoryActivity;
import com.gdut.myproject.utils.SetBackGround;

/**
 * Created by Administrator on 2016/9/19.
 */
public class Fragment4_list extends Fragment {
    private ListView list_device;
    private LinearLayout ll_frag4_list;
    private String[] items = {"实时数据", "报警记录"};
    private int[] icons = {R.mipmap.current_64, R.mipmap.history_64};
    private List<Map<String, Object>> listItems;
    private SimpleAdapter mAdapter;
    private SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.fragment4_list, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        list_device = (ListView) v.findViewById(R.id.list_device);
        ll_frag4_list = (LinearLayout) v.findViewById(R.id.ll_frag4_list);
        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);

        ll_frag4_list.setBackgroundResource(SetBackGround.setLlBackground(sp));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listItems = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", items[i]);
            map.put("icon", icons[i]);
            listItems.add(map);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter = new SimpleAdapter(getActivity(), listItems, R.layout.fragment4_listitem, new String[]{
                "name", "icon"}, new int[]{R.id.tv_frag4_title, R.id.iv_frag4_icon});
        list_device.setAdapter(mAdapter);
        list_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        Intent intent = new Intent(getActivity(), CurrentActivity.class);
                        getActivity().startActivity(intent);
                    break;
                    case 1:
                        Intent intent1 = new Intent(getActivity(), HistoryActivity.class);
                        getActivity().startActivity(intent1);
                        break;

                }
            }
        });
    }

}
