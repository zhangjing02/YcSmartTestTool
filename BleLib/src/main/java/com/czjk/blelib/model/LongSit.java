package com.czjk.blelib.model;

/**
 * Created by czjk on 2017/2/28.
 */

public class LongSit {

    private int startHour; //开始时
    private int startMinute;  //开始分
    private int endHour;  //结束时
    private int endMinute; //结束分
    private int interval;  //提醒间隔
    private String repeat; //重复周期

    public LongSit(int startHour, int startMinute, int endHour, int endMinute, int interval, String repeat) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.interval = interval;
        this.repeat = repeat;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    @Override
    public String toString() {
        return "LongSit{" +
                "startHour=" + startHour +
                ", startMinute=" + startMinute +
                ", endHour=" + endHour +
                ", endMinute=" + endMinute +
                ", interval=" + interval +
                ", repeat='" + repeat + '\'' +
                '}';
    }
}
