package com.example.herbster.shutterstockloader.ui;

import android.os.AsyncTask;

import com.example.herbster.shutterstockloader.communication.RESTService;
import com.example.herbster.shutterstockloader.communication.ServiceRequest;
import com.example.herbster.shutterstockloader.communication.ServiceResponse;

/**
 * Created by herbster on 1/28/2016.
 */
public class DownloadImage extends AsyncTask<ServiceRequest,Void,String> {

    private String mURL;

    public DownloadImage(String url) {
        mURL = url;
    }

    @Override
    protected String doInBackground(ServiceRequest... params) {
        if (params == null || params.length == 0)
            return null;

        RESTService restService = RESTService.create(mURL);
        if (restService != null) {
            ServiceResponse response = restService.doGet(params[0]);

            if (response != null && !response.isError()) {
                return response.getContent();
            }  else
                return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String string) {
    }

}
