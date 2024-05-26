package com.smartplant.smartplantandroid.storage.network;

import com.smartplant.smartplantandroid.storage.models.StorageDataMessage;
import com.smartplant.smartplantandroid.storage.network.base.StorageWSProcessor;
import com.smartplant.smartplantandroid.utils.AppLogger;
import com.smartplant.smartplantandroid.utils.network.TransferResponse;

public class ActionProcessor implements StorageWSProcessor {
    @Override
    public void processTransferResponse(TransferResponse response) {
        AppLogger.debug("Got transfer response");  // TODO
    }

    @Override
    public void processStorageDataMessage(StorageDataMessage dataMessage) {
        AppLogger.debug("Got storage data message");  // TODO
    }
}
