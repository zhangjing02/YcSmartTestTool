package com.czjk.blelib.exception;

import com.czjk.blelib.model.BleExceptionCode;

import java.io.Serializable;


public class BleException implements Serializable {
    private BleExceptionCode code;
    private String description;

    public BleException(BleExceptionCode code, String description) {
        this.code = code;
        this.description = description;
    }

    public BleExceptionCode getCode() {
        return code;
    }

    public BleException setCode(BleExceptionCode code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "BleException{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }
}
