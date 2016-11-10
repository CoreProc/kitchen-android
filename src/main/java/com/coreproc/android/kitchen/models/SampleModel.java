package com.coreproc.android.kitchen.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by IanBlanco on 9/9/2016.
 */
public class SampleModel {

    @SerializedName("login")
    private String mUser;

    @SerializedName("url")
    private String mUrl;

    @SerializedName("type")
    private String mUserType;



    public SampleModel(String user, String url, String userType){
        this.mUser = user;
        this.mUrl = url;
        this.mUserType = userType;
    }


    public String getUser() {
        return mUser;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getUserType() {
        return mUserType;
    }
}
