package com.palria.learnera;

import com.palria.learnera.models.LearnEraNotificationDataModel;

import java.util.ArrayList;

public class PersonalizedNotificationDataModel  {
    String notificationId;
    String type;
    String senderId;
    String title;
    String message;
    String dateNotified;
    ArrayList<String> notification_model_info_list;
    boolean isSeen;

    public PersonalizedNotificationDataModel(String notificationId, String type, String senderId, String title, String message, String dateNotified, ArrayList<String> notification_model_info_list,boolean isSeen) {
        this.notificationId = notificationId;
        this.type = type;
        this.senderId = senderId;
        this.title = title;
        this.message = message;
        this.dateNotified = dateNotified;
        this.isSeen = isSeen;
        this.notification_model_info_list = notification_model_info_list;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getType() {
        return type;
    }

    public String getSenderId() {
        return senderId;
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

    public boolean isSeen() {
        return isSeen;
    }

    public ArrayList<String> getNotification_model_info_list() {
        return notification_model_info_list;
    }
}
