package com.example.herbster.shutterstockloader;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by herbster on 1/28/2016.
 */
public class ShutterStockAppProperties {

    public static final String PROP_CLIENTID = "clientid";
    public static final String PROP_CLIENTSECRET = "clientsecret";
    public static final String PROP_SCHEME = "image_query_scheme";
    public static final String PROP_AUTHORITY = "image_query_authority";
    public static final String PROP_PATHS = "image_query_paths";
    public static final String PROP_NUM_IMAGES_PER_PAGE = "num_images_per_page";
    private static ShutterStockAppProperties singleton = null;
    private final String TAG = "ShutterStockProp";
    private final String PROPERTIES_FILE = "app.properties";
    private Context mContext;

    private Properties mProperties;

    private ShutterStockAppProperties(Context context) {
        mContext = context;
        try {
            loadProperties();
        } catch (IOException e) {
            Log.e(TAG,"Could not load properties file");
        }
    }

    public static synchronized ShutterStockAppProperties from(Context context) {
        if (singleton == null)
            singleton = new ShutterStockAppProperties(context);
        return singleton;
    }

    public static synchronized ShutterStockAppProperties getInstance() {
        return singleton;
    }

    private void loadProperties() throws IOException {
        mProperties = new Properties();
        mProperties.load(mContext.getAssets().open(PROPERTIES_FILE));
    }

    public String getEncodedCredentials() {
        // return empty if could not load the file
        if (mProperties.isEmpty())
            return "";
        StringBuilder credentials = new StringBuilder();
        credentials.append(mProperties.getProperty(PROP_CLIENTID));
        credentials.append(":");
        credentials.append(mProperties.getProperty(PROP_CLIENTSECRET));
        return new String(Base64.encode(credentials.toString().getBytes(), Base64.NO_WRAP));
    }

    public String getShutterStockQueryScheme() {
        if (mProperties.isEmpty())
            return "";
        return mProperties.getProperty(PROP_SCHEME);
    }

    public String getShutterStockQueryAuthority() {
        if (mProperties.isEmpty())
            return "";
        return mProperties.getProperty(PROP_AUTHORITY);
    }

    public String[] getShutterStockQueryPaths() {
        if (mProperties.isEmpty())
            return new String[0];
        return mProperties.getProperty(PROP_PATHS).split("/");
    }

    public int getNumImagesPerPage() {
        if (mProperties.isEmpty())
            return 1;
        return Integer.parseInt(mProperties.getProperty(PROP_NUM_IMAGES_PER_PAGE));

    }

}
