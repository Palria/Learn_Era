package com.palria.learnera;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
        boolean isPagination = false;
        String tutorialId = "";
        String folderId = "";
        String authorId = "";
        long numberOfPagesCreated = 0;
ArrayList<PageDataModel> pageDataModels=new ArrayList<>();
RecyclerView pagesRecyclerListView;
    PagesRcvAdapter adapter;
    TextView startPaginationTextView;
    Button paginateButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
if(getArguments()!= null){
    isTutorialPage = getArguments().getBoolean(GlobalConfig.IS_TUTORIAL_PAGE_KEY,true);
    isPagination = getArguments().getBoolean(GlobalConfig.IS_PAGINATION_KEY,false);
    tutorialId = getArguments().getString(GlobalConfig.TUTORIAL_ID_KEY);
    folderId = getArguments().getString(GlobalConfig.FOLDER_ID_KEY);
    authorId = getArguments().getString(GlobalConfig.AUTHOR_ID_KEY);
    numberOfPagesCreated = getArguments().getLong(GlobalConfig.TOTAL_NUMBER_OF_PAGES_CREATED_KEY,0);
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
//        if(isPagination){
//            startPaginationTextView.setVisibility(View.GONE);
//        paginateButton.setVisibility(View.VISIBLE);
//        }
if(!GlobalConfig.getCurrentUserId().equals(authorId)){
    startPaginationTextView.setVisibility(View.GONE);
}

        startPaginationTextView.append(Html.fromHtml("<html><a href='@null'>click to paginate</html>",Html.FROM_HTML_MODE_LEGACY));
        startPaginationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),PaginationActivity.class);
                intent.putExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,isTutorialPage);
                intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
                intent.putExtra(GlobalConfig.FOLDER_ID_KEY,folderId);
                startActivity(intent);
            }
        });
       return parentView;
    }

    private void initUi(View parentView) {

        pagesRecyclerListView=parentView.findViewById(R.id.pagesRecyclerListView);
        startPaginationTextView=parentView.findViewById(R.id.startPaginationTextViewId);
        paginateButton=parentView.findViewById(R.id.paginateButtonId);

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


        adapter = new PagesRcvAdapter(pageDataModels, getContext(), paginateButton, isTutorialPage,numberOfPagesCreated, isPagination, new PagesRcvAdapter.OnPaginationCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });

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
//            .orderBy(GlobalConfig.PAGE_NUMBER_KEY, Query.Direction.DESCENDING)
        }else{

            pageQuery =  GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                    .document(tutorialId)
                    .collection(GlobalConfig.ALL_FOLDERS_KEY)
                    .document(folderId)
                    .collection(GlobalConfig.ALL_FOLDER_PAGES_KEY);

//            .orderBy(GlobalConfig.PAGE_NUMBER_KEY, Query.Direction.DESCENDING)

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
                            long pageNumber  =  documentSnapshot.get(GlobalConfig.PAGE_NUMBER_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.PAGE_NUMBER_KEY): 0L;
                            String dateCreated  =  documentSnapshot.get(GlobalConfig.PAGE_DATE_CREATED_TIME_STAMP_KEY)!=null ? documentSnapshot.getTimestamp(GlobalConfig.PAGE_DATE_CREATED_TIME_STAMP_KEY).toDate()+"" : "Undefined";
                            if(dateCreated.length()>10){
                                dateCreated = dateCreated.substring(0,10);
                            }
                            fetchPageListener.onSuccess(new PageDataModel(pageTitle,"",coverPhotoDownloadUrl,authorId,pageId,tutorialId,folderId,dateCreated,totalViews,isTutorialPage,(int)pageNumber));
                        }
                        if(queryDocumentSnapshots.isEmpty()){
                                startPaginationTextView.setVisibility(View.GONE);
                                paginateButton.setVisibility(View.GONE);
                        }else{
                            if(isPagination){
                                startPaginationTextView.setVisibility(View.GONE);
                                paginateButton.setVisibility(View.VISIBLE);
                            }else{
                                startPaginationTextView.setVisibility(View.VISIBLE);
                            }

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