package com.binbin.date_demo.Service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.binbin.date_demo.AppWidget.DateAppWidget;
import com.binbin.date_demo.Model.DateModel;
import com.binbin.date_demo.R;
import com.binbin.date_demo.SelectDateActivity;
import com.binbin.date_demo.Tools;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AppWidgetService extends Service {

    private static final String SERVICE_DESTORY = "android.intent.action.SERVICE_DESTORY";//服务销毁


    private Timer timer;
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.share_app_widget_name), MODE_PRIVATE);
            Map<String, ?> map = sharedPreferences.getAll();
            if (map.size() > 0) {
                for (String key : map.keySet()) {
                    int appwidgetid = Integer.parseInt(key);
                    int day = 0;
                    DateModel model = Tools.GetDateModel(map.get(key).toString());
                    long parseDate = Tools.GetDate(model.Date + " 00:00:00");
                    long nowDate = Tools.GetNowDateTime();
                    if (parseDate > nowDate) {
                        day = (int) ((parseDate - nowDate) / (1000 * 60 * 60 * 24));
                    } else {
                        day = 0;
                    }
                    AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());//获得appwidget管理实例，用于管理appwidget以便进行更新操作
                    RemoteViews views = new RemoteViews(getPackageName(), R.layout.date_app_widget);//获取远程视图
                    Intent intent = new Intent(getApplicationContext(), SelectDateActivity.class);
                    intent.putExtra("thisAppWidgetID", appwidgetid);
                    PendingIntent selectIntent = PendingIntent.getActivity(getApplicationContext(), Tools.RandomNum(100), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    views.setOnClickPendingIntent(R.id.layout, selectIntent);
                    views.setTextViewText(R.id.day, day + "天");
                    views.setTextViewText(R.id.name, model.Name);
                    manager.updateAppWidget(appwidgetid, views);
                }
            }
            Log.i("debug", "TimerTask:已执行");
        }
    };

    public AppWidgetService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        timer.schedule(task, 0, Integer.parseInt(getResources().getString(R.string.app_widget_service_timer)));
        Log.i("debug", "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        Intent intent = new Intent();
        intent.setAction(SERVICE_DESTORY);
        sendBroadcast(intent);
        Log.i("debug", "onDestroy:计时已停止");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
