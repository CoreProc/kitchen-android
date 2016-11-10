package com.coreproc.android.kitchen.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Kael on 11/9/2016.
 */

public class User implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("avatarUrl")
    private String avatarUrl;

    public User(){
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
