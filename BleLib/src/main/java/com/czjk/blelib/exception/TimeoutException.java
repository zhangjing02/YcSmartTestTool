package com.czjk.blelib.exception;


import com.czjk.blelib.model.BleExceptionCode;

public class TimeoutException extends BleException {
    public TimeoutException() {
        super(BleExceptionCode.TIMEOUT, "Timeout Exception Occurred! ");
    }
}
