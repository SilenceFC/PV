package com.gdut.myproject.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.gdut.myproject.PVMonitor.R;


/**
 * Created by Administrator on 2016/9/24.
 */
public class AlarmCursorAdapter extends CursorAdapter {


    public AlarmCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder vh = new ViewHolder();
        View view = View.inflate(context, R.layout.list_alarm_item,null);
        vh.tv_time = (TextView) view.findViewById(R.id.tv_alarm_time);
        vh.tv_desc = (TextView) view.findViewById(R.id.tv_alarm_desc);
        view.setTag(vh);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder vh = (ViewHolder) view.getTag();
        if(cursor.getCount()==0){
            vh.tv_time.setText("暂无数据");
            vh.tv_desc.setText("暂无数据");
        }else{
            vh.tv_time.setText(cursor.getString(1));
            vh.tv_desc.setText(cursor.getString(2));
        }
    }

    class ViewHolder{
        TextView tv_time;
        TextView tv_desc;
    }
}
