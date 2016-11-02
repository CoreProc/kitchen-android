package com.example.ianblanco.vonbirthdayapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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

    public static class Error{

        @SerializedName("code")
        public String code;
        @SerializedName("http_code")
        public String httpCode;
        @SerializedName("message")
        public String message;

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
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

}
