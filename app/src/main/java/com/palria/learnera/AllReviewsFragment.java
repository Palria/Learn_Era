package com.palria.learnera;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AllReviewsFragment extends Fragment {

    public AllReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_all_reviews, container, false);
        initUI(parentView);
        fetchReviews(new ReviewFetchListener() {
            @Override
            public void onSuccess(String authorId, String libraryId, String tutorialId, String dateReviewed, String reviewComment, boolean isAuthorReview, boolean isLibraryReview, boolean isTutorialReview) {
                displayReviews( authorId, libraryId, tutorialId, dateReviewed, reviewComment, isAuthorReview,isLibraryReview, isTutorialReview);;
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

    private void fetchReviews(ReviewFetchListener reviewFetchListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(GlobalConfig.getCurrentUserId())
                .collection(GlobalConfig.OTHER_REVIEWS_KEY)
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
                            String authorId = ""+ documentSnapshot.get(GlobalConfig.AUTHOR_ID_KEY);
                            String libraryId = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_ID_KEY);
                            String tutorialId = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_ID_KEY);
                            String reviewComment = ""+ documentSnapshot.get(GlobalConfig.REVIEW_COMMENT_KEY);
                            String dateReviewed = ""+ documentSnapshot.get(GlobalConfig.DATE_REVIEWED_KEY);

                            final boolean isAuthorReview= (documentSnapshot.get(GlobalConfig.IS_AUTHOR_REVIEW_KEY))!=null ? (documentSnapshot.getBoolean(GlobalConfig.IS_AUTHOR_REVIEW_KEY))  :false;
                            final boolean isLibraryReview= (documentSnapshot.get(GlobalConfig.IS_LIBRARY_REVIEW_KEY))!=null ? (documentSnapshot.getBoolean(GlobalConfig.IS_LIBRARY_REVIEW_KEY))  :false;
                            final boolean isTutorialReview = (documentSnapshot.get(GlobalConfig.IS_TUTORIAL_REVIEW_KEY))!=null ? (documentSnapshot.getBoolean(GlobalConfig.IS_TUTORIAL_REVIEW_KEY))  :false;

                            reviewFetchListener.onSuccess( authorId, libraryId, tutorialId, dateReviewed, reviewComment, isAuthorReview,isLibraryReview, isTutorialReview);
                        }
                    }
                });
    }

    private void displayReviews(String authorId,String libraryId,String tutorialId,String dateReviewed,String reviewComment,final boolean isAuthorReview,final boolean isLibraryReview,final boolean isTutorialReview){
        //<Uncomment and implement>
        /*
        if(getContext() != null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View reviewView = layoutInflater.inflate(R.layout.all_review_custom_view, null);
            TextView viewTypeIndicatorTextView = reviewView.findViewById(R.id.viewTypeIndicatorTextViewId);
            TextView dateReviewedTextView = reviewView.findViewById(R.id.dateReviewedTextViewId);
            TextView titleTextView = reviewView.findViewById(R.id.titleTextViewId);
            TextView authorNameTextView = reviewView.findViewById(R.id.authorNameTextViewId);
            TextView reviewCommentTextView = reviewView.findViewById(R.id.reviewCommentTextViewId);
            ImageView coverPhotoImageView = reviewView.findViewById(R.id.coverPhotoImageViewId);
            reviewCommentTextView.setText(reviewComment);
            dateReviewedTextView.setText(dateReviewed);




             if(isLibraryReview){
                viewTypeIndicatorTextView.setText("Library");
                GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(authorId)
                        .collection(GlobalConfig.ALL_LIBRARY_KEY)
                        .document(libraryId)
                        .collection(GlobalConfig.LIBRARY_PROFILE_KEY)
                        .document(libraryId)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String libraryName =""+ documentSnapshot.get(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
                                String libraryCoverPhotoDownloadUrl =""+ documentSnapshot.get(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY);
                                titleTextView.setText(libraryName);
                                Glide.with(getContext())
                                        .load(libraryCoverPhotoDownloadUrl)
                                        .centerCrop()
                                        .into(coverPhotoImageView);
                            }
                        });
            }
            else if(isTutorialReview){
                viewTypeIndicatorTextView.setText("Tutorial");
                GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(authorId)
                        .collection(GlobalConfig.ALL_LIBRARY_KEY)
                        .document(libraryId)
                        .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                        .document(tutorialId)
                        .collection(GlobalConfig.TUTORIAL_PROFILE_KEY)
                        .document(tutorialId)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String tutorialName =""+ documentSnapshot.get(GlobalConfig.TUTORIAL_DISPLAY_NAME_KEY);
                                String tutorialCoverPhotoDownloadUrl =""+ documentSnapshot.get(GlobalConfig.TUTORIAL_COVER_PHOTO_DOWNLOAD_URL_KEY);
                                titleTextView.setText(tutorialName);
                                Glide.with(getContext())
                                        .load(tutorialCoverPhotoDownloadUrl)
                                        .centerCrop()
                                        .into(coverPhotoImageView);
                            }
                        });
            }
            GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(authorId)
                    .collection(GlobalConfig.USER_PROFILE_KEY)
                    .document(authorId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String authorName =""+ documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                            String authorProfilePhotoDownloadUrl =""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
                            if(isAuthorReview){
                                viewTypeIndicatorTextView.setText("Author");
                                titleTextView.setText(authorName);
                                Glide.with(getContext())
                                        .load(authorProfilePhotoDownloadUrl)
                                        .centerCrop()
                                        .into(coverPhotoImageView);
                            }else{
                                authorNameTextView.setText(authorName);
                            }

                        }
                    });
            containerLinearLayout.addView(reviewView);

        }
        */
    }


    interface ReviewFetchListener{
        void onSuccess(String authorId,String libraryId,String tutorialId,String dateReviewed,String reviewComment,final boolean isAuthorReview,final boolean isLibraryReview,final boolean isTutorialReview);
        void onFailed(String errorMessage);
    }

}