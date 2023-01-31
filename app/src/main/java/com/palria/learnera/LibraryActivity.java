package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.models.*;
import com.squareup.picasso.Picasso;

public class LibraryActivity extends AppCompatActivity {

String libraryId;
String authorId;
String authorName;
String authorProfilePhotoDownloadUrl;
AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        initUI();
        fetchIntentData();
        toggleProgress(true);
        fetchLibraryProfile(new LibraryProfileFetchListener() {
            @Override
            public void onFailed(String errorMessage) {
                toggleProgress(false);

            }

            @Override
            public void onSuccess(LibraryDataModel libraryDataModel) {
            //use this libraryDataModel object to access the public methods.
                toggleProgress(false);

            }
        });
        getAuthorProfile(new AuthorProfileFetchListener() {
            @Override
            public void onFailed(String errorMessage) {
                toggleProgress(false);

            }

            @Override
            public void onSuccess(String authorName, String authorProfilePhotoDownloadUrl) {
                toggleProgress(false);

            }
        });
    }

    private void initUI(){
    alertDialog = new AlertDialog.Builder(getApplicationContext())
            .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
            .create();
}

    private void toggleProgress(boolean show)
    {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.hide();
        }
    }


    private void fetchIntentData()
    {
        Intent intent = getIntent();
        libraryId = intent.getStringExtra(GlobalConfig.LIBRARY_ID_KEY);
        authorId = intent.getStringExtra(GlobalConfig.LIBRARY_AUTHOR_ID_KEY);

    }    private void fetchLibraryProfile(LibraryProfileFetchListener libraryProfileFetchListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(authorId)
                .collection(GlobalConfig.ALL_LIBRARY_KEY)
                .document(libraryId)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                long totalNumberOfLibraryView = 0L;
                if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VIEWS_KEY) != null){
                    totalNumberOfLibraryView =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VIEWS_KEY);
                }
                long totalNumberOfLibraryReach = 0L;
                if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY) != null){
                    totalNumberOfLibraryReach =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY);
                }
                long totalNumberOfOneStarRate = 0L;
                if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) != null){
                    totalNumberOfOneStarRate =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY);
                }
                long totalNumberOfTwoStarRate = 0L;
                if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) != null){
                    totalNumberOfTwoStarRate =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY);
                }
                long totalNumberOfThreeStarRate = 0L;
                if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) != null){
                    totalNumberOfThreeStarRate =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY);
                }
                long totalNumberOfFourStarRate = 0L;
                if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) != null){
                    totalNumberOfFourStarRate =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY);
                }
                long totalNumberOfFiveStarRate = 0L;
                if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) != null){
                    totalNumberOfFiveStarRate =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY);
                }
                long finalTotalNumberOfLibraryView = totalNumberOfLibraryView;
                long finalTotalNumberOfLibraryReach = totalNumberOfLibraryReach;
                long finalTotalNumberOfOneStarRate = totalNumberOfOneStarRate;
                long finalTotalNumberOfTwoStarRate = totalNumberOfTwoStarRate;
                long finalTotalNumberOfFourStarRate = totalNumberOfFourStarRate;
                long finalTotalNumberOfThreeStarRate = totalNumberOfThreeStarRate;
                long finalTotalNumberOfFiveStarRate = totalNumberOfFiveStarRate;
                documentSnapshot.getReference()
                        .collection(GlobalConfig.LIBRARY_PROFILE_KEY)
                        .document(libraryId)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {


                                String libraryName = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
                                String libraryCategory = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_CATEGORY_KEY);
                                String dateCreated = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DATE_CREATED_KEY);
                                String authorUserId = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_AUTHOR_ID_KEY);
                                String libraryCoverPhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY);
                                long totalNumberOfTutorials = 0L;
                                if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY) != null){
                                    totalNumberOfTutorials =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY);
                                }


                                libraryProfileFetchListener.onSuccess(new LibraryDataModel(
                                        libraryName,
                                        libraryId,
                                        libraryCategory,
                                        libraryCoverPhotoDownloadUrl,
                                        dateCreated,
                                        totalNumberOfTutorials,
                                        finalTotalNumberOfLibraryView,
                                        finalTotalNumberOfLibraryReach,
                                        authorUserId,
                                        finalTotalNumberOfOneStarRate,
                                        finalTotalNumberOfTwoStarRate,
                                        finalTotalNumberOfThreeStarRate,
                                        finalTotalNumberOfFourStarRate,
                                        finalTotalNumberOfFiveStarRate
                                ));
                            }
                        });
            }
        });
    }

    private void getAuthorProfile(AuthorProfileFetchListener authorProfileFetchListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(authorId)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        authorProfileFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                    authorName =""+  documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);

                    authorProfilePhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
//USE THIS authorProfilePhotoDownloadUrl TO LOAD THE AUTHOR'S COVER PHOTO INTO IMAGE VIEW
//                        Picasso.get().load(authorProfilePhotoDownloadUrl).into(/*put image view object here*/);
                        authorProfileFetchListener.onSuccess(authorName,authorProfilePhotoDownloadUrl);
                    }
                });

    }

    interface LibraryProfileFetchListener{
        void onFailed(String errorMessage);
        void onSuccess(LibraryDataModel libraryDataModel);
    }
    interface AuthorProfileFetchListener{
        void onFailed(String errorMessage);
        void onSuccess(String authorName,String authorProfilePhotoDownloadUrl);
    }
}