package com.czjk.blelib.exception;


import com.czjk.blelib.model.BleExceptionCode;


public class InitiatedException extends BleException {
    public InitiatedException() {
        super(BleExceptionCode.INITIATED_ERR, "Initiated Exception Occurred! ");
    }
}
