package com.palria.learnera.models;


import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;

/**This class is the user's data model*/
public class UsersDataModel implements Serializable {
    /**The username of the user*/
    String userName;
    /**The user Id of the user*/
    String userId;
    /**
     * The url that downloads the user's profile image/avatar
     * */
    String userProfileImageDownloadUrl;
    /**The number of libraries this user has created*/
    int totalNumberOfLibrariesCreated;
    /**This is a flag that tells if a user is author or an ordinary user in the platform
     * A user becomes an author if he creates a library, If the user creates a library, the flag "IS_AUTHOR" is set to true
     * in the database
     * */
    boolean isAnAuthor;
    /**The user's gender/sex*/
    String gender;
    /**The date when the user created his profile */
    String dateRegistered;
    /**The user's mobile number*/
    String userPhoneNumber;
    /**The user's email address*/
    String userEmail;
    /**The user's country of origin*/
    String userCountryOfOrigin;

    /**The total number of this user's one star rating.
     * Each time a reader rates this user a one star, this variable is incremented
     * in the database.
     * */
    int totalNumberOfOneStarRate;
    /**The total number of this user's two star rating.
     * Each time a reader rates this user a two star, this variable is incremented
     * in the database.
     * */
    int totalNumberOfTwoStarRate;
    /**The total number of this user's three star rating.
     * Each time a reader rates this user a three star, this variable is incremented
     * in the database.
     * */
    int totalNumberOfThreeStarRate;
    /**The total number of this user's four star rating.
     * Each time a reader rates this user a four star, this variable is incremented
     * in the database.
     * */
    int totalNumberOfFourStarRate;
    /**The total number of this user's five star rating.
     * Each time a reader rates this user a five star, this variable is incremented
     * in the database.
     * */
    int totalNumberOfFiveStarRate;

    private DocumentSnapshot userDocumentSnapshot;



    /**Empty constructor may be used to instantiate the class object if it is not
     * necessary to instantiate the global variables through the constructor. In this way
     *Setters are used to as an alternative instantiate the global variables .
     * */
    public  UsersDataModel(){

    }
    /**This parameterized Constructor helps us in initializing all the global variables
     * */
    public  UsersDataModel(
            String userName,
            String userId,
            String userProfileImageDownloadUrl,
            int totalNumberOfLibrariesCreated,
            boolean isAnAuthor,
            String gender,
            String dateRegistered,
            String userPhoneNumber,
            String userEmail,
            String userCountryOfOrigin,
            int totalNumberOfOneStarRate,
            int totalNumberOfTwoStarRate,
            int totalNumberOfThreeStarRate,
            int totalNumberOfFourStarRate,
            int totalNumberOfFiveStarRate
//            DocumentSnapshot userDocumentSnapshot


    ){

        this.userName = userName;
        this.userId = userId;
        this.userProfileImageDownloadUrl = userProfileImageDownloadUrl;
        this.totalNumberOfLibrariesCreated = totalNumberOfLibrariesCreated;
        this.isAnAuthor = isAnAuthor;
        this.gender = gender;
        this.dateRegistered = dateRegistered;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.userCountryOfOrigin = userCountryOfOrigin;
        this.totalNumberOfOneStarRate = totalNumberOfOneStarRate;
        this.totalNumberOfTwoStarRate = totalNumberOfTwoStarRate;
        this.totalNumberOfThreeStarRate = totalNumberOfThreeStarRate;
        this.totalNumberOfFourStarRate = totalNumberOfFourStarRate;
        this.totalNumberOfFiveStarRate = totalNumberOfFiveStarRate;
        this.userDocumentSnapshot = userDocumentSnapshot;

    }


    //The getters are for querying  the queried data and setters for setting the data

    public String getUserName(){
        return userName;
    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId(){
        return userId;
    }

    void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserProfileImageDownloadUrl() {
        return userProfileImageDownloadUrl;
    }

    void setUserProfileImageDownloadUrl(String userProfileImageDownloadUrl) {
        this.userProfileImageDownloadUrl = userProfileImageDownloadUrl;
    }

    public int getTotalNumberOfLibrariesCreated() {
        return totalNumberOfLibrariesCreated;
    }

    void setTotalNumberOfLibrariesCreated(int totalNumberOfLibrariesCreated) {
        this.totalNumberOfLibrariesCreated = totalNumberOfLibrariesCreated;
    }

    public boolean isAnAuthor() {
        return isAnAuthor;
    }
    void setIsAnAuthor(boolean isAnAuthor) {
        this.isAnAuthor = isAnAuthor;
    }


    public String getGender() {
        return gender;
    }

    void setGender(String gender) {
        this.gender = gender;
    }


    public String getDateRegistered() {
        return dateRegistered;
    }

    void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public String getUserCountryOfOrigin() {
        return userCountryOfOrigin;
    }

    void setUserCountryOfOrigin(String userCountryOfOrigin) {
        this.userCountryOfOrigin = userCountryOfOrigin;
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


    public DocumentSnapshot getUserDocumentSnapshot() {
        return userDocumentSnapshot;
    }

    void setUserDocumentSnapshot(DocumentSnapshot userDocumentSnapshot) {
        this.userDocumentSnapshot = userDocumentSnapshot;
    }

}
