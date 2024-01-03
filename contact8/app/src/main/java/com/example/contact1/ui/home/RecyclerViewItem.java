package com.example.contact1.ui.home;

import android.graphics.drawable.Drawable;

public class RecyclerViewItem {
    private Drawable iconDrawable;
    private String titleStr;
    private String descStr;
    private Drawable icon;
    private String name;
    private String number;

    // 생성자 등 필요한 코드

    // getName 및 getNumber 메서드 추가
    public String getName() {
        return name;
    }
    public String getNumber() {
        return number;
    }
    public RecyclerViewItem(Drawable icon, String title, String desc) {
        this.iconDrawable = icon;
        this.titleStr = title;
        this.descStr = desc;
    }

    public Drawable getIcon() {
        return this.iconDrawable;
    }

    public String getTitle() {
        return this.titleStr;
    }

    public String getDesc() {
        return this.descStr;
    }
}