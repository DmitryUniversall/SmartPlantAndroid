package com.smartplant.smartplantandroid.core.async;

import androidx.annotation.ReturnThis;

public interface ExecutedInBackground<SuccessCallbackT, FailureCallbackT> {
    @ReturnThis
    ExecutedInBackground<SuccessCallbackT, FailureCallbackT> onSuccess(SuccessCallbackT callback);

    @ReturnThis
    ExecutedInBackground<SuccessCallbackT, FailureCallbackT> onFailure(FailureCallbackT callback);

    @ReturnThis
    ExecutedInBackground<SuccessCallbackT, FailureCallbackT> after(Runnable callback);
}
