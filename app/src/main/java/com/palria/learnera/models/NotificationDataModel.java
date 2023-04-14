package com.palria.learnera.models;

import java.util.ArrayList;

public class NotificationDataModel {
    String notificationId;
    String title;
    String message;
    String dateNotified;
    ArrayList<String> notificationViewers;

    public NotificationDataModel(String notificationId, String title, String message, String dateNotified, ArrayList<String> notificationViewers){
        this.notificationId = notificationId;
        this.title = title;
        this.message = message;
        this.dateNotified = dateNotified;
        this.notificationViewers = notificationViewers;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getDateNotified() {
        return dateNotified;
    }

    public ArrayList<String> getNotificationViewers() {
        return notificationViewers;
    }
}
