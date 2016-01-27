package com.example.herbster.shutterstockloader.model;

/**
 * Created by herbster on 1/27/2016.
 */
public interface ShutterStockMedia {

    public enum MediaType {
        VIDEO, IMAGE
    }

    public MediaType getMediaType();
}
