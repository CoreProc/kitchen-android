package com.coreproc.android.kitchen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.coreproc.android.kitchen.callbacks.KitchenCallback;
import com.coreproc.android.kitchen.models.APIError;
import com.coreproc.android.kitchen.utils.ApiInterface;
import com.coreproc.android.kitchen.utils.ErrorUtil;
import com.coreproc.android.kitchen.utils.KitchenRestClient;
import com.coreproc.android.kitchen.utils.UiUtil;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kael on 11/11/2016.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract Context setContext();
    protected abstract int setLayout();

    private Context mContext;
    private String mBaseUrl;
    private String mAuthKey;
    private String mLoginUrlSegment;
    private String mSignUpUrlSegment;
    private boolean mActivityHasLayout = false;

    // UI References
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((setLayout() == 0) ? R.layout.activity_base : setLayout());

        mContext = this;
        mActivityHasLayout = (setLayout() != 0);

        if (!mActivityHasLayout) {
            UiUtil.showAlertDialog(mContext, "No Layout Found", "Please set a layout resource.");
        }

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);

        setApiValues();
    }

    private void setApiValues() {
        // Main Application Context
        // Get Base URL from meta-data
        Context mainApplicationContext = setContext();
        ApplicationInfo app = null;
        try {
            app = mainApplicationContext.getPackageManager().getApplicationInfo(mainApplicationContext.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;
            mBaseUrl = bundle.getString("base-url", "");

            if (mBaseUrl.length() == 0) {
                UiUtil.showAlertDialog(mContext, "URL not found", "Base URL not found in manifest. Please declare a meta-data value with name \"base-url\".");
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Get Login Auth Key
        try {
            app = mainApplicationContext.getPackageManager().getApplicationInfo(mainApplicationContext.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;
            mAuthKey = bundle.getString("login-auth-key", "");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Get Login URL Segment
        try {
            app = mainApplicationContext.getPackageManager().getApplicationInfo(mainApplicationContext.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;
            mLoginUrlSegment = bundle.getString("login-url-segment", "");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Get SignUp URL Segment
        try {
            app = mainApplicationContext.getPackageManager().getApplicationInfo(mainApplicationContext.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;
            mSignUpUrlSegment = bundle.getString("signup-url-segment", "");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void postRequest(JsonObject jsonObject, final KitchenCallback kitchenCallback) {

        if (jsonObject == null) {
            UiUtil.showAlertDialog(mContext, "Parameters Missing", "Please set parameter as JsonObject.");
            return;
        }

        if (kitchenCallback == null) {
            UiUtil.showAlertDialog(mContext, "Callback Missing", "Please set a callback.");
            return;
        }

        showProgress(true);
        kitchenCallback.onStart();
        String authKey = mAuthKey;
        ApiInterface apiInterface = KitchenRestClient.getmApiInterface(mBaseUrl);
        Call<JsonObject> call = apiInterface.PostRequest(mLoginUrlSegment, authKey, jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    showProgress(false);
                    Log.i("tag", "wrong credentials");
                    APIError error = ErrorUtil.parsingError(response);
                    kitchenCallback.onError(error.getError());
                    return;
                }
                Log.i("tag", "success");
                Log.i("json", "response:" + response.body());

                showProgress(false);
                kitchenCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showProgress(false);
                kitchenCallback.onFailed();
            }
        });

    }

    protected void postRequest(HashMap<String, Object> params, final KitchenCallback kitchenCallback) {

        if (params == null) {
            UiUtil.showAlertDialog(mContext, "Parameters Missing", "Please set parameter as HashMap<String, Object>.");
            return;
        }

        if (kitchenCallback == null) {
            UiUtil.showAlertDialog(mContext, "Callback Missing", "Please set a callback.");
            return;
        }

        showProgress(true);
        kitchenCallback.onStart();
        String authKey = mAuthKey;
        ApiInterface apiInterface = KitchenRestClient.getmApiInterface(mBaseUrl);
        Call<JsonObject> call = apiInterface.PostRequest(mLoginUrlSegment, authKey, params);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    showProgress(false);
                    Log.i("tag", "wrong credentials");
                    APIError error = ErrorUtil.parsingError(response);
                    kitchenCallback.onError(error.getError());
                    return;
                }
                Log.i("tag", "success");
                Log.i("json", "response:" + response.body());

                showProgress(false);
                kitchenCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showProgress(false);
                kitchenCallback.onFailed();
            }
        });
    }

    protected void putRequest(HashMap<String, Object> params, final KitchenCallback kitchenCallback) {

        if (params == null) {
            UiUtil.showAlertDialog(mContext, "Parameters Missing", "Please set parameter as HashMap<String, Object>.");
            return;
        }

        if (kitchenCallback == null) {
            UiUtil.showAlertDialog(mContext, "Callback Missing", "Please set a callback.");
            return;
        }

        showProgress(true);
        kitchenCallback.onStart();
        String authKey = mAuthKey;
        ApiInterface apiInterface = KitchenRestClient.getmApiInterface(mBaseUrl);
        Call<JsonObject> call = apiInterface.PutRequest(mLoginUrlSegment, authKey, params);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    showProgress(false);
                    Log.i("tag", "wrong credentials");
                    APIError error = ErrorUtil.parsingError(response);
                    kitchenCallback.onError(error.getError());
                    return;
                }
                Log.i("tag", "success");
                Log.i("json", "response:" + response.body());

                showProgress(false);
                kitchenCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showProgress(false);
                kitchenCallback.onFailed();
            }
        });
    }

    protected void putRequest(JsonObject jsonObject, final KitchenCallback kitchenCallback) {

        if (jsonObject == null) {
            UiUtil.showAlertDialog(mContext, "Parameters Missing", "Please set parameter as JsonObject.");
            return;
        }

        if (kitchenCallback == null) {
            UiUtil.showAlertDialog(mContext, "Callback Missing", "Please set a callback.");
            return;
        }

        showProgress(true);
        kitchenCallback.onStart();
        String authKey = mAuthKey;
        ApiInterface apiInterface = KitchenRestClient.getmApiInterface(mBaseUrl);
        Call<JsonObject> call = apiInterface.PutRequest(mLoginUrlSegment, authKey, jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    showProgress(false);
                    Log.i("tag", "wrong credentials");
                    APIError error = ErrorUtil.parsingError(response);
                    kitchenCallback.onError(error.getError());
                    return;
                }
                Log.i("tag", "success");
                Log.i("json", "response:" + response.body());

                showProgress(false);
                kitchenCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showProgress(false);
                kitchenCallback.onFailed();
            }
        });

    }

    private void showProgress(final boolean show) {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);

        if (show) {
            mProgressDialog.show();
        } else {
            if (mProgressDialog.isShowing())
                mProgressDialog.hide();
        }

    }

}
