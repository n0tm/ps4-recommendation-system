package com.example.playstationsearchjava.Utils.Api.Models;

import org.json.JSONObject;

import okhttp3.Headers;

public class ResponseModel {
    private Integer StatusCode;
    private String StatusMsg;
    private JSONObject Response;
    private Headers Headers;

    public ResponseModel(Integer StatusCode,
            String StatusMsg,
            JSONObject Response,
            Headers Headers)
    {
        this.StatusCode = StatusCode;
        this.StatusMsg = StatusMsg;
        this.Response = Response;
        this.Headers = Headers;
    }

    public ResponseModel(Integer StatusCode,
                         String StatusMsg,
                         JSONObject Response)
    {
        this.StatusCode = StatusCode;
        this.StatusMsg = StatusMsg;
        this.Response = Response;
    }

    public ResponseModel(Integer StatusCode, String StatusMsg)
    {
        this.StatusCode = StatusCode;
        this.StatusMsg = StatusMsg;
    }

    public ResponseModel(Integer StatusCode)
    {
        this.StatusCode = StatusCode;
    }

    public JSONObject getResponse() {
        return Response;
    }

    public Integer getStatusCode() {
        return StatusCode;
    }

    public String getStatusMsg() {
        return StatusMsg;
    }

    public okhttp3.Headers getHeaders() {
        return Headers;
    }

    public Boolean isNullResponse()
    {
        return (this.Response == null);
    }
}
