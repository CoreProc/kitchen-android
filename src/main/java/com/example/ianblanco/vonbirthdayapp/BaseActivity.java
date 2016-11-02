package com.example.ianblanco.vonbirthdayapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ianblanco.vonbirthdayapp.models.APIError;
import com.example.ianblanco.vonbirthdayapp.models.SampleUserCredentials;
import com.example.ianblanco.vonbirthdayapp.utils.ErrorUtil;
import com.example.ianblanco.vonbirthdayapp.utils.RestClient;
import com.example.ianblanco.vonbirthdayapp.utils.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IanBlanco on 9/7/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getLayout();

    protected String mBaseUrl = "http://www.travelbook.ph/";
    public Context mContext;
    private AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;
    private JsonObject jsonObject;

    public interface loginCallback {
        void onSuccess(JsonObject jsonObject);

        void onError(APIError.Error error);

        void onFailed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mContext = this;

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


    protected void loginFunction(String user, String password, final loginCallback callBack) {

        Log.i("tag", user);
        if (user.equals("") && password.equals("")) {
            String title = "Error";
            String message = "Please fill required fields";
            showAlertDialog(title, message, true);
        } else {
            String auth = "c814048d0ecb678a451f58da18c4897ca8c068b8";
            ApiInterface apiInterface = RestClient.getmApiInterface(mBaseUrl);
            SampleUserCredentials userCredentials = new SampleUserCredentials(user, password);
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
                    callBack.onSuccess(response.body());

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
