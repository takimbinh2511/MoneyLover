package vianh.nva.moneymanager.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "money",
        indices = {@Index("type"), @Index("categoryId")},
        foreignKeys = @ForeignKey(entity = Category.class,
                parentColumns = "id", childColumns = "categoryId"))
public class Money implements Serializable {
    @Ignore
    public static final int TYPE_SPEND = 0;

    @Ignore
    public static final int TYPE_EARN = 1;


    public Money() {}

    @Ignore
    public Money(String note, long money, int categoryId, int day, int month, int year, int type) {
        this.note = note;
        this.money = money;
        this.categoryId = categoryId;
        this.day = day;
        this.month = month;
        this.year = year;
        this.type = type;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    private String note;
    private long money;
    private int categoryId;
    private int day;
    private int month;
    private int year;

    // Type of money, earn or spend
    private int type;
}
