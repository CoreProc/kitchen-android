package com.coreproc.android.kitchen.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by willm on 8/2/2017.
 */

public class Country implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("name")
    @Expose
    public String name;

    public Country(JsonObject jsonObject) {
        this.id = "" + jsonObject.get("id").getAsInt();
        this.name = jsonObject.get("name").getAsString();
    }

}
