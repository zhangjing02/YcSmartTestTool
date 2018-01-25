package com.czjk.bleDemo.model;

import java.util.List;

/**
 * Created by czjk on 2017/2/27.
 */

public class Heart {
    private int year;
    private int month;
    private int day;
    private int silentHeart;
    private List<HeartItem> list;

    public Heart() {
    }

    public Heart(int year, int month, int day, int silentHeart, List<HeartItem> list) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.silentHeart = silentHeart;
        this.list = list;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getSilentHeart() {
        return silentHeart;
    }

    public void setSilentHeart(int silentHeart) {
        this.silentHeart = silentHeart;
    }

    public List<HeartItem> getList() {
        return list;
    }

    public void setList(List<HeartItem> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Heart{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", silentHeart=" + silentHeart +
                ", list=" + getString(list)  +
                '}';
    }

    private String getString(List<HeartItem> list){
        String s = "";
        for (HeartItem heartItem : list) {
            s+= heartItem.toString()+"\n";
        }
        return s;
    }
}
