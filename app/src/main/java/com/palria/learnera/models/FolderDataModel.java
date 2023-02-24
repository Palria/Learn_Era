package com.palria.learnera.models;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;

public class FolderDataModel implements Serializable {

    private String id;
    private String authorId;
    private String libraryId;
    private String tutorialId;
    private String folderName;
    private String dateCreated;
    private long numOfPages;
    private long numOfViews;
    private DocumentSnapshot folderDocumentSnapshot;
    //add more fields if required below and change constructor.

    public FolderDataModel(){}
    /**
     *
     * @param id
     * @param tutorialId
     * @param folderName
     * @param dateCreated
     */
    public FolderDataModel(String id, String authorId,String libraryId,String tutorialId, String folderName, String dateCreated, long numOfPages, long numOfViews) {
        this.id = id;
        this.authorId = authorId;
        this.libraryId = libraryId;
        this.tutorialId = tutorialId;
        this.folderName = folderName;
        this.dateCreated = dateCreated;
        this.numOfPages = numOfPages;
        this.numOfViews = numOfViews;
//        this.folderDocumentSnapshot = folderDocumentSnapshot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public String getTutorialId() {
        return tutorialId;
    }

    public void setTutorialId(String tutorialId) {
        this.tutorialId = tutorialId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(long numOfPages) {
        this.numOfPages = numOfPages;
    }


    public long getNumOfViews() {
        return numOfViews;
    }

    public void setNumOfViews(long numOfViews) {
        this.numOfViews = numOfViews;
    }


    public DocumentSnapshot getFolderDocumentSnapshot() {
        return folderDocumentSnapshot;
    }

    public void setFolderDocumentSnapshot(DocumentSnapshot folderDocumentSnapshot) {
        this.folderDocumentSnapshot = folderDocumentSnapshot;
    }

    @Override
    public String toString() {
        return "FolderDataModel{" +
                "id='" + id + '\'' +
                ", tutorialId='" + tutorialId + '\'' +
                ", folderName='" + folderName + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                '}';
    }
}
