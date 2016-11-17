package com.coreproc.android.kitchen.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by IanBlanco on 9/9/2016.
 */
public class KitchenRestClient {

    private static ApiInterface mApiInterface;
//    private static String BASE_URL = "https://api.github.com/";
    private static Retrofit mRetrofit = null;

    public static ApiInterface getmApiInterface(String url) {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit.create(ApiInterface.class);
    }

    public static <T> T create(Context context, Class<T> customInterface, boolean withAuthorization) {
        if (mRetrofit == null) {
            String baseUrl = "";
            // Get Base URL from meta-data
            ApplicationInfo app = null;
            try {
                app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                Bundle bundle = app.metaData;
                baseUrl = bundle.getString("base-url", "");

                if (baseUrl.length() == 0) {
                    UiUtil.showAlertDialog(context, "URL not found", "Base URL not found in manifest. Please declare a meta-data value with name \"base-url\".");
                    return null;
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit.create(customInterface);
    }

    public static Retrofit getmRetrofit(){
        return mRetrofit;
    }

}

