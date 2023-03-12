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
import com.palria.learnera.adapters.PagesRcvAdapter;
import com.palria.learnera.models.PageDataModel;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class AllTutorialPageFragment extends Fragment {

    public AllTutorialPageFragment() {
        // Required empty public constructor
    }

        boolean isTutorialPage = true;
        String tutorialId = "";
        String folderId = "";

ArrayList<PageDataModel> pageDataModels=new ArrayList<>();
RecyclerView pagesRecyclerListView;
    PagesRcvAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
if(getArguments()!= null){
    isTutorialPage = getArguments().getBoolean(GlobalConfig.IS_TUTORIAL_PAGE_KEY,true);
    tutorialId = getArguments().getString(GlobalConfig.TUTORIAL_ID_KEY);
    folderId = getArguments().getString(GlobalConfig.FOLDER_ID_KEY);
}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View parentView = inflater.inflate(R.layout.fragment_all_tutorial_page, container, false);
        initUi(parentView);
        fetchPages(new FetchPageListener() {
            @Override
            public void onSuccess(PageDataModel pageDataModel) {
                pageDataModels.add(pageDataModel);
                adapter.notifyItemChanged(pageDataModels.size());
            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });

       return parentView;
    }

    private void initUi(View parentView) {

        pagesRecyclerListView=parentView.findViewById(R.id.pagesRecyclerListView);
//
//        pageDataModels.add(new PageDataModel("How to hold","this is content",
//                "",
//                "author",
//                "pageId",
//                "tutorialId",
//                "folderId",
//                "1 hrs ago",true));
//        pageDataModels.add(new PageDataModel("How to hold","this is content",
//                "",
//                "author",
//                "pageId",
//                "tutorialId",
//                "folderId",
//                "1 hrs ago",true));
//        pageDataModels.add(new PageDataModel("How to hold","this is content",
//                "",
//                "author",
//                "pageId",
//                "tutorialId",
//                "folderId",
//                "1 hrs ago",true));
//        pageDataModels.add(new PageDataModel("How to hold","this is content",
//                "",
//                "author",
//                "pageId",
//                "tutorialId",
//                "folderId",
//                "1 hrs ago",true));


        adapter = new PagesRcvAdapter(pageDataModels,getContext());

//        pagesRecyclerListView.setHasFixedSize(true);
        pagesRecyclerListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        pagesRecyclerListView.setAdapter(adapter);

    }


    private void fetchPages(FetchPageListener fetchPageListener){
        Query pageQuery = null;
        if(isTutorialPage){
            pageQuery =   GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                    .document(tutorialId)
                    .collection(GlobalConfig.ALL_TUTORIAL_PAGES_KEY);
        }else{

            pageQuery =  GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                    .document(tutorialId)
                    .collection(GlobalConfig.ALL_FOLDERS_KEY)
                    .document(folderId)
                    .collection(GlobalConfig.ALL_FOLDER_PAGES_KEY);

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
                            String pageTitle  = ""+ documentSnapshot.get(GlobalConfig.PAGE_TITLE_KEY);
                            String authorId  = ""+ documentSnapshot.get(GlobalConfig.AUTHOR_ID_KEY);
                            String coverPhotoDownloadUrl  = ""+ documentSnapshot.get(GlobalConfig.PAGE_COVER_PHOTO_DOWNLOAD_URL_KEY);
                            long totalViews  =  documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_PAGE_VISITOR_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_PAGE_VISITOR_KEY): 0L;
                            String dateCreated  =  documentSnapshot.get(GlobalConfig.PAGE_DATE_CREATED_TIME_STAMP_KEY)!=null ? documentSnapshot.getTimestamp(GlobalConfig.PAGE_DATE_CREATED_TIME_STAMP_KEY).toDate()+"" : "Undefined";
                            if(dateCreated.length()>10){
                                dateCreated = dateCreated.substring(0,10);
                            }
                            fetchPageListener.onSuccess(new PageDataModel(pageTitle,"",coverPhotoDownloadUrl,authorId,pageId,tutorialId,folderId,dateCreated,totalViews,isTutorialPage));
                        }

                    }
                });
    }

    interface FetchPageListener{
//        void onSuccess(String pageId , String pageName, String dateCreated);
        void onSuccess(PageDataModel pageDataModel);
        void onFailed(String errorMessage);
    }
}