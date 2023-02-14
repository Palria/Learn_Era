package com.palria.learnera.models;

public class FolderDataModel {

    private String id;
    private String tutorialId;
    private String folderName;
    private String dateCreated;
    private long numOfPages;
    //add more fields if required below and change constructor.

    /**
     *
     * @param id
     * @param tutorialId
     * @param folderName
     * @param dateCreated
     */

    public FolderDataModel(String id, String tutorialId, String folderName, String dateCreated, long numOfPages) {
        this.id = id;
        this.tutorialId = tutorialId;
        this.folderName = folderName;
        this.dateCreated = dateCreated;
        this.numOfPages = numOfPages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
