/*
package com.czjk.blelib.model;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;


public class BleDevice implements Parcelable {

    private final BluetoothDevice mDevice;
    private final byte[] mScanRecord;
    private int mCurrentRssi;

    public BleDevice(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
        mDevice = device;
        mCurrentRssi = rssi;
        mScanRecord = scanRecord;
    }


    public String getAddress() {
        return mDevice.getAddress();
    }
    public BluetoothDevice getDevice() {
        return mDevice;
    }
    public String getName() {
        return mDevice.getName();
    }
    public int getRssi() {
        return mCurrentRssi;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mDevice, flags);
        dest.writeByteArray(this.mScanRecord);
        dest.writeInt(this.mCurrentRssi);
    }

    protected BleDevice(Parcel in) {
        this.mDevice = in.readParcelable(BluetoothDevice.class.getClassLoader());
        this.mScanRecord = in.createByteArray();
        this.mCurrentRssi = in.readInt();
    }

    public static final Creator<BleDevice> CREATOR = new Creator<BleDevice>() {
        @Override
        public BleDevice createFromParcel(Parcel source) {
            return new BleDevice(source);
        }

        @Override
        public BleDevice[] newArray(int size) {
            return new BleDevice[size];
        }
    };
}
*/
