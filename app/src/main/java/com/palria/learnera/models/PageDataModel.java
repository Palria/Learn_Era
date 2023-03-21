package com.palria.learnera.models;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;

public class PageDataModel implements Serializable {

    private String title;
    private String description;
    private String coverDownloadUrl;
    private String authorId;
    private String pageId;
    private String tutorialId;
    private String folderId;
    private long totalViews;
    private boolean  isTutorialPage;

    private String dateCreated;
    //add other fields below
    private DocumentSnapshot pageDocumentSnapshot;
    private int pageNumber;

    public PageDataModel(String title, String description, String coverDownloadUrl, String authorId, String pageId,String tutorialId,String folderId, String dateCreated,long totalViews,boolean isTutorialPage,int pageNumber) {
        this.title = title;
        this.description = description;
        this.coverDownloadUrl = coverDownloadUrl;
        this.authorId = authorId;
        this.pageId = pageId;
        this.tutorialId = tutorialId;
        this.folderId = folderId;
        this.dateCreated = dateCreated;
        this.pageDocumentSnapshot = pageDocumentSnapshot;
        this.totalViews = totalViews;
        this.isTutorialPage = isTutorialPage;
        this.pageNumber = pageNumber;
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

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getTutorialId() {
        return tutorialId;
    }

    public void setTutorialId(String tutorialId) {
        this.tutorialId = tutorialId;
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
    public void setPageViews(long totalViews) {
        this.totalViews = totalViews;
    }
    public long getPageViews() {
      return totalViews;
    }
    public boolean isTutorialPage() {
        return isTutorialPage;
    }

    public void setIsTutorialPage(boolean isTutorialPage) {
        this.isTutorialPage =isTutorialPage;
    }

    public DocumentSnapshot getPageDocumentSnapshot() {
        return pageDocumentSnapshot;
    }

    public void setPageDocumentSnapshot(DocumentSnapshot pageDocumentSnapshot) {
        this.pageDocumentSnapshot = pageDocumentSnapshot;
    }
    public int getPageNumber() {
        return pageNumber;
    }
}
