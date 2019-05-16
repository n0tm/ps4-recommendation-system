package com.example.playstationsearchjava.Utils.Api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.example.playstationsearchjava.Utils.Api.Methods.Games;
import com.example.playstationsearchjava.Utils.Api.Methods.Genres;
import com.example.playstationsearchjava.Utils.Api.Models.ResponseModel;

public class YoApi {

    private ResponseModel Response;
    private Genres Genres;
    private Games Games;

    public static final String API_URL = "http://159.69.35.190:8000/";

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

            this.Response = new ResponseModel(response, headers);

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

    public Genres Genres()
    {
        if (this.Genres == null) this.Genres = new Genres();

        return this.Genres;
    }

    public Games Games()
    {
        if (this.Games == null) this.Games = new Games();

        return this.Games;
    }
}
