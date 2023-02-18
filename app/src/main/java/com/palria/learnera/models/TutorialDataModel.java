package com.palria.learnera.models;


import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;

public class TutorialDataModel implements Serializable {

/**The name of the tutorial*/
    String tutorialName;
    String tutorialCategory;
    String tutorialDescription;
    /**The id of the book*/
    String tutorialId;
    /**The date when this book was created*/
    String dateCreated;
    /**The number of pages this book contains*/
    long totalNumberOfPages;
    long totalNumberOfFolders;
    /**The number of time a reader viewed this book*/
    long totalNumberOfTutorialViews;
    /**The number of time this book appeared to users*/
    long totalNumberOfTutorialReach;
    /**The author's id.
     * This is necessary if the reader wants to know about the author
     * */
    String authorId;
    /**The id of the library
     * This is necessary if the reader wants to know more about the library
     * */
    String libraryId;
    /**The download url  of the tutorial's cover photo
     * */
    String tutorialCoverPhotoDownloadUrl;
    /**The total number of this book's one star rating.
     * Each time a reader rates this book's a one star, this variable is incremented
     * in the database.
     * */
    long totalNumberOfOneStarRate;
    /**The total number of this book's two star rating.
     * Each time a reader rates this book's a two star, this variable is incremented
     * in the database.
     * */
    long totalNumberOfTwoStarRate;
    /**The total number of this book's three star rating.
     * Each time a reader rates this book's a three star, this variable is incremented
     * in the database.
     * */
    long totalNumberOfThreeStarRate;
    /**The total number of this book's four star rating.
     * Each time a reader rates this book's a four star, this variable is incremented
     * in the database.
     * */
    long totalNumberOfFourStarRate;
    /**The total number of this book's five star rating.
     * Each time a reader rates this book's a five star, this variable is incremented
     * in the database.
     * */
    long totalNumberOfFiveStarRate;

    private DocumentSnapshot tutorialDocumentSnapshot;

    /**Empty constructor may be used to instantiate the class object if it is not
     * necessary to instantiate the global variables through the constructor. In this way
     *Setters are used to as an alternative instantiate the global variables .
     * */
//    public    TutorialDataModel(){
//
//    }


    /**This parameterized Constructor helps us in initializing all the global variables
     * */
   public  TutorialDataModel(
            String tutorialName,
            String tutorialCategory,
            String tutorialDescription,
            String tutorialId,
            String dateCreated,
            long totalNumberOfPages,
            long totalNumberOfFolders,
            long totalNumberOfTutorialViews,
            long totalNumberOfTutorialReach,
            String authorId,
            String libraryId,
            String tutorialCoverPhotoDownloadUrl,
            long totalNumberOfOneStarRate,
            long totalNumberOfTwoStarRate,
            long totalNumberOfThreeStarRate,
            long totalNumberOfFourStarRate,
            long totalNumberOfFiveStarRate
//            DocumentSnapshot tutorialDocumentSnapshot
    ){
        this.tutorialName = tutorialName;
        this.tutorialCategory = tutorialCategory;
        this.tutorialDescription = tutorialDescription;
        this.tutorialId = tutorialId;
        this.dateCreated = dateCreated;
        this.totalNumberOfPages = totalNumberOfPages;
        this.totalNumberOfFolders = totalNumberOfFolders;
        this.totalNumberOfTutorialViews = totalNumberOfTutorialViews;
        this.totalNumberOfTutorialReach = totalNumberOfTutorialReach;
        this.authorId = authorId;
        this.libraryId = libraryId;
        this.tutorialCoverPhotoDownloadUrl = tutorialCoverPhotoDownloadUrl;
        this.totalNumberOfOneStarRate = totalNumberOfOneStarRate;
        this.totalNumberOfTwoStarRate = totalNumberOfTwoStarRate;
        this.totalNumberOfThreeStarRate = totalNumberOfThreeStarRate;
        this.totalNumberOfFourStarRate = totalNumberOfFourStarRate;
        this.totalNumberOfFiveStarRate = totalNumberOfFiveStarRate;
//        this.tutorialDocumentSnapshot = tutorialDocumentSnapshot;

    }

    //The getters are for querying  the queried data and setters for setting the data

    public String getTutorialName() {
        return tutorialName;
    }

    void setTutorialName(String tutorialName) {
        this.tutorialName = tutorialName;
    }

    public String getTutorialCategory() {
        return tutorialCategory;
    }

    void setTutorialCategory(String tutorialCategory) {
        this.tutorialCategory = tutorialCategory;
    }

    public String getTutorialDescription() {
        return tutorialDescription;
    }

    void setTutorialDescription(String tutorialDescription) {
        this.tutorialDescription = tutorialDescription;
    }

      public String getTutorialId() {
        return tutorialId;
    }

    void setTutorialId(String tutorialId) {
        this.tutorialId = tutorialId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getTotalNumberOfPages() {
        return totalNumberOfPages;
    }

    void setTotalNumberOfPages(int totalNumberOfPages) {
        this.totalNumberOfPages = totalNumberOfPages;
    }

    public long getTotalNumberOfFolders() {
        return totalNumberOfFolders;
    }

    void setTotalNumberOfFolders(int totalNumberOfFolders) {
        this.totalNumberOfFolders = totalNumberOfFolders;
    }

    public long getTotalNumberOfTutorialViews() {
        return totalNumberOfTutorialViews;
    }

    void setTotalNumberOfTutorialViews(int totalNumberOfTutorialViews) {
        this.totalNumberOfTutorialViews = totalNumberOfTutorialViews;
    }

    public long getTotalNumberOfTutorialReach() {
        return totalNumberOfTutorialReach;
    }

    void setTotalNumberOfBookReach(int totalNumberOfTutorialReach) {
        this.totalNumberOfTutorialReach = totalNumberOfTutorialReach;
    }


    public String getAuthorId() {
        return authorId;
    }

    void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getLibraryId() {
        return libraryId;
    }

    void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public String getTutorialCoverPhotoDownloadUrl() {
        return tutorialCoverPhotoDownloadUrl;
    }

    void setTutorialCoverPhotoDownloadUrl(String tutorialCoverPhotoDownloadUrl) {
        this.tutorialCoverPhotoDownloadUrl = tutorialCoverPhotoDownloadUrl;
    }

    public long getTotalNumberOfOneStarRate() {
        return totalNumberOfOneStarRate;
    }

    void setTotalNumberOfOneStarRate(int totalNumberOfOneStarRate) {
        this.totalNumberOfOneStarRate = totalNumberOfOneStarRate;
    }

    public long getTotalNumberOfTwoStarRate() {
        return totalNumberOfTwoStarRate;
    }

    void setTotalNumberOfTwoStarRate(int totalNumberOfTwoStarRate) {
        this.totalNumberOfTwoStarRate = totalNumberOfTwoStarRate;
    }

    public long getTotalNumberOfThreeStarRate() {
        return totalNumberOfThreeStarRate;
    }

    void setTotalNumberOfThreeStarRate(int totalNumberOfThreeStarRate) {
        this.totalNumberOfThreeStarRate = totalNumberOfThreeStarRate;
    }

    public long getTotalNumberOfFourStarRate() {
        return totalNumberOfFourStarRate;
    }

    void setTotalNumberOfFourStarRate(int totalNumberOfFourStarRate) {
        this.totalNumberOfFourStarRate = totalNumberOfFourStarRate;
    }

    public long getTotalNumberOfFiveStarRate() {
        return totalNumberOfFiveStarRate;
    }

    void setTotalNumberOfFiveStarRate(int totalNumberOfFiveStarRate) {
        this.totalNumberOfFiveStarRate = totalNumberOfFiveStarRate;
    }

    public DocumentSnapshot getTutorialDocumentSnapshot() {
        return tutorialDocumentSnapshot;
    }

    void setTutorialDocumentSnapshot(DocumentSnapshot tutorialDocumentSnapshot) {
        this.tutorialDocumentSnapshot = tutorialDocumentSnapshot;
    }
}
