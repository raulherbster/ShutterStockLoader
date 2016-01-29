package com.example.herbster.shutterstockloader.model;

/**
 * Created by herbster on 1/29/2016.
 */
public class ShutterStockImageModel implements Comparable {

    private String mId;

    public ShutterStockImageModel() {

    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    @Override
    public int compareTo(Object another) {
        ShutterStockImageModel anotherModel = (ShutterStockImageModel)another;
        return this.mId.compareTo(anotherModel.mId);
    }
}
