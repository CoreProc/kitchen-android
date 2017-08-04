package com.coreproc.android.kitchen.models;

import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * Created by willm on 8/2/2017.
 */

public class City implements Serializable{

    public String id = "";
    public String name;
    public String stateId;
    public String countryId;
    public String regionId;
    public String latitude;
    public String longitude;
    public StateInfo stateInfo;

    public City(JsonObject jsonObject) {
        this.id = jsonObject.get("id").isJsonNull() ? "0" : jsonObject.get("id").getAsString();
        this.name = jsonObject.get("name").getAsString();
        try {
            this.stateInfo = new StateInfo(jsonObject.get("state").getAsJsonObject());
        } catch (Exception ex) {
            this.stateInfo = null;
        }
    }

    public class StateInfo implements Serializable {

        public State state;
        public StateInfo(JsonObject jsonObject) {
            this.state = new State(jsonObject.get("data").getAsJsonObject());
        }

    }

    public class State implements Serializable {

        public String id;
        public String name;

        public State(JsonObject jsonObject) {
            this.id = jsonObject.get("id").isJsonNull() ? "0" : jsonObject.get("id").getAsString();
            this.name = jsonObject.get("name").getAsString();
        }
    }

}
