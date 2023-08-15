package com.palria.learnera;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.adapters.UsersRCVAdapter;
import com.palria.learnera.models.UsersDataModel;

import java.util.ArrayList;

public class AllUsersFragment extends Fragment {
boolean isAuthorOpenType = true;
UsersRCVAdapter usersRCVAdapter;
ArrayList<UsersDataModel> usersDataModelArrayList = new ArrayList<>();
RecyclerView recyclerView;


    boolean isFromSearchContext = false;
    boolean isVerification = false;
    String searchKeyword = "";

    public AllUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            isAuthorOpenType = getArguments().getBoolean(GlobalConfig.IS_AUTHOR_OPEN_TYPE_KEY,false);
            isVerification = getArguments().getBoolean(GlobalConfig.IS_ACCOUNT_VERIFICATION_KEY,false);
            isFromSearchContext = getArguments().getBoolean(GlobalConfig.IS_FROM_SEARCH_CONTEXT_KEY,false);
            searchKeyword = getArguments().getString(GlobalConfig.SEARCH_KEYWORD_KEY,"");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_all_users, container, false);
        initUI(parentView);
        fetchUsers(new UserFetchListener() {
            @Override
            public void onSuccess(UsersDataModel usersDataModel) {

                usersDataModelArrayList.add(usersDataModel);
                usersRCVAdapter.notifyItemChanged(usersDataModelArrayList.size());

            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });

        return parentView;
    }
//
private void initUI(View parentView){
        recyclerView = parentView.findViewById(R.id.usersRecyclerListViewId);
        usersRCVAdapter = new UsersRCVAdapter(usersDataModelArrayList,getContext(),isVerification);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
    recyclerView.setAdapter(usersRCVAdapter);
    recyclerView.setLayoutManager(layoutManager);


}
    private void fetchUsers(UserFetchListener userFetchListener){
//        Query authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).whereEqualTo(GlobalConfig.IS_USER_AUTHOR_KEY,true).whereArrayContains(GlobalConfig.AUTHOR_CATEGORY_TAG_ARRAY_KEY,categoryTag).orderBy(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_VISITORS_KEY);

        Query authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY);
        if(isAuthorOpenType){
//            authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).whereEqualTo(GlobalConfig.IS_USER_AUTHOR_KEY,true);
            authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY);
            if(isFromSearchContext){
//                authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).whereEqualTo(GlobalConfig.IS_USER_AUTHOR_KEY,true).whereArrayContains(GlobalConfig.USER_SEARCH_ANY_MATCH_KEYWORD_KEY,searchKeyword);
                authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).whereArrayContains(GlobalConfig.USER_SEARCH_ANY_MATCH_KEYWORD_KEY,searchKeyword);
            }
        }

        else if(isVerification){
            authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).whereEqualTo(GlobalConfig.IS_SUBMITTED_FOR_VERIFICATION_KEY,true);
            if(isFromSearchContext){
                authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).whereEqualTo(GlobalConfig.IS_SUBMITTED_FOR_VERIFICATION_KEY,true).whereArrayContains(GlobalConfig.USER_SEARCH_ANY_MATCH_KEYWORD_KEY,searchKeyword);
            }
        }
        else if (isFromSearchContext){
                authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).whereArrayContains(GlobalConfig.USER_SEARCH_ANY_MATCH_KEYWORD_KEY,searchKeyword);
        }


            authorQuery.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        userFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                            String authorId  = documentSnapshot.getId();
                            final String userName = ""+ documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                            final String description = ""+ documentSnapshot.get(GlobalConfig.USER_DESCRIPTION_KEY);
                            final String gender = ""+ documentSnapshot.get(GlobalConfig.USER_GENDER_TYPE_KEY);
                            final String userPhoneNumber = ""+ documentSnapshot.get(GlobalConfig.USER_CONTACT_PHONE_NUMBER_KEY);
                            final String userEmail = ""+ documentSnapshot.get(GlobalConfig.USER_CONTACT_EMAIL_ADDRESS_KEY);
                            final String userCountryOfOrigin = ""+ documentSnapshot.get(GlobalConfig.USER_COUNTRY_OF_RESIDENCE_KEY);
                            String dateRegistered =  documentSnapshot.get(GlobalConfig.USER_PROFILE_DATE_CREATED_TIME_STAMP_KEY)!=null ?  documentSnapshot.getTimestamp(GlobalConfig.USER_PROFILE_DATE_CREATED_TIME_STAMP_KEY).toDate()+""  :"Moments ago";
                            if(dateRegistered.length()>10){
                                dateRegistered = dateRegistered.substring(0,10);
                            }
                            final String finalDateRegistered = dateRegistered;
                            final boolean isAnAuthor =  documentSnapshot.get(GlobalConfig.IS_USER_AUTHOR_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_USER_AUTHOR_KEY) :false;
                            final boolean isAccountVerified =  documentSnapshot.get(GlobalConfig.IS_ACCOUNT_VERIFIED_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_ACCOUNT_VERIFIED_KEY) :false;
                            final String userProfilePhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
                            final long totalNumberOfLibrary = documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY)!= null ?documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY)  :0L ;
                            long totalNumberOfProfileVisitor = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_VISITORS_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_VISITORS_KEY) : 0L;
                            long totalNumberOfProfileReach = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_REACH_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_REACH_KEY) : 0L;
                            long totalNumberOfOneStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY) : 0L;
                            long totalNumberOfTwoStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY) : 0L;
                            long totalNumberOfThreeStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY) : 0L;
                            long totalNumberOfFourStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY) : 0L;
                            long totalNumberOfFiveStarRate = (documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) != null) ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY) : 0L;


                            userFetchListener.onSuccess(new UsersDataModel(
                                     userName,
                                     description,
                                     authorId,
                                     userProfilePhotoDownloadUrl,
                                     (int)totalNumberOfLibrary,
                                     isAnAuthor,
                                    isAccountVerified,
                                     gender,
                                    finalDateRegistered,
                                     userPhoneNumber,
                                     userEmail,
                                     userCountryOfOrigin,
                                    (int)totalNumberOfOneStarRate,
                                    (int)totalNumberOfTwoStarRate,
                                    (int)totalNumberOfThreeStarRate,
                                    (int)totalNumberOfFourStarRate,
                                    (int)totalNumberOfFiveStarRate
//            DocumentSnapshot userDocumentSnapshot


    ));

                        }
                    }
                });
    }

    interface UserFetchListener{
        void onSuccess(UsersDataModel usersDataModel);
        void onFailed(String errorMessage);
    }

}