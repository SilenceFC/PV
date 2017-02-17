package com.gdut.myproject.PVMonitor;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gdut.myproject.Async.MyAsyncTask;
import com.gdut.myproject.Async.RefreshAsync;
import com.gdut.myproject.Bean.MainEvent;
import com.gdut.myproject.Fragments.Fragment1;
import com.gdut.myproject.Fragments.Fragment2;
import com.gdut.myproject.Fragments.Fragment3;
import com.gdut.myproject.Fragments.Fragment4_list;
import de.greenrobot.event.EventBus;
import com.gdut.myproject.utils.DbService;
import com.gdut.myproject.utils.LogUtils;

/**
 * Created by Administrator on 2016/5/6.
 */

public class FirstActivity extends FragmentActivity {
    private static final String PATH = "http://124.16.2.182:8080/MyPVMonitorServlet/servlet/Data_Statics?date=";
    private String date;
    private RadioGroup rg_menu;
    private ImageView iv_add;
    SharedPreferences sp;
    PopupMenu menu;
    Fragment1 fg1;
    Fragment2 fg2;
    Fragment3 fg3;

    Fragment4_list fg4_list;
    DbService db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        setContentView(R.layout.activity_first);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#63B8FF"));
        }
        EventBus.getDefault().register(this);

        sp = getSharedPreferences("config", MODE_PRIVATE);
        db = DbService.getInstance();
       date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        iv_add = (ImageView) findViewById(R.id.iv_add);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu = new PopupMenu(FirstActivity.this,v);
                getMenuInflater().inflate(R.menu.menu_add, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.exit:
                                finish();
                                break;
                            case R.id.refresh:
                                ProgressDialog pd = ProgressDialog.show(FirstActivity.this, "刷新中", "请稍等。。。");
                                String add = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                                RefreshAsync ra = new RefreshAsync(sp,FirstActivity.this,pd);
                                ra.execute(add);
                                break;
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });

        MyAsyncTask task = new MyAsyncTask(db);
        task.execute(PATH + date);
        LogUtils.Loge("task", "已经执行");

        fg1 = new Fragment1();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fg1).commit();

        rg_menu = (RadioGroup) findViewById(R.id.rg_menu);


        rg_menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bt_main:
                        if (fg1 == null) {
                            fg1 = new Fragment1();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fg1).commit();
                        break;
                    case R.id.bt_weather:
                        if (fg2 == null) {
                            fg2 = new Fragment2();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fg2).commit();
                        break;
                    case R.id.bt_chart:
                        if (fg3 == null) {
                            fg3 = new Fragment3();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fg3).commit();
                        break;
                    case R.id.bt_device:
                        if (fg4_list == null) {
                            fg4_list = new Fragment4_list();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fg4_list).commit();
                        break;
                }
            }
        });
    }

    public void onEventMainThread(MainEvent event){
        Toast.makeText(this,event.getMsg(),Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
