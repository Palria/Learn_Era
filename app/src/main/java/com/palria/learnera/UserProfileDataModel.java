package com.palria.learnera;


/**This class is the user's data model*/
public class UserProfileDataModel {
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
/**Tells us whether this user has a library */
boolean isUserHasLibrary;
/**The user's gender/sex*/
String gender;
/**The age of the user*/
int age;
/**The user's date of birth*/
String dateOfBirth;
/**The date when the user created his profile */
String dateRegistered;
/**The last time this user visited the platform*/
String lastSeen;
/**Tells us whether the user completed the creation of his profile*/
boolean isUserProfileCompleted;
/**The user's mobile number*/
String userPhoneNumber;
/**The user's email address*/
String userEmail;
/**The user's first residential address*/
String userResidentialAddress_1;
/**The user's second residential address*/
String userResidentialAddress_2;
/**The user's country of origin*/
String userCountryOfOrigin;
/**The user's country of residence*/
String userCountryOfResidence;

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


/**Empty constructor may be used to instantiate the class object if it is not
 * necessary to instantiate the global variables through the constructor. In this way
 *Setters are used to as an alternative instantiate the global variables .
 * */
    UserProfileDataModel(){

    }
/**This parameterized Constructor helps us in initializing all the global variables
 * */
    UserProfileDataModel(
            String userName,
            String userId,
            String userProfileImageDownloadUrl,
            int totalNumberOfLibrariesCreated,
            boolean isAnAuthor,
            boolean isUserHasLibrary,
            String gender,
            int age,
            String dateOfBirth,
            String dateRegistered,
            String lastSeen,
            boolean isUserProfileCompleted,
            String userPhoneNumber,
            String userEmail,
            String userResidentialAddress_1,
            String userResidentialAddress_2,
            String userCountryOfOrigin,
            String userCountryOfResidence,
            int totalNumberOfOneStarRate,
            int totalNumberOfTwoStarRate,
            int totalNumberOfThreeStarRate,
            int totalNumberOfFourStarRate,
            int totalNumberOfFiveStarRate


    ){

        this.userName = userName;
        this.userId = userId;
        this.userProfileImageDownloadUrl = userProfileImageDownloadUrl;
        this.totalNumberOfLibrariesCreated = totalNumberOfLibrariesCreated;
        this.isAnAuthor = isAnAuthor;
        this.isUserHasLibrary = isUserHasLibrary;
        this.gender = gender;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.dateRegistered = dateRegistered;
        this.lastSeen = lastSeen;
        this.isUserProfileCompleted = isUserProfileCompleted;

        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.userResidentialAddress_1 = userResidentialAddress_1;
        this.userResidentialAddress_2 = userResidentialAddress_2;
        this.userCountryOfOrigin = userCountryOfOrigin;
        this.userCountryOfResidence = userCountryOfResidence;

        this.totalNumberOfOneStarRate = totalNumberOfOneStarRate;
        this.totalNumberOfTwoStarRate = totalNumberOfTwoStarRate;
        this.totalNumberOfThreeStarRate = totalNumberOfThreeStarRate;
        this.totalNumberOfFourStarRate = totalNumberOfFourStarRate;
        this.totalNumberOfFiveStarRate = totalNumberOfFiveStarRate;

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


    public boolean isUserHasLibrary() {
        return isUserHasLibrary;
    }

     void setIsUserHasLibrary(boolean isUserHasLibrary) {
        this.isUserHasLibrary = isUserHasLibrary;
    }

    public String getGender() {
        return gender;
    }

     void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

     void setAge(int age) {
        this.age = age;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

     void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }

     void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }
    public String getLastSeen() {
        return lastSeen;
    }

     void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public boolean isUserProfileCompleted() {
        return isUserProfileCompleted;
    }

     void setIsUserProfileCompleted(boolean isUserProfileCompleted) {
        this.isUserProfileCompleted = isUserProfileCompleted;
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

    public String getUserResidentialAddress_1() {
        return userResidentialAddress_1;
    }

     void setUserResidentialAddress_1(String userResidentialAddress_1) {
        this.userResidentialAddress_1 = userResidentialAddress_1;
    }

    public String getUserResidentialAddress_2() {
        return userResidentialAddress_2;
    }

     void setUserResidentialAddress_2(String userResidentialAddress_2) {
        this.userResidentialAddress_2 = userResidentialAddress_2;
    }

    public String getUserCountryOfOrigin() {
        return userCountryOfOrigin;
    }

     void setUserCountryOfOrigin(String userCountryOfOrigin) {
        this.userCountryOfOrigin = userCountryOfOrigin;
    }

    public String getUserCountryOfResidence() {
        return userCountryOfResidence;
    }

     void setUserCountryOfResidence(String userCountryOfResidence) {
        this.userCountryOfResidence = userCountryOfResidence;
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
