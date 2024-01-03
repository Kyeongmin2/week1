package com.example.contact1.ui.home;

import android.graphics.drawable.Drawable;

public class RecyclerViewItem {
    private Drawable iconDrawable;
    private String titleStr;
    private String descStr;
    private Drawable icon;
    private String name;
    private String number;
    private String memo;

    // 생성자 등 필요한 코드

    // getName 및 getNumber 메서드 추가
    public String getName() {
        return name;
    }
    public String getNumber() {
        return number;
    }
    public RecyclerViewItem(Drawable icon, String name, String number, String memo) {
        this.iconDrawable = icon;
        this.name = name;
        this.number = number;
        this.memo = memo;
    }

    public Drawable getIcon() {
        return this.iconDrawable;
    }

    public String getTitle() {
        return this.name;
    }

    public String getDesc() {
        return this.number;
    }
    public void setName(String newName) {
        this.name = newName;
    }

    public void setNumber(String newNumber) {
        this.number = newNumber;
    }
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
}