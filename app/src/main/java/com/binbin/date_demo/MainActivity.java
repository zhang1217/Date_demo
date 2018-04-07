package com.binbin.date_demo;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.binbin.date_demo.FragmentPackge.MainFragment;
import com.binbin.date_demo.Model.DateModel;
import com.gigamole.library.navigationtabstrip.NavigationTabStrip;

import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //2018-4-7-11-12
    //region 变量
    //tab视图
    private NavigationTabStrip navigationTabStrip;
    //未到的事件
    private ArrayList<DateModel> HaveModel;
    //过时的事件
    private ArrayList<DateModel> HavedModel;
    //endregion

    //region OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //读取数据
        GetInfo();
        //初始化
        Init();
    }
    //endregion

    //region  onRestart
    @Override
    protected void onRestart() {
        super.onRestart();
        GetInfo();
        getFragmentManager().beginTransaction().replace(R.id.frameLayout, MainFragment.newInstance(HaveModel, true)).commitAllowingStateLoss();
        navigationTabStrip.setTabIndex(0, true);
    }
    //endregion

    //region 初始化信息
    private void Init() {
        //进入默认加载第一个fragment
        getFragmentManager().beginTransaction().add(R.id.frameLayout, MainFragment.newInstance(HaveModel, true)).commitAllowingStateLoss();
        //找到tab,并监听事件
        navigationTabStrip = findViewById(R.id.nts);
        navigationTabStrip.setTabIndex(0, true);
        navigationTabStrip.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(String title, int index) {
                switch (index) {
                    case 0:
                        getFragmentManager().beginTransaction().replace(R.id.frameLayout, MainFragment.newInstance(HaveModel, true)).commitAllowingStateLoss();
                        break;
                    case 1:
                        getFragmentManager().beginTransaction().replace(R.id.frameLayout, MainFragment.newInstance(HavedModel, false)).commitAllowingStateLoss();
                        break;
                }
            }

            @Override
            public void onEndTabSelected(String title, int index) {

            }
        });
    }
    //endregion

    //region 创建右上角菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.addDate:
                intent = new Intent(this, AddDateActivity.class);
                break;
        }
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region 读取数据
    public void GetInfo() {
        HaveModel = new ArrayList<DateModel>();
        HavedModel = new ArrayList<DateModel>();
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.share_name), MODE_PRIVATE);
        Map<String, ?> keysMap = sharedPreferences.getAll();
        for (String key : keysMap.keySet()) {
            DateModel model = Tools.GetDateModel(keysMap.get(key).toString());
            model.Id = key;
            long parseDate = Tools.GetDate(model.Date + " 00:00:00");
            long nowDate = Tools.GetNowDateTime();
            if (parseDate > nowDate) {
                model.HaveDay = "+," + ((parseDate - nowDate) / (1000 * 60 * 60 * 24));
                HaveModel.add(model);
            } else {
                model.HaveDay = "-," + (nowDate - parseDate) / (1000 * 60 * 60 * 24);
                HavedModel.add(model);
            }
        }
    }
    //endregion

    //region 更新model

    /**
     * 更新model
     *
     * @param id   对象的id
     * @param have 是不是历史事件 0历史事件 1现有事件
     */
    public void UpdateInfo(String id, boolean have) {
        if (have) {
            HaveModel.remove(Tools.GetSame(HaveModel, id));
        } else {
            HavedModel.remove(Tools.GetSame(HavedModel, id));
        }
    }
    //endregion

}
