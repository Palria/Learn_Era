package com.palria.learnera.models;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 *
 * The model of a library*/
public class LibraryDataModel implements Serializable {

/**The library name*/
    private  String libraryName;
/**The library Id*/
    private   String libraryId;
/**The library category list*/
    private   ArrayList<String> libraryCategoryArrayList;
    /**Cover photo download url from database*/
    private   String libraryCoverPhotoDownloadUrl;
    /**The date at which the library was created*/
    private   String dateCreated;
    private   String libraryDescription;
    /**The number of books this library contains*/
    private   long totalNumberOfTutorials;
    /**The numbers of time users viewed this library.
     * Each time a user views this library , this field is incremented is  the database
     * */
    private   long totalNumberOfLibraryViews;
    /**The numbers of time this library appeared to users either in a list or somewhere else
     * Each time this library appears to a user, this field is incremented is  the database
     * */
    private   long totalNumberOfLibraryReach;
    /**The name of the user who created this library*/
//    String authorName;
    /**The ID of the author
     * This ID helps in case the user who is interacting with the library wants to know about the author
     * */
    private   String authorUserId;
    /**The number of one star this library has
     * This variable is incremented each time a user rates the library one star
     * */
    private   long totalNumberOfOneStarRate;
    /**The number of two star this library has
     * This variable is incremented each time a user rates the library two star
     * */
    private   long totalNumberOfTwoStarRate;
    /**The number of three star this library has
     * This variable is incremented each time a user rates the library three star
     * */
    private   long totalNumberOfThreeStarRate;
    /**The number of four star this library has
     * This variable is incremented each time a user rates the library four star
     * */
    private   long totalNumberOfFourStarRate;
    /**The number of five star this library has
     * This variable is incremented each time a user rates the library five star
     * */
    private   long totalNumberOfFiveStarRate;

    private DocumentSnapshot libraryDocumentSnapshot;
    /**Empty constructor may be used to instantiate the class object if it is not
     * necessary to instantiate the global variables through the constructor. In this way
     *Setters are used to as an alternative instantiate the global variables .
     * */
    public  LibraryDataModel(){

    }

    /**This parameterized Constructor helps us in initializing all the global variables
     * */
   public LibraryDataModel(
            String libraryName,
            String libraryId,
            ArrayList<String> libraryCategoryArrayList,
            String libraryCoverPhotoDownloadUrl,
            String libraryDescription,
            String dateCreated,
            long totalNumberOfTutorials,
            long totalNumberOfLibraryViews,
            long totalNumberOfLibraryReach,
//            String authorName,
            String authorUserId,
            long totalNumberOfOneStarRate,
            long totalNumberOfTwoStarRate,
            long totalNumberOfThreeStarRate,
            long totalNumberOfFourStarRate,
            long totalNumberOfFiveStarRate
//            DocumentSnapshot libraryDocumentSnapshot
    ){
        this.libraryName = libraryName;
        this.libraryId = libraryId;
        this.libraryCategoryArrayList = libraryCategoryArrayList;
        this.libraryCoverPhotoDownloadUrl = libraryCoverPhotoDownloadUrl;
        this.libraryDescription = libraryDescription;
        this.dateCreated = dateCreated;
        this.totalNumberOfTutorials = totalNumberOfTutorials;
        this.totalNumberOfLibraryViews = totalNumberOfLibraryViews;
        this.totalNumberOfLibraryReach = totalNumberOfLibraryReach;
//        this.authorName = authorName;
        this.authorUserId = authorUserId;
        this.totalNumberOfOneStarRate = totalNumberOfOneStarRate;
        this.totalNumberOfTwoStarRate = totalNumberOfTwoStarRate;
        this.totalNumberOfThreeStarRate = totalNumberOfThreeStarRate;
        this.totalNumberOfFourStarRate = totalNumberOfFourStarRate;
        this.totalNumberOfFiveStarRate = totalNumberOfFiveStarRate;
//        this.libraryDocumentSnapshot = libraryDocumentSnapshot;

    }

    //The getters are for querying  the queried data and setters for setting the data

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }
    public String getLibraryId() {
        return libraryId;
    }

    public  void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }
    public ArrayList<String> getLibraryCategoryArrayList() {
        return libraryCategoryArrayList;
    }

    public  void setLibraryCategoryArrayList(ArrayList<String> libraryCategoryArrayList) {
        this.libraryCategoryArrayList = libraryCategoryArrayList;
    }

    public String getLibraryCoverPhotoDownloadUrl() {
        return libraryCoverPhotoDownloadUrl;
    }

    public  void setLibraryCoverPhotoDownloadUrl(String libraryCoverPhotoDownloadUrl) {
        this.libraryCoverPhotoDownloadUrl = libraryCoverPhotoDownloadUrl;
    }

    public String getLibraryDescription() {
        return libraryDescription;
    }

    public  void setLibraryDescription(String libraryDescription) {
        this.libraryDescription = libraryDescription;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public  void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getTotalNumberOfTutorials() {
        return totalNumberOfTutorials;
    }

    public   void setTotalNumberOfTutorials(long totalNumberOfTutorials) {
        this.totalNumberOfTutorials = totalNumberOfTutorials;
    }

    public long getTotalNumberOfLibraryViews() {
        return totalNumberOfLibraryViews;
    }

    public  void setTotalNumberOfLibraryViews(long totalNumberOfLibraryViews) {
        this.totalNumberOfLibraryViews = totalNumberOfLibraryViews;
    }

    public long getTotalNumberOfLibraryReach() {
        return totalNumberOfLibraryReach;
    }

    public  void setTotalNumberOfLibraryReach(long totalNumberOfLibraryReach) {
        this.totalNumberOfLibraryReach = totalNumberOfLibraryReach;
    }
    /*

    public String getAuthorName() {
        return authorName;
    }

     void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
*/
    public String getAuthorUserId() {
        return authorUserId;
    }

    public  void setAuthorUserId(String authorUserId) {
        this.authorUserId = authorUserId;
    }

    public long getTotalNumberOfOneStarRate() {
        return totalNumberOfOneStarRate;
    }

    public  void setTotalNumberOfOneStarRate(long totalNumberOfOneStarRate) {
        this.totalNumberOfOneStarRate = totalNumberOfOneStarRate;
    }

    public long getTotalNumberOfTwoStarRate() {
        return totalNumberOfTwoStarRate;
    }

    public  void setTotalNumberOfTwoStarRate(long totalNumberOfTwoStarRate) {
        this.totalNumberOfTwoStarRate = totalNumberOfTwoStarRate;
    }

    public long getTotalNumberOfThreeStarRate() {
        return totalNumberOfThreeStarRate;
    }

    public  void setTotalNumberOfThreeStarRate(long totalNumberOfThreeStarRate) {
        this.totalNumberOfThreeStarRate = totalNumberOfThreeStarRate;
    }

    public long getTotalNumberOfFourStarRate() {
        return totalNumberOfFourStarRate;
    }

    public  void setTotalNumberOfFourStarRate(long totalNumberOfFourStarRate) {
        this.totalNumberOfFourStarRate = totalNumberOfFourStarRate;
    }

    public long getTotalNumberOfFiveStarRate() {
        return totalNumberOfFiveStarRate;
    }

    public  void setTotalNumberOfFiveStarRate(long totalNumberOfFiveStarRate) {
        this.totalNumberOfFiveStarRate = totalNumberOfFiveStarRate;
    }

    public DocumentSnapshot getLibraryDocumentSnapshot() {
        return libraryDocumentSnapshot;
    }

    public  void setLibraryDocumentSnapshot(DocumentSnapshot libraryDocumentSnapshot) {
        this.libraryDocumentSnapshot = libraryDocumentSnapshot;
    }
}
