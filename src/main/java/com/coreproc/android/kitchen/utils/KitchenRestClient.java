package com.coreproc.android.kitchen.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.coreproc.android.kitchen.preferences.Preferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
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

    public static <T> T create(final Context context, Class<T> interFace, boolean withAuthorization) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

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

            // Check for authorization
            OkHttpClient client;
            if (withAuthorization) {
                client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request request = chain.request()
                                        .newBuilder()
                                        .addHeader("X-Authorization", Preferences.getString(context, Preferences.API_KEY))
                                        .build();
                                return chain.proceed(request);
                            }
                        }).build();
            } else {
                client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            }


            mRetrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit.create(interFace);
    }

    public static Retrofit getmRetrofit(){
        return mRetrofit;
    }

}

