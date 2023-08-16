package com.palria.learnera.models;

import java.io.Serializable;
import java.util.ArrayList;

public class PostDataModel implements Serializable {
    String updateId;
    String title;
    String description;
    String datePosted;
    ArrayList<String> imageUrlList;
//    ArrayList<String> customersViewedList;
    int numOfViews;

    public PostDataModel(
            String updateId,
            String title,
            String description,
            String datePosted,
            ArrayList<String> imageUrlList,
//            ArrayList<String> customersViewedList,
            int numOfViews,
            boolean isPrivate
    ){
        this.updateId=updateId;
        this.title=title;
        this.description=description;
        this.datePosted=datePosted;
        this.imageUrlList=imageUrlList;
//        this.customersViewedList=customersViewedList;
        this.numOfViews=numOfViews;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public ArrayList<String> getImageUrlList() {
        return imageUrlList;
    }

//    public ArrayList<String> getCustomersViewedList() {
//        return customersViewedList;
//    }

    public int getNumOfViews() {
        return numOfViews;
    }

    public String getUpdateId() {
        return updateId;
    }
}
