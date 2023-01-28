package com.palria.learnera;










/**
 *
 *
 * The model of a library*/
public class LibraryDataModel {

/**The library name*/
    String libraryName;
/**The library Id*/
    String libraryId;
/**The library category*/
    String libraryCategory;
    /**The date at which the library was created*/
    String dateCreated;
    /**The number of books this library contains*/
    long totalNumberOfTutorials;
    /**The numbers of time users viewed this library.
     * Each time a user views this library , this field is incremented is  the database
     * */
    long totalNumberOfLibraryViews;
    /**The numbers of time this library appeared to users either in a list or somewhere else
     * Each time this library appears to a user, this field is incremented is  the database
     * */
    long totalNumberOfLibraryReach;
    /**The name of the user who created this library*/
//    String authorName;
    /**The ID of the author
     * This ID helps in case the user who is interacting with the library wants to know about the author
     * */
    String authorUserId;
    /**The number of one star this library has
     * This variable is incremented each time a user rates the library one star
     * */
    long totalNumberOfOneStarRate;
    /**The number of two star this library has
     * This variable is incremented each time a user rates the library two star
     * */
    long totalNumberOfTwoStarRate;
    /**The number of three star this library has
     * This variable is incremented each time a user rates the library three star
     * */
    long totalNumberOfThreeStarRate;
    /**The number of four star this library has
     * This variable is incremented each time a user rates the library four star
     * */
    long totalNumberOfFourStarRate;
    /**The number of five star this library has
     * This variable is incremented each time a user rates the library five star
     * */
    long totalNumberOfFiveStarRate;

    /**Empty constructor may be used to instantiate the class object if it is not
     * necessary to instantiate the global variables through the constructor. In this way
     *Setters are used to as an alternative instantiate the global variables .
     * */
    LibraryDataModel(){

    }

    /**This parameterized Constructor helps us in initializing all the global variables
     * */
    LibraryDataModel(
            String libraryName,
            String libraryId,
            String libraryCategory,
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
    ){
        this.libraryName = libraryName;
        this.libraryId = libraryId;
        this.libraryCategory = libraryCategory;
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

    }

    //The getters are for querying  the queried data and setters for setting the data

    public String getLibraryName() {
        return libraryName;
    }

     void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }
    public String getLibraryId() {
        return libraryId;
    }

     void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }
    public String getLibraryCategory() {
        return libraryCategory;
    }

     void setLibraryCategory(String libraryCategory) {
        this.libraryCategory = libraryCategory;
    }

    public String getDateCreated() {
        return dateCreated;
    }

     void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getTotalNumberOfTutorials() {
        return totalNumberOfTutorials;
    }

     void setTotalNumberOfTutorials(long totalNumberOfTutorials) {
        this.totalNumberOfTutorials = totalNumberOfTutorials;
    }

    public long getTotalNumberOfLibraryViews() {
        return totalNumberOfLibraryViews;
    }

     void setTotalNumberOfLibraryViews(long totalNumberOfLibraryViews) {
        this.totalNumberOfLibraryViews = totalNumberOfLibraryViews;
    }

    public long getTotalNumberOfLibraryReach() {
        return totalNumberOfLibraryReach;
    }

     void setTotalNumberOfLibraryReach(long totalNumberOfLibraryReach) {
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

     void setAuthorUserId(String authorUserId) {
        this.authorUserId = authorUserId;
    }

    public long getTotalNumberOfOneStarRate() {
        return totalNumberOfOneStarRate;
    }

     void setTotalNumberOfOneStarRate(long totalNumberOfOneStarRate) {
        this.totalNumberOfOneStarRate = totalNumberOfOneStarRate;
    }

    public long getTotalNumberOfTwoStarRate() {
        return totalNumberOfTwoStarRate;
    }

     void setTotalNumberOfTwoStarRate(long totalNumberOfTwoStarRate) {
        this.totalNumberOfTwoStarRate = totalNumberOfTwoStarRate;
    }

    public long getTotalNumberOfThreeStarRate() {
        return totalNumberOfThreeStarRate;
    }

     void setTotalNumberOfThreeStarRate(long totalNumberOfThreeStarRate) {
        this.totalNumberOfThreeStarRate = totalNumberOfThreeStarRate;
    }

    public long getTotalNumberOfFourStarRate() {
        return totalNumberOfFourStarRate;
    }

     void setTotalNumberOfFourStarRate(long totalNumberOfFourStarRate) {
        this.totalNumberOfFourStarRate = totalNumberOfFourStarRate;
    }

    public long getTotalNumberOfFiveStarRate() {
        return totalNumberOfFiveStarRate;
    }

     void setTotalNumberOfFiveStarRate(long totalNumberOfFiveStarRate) {
        this.totalNumberOfFiveStarRate = totalNumberOfFiveStarRate;
    }
}
