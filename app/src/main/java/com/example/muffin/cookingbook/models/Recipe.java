package com.example.muffin.cookingbook.models;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Recipe extends RealmObject{

    private int mId;
    private String mTitle;
    private String mType;
    private int mCookingTime;
    private boolean isFavorite;
    private String mImageFilePath;
    private RealmList<RealmString> mIngredients;
    private RealmList<RealmString> mSteps;

    public Recipe(){
    }

    public Recipe(String title,int cookingTime, boolean isFavorite, RealmList<RealmString> ingredients, RealmList<RealmString> steps) {
        mTitle = title;
        mCookingTime = cookingTime;
        this.isFavorite = isFavorite;
        mIngredients = ingredients;
        mSteps = steps;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getImageFilePath() {
        return mImageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        mImageFilePath = imageFilePath;
    }

    public String getType() {
        return mType;
    }

    public void setSteps(RealmList<RealmString>steps) {
        mSteps = steps;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getCookingTime() {
        return mCookingTime;
    }

    public void setCookingTime(int cookingTime) {
        mCookingTime = cookingTime;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public RealmList<RealmString> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(RealmList<RealmString> ingredients) {
        mIngredients = ingredients;
    }

    public RealmList<RealmString> getSteps() {
        return mSteps;
    }

}
