package com.example.herbster.shutterstockloader.model;

/**
 * Created by herbster on 1/27/2016.
 */
public class ShutterStockContributor {

    private String mId;

    public ShutterStockContributor() {
        setId("");
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getId() {
        return mId;
    }
}
