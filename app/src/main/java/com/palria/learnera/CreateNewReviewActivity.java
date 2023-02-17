package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.HashBiMap;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;

public class CreateNewReviewActivity extends AppCompatActivity {
String authorId;
String libraryId;
String tutorialId;

boolean isAuthorReview = false;
boolean isLibraryReview = false;
boolean isTutorialReview = false;

String comment;
String performanceTag;

long starLevel = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_review);

    }

    private void reviewAuthor(String authorId,String comment,long starLevel,OnReviewListener onReviewListener){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference authorReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(authorId).collection(GlobalConfig.REVIEWS_KEY).document(GlobalConfig.getCurrentUserId());
        HashMap<String,Object> authorReviewDetails = new HashMap<>();
        authorReviewDetails.put(GlobalConfig.REVIEWER_ID_KEY, GlobalConfig.getCurrentUserId());
        authorReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
        authorReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, starLevel);
//        authorReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
        authorReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
//        authorReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
        writeBatch.set(authorReviewDocumentReference,authorReviewDetails);

        DocumentReference authorDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(authorId);
        HashMap<String,Object> authorDetails = new HashMap<>();
        authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_AUTHOR_REVIEWS_KEY, FieldValue.increment(1L));
        switch(Math.toIntExact(starLevel)){
            case 1: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY, FieldValue.increment(1L));
                return;
            case 2: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY,  FieldValue.increment(1L));
                return;
            case 3: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY,  FieldValue.increment(1L));
                return;
            case 4: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY, FieldValue.increment(1L));
                return;
            case 5: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY, FieldValue.increment(1L));
                return;
        }
        writeBatch.update(authorDocumentReference,authorDetails);



        DocumentReference reviewerDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.OTHER_REVIEWS_KEY).document(authorId);
        HashMap<String,Object> reviewerReviewDetails = new HashMap<>();
        reviewerReviewDetails.put(GlobalConfig.AUTHOR_ID_KEY, authorId);
        reviewerReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
        reviewerReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, starLevel);
        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        reviewerReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
        reviewerReviewDetails.put(GlobalConfig.IS_AUTHOR_REVIEW_KEY, true);
        writeBatch.set(reviewerDocumentReference,reviewerReviewDetails);

        writeBatch.commit()
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onReviewListener.onFailed(e.getMessage());
            }
        })
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_REVIEW_AUTHOR_TYPE_KEY,authorId, null, null,  null, null, null,GlobalConfig.getCurrentUserId(), new GlobalConfig.ActionCallback() {
                    @Override
                    public void onSuccess() {
//                        toggleProgress(false);

                    }

                    @Override
                    public void onFailed(String errorMessage) {
//                        toggleProgress(false);

                    }
                });

                onReviewListener.onSuccess(true,false,false);
            }
        });

    }
    private void reviewLibrary(String authorId,String libraryId,String comment,long starLevel,OnReviewListener onReviewListener){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference libraryReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId).collection(GlobalConfig.REVIEWS_KEY).document(GlobalConfig.getCurrentUserId());
        HashMap<String,Object> libraryReviewDetails = new HashMap<>();
        libraryReviewDetails.put(GlobalConfig.REVIEWER_ID_KEY, GlobalConfig.getCurrentUserId());
        libraryReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
        libraryReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, starLevel);
        libraryReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
        libraryReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        libraryReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
        writeBatch.set(libraryReviewDocumentReference,libraryReviewDetails);

        DocumentReference libraryDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId);
        HashMap<String,Object> libraryDetails = new HashMap<>();
        libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REVIEWS_KEY, FieldValue.increment(1L));
        switch(Math.toIntExact(starLevel)){
            case 1: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY, FieldValue.increment(1L));
                return;
            case 2: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY,  FieldValue.increment(1L));
                return;
            case 3: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY,  FieldValue.increment(1L));
                return;
            case 4: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY, FieldValue.increment(1L));
                return;
            case 5: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY, FieldValue.increment(1L));
                return;
        }
        writeBatch.set(libraryDocumentReference,libraryDetails);

        DocumentReference reviewerDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.OTHER_REVIEWS_KEY).document(libraryId);
        HashMap<String,Object> reviewerReviewDetails = new HashMap<>();
        reviewerReviewDetails.put(GlobalConfig.AUTHOR_ID_KEY, authorId);
        reviewerReviewDetails.put(GlobalConfig.LIBRARY_ID_KEY, libraryId);
        reviewerReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
        reviewerReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, starLevel);
        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        reviewerReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
        reviewerReviewDetails.put(GlobalConfig.IS_LIBRARY_REVIEW_KEY, true);
        writeBatch.set(reviewerDocumentReference,reviewerReviewDetails);

        writeBatch.commit()
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onReviewListener.onFailed(e.getMessage());
            }
        })
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_REVIEW_LIBRARY_TYPE_KEY,authorId, libraryId, null,  null, null, null,GlobalConfig.getCurrentUserId(),  new GlobalConfig.ActionCallback() {
                    @Override
                    public void onSuccess() {
//                        toggleProgress(false);

                    }

                    @Override
                    public void onFailed(String errorMessage) {
//                        toggleProgress(false);

                    }
                });

                onReviewListener.onSuccess(false,true,false);
            }
        });

    }
    private void reviewTutorial(String authorId,String libraryId,String tutorialId,String comment,long starLevel,OnReviewListener onReviewListener){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference tutorialReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                .document(tutorialId).collection(GlobalConfig.REVIEWS_KEY)
                .document(GlobalConfig.getCurrentUserId());

        HashMap<String,Object> tutorialReviewDetails = new HashMap<>();
        tutorialReviewDetails.put(GlobalConfig.REVIEWER_ID_KEY, GlobalConfig.getCurrentUserId());
        tutorialReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
        tutorialReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, starLevel);
        tutorialReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
        tutorialReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        tutorialReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
        writeBatch.set(tutorialReviewDocumentReference,tutorialReviewDetails);


        DocumentReference tutorialDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId);
        HashMap<String,Object> tutorialDetails = new HashMap<>();
        tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_REVIEWS_KEY, FieldValue.increment(1L));
        switch(Math.toIntExact(starLevel)){
            case 1: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY, FieldValue.increment(1L));
                return;
            case 2: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY,  FieldValue.increment(1L));
                return;
            case 3: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY,  FieldValue.increment(1L));
                return;
            case 4: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY, FieldValue.increment(1L));
                return;
            case 5: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY, FieldValue.increment(1L));
                return;
        }
        writeBatch.set(tutorialDocumentReference,tutorialDetails);


        DocumentReference reviewerDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(GlobalConfig.getCurrentUserId())
                .collection(GlobalConfig.OTHER_REVIEWS_KEY)
                .document(tutorialId);

        HashMap<String,Object> reviewerReviewDetails = new HashMap<>();
        reviewerReviewDetails.put(GlobalConfig.AUTHOR_ID_KEY, authorId);
        reviewerReviewDetails.put(GlobalConfig.LIBRARY_CONTAINER_ID_KEY, libraryId);
        reviewerReviewDetails.put(GlobalConfig.TUTORIAL_ID_KEY, tutorialId);
        reviewerReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
        reviewerReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, starLevel);
        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        reviewerReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
        reviewerReviewDetails.put(GlobalConfig.IS_TUTORIAL_REVIEW_KEY, true);
        writeBatch.set(reviewerDocumentReference,reviewerReviewDetails);

        writeBatch.commit()
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onReviewListener.onFailed(e.getMessage());
            }
        })
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_REVIEW_TUTORIAL_TYPE_KEY,authorId, libraryId, tutorialId, null, null, null,GlobalConfig.getCurrentUserId(), new GlobalConfig.ActionCallback() {
                    @Override
                    public void onSuccess() {
//                        toggleProgress(false);

                    }

                    @Override
                    public void onFailed(String errorMessage) {
//                        toggleProgress(false);

                    }
                });

                onReviewListener.onSuccess(false,false,true);
            }
        });

    }

    interface OnReviewListener{
        void onSuccess(boolean isReviewAuthor,boolean isReviewLibrary,boolean isReviewTutorial);
        void onFailed(String errorMessage);
    }
}