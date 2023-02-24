package com.palria.learnera;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.adapters.RatingItemRecyclerViewAdapter;
import com.palria.learnera.models.RatingDataModel;

import java.util.ArrayList;

public class AllUserReviewsFragment extends Fragment {
    String userId;
    RecyclerView recyclerView;
    RatingItemRecyclerViewAdapter ratingItemRecyclerViewAdapter;
    ArrayList<RatingDataModel> ratingDataModels = new ArrayList<>();
    public AllUserReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){
            userId = getArguments().getString(GlobalConfig.USER_ID_KEY,"0");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_all_user_reviews, container, false);
        initUI(parentView);
        fetchReviews(new ReviewFetchListener() {
            @Override
            public void onSuccess(RatingDataModel ratingDataModel) {
//                displayReviews( authorId, libraryId, tutorialId, dateReviewed, reviewComment, starLevel, isAuthorReview,isLibraryReview, isTutorialReview);;

                ratingDataModels.add(ratingDataModel);
                ratingItemRecyclerViewAdapter.notifyItemChanged(ratingDataModels.size());
            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });

        return parentView;
    }


    private void initUI(View parentView){
        //use the parentView to find the  Id as in : parentView.findViewById(...);
        ratingItemRecyclerViewAdapter = new RatingItemRecyclerViewAdapter(ratingDataModels,getContext());
        recyclerView = parentView.findViewById(R.id.ratingsRecyclerListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL ,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ratingItemRecyclerViewAdapter);

    }


    private void fetchReviews(ReviewFetchListener reviewFetchListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(userId)
                .collection(GlobalConfig.OTHER_REVIEWS_KEY)
                .whereEqualTo(GlobalConfig.IS_AUTHOR_REVIEW_KEY,true)
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
                            long starLevel =  documentSnapshot.get(GlobalConfig.STAR_LEVEL_KEY)!=null && documentSnapshot.get(GlobalConfig.STAR_LEVEL_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.STAR_LEVEL_KEY) : 0L;

                            String dateReviewed = documentSnapshot.get(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY) != null && documentSnapshot.get(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY) instanceof Timestamp ? documentSnapshot.getTimestamp(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY).toDate().toString() : "Undefined";
                            if(dateReviewed.length()>10){
                                dateReviewed = dateReviewed.substring(0,10);
                            }
                            final String finalDateReviewed = dateReviewed;

                            final boolean isAuthorReview= (documentSnapshot.get(GlobalConfig.IS_AUTHOR_REVIEW_KEY))!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_AUTHOR_REVIEW_KEY)  :false;
                            final boolean isLibraryReview= (documentSnapshot.get(GlobalConfig.IS_LIBRARY_REVIEW_KEY))!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_LIBRARY_REVIEW_KEY)  :false;
                            final boolean isTutorialReview = (documentSnapshot.get(GlobalConfig.IS_TUTORIAL_REVIEW_KEY))!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_TUTORIAL_REVIEW_KEY)  :false;

                                GlobalConfig.getFirebaseFirestoreInstance()
                                        .collection(GlobalConfig.ALL_USERS_KEY)
                                        .document(authorId)
                                        .get()
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                reviewFetchListener.onSuccess(new RatingDataModel(GlobalConfig.getCurrentUserId(), "Error", authorId, "user", finalDateReviewed, reviewComment, (int) starLevel, "Error"));

                                            }
                                        })
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                String userName = "" + documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                                                String userProfilePhotoDownloadUrl = "" + documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
                                                reviewFetchListener.onSuccess(new RatingDataModel(GlobalConfig.getCurrentUserId(), userName, authorId, GlobalConfig.AUTHOR_TYPE_KEY, finalDateReviewed, reviewComment, (int) starLevel, userProfilePhotoDownloadUrl));

                                            }
                                        });

//                            reviewFetchListener.onSuccess( authorId, libraryId, tutorialId, dateReviewed, reviewComment, starLevel, isAuthorReview,isLibraryReview, isTutorialReview);
                        }
                    }
                });
    }



    interface ReviewFetchListener{
        //        void onSuccess(String authorId,String libraryId,String tutorialId,String dateReviewed,String reviewComment,long starLevel,final boolean isAuthorReview,final boolean isLibraryReview,final boolean isTutorialReview);
        void onSuccess(RatingDataModel ratingDataModel);
        void onFailed(String errorMessage);
    }

}