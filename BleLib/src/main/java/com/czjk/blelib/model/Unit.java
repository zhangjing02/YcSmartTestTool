package com.czjk.blelib.model;

/**
 * Created by czjk on 2017/2/28.
 */

public class Unit {
    private int unit;  // 0: 公制      1: 英制
    private int timeMode;  // 0: 24小时制  1: 12小时制

    public Unit(int unit, int timeMode) {
        this.unit = unit;
        this.timeMode = timeMode;
    }

    public Unit() {
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getTimeMode() {
        return timeMode;
    }

    public void setTimeMode(int timeMode) {
        this.timeMode = timeMode;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "unit=" + unit +
                ", timeMode=" + timeMode +
                '}';
    }
}
