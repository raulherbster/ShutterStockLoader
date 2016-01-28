package com.example.herbster.shutterstockloader.communication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

/**
 * This class provides an interface to access REST services through GET, POST, ... methods.
 *
 * Created by herbster on 1/28/2016.
 */
public class RESTService {

    private static final String TAG = "ServiceRequest";

    public static final String GET_REQUEST = "GET";
    public static final int READ_TIMETOUT = 30000;
    public static final int CONNECT_TIMETOUT = 30000;

    private URL mURL;

    private RESTService(URL url) {
        mURL = url;
    }

    /**
     * Create an instance of this object from a URL
     * @param url the URL as string to create a REST service from
     * @return the REST service instance from a given URL
     */
    public static RESTService create(String url) {
        try {
            URL aURL = new URL(url);
            return new RESTService(aURL);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Wrong URL format");
            return null;
        }
    }

    /**
     * Reads the input stream and returns its content as a String
     * @param is the stream to read the content
     * @return the content of the input stream as a string; null, in case of IO exceptions
     * @throws IOException in case of any IO error
     */
    private String getContentAsString(InputStream is) throws IOException {
        String contentAsString = null;
        try {
            contentAsString = readIt(is);
        } catch (IOException e) {
            Log.e(TAG,"Could not read the content");
        } finally {
            is.close();
        }
        return contentAsString;
    }

    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line = "";
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }

    /**
     * Performs a GET method to this URL
     * @param request a service request for such GET request
     * @return the response from the given request
     */
    public ServiceResponse doGet(ServiceRequest request) {
        ServiceResponse response = new ServiceResponse();
        try {
            HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();
            conn.setReadTimeout(READ_TIMETOUT);
            conn.setConnectTimeout(CONNECT_TIMETOUT);
            conn.setRequestMethod(GET_REQUEST);

            Set<Map.Entry<String,String>> entries = request.getProperties().entrySet();
            for (Map.Entry<String,String> entry : entries) {
                conn.setRequestProperty(entry.getKey(),entry.getValue());
            }

            conn.connect();

            response.setResponseCode(conn.getResponseCode());
            String content = getContentAsString(conn.getInputStream());
            response.setContent(content);

            return response;
        } catch (IOException e) {
            Log.e(TAG,"Fail on connection : " + e.getLocalizedMessage());
            return null;
        }
    }
}
