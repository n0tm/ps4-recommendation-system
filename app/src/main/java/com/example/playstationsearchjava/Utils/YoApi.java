package com.example.playstationsearchjava.Utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import com.example.playstationsearchjava.Utils.*;

public class YoApi {

    private static final String API_URL = "http://s2.livecover.xyz/experiments/api.php/";

    private OkHttpClient client = new OkHttpClient();

    public Request.Builder getRequestQuery(String method)
    {
        if (!method.isEmpty()) return new Request.Builder()
                .url(API_URL + method + "/");

        return new Request.Builder()
                .url(API_URL);
    }

    public JSONObject request(Request request)
    {
        return _request(request);
    }

    private JSONObject _request(Request request)
    {
        Log.d("New request, URL -", request.toString());

        JSONObject response = new JSONObject();

        try {
            String responseString = client.newCall(request).execute().body().string();
            response = new JSONObject(responseString);
            Log.d("Response from request", responseString);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }
}
