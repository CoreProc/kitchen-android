package com.example.ianblanco.kitchen.utils;

import com.example.ianblanco.kitchen.models.SampleUserCredentials;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by IanBlanco on 9/9/2016.
 */
public interface ApiInterface {

    String AUTHORIZATION = "X-Authorization";

    @POST()
    Call<JsonObject> Login(
            @Url String url, @Header(AUTHORIZATION) String authorization, @Body SampleUserCredentials userCredentials);

}

