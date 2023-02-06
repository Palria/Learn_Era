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
import com.google.firebase.firestore.QuerySnapshot;

public class AllTutorialFoldersFragment extends Fragment {

    public AllTutorialFoldersFragment() {
        // Required empty public constructor
    }
String tutorialId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            tutorialId = getArguments().getString(GlobalConfig.TUTORIAL_ID_KEY);
        }

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View parentView = inflater.inflate(R.layout.fragment_all_tutorial_folders, container, false);
        initUI();
        fetchTutorialFolders(new FetchTutorialFolderListener() {
            @Override
            public void onSuccess(String folderId, String folderName) {

            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });
      return parentView;
    }

    private void initUI(){

    }

    private void fetchTutorialFolders(FetchTutorialFolderListener fetchTutorialFolderListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                .document(tutorialId)
                .collection(GlobalConfig.ALL_TUTORIAL_FOLDERS_KEY)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        fetchTutorialFolderListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String folderId = documentSnapshot.getId();
                            String folderName  = ""+ documentSnapshot.get(GlobalConfig.FOLDER_NAME_KEY);
                            fetchTutorialFolderListener.onSuccess(folderId,folderName);
                        }

                    }
                });
    }

    interface FetchTutorialFolderListener{
        void onSuccess(String folderId , String folderName);
        void onFailed(String errorMessage);
    }
}