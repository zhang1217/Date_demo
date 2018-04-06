package com.binbin.date_demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.binbin.date_demo.Model.DateModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

}
