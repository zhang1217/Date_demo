package com.binbin.date_demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.binbin.date_demo.AdapterPackage.DateInfoAdapter;
import com.binbin.date_demo.AdapterPackage.SelectDateInfoAdapter;
import com.binbin.date_demo.Model.DateModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class SelectDateActivity extends AppCompatActivity {

    private static final String UPDATE_MODEL = "android.intent.action.UPDATE_MODEL";//更新MODEL
    //未到的事件
    private ArrayList<DateModel> HaveModel;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        Intent intent = getIntent();
        final int thisAppWidgetID = intent.getIntExtra("thisAppWidgetID", 0);
        GetInfo();
        listView = findViewById(R.id.list_view);
        listView.setAdapter(new SelectDateInfoAdapter(getLayoutInflater(), HaveModel));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setAction(UPDATE_MODEL);
                intent.putExtra("thisAppWidgetID", thisAppWidgetID);
                DateModel model = HaveModel.get(position);
                model.isSend = 0;
                intent.putExtra("model", Tools.ToJson(model));
                sendBroadcast(intent);
                finish();
            }
        });
    }

    public void GetInfo() {
        HaveModel = new ArrayList<DateModel>();
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.share_name), MODE_PRIVATE);
        Map<String, ?> keysMap = sharedPreferences.getAll();
        for (String key : keysMap.keySet()) {
            DateModel model = Tools.GetDateModel(keysMap.get(key).toString());
            model.Id = key;
            long parseDate = Tools.GetDate(model.Date + " 00:00:00");
            long nowDate = Tools.GetNowDateTime();
            if (parseDate > nowDate) {
                model.HaveDay = "+," + ((parseDate - nowDate) / (1000 * 60 * 60 * 24) + 1);
                HaveModel.add(model);
            }
        }
    }

}
