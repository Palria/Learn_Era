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

public class AllTutorialPageFragment extends Fragment {


    public AllTutorialPageFragment() {
        // Required empty public constructor
    }

boolean isFolderPage = false;
String tutorialId = "";
String folderId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
if(getArguments()!= null){
    isFolderPage = getArguments().getBoolean(GlobalConfig.IS_FOLDER_PAGE_KEY);
    tutorialId = getArguments().getString(GlobalConfig.TUTORIAL_ID_KEY);
    folderId = getArguments().getString(GlobalConfig.FOLDER_ID_KEY);
}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View parentView = inflater.inflate(R.layout.fragment_all_tutorial_page, container, false);

       return parentView;
    }


    private void fetchFolderPages(FetchPageListener fetchPageListener){
        Query pageQuery = null;
        if(isFolderPage){
            pageQuery =  GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                    .document(tutorialId)
                    .collection(GlobalConfig.ALL_TUTORIAL_FOLDERS_KEY)
                    .document(folderId)
                    .collection(GlobalConfig.ALL_FOLDER_PAGES_KEY);

        }else{
            pageQuery =   GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                    .document(tutorialId)
                    .collection(GlobalConfig.ALL_TUTORIAL_PAGES_KEY);

        }


                pageQuery.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        fetchPageListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String pageId = documentSnapshot.getId();
                            String pageName  = ""+ documentSnapshot.get(GlobalConfig.PAGE_NAME_KEY);
                            fetchPageListener.onSuccess(pageId,pageName);
                        }

                    }
                });
    }

    interface FetchPageListener{
        void onSuccess(String pageId , String pageName);
        void onFailed(String errorMessage);
    }
}