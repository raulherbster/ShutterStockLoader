package com.example.herbster.shutterstockloader.communication;

import android.net.Uri;
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

    private String mScheme;
    private String mAuthority;
    private String[] mPaths;

    private RESTService(String scheme, String authority, String[] paths) {
        mScheme = scheme;
        mAuthority = authority;
        mPaths = paths;
    }

    /**
     */
    public static RESTService create(String scheme, String authority, String[] paths) {
        if (scheme == null || authority == null)
            return null;
        return new RESTService(scheme,authority,paths);
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
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(mScheme)
                    .authority(mAuthority);
            for (String path : mPaths) {
                builder.appendPath(path);
            }

            Set<Map.Entry<String,String>> parameters = request.getParameters().entrySet();
            for (Map.Entry<String,String> entry : parameters) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
            URL url = new URL(builder.build().toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMETOUT);
            conn.setConnectTimeout(CONNECT_TIMETOUT);
            conn.setRequestMethod(GET_REQUEST);

            Set<Map.Entry<String,String>> properties = request.getProperties().entrySet();
            for (Map.Entry<String,String> entry : properties) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }

            conn.connect();

            response.setResponseCode(conn.getResponseCode());
            String content = getContentAsString(conn.getInputStream());
            response.setContent(content);

            return response;
        } catch (IOException e) {
            Log.e(TAG, " Fail on connection : (" + response.getResponseCode() + ")" + e.getLocalizedMessage());
            return null;
        }
    }
}
