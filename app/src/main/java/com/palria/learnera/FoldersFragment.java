package com.palria.learnera;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.adapters.FolderRcvAdapter;
import com.palria.learnera.models.FolderDataModel;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class FoldersFragment extends Fragment {

   RecyclerView foldersRcv;
   ArrayList<FolderDataModel> folderDataModels = new ArrayList<>();
    FolderRcvAdapter folderRcvAdapter;
    LinearLayout loadingLayout;
    LinearLayout noDataFoundLayout;

    public FoldersFragment() {
        // Required empty public constructor
    }
    String tutorialId = "";
    String authorId = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            tutorialId = getArguments().getString(GlobalConfig.TUTORIAL_ID_KEY);
            authorId = getArguments().getString(GlobalConfig.AUTHOR_ID_KEY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_folders, container, false);


        initUi(view);


        fetchTutorialFolders(new FetchTutorialFolderListener() {
            @Override
            public void onSuccess(FolderDataModel folderDataModel) {

                folderDataModels.add(folderDataModel);
                folderRcvAdapter.notifyItemChanged(folderDataModels.size());

            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });

        return view;

    }

    private void toggleContentVisibility(boolean show){
        if(!show){
            loadingLayout.setVisibility(View.VISIBLE);
            foldersRcv.setVisibility(View.GONE);
        }else{
            loadingLayout.setVisibility(View.GONE);
            foldersRcv.setVisibility(View.VISIBLE);
        }
    }

    private void initUi(View view) {
        foldersRcv=view.findViewById(R.id.foldersRcv);
        noDataFoundLayout=view.findViewById(R.id.noDataFound);
        loadingLayout=view.findViewById(R.id.loadingLayout);
        toggleContentVisibility(false);

        //init oflder rcv here
//        folderDataModels.add(new FolderDataModel("id","id","Design principles","1 min ago",4l));
//        folderDataModels.add(new FolderDataModel("id","id","OOps Concept Advanced","45 min ago",05l));
//        folderDataModels.add(new FolderDataModel("id","id","Ui Guide 2023","1 hours ago",45l));
//        folderDataModels.add(new FolderDataModel("id","id","Domain Registration P","5 hours ago",7l));
//        folderDataModels.add(new FolderDataModel("id","id","Lambda Expression with Joy","1 day ago",80l));
//        folderDataModels.add(new FolderDataModel("id","id","Youtube Policy changes overview","1 week ago",99l));
//
//
        folderRcvAdapter= new FolderRcvAdapter(folderDataModels,getContext());

        foldersRcv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        foldersRcv.setHasFixedSize(false);
        foldersRcv.setAdapter(folderRcvAdapter);



    }


    private void fetchTutorialFolders(FetchTutorialFolderListener fetchTutorialFolderListener){
       Query folderQuery = GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                .document(tutorialId)
                .collection(GlobalConfig.ALL_FOLDERS_KEY);
        if(!GlobalConfig.getCurrentUserId().equals(authorId+"")){
            folderQuery.whereEqualTo(GlobalConfig.IS_PUBLIC_KEY,true);
        }


                folderQuery.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        fetchTutorialFolderListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size()==0){
                            noDataFoundLayout.setVisibility(View.VISIBLE);
                            TextView title = noDataFoundLayout.findViewById(R.id.title);
                            TextView body = noDataFoundLayout.findViewById(R.id.body);

                            title.setText("Nothing found.");
                            body.setText("No folders found.");
                        }
                        toggleContentVisibility(true);
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String folderId = documentSnapshot.getId();
                            String folderName  = ""+ documentSnapshot.get(GlobalConfig.FOLDER_NAME_KEY);
                            String authorId  = ""+ documentSnapshot.get(GlobalConfig.AUTHOR_ID_KEY);
                            String libraryId  = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_ID_KEY);
                            String dateCreated  = documentSnapshot.get(GlobalConfig.FOLDER_CREATED_DATE_TIME_STAMP_KEY)!=null ?documentSnapshot.getTimestamp(GlobalConfig.FOLDER_CREATED_DATE_TIME_STAMP_KEY).toDate() +""  :"Undefined";
                            boolean isPublic  =  documentSnapshot.get(GlobalConfig.IS_PUBLIC_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_PUBLIC_KEY): true;
                            long numOfViews  = documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_FOLDER_VISITOR_KEY)!=null ?documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_FOLDER_VISITOR_KEY) : 0L ;
                            if(dateCreated.length()>10){
                                dateCreated = dateCreated.substring(0,10);
                            }
                            long numOfPages  =  documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_PAGES_CREATED_KEY)!=null ?  documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_PAGES_CREATED_KEY) : 0L;
                            fetchTutorialFolderListener.onSuccess(new FolderDataModel(folderId,authorId,libraryId,tutorialId,folderName,dateCreated,numOfPages,numOfViews,isPublic));
                        }

                    }
                });
    }

    interface FetchTutorialFolderListener{
        void onSuccess(FolderDataModel folderDataModel);
        void onFailed(String errorMessage);
    }
}