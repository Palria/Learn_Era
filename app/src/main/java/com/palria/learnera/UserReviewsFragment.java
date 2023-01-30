package com.palria.learnera;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserReviewsFragment extends Fragment {
    public UserReviewsFragment() {
        // Required empty public constructor
    }
LinearLayout containerLinearLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_user_reviews, container, false);
        initUI(parentView);
        fetchReviews(new ReviewFetchListener() {
            @Override
            public void onSuccess(String reviewerId, String comment, String dateReviewed, long starLevel) {
                displayReview(reviewerId,comment ,dateReviewed,starLevel);
            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });
        return parentView;
    }


    private void initUI(View parentView){
        //use the parentView to find the by Id as in : parentView.findViewById(...);

    }

    private  void fetchReviews(ReviewFetchListener reviewFetchListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(GlobalConfig.getCurrentUserId())
                .collection(GlobalConfig.REVIEWS_KEY)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    reviewFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                           final String reviewerId = documentSnapshot.getId();
                           final String comment = ""+ documentSnapshot.get(GlobalConfig.REVIEW_COMMENT_KEY);
                           final String dateReviewed = ""+ documentSnapshot.get(GlobalConfig.DATE_REVIEWED_KEY);
                           final long starLevel = documentSnapshot.get(GlobalConfig.STAR_LEVEL_KEY)!=null ?  documentSnapshot.getLong(GlobalConfig.STAR_LEVEL_KEY) : 0;

                           reviewFetchListener.onSuccess(reviewerId,comment ,dateReviewed,starLevel);
                        }
                    }
                });
    }

    private void displayReview(final String reviewerId,final String comment ,final String dateReviewed,final long starLevel){
        //<Uncomment and implement>
        /*
        if(getContext() != null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View reviewView = layoutInflater.inflate(R.layout.review_custom_view, null);
            TextView dateReviewedTextView = reviewView.findViewById(R.id.dateReviewedTextViewId);
            TextView commentTextView = reviewView.findViewById(R.id.commentTextViewId);
            TextView reviewerNameTextView = reviewView.findViewById(R.id.reviewerNameTextViewId);
            ImageView reviewerProfilePhoto = reviewView.findViewById(R.id.reviewerProfilePhotoId);
            dateReviewedTextView.setText(dateReviewed);
            commentTextView.setText(comment);
            //get the reviewer's name and profile photo
            GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(reviewerId)
                    .collection(GlobalConfig.USER_PROFILE_KEY)
                    .document(reviewerId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String reviewerName =""+ documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                            String reviewerProfilePhotoDownloadUrl =""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
                            reviewerNameTextView.setText(reviewerName);
                            Glide.with(getContext())
                                    .load(reviewerProfilePhotoDownloadUrl)
                                    .centerCrop()
                                    .into(reviewerProfilePhoto);
                        }
                    });

            containerLinearLayout.addView(reviewView);
        }
        */
    }

    interface ReviewFetchListener{
        void onSuccess(final String reviewerId,final String comment ,final String dateReviewed,final long starLevel);
                void onFailed(String errorMessage);
    }

}