package com.coreproc.android.kitchen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.design.widget.TabLayout;

import com.coreproc.android.kitchen.models.APIError;
import com.coreproc.android.kitchen.models.SampleUserCredentials;
import com.coreproc.android.kitchen.models.User;
import com.coreproc.android.kitchen.utils.ErrorUtil;
import com.coreproc.android.kitchen.utils.RestClient;
import com.coreproc.android.kitchen.utils.ApiInterface;
import com.coreproc.android.kitchen.utils.UiUtil;
import com.google.gson.Gson;
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

    public Context mContext;
    private AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;
    private boolean mApplicationHasLayout = false;

    // API Values
    protected String mBaseUrl;
    protected String mAuthKey;
    protected String mLoginUrlSegment;
    protected LoginCallback mLoginCallback = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    public interface LoginCallback {
        void onStart();

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

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);


        if (!mApplicationHasLayout) {

            // No layout; will use the kitchen layout
            getSupportActionBar().hide();
            prepareTabs();
            prepareLoginForm();
            prepareSignUp();

        }
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

    private void prepareTabs() {

        final LinearLayout loginLinearLayout = (LinearLayout) findViewById(R.id.layout_login);
        final LinearLayout signUpLinearLayout = (LinearLayout) findViewById(R.id.layout_sign_up);

        loginLinearLayout.setVisibility(View.VISIBLE);
        signUpLinearLayout.setVisibility(View.GONE);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.action_log_in_short)).setTag("login"));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.action_sign_up)).setTag("signup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getTag().toString()) {
                    case "login":
                        loginLinearLayout.setVisibility(View.VISIBLE);
                        signUpLinearLayout.setVisibility(View.GONE);
                        break;

                    case "signup":
                        loginLinearLayout.setVisibility(View.GONE);
                        signUpLinearLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void prepareLoginForm() {

        mEmailView = (EditText) findViewById(R.id.email);
        mEmailView.addTextChangedListener(mEmailViewTextWatcher);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.addTextChangedListener(mPasswordViewTextWatcher);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        loginFunction(mEmailView, mPasswordView, mEmailSignInButton, mLoginCallback);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void prepareSignUp() {
        mEmailView = (EditText) findViewById(R.id.email);
        mEmailView.addTextChangedListener(mEmailViewTextWatcher);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.addTextChangedListener(mPasswordViewTextWatcher);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        loginFunction(mEmailView, mPasswordView, mEmailSignInButton, mLoginCallback);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void showProgress(final boolean show) {


        if (mApplicationHasLayout) {
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

            return;
        }

        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    TextWatcher mEmailViewTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s.toString())) {
                mEmailView.setError(getString(R.string.error_field_required));
            } else if (!isEmailValid(s.toString())) {
                mEmailView.setError(getString(R.string.error_invalid_email));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void setLoginCallback(LoginCallback loginCallback) {
        mLoginCallback = loginCallback;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    TextWatcher mPasswordViewTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Check for a valid password, if the user entered one.
            if (!TextUtils.isEmpty(s.toString()) && !isPasswordValid(s.toString())) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private void setApiValues() {
        // Main Application Context
        // Getting Base URL from meta-data
        Context mainApplicationContext = setApplicationContext();
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

        try {
            app = mainApplicationContext.getPackageManager().getApplicationInfo(mainApplicationContext.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;
            mAuthKey = bundle.getString("login-auth-key", "");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            app = mainApplicationContext.getPackageManager().getApplicationInfo(mainApplicationContext.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;
            mLoginUrlSegment = bundle.getString("login-url-segment", "");

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

    protected void loginFunction(final TextView userNameTextView, final TextView passwordTextView, Button loginButton, final LoginCallback callBack) {

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (callBack == null) {
                    UiUtil.showAlertDialog(mContext, "Callback not found", "Please set a LOGIN callback using \"setLoginCallback()\".");
                    return;
                }

                callBack.onStart();

                String userName = userNameTextView.getText().toString();
                String password = passwordTextView.getText().toString();

                if (userName.equals("") && password.equals("")) {
                    String title = "Error";
                    String message = "Please fill required fields";
                    UiUtil.showAlertDialog(mContext, title, message, true);
                    return;
                }

                if (mLoginUrlSegment.length() == 0) {
                    UiUtil.showAlertDialog(mContext, "URL not found", "Login URL not found in manifest. Please declare a meta-data value with name \"login-url-segment\".");
                    return;
                }

                showProgress(true);
                String auth = mAuthKey;
                ApiInterface apiInterface = RestClient.getmApiInterface(mBaseUrl);
                SampleUserCredentials userCredentials = new SampleUserCredentials(userName, password);
                Call<JsonObject> call = apiInterface.Login(mLoginUrlSegment, auth, userCredentials);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (!response.isSuccessful()) {
                            showProgress(false);
                            Log.i("tag", "wrong credentials");
                            APIError error = ErrorUtil.parsingError(response);
                            callBack.onError(error.getError());
                            return;
                        }
                        Log.i("tag", "success");
                        Log.i("json", "response:" + response.body());

                        User user = new Gson().fromJson(response.body().get("data").getAsJsonObject(), User.class);

                        showProgress(false);
                        callBack.onSuccess(user, response.body());

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        showProgress(false);
                        callBack.onFailed();
                    }
                });
            }
        });

    }

    protected void loginFunction(String userName, String password, final LoginCallback callBack) {

        if (callBack == null) {
            UiUtil.showAlertDialog(mContext, "Callback not found", "Please set a LOGIN callback using \"setLoginCallback()\".");
            return;
        }

        callBack.onStart();


        if (userName.equals("") && password.equals("")) {
            String title = "Error";
            String message = "Please fill required fields";
            UiUtil.showAlertDialog(mContext, title, message, true);
            return;
        }

        if (mLoginUrlSegment.length() == 0) {
            UiUtil.showAlertDialog(mContext, "URL not found", "Login URL not found in manifest. Please declare a meta-data value with name \"login-url-segment\".");
            return;
        }

        showProgress(true);
        String auth = mAuthKey;
        ApiInterface apiInterface = RestClient.getmApiInterface(mBaseUrl);
        SampleUserCredentials userCredentials = new SampleUserCredentials(userName, password);
        Call<JsonObject> call = apiInterface.Login(mLoginUrlSegment, auth, userCredentials);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    showProgress(false);
                    Log.i("tag", "wrong credentials");
                    APIError error = ErrorUtil.parsingError(response);
                    callBack.onError(error.getError());
                    return;
                }
                Log.i("tag", "success");
                Log.i("json", "response:" + response.body());

                User user = new Gson().fromJson(response.body().get("data").getAsJsonObject(), User.class);

                showProgress(false);
                callBack.onSuccess(user, response.body());

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showProgress(false);
                callBack.onFailed();
            }
        });


    }


}
