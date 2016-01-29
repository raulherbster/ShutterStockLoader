package com.example.herbster.shutterstockloader.model;

/**
 * Created by herbster on 1/27/2016.
 */
public class ShutterStockImageAsset implements Comparable {

    private String mName;
    private String mDisplayName;
    private int mDPI;
    private int mFileSize;
    private String mFormat;
    private int mHeight;
    private boolean mIsLicensable;
    private int mWidth;
    private String mURL;
    private Object height;

    public ShutterStockImageAsset(String name) {
        this.mName = name;
    }

    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public void setDPI(int DPI) {
        this.mDPI = DPI;
    }

    public void setFileSize(int fileSize) {
        this.mFileSize = fileSize;
    }

    public void setFormat(String format) {
        this.mFormat = format;
    }

    public void setIsLicensable(boolean isLicensable) {
        this.mIsLicensable = isLicensable;
    }

    public String getURL() {
        return mURL;
    }

    public void setURL(String URL) {
        this.mURL = URL;
    }

    @Override
    public int compareTo(Object another) {
        ShutterStockImageAsset anotherAsset = (ShutterStockImageAsset) another;
        return this.mName.compareTo(anotherAsset.mName);
    }

    public String getName() {
        return mName;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

}
