package com.example.ianblanco.vonbirthdayapp.utils;

import com.example.ianblanco.vonbirthdayapp.models.SampleModel;
import com.example.ianblanco.vonbirthdayapp.models.SampleUserCredentials;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
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

