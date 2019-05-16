package com.example.playstationsearchjava.Utils.Api.Models;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class ResponseModel {
    private JSONObject Response;
    private Headers Headers;

    public ResponseModel(JSONObject Response, Headers headers)
    {
        this.Response = Response;
        this.Headers = headers;
    }

    public JSONObject getResponse() {
        try {
            return this.Response.getJSONObject("result");
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public JSONObject getFullResponse()
    {
        return this.Response;
    }

    public Integer getStatusCode() {
        try {
            return this.Response.getInt("StatusCode");
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getStatusMsg() {
        try {
            return this.Response.getString("StatusMsg");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public okhttp3.Headers getHeaders() {
        return Headers;
    }
}
