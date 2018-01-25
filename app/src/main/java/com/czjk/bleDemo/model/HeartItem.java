package com.czjk.bleDemo.model;

/**
 * Created by czjk on 2017/2/27.
 */

public class HeartItem {
    private int value;
    private int offset;

    public HeartItem() {
    }

    public HeartItem(int value, int offset) {
        this.value = value;
        this.offset = offset;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "HeartItem{" +
                "value=" + value +
                ", offset=" + offset +
                '}';
    }
}
