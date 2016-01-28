package com.example.herbster.shutterstockloader.model;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by herbster on 1/27/2016.
 */
public class ShutterStockQueryResponse {

    private String mId;
    private int mNumElements;
    private Set<ShutterStockImage> mImagesSet;

    public ShutterStockQueryResponse() {
        mImagesSet = new TreeSet<ShutterStockImage>();
        setId("");
        setNumElements(0);
    }

    public boolean merge(ShutterStockQueryResponse newQueryResponse) {
        if (newQueryResponse.mId.equals(this.mId)) {
            return this.mImagesSet.addAll(newQueryResponse.getImages());
        } else {
            return false;
        }
    }

    public String getId() {
        return mId;
    }

    public int getNumElements() {
        return mNumElements;
    }

    public int getNumAddedElements() {
        return mImagesSet.size();
    }

    public void setId(String id) {
        this.mId = id;
    }

    public void setNumElements(int numElements) {
        this.mNumElements = numElements;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof ShutterStockQueryResponse))
            return false;
        ShutterStockQueryResponse response = (ShutterStockQueryResponse)obj;
        return this.getId().equals(response.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public void addImages(List<ShutterStockImage> images) {
        mImagesSet.addAll(images);
    }

    public Set<ShutterStockImage> getImages() {
        return mImagesSet;
    }
}
