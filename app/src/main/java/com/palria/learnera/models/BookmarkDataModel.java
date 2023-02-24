package com.palria.learnera.models;

public class BookmarkDataModel {
//This id is not useful because the id of the bookmark is same as the id of the library or tutorial or folder
    private String id;
    /**
     * type eg . library, page, tutorial etc.
     */
    private String type;
    /**
     * id of library, page, tutorial etc.
     */
    private String refrence_id;
    /**
     * title of libbrary tutorial, page etc
     */
    private String title;
    /**
     * description for bookmark
     * like description of library, books etc
     * eg. Hari Bahadur - books and cover album design in photoshop
     * syntax= author - description
     */
    private String description;
    /**
     * icon download url of bookmarked item
     * eg library cover, tutorial cover url etc
     */
    private String iconDownloadUrl;
    /**
     * bookmarked date.
     */
    private String date;

    private String authorId;
    private String libraryId;
    private String tutorialId;
    private String folderId;
    private String pageId;

    public BookmarkDataModel(String type,  String authorId, String libraryId, String tutorialId, String folderId, String pageId, String title, String description, String iconDownloadUrl, String date) {
        this.type = type;
        this.authorId = authorId;
        this.libraryId = libraryId;
        this.tutorialId = tutorialId;
        this.folderId = folderId;
        this.pageId = pageId;
        this.title = title;
        this.description = description;
        this.iconDownloadUrl = iconDownloadUrl;
        this.date = date;
    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
//
    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }
//
    public String getTutorialId() {
        return tutorialId;
    }

    public void setTutorialId(String tutorialId) {
        this.tutorialId = tutorialId;
    }
//
    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }
//
    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }
//
//    public String getRefrence_id() {
//        return refrence_id;
//    }
//
//    public void setRefrence_id(String refrence_id) {
//        this.refrence_id = refrence_id;
//    }

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

    public String getIconDownloadUrl() {
        return iconDownloadUrl;
    }

    public void setIconDownloadUrl(String iconDownloadUrl) {
        this.iconDownloadUrl = iconDownloadUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "BookmarkDataModel{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", refrence_id='" + refrence_id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", iconDownloadUrl='" + iconDownloadUrl + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
