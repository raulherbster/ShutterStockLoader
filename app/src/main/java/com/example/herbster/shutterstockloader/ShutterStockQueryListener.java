package com.example.herbster.shutterstockloader;

import com.example.herbster.shutterstockloader.model.ShutterStockQueryResponse;

/**
 * Listener interface for objects that need to be notified when a certain query to the ShutterStock
 * server has been finished.
 *
 * Created by herbster on 1/28/2016.
 */
public interface ShutterStockQueryListener {

    /**
     * Notifies when a query to the server has been successfully completed.
     * @param response the response as the result of the query to the server.
     */
    public void onQueryFinished(ShutterStockQueryResponse response);

    /**
     * Notifies when a query to the server fails.
     */
    public void onQueryError();

}
