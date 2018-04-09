package com.example.mayank.finalchatapp;

/**
 * Created by mayank on 11/1/18.
 */

class DataRequest {
    private  String requestType ;

    public DataRequest() {
    }

    public DataRequest(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
