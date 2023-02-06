package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.adapters.UserActivityItemRCVAdapter;
import com.palria.learnera.models.UserActivityDataModel;

import java.util.ArrayList;

public class UserActivityLogActivity extends AppCompatActivity {


    private RecyclerView activityLogContainer;
    ArrayList<UserActivityDataModel> activityDataModelArrayList = new ArrayList<>();
    UserActivityItemRCVAdapter userActivityItemRCVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_log);

        initUi();
fetchLogs(new OnLogFetchListener() {
    @Override
    public void onSuccess(String activityLogType, String authorId, String libraryId, String tutorialId, boolean isAuthorAffected, boolean isLibraryAffected, boolean isTutorialAffected,String tutorialFolderId ,String tutorialPageId ,String folderPageId , boolean isTutorialFolderAffected, boolean isTutorialPageAffected, boolean isFolderPageAffected,String eventDate,String logId){

        switch(activityLogType){
            case GlobalConfig.ACTIVITY_LOG_USER_REVIEW_AUTHOR_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_DELETE_AUTHOR_REVIEW_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_AUTHOR_REVIEW_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_SIGN_UP_TYPE_KEY:
                activityDataModelArrayList.add(new UserActivityDataModel("You signed up.",
                        eventDate, "...","null",1,"rating",false));
                userActivityItemRCVAdapter.notifyItemChanged(activityDataModelArrayList.size());

                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_SIGN_IN_TYPE_KEY:
                activityDataModelArrayList.add(new UserActivityDataModel("You signed in.",
                        eventDate, "...","null",1,"rating",false));
                userActivityItemRCVAdapter.notifyItemChanged(activityDataModelArrayList.size());

                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_ACCOUNT_TYPE_KEY:
                activityDataModelArrayList.add(new UserActivityDataModel("You edited your account.",
                        eventDate, "...","null",1,"rating",false));
                userActivityItemRCVAdapter.notifyItemChanged(activityDataModelArrayList.size());

                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_VISIT_AUTHOR_TYPE_KEY:
                activityDataModelArrayList.add(new UserActivityDataModel("You visited this author.",
                        eventDate, "...","null",1,"rating",false));
                userActivityItemRCVAdapter.notifyItemChanged(activityDataModelArrayList.size());

                ;
                return;

            case GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_LIBRARY_TYPE_KEY:
                activityDataModelArrayList.add(new UserActivityDataModel("You created new library.",
                        eventDate, "...","null",1,"rating",false));
                userActivityItemRCVAdapter.notifyItemChanged(activityDataModelArrayList.size());

                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_DELETE_LIBRARY_TYPE_KEY:
                activityDataModelArrayList.add(new UserActivityDataModel("You deleted your library.",
                        eventDate, "...","null",1,"rating",false));
                userActivityItemRCVAdapter.notifyItemChanged(activityDataModelArrayList.size());

                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_LIBRARY_TYPE_KEY:
                activityDataModelArrayList.add(new UserActivityDataModel("You edited your library.",
                        eventDate, "...","null",1,"rating",false));
                userActivityItemRCVAdapter.notifyItemChanged(activityDataModelArrayList.size());

                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_REVIEW_LIBRARY_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_LIBRARY_REVIEW_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_DELETE_LIBRARY_REVIEW_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_VISIT_LIBRARY_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_BOOK_MARK_LIBRARY_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_LIBRARY_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_VISIT_TUTORIAL_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_REVIEW_TUTORIAL_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_DELETE_TUTORIAL_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_DELETE_TUTORIAL_REVIEW_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_TUTORIAL_REVIEW_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_TUTORIAL_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_BOOK_MARK_TUTORIAL_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_TUTORIAL_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_FOLDER_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_TUTORIAL_FOLDER_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_DELETE_TUTORIAL_FOLDER_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_PAGE_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_TUTORIAL_PAGE_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_DELETE_TUTORIAL_PAGE_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_FOLDER_PAGE_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_FOLDER_PAGE_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_DELETE_FOLDER_PAGE_TYPE_KEY:
                ;
                return;
        }

    }

    @Override
    public void onFailed(String errorMessage) {
        activityDataModelArrayList.add(new UserActivityDataModel("Logs retrieval failed.",
                "today", errorMessage,"https://picsum.photos/400/200?random=22",1,"rating",false));
        userActivityItemRCVAdapter.notifyItemChanged(activityDataModelArrayList.size());

    }
});
    }

    private void initUi() {
        Toolbar actionBar = (Toolbar)  findViewById(R.id.topBar);
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activityLogContainer = findViewById(R.id.activityLogContainer);

        //date divider model
        activityDataModelArrayList.add(new UserActivityDataModel("",
                "Today", "","",0,"",true));

        activityDataModelArrayList.add(new UserActivityDataModel("User created a new Library.",
                "15 Feb 2023", "","https://picsum.photos/400/200?random=10",1,"library",false));
        activityDataModelArrayList.add(new UserActivityDataModel("User post a review in Tutorials.",
                "15 Feb 2023", "","https://picsum.photos/400/200?random=5",1,"tutorial",false));
        activityDataModelArrayList.add(new UserActivityDataModel("Post a new book in "+ Html.fromHtml("<b style='color:black;'> Java Development </b>") +" .",
                "15 Feb 2023", "","https://picsum.photos/400/200?random=12",1,"post",false));
        activityDataModelArrayList.add(new UserActivityDataModel("Logs a new Rating to the Ram Magar Library.",
                "15 Feb 2023", "","https://picsum.photos/400/200?random=56",1,"library",false));
        activityDataModelArrayList.add(new UserActivityDataModel("Deleted review from Hemans Tutorial.",
                "15 Feb 2023", "Hello This is my first Tutorial...","https://picsum.photos/400/200?random=22",1,"rating",false));
        //date divider model
        activityDataModelArrayList.add(new UserActivityDataModel("",
                "Yesterday", "","",0,"",true));
        activityDataModelArrayList.add(new UserActivityDataModel("Post a new book in "+ Html.fromHtml("<b style='color:black;'> Java Development </b>") +" .",
                "15 Feb 2023", "","https://picsum.photos/400/200?random=12",1,"post",false));
        activityDataModelArrayList.add(new UserActivityDataModel("Logs a new Rating to the Ram Magar Library.",
                "15 Feb 2023", "","https://picsum.photos/400/200?random=56",1,"library",false));
        activityDataModelArrayList.add(new UserActivityDataModel("Deleted review from Hemans Tutorial.",
                "15 Feb 2023", "Hello This is my first Tutorial...","https://picsum.photos/400/200?random=22",1,"rating",false));

        //date divider model
        activityDataModelArrayList.add(new UserActivityDataModel("",
                "Earlier this week", "","",0,"",true));
        activityDataModelArrayList.add(new UserActivityDataModel("Post a new book in "+ Html.fromHtml("<b style='color:black;'> Java Development &#9733; &#9733; &#9733; </b>") +" .",
                "15 Feb 2023", "","https://picsum.photos/400/200?random=12",1,"post",false));
        activityDataModelArrayList.add(new UserActivityDataModel("Logs a new Rating to the Ram Magar Library.",
                "15 Feb 2023", "","https://picsum.photos/400/200?random=56",1,"library",false));
        activityDataModelArrayList.add(new UserActivityDataModel("Deleted review from Hemans Tutorial.",
                "15 Feb 2023", "Hello This is my first Tutorial...","https://picsum.photos/400/200?random=22",1,"rating",false));


        userActivityItemRCVAdapter = new UserActivityItemRCVAdapter(activityDataModelArrayList,this);
//        activityLogContainer.setHasFixedSize(true);
        activityLogContainer.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        activityLogContainer.setAdapter(userActivityItemRCVAdapter);


    }

    void fetchLogs(OnLogFetchListener onLogFetchListener){
        GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY)
                .document(GlobalConfig.getCurrentUserId())
                .collection(GlobalConfig.USER_ACTIVITY_LOG_KEY)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onLogFetchListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size() !=0) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                String logId = documentSnapshot.getId();
                                String actionDoerId = "" + documentSnapshot.get(GlobalConfig.ACTION_DOER_ID_KEY);
                                String authorId = "" + documentSnapshot.get(GlobalConfig.AUTHOR_ID_KEY);
                                String libraryId = "" + documentSnapshot.get(GlobalConfig.LIBRARY_ID_KEY);
                                String tutorialId = "" + documentSnapshot.get(GlobalConfig.TUTORIAL_ID_KEY);
                                String activityLogType = "" + documentSnapshot.get(GlobalConfig.ACTIVITY_LOG_TYPE_KEY);
                                String eventDate = documentSnapshot.get(GlobalConfig.LOG_TIME_STAMP_KEY) != null && documentSnapshot.get(GlobalConfig.LOG_TIME_STAMP_KEY) instanceof Timestamp ? "" + documentSnapshot.getTimestamp(GlobalConfig.LOG_TIME_STAMP_KEY).toDate() : "undefined";
                                boolean isAuthorAffected = documentSnapshot.get(GlobalConfig.IS_AUTHOR_AFFECTED_KEY) != null ? documentSnapshot.getBoolean(GlobalConfig.IS_AUTHOR_AFFECTED_KEY) : true;
                                boolean isLibraryAffected = documentSnapshot.get(GlobalConfig.IS_LIBRARY_AFFECTED_KEY) != null ? documentSnapshot.getBoolean(GlobalConfig.IS_LIBRARY_AFFECTED_KEY) : false;
                                boolean isTutorialAffected = documentSnapshot.get(GlobalConfig.IS_TUTORIAL_AFFECTED_KEY) != null ? documentSnapshot.getBoolean(GlobalConfig.IS_TUTORIAL_AFFECTED_KEY) : false;

                                boolean isTutorialFolderAffected = documentSnapshot.get(GlobalConfig.IS_TUTORIAL_FOLDER_AFFECTED_KEY) != null ? documentSnapshot.getBoolean(GlobalConfig.IS_TUTORIAL_FOLDER_AFFECTED_KEY) : false;
                                boolean isTutorialPageAffected = documentSnapshot.get(GlobalConfig.IS_TUTORIAL_PAGE_AFFECTED_KEY) != null ? documentSnapshot.getBoolean(GlobalConfig.IS_TUTORIAL_PAGE_AFFECTED_KEY) : false;
                                boolean isFolderPageAffected = documentSnapshot.get(GlobalConfig.IS_FOLDER_PAGE_AFFECTED_KEY) != null ? documentSnapshot.getBoolean(GlobalConfig.IS_FOLDER_PAGE_AFFECTED_KEY) : false;

                                String tutorialFolderId = "" + documentSnapshot.get(GlobalConfig.FOLDER_ID_KEY);
                                String tutorialPageId = "" + documentSnapshot.get(GlobalConfig.TUTORIAL_PAGE_ID_KEY);
                                String folderPageId = "" + documentSnapshot.get(GlobalConfig.FOLDER_PAGE_ID_KEY);

                                onLogFetchListener.onSuccess(activityLogType, authorId, libraryId, tutorialId, isAuthorAffected, isLibraryAffected, isTutorialAffected, tutorialFolderId, tutorialPageId, folderPageId, isTutorialFolderAffected, isTutorialPageAffected, isFolderPageAffected, eventDate, logId);


                            }
                        }else{
                            activityDataModelArrayList.add(new UserActivityDataModel("Logs retrieval: 0 log found.",
                                    "Undefined", "Logs retrieval  found 0 logs...","https://picsum.photos/400/200?random=22",1,"rating",false));
                            userActivityItemRCVAdapter.notifyItemChanged(activityDataModelArrayList.size());

                        }
                    }
                });
    }

    interface OnLogFetchListener{
        void onSuccess(String activityLogType, String authorId, String libraryId, String tutorialId, boolean isAuthorAffected, boolean isLibraryAffected, boolean isTutorialAffected,String tutorialFolderId ,String tutorialPageId ,String folderPageId , boolean isTutorialFolderAffected, boolean isTutorialPageAffected, boolean isFolderPageAffected,String eventDate,String logId);
        void onFailed(String errorMessage);
    }
}