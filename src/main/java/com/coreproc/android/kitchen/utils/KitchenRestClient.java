package com.coreproc.android.kitchen.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import android.util.Log;

import com.coreproc.android.kitchen.Kitchen;
import com.coreproc.android.kitchen.preferences.Preferences;
import com.coreproc.android.kitchen.utils.KitchenUiUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class KitchenRestClient {

    private static String PREFS_CUSTOM_HEADERS = "PREFS_CUSTOM_HEADERS";
    private static String PREFS_INCLUDE_ADDITIONAL_HEADERS = "PREFS_INCLUDE_ADDITIONAL_HEADERS";
    private static Retrofit mRetrofit = null;
    private String appVersionName = "N/A";

    private static ApiInterface mApiInterface;
    //    private static String BASE_URL = "https://api.github.com/";

    public static ApiInterface getmApiInterface(String url) {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit.create(ApiInterface.class);
    }
    
    public static Retrofit getmRetrofit() {
        return mRetrofit;
    }

    
    public KitchenRestClient() {

    }

    public static Retrofit getRetrofitInstance() {
        return mRetrofit;
    }

    public static void includeAdditionalHeaders(Context context, boolean include) {
        Preferences.setBoolean(context, PREFS_INCLUDE_ADDITIONAL_HEADERS, include);
    }

    public static <T> T create(final Context context, Class<T> interFace, final boolean withAuthorization) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(Level.BODY);
        String baseUrl = Kitchen.getMetadataValue(context, "base-url");
        if (baseUrl.length() == 0) {
            KitchenUiUtils.showAlertDialog(context, "URL not found", "Base URL not found in manifest. Please declare a meta-data value with name \"base-url\".");
            return null;
        }

        OkHttpClient client = (new okhttp3.OkHttpClient.Builder())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(getHeadersInterceptor(context, withAuthorization, null))
                .build();
        mRetrofit = (new Builder())
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create()).build();
        return mRetrofit.create(interFace);

    }

    public static <T> T create(final Context context, Class<T> interFace, final boolean withAuthorization, HashMap<String, String> customHeaders) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(Level.HEADERS);
        String baseUrl = Kitchen.getMetadataValue(context, "base-url");
        if (baseUrl.length() == 0) {
            KitchenUiUtils.showAlertDialog(context, "URL not found", "Base URL not found in manifest. Please declare a meta-data value with name \"base-url\".");
            return null;
        }

        OkHttpClient client = (new okhttp3.OkHttpClient.Builder())
                .addInterceptor(interceptor)
                .addInterceptor(getHeadersInterceptor(context, withAuthorization, customHeaders))
                .build();
        mRetrofit = (new Builder()).client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        return mRetrofit.create(interFace);

    }


    /**
     * HEADERS
     */

    private static Interceptor getHeadersInterceptor(final Context context, final boolean withAuthorization,
                                                     final HashMap<String, String> customHeaders) {
        final String authKey = Preferences.getString(context, Preferences.API_KEY);
        final String fcmToken = Preferences.getString(context, Preferences.FCM_TOKEN);
        final String osVersion = VERSION.RELEASE;

        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersionName = pInfo.versionName;
        } catch (Exception e) {
        }


        Log.d("authkey", "kitchentoken " + authKey);
        Log.d("authkey", "fcmkey " + fcmToken);

        return new Interceptor() {
            public Response intercept(Chain chain) throws IOException {

                Request.Builder builder = chain.request().newBuilder()
                        .addHeader("X-Device-App-Version", appVersionName)
                        .addHeader("X-Device-OS", "android")
                        .addHeader("X-Device-OS-Version", osVersion)
                        .addHeader("X-Device-Manufacturer", "" + Build.MANUFACTURER)
                        .addHeader("X-Device-Model", "" + Build.MODEL)
                        .addHeader("X-Device-UDID", System.getString(context.getContentResolver(), "android_id"))
                        .addHeader("X-Device-FCM-Token", fcmToken);

                HashMap<String, String> headers = new HashMap<>(getCustomHeaders(context));
                if (customHeaders != null) {
                    headers.putAll(customHeaders);
                }

                if (includeCustomHeaders(context)) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        builder.addHeader(key, value);
                    }
                }

                if (withAuthorization) {
                    builder.addHeader("Authorization", "" + authKey);
                }

                Request request = builder.build();
                return chain.proceed(request);
            }
        };

    }


    private static boolean includeCustomHeaders(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), 0);
        return sharedPreferences.getBoolean(PREFS_INCLUDE_ADDITIONAL_HEADERS, true);
    }

    // Extract custom headers
    public static void setAdditionalHeaders(Context context, HashMap<String, String> hash) {

        if (hash == null) {
            return;
        }

        JsonObject jsonObjectBase = new JsonObject();
        JsonArray jsonArrayHeaders = new JsonArray();

        for (Map.Entry<String, String> entry : hash.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            JsonObject jsonObjectHeader = new JsonObject();
            jsonObjectHeader.addProperty("key", key);
            jsonObjectHeader.addProperty("value", value);
            jsonArrayHeaders.add(jsonObjectHeader);
        }

        jsonObjectBase.add("headers", jsonArrayHeaders);

        Preferences.setString(context, PREFS_CUSTOM_HEADERS, jsonObjectBase + "");

    }

    private static HashMap<String, String> getCustomHeaders(Context context) {

        HashMap<String, String> headers = new HashMap<>();

        // get Headers from prefs
        String jsonPrefs = Preferences.getString(context, PREFS_CUSTOM_HEADERS);
        if (jsonPrefs.isEmpty() || jsonPrefs.contains("{}"))
            return headers;

        JsonObject jsonObjectPrefs = new JsonParser().parse(jsonPrefs).getAsJsonObject();
        JsonArray jsonArray = jsonObjectPrefs.get("headers").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            headers.put(jsonObject.get("key").getAsString(),
                    jsonObject.get("value").getAsString());
        }

        return headers;
    }
}
