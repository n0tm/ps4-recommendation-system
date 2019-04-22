package com.example.playstationsearchjava.Utils.Api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.example.playstationsearchjava.Utils.Api.Models.ResponseModel;
import com.example.playstationsearchjava.Utils.Exceptions.YoApiException;

public class YoApi {

    private ResponseModel Response;

    private static final String API_URL = "http://159.69.35.190:8000/";

    public Request.Builder getRequestQuery(String method)
    {
        if (!method.isEmpty()) return new Request.Builder()
                .url(API_URL + method);

        return new Request.Builder()
                .url(API_URL);
    }

    public ResponseModel request(Request request)
    {
        OkHttpClient client = new OkHttpClient
                .Builder()
                .retryOnConnectionFailure(true)
                .build();

        Log.d("New request, URL -", request.toString());

        try {

            Response responseData = client.newCall(request).execute();

            String responseString = responseData.body().string();
            JSONObject response = new JSONObject(responseString);
            Headers headers = responseData.headers();

            this.Response = new ResponseModel(
                    response.getInt("StatusCode"),
                    response.getString("StatusMsg"),
                    response.isNull("response") ? new JSONObject() : response.getJSONObject("response"),
                    headers);

//            if (this.Response.getStatusCode() > 300) throw new YoApiException(ApiExceptions.parse(this.Response.getStatusCode()));

            return Response;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
