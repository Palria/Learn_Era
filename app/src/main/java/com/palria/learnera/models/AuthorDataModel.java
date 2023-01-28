package com.palria.learnera.models;


/**This class model is necessary when listing out the authors*/
public class AuthorDataModel {
    /**The name of the author*/
    String authorName;
    /**The author's id*/
    String authorId;
    /**The number of libraries this author has*/
    int totalNumberOfLibraries;
    /**The total number of this author's one star rating.
     * Each time a reader rates this author a one star, this variable is incremented
     * in the database.
     * */
    int totalNumberOfOneStarRate;
    /**The total number of this author's two star rating.
     * Each time a reader rates this author's a two star, this variable is incremented
     * in the database.
     * */
    int totalNumberOfTwoStarRate;
    /**The total number of this author's three star rating.
     * Each time a reader rates this author's a three star, this variable is incremented
     * in the database.
     * */
    int totalNumberOfThreeStarRate;
    /**The total number of this author's four star rating.
     * Each time a reader rates this author's a four star, this variable is incremented
     * in the database.
     * */
    int totalNumberOfFourStarRate;
    /**The total number of this author's five star rating.
     * Each time a reader rates this author's a five star, this variable is incremented
     * in the database.
     * */
    int totalNumberOfFiveStarRate;



    /**Empty constructor may be used to instantiate the class object if it is not
     * necessary to instantiate the global variables through the constructor. In this way
     *Setters are used to as an alternative instantiate the global variables .
     * */
    public  AuthorDataModel(){

    }


    /**This parameterized Constructor helps us in initializing all the global variables
     * */
    AuthorDataModel(String authorName,String authorId, int totalNumberOfLibraries, int totalNumberOfOneStarRate, int totalNumberOfTwoStarRate, int totalNumberOfThreeStarRate, int totalNumberOfFourStarRate, int totalNumberOfFiveStarRate){
                    this.authorName = authorName;
                    this.authorId = authorId;
                    this.totalNumberOfLibraries = totalNumberOfLibraries;
                    this.totalNumberOfOneStarRate = totalNumberOfOneStarRate;
                    this.totalNumberOfTwoStarRate = totalNumberOfTwoStarRate;
                    this.totalNumberOfThreeStarRate = totalNumberOfThreeStarRate;
                    this.totalNumberOfFourStarRate = totalNumberOfFourStarRate;
                    this.totalNumberOfFiveStarRate = totalNumberOfFiveStarRate;
                }

    //The getters are for querying  the queried data and setters for setting the data

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public int getTotalNumberOfLibraries() {
        return totalNumberOfLibraries;
    }

    public void setTotalNumberOfLibraries(int totalNumberOfLibraries) {
        this.totalNumberOfLibraries = totalNumberOfLibraries;
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
