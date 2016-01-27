package com.example.herbster.shutterstockloader.json;

import com.example.herbster.shutterstockloader.model.ShutterStockQueryResponse;

import java.io.InputStream;

/**
 * Created by herbster on 1/27/2016.
 */
public class ShutterStockJSONParser {

    private static ShutterStockJSONParser singleton = null;

    private ShutterStockJSONParser() {

    }

    public synchronized static ShutterStockJSONParser getSingleton() {
        if (singleton == null)
            singleton = new ShutterStockJSONParser();
        return singleton;
    }

    public ShutterStockQueryResponse parserShutterStockJSONResponse(InputStream in) {
        return new ShutterStockQueryResponse();
    }
}
