package com.example.ianblanco.vonbirthdayapp.utils;


import com.example.ianblanco.vonbirthdayapp.models.APIError;
import com.google.gson.JsonObject;

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
                RestClient.getmRetrofit()
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