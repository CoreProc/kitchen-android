package com.coreproc.android.kitchen.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kael on 11/17/2016.
 */

public class Preferences {

    public static String API_KEY = "apiKey";

    public static void setSharedPreferencesValue(Context context, String apiKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(API_KEY, apiKey)
                .apply();
    }

    public static void getSharedPreferencesValue(Context context, String )

}
