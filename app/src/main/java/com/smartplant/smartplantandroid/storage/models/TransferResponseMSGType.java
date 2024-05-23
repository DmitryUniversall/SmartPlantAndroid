package com.smartplant.smartplantandroid.storage.models;

public enum TransferResponseMSGType {
    RESPONSE(1),
    DATA(2);

    private final int value;

    TransferResponseMSGType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
