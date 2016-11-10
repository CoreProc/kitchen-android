package com.example.ianblanco.kitchen.utils;

import com.example.ianblanco.kitchen.models.SampleUserCredentials;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by IanBlanco on 9/9/2016.
 */
public interface ApiInterface {

    String AUTHORIZATION = "X-Authorization";

    @POST("api/v2/users/login")
    Call<JsonObject> Login(@Header(AUTHORIZATION) String authorization, @Body SampleUserCredentials userCredentials);



}

