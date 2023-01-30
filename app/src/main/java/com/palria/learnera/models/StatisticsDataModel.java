package com.palria.learnera.models;


/**This class is the statistic's data model*/
public class StatisticsDataModel {
    /**The number of libraries this user has created*/
    long totalNumberOfLibrariesCreated;
    long totalNumberOfProfileVisitor;
    long totalNumberOfProfileReach;
    /**This is a flag that tells if a user is author or an ordinary user in the platform
     * A user becomes an author if he creates a library, If the user creates a library, the flag "IS_AUTHOR" is set to true
     * in the database
     * */
    boolean isAnAuthor;
    String lastSeen;

    /**The total number of this user's one star rating.
     * Each time a reader rates this user a one star, this variable is incremented
     * in the database.
     * */
    long totalNumberOfOneStarRate;
    /**The total number of this user's two star rating.
     * Each time a reader rates this user a two star, this variable is incremented
     * in the database.
     * */
    long totalNumberOfTwoStarRate;
    /**The total number of this user's three star rating.
     * Each time a reader rates this user a three star, this variable is incremented
     * in the database.
     * */
    long totalNumberOfThreeStarRate;
    /**The total number of this user's four star rating.
     * Each time a reader rates this user a four star, this variable is incremented
     * in the database.
     * */
    long totalNumberOfFourStarRate;
    /**The total number of this user's five star rating.
     * Each time a reader rates this user a five star, this variable is incremented
     * in the database.
     * */
    long totalNumberOfFiveStarRate;


    /**Empty constructor may be used to instantiate the class object if it is not
     * necessary to instantiate the global variables through the constructor. In this way
     *Setters are used to as an alternative instantiate the global variables .
     * */
//    public  StatisticsDataModel(){
//
//    }
    /**This parameterized Constructor helps us in initializing all the global variables
     * */
    public  StatisticsDataModel(
            long totalNumberOfLibrariesCreated,
            long totalNumberOfProfileVisitor,
            long totalNumberOfProfileReach,
            boolean isAnAuthor,
            String lastSeen,
            long totalNumberOfOneStarRate,
            long totalNumberOfTwoStarRate,
            long totalNumberOfThreeStarRate,
            long totalNumberOfFourStarRate,
            long totalNumberOfFiveStarRate
    ){
        this.totalNumberOfLibrariesCreated = totalNumberOfLibrariesCreated;
        this.totalNumberOfProfileVisitor = totalNumberOfProfileVisitor;
        this.totalNumberOfProfileReach = totalNumberOfProfileReach;
        this.isAnAuthor = isAnAuthor;
        this.lastSeen = lastSeen;
        this.totalNumberOfOneStarRate = totalNumberOfOneStarRate;
        this.totalNumberOfTwoStarRate = totalNumberOfTwoStarRate;
        this.totalNumberOfThreeStarRate = totalNumberOfThreeStarRate;
        this.totalNumberOfFourStarRate = totalNumberOfFourStarRate;
        this.totalNumberOfFiveStarRate = totalNumberOfFiveStarRate;

    }


    //The getters are for querying  the queried data and setters for setting the data


    public long getTotalNumberOfLibrariesCreated() {
        return totalNumberOfLibrariesCreated;
    }

    void setTotalNumberOfLibrariesCreated(int totalNumberOfLibrariesCreated) {
        this.totalNumberOfLibrariesCreated = totalNumberOfLibrariesCreated;
    }

    public long getTotalNumberOfProfileVisitor() {
        return totalNumberOfProfileVisitor;
    }

    void setTotalNumberOfProfileVisitor(int totalNumberOfProfileVisitor) {
        this.totalNumberOfProfileVisitor = totalNumberOfProfileVisitor;
    }

    public long getTotalNumberOfProfileReach() {
        return totalNumberOfProfileReach;
    }

    void setTotalNumberOfProfileReach(int totalNumberOfProfileReach) {
        this.totalNumberOfProfileReach = totalNumberOfProfileReach;
    }

    public boolean isAnAuthor() {
        return isAnAuthor;
    }
    void setIsAnAuthor(boolean isAnAuthor) {
        this.isAnAuthor = isAnAuthor;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
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

}
