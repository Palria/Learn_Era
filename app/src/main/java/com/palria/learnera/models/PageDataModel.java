package com.palria.learnera.models;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;

public class PageDataModel implements Serializable {

    private String title;
    private String description;
    private String coverDownloadUrl;
    private String authorId;
    private String folderId;

    private String dateCreated;
    //add other fields below
    private DocumentSnapshot pageDocumentSnapshot;

    public PageDataModel(String title, String description, String coverDownloadUrl, String authorId, String folderId, String dateCreated) {
        this.title = title;
        this.description = description;
        this.coverDownloadUrl = coverDownloadUrl;
        this.authorId = authorId;
        this.folderId = folderId;
        this.dateCreated = dateCreated;
        this.pageDocumentSnapshot = pageDocumentSnapshot;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverDownloadUrl() {
        return coverDownloadUrl;
    }

    public void setCoverDownloadUrl(String coverDownloadUrl) {
        this.coverDownloadUrl = coverDownloadUrl;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public DocumentSnapshot getPageDocumentSnapshot() {
        return pageDocumentSnapshot;
    }

    public void setDateCreated(DocumentSnapshot pageDocumentSnapshot) {
        this.dateCreated = dateCreated;
    }
}
