package com.palria.learnera;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.adapters.RatingItemRecyclerViewAdapter;
import com.palria.learnera.models.RatingDataModel;

import java.util.ArrayList;

public class UserReviewsFragment extends Fragment {
    public UserReviewsFragment(){
        // Required empty public constructor
    }
    LinearLayout containerLinearLayout;
    RecyclerView recyclerView;
    RatingItemRecyclerViewAdapter ratingItemRecyclerViewAdapter;
    ArrayList<RatingDataModel> ratingDataModels = new ArrayList<>();
    View noDataFound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_user_reviews, container, false);
        initUI(parentView);
        fetchReviews(new ReviewFetchListener() {
            @Override
            public void onSuccess(RatingDataModel ratingDataModel) {
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

        //add test
//        ratingDataModels.add(new RatingDataModel(
//                "sad",
//                "Anish",
//                "Anish Magar",
//                "Author",
//                "10 Mar",
//                "Hello dami cha hai.",
//                4,
//                "null"
//        ));
//        ratingDataModels.add(new RatingDataModel(
//                "sad",
//                "Prajwal",
//                "Prajwal",
//                "Author",
//                "15 Mar",
//                "There is a quiet issue that helps me .",
//                5,
//                "null"
//        ));
        noDataFound=parentView.findViewById(R.id.noDataFound);

        ratingItemRecyclerViewAdapter = new RatingItemRecyclerViewAdapter(ratingDataModels,getContext());
        recyclerView = parentView.findViewById(R.id.ratingsRecyclerListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL ,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ratingItemRecyclerViewAdapter);
        //show not found if no datas



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
                           String dateReviewed =  documentSnapshot.get(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY)!=null ? documentSnapshot.getTimestamp(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY).toDate()+"" : "Undefined";
                           if(dateReviewed.length()>10){
                               dateReviewed = dateReviewed.substring(0,10);
                           }
                           final String finalDateReviewed = dateReviewed;
                           final long starLevel = documentSnapshot.get(GlobalConfig.STAR_LEVEL_KEY)!=null ?  documentSnapshot.getLong(GlobalConfig.STAR_LEVEL_KEY) : 0L;
                            GlobalConfig.getFirebaseFirestoreInstance()
                                    .collection(GlobalConfig.ALL_USERS_KEY)
                                    .document(reviewerId)
                                    .get()
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            reviewFetchListener.onSuccess(new RatingDataModel(reviewerId,"Error",GlobalConfig.getCurrentUserId(),"user",finalDateReviewed,comment, (int) starLevel,"Error"));

                                        }
                                    })
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            String userName = ""+ documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                                            String userProfilePhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
                                            reviewFetchListener.onSuccess(new RatingDataModel(reviewerId,userName,GlobalConfig.getCurrentUserId(),"user",finalDateReviewed,comment, (int) starLevel,userProfilePhotoDownloadUrl));

                                        }
                                    });
//                           reviewFetchListener.onSuccess(reviewerId,comment ,dateReviewed,starLevel);
                        }
                        if(queryDocumentSnapshots.size()==0){
                            noDataFound.setVisibility(View.VISIBLE);
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
//        void onSuccess(final String reviewerId,final String comment ,final String dateReviewed,final long starLevel);
        void onSuccess(RatingDataModel ratingDataModel);
        void onFailed(String errorMessage);
    }

}