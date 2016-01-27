package com.example.herbster.shutterstockloader.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by herbster on 1/27/2016.
 */
public class ShutterStockImage implements ShutterStockMedia, Comparable {

    public enum ImageType {
        PHOTO, VECTOR
    }

    private String mId;
    private Date mAddedDate;
    private double mAspect;
    private Set<ShutterStockImageAsset> mAssetsList;
    private Set<ShutterStockCategory> mCategories;
    private ShutterStockContributor mContributor;
    private String mDescription;
    private ImageType mImageType;
    private MediaType mMediaType;
    private boolean mIsAdult;
    private boolean mIsIllustration;
    private Set<String> mKeywords;

    public ShutterStockImage() {
        mAssetsList = new TreeSet<ShutterStockImageAsset>();
        mCategories = new TreeSet<ShutterStockCategory>();
        mKeywords = new TreeSet<String>();
    }

    @Override
    public MediaType getMediaType() {
        return MediaType.IMAGE;
    }


    public void setId(String id) {
        this.mId = id;
    }

    public void setAddedDate(Date addedDate) {
        this.mAddedDate = addedDate;
    }

    public void setAspect(double aspect) {
        this.mAspect = aspect;
    }

    public void setContributor(ShutterStockContributor contributor) {
        this.mContributor = contributor;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public void setImageType(String imageType) {
        if (imageType.equals("vector")) {
            this.mImageType = ImageType.VECTOR;
        } else if (imageType.equals("photo")) {
            this.mImageType = ImageType.PHOTO;
        }
    }

    public void setIsAdult(boolean isAdult) {
        this.mIsAdult = isAdult;
    }

    public void setIsIllustration(boolean isIllustration) {
        this.mIsIllustration = isIllustration;
    }

    public void setKeywords(List<String> keywords) {
        this.mKeywords.addAll(keywords);
    }

    public void addAssets(List<ShutterStockImageAsset> shutterStockImageAssets) {
        mAssetsList.addAll(shutterStockImageAssets);
    }

    public void addCategories(List<ShutterStockCategory> shutterStockCategories) {
        mCategories.addAll(shutterStockCategories);
    }

    @Override
    public int compareTo(Object another) {
        ShutterStockImage anotherImage = (ShutterStockImage)another;
        return this.mId.compareTo(anotherImage.mId);
    }

}
