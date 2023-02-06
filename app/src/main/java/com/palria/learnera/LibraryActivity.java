package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.models.*;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

String libraryId;
String authorId;
String authorName;
String authorProfilePhotoDownloadUrl;
AlertDialog alertDialog;
FrameLayout tutorialsFrameLayout;
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
                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_VISIT_LIBRARY_TYPE_KEY,authorId,libraryId,null,false,true,false,null,null,null,false,false,false);

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
        openAllTutorialFragment();
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
            alertDialog.cancel();
        }
    }


    private void fetchIntentData()
    {
        Intent intent = getIntent();
        libraryId = intent.getStringExtra(GlobalConfig.LIBRARY_ID_KEY);
        authorId = intent.getStringExtra(GlobalConfig.LIBRARY_AUTHOR_ID_KEY);

    }

    private void fetchLibraryProfile(LibraryProfileFetchListener libraryProfileFetchListener){
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
            public void onSuccess(DocumentSnapshot documentSnapshot){
                String libraryId = documentSnapshot.getId();
                long totalNumberOfLibraryVisitor = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VISITOR_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VISITOR_KEY) : 0L;
                long totalNumberOfLibraryReach = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY) : 0L;
                long totalNumberOfOneStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) : 0L;
                long totalNumberOfTwoStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) : 0L;
                long totalNumberOfThreeStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) : 0L;
                long totalNumberOfFourStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) : 0L;
                long totalNumberOfFiveStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) : 0L;
                long totalNumberOfTutorials = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY) : 0L;


                String libraryName = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
                ArrayList<String> libraryCategoryArray = (ArrayList<String>) documentSnapshot.get(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY);
                String dateCreated = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DATE_CREATED_KEY);
                String authorUserId = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_AUTHOR_ID_KEY);
                String libraryCoverPhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY);



                libraryProfileFetchListener.onSuccess(new LibraryDataModel(
                        libraryName,
                        libraryId,
                        libraryCategoryArray,
                        libraryCoverPhotoDownloadUrl,
                        dateCreated,
                        totalNumberOfTutorials,
                        totalNumberOfLibraryVisitor,
                        totalNumberOfLibraryReach,
                        authorUserId,
                        totalNumberOfOneStarRate,
                        totalNumberOfTwoStarRate,
                        totalNumberOfThreeStarRate,
                        totalNumberOfFourStarRate,
                        totalNumberOfFiveStarRate
                ));
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

    private void openAllTutorialFragment(){
        AllTutorialFragment tutorialsFragment = new AllTutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(GlobalConfig.IS_FROM_LIBRARY_ACTIVITY_CONTEXT_KEY,true);
        bundle.putString(GlobalConfig.LIBRARY_ID_KEY,libraryId);
        tutorialsFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(tutorialsFrameLayout.getId(),tutorialsFragment)
                .commit();
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