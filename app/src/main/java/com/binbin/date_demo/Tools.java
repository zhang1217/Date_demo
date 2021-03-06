package com.binbin.date_demo;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.binbin.date_demo.Model.DateModel;
import com.binbin.date_demo.Model.ShowNotification;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by 彬彬 on 2018/3/28.
 */

public class Tools {

    //region 关闭软键盘

    /**
     * 关闭软键盘
     *
     * @param edit    EditText 控件
     * @param context
     */
    public static void CloseKeyBord(EditText edit, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }
    //endregion

    //region 时间处理

    /**
     * 获取当前时间
     *
     * @return 转换当前时间为字符串
     */
    public static String GetNowDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    /**
     * 得到5天前的时间为字符串
     *
     * @return
     */
    public static String GetFiveAgoDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis() - (86400000 * 5));
        return simpleDateFormat.format(date);
    }

    /**
     * 获取当前时间
     *
     * @return 获取当前时间的time
     */
    public static long GetNowDateTime() {
        Date date = new Date(System.currentTimeMillis());
        return date.getTime();
    }

    /**
     * 根据字符串转日期得到时间
     *
     * @param time 字符串的时间
     * @return
     */
    public static long GetDate(String time) {
        try {
            @SuppressLint("SimpleDateFormat")
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = formatter.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
    //endregion

    //region 对象Json转换

    /**
     * 对象转Json
     *
     * @param obj 对象
     * @return
     */
    public static String ToJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * 转换字符串为对象
     *
     * @param str json 串
     * @return DateModel 对象
     */
    public static DateModel GetDateModel(String str) {
        Gson gson = new Gson();
        Type type = new TypeToken<DateModel>() {
        }.getType();
        DateModel model = gson.fromJson(str, type);
        return model;
    }

    /**
     * 得到datemodels集合
     *
     * @param str json串
     * @return 一个datemodel的集合
     */
    public static ArrayList<DateModel> GetArrayDate(String str) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<DateModel>>() {
        }.getType();
        ArrayList<DateModel> models = gson.fromJson(str, type);
        return models;
    }
    //endregion

    //region 得到相同的对象

    /**
     * 得到相同的对象
     *
     * @param dateModels 相同对象的集合
     * @param id         相同的ID
     * @return
     */
    public static DateModel GetSame(ArrayList<DateModel> dateModels, String id) {
        for (DateModel item : dateModels) {
            if (item.Id.equals(id)) {
                return item;
            }
        }
        return null;
    }
    //endregion

    //region 获取随机数

    /**
     * 获取随机数
     *
     * @param max 最大值
     * @return 返回随机数
     */
    public static int RandomNum(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }
    //endregion

    //region Toast

    /**
     * Toast
     *
     * @param context 上下文
     * @param message 显示信息
     * @param time    显示时间
     */
    public static void Toast(Context context, String message, int time) {
        Toast.makeText(context, message, time).show();
    }
    //endregion

    //region  实例化通知

    /**
     * 实例化通知
     *
     * @param context 上下文
     * @param model   模型
     * @param manager 通知栏服务
     * @param id      通知栏ID
     */
    public static void ShowNotification(Context context, ShowNotification model, NotificationManager manager, int id) {
        Notification notification = new Notification.Builder(context)
                .setContentTitle(model.title)
                .setContentText(model.text)
                .setSubText(model.subText)
                .setTicker(model.ticker)
                .setSmallIcon(model.smallIcon)
                .setLargeIcon(model.lagerIcon)
                .setWhen(model.when)
                .setAutoCancel(model.autoCanel)
                .setDefaults(model.defaults)
                .setVisibility(model.visibility)
                .build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(id, notification);
    }
    //endregion

    //region Preferences

    /**
     * 编辑Preferences
     *
     * @param context 上下文
     * @param key     key
     * @param name    文件名
     * @param value   存储的值
     */
    public static void EditPreferences(Context context, String key, String name, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 读取Preferences
     *
     * @param context 上下文
     * @param key     key
     * @param name    文件名
     */
    public static String ReadPreferences(Context context, String key, String name) {
        SharedPreferences editor = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        String title = editor.getString(key, "");
//        if(edi) {
//            return editor.getString(key, "");
//        }
        return title;
    }
    //endregion

    //region 获取通知权限

    /**
     * 获取通知权限
     *
     * @param context 上下文
     */
    static void CheckNotificationPermission(final Context context) {
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            QMUIDialog.MessageDialogBuilder builder = new QMUIDialog.MessageDialogBuilder(context)
                    .setTitle("通知权限!")
                    .setMessage("防止收不到通知!请打开通知权限!")
                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                        }
                    }).addAction("确定", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            NotificationPermission(context);
                        }
                    });
            builder.show();
        }
    }
    //endregion

    //region 权限对应的Action
    private static void NotificationPermission(Context context) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {//android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 26) {//android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else {//其他
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        }
        context.startActivity(intent);
    }
    //endregion

}
