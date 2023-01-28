package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class LibraryActivity extends AppCompatActivity {
String libraryId;
String authorId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
    }

    private void fetchLibraryProfile(ProfileFetchListener profileFetchListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(authorId)
                .collection(GlobalConfig.ALL_LIBRARY_KEY)
                .document(libraryId)
                .collection(GlobalConfig.LIBRARY_PROFILE_KEY)
                .document(libraryId)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        profileFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        String libraryName = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
                        String libraryCategory = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_CATEGORY_KEY);
                        String dateCreated = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_DATE_CREATED_KEY);
                        String authorUserId = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_AUTHOR_ID_KEY);
                        long totalNumberOfTutorials = 0L;
                        if(documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY) != null){
                          totalNumberOfTutorials =   documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY);
                        }
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


                        profileFetchListener.onSuccess(new LibraryDataModel(
                                                                             libraryName,
                                                                             libraryId,
                                                                             libraryCategory,
                                                                             dateCreated,
                                                                             totalNumberOfTutorials,
                                                                             totalNumberOfLibraryView,
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

    interface ProfileFetchListener{
        void onFailed(String errorMessage);
        void onSuccess(LibraryDataModel libraryDataModel);
    }
}