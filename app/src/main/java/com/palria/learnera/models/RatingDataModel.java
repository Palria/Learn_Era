package com.palria.learnera.models;

public class RatingDataModel {

    private String userId;
    private String userName;

    /**
     * model id eg, libraryid, tutorialid , userid etc
     */
    private String modelId;
    /**
     * model type eg, user, library, tutorial etc.
     */
    private String modelType;
    /**
     * posted date eg sep 02 2022
     */
    private  String date;
    /**
     * comment - mesage or rating body posted by the user
     * eg. i love this app.
     */
    private  String message;
    /**
     * rating provided by the user
     * 1,2,3,4,5 any eg 1 = 1 star by user
     */
    private int rating;
    /**
     * rating poster user profile doenlaod url or
     * get data from userid
     */
    private String userProfileDownlaodUrl;

    /**
     * add other methodsa and fields here
     * if required any.
     */

    public RatingDataModel(String userId, String userName, String modelId, String modelType, String date, String message, int rating, String userProfileDownlaodUrl) {
        this.userId = userId;
        this.userName = userName;
        this.modelId = modelId;
        this.modelType = modelType;
        this.date = date;
        this.message = message;
        this.rating = rating;
        this.userProfileDownlaodUrl = userProfileDownlaodUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUserProfileDownlaodUrl() {
        return userProfileDownlaodUrl;
    }

    public void setUserProfileDownlaodUrl(String userProfileDownlaodUrl) {
        this.userProfileDownlaodUrl = userProfileDownlaodUrl;
    }
}
