package com.palria.learnera.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.R;

import java.util.HashMap;

public class RatingBottomSheetWidget extends BottomSheetDialog {


    private Context context;

    private OnRatingPosted onPost;
    boolean isAuthorReview;
    boolean isLibraryReview;
    boolean isTutorialReview;
    String authorId;
    String libraryId;
    String tutorialId;

    //this is a descriptive word/tag illustrating the library's / tutorial's performance
    String performanceTag ="Teachable";

    public RatingBottomSheetWidget(@NonNull Context context,String authorId,String libraryId,String tutorialId,boolean  isAuthorReview,boolean isLibraryReview,boolean isTutorialReview) {
        super(context);
        this.context=context;
        this.authorId=authorId;
        this.libraryId=libraryId;
        this.tutorialId=tutorialId;
        this.isAuthorReview=isAuthorReview;
        this.isLibraryReview=isLibraryReview;
        this.isTutorialReview=isTutorialReview;

    }


    public void render(){
        //initialize the view for the bottom sheet

        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.rating_widget_bottom_sheet_default,null);


        EditText editText = view.findViewById(R.id.ratingBodyEditText);
        Button button = view.findViewById(R.id.postRatingActionButton);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = editText.getText().toString();
                int starLevel = ratingBar.getProgress();
                     onPost.onPost(starLevel,comment.trim());
                    //onRatingPosted.onPost(ratingBar.getProgress(), editText.getText().toString().trim());

                if(isAuthorReview){
                    reviewAuthor(authorId,comment,starLevel);
                }
                 else if(isLibraryReview){
                    reviewLibrary(authorId,libraryId,comment,starLevel);
                }
                else  if(isTutorialReview){
                    reviewTutorial(authorId,libraryId, tutorialId,comment,starLevel);
                }


            }
        });



        this.setContentView(view);

    }


    private void reviewAuthor(String authorId,String comment,long starLevel){
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
//        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
//        reviewerReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
        reviewerReviewDetails.put(GlobalConfig.IS_AUTHOR_REVIEW_KEY, true);
        writeBatch.set(reviewerDocumentReference,reviewerReviewDetails, SetOptions.merge());

        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onPost.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_REVIEW_AUTHOR_TYPE_KEY,authorId, null, null,  null, null, GlobalConfig.getCurrentUserId(),  new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {
//                        toggleProgress(false);

                            }

                            @Override
                            public void onFailed(String errorMessage) {
//                        toggleProgress(false);

                            }
                        });

                        onPost.onSuccess(true,false,false);
                    }
                });

    }
    private void reviewLibrary(String authorId,String libraryId,String comment,long starLevel){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference libraryReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId).collection(GlobalConfig.REVIEWS_KEY).document(GlobalConfig.getCurrentUserId());
        HashMap<String,Object> libraryReviewDetails = new HashMap<>();
        libraryReviewDetails.put(GlobalConfig.REVIEWER_ID_KEY, GlobalConfig.getCurrentUserId());
        libraryReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
        libraryReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, starLevel);
        libraryReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
        libraryReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        libraryReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
        writeBatch.set(libraryReviewDocumentReference,libraryReviewDetails,SetOptions.merge());

        DocumentReference libraryDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId);
        HashMap<String,Object> libraryDetails = new HashMap<>();
        libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REVIEWS_KEY, FieldValue.increment(1L));
        switch(Math.toIntExact(starLevel)){
            case 1: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY, FieldValue.increment(1L));
                break;
            case 2: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY,  FieldValue.increment(1L));
                break;
            case 3: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY,  FieldValue.increment(1L));
                break;
            case 4: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY, FieldValue.increment(1L));
                break;
            case 5: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY, FieldValue.increment(1L));
                break;
        }
        writeBatch.set(libraryDocumentReference,libraryDetails,SetOptions.merge());

        DocumentReference reviewerDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.OTHER_REVIEWS_KEY).document(libraryId);
        HashMap<String,Object> reviewerReviewDetails = new HashMap<>();
        reviewerReviewDetails.put(GlobalConfig.AUTHOR_ID_KEY, authorId);
        reviewerReviewDetails.put(GlobalConfig.LIBRARY_ID_KEY, libraryId);
        reviewerReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
        reviewerReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, starLevel);
//        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        reviewerReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
        reviewerReviewDetails.put(GlobalConfig.IS_LIBRARY_REVIEW_KEY, true);
        writeBatch.set(reviewerDocumentReference,reviewerReviewDetails, SetOptions.merge());

        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onPost.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_REVIEW_LIBRARY_TYPE_KEY,authorId, libraryId, null,  null, null, GlobalConfig.getCurrentUserId(), new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {
//                        toggleProgress(false);

                            }

                            @Override
                            public void onFailed(String errorMessage) {
//                        toggleProgress(false);

                            }
                        });

                        onPost.onSuccess(false,true,false);
                    }
                });

    }
    private void reviewTutorial(String authorId,String libraryId,String tutorialId,String comment,long starLevel){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference tutorialReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                .document(tutorialId).collection(GlobalConfig.REVIEWS_KEY)
                .document(GlobalConfig.getCurrentUserId());

        HashMap<String,Object> tutorialReviewDetails = new HashMap<>();
        tutorialReviewDetails.put(GlobalConfig.REVIEWER_ID_KEY, GlobalConfig.getCurrentUserId());
        tutorialReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
        tutorialReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, starLevel);
//        tutorialReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
        tutorialReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        tutorialReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
        writeBatch.set(tutorialReviewDocumentReference,tutorialReviewDetails, SetOptions.merge());


        DocumentReference tutorialDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId);
        HashMap<String,Object> tutorialDetails = new HashMap<>();
        tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_REVIEWS_KEY, FieldValue.increment(1L));
        switch(Math.toIntExact(starLevel)){
            case 1: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY, FieldValue.increment(1L));
                break;
            case 2: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY,  FieldValue.increment(1L));
                break;
            case 3: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY,  FieldValue.increment(1L));
                break;
            case 4: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY, FieldValue.increment(1L));
                break;
            case 5: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY, FieldValue.increment(1L));
                break;
        }
        writeBatch.set(tutorialDocumentReference,tutorialDetails,SetOptions.merge());


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
        writeBatch.set(reviewerDocumentReference,reviewerReviewDetails, SetOptions.merge());

        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onPost.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_REVIEW_TUTORIAL_TYPE_KEY,authorId, libraryId, tutorialId,  null, null, GlobalConfig.getCurrentUserId(),  new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {
//                        toggleProgress(false);

                            }

                            @Override
                            public void onFailed(String errorMessage) {
//                        toggleProgress(false);

                            }
                        });

                        onPost.onSuccess(false,false,true);
                    }
                });

    }


    public RatingBottomSheetWidget setRatingPostListener(OnRatingPosted onRatingPosted){
        this.onPost=onRatingPosted;
        return this;
    }


    public static class OnRatingPosted implements OnPostRating{


        @Override
        public void onPost(int star, String message) {

        }

        @Override
       public  void onFailed(String errorMessage){

        }

        @Override
       public void onSuccess(boolean isReviewAuthor,boolean isReviewLibrary,boolean isReviewTutorial){

        }

    }



    interface OnPostRating{
        void onPost(int star, String message);
        void onSuccess(boolean isReviewAuthor,boolean isReviewLibrary,boolean isReviewTutorial);
        void onFailed(String errorMessage);

    }

}
