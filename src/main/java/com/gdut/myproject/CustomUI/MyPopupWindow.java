package com.gdut.myproject.CustomUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.gdut.myproject.PVMonitor.R;


/**
 * Created by Administrator on 2016/6/2.
 */
public class MyPopupWindow extends PopupWindow {
    private Button btn_refresh,btn_exit;
    private View menu ;

    public MyPopupWindow(Context context,View.OnClickListener itemOnClick){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menu = inflater.inflate(R.layout.view_dialog,null);

        btn_refresh = (Button) menu.findViewById(R.id.btn_refresh);
        btn_exit = (Button) menu.findViewById(R.id.btn_exit);

        btn_refresh.setOnClickListener(itemOnClick);
        btn_exit.setOnClickListener(itemOnClick);

        this.setContentView(menu);
        this.setFocusable(true);
        this.setTouchable(true);
        this.setAnimationStyle(R.style.AppTheme_PopupOverlay);

    }
}
