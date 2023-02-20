package com.palria.learnera;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class AllUsersFragment extends Fragment {
boolean isAuthorOpenType = true;

    public AllUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            isAuthorOpenType = getArguments().getBoolean(GlobalConfig.IS_AUTHOR_OPEN_TYPE_KEY,false);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_all_users, container, false);

        fetchUsers(new UserFetchListener() {
            @Override
            public void onSuccess(String userName, String userId, String userProfilePhotoDownloadUrl, long totalNumberOfLibrary) {

            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });

        return parentView;
    }


    private void fetchUsers(UserFetchListener userFetchListener){
//        Query authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).whereEqualTo(GlobalConfig.IS_USER_AUTHOR_KEY,true).whereArrayContains(GlobalConfig.AUTHOR_CATEGORY_TAG_ARRAY_KEY,categoryTag).orderBy(GlobalConfig.TOTAL_NUMBER_OF_USER_PROFILE_VISITORS_KEY);

        Query authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY);
        if(isAuthorOpenType){
            authorQuery = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).whereEqualTo(GlobalConfig.IS_USER_AUTHOR_KEY,true);
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
                            final String userProfilePhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
                            final long totalNumberOfLibrary = documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY)!= null ?documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY)  :0L ;
                            userFetchListener.onSuccess(userName,authorId,userProfilePhotoDownloadUrl,totalNumberOfLibrary);

                        }
                    }
                });
    }

    interface UserFetchListener{
        void onSuccess(final String userName, final String userId,final String userProfilePhotoDownloadUrl,final long totalNumberOfLibrary );
        void onFailed(String errorMessage);
    }

}