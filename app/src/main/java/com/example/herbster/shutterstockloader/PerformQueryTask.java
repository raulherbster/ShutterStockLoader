package com.example.herbster.shutterstockloader;

import android.os.AsyncTask;

import com.example.herbster.shutterstockloader.communication.RESTService;
import com.example.herbster.shutterstockloader.communication.ServiceRequest;
import com.example.herbster.shutterstockloader.communication.ServiceResponse;
import com.example.herbster.shutterstockloader.json.ShutterStockJSONParser;
import com.example.herbster.shutterstockloader.model.ShutterStockQueryResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by herbster on 1/28/2016.
 */
public class PerformQueryTask extends AsyncTask<String, Void, ShutterStockQueryResponse> {

    public static final String PARAM_PAGE = "page";
    public static final String PARAM_PER_PAGE = "per_page";
    public static final String PARAM_QUERY = "query";
    public static final String PARAM_VIEW = "view";
    private List<ShutterStockQueryListener> mQueryListenersList;

    public PerformQueryTask() {
        mQueryListenersList = new ArrayList<ShutterStockQueryListener>();
    }

    public boolean addQueryListener(ShutterStockQueryListener l) {
        if (!mQueryListenersList.contains(l))
            return mQueryListenersList.add(l);
        return false;
    }

    public boolean removeQueryListener(ShutterStockQueryListener l) {
        return mQueryListenersList.remove(l);
    }

    @Override
    protected ShutterStockQueryResponse doInBackground(String... params) {
        if (params == null || params.length == 0)
            return null;

        ShutterStockAppProperties properties = ShutterStockAppProperties.getInstance();
        if (properties == null)
            return null;

        String scheme = properties.getShutterStockQueryScheme();
        String authority = properties.getShutterStockQueryAuthority();
        String paths[] = properties.getShutterStockQueryPaths();

        RESTService service = RESTService.create(scheme, authority, paths);
        if (service != null) {
            ServiceRequest request = new ServiceRequest();
            request.setAuthBasic(properties.getEncodedCredentials());

            request.addParameter(PARAM_PAGE, Integer.toString(1));
            request.addParameter(PARAM_PER_PAGE, Integer.toString(properties.getNumImagesPerPage()));
            request.addParameter(PARAM_QUERY, params[0]);
            request.addParameter(PARAM_VIEW, "full");

            ServiceResponse response = service.doGet(request);

            if (response != null && !response.isError()) {
                InputStream stream = new ByteArrayInputStream(response.getContent().getBytes());
                ShutterStockQueryResponse queryResponse = ShutterStockJSONParser.getInstance().parseShutterStockJSONResponse(stream);
                return queryResponse;
            }

        }
        return null;
    }

    @Override
    protected void onPostExecute(ShutterStockQueryResponse shutterStockQueryResponse) {
        if (shutterStockQueryResponse == null) {
            for (ShutterStockQueryListener listener : mQueryListenersList)
                listener.onQueryError();
        } else {
            for (ShutterStockQueryListener listener : mQueryListenersList)
                listener.onQueryFinished(shutterStockQueryResponse);
        }
    }
}
