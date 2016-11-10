package com.coreproc.android.kitchen.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by IanBlanco on 9/9/2016.
 */
public class RestClient {

    private static ApiInterface mApiInterface;
//    private static String BASE_URL = "https://api.github.com/";
    private static Retrofit mRetrofit = null;

    public static ApiInterface getmApiInterface(String url) {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return mRetrofit.create(ApiInterface.class);
    }
    public static Retrofit getmRetrofit(){
        return mRetrofit;
    }

}

