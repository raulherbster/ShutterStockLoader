package com.example.herbster.shutterstockloader.model;

/**
 * Created by herbster on 1/27/2016.
 */
public class ShutterStockCategory implements Comparable {

    private String mId;

    private String mName;

    public ShutterStockCategory() {
        setId("");
        setName("");
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public void setName(String name) {
        this.mName = name;
    }


    @Override
    public int compareTo(Object another) {
        ShutterStockCategory anotherCat = (ShutterStockCategory) another;
        return this.mId.compareTo(anotherCat.mId);
    }
}
