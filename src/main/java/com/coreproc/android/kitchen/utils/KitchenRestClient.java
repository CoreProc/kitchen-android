package com.coreproc.android.kitchen.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.coreproc.android.kitchen.Kitchen;
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

        // Get Base URL from meta-data
        String baseUrl = Kitchen.getMetadataValue(context, "base-url");
        if (baseUrl.length() == 0) {
            KitchenUiUtils.showAlertDialog(context, "URL not found", "Base URL not found in manifest. Please declare a meta-data value with name \"base-url\".");
            return null;
        }

        // Check for authorization
        OkHttpClient client;

        final String authKey = Preferences.getString(context, Preferences.API_KEY);
        final String fcmToken = Preferences.getString(context, "FCM_TOKEN");
        Log.d("authkey", "HELLO " + authKey);
        Log.d("authkey", "FCMKey " + fcmToken);

        PackageInfo pInfo = null;
        String appVersionName = "";
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            appVersionName = "N/A";
        }

        final String osVersion = android.os.Build.VERSION.RELEASE;


        final String finalAppVersionName = appVersionName;
        client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("X-Authorization", authKey)
                                .addHeader("X-OS", "android")
                                .addHeader("X-App-Version", finalAppVersionName)
                                .addHeader("X-OS-Version", osVersion)
                                .addHeader("X-Device-Name", getDeviceName())
                                .addHeader("X-FCMToken", fcmToken)
                                .build();
                        return chain.proceed(request);
                    }
                }).build();


        mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return mRetrofit.create(interFace);
    }

    public static Retrofit getmRetrofit() {
        return mRetrofit;
    }

    /** Returns the consumer friendly device name */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

//        String phrase = "";
        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
//                phrase += Character.toUpperCase(c);
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
//            phrase += c;
            phrase.append(c);
        }

        return phrase.toString();
    }

}

