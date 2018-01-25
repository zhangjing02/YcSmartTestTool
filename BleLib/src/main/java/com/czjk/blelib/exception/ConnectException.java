package com.czjk.blelib.exception;

import android.bluetooth.BluetoothGatt;

import com.czjk.blelib.model.BleExceptionCode;


public class ConnectException extends BleException {
    private BluetoothGatt bluetoothGatt;
    private int gattStatus;

    public ConnectException(BluetoothGatt bluetoothGatt, int gattStatus) {
        super(BleExceptionCode.CONNECT_ERR, "Connect Exception Occurred! ");
        this.bluetoothGatt = bluetoothGatt;
        this.gattStatus = gattStatus;
    }

    @Override
    public String toString() {
        return "ConnectException{" +
                "gattStatus=" + gattStatus +
                ", bluetoothGatt=" + bluetoothGatt +
                "} " + super.toString();
    }
}
