package com.czjk.bleDemo.model;

import java.util.List;

/**
 * Created by czjk on 2017/2/25.
 */

public class Sleep {
    private int year;
    private int month;
    private int day;
    private String BeginDate;
    private int sleepBeginedTimeH;
    private int sleepBeginedTimeM;
    private String EndDate;
    private int sleepEndedTimeH;
    private int sleepEndedTimeM;
    private int totalSleepMinutes;
    private int lightSleepMinutes;
    private int deepSleepMinutes;


    private List<SleepItem> list;

    public Sleep() {
    }

    public Sleep(int year, int month, int day,int sleepBeginedTimeH,int sleepBeginedTimeM ,String BeginDate,int sleepEndedTimeH, int sleepEndedTimeM,String EndDate, int totalSleepMinutes, int lightSleepMinutes, int deepSleepMinutes, List<SleepItem> list) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.sleepBeginedTimeH=sleepBeginedTimeH;
        this.sleepBeginedTimeM=sleepBeginedTimeM;
        this.BeginDate=BeginDate;
        this.sleepEndedTimeH = sleepEndedTimeH;
        this.sleepEndedTimeM = sleepEndedTimeM;
        this.EndDate=EndDate;
        this.totalSleepMinutes = totalSleepMinutes;
        this.lightSleepMinutes = lightSleepMinutes;
        this.deepSleepMinutes = deepSleepMinutes;
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

    public String getBeginDate() {
        return BeginDate;
    }

    public void setBeginDate(String beginDate) {
        BeginDate = beginDate;
    }

    public int getSleepBeginedTimeH() {
        return sleepBeginedTimeH;
    }

    public void setSleepBeginedTimeH(int sleepBeginedTimeH) {
        this.sleepBeginedTimeH = sleepBeginedTimeH;
    }

    public int getSleepBeginedTimeM() {
        return sleepBeginedTimeM;
    }

    public void setSleepBeginedTimeM(int sleepBeginedTimeM) {
        this.sleepBeginedTimeM = sleepBeginedTimeM;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public int getSleepEndedTimeH() {
        return sleepEndedTimeH;
    }

    public void setSleepEndedTimeH(int sleepEndedTimeH) {
        this.sleepEndedTimeH = sleepEndedTimeH;
    }

    public int getSleepEndedTimeM() {
        return sleepEndedTimeM;
    }

    public void setSleepEndedTimeM(int sleepEndedTimeM) {
        this.sleepEndedTimeM = sleepEndedTimeM;
    }

    public int getTotalSleepMinutes() {
        return totalSleepMinutes;
    }

    public void setTotalSleepMinutes(int totalSleepMinutes) {
        this.totalSleepMinutes = totalSleepMinutes;
    }

    public int getLightSleepMinutes() {
        return lightSleepMinutes;
    }

    public void setLightSleepMinutes(int lightSleepMinutes) {
        this.lightSleepMinutes = lightSleepMinutes;
    }

    public int getDeepSleepMinutes() {
        return deepSleepMinutes;
    }

    public void setDeepSleepMinutes(int deepSleepMinutes) {
        this.deepSleepMinutes = deepSleepMinutes;
    }

    public List<SleepItem> getList() {
        return list;
    }

    public void setList(List<SleepItem> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "healthSleep{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", BeginDate=" + BeginDate +
                ", sleepBeginedTimeH=" + sleepBeginedTimeH +
                ", sleepBeginedTimeM=" + sleepBeginedTimeM +
                ", EndDate=" + EndDate +
                ", sleepEndedTimeH=" + sleepEndedTimeH +
                ", sleepEndedTimeM=" + sleepEndedTimeM +
                ", totalSleepMinutes=" + totalSleepMinutes +
                ", lightSleepMinutes=" + lightSleepMinutes +
                ", deepSleepMinutes=" + deepSleepMinutes +
                ", list=" + getString(list) +
                '}';
    }

    private String getString(List<SleepItem> list){
        String s = "";
        for (SleepItem sleepItem : list) {
            s+= sleepItem.toString()+"\n";
        }
        return s;
    }

}
