package com.smartplant.smartplantandroid.storage.network.base;

import com.smartplant.smartplantandroid.storage.models.StorageDataMessage;
import com.smartplant.smartplantandroid.utils.network.TransferResponse;

public interface StorageWSProcessor {
    void processTransferResponse(TransferResponse response);

    void processStorageDataMessage(StorageDataMessage dataMessage);
}
