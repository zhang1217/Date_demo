package com.binbin.date_demo.Model;

import android.graphics.Bitmap;

/**
 * Created by 彬彬 on 2018/4/18.
 */

public class ShowNotification {
    public String title;
    public String text;
    public String subText;
    public String ticker;
    public int smallIcon;
    public Bitmap lagerIcon;
    public long when;
    public boolean autoCanel;
    public int defaults;
    public int visibility;

    public ShowNotification(String title, String text, String subText, String ticker, int smallIcon, Bitmap lagerIcon, long when, boolean autoCanel, int defaults, int visibility) {
        this.title = title;
        this.text = text;
        this.subText = subText;
        this.ticker = ticker;
        this.smallIcon = smallIcon;
        this.lagerIcon = lagerIcon;
        this.when = when;
        this.autoCanel = autoCanel;
        this.defaults = defaults;
        this.visibility = visibility;
    }
}
