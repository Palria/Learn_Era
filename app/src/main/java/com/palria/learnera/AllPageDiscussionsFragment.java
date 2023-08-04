package com.palria.learnera;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.adapters.PageDiscussionRcvAdapter;
import com.palria.learnera.adapters.PagesRcvAdapter;
import com.palria.learnera.models.PageDataModel;
import com.palria.learnera.models.PageDiscussionDataModel;
import com.palria.learnera.widgets.BottomSheetFormBuilderWidget;

import java.util.ArrayList;

public class AllPageDiscussionsFragment extends Fragment {

    LinearLayout featuredItemLayoutContainer;
    LinearLayout noDataFound;
    LinearLayout loadingLayout;
    View dummyView;
    public AllPageDiscussionsFragment() {
        // Required empty public constructor
    }

        boolean isForPreview = false;
        boolean isFromPageContext = true;
        boolean isViewAllDiscussionsForSinglePage = false;
        boolean isViewSingleDiscussionReplies = false;
        boolean isTutorialPage = true;
        String authorId = "";
        String tutorialId = "";
        String folderId = "";
        String pageId = "";
        String parentDiscussionId = "";
//        String discussionId = "";
ArrayList<PageDiscussionDataModel> pageDiscussionDataModels=new ArrayList<>();
RecyclerView pagesDiscussionRecyclerListView;
    PageDiscussionRcvAdapter adapter;
    Button discussButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
if(getArguments()!= null){
    isForPreview = getArguments().getBoolean(GlobalConfig.IS_FOR_PREVIEW_KEY,false);
    isFromPageContext = getArguments().getBoolean(GlobalConfig.IS_FROM_PAGE_CONTEXT_KEY,false);
    isViewAllDiscussionsForSinglePage = getArguments().getBoolean(GlobalConfig.IS_VIEW_ALL_DISCUSSIONS_FOR_SINGLE_PAGE_KEY,false);
    isViewSingleDiscussionReplies = getArguments().getBoolean(GlobalConfig.IS_VIEW_SINGLE_DISCUSSION_REPLY_KEY,false);
    isTutorialPage = getArguments().getBoolean(GlobalConfig.IS_TUTORIAL_PAGE_KEY,false);
    authorId = getArguments().getString(GlobalConfig.AUTHOR_ID_KEY);
    tutorialId = getArguments().getString(GlobalConfig.TUTORIAL_ID_KEY);
    folderId = getArguments().getString(GlobalConfig.FOLDER_ID_KEY);
    pageId = getArguments().getString(GlobalConfig.PAGE_ID_KEY);
    parentDiscussionId = getArguments().getString(GlobalConfig.PARENT_DISCUSSION_ID_KEY);
//    discussionId = getArguments().getString(GlobalConfig.DISCUSSION_ID_KEY);
}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View parentView = inflater.inflate(R.layout.fragment_all_page_discussions, container, false);
        initUi(parentView);
        fetchPageDiscussions(new FetchPageDiscussionListener() {
            @Override
            public void onSuccess(PageDiscussionDataModel pageDiscussionDataModel) {
                pageDiscussionDataModels.add(pageDiscussionDataModel);
                adapter.notifyItemChanged(pageDiscussionDataModels.size());
            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });

        if(isFromPageContext || isForPreview){
            noDataFound.setVisibility(View.GONE);
            discussButton.setVisibility(View.GONE);
            dummyView.setVisibility(View.GONE);
//            Toast.makeText(getContext(), isForPreview+" checking " +parentDiscussionId, Toast.LENGTH_SHORT).show();
        }


        discussButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiscussionForm();
            }
        });
        if(isViewAllDiscussionsForSinglePage){
            openPageFragment();

        }else if(isViewSingleDiscussionReplies){
            openDiscussionFragment();
        }
         return parentView;
    }

    private void toggleContentVisibility(boolean show){
        if(!show){
            loadingLayout.setVisibility(View.VISIBLE);
            pagesDiscussionRecyclerListView.setVisibility(View.GONE);
        }else{
            loadingLayout.setVisibility(View.GONE);
            pagesDiscussionRecyclerListView.setVisibility(View.VISIBLE);
        }
    }

    private void initUi(View parentView) {

        pagesDiscussionRecyclerListView=parentView.findViewById(R.id.pageDiscussionsRecyclerListViewId);
        discussButton=parentView.findViewById(R.id.discussButtonId);

        featuredItemLayoutContainer=parentView.findViewById(R.id.featuredItemLayoutContainerId);
        loadingLayout=parentView.findViewById(R.id.loadingLayout);
        noDataFound=parentView.findViewById(R.id.noDataFound);
        dummyView=parentView.findViewById(R.id.dummyViewId);
        toggleContentVisibility(false);
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


        adapter = new PageDiscussionRcvAdapter(pageDiscussionDataModels, getContext(),authorId,isTutorialPage);

//        pagesRecyclerListView.setHasFixedSize(true);
        pagesDiscussionRecyclerListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        pagesDiscussionRecyclerListView.setAdapter(adapter);

    }
    void showDiscussionForm(){
        new BottomSheetFormBuilderWidget(getContext())
                .setTitle("Join in discussion of this lesson by contributing your idea")
                .setPositiveTitle("Discuss")
                .addInputField(new BottomSheetFormBuilderWidget.EditTextInput(getContext())
                        .setHint("Enter your idea")
                        .autoFocus())
                .setOnSubmit(new BottomSheetFormBuilderWidget.OnSubmitHandler() {
                    @Override
                    public void onSubmit(String[] values) {
                        super.onSubmit(values);

//                         Toast.makeText(PageActivity.this, values[0], Toast.LENGTH_SHORT).show();
                        //values will be returned as array of strings as per input list position
                        //eg first added input has first value
                        String body = values[0];
                        if (body.trim().equals("")) {
                            //     leBottomSheetDialog.setTitle("Folder needs name, must enter name for the folder");

                            Toast.makeText(getContext(), "Please enter your idea",Toast.LENGTH_SHORT).show();

                        } else {

                            String discussionId = GlobalConfig.getRandomString(80);
                            GlobalConfig.createSnackBar(getContext(),discussButton, "Adding discussion: "+body, Snackbar.LENGTH_INDEFINITE);

                            GlobalConfig.discuss(new PageDiscussionDataModel(discussionId,GlobalConfig.getCurrentUserId(),body,"",pageId,authorId,"",tutorialId,folderId,isTutorialPage,false,false,false,false,"",0L,0L,new ArrayList(),new ArrayList()),new GlobalConfig.ActionCallback(){
                                @Override
                                public void onFailed(String errorMessage){
                                    Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();

                                }
                                @Override
                                public void onSuccess(){
//                                     Toast.makeText(PageActivity.this, body, Toast.LENGTH_SHORT).show();
                                    GlobalConfig.createSnackBar(getContext(),discussButton, "Thanks discussion added: "+body,Snackbar.LENGTH_SHORT);
//                                    int currentDiscussionCount = Integer.parseInt((discussionCountTextView.getText()+""));
//                                    discussionCountTextView.setText((currentDiscussionCount+1)+"");

                                }
                            });
//                             createNewFolder(values[0],isPublic[0]);
//                                           leBottomSheetDialog.setTitle("Creating "+values[0]+" folder in progress...");
//                                           leBottomSheetDialog.hide();
                        }
                        //create folder process here
                    }
                })
                .render()
                .show();
    }

    void openPageFragment(){
        AllTutorialPageFragment allTutorialPageFragment = new AllTutorialPageFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(GlobalConfig.IS_TUTORIAL_PAGE_KEY,isTutorialPage);
        bundle.putBoolean(GlobalConfig.IS_FOR_PREVIEW_KEY,true);
        bundle.putBoolean(GlobalConfig.IS_TUTORIAL_PAGE_KEY,isTutorialPage);
        bundle.putString(GlobalConfig.AUTHOR_ID_KEY,authorId);
        bundle.putString(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
        bundle.putString(GlobalConfig.FOLDER_ID_KEY,folderId);
        bundle.putString(GlobalConfig.PAGE_ID_KEY,pageId);
        allTutorialPageFragment.setArguments(bundle);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.featuredItemLayoutContainerId, allTutorialPageFragment)
                .commit();

    }

    void openDiscussionFragment(){
        AllPageDiscussionsFragment allDiscussionsFragment = new AllPageDiscussionsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(GlobalConfig.IS_FOR_PREVIEW_KEY,true);
        bundle.putBoolean(GlobalConfig.IS_TUTORIAL_PAGE_KEY,isTutorialPage);
        bundle.putString(GlobalConfig.AUTHOR_ID_KEY,authorId);
        bundle.putString(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
        bundle.putString(GlobalConfig.FOLDER_ID_KEY,folderId);
        bundle.putString(GlobalConfig.PAGE_ID_KEY,pageId);
        bundle.putString(GlobalConfig.PARENT_DISCUSSION_ID_KEY,parentDiscussionId);
        allDiscussionsFragment.setArguments(bundle);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.featuredItemLayoutContainerId, allDiscussionsFragment)
                .commit();
//        Toast.makeText(getContext(), isForPreview+" now opening fragment "+parentDiscussionId, Toast.LENGTH_SHORT).show();

    }


    private void fetchPageDiscussions(FetchPageDiscussionListener fetchPageDiscussionListener){
        Query pageQuery = null;
        if(isFromPageContext){
            pageQuery =   GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(authorId)
                    .collection(GlobalConfig.MY_PAGES_DISCUSSION_KEY)
                    .whereEqualTo(GlobalConfig.PAGE_ID_KEY,pageId)
                    .whereNotEqualTo(GlobalConfig.HAS_PARENT_DISCUSSION_KEY,true)
                    .orderBy(GlobalConfig.HAS_PARENT_DISCUSSION_KEY,Query.Direction.DESCENDING)
                    .orderBy(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY,Query.Direction.DESCENDING)
                    .limit(3L);
//            if(!GlobalConfig.getCurrentUserId().equals(authorId+"")){
//                pageQuery.whereEqualTo(GlobalConfig.IS_PUBLIC_KEY,true);
//
//            }
//            .orderBy(GlobalConfig.PAGE_NUMBER_KEY, Query.Direction.DESCENDING)
        }
       else if(isViewAllDiscussionsForSinglePage){
            pageQuery =   GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(authorId)
                    .collection(GlobalConfig.MY_PAGES_DISCUSSION_KEY)
                    .whereEqualTo(GlobalConfig.PAGE_ID_KEY,pageId)
                    .whereNotEqualTo(GlobalConfig.HAS_PARENT_DISCUSSION_KEY,true)
                    .orderBy(GlobalConfig.HAS_PARENT_DISCUSSION_KEY,Query.Direction.DESCENDING)
                    .orderBy(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY,Query.Direction.DESCENDING);
        }

        else if(isViewSingleDiscussionReplies){
            pageQuery =   GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(authorId)
                    .collection(GlobalConfig.MY_PAGES_DISCUSSION_KEY)
                    .whereEqualTo(GlobalConfig.PAGE_ID_KEY,pageId)
                    .whereEqualTo(GlobalConfig.PARENT_DISCUSSION_ID_KEY,parentDiscussionId)
                    .orderBy(GlobalConfig.PARENT_DISCUSSION_ID_KEY,Query.Direction.DESCENDING)
                    .orderBy(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY,Query.Direction.DESCENDING);
        }
        else if(isForPreview){
            pageQuery =   GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(authorId)
                    .collection(GlobalConfig.MY_PAGES_DISCUSSION_KEY)
                    .whereEqualTo(GlobalConfig.PAGE_ID_KEY,pageId)
                    .whereEqualTo(GlobalConfig.DISCUSSION_ID_KEY,parentDiscussionId)
                    .limit(1L);
//            Toast.makeText(getContext(), isForPreview+" trying to query "+parentDiscussionId, Toast.LENGTH_SHORT).show();

        }

        else{

            pageQuery =   GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(authorId)
                    .collection(GlobalConfig.MY_PAGES_DISCUSSION_KEY)
                    .whereNotEqualTo(GlobalConfig.PAGE_ID_KEY,pageId)
                    .orderBy(GlobalConfig.PAGE_ID_KEY,Query.Direction.ASCENDING)
                    .orderBy(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY,Query.Direction.DESCENDING)
                    .limit(100L);
if(!GlobalConfig.getCurrentUserId().equals(authorId+"")){
    pageQuery.whereEqualTo(GlobalConfig.IS_HIDDEN_BY_POSTER_KEY,false);
    pageQuery.whereEqualTo(GlobalConfig.IS_HIDDEN_BY_AUTHOR_KEY,false);

}

//            .orderBy(GlobalConfig.PAGE_NUMBER_KEY, Query.Direction.DESCENDING)

        }


                pageQuery.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        fetchPageDiscussionListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(queryDocumentSnapshots.size()==0){
                                noDataFound.setVisibility(View.VISIBLE);
                                TextView title = noDataFound.findViewById(R.id.title);
                                TextView body = noDataFound.findViewById(R.id.body);
                                title.setText("No Page Discussions ");
                                body.setText("There is no page discussions yet, click the add button to contribute your idea.");

//                                Toast.makeText(getContext(), isForPreview+"data not found "+parentDiscussionId, Toast.LENGTH_LONG).show();

                                if(isFromPageContext || isForPreview){
                                    noDataFound.setVisibility(View.GONE);
                                    discussButton.setVisibility(View.GONE);
                                }
                            }

                            toggleContentVisibility(true);
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String discussionId = documentSnapshot.getId();
                            String discussionPosterId  = ""+ documentSnapshot.get(GlobalConfig.DISCUSSION_POSTER_ID_KEY);
                            String description  = ""+ documentSnapshot.get(GlobalConfig.DISCUSSION_DESCRIPTION_KEY);
                            String coverPhotoDownloadUrl  = ""+ documentSnapshot.get(GlobalConfig.DISCUSSION_COVER_PHOTO_DOWNLOAD_URL_KEY);
                            String pageId  = ""+ documentSnapshot.get(GlobalConfig.PAGE_ID_KEY);
                            String parentDiscussionId  = ""+ documentSnapshot.get(GlobalConfig.PARENT_DISCUSSION_ID_KEY);
                            String tutorialId  = ""+ documentSnapshot.get(GlobalConfig.TUTORIAL_ID_KEY);
                            String folderId  = ""+ documentSnapshot.get(GlobalConfig.FOLDER_ID_KEY);
                            boolean isTutorialPage  =  documentSnapshot.get(GlobalConfig.IS_TUTORIAL_PAGE_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_TUTORIAL_PAGE_KEY): true;
                            boolean hasParentDiscussion  =  documentSnapshot.get(GlobalConfig.HAS_PARENT_DISCUSSION_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.HAS_PARENT_DISCUSSION_KEY): true;
                            boolean hasReplies  =  documentSnapshot.get(GlobalConfig.HAS_REPLIES_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.HAS_REPLIES_KEY): true;
                            boolean isHiddenByAuthor  =  documentSnapshot.get(GlobalConfig.IS_HIDDEN_BY_AUTHOR_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_HIDDEN_BY_AUTHOR_KEY): true;
                            boolean isHiddenByPoster  =  documentSnapshot.get(GlobalConfig.IS_HIDDEN_BY_POSTER_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_HIDDEN_BY_POSTER_KEY): true;
                            long totalReplies  =  documentSnapshot.get(GlobalConfig.TOTAL_REPLIES_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.TOTAL_REPLIES_KEY): 0L;
                            long totalLikes  =  documentSnapshot.get(GlobalConfig.TOTAL_LIKES_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.TOTAL_LIKES_KEY): 0L;
//                            long totalDisLikes  =  documentSnapshot.get(GlobalConfig.TOTAL_DISLIKES_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.TOTAL_DISLIKES_KEY): 0L;
                            ArrayList repliersIdList  =  documentSnapshot.get(GlobalConfig.REPLIERS_ID_LIST_KEY)!=null ? (ArrayList)documentSnapshot.get(GlobalConfig.REPLIERS_ID_LIST_KEY): new ArrayList();
                            ArrayList likersIdList  =  documentSnapshot.get(GlobalConfig.LIKERS_ID_LIST_KEY)!=null ? (ArrayList)documentSnapshot.get(GlobalConfig.LIKERS_ID_LIST_KEY): new ArrayList();
//                            ArrayList disLikersIdList  =  documentSnapshot.get(GlobalConfig.DISLIKERS_ID_LIST_KEY)!=null ? (ArrayList)documentSnapshot.get(GlobalConfig.DISLIKERS_ID_LIST_KEY): new ArrayList();
                            String dateCreated  =  documentSnapshot.get(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY)!=null ? documentSnapshot.getTimestamp(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY).toDate()+"" : "Moment ago";
                            if(dateCreated.length()>10){
                                dateCreated = dateCreated.substring(0,10);
//                                Toast.makeText(getContext(), isForPreview+" well data found well"+parentDiscussionId, Toast.LENGTH_SHORT).show();

                            }
                            fetchPageDiscussionListener.onSuccess(new PageDiscussionDataModel(
                                     discussionId,
                                     discussionPosterId,
                                     description,
                                    coverPhotoDownloadUrl,
                                     pageId,
                                     authorId,
                                     parentDiscussionId,
                                     tutorialId,
                                     folderId,
                                     isTutorialPage,
                                      hasParentDiscussion,
                                      hasReplies,
                                      isHiddenByAuthor,
                                      isHiddenByPoster,
                                     dateCreated,
                                     totalReplies,
                                     totalLikes,
//                                     totalDisLikes,
                                     repliersIdList,
                                     likersIdList
//                                     disLikersIdList
    ));


                        }

                    }
                });
    }

    interface FetchPageDiscussionListener{
//        void onSuccess(String pageId , String pageName, String dateCreated);
        void onSuccess(PageDiscussionDataModel pageDiscussionDataModel);
        void onFailed(String errorMessage);
    }
}

