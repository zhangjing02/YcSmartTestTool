package com.czjk.blelib.model;

/**
 * Created by czjk on 2017/2/28.
 */

public class RealTimeData {
    private int step;
    private int heart;
    private int blood;
    private int distance;  //米
    private int cal;     //千卡

    public RealTimeData(int step, int heart, int blood, int distance, int cal) {
        this.step = step;
        this.heart = heart;
        this.blood = blood;
        this.distance = distance;
        this.cal = cal;
    }

    public RealTimeData() {
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getCal() {
        return cal;
    }

    public void setCal(int cal) {
        this.cal = cal;
    }

    @Override
    public String toString() {
        return "RealTimeData{" +
                "step=" + step +
                ", heart=" + heart +
                ", blood=" + blood +
                ", distance=" + distance +
                ", cal=" + cal +
                '}';
    }
}
