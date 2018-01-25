package com.czjk.blelib.exception;


import com.czjk.blelib.model.BleExceptionCode;


public class GattException extends BleException {
    private int gattStatus;

    public GattException(int gattStatus) {
        super(BleExceptionCode.GATT_ERR, "Gatt Exception Occurred! ");
        this.gattStatus = gattStatus;
    }

    @Override
    public String toString() {
        return "GattException{" +
                "gattStatus=" + gattStatus +
                '}' + super.toString();
    }
}
