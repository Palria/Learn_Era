package com.palria.learnera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class GlobalConfig {

    private static String CURRENT_USER_ID;
    private static String CURRENT_USER_TOKEN_ID;

    //FIRESTORE VARIABLE KEYS
    public static final String ALL_USERS_KEY = "ALL_USERS";
    public static final String USER_PROFILE_KEY = "USER_PROFILE";
    public static final String USER_DISPLAY_NAME_KEY = "USER_DISPLAY_NAME";
    public static final String USER_PROFILE_DATE_CREATED_KEY = "USER_PROFILE_DATE_CREATED";
    public static final String USER_PROFILE_DATE_CREATED_TIME_STAMP_KEY = "USER_PROFILE_DATE_CREATED_TIME_STAMP";
    public static final String USER_TOKEN_ID_KEY = "USER_TOKEN_ID";
    public static final String USER_GENDER_TYPE_KEY = "USER_GENDER_TYPE";
    public static final String USER_COUNTRY_OF_ORIGIN_KEY = "USER_COUNTRY_OF_ORIGIN";
    public static final String USER_SEARCH_VERBATIM_KEYWORD_KEY = "USER_SEARCH_VERBATIM_KEYWORD";
    public static final String USER_SEARCH_ANY_MATCH_KEYWORD_KEY = "USER_SEARCH_ANY_MATCH_KEYWORD";
    public static final String IS_CREATE_NEW_LIBRARY_KEY = "ALL_LIBRARY";
    public static final String ALL_LIBRARY_KEY = "ALL_LIBRARY";
    public static final String LIBRARY_PROFILE_KEY = "LIBRARY_PROFILE";
    public static final String LIBRARY_DISPLAY_NAME_KEY = "LIBRARY_DISPLAY_NAME";
    public static final String LIBRARY_DESCRIPTION_KEY = "LIBRARY_DESCRIPTION";
    public static final String LIBRARY_CATEGORY_KEY = "LIBRARY_CATEGORY";
    public static final String LIBRARY_ID_KEY = "LIBRARY_ID";
    public static final String LIBRARY_OWNER_ID_KEY = "LIBRARY_OWNER_ID";
    public static final String LIBRARY_DATE_CREATED_KEY = "LIBRARY_DATE_CREATED";
    public static final String LIBRARY_DATE_CREATED_TIME_STAMP_KEY = "LIBRARY_DATE_CREATED_TIME_STAMP";
    public static final String LIBRARY_SEARCH_VERBATIM_KEYWORD_KEY = "LIBRARY_SEARCH_VERBATIM_KEYWORD";
    public static final String LIBRARY_SEARCH_ANY_MATCH_KEYWORD_KEY = "LIBRARY_SEARCH_ANY_MATCH_KEYWORD";
    public static final String LAST_LIBRARY_CREATED_ID_KEY = "LAST_LIBRARY_CREATED_ID";
    public static final String LAST_LIBRARY_DATE_CREATED_KEY = "LAST_LIBRARY_DATE_CREATED";
    public static final String LIBRARY_DATE_EDITED_KEY = "LIBRARY_DATE_EDITED";
    public static final String LIBRARY_DATE_EDITED_TIME_STAMP_KEY = "LAST_LIBRARY_DATE_CREATED_TIME_STAMP";
    public static final String LAST_LIBRARY_DATE_CREATED_TIME_STAMP_KEY = "LAST_LIBRARY_DATE_CREATED_TIME_STAMP";
    public static final String TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY = "TOTAL_NUMBER_OF_LIBRARY_CREATED";
    public static final String LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY = "LIBRARY_COVER_PHOTO_DOWNLOAD_URL";
    public static final String IS_LIBRARY_COVER_PHOTO_INCLUDED_KEY = "IS_LIBRARY_COVER_PHOTO_INCLUDED";
    public static final String LIBRARY_COVER_PHOTO_KEY = "LIBRARY_COVER_PHOTO";
    public static final String LIBRARY_IMAGES_KEY = "LIBRARY_IMAGES";




    private static FirebaseFirestore firebaseFirestoreInstance;
    private static FirebaseStorage firebaseStorageInstance;





    static boolean isUserLoggedIn(){

        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }


   static void setCurrentUserId(@NonNull String currentUserId){
        GlobalConfig.CURRENT_USER_ID = currentUserId;
    }

    static String getCurrentUserId(){
       return GlobalConfig.CURRENT_USER_ID;
    }

   static void setCurrentUserTokenId(@NonNull String currentUserTokenId){
        GlobalConfig.CURRENT_USER_TOKEN_ID = currentUserTokenId;
    }

    static String getCurrentUserTokenId(){
       return GlobalConfig.CURRENT_USER_TOKEN_ID;
    }

   static void setFirebaseFirestoreInstance(FirebaseFirestore firebaseFirestoreInstance){
        GlobalConfig.firebaseFirestoreInstance = firebaseFirestoreInstance;
    }

    static FirebaseFirestore getFirebaseFirestoreInstance(){
       return GlobalConfig.firebaseFirestoreInstance;
    }

   static void setFirebaseStorageInstance(FirebaseStorage firebaseStorageInstance){
        GlobalConfig.firebaseStorageInstance = firebaseStorageInstance;
    }

    static FirebaseStorage getFirebaseStorageInstance(){
       return GlobalConfig.firebaseStorageInstance;
    }

    interface SignInListener{
        void onSuccess(String email, String password);
        void onFailed(String errorMessage);
        void onEmptyInput(boolean isEmailEmpty, boolean isPasswordEmpty);
    }
  interface SignUpListener{
        void onSuccess(String email, String password);
        void onFailed(String errorMessage);
        void onEmptyInput(boolean isEmailEmpty, boolean isPasswordEmpty);
    }


    static void signInUserWithEmailAndPassword(@NonNull String email, @NonNull String password,SignInListener signInListener){
        if(email != null && password != null){
            if(email.isEmpty() && password.isEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                signInListener.onFailed(e.getMessage());
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                signInListener.onSuccess(email, password);
                            }
                        });
            }else{
                signInListener.onEmptyInput(email.isEmpty(),password.isEmpty());
            }
        }else{
            signInListener.onEmptyInput(email == null,password == null);

        }
    }
    static void signUpUserWithEmailAndPassword(@NonNull String email,@NonNull  String password,SignUpListener signUpListener){
        if(email != null && password != null){
            if(email.isEmpty() && password.isEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                signUpListener.onFailed(e.getMessage());
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                signUpListener.onSuccess(email, password);
                            }
                        });
            }else{
                signUpListener.onEmptyInput(email.isEmpty(),password.isEmpty());
            }
        }else{
            signUpListener.onEmptyInput(email == null,password == null);

        }
    }



    @SuppressLint("MissingPermission")
    static boolean isConnectedOnline(Context context) {



        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if( cm.getActiveNetworkInfo() != null){
            return cm.getActiveNetworkInfo().isConnected();

        }
        return false;
    }

    public static String getRandomString(int length) {
        String[] characterArray = new String[]{
                "a", "A", "1",
                "b", "B", "2",
                "c", "C", "3",
                "d", "D", "4",
                "e", "E", "5",
                "f", "F", "6",
                "g", "G", "7",
                "h", "H", "8",
                "i", "I", "9",
                "j", "J", "0",
                "k", "K", "1",
                "l", "L", "2",
                "m", "M", "3",
                "n", "N", "4",
                "o", "O", "5",
                "p", "P", "6",
                "q", "Q", "7",
                "r", "R", "8",
                "s", "S", "9",
                "t", "T", "0",
                "u", "U", "1",
                "v", "V", "2",
                "w", "W", "3",
                "x", "X", "4",
                "y", "Y", "5",
                "z", "Z", "6"
        };

        StringBuilder randomString = new StringBuilder("AUN");

        for(int i=0; i<length; i++){

            int randomPosition = new Random().nextInt(characterArray.length -1);

            randomString.append(Array.get(characterArray, randomPosition));
        }
        return randomString + "CJD";
    }

    public static ArrayList getCountryArrayList(@Nullable ArrayList<String> countryArrayList){
        ArrayList<String> arrayList = null;
        if(countryArrayList == null){
            arrayList = new ArrayList<>();

        }else{
            arrayList = countryArrayList;

        }

        arrayList.add("Albania");
        arrayList.add("Algeria");
        arrayList.add("American Samoa");
        arrayList.add("Andorra");
        arrayList.add("Angola");
        arrayList.add("Anguilla");
        arrayList.add("Antarctica");
        arrayList.add("Antigua and Barbuda");
        arrayList.add("Argentina");
        arrayList.add("Armenia");
        arrayList.add("Aruba");
        arrayList.add("Australia");
        arrayList.add("Austria");
        arrayList.add("Azerbaijan");
        arrayList.add("Bahamas");
        arrayList.add("Bahrain");
        arrayList.add("Bangladesh");
        arrayList.add("Barbados");
        arrayList.add("Belgium");
        arrayList.add("Belize");
        arrayList.add("Benin");
        arrayList.add("Bermuda");
        arrayList.add("Bhutan");
        arrayList.add("Bolivia");
        arrayList.add("Bonaire, Sint Eustatius and Saba");
        arrayList.add("Bosnia and Herzegovina");
        arrayList.add("Botswana");
        arrayList.add("Bouvet Island");
        arrayList.add("Brazil");
        arrayList.add("British Indian Ocean Territory");
        arrayList.add("British Virgin Islands");
        arrayList.add("Brunei Darussalam");
        arrayList.add("Bulgaria");
        arrayList.add("Burkina Faso");
        arrayList.add("Burundi");
        arrayList.add("Cambodia");
        arrayList.add("Cameroon");
        arrayList.add("Canada");
        arrayList.add("Cape Verde");
        arrayList.add("Cayman Islands");
        arrayList.add("Central African Republic");
        arrayList.add("Chad");
        arrayList.add("Chile");
        arrayList.add("China");
        arrayList.add("Christmas Island");
        arrayList.add("Cocos (Keeling) Islands");
        arrayList.add("Colombia");
        arrayList.add("Comoros");
        arrayList.add("Congo");
        arrayList.add("Cook Islands");
        arrayList.add("Costa Rica");
        arrayList.add("Cote d'Ivoire");
        arrayList.add("Croatia");
        arrayList.add("Curacao");
        arrayList.add("Cyprus");
        arrayList.add("Czech Republic");
        arrayList.add("Denmark");
        arrayList.add("Djibouti");
        arrayList.add("Dominica");
        arrayList.add("Dominican Republic");
        arrayList.add("Ecuador");
        arrayList.add("Egypt");
        arrayList.add("El Salvador");
        arrayList.add("Equatorial Guinea");
        arrayList.add("Eritrea");
        arrayList.add("Estonia");
        arrayList.add("Ethiopia");
        arrayList.add("Falkland Islands");
        arrayList.add("Faroe Islands");
        arrayList.add("Fiji");
        arrayList.add("Finland");
        arrayList.add("France");
        arrayList.add("French Guiana");
        arrayList.add("French Polynesia");
        arrayList.add("French Southern and Antarctic Lands");
        arrayList.add("Gabon");
        arrayList.add("Gambia");
        arrayList.add("Georgia");
        arrayList.add("Germany");
        arrayList.add("Ghana");
        arrayList.add("Gibraltar");
        arrayList.add("Greece");
        arrayList.add("Greenland");
        arrayList.add("Grenada");
        arrayList.add("Guadeloupe");
        arrayList.add("Guam");
        arrayList.add("Guatemala");
        arrayList.add("Guernsey");
        arrayList.add("Guinea");
        arrayList.add("Guinea-Bissau");
        arrayList.add("Guyana");
        arrayList.add("Haiti");
        arrayList.add("Heard Island and McDonald Islands");
        arrayList.add("Holy See");
        arrayList.add("Honduras");
        arrayList.add("Hong Kong");
        arrayList.add("Hungary");
        arrayList.add("Iceland");
        arrayList.add("Indonesia");
        arrayList.add("Ireland");
        arrayList.add("Isle of Man");
        arrayList.add("Israel");
        arrayList.add("Italy");
        arrayList.add("Jamaica");
        arrayList.add("Japan");
        arrayList.add("Jordan");
        arrayList.add("Kazakhstan");
        arrayList.add("Kenya");
        arrayList.add("Kiribati");
        arrayList.add("Kuwait");
        arrayList.add("Kyrgyzstan");
        arrayList.add("Laos");
        arrayList.add("Latvia");
        arrayList.add("Lebanon");
        arrayList.add("Lesotho");
        arrayList.add("Liechtenstein");
        arrayList.add("Lithuania");
        arrayList.add("Luxembourg");
        arrayList.add("Macao");
        arrayList.add("Macedonia");
        arrayList.add("Madagascar");
        arrayList.add("Malawi");
        arrayList.add("Malaysia");
        arrayList.add("Maldives");
        arrayList.add("Mali");
        arrayList.add("Malta");
        arrayList.add("Marshall Islands");
        arrayList.add("Martinique");
        arrayList.add("Mauritania");
        arrayList.add("Mauritius");
        arrayList.add("Mayotte");
        arrayList.add("Moldova");
        arrayList.add("Monaco");
        arrayList.add("Mongolia");
        arrayList.add("Montenegro");
        arrayList.add("Montserrat");
        arrayList.add("Morocco");
        arrayList.add("Mozambique");
        arrayList.add("Myanmar");
        arrayList.add("Namibia");
        arrayList.add("Nauru");
        arrayList.add("Nepal");
        arrayList.add("Netherlands");
        arrayList.add("Netherlands Antilles");
        arrayList.add("New Caledonia");
        arrayList.add("New Zealand");
        arrayList.add("Nicaragua");
        arrayList.add("Niger");
        arrayList.add("Nigeria");
        arrayList.add("Niue");
        arrayList.add("Norfolk Island");
        arrayList.add("Northern Mariana Islands");
        arrayList.add("Norway");
        arrayList.add("Oman");
        arrayList.add("Pakistan");
        arrayList.add("Palau");
        arrayList.add("Palestinian Territories");
        arrayList.add("Panama");
        arrayList.add("Papua New Guinea");
        arrayList.add("Paraguay");
        arrayList.add("Peru");
        arrayList.add("Philippines");
        arrayList.add("Pitcairn");
        arrayList.add("Poland");
        arrayList.add("Portugal");
        arrayList.add("Puerto Rico");
        arrayList.add("Qatar");
        arrayList.add("Reunion");
        arrayList.add("Romania");
        arrayList.add("Rwanda");
        arrayList.add("Saint Barthelemy");
        arrayList.add("Saint Helena");
        arrayList.add("Saint Kitts and Nevis");
        arrayList.add("Saint Lucia");
        arrayList.add("Saint Martin (French part)");
        arrayList.add("Saint Pierre and Miquelon");
        arrayList.add("Saint Vincent and the Grenadines");
        arrayList.add("Samoa");
        arrayList.add("San Marino");
        arrayList.add("Sao Tome and Principe");
        arrayList.add("Saudi Arabia");
        arrayList.add("Senegal");
        arrayList.add("Serbia");
        arrayList.add("Seychelles");
        arrayList.add("Sierra Leone");
        arrayList.add("Singapore");
        arrayList.add("Sint Maarten (Dutch part)");
        arrayList.add("Slovakia");
        arrayList.add("Slovenia");
        arrayList.add("Solomon Islands");
        arrayList.add("Somalia");
        arrayList.add("South Africa");
        arrayList.add("South Korea");
        arrayList.add("Spain");
        arrayList.add("Sri Lanka");
        arrayList.add("Suriname");
        arrayList.add("Svalbard and Jan Mayen");
        arrayList.add("Swaziland");
        arrayList.add("Sweden");
        arrayList.add("Switzerland");
        arrayList.add("Taiwan");
        arrayList.add("Tajikistan");
        arrayList.add("Tanzania");
        arrayList.add("Thailand");
        arrayList.add("Timor-Leste");
        arrayList.add("Togo");
        arrayList.add("Tokelau");
        arrayList.add("Tonga");
        arrayList.add("Trinidad and Tobago");
        arrayList.add("Tunisia");
        arrayList.add("Turkey");
        arrayList.add("Turkmenistan");
        arrayList.add("Turks and Caicos Islands");
        arrayList.add("Tuvalu");
        arrayList.add("Uganda");
        arrayList.add("Ukraine");
        arrayList.add("United Arab Emirates");
        arrayList.add("United Kingdom");
        arrayList.add("United States Minor Outlying Islands");
        arrayList.add("United States Virgin Islands");
        arrayList.add("Uruguay");
        arrayList.add("Uzbekistan");
        arrayList.add("Vanuatu");
        arrayList.add("Venezuela");
        arrayList.add("Vietnam");
        arrayList.add("Wallis and Futuna");
        arrayList.add("Western Sahara");
        arrayList.add("Zambia");
        arrayList.add("Zimbabwe");

        return arrayList;
    }

    static  void checkIfDocumentExists(DocumentReference documentReference, OnDocumentExistStatusCallback onDocumentExistStatusCallback){
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        onDocumentExistStatusCallback.onExist();
                    }else if (!documentSnapshot.exists()){
                        onDocumentExistStatusCallback.onNotExist();
                    }
                }
                else{
                    onDocumentExistStatusCallback.onFailed(task.getException().getMessage());
                }
            }
        });
    }

    public static void createPopUpMenu(Context context, int menuRes, View anchorView, OnMenuItemClickListener onMenuItemClickListener) {


        PopupMenu popupMenu = new PopupMenu(context, anchorView);
        popupMenu.getMenuInflater().inflate(menuRes, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMenuItemClickListener.onMenuItemClicked(item);
                return false;

            }
        });
        popupMenu.show();


    }

    static ArrayList<String> generateSearchVerbatimKeyWords(@NonNull String itemName){
        ArrayList<String> searchVerbatimKeywordsArrayList = null;

        if(itemName != null && itemName.isEmpty()) {
            itemName = itemName.toLowerCase();
            int itemLength = itemName.length();

            searchVerbatimKeywordsArrayList = (ArrayList<String>) Arrays.asList(itemName.split(" "));
            searchVerbatimKeywordsArrayList.add(itemName);

        }else{
            searchVerbatimKeywordsArrayList = new ArrayList<>();
        }

        return searchVerbatimKeywordsArrayList;
    }

    static ArrayList<String> generateSearchAnyMatchKeyWords(@NonNull String itemName) {
        ArrayList<String> searchAnyMatchKeywordsArrayList = new ArrayList<>();

        if (itemName != null && itemName.isEmpty()) {
            itemName = itemName.toLowerCase();
            int itemLength = itemName.length();

            if (itemLength != 0) {
                for (int i = 0; i < itemLength; i++) {
                    searchAnyMatchKeywordsArrayList.add(itemName.substring(i));


                    for (int j = itemLength - 1; j > i; j--) {

                        searchAnyMatchKeywordsArrayList.add(itemName.substring(i, j));


                    }


                }

                for (int i = 0; i < itemLength; i++) {
                    for (int k = i + 1; k < itemLength; k++) {
                        if (!searchAnyMatchKeywordsArrayList.contains(itemName.charAt(i) + "" + itemName.charAt(k))) {

                            searchAnyMatchKeywordsArrayList.add(itemName.charAt(i) + "" + itemName.charAt(k));

                        }
                    }

                }
            }
        }
            return searchAnyMatchKeywordsArrayList;
        }

    static  public String getDate(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd', 'HH:mm:ss", Locale.US);
        Date date = new Date();
        String formattedDate = simpleDateFormat.format(date);


        return formattedDate;
    }


     //
    //INTERFACES
    //
    public interface OnMenuItemClickListener {
        boolean onMenuItemClicked(MenuItem item);

    }
    interface OnDocumentExistStatusCallback{

        void onExist();
        void onNotExist();
        void onFailed(@NonNull String errorMessage);

    }

}
