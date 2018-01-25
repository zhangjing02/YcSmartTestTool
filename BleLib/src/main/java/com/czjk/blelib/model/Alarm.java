package com.czjk.blelib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by czjk on 2016/12/4.
 */

public class Alarm implements Parcelable{

    private int alarmId;
    private String name;
    private String time;
    private String repeat;
    private int state;  //1 kai 0 关闭
    public Alarm(int alarmId, String name, String time, String repeat, int state) {
        this.alarmId = alarmId;
        this.name = name;
        this.time = time;
        this.repeat = repeat;
        this.state = state;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.alarmId);
        dest.writeString(this.name);
        dest.writeString(this.time);
        dest.writeString(this.repeat);
        dest.writeInt(this.state);
    }

    public Alarm() {
    }

    protected Alarm(Parcel in) {
        this.alarmId = in.readInt();
        this.name = in.readString();
        this.time = in.readString();
        this.repeat = in.readString();
        this.state = in.readInt();
    }

    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel source) {
            return new Alarm(source);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    @Override
    public String toString() {
        return "Alarm{" +
                "alarmId=" + alarmId +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", repeat='" + repeat + '\'' +
                ", state=" + state +
                '}';
    }
}
