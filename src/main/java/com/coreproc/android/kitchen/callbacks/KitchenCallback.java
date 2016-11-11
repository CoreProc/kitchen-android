package com.coreproc.android.kitchen.callbacks;

import com.coreproc.android.kitchen.models.APIError;
import com.coreproc.android.kitchen.models.User;
import com.google.gson.JsonObject;

/**
 * Created by Kael on 11/11/2016.
 */

public interface KitchenCallback {

    void onStart();

    void onSuccess(JsonObject jsonObject);

    void onError(APIError.Error error);

    void onFailed();

}
