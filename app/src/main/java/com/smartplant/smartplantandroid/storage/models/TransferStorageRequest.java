package com.smartplant.smartplantandroid.storage.models;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class TransferStorageRequest {
    @SerializedName("msg_type")
    private @NonNull TransferRequestMSGType _msgType;

    @SerializedName("target_device_id")
    private @NonNull String _targetDeviceId;

    @SerializedName("data")
    private @Nullable JsonObject _data;

    public TransferStorageRequest(@NonNull TransferRequestMSGType msgType, @NonNull String targetDeviceId, @Nullable JsonObject data) {
        this._msgType = msgType;
        this._targetDeviceId = targetDeviceId;
        this._data = data;
    }

    @NonNull
    public TransferRequestMSGType getMsgType() {
        return _msgType;
    }

    public void setMsgType(TransferRequestMSGType _msgType) {
        this._msgType = _msgType;
    }

    @NonNull
    public String getTargetDeviceId() {
        return _targetDeviceId;
    }

    public void setTargetDeviceId(String _targetDeviceId) {
        this._targetDeviceId = _targetDeviceId;
    }

    @Nullable
    public JsonObject getData() {
        return _data;
    }

    public void setData(@Nullable JsonObject _data) {
        this._data = _data;
    }
}
