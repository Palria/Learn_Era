package com.palria.learnera;











public class TutorialDataModel {

/**The name of the tutorial*/
    String tutorialName;
    /**The id of the book*/
    String tutorialId;
    /**The date when this book was created*/
    String dateCreated;
    /**The number of pages this book contains*/
    int totalNumberOfPages;
    int totalNumberOfFolders;
    /**The number of time a reader viewed this book*/
    int totalNumberOfTutorialViews;
    /**The number of time this book appeared to users*/
    int totalNumberOfTutorialReach;
    /**The name of this book's author*/
    String authorName;
    /**The author's id.
     * This is necessary if the reader wants to know about the author
     * */
    String authorId;
    /**The name of this book's library*/
    String libraryName;
    /**The id of the library
     * This is necessary if the reader wants to know more about the library
     * */
    String libraryId;
    /**The total number of this book's one star rating.
     * Each time a reader rates this book's a one star, this variable is incremented
     * in the database.
     * */
    int totalNumberOfOneStarRate;
    /**The total number of this book's two star rating.
     * Each time a reader rates this book's a two star, this variable is incremented
     * in the database.
     * */
    int totalNumberOfTwoStarRate;
    /**The total number of this book's three star rating.
     * Each time a reader rates this book's a three star, this variable is incremented
     * in the database.
     * */
    int totalNumberOfThreeStarRate;
    /**The total number of this book's four star rating.
     * Each time a reader rates this book's a four star, this variable is incremented
     * in the database.
     * */
    int totalNumberOfFourStarRate;
    /**The total number of this book's five star rating.
     * Each time a reader rates this book's a five star, this variable is incremented
     * in the database.
     * */
    int totalNumberOfFiveStarRate;


    /**Empty constructor may be used to instantiate the class object if it is not
     * necessary to instantiate the global variables through the constructor. In this way
     *Setters are used to as an alternative instantiate the global variables .
     * */
    TutorialDataModel(){

    }


    /**This parameterized Constructor helps us in initializing all the global variables
     * */
    TutorialDataModel(
            String tutorialName,
            String tutorialId,
            String dateCreated,
            int totalNumberOfPages,
            int totalNumberOfFolders,
            int totalNumberOfTutorialViews,
            int totalNumberOfTutorialReach,
            String authorName,
            String authorId,
            String libraryName,
            String libraryId,
            int totalNumberOfOneStarRate,
            int totalNumberOfTwoStarRate,
            int totalNumberOfThreeStarRate,
            int totalNumberOfFourStarRate,
            int totalNumberOfFiveStarRate
    ){
        this.tutorialName = tutorialName;
        this.tutorialId = tutorialId;
        this.dateCreated = dateCreated;
        this.totalNumberOfPages = totalNumberOfPages;
        this.totalNumberOfFolders = totalNumberOfFolders;
        this.totalNumberOfTutorialViews = totalNumberOfTutorialViews;
        this.totalNumberOfTutorialReach = totalNumberOfTutorialReach;
        this.authorName = authorName;
        this.authorId = authorId;
        this.libraryName = libraryName;
        this.libraryId = libraryId;
        this.totalNumberOfOneStarRate = totalNumberOfOneStarRate;
        this.totalNumberOfTwoStarRate = totalNumberOfTwoStarRate;
        this.totalNumberOfThreeStarRate = totalNumberOfThreeStarRate;
        this.totalNumberOfFourStarRate = totalNumberOfFourStarRate;
        this.totalNumberOfFiveStarRate = totalNumberOfFiveStarRate;

    }

    //The getters are for querying  the queried data and setters for setting the data

    public String getTutorialName() {
        return tutorialName;
    }

    void setTutorialName(String tutorialName) {
        this.tutorialName = tutorialName;
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

    public int getTotalNumberOfPages() {
        return totalNumberOfPages;
    }

    void setTotalNumberOfPages(int totalNumberOfPages) {
        this.totalNumberOfPages = totalNumberOfPages;
    }

    public int getTotalNumberOfFolders() {
        return totalNumberOfFolders;
    }

    void setTotalNumberOfFolders(int totalNumberOfFolders) {
        this.totalNumberOfFolders = totalNumberOfFolders;
    }

    public int getTotalNumberOfTutorialViews() {
        return totalNumberOfTutorialViews;
    }

    void setTotalNumberOfTutorialViews(int totalNumberOfTutorialViews) {
        this.totalNumberOfTutorialViews = totalNumberOfTutorialViews;
    }

    public int getTotalNumberOfTutorialReach() {
        return totalNumberOfTutorialReach;
    }

    void setTotalNumberOfBookReach(int totalNumberOfTutorialReach) {
        this.totalNumberOfTutorialReach = totalNumberOfTutorialReach;
    }

    public String getAuthorName() {
        return authorName;
    }

    void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorId() {
        return authorId;
    }

    void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
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

    public int getTotalNumberOfOneStarRate() {
        return totalNumberOfOneStarRate;
    }

    void setTotalNumberOfOneStarRate(int totalNumberOfOneStarRate) {
        this.totalNumberOfOneStarRate = totalNumberOfOneStarRate;
    }

    public int getTotalNumberOfTwoStarRate() {
        return totalNumberOfTwoStarRate;
    }

    void setTotalNumberOfTwoStarRate(int totalNumberOfTwoStarRate) {
        this.totalNumberOfTwoStarRate = totalNumberOfTwoStarRate;
    }

    public int getTotalNumberOfThreeStarRate() {
        return totalNumberOfThreeStarRate;
    }

    void setTotalNumberOfThreeStarRate(int totalNumberOfThreeStarRate) {
        this.totalNumberOfThreeStarRate = totalNumberOfThreeStarRate;
    }

    public int getTotalNumberOfFourStarRate() {
        return totalNumberOfFourStarRate;
    }

    void setTotalNumberOfFourStarRate(int totalNumberOfFourStarRate) {
        this.totalNumberOfFourStarRate = totalNumberOfFourStarRate;
    }

    public int getTotalNumberOfFiveStarRate() {
        return totalNumberOfFiveStarRate;
    }

    void setTotalNumberOfFiveStarRate(int totalNumberOfFiveStarRate) {
        this.totalNumberOfFiveStarRate = totalNumberOfFiveStarRate;
    }
}
