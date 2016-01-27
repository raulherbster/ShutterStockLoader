package com.example.herbster.shutterstockloader.communication;

import java.net.HttpURLConnection;

/**
 * Created by herbster on 1/28/2016.
 */
public class ServiceResponse {

    private int mResponseCode;

    private String mContent;

    public ServiceResponse() {
        mResponseCode = HttpURLConnection.HTTP_OK;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public String getContent() {
        return mContent;
    }

    public void setResponseCode(int responseCode) {
        this.mResponseCode = responseCode;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public boolean isError() {
        return getResponseCode() != HttpURLConnection.HTTP_OK && getResponseCode() != HttpURLConnection.HTTP_ACCEPTED;
    }

}
