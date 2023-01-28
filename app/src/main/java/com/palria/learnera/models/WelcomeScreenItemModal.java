package com.palria.learnera.models;

public class WelcomeScreenItemModal {

    private int imageId;
    private String title;
    private String subTitle;


    public WelcomeScreenItemModal(int imageId, String title, String subTitle) {
        this.imageId = imageId;
        this.title = title;
        this.subTitle = subTitle;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
