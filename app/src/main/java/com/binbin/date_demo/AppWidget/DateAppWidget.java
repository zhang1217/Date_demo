package com.binbin.date_demo.AppWidget;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.binbin.date_demo.Model.DateModel;
import com.binbin.date_demo.Model.ShowNotification;
import com.binbin.date_demo.R;
import com.binbin.date_demo.SelectDateActivity;
import com.binbin.date_demo.Service.AppWidgetService;
import com.binbin.date_demo.Tools;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of App Widget functionality.
 */
public class DateAppWidget extends AppWidgetProvider {

    private static final String SERVICE_DESTORY = "android.intent.action.SERVICE_DESTORY";//服务销毁
    private static final String UPDATE_MODEL = "android.intent.action.UPDATE_MODEL";//更新MODEL

    private static int thisAppWidgetID;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        //Log.i("debug", thisAppWidgetID + ":updateAppWidget");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.date_app_widget);
        Intent intent = new Intent(context, SelectDateActivity.class);
        intent.putExtra("thisAppWidgetID", thisAppWidgetID);
        PendingIntent selectIntent = PendingIntent.getActivity(context, Tools.RandomNum(100), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.layout, selectIntent);
//        views.setTextViewText(R.id.day, day + "天");
//        views.setTextViewText(R.id.name, name);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            thisAppWidgetID = appWidgetId;
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        Intent intent = new Intent(context, AppWidgetService.class);
        context.stopService(intent);
        context.startService(intent);
        //Log.i("debug", "onUpdate");
    }

    @Override
    public void onEnabled(Context context) {
        //Log.i("debug", "onEnabled:服务已启动");
    }

    @Override
    public void onDisabled(Context context) {
        Intent intent = new Intent(context, AppWidgetService.class);
        context.stopService(intent);
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.share_app_widget_name), context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        //Log.i("debug", "onDisabled:服务已结束");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        thisAppWidgetID = intent.getIntExtra("thisAppWidgetID", 0);
        if (Objects.equals(action, SERVICE_DESTORY)) {//服务销毁
            //Log.i("debug", thisAppWidgetID + ":服务销毁");
        } else if (Objects.equals(action, UPDATE_MODEL)) {//更新Model
            Save(context, intent.getStringExtra("model"), String.valueOf(thisAppWidgetID));
            updateView(context);
        }
//        Log.i("debug", "onReceive:thisAppWidgetID:" + thisAppWidgetID);
//        Log.i("debug", "onReceive" + intent.getAction());
    }

    public void updateView(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.share_app_widget_name), context.MODE_PRIVATE);
        Map<String, ?> map = sharedPreferences.getAll();
        if (map.size() > 0) {
            for (String key : map.keySet()) {
                int appwidgetid = Integer.parseInt(key);
                int day = 0;
                DateModel model = Tools.GetDateModel(map.get(key).toString());
                long parseDate = Tools.GetDate(model.Date + " 00:00:00");
                long nowDate = Tools.GetNowDateTime();
                if (parseDate > nowDate) {
                    day = (int) ((parseDate - nowDate) / (1000 * 60 * 60 * 24) + 1);
                    if (day <= 2 && model.isSend == 0) {
                        //发送通知
                        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                        ShowNotification notification = new ShowNotification(model.Name, "您的" + model.Name + "还有两天就到了", model.Desc, R.drawable.time,
                                BitmapFactory.decodeResource(context.getResources(), R.drawable.time), System.currentTimeMillis(), true, Notification.DEFAULT_ALL, Notification.VISIBILITY_PRIVATE);
                        Tools.ShowNotification(context.getApplicationContext(), notification, manager, 0);
                        model.isSend = 2;
                        Tools.EditPreferences(context.getApplicationContext(), key, context.getResources().getString(R.string.share_app_widget_name), Tools.ToJson(model));
                    }
                    if (day < 1 && model.isSend == 2) {
                        //发送通知
                        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                        ShowNotification notification = new ShowNotification(model.Name, "您的" + model.Name + "就要到了", model.Desc, R.drawable.time,
                                BitmapFactory.decodeResource(context.getResources(), R.drawable.time), System.currentTimeMillis(), true, Notification.DEFAULT_ALL, Notification.VISIBILITY_PRIVATE);
                        Tools.ShowNotification(context, notification, manager, 0);
                        model.isSend = 1;
                        Tools.EditPreferences(context, key, context.getResources().getString(R.string.share_app_widget_name), Tools.ToJson(model));
                    }
                } else {
                    day = 0;
                }
                AppWidgetManager manager = AppWidgetManager.getInstance(context);//获得appwidget管理实例，用于管理appwidget以便进行更新操作
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.date_app_widget);//获取远程视图
                Intent intent = new Intent(context, SelectDateActivity.class);
                intent.putExtra("thisAppWidgetID", appwidgetid);
                PendingIntent selectIntent = PendingIntent.getActivity(context, Tools.RandomNum(100), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.layout, selectIntent);
                views.setTextViewText(R.id.day, day + "天");
                views.setTextViewText(R.id.name, model.Name);
                manager.updateAppWidget(appwidgetid, views);
            }
        }
    }

    public void Save(Context context, String str, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.share_app_widget_name), Context.MODE_PRIVATE).edit();
        editor.putString(key, str);
        editor.apply();
    }
}

