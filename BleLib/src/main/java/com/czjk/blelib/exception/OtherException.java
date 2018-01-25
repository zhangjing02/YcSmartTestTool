package com.czjk.blelib.exception;


import com.czjk.blelib.model.BleExceptionCode;


public class OtherException extends BleException {
    public OtherException(String description) {
        super(BleExceptionCode.OTHER_ERR, description);
    }
}
