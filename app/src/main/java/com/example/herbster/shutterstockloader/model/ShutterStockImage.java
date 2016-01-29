package com.example.herbster.shutterstockloader.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Map<String,ShutterStockImageAsset> mAssetsMap;
    private Set<ShutterStockCategory> mCategories;
    private Set<ShutterStockImageModel> mModels;
    private ShutterStockContributor mContributor;
    private String mDescription;
    private ImageType mImageType;
    private MediaType mMediaType;
    private boolean mIsAdult;
    private boolean mIsIllustration;
    private Set<String> mKeywords;
    private boolean mHasModelRelease;
    private boolean mHasPropertyRelease;
    private boolean mIsEditorial;

    public ShutterStockImage() {
        mAssetsMap = new HashMap<String,ShutterStockImageAsset>();
        mCategories = new TreeSet<ShutterStockCategory>();
        mKeywords = new TreeSet<String>();
        mModels = new TreeSet<ShutterStockImageModel>();
    }

    public void setHasModelRelease(boolean hasModelRelease) {
        this.mHasModelRelease = hasModelRelease;
    }

    public void setHasPropertyRelease(boolean hasPropertyRelease) {
        this.mHasPropertyRelease = hasPropertyRelease;
    }

    public void setModels(List<ShutterStockImageModel> models) {
        this.mModels.addAll(models);
    }

    public ShutterStockContributor getContributor() {
        return mContributor;
    }

    public double getAspect() {
        return mAspect;
    }

    public String getDescription() {
        return mDescription;
    }

    public boolean isAdultImage() {
        return mIsAdult;
    }

    public boolean isIllustrationImage() {
        return mIsIllustration;
    }

    public Set<String> getKeywords() {
        return mKeywords;
    }

    @Override
    public MediaType getMediaType() {
        return MediaType.IMAGE;
    }


    public void setId(String id) {
        this.mId = id;
    }

    public String getId() {
        return mId;
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
        for (ShutterStockImageAsset asset : shutterStockImageAssets)
            mAssetsMap.put(asset.getName(),asset);
    }

    public void addCategories(List<ShutterStockCategory> shutterStockCategories) {
        mCategories.addAll(shutterStockCategories);
    }

    public ShutterStockImageAsset getAsset(String name) {
        return mAssetsMap.get(name);
    }


    public void setIsEditorial(boolean isEditorial) {
        this.mIsEditorial = isEditorial;
    }

    public boolean isEditorial() {
        return mIsEditorial;
    }

    @Override
    public int compareTo(Object another) {
        ShutterStockImage anotherImage = (ShutterStockImage)another;
        return this.mId.compareTo(anotherImage.mId);
    }

}
