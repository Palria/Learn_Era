package com.palria.learnera.models;


/**This class is the user's data model*/
public class CurrentUserProfileDataModel {
    /**The username of the user*/
    static  String userName;
    /**The user Id of the user*/
    static  String userId;
    /**
     * The url that downloads the user's profile image/avatar
     * */
    static  String userProfileImageDownloadUrl;
    /**The number of libraries this user has created*/
    static  long  totalNumberOfLibrariesCreated;
    /**This is a flag that tells if a user is author or an ordinary user in the platform
     * A user becomes an author if he creates a library, If the user creates a library, the flag "IS_AUTHOR" is set to true
     * in the database
     * */
    static  boolean isAnAuthor;
    /**Tells us whether this user has a library */
    static boolean isUserHasLibrary;
    /**The user's gender/sex*/
    static String gender;
    /**The age of the user*/
    static  long  age;
    /**The user's date of birth*/
    static  String dateOfBirth;
    /**The date when the user created his profile */
    static  String dateRegistered;
    /**The last time this user visited the platform*/
    static  String lastSeen;
    /**Tells us whether the user completed the creation of his profile*/
    static  boolean isUserProfileCompleted;
    /**The user's mobile number*/
    static  String userPhoneNumber;
    /**The user's email address*/
    static String userEmail;
    /**The user's residential address*/
    static  String userResidentialAddress;
    /**The user's country of residence*/
    static  String userCountryOfResidence;
    static long  totalNumberOfProfileVisitor;
    static long  totalNumberOfProfileReach;
    /**The total number of this user's one star rating.
     * Each time a reader rates this user a one star, this variable is incremented
     * in the database.
     * */
    static  long totalNumberOfOneStarRate;
    /**The total number of this user's two star rating.
     * Each time a reader rates this user a two star, this variable is incremented
     * in the database.
     * */
    static  long  totalNumberOfTwoStarRate;
    /**The total number of this user's three star rating.
     * Each time a reader rates this user a three star, this variable is incremented
     * in the database.
     * */
    static  long  totalNumberOfThreeStarRate;
    /**The total number of this user's four star rating.
     * Each time a reader rates this user a four star, this variable is incremented
     * in the database.
     * */
    static  long  totalNumberOfFourStarRate;
    /**The total number of this user's five star rating.
     * Each time a reader rates this user a five star, this variable is incremented
     * in the database.
     * */
    static  long  totalNumberOfFiveStarRate;


    /**This parameterized Constructor helps us in initializing all the global variables
     * */
    public  CurrentUserProfileDataModel(
            String userName,
            String userId,
            String userProfileImageDownloadUrl,
            long  totalNumberOfLibrariesCreated,
            boolean isAnAuthor,
            boolean isUserHasLibrary,
            String gender,
            long  age,
            String dateOfBirth,
            String dateRegistered,
            String lastSeen,
            boolean isUserProfileCompleted,
            String userPhoneNumber,
            String userEmail,
            String userResidentialAddress,
            String userCountryOfResidence,
            long  totalNumberOfProfileVisitor,
            long  totalNumberOfProfileReach,
            long  totalNumberOfOneStarRate,
            long  totalNumberOfTwoStarRate,
            long  totalNumberOfThreeStarRate,
            long  totalNumberOfFourStarRate,
            long  totalNumberOfFiveStarRate


    ){

        CurrentUserProfileDataModel.userName = userName;
        CurrentUserProfileDataModel.userId = userId;
        CurrentUserProfileDataModel.userProfileImageDownloadUrl = userProfileImageDownloadUrl;
        CurrentUserProfileDataModel.totalNumberOfLibrariesCreated = totalNumberOfLibrariesCreated;
        CurrentUserProfileDataModel.isAnAuthor = isAnAuthor;
        CurrentUserProfileDataModel.isUserHasLibrary = isUserHasLibrary;
        CurrentUserProfileDataModel.gender = gender;
        CurrentUserProfileDataModel.age = age;
        CurrentUserProfileDataModel.dateOfBirth = dateOfBirth;
        CurrentUserProfileDataModel.dateRegistered = dateRegistered;
        CurrentUserProfileDataModel.lastSeen = lastSeen;
        CurrentUserProfileDataModel.isUserProfileCompleted = isUserProfileCompleted;
        CurrentUserProfileDataModel.userPhoneNumber = userPhoneNumber;
        CurrentUserProfileDataModel.userEmail = userEmail;
        CurrentUserProfileDataModel.userResidentialAddress = userResidentialAddress;
        CurrentUserProfileDataModel.userCountryOfResidence = userCountryOfResidence;
        CurrentUserProfileDataModel.totalNumberOfProfileVisitor = totalNumberOfProfileVisitor;
        CurrentUserProfileDataModel.totalNumberOfProfileReach = totalNumberOfProfileReach;
        CurrentUserProfileDataModel.totalNumberOfOneStarRate = totalNumberOfOneStarRate;
        CurrentUserProfileDataModel.totalNumberOfTwoStarRate = totalNumberOfTwoStarRate;
        CurrentUserProfileDataModel.totalNumberOfThreeStarRate = totalNumberOfThreeStarRate;
        CurrentUserProfileDataModel.totalNumberOfFourStarRate = totalNumberOfFourStarRate;
        CurrentUserProfileDataModel.totalNumberOfFiveStarRate = totalNumberOfFiveStarRate;

    }


    //The getters are for querying  the queried data and setters for setting the data

   static public String getUserName(){
        return userName;
    }

    static  void setUserName(String userName) {
        CurrentUserProfileDataModel.userName = userName;
    }

    static  public String getUserId(){
        return userId;
    }

    static  void setUserId(String userId) {
        CurrentUserProfileDataModel.userId = userId;
    }

    static  public String getUserProfileImageDownloadUrl() {
        return userProfileImageDownloadUrl;
    }

    static  void setUserProfileImageDownloadUrl(String userProfileImageDownloadUrl) {
        CurrentUserProfileDataModel.userProfileImageDownloadUrl = userProfileImageDownloadUrl;
    }

    static  public long  getTotalNumberOfLibrariesCreated() {
        return totalNumberOfLibrariesCreated;
    }

    static void setTotalNumberOfLibrariesCreated(long  totalNumberOfLibrariesCreated) {
        CurrentUserProfileDataModel.totalNumberOfLibrariesCreated = totalNumberOfLibrariesCreated;
    }

    static  public boolean isAnAuthor() {
        return isAnAuthor;
    }
    static  void setIsAnAuthor(boolean isAnAuthor) {
        CurrentUserProfileDataModel.isAnAuthor = isAnAuthor;
    }


    static  public boolean isUserHasLibrary() {
        return isUserHasLibrary;
    }

    static  void setIsUserHasLibrary(boolean isUserHasLibrary) {
        CurrentUserProfileDataModel.isUserHasLibrary = isUserHasLibrary;
    }

    static  public String getGender() {
        return gender;
    }

    static void setGender(String gender) {
        CurrentUserProfileDataModel.gender = gender;
    }

    static public long  getAge() {
        return age;
    }

    static  void setAge(long  age) {
        CurrentUserProfileDataModel.age = age;
    }

    static  public String getDateOfBirth() {
        return dateOfBirth;
    }

    static void setDateOfBirth(String dateOfBirth) {
        CurrentUserProfileDataModel.dateOfBirth = dateOfBirth;
    }

    static  public String getDateRegistered() {
        return dateRegistered;
    }

    static  void setDateRegistered(String dateRegistered) {
        CurrentUserProfileDataModel.dateRegistered = dateRegistered;
    }
    static  public String getLastSeen() {
        return lastSeen;
    }

    static void setLastSeen(String lastSeen) {
        CurrentUserProfileDataModel.lastSeen = lastSeen;
    }

    static public boolean isUserProfileCompleted() {
        return isUserProfileCompleted;
    }

    static void setIsUserProfileCompleted(boolean isUserProfileCompleted) {
        CurrentUserProfileDataModel.isUserProfileCompleted = isUserProfileCompleted;
    }

    static public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    static  void setUserPhoneNumber(String userPhoneNumber) {
        CurrentUserProfileDataModel.userPhoneNumber = userPhoneNumber;
    }

    static   public String getUserEmail() {
        return userEmail;
    }

    static  void setUserEmail(String userEmail) {
        CurrentUserProfileDataModel.userEmail = userEmail;
    }

    static  public String getUserResidentialAddress() {
        return userResidentialAddress;
    }

    static void setUserResidentialAddress(String userResidentialAddress) {
        CurrentUserProfileDataModel.userResidentialAddress = userResidentialAddress;
    }


    static public String getUserCountryOfResidence() {
        return userCountryOfResidence;
    }

    static void setUserCountryOfResidence(String userCountryOfResidence) {
        CurrentUserProfileDataModel.userCountryOfResidence = userCountryOfResidence;
    }

    static  public long  getTotalNumberOfProfileVisitor() {
        return totalNumberOfProfileVisitor;
    }

    static void setTotalNumberOfProfileVisitor(long  totalNumberOfProfileVisitor) {
        CurrentUserProfileDataModel.totalNumberOfProfileVisitor = totalNumberOfProfileVisitor;
    }

    static  public long  getTotalNumberOfProfileReach() {
        return totalNumberOfProfileReach;
    }

    static void setTotalNumberOfProfileReach(long  totalNumberOfProfileReach) {
        CurrentUserProfileDataModel.totalNumberOfProfileReach = totalNumberOfProfileReach;
    }

    static  public long  getTotalNumberOfOneStarRate() {
        return totalNumberOfOneStarRate;
    }

    static void setTotalNumberOfOneStarRate(long  totalNumberOfOneStarRate) {
        CurrentUserProfileDataModel.totalNumberOfOneStarRate = totalNumberOfOneStarRate;
    }

    static public long  getTotalNumberOfTwoStarRate() {
        return totalNumberOfTwoStarRate;
    }

    static void setTotalNumberOfTwoStarRate(long  totalNumberOfTwoStarRate) {
        CurrentUserProfileDataModel.totalNumberOfTwoStarRate = totalNumberOfTwoStarRate;
    }

    public long  getTotalNumberOfThreeStarRate() {
        return totalNumberOfThreeStarRate;
    }

    static void setTotalNumberOfThreeStarRate(long  totalNumberOfThreeStarRate) {
        CurrentUserProfileDataModel.totalNumberOfThreeStarRate = totalNumberOfThreeStarRate;
    }

    static public long  getTotalNumberOfFourStarRate() {
        return totalNumberOfFourStarRate;
    }

    static  void setTotalNumberOfFourStarRate(long  totalNumberOfFourStarRate) {
        CurrentUserProfileDataModel.totalNumberOfFourStarRate = totalNumberOfFourStarRate;
    }

    static public long  getTotalNumberOfFiveStarRate() {
        return totalNumberOfFiveStarRate;
    }

    static  void setTotalNumberOfFiveStarRate(long  totalNumberOfFiveStarRate) {
        CurrentUserProfileDataModel.totalNumberOfFiveStarRate = totalNumberOfFiveStarRate;
    }

}
