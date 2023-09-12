package com.palria.learnera;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.adapters.AnswerRcvAdapter;
import com.palria.learnera.adapters.PageDiscussionRcvAdapter;
import com.palria.learnera.models.AnswerDataModel;
import com.palria.learnera.models.PageDiscussionDataModel;
import com.palria.learnera.widgets.BottomSheetFormBuilderWidget;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class AllAnswersFragment extends Fragment {

    LinearLayout featuredItemLayoutContainer;
    LinearLayout noDataFound;
    LinearLayout loadingLayout;
    View dummyView;
    public AllAnswersFragment() {
        // Required empty public constructor
    }
    String questionId;
    String answerId;
        boolean isForPreview = false;
        boolean isFromQuestionContext = true;
        boolean isViewAllAnswersForSingleQuestion = false;
        boolean isViewSingleAnswerReplies = false;
        String authorId = "";
    ArrayList<AnswerDataModel> answerDataModels=new ArrayList<>();
    RecyclerView answerRecyclerListView;
    AnswerRcvAdapter adapter;
    Button answerButton;
    AlertDialog alertDialog;
    public static AddAnswerListener addAnswerListener;
    BottomSheetFormBuilderWidget bottomSheetCatalogFormBuilderWidget;
    boolean isPhotoIncluded = false;
    ImageView answerPhoto;
    Uri galleryImageUri;
    /**
     * A  variable for launching the gallery {@link Intent}
     * */
    ActivityResultLauncher<Intent> openGalleryLauncher;

    /**
     * A  variable for launching the camera {@link Intent}
     * */
    ActivityResultLauncher<Intent> openCameraLauncher;
    int CAMERA_PERMISSION_REQUEST_CODE = 2002;
    int GALLERY_PERMISSION_REQUEST_CODE = 23;

    GlobalConfig.AnswerCallback answerCallback;
    AnswerDataModel answerDataModelForEdition;
    boolean isEdition=false;
    boolean isAnswer=true;
    String parentId = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
if(getArguments()!= null){
    isForPreview = getArguments().getBoolean(GlobalConfig.IS_FOR_PREVIEW_KEY,false);
    isFromQuestionContext = getArguments().getBoolean(GlobalConfig.IS_FROM_QUESTION_CONTEXT_KEY,false);
    isViewAllAnswersForSingleQuestion = getArguments().getBoolean(GlobalConfig.IS_VIEW_ALL_ANSWERS_FOR_SINGLE_QUESTION_KEY,false);
    isViewSingleAnswerReplies = getArguments().getBoolean(GlobalConfig.IS_VIEW_SINGLE_ANSWER_REPLY_KEY,false);
    authorId = getArguments().getString(GlobalConfig.AUTHOR_ID_KEY);
    answerId = getArguments().getString(GlobalConfig.ANSWER_ID_KEY);
    questionId = getArguments().getString(GlobalConfig.QUESTION_ID_KEY);
}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View parentView = inflater.inflate(R.layout.fragment_all_answers, container, false);
        initUi(parentView);
        fetchAnswers(new FetchAnswerListener() {
            @Override
            public void onSuccess(AnswerDataModel answerDataModel) {
                answerDataModels.add(answerDataModel);
                adapter.notifyItemChanged(answerDataModels.size());
            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });

        if(isFromQuestionContext || isForPreview){
            noDataFound.setVisibility(View.GONE);
//            answerButton.setVisibility(View.GONE);
            dummyView.setVisibility(View.GONE);
//            Toast.makeText(getContext(), isForPreview+" checking " +parentDiscussionId, Toast.LENGTH_SHORT).show();
        }


        openGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null) {
                    Intent data=result.getData();
                    galleryImageUri = data.getData();
//                        Picasso.get().load(galleryImageUri)
//                                .centerCrop()
//                                .into(pickImageActionButton);
                    Glide.with(getContext())
                            .load(galleryImageUri)
                            .centerCrop()
                            .into(answerPhoto);

                    isPhotoIncluded = true;


                }
            }
        });
        openCameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK) {

                    if (result.getData() != null) {
                        Intent data = result.getData();
                        Bitmap bitmapFromCamera =(Bitmap) data.getExtras().get("data");

                        if(bitmapFromCamera != null) {
                            Bitmap   cameraImageBitmap = bitmapFromCamera;
                            //coverPhotoImageView.setImageBitmap(cameraImageBitmap);
                            Glide.with(getContext())
                                    .load(cameraImageBitmap)
                                    .centerCrop()
                                    .into(answerPhoto);
                            isPhotoIncluded = true;
                        }

                    }
                }else{
                    Toast.makeText(getContext(), "No image captured!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(isViewSingleAnswerReplies) {
            openAnswerFragment(answerId);

        }
//        if(isViewAllDiscussionsForSinglePage){
//            openPageFragment();
//
//        }
//            if(isViewSingleAnswerReplies){
        /**listens to when user wants to add an answer*/
        addAnswerListener = new AddAnswerListener() {
            @Override
            public void onAddAnswerTriggered(AnswerDataModel answerDataModel1, String parentId1,boolean isEdition1,boolean isAnswer1) {
                answerDataModelForEdition = answerDataModel1;
                isEdition = isEdition1;
                isAnswer = isAnswer1;
                parentId = parentId1;


//                if(isFromQuestionContext) {
//                if(!isForPreview) {
                showAnswerForm();

//                }
//                }
            }
        };


//        }

        /**listens to  answer start point,media selection,completion,failure, and so on*/
        answerCallback = new GlobalConfig.AnswerCallback() {
            @Override
            public void onImageGallerySelected(ImageView imageView) {
                answerPhoto=imageView;
                openGallery();
            }

            @Override
            public void onCameraSelected(ImageView imageView) {
                answerPhoto=imageView;
                openCamera();
            }

            @Override
            public void onVideoGallery(View view) {

            }

            @Override
            public void onStart(String answerId) {
                toggleProgress(true);
            }

            @Override
            public void onSuccess(String answerId) {
                toggleProgress(false);
                answerId = GlobalConfig.getRandomString(60);
                bottomSheetCatalogFormBuilderWidget = GlobalConfig.getAnswerForm(getContext(), authorId, questionId,parentId, answerId, isEdition,isAnswer, answerDataModelForEdition, answerCallback);
                GlobalConfig.createSnackBar(getContext(), featuredItemLayoutContainer, "Thanks for your contribution,your answer was successfully posted, refresh and see your answer added", Snackbar.LENGTH_INDEFINITE);

            }

            @Override
            public void onFailed(String errorMessage) {
                toggleProgress(false);
                GlobalConfig.createSnackBar2(getContext(), featuredItemLayoutContainer, "Your answer failed to add, please try again", "Try again", Snackbar.LENGTH_INDEFINITE, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showAnswerForm();
                    }
                });
            }
        };
//        bottomSheetCatalogFormBuilderWidget = GlobalConfig.getAnswerForm(getContext(), authorId, questionId,parentId, newAnswerId, isEdition, isAnswer,answerDataModelForEdition, answerCallback);

        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        /**listens to when user wants to add an answer*/
        addAnswerListener = new AddAnswerListener() {
            @Override
            public void onAddAnswerTriggered(AnswerDataModel answerDataModel1, String parentId1,boolean isEdition1,boolean isAnswer1) {
                answerDataModelForEdition = answerDataModel1;
                isEdition = isEdition1;
                isAnswer = isAnswer1;
                parentId = parentId1;


                showAnswerForm();

            }
        };
    }

    private void toggleProgress(boolean show)
    {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.hide();
        }
    }

    private void toggleContentVisibility(boolean show){
        if(!show){
            loadingLayout.setVisibility(View.VISIBLE);
            answerRecyclerListView.setVisibility(View.GONE);
        }else{
            loadingLayout.setVisibility(View.GONE);
            answerRecyclerListView.setVisibility(View.VISIBLE);
        }
    }

    private void initUi(View parentView) {

        answerRecyclerListView=parentView.findViewById(R.id.answerRecyclerListViewId);

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


        adapter = new AnswerRcvAdapter(answerDataModels, getContext(),authorId);

//        pagesRecyclerListView.setHasFixedSize(true);
        answerRecyclerListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        answerRecyclerListView.setAdapter(adapter);


        alertDialog = new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();

    }
    void showAnswerForm(){
        String newAnswerId = GlobalConfig.getRandomString(60);
        bottomSheetCatalogFormBuilderWidget = GlobalConfig.getAnswerForm(getContext(), authorId, questionId,parentId, newAnswerId, isEdition, isAnswer,answerDataModelForEdition, answerCallback);

        bottomSheetCatalogFormBuilderWidget.show();
    }


    void openAnswerFragment(String answerId){
        AllAnswersFragment allAnswersFragment = new AllAnswersFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(GlobalConfig.IS_FOR_PREVIEW_KEY,true);
        bundle.putString(GlobalConfig.AUTHOR_ID_KEY,authorId);
        bundle.putString(GlobalConfig.ANSWER_ID_KEY,answerId);
        bundle.putString(GlobalConfig.QUESTION_ID_KEY,questionId);
        allAnswersFragment.setArguments(bundle);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.featuredItemLayoutContainerId, allAnswersFragment)
                .commit();
//        Toast.makeText(getContext(), isForPreview+" now opening fragment "+parentDiscussionId, Toast.LENGTH_SHORT).show();

    }


    private void fetchAnswers(FetchAnswerListener answerListener){
//        if(true)return ;

        Query pageQuery = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_QUESTIONS_KEY)
                        .document(questionId)
                        .collection(GlobalConfig.ALL_ANSWERS_KEY)
                        .orderBy(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY,Query.Direction.DESCENDING);
        if(isFromQuestionContext){
            pageQuery =   GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_QUESTIONS_KEY)
                    .document(questionId)
                    .collection(GlobalConfig.ALL_ANSWERS_KEY)
                    .whereEqualTo(GlobalConfig.IS_ANSWER_KEY,true);
//                    .orderBy(GlobalConfig.IS_ANSWER_KEY,Query.Direction.DESCENDING)
//                    .orderBy(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY,Query.Direction.DESCENDING);
//            if(!GlobalConfig.getCurrentUserId().equals(authorId+"")){
//                pageQuery.whereEqualTo(GlobalConfig.IS_PUBLIC_KEY,true);
//
//            }
//            .orderBy(GlobalConfig.PAGE_NUMBER_KEY, Query.Direction.DESCENDING)
        }
//       else if(isViewAllAnswersForSingleQuestion){
//            pageQuery =   GlobalConfig.getFirebaseFirestoreInstance()
//                    .collection(GlobalConfig.ALL_USERS_KEY)
//                    .document(authorId)
//                    .collection(GlobalConfig.MY_PAGES_DISCUSSION_KEY)
//                    .whereEqualTo(GlobalConfig.PAGE_ID_KEY,pageId)
//                    .whereNotEqualTo(GlobalConfig.HAS_PARENT_DISCUSSION_KEY,true)
//                    .orderBy(GlobalConfig.HAS_PARENT_DISCUSSION_KEY,Query.Direction.DESCENDING)
//                    .orderBy(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY,Query.Direction.DESCENDING);
//        }

        else if(isViewSingleAnswerReplies){
            pageQuery =   GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_QUESTIONS_KEY)
                    .document(questionId)
                    .collection(GlobalConfig.ALL_ANSWERS_KEY)
                    .whereEqualTo(GlobalConfig.IS_ANSWER_KEY,false)
                    .whereEqualTo(GlobalConfig.PARENT_ID_KEY,answerId);
//                    .orderBy(GlobalConfig.IS_ANSWER_KEY,Query.Direction.DESCENDING)
//                    .orderBy(GlobalConfig.PARENT_ID_KEY,Query.Direction.DESCENDING)
//                    .orderBy(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY,Query.Direction.DESCENDING);
        }
        else if(isForPreview){
            pageQuery =   GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_QUESTIONS_KEY)
                .document(questionId)
                .collection(GlobalConfig.ALL_ANSWERS_KEY)
                    .whereEqualTo(GlobalConfig.ANSWER_ID_KEY,answerId)
                    .limit(1L);
//            Toast.makeText(getContext(), isForPreview+" trying to query "+parentDiscussionId, Toast.LENGTH_SHORT).show();

        }

        else{
                        Toast.makeText(getContext(), "do sth", Toast.LENGTH_SHORT).show();

        }


                pageQuery.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        answerListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(queryDocumentSnapshots.size()==0){
                                noDataFound.setVisibility(View.VISIBLE);
                                TextView title = noDataFound.findViewById(R.id.title);
                                TextView body = noDataFound.findViewById(R.id.body);
                                title.setText("No Replies ");
                                body.setText("There is no reply yet, be the first to reply this answer.");

//                                Toast.makeText(getContext(), isForPreview+"data not found "+parentDiscussionId, Toast.LENGTH_LONG).show();

                                if(isFromQuestionContext || isForPreview){
                                    noDataFound.setVisibility(View.GONE);
                                    //answerButton.setVisibility(View.GONE);
                                }
                            }

                            toggleContentVisibility(true);
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String answerId = documentSnapshot.getId();
                            String contributorId  = ""+ documentSnapshot.get(GlobalConfig.CONTRIBUTOR_ID_KEY);
                            String parentId  = ""+ documentSnapshot.get(GlobalConfig.PARENT_ID_KEY);
                            String answer  = ""+ documentSnapshot.get(GlobalConfig.ANSWER_BODY_KEY);
                            String answerPhotoDownloadUrl  = ""+ documentSnapshot.get(GlobalConfig.ANSWER_PHOTO_DOWNLOAD_URL_KEY);

                            boolean hasParent  =  documentSnapshot.get(GlobalConfig.HAS_PARENT_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.HAS_PARENT_KEY): true;
                            boolean isAnswer  =  documentSnapshot.get(GlobalConfig.IS_ANSWER_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_ANSWER_KEY): true;
                            boolean hasReplies  =  documentSnapshot.get(GlobalConfig.HAS_REPLIES_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.HAS_REPLIES_KEY): true;
                            boolean isPhotoIncluded  =  documentSnapshot.get(GlobalConfig.IS_PHOTO_INCLUDED_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_PHOTO_INCLUDED_KEY): false;
                            boolean isHiddenByAuthor  =  documentSnapshot.get(GlobalConfig.IS_HIDDEN_BY_AUTHOR_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_HIDDEN_BY_AUTHOR_KEY): false;
                            boolean isHiddenByContributor  =  documentSnapshot.get(GlobalConfig.IS_HIDDEN_BY_CONTRIBUTOR_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_HIDDEN_BY_CONTRIBUTOR_KEY): false;
                            long  totalReplies  =  documentSnapshot.get(GlobalConfig.TOTAL_REPLIES_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.TOTAL_REPLIES_KEY): 0L;
                            long  totalUpVotes =  documentSnapshot.get(GlobalConfig.TOTAL_UP_VOTES_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.TOTAL_UP_VOTES_KEY): 0L;
                            long totalDownVotes  =  documentSnapshot.get(GlobalConfig.TOTAL_DOWN_VOTES_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.TOTAL_DOWN_VOTES_KEY): 0L;
                            ArrayList repliersIdList  =  documentSnapshot.get(GlobalConfig.REPLIERS_ID_LIST_KEY)!=null ? (ArrayList)documentSnapshot.get(GlobalConfig.REPLIERS_ID_LIST_KEY): new ArrayList();
                            ArrayList upVotersIdList  =  documentSnapshot.get(GlobalConfig.UP_VOTERS_ID_LIST_KEY)!=null ? (ArrayList)documentSnapshot.get(GlobalConfig.UP_VOTERS_ID_LIST_KEY): new ArrayList();
                            ArrayList downVotersIdList  =  documentSnapshot.get(GlobalConfig.DOWN_VOTERS_ID_LIST_KEY)!=null ? (ArrayList)documentSnapshot.get(GlobalConfig.DOWN_VOTERS_ID_LIST_KEY): new ArrayList();
                            String dateCreated  =  documentSnapshot.get(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY)!=null ? documentSnapshot.getTimestamp(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY).toDate()+"" : "Moment ago";
                            if(dateCreated.length()>10){
                                dateCreated = dateCreated.substring(0,10);
//                                Toast.makeText(getContext(), isForPreview+" well data found well"+parentDiscussionId, Toast.LENGTH_SHORT).show();

                            }
                            answerListener.onSuccess(new AnswerDataModel(
                                     answerId,
                                     questionId,
                                     contributorId,
                                     answer,
                                     answerPhotoDownloadUrl,
                                     authorId,
                                      parentId,
                                     hasParent,
                                     isAnswer,
                                      hasReplies,
                                      isPhotoIncluded,
                                      isHiddenByAuthor,
                                      isHiddenByContributor,
                                     dateCreated,
                                     totalReplies,
                                     totalUpVotes,
                                     totalDownVotes,
                                     repliersIdList,
                                     upVotersIdList,
                                     downVotersIdList
    ));


                        }

                    }
                });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(getContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

            if (requestCode == CAMERA_PERMISSION_REQUEST_CODE){
                fireCameraIntent();
            }

            if (requestCode == GALLERY_PERMISSION_REQUEST_CODE){
                fireGalleryIntent();
            }

        }
    }

    public void openGallery(){
        requestForPermissionAndPickImage(GALLERY_PERMISSION_REQUEST_CODE);

    }
    public void openCamera(){
        requestForPermissionAndPickImage(CAMERA_PERMISSION_REQUEST_CODE);
    }

    public void fireGalleryIntent(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        openGalleryLauncher.launch(galleryIntent);
    }
    public void fireCameraIntent(){
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraLauncher.launch(cameraIntent);
    }
    public void requestForPermissionAndPickImage(int requestCode){
        if(getContext().checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED || getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},requestCode);
        }else{
            if(requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
                fireCameraIntent();
            }
            if(requestCode == GALLERY_PERMISSION_REQUEST_CODE) {
                fireGalleryIntent();
            }
        }



    }

    interface FetchAnswerListener{
//        void onSuccess(String pageId , String pageName, String dateCreated);
        void onSuccess(AnswerDataModel answerDataModel);
        void onFailed(String errorMessage);
    }
    public interface AddAnswerListener{
         void onAddAnswerTriggered(@NonNull AnswerDataModel answerDataModel, String parentId, boolean isEdition, boolean isAnswer);
    }
}

