package com.binbin.date_demo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.binbin.date_demo.Model.DateModel;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.UUID;


public class AddDateActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Date;
    private EditText Desc;
    private LinearLayout AddDateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_date);
        Init();
    }

    //region 初始化
    private void Init() {
        Name = findViewById(R.id.name);
        Date = findViewById(R.id.date);
        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.CloseKeyBord(Name, AddDateActivity.this);
                TimeSelector timeSelector = new TimeSelector(AddDateActivity.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        Date.setText(time.split(" ")[0]);
                    }
                }, Tools.GetFiveAgoDate(), "2060-01-01 00:00:00");
                timeSelector.setMode(TimeSelector.MODE.YMD);
                timeSelector.show();
            }
        });
        Desc = findViewById(R.id.desc);
        AddDateLayout = findViewById(R.id.addDate_layout);
        AddDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.CloseKeyBord(Name, AddDateActivity.this);
            }
        });
    }
    //endregion

    //region 创建右上角菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_date, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final QMUITipDialog dialog = new QMUITipDialog.Builder(AddDateActivity.this).setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS).setTipWord("保存成功").create();
        boolean finish = false;
        switch (item.getItemId()) {
            case R.id.finishDate:
                SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.share_name), MODE_PRIVATE).edit();
                if (!Name.getText().toString().equals("") && Name.getText() != null && !Date.getText().toString().equals("") && Date.getText() != null) {
                    DateModel dateModel = new DateModel();
                    dateModel.Name = Name.getText().toString();
                    dateModel.Date = Date.getText().toString();
                    dateModel.Desc = Desc.getText().toString();
                    editor.putString(UUID.randomUUID().toString(), Tools.ToJson(dateModel));
                    editor.apply();
                    dialog.show();
                    finish = true;
                }
                break;
        }
        if (finish) {
            AddDateLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    finish();
                }
            }, 2000);
        } else {
            Toast.makeText(AddDateActivity.this, "信息不能为空", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

}
