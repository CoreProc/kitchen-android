package com.coreproc.android.kitchen.utils;


import com.coreproc.android.kitchen.models.APIError;

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
        Converter<ResponseBody, APIError> converter =
                KitchenRestClient.getmRetrofit()
                        .responseBodyConverter(APIError.class, new Annotation[0]);

        APIError error = null;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return error;
    }
}