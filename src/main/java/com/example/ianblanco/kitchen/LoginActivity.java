package com.example.ianblanco.kitchen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ianblanco.kitchen.models.APIError;
import com.example.ianblanco.kitchen.models.SampleUserCredentials;
import com.example.ianblanco.kitchen.models.User;
import com.example.ianblanco.kitchen.utils.ErrorUtil;
import com.example.ianblanco.kitchen.utils.RestClient;
import com.example.ianblanco.kitchen.utils.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IanBlanco on 9/7/2016.
 */
public abstract class LoginActivity extends AppCompatActivity {

    protected abstract int setLayout();
    protected abstract Context setApplicationContext();

    protected String mBaseUrl;
    public Context mContext;
    private AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;
    private boolean mApplicationHasLayout = false;

    public interface LoginCallback {
        void onSuccess(User user, JsonObject jsonObject);

        void onError(APIError.Error error);

        void onFailed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(setLayout() == 0 ? R.layout.login_layout : setLayout());
        mContext = this;

        mApplicationHasLayout = setLayout() != 0;
        setApiValues();

//        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        // Get the ActionBar here to configure the way it behaves.
//        final ActionBar mActionBar = getSupportActionBar();
//        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
//        mActionBar.setDisplayShowHomeEnabled(true); // show or hide the default home button
//        mActionBar.setDisplayHomeAsUpEnabled(true);
//        mActionBar.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
//        mActionBar.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

    }

    private void setApiValues() {
        // Main Application Context
        // Base URL
        Context mainApplicationContext = setApplicationContext();
        ApplicationInfo app = null;
        try {
            app = mainApplicationContext.getPackageManager().getApplicationInfo(mainApplicationContext.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;
            mBaseUrl = bundle.getString("base-url", "");

            if (mBaseUrl.length() == 0) {
                showAlertDialog("URL not found", "Base URL not found in manifest. Please declare a meta-data value with name \"base-url\".");
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        mProgressDialog = null;

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }

        mAlertDialog = null;
    }

    protected void loginFunction(String userName, String password, final LoginCallback callBack) {

        Log.i("tag", userName);
        if (userName.equals("") && password.equals("")) {
            String title = "Error";
            String message = "Please fill required fields";
            showAlertDialog(title, message, true);
        } else {
            String auth = "c814048d0ecb678a451f58da18c4897ca8c068b8";
            ApiInterface apiInterface = RestClient.getmApiInterface(mBaseUrl);
            SampleUserCredentials userCredentials = new SampleUserCredentials(userName, password);
            Call<JsonObject> call = apiInterface.Login(auth, userCredentials);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (!response.isSuccessful()) {
                        Log.i("tag", "wrong credentials");
                        APIError error = ErrorUtil.parsingError(response);
                        callBack.onError(error.getError());
                        return;
                    }
                    Log.i("tag", "success");
                    Log.i("json", "response:" + response.body());

                    User user = new Gson().fromJson(response.body().get("data").getAsJsonObject(), User.class);

                    callBack.onSuccess(user, response.body());

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                   callBack.onFailed();
                }
            });


        }
    }


    protected void showAlertDialog(AlertDialog alertDialog) {
        dismissAlertDialog();
        mAlertDialog = alertDialog;
        mAlertDialog.show();
    }

    protected void showAlertDialog(String title, String message, boolean cancelable) {
        if (mContext == null) {
            return;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                .create();
        showAlertDialog(alertDialog);
    }

    protected void showAlertDialog(String title, String message) {
        showAlertDialog(title, message, false);
    }

    protected void showAlertDialog(String message) {
        showAlertDialog("", message, false);
    }

    protected void dismissAlertDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        mAlertDialog = null;
    }


}
