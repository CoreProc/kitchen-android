package com.coreproc.android.kitchen.models;

import android.support.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IanBlanco on 10/5/2016.
 */

public class APIError {
    @SerializedName("error")
    public Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public static class Error {

        @SerializedName("code")
        public String code;

        @SerializedName("http_code")
        public String httpCode;

        @SerializedName("message")
        @Nullable
        protected JsonElement message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getHttpCode() {
            return httpCode;
        }

        public void setHttpCode(String httpCode) {
            this.httpCode = httpCode;
        }

        public String getMessage() {
            // parse message here
            String errorDescription = "";
            try {
                if (message.isJsonObject()) {
                    JsonObject messageJsonObject = (JsonObject) message;
                    Set<Map.Entry<String, JsonElement>> entries = messageJsonObject.entrySet();
                    for (Map.Entry<String, JsonElement> entry : entries) {
                        String message = messageJsonObject.get(entry.getKey()).getAsString();
                        errorDescription += message + "\n";
                    }
                } else {
                    errorDescription = message.getAsString();
                }
                return errorDescription;
            } catch (Exception ex) {
                return "An error occured. Please try again.";
            }
        }

    }

}
