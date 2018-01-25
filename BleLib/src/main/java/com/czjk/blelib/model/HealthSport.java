package com.czjk.blelib.model;

import android.os.Parcel;
import android.os.Parcelable;


public class HealthSport implements Parcelable {

	private String date;  //日期  2017 01 17   HH 0--23  mm 00 10 20   50
	private int step; //步数
	private int type; //类型
	private int heartValue;
	private int restingHeartValue;  //静息心率
	private int systolicValue;
	private int diatolicValue;

	public HealthSport(String date, int step, int type, int heartValue, int restingHeartValue, int systolicValue, int diatolicValue) {
		this.date = date;
		this.step = step;
		this.type = type;
		this.heartValue = heartValue;
		this.restingHeartValue = restingHeartValue;
		this.systolicValue = systolicValue;
		this.diatolicValue = diatolicValue;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getHeartValue() {
		return heartValue;
	}

	public void setHeartValue(int heartValue) {
		this.heartValue = heartValue;
	}

	public int getRestingHeartValue() {
		return restingHeartValue;
	}

	public void setRestingHeartValue(int restingHeartValue) {
		this.restingHeartValue = restingHeartValue;
	}

	public int getSystolicValue() {
		return systolicValue;
	}

	public void setSystolicValue(int systolicValue) {
		this.systolicValue = systolicValue;
	}

	public int getDiatolicValue() {
		return diatolicValue;
	}

	public void setDiatolicValue(int diatolicValue) {
		this.diatolicValue = diatolicValue;
	}

	@Override
	public String toString() {
		return "HealthSport{" +
				"date='" + date + '\'' +
				", step=" + step +
				", type=" + type +
				", heartValue=" + heartValue +
				", restingHeartValue=" + restingHeartValue +
				", systolicValue=" + systolicValue +
				", diatolicValue=" + diatolicValue +
				'}';
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.date);
		dest.writeInt(this.step);
		dest.writeInt(this.type);
		dest.writeInt(this.heartValue);
		dest.writeInt(this.restingHeartValue);
		dest.writeInt(this.systolicValue);
		dest.writeInt(this.diatolicValue);
	}

	protected HealthSport(Parcel in) {
		this.date = in.readString();
		this.step = in.readInt();
		this.type = in.readInt();
		this.heartValue = in.readInt();
		this.restingHeartValue = in.readInt();
		this.systolicValue = in.readInt();
		this.diatolicValue = in.readInt();
	}

	public static final Creator<HealthSport> CREATOR = new Creator<HealthSport>() {
		@Override
		public HealthSport createFromParcel(Parcel source) {
			return new HealthSport(source);
		}

		@Override
		public HealthSport[] newArray(int size) {
			return new HealthSport[size];
		}
	};
}
