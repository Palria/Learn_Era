package com.palria.learnera.models;

import java.io.Serializable;
import java.util.ArrayList;















public class QuestionDataModel implements Serializable {
    private String questionId;
    private String questionBody;
    private String photoDownloadUrl;
    private String dateAsked;
    private String authorId;
    private long numOfAnswers;
    private long numOfViews;
    private boolean isPublic = true;
    private boolean isClosed = false;
    private  boolean isPhotoIncluded;

    public QuestionDataModel(
            String questionId,
            String questionBody,
            String photoDownloadUrl,
            String dateAsked,
            String authorId,
            long numOfAnswers,
            long numOfViews,
            boolean isPublic,
            boolean isClosed,
            boolean isPhotoIncluded
    ){
        this.questionId=questionId;
        this.questionBody=questionBody;
        this.photoDownloadUrl=photoDownloadUrl;
        this.dateAsked=dateAsked;
        this.authorId=authorId;
        this.numOfAnswers=numOfAnswers;
        this.numOfViews=numOfViews;
        this.isPublic=isPublic;
        this.isClosed=isClosed;
        this.isPhotoIncluded=isPhotoIncluded;


    }

    public String getQuestionId() {
        return questionId;
    }

    public String getQuestionBody() {
        return questionBody;
    }

    public String getPhotoDownloadUrl() {
        return photoDownloadUrl;
    }

    public String getDateAsked() {
        return dateAsked;
    }

    public String getAuthorId() {
        return authorId;
    }

    public long getNumOfAnswers() {
        return numOfAnswers;
    }

    public long getNumOfViews() {
        return numOfViews;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public boolean isPublic() {
        return isPublic;
    }
    public boolean isPhotoIncluded() {
        return isPhotoIncluded;
    }
}












