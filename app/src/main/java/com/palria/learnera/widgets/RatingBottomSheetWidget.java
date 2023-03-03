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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    static Snackbar ratingSnackbar;
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


    public RatingBottomSheetWidget render(View mainLayout,boolean isEdition){
        //initialize the view for the bottom sheet

        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.rating_widget_bottom_sheet_default,null);


        EditText editText = view.findViewById(R.id.ratingBodyEditText);
        Button button = view.findViewById(R.id.postRatingActionButton);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RatingBottomSheetWidget.this.dismiss();
               ratingSnackbar = GlobalConfig.createSnackBar(context,mainLayout,"Rating in progress please wait...",Snackbar.LENGTH_INDEFINITE);
                String comment = editText.getText().toString();
                int starLevel = ratingBar.getProgress();
                     onPost.onPost(starLevel,comment.trim());
                    //onRatingPosted.onPost(ratingBar.getProgress(), editText.getText().toString().trim());

                if(isAuthorReview){
                    if(isEdition) {

                        GlobalConfig.editAuthorReview(authorId, comment, performanceTag, starLevel, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {

                                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_EDIT_AUTHOR_REVIEW_TYPE_KEY, authorId, null, null, null, null, GlobalConfig.getCurrentUserId(), new GlobalConfig.ActionCallback() {
                                    @Override
                                    public void onSuccess() {
//                        toggleProgress(false);

                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {
//                        toggleProgress(false);

                                    }
                                });

                                onPost.onSuccess(true, false, false);
                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                onPost.onFailed(errorMessage);
                            }
                        });
                    }else {

                        GlobalConfig.reviewAuthor(authorId, comment, performanceTag, starLevel, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {

                                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_REVIEW_AUTHOR_TYPE_KEY, authorId, null, null, null, null, GlobalConfig.getCurrentUserId(), new GlobalConfig.ActionCallback() {
                                    @Override
                                    public void onSuccess() {
//                        toggleProgress(false);

                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {
//                        toggleProgress(false);

                                    }
                                });

                                onPost.onSuccess(true, false, false);
                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                onPost.onFailed(errorMessage);

                            }
                        });
                    }

                }
                 else if(isLibraryReview){
                    if(isEdition) {
                        GlobalConfig.editLibraryReview(authorId, libraryId, comment, performanceTag, starLevel, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {

                                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_REVIEW_LIBRARY_TYPE_KEY, authorId, libraryId, null, null, null, GlobalConfig.getCurrentUserId(), new GlobalConfig.ActionCallback() {
                                    @Override
                                    public void onSuccess() {
//                        toggleProgress(false);

                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {
//                        toggleProgress(false);

                                    }
                                });

                                onPost.onSuccess(false, true, false);
                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                onPost.onFailed(errorMessage);

                            }
                        });
                    }else {
                        GlobalConfig.reviewLibrary(authorId, libraryId, comment, performanceTag, starLevel, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {

                                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_REVIEW_LIBRARY_TYPE_KEY, authorId, libraryId, null, null, null, GlobalConfig.getCurrentUserId(), new GlobalConfig.ActionCallback() {
                                    @Override
                                    public void onSuccess() {
//                        toggleProgress(false);

                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {
//                        toggleProgress(false);

                                    }
                                });

                                onPost.onSuccess(false, true, false);
                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                onPost.onFailed(errorMessage);

                            }
                        });
                    }
                }
                else  if(isTutorialReview){
                    if(isEdition) {
                        GlobalConfig.editTutorialReview(authorId, libraryId, tutorialId, comment, performanceTag, starLevel, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {

                                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_REVIEW_TUTORIAL_TYPE_KEY, authorId, libraryId, tutorialId, null, null, GlobalConfig.getCurrentUserId(), new GlobalConfig.ActionCallback() {
                                    @Override
                                    public void onSuccess() {
//                        toggleProgress(false);

                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {
//                        toggleProgress(false);

                                    }
                                });

                                onPost.onSuccess(false, false, true);
                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                onPost.onFailed(errorMessage);

                            }
                        });
                    }else {
                        GlobalConfig.reviewTutorial(authorId, libraryId, tutorialId, comment, performanceTag, starLevel, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {

                                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_REVIEW_TUTORIAL_TYPE_KEY, authorId, libraryId, tutorialId, null, null, GlobalConfig.getCurrentUserId(), new GlobalConfig.ActionCallback() {
                                    @Override
                                    public void onSuccess() {
//                        toggleProgress(false);

                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {
//                        toggleProgress(false);

                                    }
                                });

                                onPost.onSuccess(false, false, true);
                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                onPost.onFailed(errorMessage);

                            }
                        });
                    }
                }


            }
        });



        this.setContentView(view);
return this;
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
            RatingBottomSheetWidget.ratingSnackbar.setText("Failed: "+errorMessage).setDuration(Snackbar.LENGTH_SHORT);

        }

        @Override
       public void onSuccess(boolean isReviewAuthor,boolean isReviewLibrary,boolean isReviewTutorial){
            RatingBottomSheetWidget.ratingSnackbar.setText("Rating succeeded!").setDuration(Snackbar.LENGTH_SHORT);
        }

    }



    interface OnPostRating{
        void onPost(int star, String message);
        void onSuccess(boolean isReviewAuthor,boolean isReviewLibrary,boolean isReviewTutorial);
        void onFailed(String errorMessage);

    }

}
