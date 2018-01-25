package com.czjk.bleDemo.model;

/**
 * Created by czjk on 2017/2/25.
 */

public class SleepItem{
    private String date;  //日期  2017 01 17   HH 0--23  mm 00 10 20   50
    private int type;
    private int offset;
    public SleepItem() {
    }

    public SleepItem(String date,int type, int offset) {
        this.date = date;
        this.type = type;
        this.offset = offset;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "SleepItem{" +
                ", date=" + date +
                ", type=" + type +
                ", offset=" + offset +
                '}';
    }
}