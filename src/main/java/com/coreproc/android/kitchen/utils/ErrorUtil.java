package com.coreproc.android.kitchen.utils;


import android.util.Log;

import com.coreproc.android.kitchen.models.APIError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by IanBlanco on 10/6/2016.
 */

public class ErrorUtil {
    public static APIError parsingError(Response<?> response) {

        APIError apiError = null;
        Converter<ResponseBody, APIError> converter = null;

        converter =
                KitchenRestClient.getmRetrofit()
                        .responseBodyConverter(APIError.class, new Annotation[0]);
        try {
            apiError = converter.convert(response.errorBody());
        } catch (IOException e) {
            apiError = new APIError();
            APIError.Error error = new APIError.Error();
            error.code = "500";
            error.message = new JsonParser().parse("{\"message\" : \"An error occured.\"}");
            error.httpCode = "GEN-ERROR";
            apiError.error = error;
        }

        return apiError;
    }
}