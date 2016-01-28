package com.example.herbster.shutterstockloader.communication;

import android.util.Base64;

import java.util.HashMap;
import java.util.Map;

/**
 * This class models a request to a REST service. Requests contains some information to be used for
 * a given request, for instance, header's properties to be set.
 *
 * Created by herbster on 1/28/2016.
 */
public class ServiceRequest {

    private Map<String,String> mHeaderElements;

    /**
     * Default constructor.
     */
    public ServiceRequest() {
        mHeaderElements = new HashMap<String,String>();
    }

    /**
     * Sets the credentials for a basic authentication in which no request token is necessary, just
     * the credentials in plain text and in the format client_id:client_password
     * @param credentials the credentials in the format client_id:client_password
     * @return true, in case the credentials have been properly set; false otherwise.
     */
    public boolean setAuthBasic(String credentials) {
        if (credentials == null)
            return false;
        String credentialsEncoded = "Basic " + Base64.encode(credentials.getBytes(), Base64.DEFAULT);
        mHeaderElements.put("Authorization",credentialsEncoded);
        return true;
    }

    /**
     * Returns the properties as a Map of strings.
     * @return the properties as a Map of strings.
     */
    public Map<String,String> getProperties() {
        return mHeaderElements;
    }

}
