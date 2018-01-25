package com.czjk.bleDemo.utils;

import com.czjk.blelib.model.HealthSport;

import java.util.ArrayList;
import java.util.List;

import static senssun.ycblelib.db.DBTools.dbTools;

/**
 * Created by czjk on 2017/1/9.
 */

public class SleepUtils {

    public static ArrayList<HealthSport> getDaySleepData(String preTime, String time) {
        //实际睡眠
        ArrayList<HealthSport> newHealthSports = new ArrayList<>();
        List<HealthSport> yesterdayHealthSports = dbTools.selectData(preTime);
        String sleepStart = getSleepStart(yesterdayHealthSports);
        List<HealthSport> todayHealthSports = dbTools.selectData(time);
        String sleepStop = getSleepStop(todayHealthSports);
        if (sleepStart.length() == 0) {//00点之前没有睡觉
            for (int i = 0; i < todayHealthSports.size(); i++) {
                HealthSport braceletData = todayHealthSports.get(i);
                if (braceletData.getType() == 17) {
                    sleepStart = braceletData.getDate();
                    break;
                }
            }
            if (sleepStart.length() == 0) {
                return null;
            }
            if (sleepStop.length() == 0) {
                return null;
            }
            int start = Integer.parseInt(sleepStart.substring(4, 12));
            int stop = Integer.parseInt(sleepStop.substring(4, 12));
            for (int i = 0; i < todayHealthSports.size(); i++) {
                HealthSport braceletData = todayHealthSports.get(i);
                int t = Integer.parseInt(braceletData.getDate().substring(4, 12));
                if (t <= stop && t >= start) {
                    newHealthSports.add(braceletData);
                }
            }
        } else { //00点之前睡
            int start = Integer.parseInt(sleepStart.substring(4, 12));
            if (sleepStop.length() == 0) {
                return null;
            }
            int stop = Integer.parseInt(sleepStop.substring(4, 12));
            for (int i = 0; i < yesterdayHealthSports.size(); i++) {
                HealthSport braceletData = yesterdayHealthSports.get(i);
                int t = Integer.parseInt(braceletData.getDate().substring(4, 12));
                if (t >= start) {
                    newHealthSports.add(braceletData);
                }
            }
            for (int i = 0; i < todayHealthSports.size(); i++) {
                HealthSport braceletData = todayHealthSports.get(i);
                int t = Integer.parseInt(braceletData.getDate().substring(4, 12));
                if (t <= stop) {
                    newHealthSports.add(braceletData);
                } else {
                    break;
                }
            }
        }
        return newHealthSports;
    }

    //步数类型为17的时间点
    private static String getSleepStart(List<HealthSport> yesterdayHealthSports) {
        String sleepStart = "";
        int size = yesterdayHealthSports.size();
        for (int i = size - 1; i >= 0; i--) {
            HealthSport braceletData = yesterdayHealthSports.get(i);
            String date = braceletData.getDate();
            int type = braceletData.getType();
            int hour = Integer.parseInt(date.substring(8, 10));
            if (type == 21 || hour < 18) {
                return sleepStart;
            }
            if (type == 17) {
                return date;
            }
        }
        return sleepStart;
    }

    //步数类型为21的时间点
    private static String getSleepStop(List<HealthSport> todayHealthSports) {
        String sleepStop = "";
        int size = todayHealthSports.size();
        for (int i = 0; i < size; i++) {
            HealthSport braceletData = todayHealthSports.get(i);
            if (braceletData.getType() == 21) {
                return braceletData.getDate();
            }
        }
        return sleepStop;
    }


}
