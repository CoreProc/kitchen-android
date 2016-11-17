package com.coreproc.android.kitchen.utils;

import com.coreproc.android.kitchen.callbacks.KitchenCallback;
import com.coreproc.android.kitchen.models.LoginCredentials;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

/**
 * Created by IanBlanco on 9/9/2016.
 */
public interface ApiInterface {

    String AUTHORIZATION = "X-Authorization";

    @POST()
    KitchenCallback Login(
            @Url String url, @Header(AUTHORIZATION) String authorization, @Body LoginCredentials userCredentials);

    @POST()
    Call<JsonObject> SignUp(
            @Url String url, @Header(AUTHORIZATION) String authorization, @Body LoginCredentials userCredentials);

    @POST()
    Call<JsonObject> PostRequest(
            @Url String url, @Header(AUTHORIZATION) String authorization, @Body HashMap<String, Object> query);

    @POST()
    Call<JsonObject> PostRequest(
            @Url String url, @Header(AUTHORIZATION) String authorization, @Body JsonObject jsonObject);

    @GET()
    Call<JsonObject> GetRequest(
            @Url String url, @Header(AUTHORIZATION) String authorization, @Body HashMap<String, Object> query);

    @PUT()
    Call<JsonObject> PutRequest(
            @Url String url, @Header(AUTHORIZATION) String authorization, @Body HashMap<String, Object> query);

    @POST()
    Call<JsonObject> PutRequest(
            @Url String url, @Header(AUTHORIZATION) String authorization, @Body JsonObject jsonObject);

}

