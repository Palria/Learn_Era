package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.models.TutorialDataModel;

public class TutorialActivity extends AppCompatActivity {
String tutorialId = "";
String authorId = "";
String libraryId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        iniUI();
        fetchIntentData();

        fetchTutorial(new TutorialFetchListener() {
            @Override
            public void onSuccess(TutorialDataModel tutorialDataModel) {
                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_VISIT_TUTORIAL_TYPE_KEY,authorId,libraryId,tutorialId,false,false,true,null,null,null,false,false,false);

            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });
    }

    private void iniUI(){

    }
    private void fetchIntentData(){
        Intent intent = getIntent();
        tutorialId = intent.getStringExtra(GlobalConfig.TUTORIAL_ID_KEY);

    }

    private void fetchTutorial(TutorialFetchListener tutorialFetchListener){
       GlobalConfig.getFirebaseFirestoreInstance()
               .collection(GlobalConfig.ALL_TUTORIAL_KEY)
               .document(tutorialId)
               .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tutorialFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot){
                            String libraryId =""+ documentSnapshot.get(GlobalConfig.LIBRARY_CONTAINER_ID_KEY);
                            String tutorialId =""+ documentSnapshot.get(GlobalConfig.TUTORIAL_ID_KEY);

                            long totalNumberOfTutorialVisitor = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_VISITOR_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_VISITOR_KEY) : 0L;
                            long totalNumberOfTutorialReach = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_REACH_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_REACH_KEY) : 0L;
                            long totalNumberOfOneStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) : 0L;
                            long totalNumberOfTwoStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) : 0L;
                            long totalNumberOfThreeStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) : 0L;
                            long totalNumberOfFourStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) : 0L;
                            long totalNumberOfFiveStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) : 0L;


                            String tutorialName = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_DISPLAY_NAME_KEY);
                            String tutorialCategory = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_CATEGORY_KEY);
                            String dateCreated = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_DATE_CREATED_KEY);
                            String authorUserId = ""+ documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_PAGES_CREATED_KEY);
                            long totalNumberOfPages = documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_FOLDERS_CREATED_KEY)!=null ?documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOLDERS_CREATED_KEY) :0L;
                            long totalNumberOfFolders =  documentSnapshot.get(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY)!=null ?documentSnapshot.getLong(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY) :0L;
                            String tutorialCoverPhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_COVER_PHOTO_DOWNLOAD_URL_KEY);



                            tutorialFetchListener.onSuccess(new TutorialDataModel(
                                                            tutorialName,
                                                            tutorialCategory,
                                                            tutorialId,
                                                            dateCreated,
                                                            totalNumberOfPages,
                                                            totalNumberOfFolders,
                                                            totalNumberOfTutorialVisitor,
                                                            totalNumberOfTutorialReach,
                                                            authorUserId,
                                                            libraryId,
                                                            tutorialCoverPhotoDownloadUrl,
                                                            totalNumberOfOneStarRate,
                                                            totalNumberOfTwoStarRate,
                                                            totalNumberOfThreeStarRate,
                                                            totalNumberOfFourStarRate,
                                                            totalNumberOfFiveStarRate
                                                            ));
                                }
                            });


    }


    interface TutorialFetchListener{
        void onSuccess(TutorialDataModel tutorialDataModel);
        void onFailed(String errorMessage);
    }

}