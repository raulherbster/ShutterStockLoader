package com.example.herbster.shutterstockloader;

import com.example.herbster.shutterstockloader.model.ShutterStockQueryResponse;

/**
 * Created by herbster on 1/28/2016.
 */
public interface ShutterStockQueryListener {

    public void onQueryFinished(ShutterStockQueryResponse response);

    public void onQueryError();

}
