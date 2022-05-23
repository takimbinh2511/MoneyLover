package vianh.nva.moneymanager.data.entity;

import androidx.room.Ignore;

import java.io.Serializable;

public class TotalMoneyDisplay implements Serializable {
    private long totalMoney;
    private int type;
    private int day;
    private int month;
    private int year;
    private String iconName;
    private String colorName;
    private String description;

    public TotalMoneyDisplay() {
    }

    @Ignore
    public TotalMoneyDisplay(long totalMoney, int type, int day, int month, int year, String iconName, String colorName, String description) {
        this.totalMoney = totalMoney;
        this.type = type;
        this.day = day;
        this.month = month;
        this.year = year;
        this.iconName = iconName;
        this.colorName = colorName;
        this.description = description;
    }

    public long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
