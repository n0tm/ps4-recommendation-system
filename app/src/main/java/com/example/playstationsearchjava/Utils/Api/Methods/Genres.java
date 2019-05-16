package com.example.playstationsearchjava.Utils.Api.Methods;

import com.example.playstationsearchjava.Utils.Api.Models.ResponseModel;
import com.example.playstationsearchjava.Utils.Api.YoApi;

import okhttp3.Request;

public class Genres {

    private YoApi api;

    public Genres()
    {
        this.api = new YoApi();
    }

    public ResponseModel getFavorite(String Cookie)
    {
        Request request = this.api.getRequestQuery(MethodsList.GET_FAVORITE_GENRES)
                .addHeader("Cookie", Cookie)
                .build();

        ResponseModel Response = this.api.request(request);

        return Response;
    }

    public ResponseModel get(String Cookie)
    {
        Request request = this.api.getRequestQuery(MethodsList.GET_GENRES)
                .addHeader("Cookie", Cookie)
                .build();

        ResponseModel Response = this.api.request(request);

        return Response;
    }

}
