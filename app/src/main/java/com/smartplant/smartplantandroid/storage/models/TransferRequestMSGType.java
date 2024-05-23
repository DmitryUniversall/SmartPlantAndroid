package com.smartplant.smartplantandroid.storage.models;

public enum TransferRequestMSGType {
    SEND_QUEUE_MESSAGE(1);

    private final int value;

    TransferRequestMSGType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
