package com.palria.learnera;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.palria.learnera.adapters.ClassStudentRCVAdapter;
import com.palria.learnera.models.ClassDataModel;
import com.palria.learnera.models.ClassStudentDatamodel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ClassActivity extends AppCompatActivity {

    ImageButton backButton;
    NestedScrollView mainScrollView;
    LinearLayout classInfo;
    TabLayout viewTabLayout;
    ShimmerFrameLayout shimmerLayout;

    Toolbar toolbar;
    String authorId;
    String classId;
//    TextView maxTimeTextView;
    TextView joinActionTextView;
    TextView statusTextView;
    TextView studentCountTextView;
    TextView viewCountTextView;
    TextView descriptionTextView;
    TextView datePostedTextView;
    TextView posterNameTextView;
    ImageView posterPhoto,verificationFlagImageView;
//    FloatingActionButton submitActionButton;
    AlertDialog alertDialog;
    ClassDataModel classDataModel;
    ArrayList questionList = new ArrayList();
    ArrayList<ArrayList<String>> answersList = new ArrayList();
//    ArrayList<ArrayList<String>> authorSavedAnswersList = new ArrayList();
    LinearLayout containerLinearLayout;
    TextView regFeeTextView;
    TextView startTimeTextView;
    TextView endTimeTextView;
    boolean isJoined = false;
    boolean isAuthor = false;
    boolean isLoadFromOnline = false;
    boolean isClosed = false;
    MaterialButton markClassAsClosedActionTextView;
    RecyclerView studentRecyclerView;
    ClassStudentRCVAdapter classStudentRCVAdapter;
    ArrayList<ClassStudentDatamodel> classStudentDatamodels = new ArrayList<>();
    CountDownTimer countDownTimer;
    long startYear = 0L;
    long startMonth = 0L;
    long startDay = 0L;
    long startHour = 0L;
    long startMinute = 0L;

    long endYear = 0L;
    long endMonth = 0L;
    long endDay = 0L;
    long endHour = 0L;
    long endMinute = 0L;
    boolean isStarted;
    boolean isJustMarkedAsStarted = false;




    ActivityResultLauncher<Intent> openGalleryLauncher;
    ActivityResultLauncher<Intent> openVideoGalleryLauncher;
    ActivityResultLauncher<Intent> openCameraLauncher;


    private final int CAMERA_PERMISSION_REQUEST_CODE = 2002;
    private final int GALLERY_PERMISSION_REQUEST_CODE = 23;
    private final int VIDEO_PERMISSION_REQUEST_CODE = 20;

    LinearLayout infoLinearLayout;

    LinearLayout lessonLinearLayout;
    LinearLayout handRaisersLinearLayout;
    LinearLayout composeMessageLinearLayout;
    EditText lessonNoteEditText;

//    LinearLayout sendChatActionLayout;
    LinearLayout mediaActionActionLayout;
    LinearLayout raiseHandActionLayout;
    LinearLayout lockDiscussionActionLayout;

    ImageButton sendChatActionImageButton;
    ImageButton mediaActionImageButton;
    ImageButton raiseHandActionImageButton;
    ImageButton lockDiscussionActionImageButton;

    TextView lockDiscussionCaption;
    boolean isDiscussionLocked = false;

    ArrayList<String> renderedLessonNoteIds = new ArrayList<>();
    ArrayList<String> renderedHandRaisersIds = new ArrayList<>();
    HashMap<String,String> studentsName = new HashMap<>();
    HashMap<String,Bitmap> studentsIconBitmaps = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(GlobalConfig.recentlydeletedClassList.contains(classId)){
            Toast.makeText(getApplicationContext(), "Error occurred: Class deleted", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
            return;
        }else {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        fetchIntentData();
        initUI();
        setSupportActionBar(toolbar);

            //check if class is closed and update the isClosed boolean variable
           isClosed = classDataModel.isClosed();
            if (authorId.equalsIgnoreCase(GlobalConfig.getCurrentUserId())) {
                isAuthor = true;
            } else {

            }
            getAuthorInfo();
            if(isLoadFromOnline){
//                renderClassFromOnline();
            }else {
                renderClass(classDataModel);

            }

            listenToLessonUpdates();
        }
        createTabLayout();


        openGalleryLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null) {
                    Intent data=result.getData();

                    displayAndConfigureSendingImage(String.valueOf(data.getData()), null);


                }


                //addProductCustomRecyclerViewAdapter.notifyDataSetChanged();

            }
        });
        openVideoGalleryLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null) {
                    Intent data=result.getData();


                }
            }
        });
        openCameraLauncher=  registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK) {

//                    if(android.os.Build.VERSION.SDK_INT >= 29) {
//                        handleCameraResult();
//                    }else {
                    if (result.getData() != null) {
                        Intent data = result.getData();
                        Bitmap bitmapFromCamera =(Bitmap) data.getExtras().get("data");

//                            uriArrayList.add(uriFromCamera);
//                        if(bitmapFromCamera != null) {
                        displayAndConfigureSendingImage(String.valueOf(data.getData()), bitmapFromCamera);
//                        }

                    }
                    // }
                }else{
                    Toast.makeText(getApplicationContext(), "No image captured!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        View.OnClickListener sendChatClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
String lessonNoteId = GlobalConfig.getRandomString(60);
ArrayList<Object> lessonInfoList = new ArrayList<>();
String lessonNote = lessonNoteEditText.getText()+"";

//displayLessonNote(false, new ArrayList<>(),lessonNoteId,lessonNote,Uri.parse(""), null);



//the type of lesson whether it's plain,image,video, or other types.
lessonInfoList.add(0,GlobalConfig.IS_PLAIN_TEXT_LESSON_NOTE_TYPE_KEY);
//the lesson note id
lessonInfoList.add(1,lessonNoteId);
//the lesson note itself
lessonInfoList.add(2,lessonNote);
//sender id
lessonInfoList.add(3,GlobalConfig.getCurrentUserId());

////date sent
//lessonInfoList.add(4,FieldValue.serverTimestamp());

                //display it instantly
displayLessonNote(true,false,lessonInfoList,lessonNoteId,lessonNote,null,null,"now");
                lessonNoteEditText.setText("");
                notifyStudentsWhetherTeacherIsTyping(false);
            postLessonNote(lessonNoteId, lessonInfoList, new GlobalConfig.ActionCallback() {
                @Override
                public void onSuccess() {
//                    Toast.makeText(getApplicationContext(), "lesson note: "+lessonNote+" posted", Toast.LENGTH_SHORT).show();

                    for(int i=0;i<lessonLinearLayout.getChildCount(); i++) {
                        View lessonNoteView = lessonLinearLayout.getChildAt(i);
                        ImageView lessonNoteStateIndicatorImageView = lessonNoteView.findViewById(R.id.lessonNoteStateIndicatorImageViewId);
                        TextView lessonNoteIdHolderDummyTextView = lessonNoteView.findViewById(R.id.lessonNoteIdHolderDummyTextViewId);

                        if(lessonNoteId.equals(lessonNoteIdHolderDummyTextView.getText()+"")){
                            lessonNoteStateIndicatorImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_sent_12,getTheme()));
                        }

                    }
                }

                @Override
                public void onFailed(String errorMessage) {

                }
            });

            }
        };
        sendChatActionImageButton.setOnClickListener(sendChatClickListener);
//        sendChatActionLayout.setOnClickListener(sendChatClickListener);

        View.OnClickListener mediaActionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalConfig.createPopUpMenu(ClassActivity.this, R.menu.lesson_media_menu, mediaActionImageButton, new GlobalConfig.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClicked(MenuItem item) {
                        if(item.getItemId() == R.id.openImageGalleryId){

                            openGallery();
                        }
                        if(item.getItemId() == R.id.openImageCameraId){
                            openCamera();

                        }

                        return true;
                    }
                });
            }
        };
        mediaActionImageButton.setOnClickListener(mediaActionClickListener);
        mediaActionActionLayout.setOnClickListener(mediaActionClickListener);

        View.OnClickListener raiseHandActionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                raiseHandActionImageButton.setEnabled(false);
                raiseHandActionLayout.setEnabled(false);
                raiseHand(new GlobalConfig.ActionCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailed(String errorMessage) {
                        raiseHandActionImageButton.setEnabled(true);
                        raiseHandActionLayout.setEnabled(true);
                    }
                });
            }
        };
        raiseHandActionImageButton.setOnClickListener(raiseHandActionClickListener);
        raiseHandActionLayout.setOnClickListener(raiseHandActionClickListener);

        View.OnClickListener lockDiscussionActionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lockDiscussionActionImageButton.setEnabled(false);
                lockDiscussionActionLayout.setEnabled(false);
                if(isDiscussionLocked){
                    unlockDiscussion(new GlobalConfig.ActionCallback() {
                        @Override
                        public void onSuccess() {
                            isDiscussionLocked = false;
                            lockDiscussionActionImageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_unlock_24,getTheme()));
                            lockDiscussionCaption.setText("Lock chat");
                            lockDiscussionActionImageButton.setEnabled(true);
                            lockDiscussionActionLayout.setEnabled(true);

                        }

                        @Override
                        public void onFailed(String errorMessage) {
                            lockDiscussionActionImageButton.setEnabled(true);
                            lockDiscussionActionLayout.setEnabled(true);

                        }
                    });
                }else {
                    lockDiscussion(new GlobalConfig.ActionCallback() {
                        @Override
                        public void onSuccess() {
                            isDiscussionLocked = true;
                            lockDiscussionActionImageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_lock_24,getTheme()));
                            lockDiscussionCaption.setText("Unlock chat");
                            lockDiscussionActionImageButton.setEnabled(true);
                            lockDiscussionActionLayout.setEnabled(true);

                        }

                        @Override
                        public void onFailed(String errorMessage) {
                            lockDiscussionActionImageButton.setEnabled(true);
                            lockDiscussionActionLayout.setEnabled(true);

                        }
                    });
                }
            }
        };
        lockDiscussionActionImageButton.setOnClickListener(lockDiscussionActionClickListener);
        lockDiscussionActionLayout.setOnClickListener(lockDiscussionActionClickListener);

    }

    @Override
    public void onBackPressed() {
       createConfirmExitDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isAuthor || GlobalConfig.isLearnEraAccount()) {
            getMenuInflater().inflate(R.menu.class_action_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.editId) {
            if (classDataModel.getStudentsList().size() < 1) {
                //allow edit because no student has joined
                Intent intent = new Intent(ClassActivity.this, CreateClassActivity.class);
                intent.putExtra(GlobalConfig.IS_EDITION_KEY, true);
                intent.putExtra(GlobalConfig.CLASS_ID_KEY, classId);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Can't take action: User has already joined and can't be edited", Toast.LENGTH_SHORT).show();
            }
        }
        else if (item.getItemId() == R.id.deleteId) {
            //todo
            if (classDataModel.getStudentsList().size() < 1 || GlobalConfig.isLearnEraAccount()) {
                //allow delete because no student has joined

                toggleProgress(true);
                GlobalConfig.deleteClass(getApplicationContext(), authorId, classId, new GlobalConfig.ActionCallback() {
                    @Override
                    public void onSuccess() {

                        toggleProgress(false);
                        GlobalConfig.recentlydeletedClassList.add(classId);
                        ClassActivity.super.onBackPressed();

                    }

                    @Override
                    public void onFailed(String errorMessage) {
                        toggleProgress(false);

                        Toast.makeText(getApplicationContext(), "Error occurred: Can't delete", Toast.LENGTH_SHORT).show();

                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Can't take action: A user has already joined and can't be deleted", Toast.LENGTH_SHORT).show();
            }
        }
        else if(item.getItemId()==R.id.home){
            super.onBackPressed();

        }
        return true;
    }

    private void initUI(){

        //init programmatically


        mainScrollView = findViewById(R.id.mainScrollViewId);
        classInfo = findViewById(R.id.classInfoId);
        viewTabLayout = findViewById(R.id.viewTabLayoutId);
        shimmerLayout = findViewById(R.id.shimmerLayoutId);

        backButton = findViewById(R.id.backButtonId);
        toolbar = findViewById(R.id.topBar);
//        maxTimeTextView = findViewById(R.id.maxTimeTextViewId);
        joinActionTextView = findViewById(R.id.joinClassActionTextViewId);
        statusTextView = findViewById(R.id.activeStatusTextViewId);
        studentCountTextView = findViewById(R.id.studentCountTextViewId);
        descriptionTextView = findViewById(R.id.descriptionTextViewId);
        viewCountTextView = findViewById(R.id.viewCountTextViewId);
        datePostedTextView = findViewById(R.id.datePostedTextViewId);
        posterNameTextView = findViewById(R.id.posterNameTextViewId);
        posterPhoto = findViewById(R.id.posterProfilePhotoId);
        verificationFlagImageView = findViewById(R.id.verificationFlagImageViewId);
        containerLinearLayout = findViewById(R.id.containerLinearLayoutId);
//        submitActionButton = findViewById(R.id.submitActionButtonId);
        studentRecyclerView = findViewById(R.id.studentRecyclerViewId);
        startTimeTextView=findViewById(R.id.startTimeTextViewId);
        endTimeTextView=findViewById(R.id.endTimeTextViewId);
        markClassAsClosedActionTextView=findViewById(R.id.markClassAsClosedActionTextViewId);

        regFeeTextView=findViewById(R.id.regFeeValueTextViewId);


        infoLinearLayout = findViewById(R.id.infoLinearLayoutId);


        handRaisersLinearLayout = findViewById(R.id.handRaisersLinearLayoutId);
        lessonLinearLayout = findViewById(R.id.lessonLinearLayoutId);
        composeMessageLinearLayout = findViewById(R.id.composeMessageLinearLayoutId);
        lessonNoteEditText = findViewById(R.id.lessonEditTextId);

        mediaActionActionLayout = findViewById(R.id.mediaActionActionLayoutId);
        mediaActionImageButton = findViewById(R.id.mediaActionImageButtonId);

        raiseHandActionImageButton = findViewById(R.id.raiseHandActionImageButtonId);
        raiseHandActionLayout = findViewById(R.id.raiseHandActionLayoutId);

        lockDiscussionCaption = findViewById(R.id.lockDiscussionCaptionId);
        lockDiscussionActionImageButton = findViewById(R.id.lockDiscussionActionImageButtonId);
        lockDiscussionActionLayout = findViewById(R.id.lockDiscussionActionLayoutId);

//        sendChatActionLayout = findViewById(R.id.sendChatActionLayoutId);
        sendChatActionImageButton = findViewById(R.id.sendLessonActionImageButtonId);


        alertDialog = new AlertDialog.Builder(ClassActivity.this)
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();
    }

    private  void fetchIntentData(){
        Intent intent = getIntent();
        classDataModel = (ClassDataModel) intent.getSerializableExtra(GlobalConfig.CLASS_DATA_MODEL_KEY);
        authorId = intent.getStringExtra(GlobalConfig.AUTHOR_ID_KEY);
        classId = intent.getStringExtra(GlobalConfig.CLASS_ID_KEY);
        isLoadFromOnline =intent.getBooleanExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY,false);
    }
    private void createConfirmExitDialog(){
        AlertDialog confirmExitDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Exit Warning!");
        builder.setMessage("Click exit button to confirm exit the screen");
        builder.setCancelable(true);
        builder.setIcon(R.drawable.baseline_error_outline_red_24);

        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClassActivity.super.onBackPressed();
                if(countDownTimer!=null) {
                    countDownTimer.cancel();
                }
            }
        })
                .setNegativeButton("Continue", null);
        confirmExitDialog = builder.create();
        confirmExitDialog.show();

    }


    private void toggleProgress(boolean show)
    {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
    }


    private void updateClassCountDownTimer(){
        countDownTimer = new CountDownTimer(9000000000000000000L,1000) {
            @Override
            public void onTick(long l) {

                //count down time for the class. may not be needeed anymore
//                timeRemain = timeRemain-1;
//                maxTimeTextView.setText("Time "+timeRemain+"s/"+totalTimeLimit+"s");
//                if(timeRemain == 5){
//                    maxTimeTextView.setTextColor(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
//                }
//                if(timeRemain == 0 && !isSubmitted && !isAuthor){
//                    processAndSubmitAnswer();
//                }
                //check if class is marked completed by author
                if (classDataModel.isClosed() || GlobalConfig.recentlyClosedClassList.contains(classId)) {
//                    submitActionButton.setVisibility(View.GONE);

                } else {
                    if ((GlobalConfig.isClassExpired(endYear, endMonth, endDay, endHour, endMinute) || !GlobalConfig.isClassStarted(startYear, startMonth, startDay, startHour, startMinute)) &&  !isAuthor) {
//                        submitActionButton.setVisibility(View.GONE);

                    }
                }


//check if the class has started
                if(!isStarted && GlobalConfig.isClassStarted(startYear,startMonth,startDay,startHour,startMinute) && !isAuthor) {
                    renderStudentInfo();


                }
                if(GlobalConfig.isClassStarted(startYear,startMonth,startDay,startHour,startMinute) && !classDataModel.isStarted() && !isJustMarkedAsStarted){
                    GlobalConfig.markClassAsStarted(ClassActivity.this,classId,null);
                    isJustMarkedAsStarted = true;
                }

            }

            @Override
            public void onFinish() {

            //may never finish

            }
        }.start();

    }

    private void renderClass(ClassDataModel classDataModel) {
        this.classDataModel = classDataModel;
        toolbar.setTitle(classDataModel.getClassTitle());
        descriptionTextView.setText(classDataModel.getClassDescription());
        regFeeTextView.setText(classDataModel.getTotalClassFeeCoins() + " COINS");
//        submitActionButton.setVisibility(View.VISIBLE);

        //helps keep name of students at runtime to avoid frequent queries for the server
for(String studentId : classDataModel.getStudentsList()){
    studentsName.put(studentId,"Student");
    studentsIconBitmaps.put(studentId,null);
}
        mainScrollView.setVisibility(View.VISIBLE);
        classInfo.setVisibility(View.VISIBLE);
        shimmerLayout.setVisibility(View.GONE);
        shimmerLayout.stopShimmer();

        ArrayList<Long> startTime = classDataModel.getStartDateList();
        if (startTime.size() >= 5) {
            startDay = startTime.get(2);
            startMonth = startTime.get(1);
            startYear = startTime.get(0);
            startHour = startTime.get(3);
            startMinute = startTime.get(4);

        }

        startTimeTextView.setText(startYear + "/" + startMonth + "/" + startYear + " :" + startHour + ":" + startMinute);
        ArrayList<Long> endTime = classDataModel.getEndDateList();
        if (startTime.size() >= 5) {
            endDay = endTime.get(2);
            endMonth = endTime.get(1);
            endYear = endTime.get(0);
            endHour = endTime.get(3);
            endMinute = endTime.get(4);

        }
        endTimeTextView.setText(endYear + "/" + endMonth + "/" + endYear + " :" + endHour + ":" + endMinute);


        if (isAuthor) {
            joinActionTextView.setText("You are the author");
        } else {
            if (classDataModel.getStudentsList().contains(GlobalConfig.getCurrentUserId()) || GlobalConfig.newlyJoinedClassList.contains(classId)) {
                joinActionTextView.setText("Joined");
                isJoined = true;
            } else {
                joinActionTextView.setText("Join");
                if (isClosed || GlobalConfig.isClassExpired(endYear, endMonth, endDay, endHour, endMinute)) {
                    joinActionTextView.setEnabled(false);
//                    submitActionButton.setVisibility(View.GONE);
                    statusTextView.setText("Closed");
                } else {
                    joinActionTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //navigate to JoinClassActivity
                            Intent intent = new Intent(ClassActivity.this, JoinClassActivity.class);
                            intent.putExtra(GlobalConfig.CLASS_DATA_MODEL_KEY, classDataModel);
                            startActivity(intent);

                            ClassActivity.super.onBackPressed();

//                            joinClass();

                        }
                    });
                }

            }
        }

        if (GlobalConfig.isClassExpired(endYear, endMonth, endDay, endHour, endMinute)) {
            statusTextView.setText("Closed");
        }
      if (classDataModel.getAuthorId().equals(GlobalConfig.getCurrentUserId()) && (!classDataModel.isClosed() && !GlobalConfig.recentlyClosedClassList.contains(classId))) {
            markClassAsClosedActionTextView.setVisibility(View.VISIBLE);
        }

        studentCountTextView.setText(classDataModel.getTotalStudents() + "");
        viewCountTextView.setText(classDataModel.getTotalViews() + "");
        datePostedTextView.setText(classDataModel.getDateCreated() + "");

            updateClassCountDownTimer();




        }




    void getAuthorInfo(){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(authorId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String userProfilePhotoDownloadUrl = "" + documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
                        try {
                            Glide.with(ClassActivity.this)
                                    .load(userProfilePhotoDownloadUrl)
                                    .centerCrop()
                                    .placeholder(R.drawable.default_profile)
                                    .into(posterPhoto);
                        } catch (Exception ignored) {
                        }

                        String userDisplayName = "" + documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                        posterNameTextView.setText(userDisplayName);


                        boolean isVerified = documentSnapshot.get(GlobalConfig.IS_ACCOUNT_VERIFIED_KEY)!=null?documentSnapshot.getBoolean(GlobalConfig.IS_ACCOUNT_VERIFIED_KEY):false;
                        if(isVerified){
                            verificationFlagImageView.setVisibility(View.VISIBLE);
                        }else{
                            verificationFlagImageView.setVisibility(View.INVISIBLE);

                        }
                    }
                });

        posterNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(GlobalConfig.getHostActivityIntent(ClassActivity.this,null,GlobalConfig.USER_PROFILE_FRAGMENT_TYPE_KEY,classDataModel.getAuthorId()));

            }
        });
        posterPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(GlobalConfig.getHostActivityIntent(ClassActivity.this,null,GlobalConfig.USER_PROFILE_FRAGMENT_TYPE_KEY,classDataModel.getAuthorId()));

            }
        });


    }
    void getStudents(){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_CLASS_KEY).document(classId)
                .collection(GlobalConfig.ALL_STUDENTS_KEY)
                .get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots) {

//                    String studentId = "" + documentSnapshot.get(GlobalConfig.PARTICIPANT_ID_KEY);
                    String studentId = "" + documentSnapshot.getId();
                    String dateJoined = "" + documentSnapshot.get(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY);

                    if((studentId+"").equalsIgnoreCase(GlobalConfig.getCurrentUserId())){

                    }
                    ClassStudentDatamodel classStudentDatamodel = new ClassStudentDatamodel(studentId,dateJoined);
                    classStudentDatamodels.add(classStudentDatamodel);
                    classStudentRCVAdapter.notifyItemChanged(classStudentDatamodels.size());
                }
            }
        });

    }

    void listenToLessonUpdates(){
      FirebaseFirestore f = GlobalConfig.getFirebaseFirestoreInstance();

      f.collection(GlobalConfig.ALL_CLASS_KEY).document(classId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(error != null){

                }
                else {
                    if (documentSnapshot == null || !documentSnapshot.exists()) {
                        Toast.makeText(ClassActivity.this, "Error: Class does not exist or may have been deleted", Toast.LENGTH_SHORT).show();
                        ClassActivity.super.onBackPressed();

                        return;
                    }
//                    Toast.makeText(ClassActivity.this, "Success: Class heard", Toast.LENGTH_SHORT).show();

                    String classId = "" + documentSnapshot.getId();
                    String authorId = "" + documentSnapshot.get(GlobalConfig.AUTHOR_ID_KEY);
                    String classTitle = "" + documentSnapshot.get(GlobalConfig.CLASS_TITLE_KEY);
                    String classDescription = "" + documentSnapshot.get(GlobalConfig.CLASS_DESCRIPTION_KEY);
                    long totalClassFeeCoins = documentSnapshot.get(GlobalConfig.TOTAL_CLASS_FEE_COINS_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_CLASS_FEE_COINS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_CLASS_FEE_COINS_KEY) : 0L;
                    long totalStudents = documentSnapshot.get(GlobalConfig.TOTAL_STUDENTS_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_STUDENTS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_STUDENTS_KEY) : 0L;
                    long totalViews = documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_VIEWS_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_VIEWS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_VIEWS_KEY) : 0L;
                    boolean isPublic = documentSnapshot.get(GlobalConfig.IS_PUBLIC_KEY) != null && documentSnapshot.get(GlobalConfig.IS_PUBLIC_KEY) instanceof Boolean ? documentSnapshot.getBoolean(GlobalConfig.IS_PUBLIC_KEY) : true;
                    boolean isClosed = documentSnapshot.get(GlobalConfig.IS_CLOSED_KEY) != null && documentSnapshot.get(GlobalConfig.IS_CLOSED_KEY) instanceof Boolean ? documentSnapshot.getBoolean(GlobalConfig.IS_CLOSED_KEY) : false;
                    boolean isStarted = documentSnapshot.get(GlobalConfig.IS_STARTED_KEY) != null && documentSnapshot.get(GlobalConfig.IS_STARTED_KEY) instanceof Boolean ? documentSnapshot.getBoolean(GlobalConfig.IS_STARTED_KEY) : false;

                    ArrayList startDateList = documentSnapshot.get(GlobalConfig.CLASS_START_DATE_LIST_KEY) != null && documentSnapshot.get(GlobalConfig.CLASS_START_DATE_LIST_KEY) instanceof ArrayList ? (ArrayList) documentSnapshot.get(GlobalConfig.CLASS_START_DATE_LIST_KEY) : new ArrayList();
                    ArrayList endDateList = documentSnapshot.get(GlobalConfig.CLASS_END_DATE_LIST_KEY) != null && documentSnapshot.get(GlobalConfig.CLASS_END_DATE_LIST_KEY) instanceof ArrayList ? (ArrayList) documentSnapshot.get(GlobalConfig.CLASS_END_DATE_LIST_KEY) : new ArrayList();
                    ArrayList studentsList = documentSnapshot.get(GlobalConfig.STUDENTS_LIST_KEY) != null && documentSnapshot.get(GlobalConfig.STUDENTS_LIST_KEY) instanceof ArrayList ? (ArrayList) documentSnapshot.get(GlobalConfig.STUDENTS_LIST_KEY) : new ArrayList();
                    String dateCreated = documentSnapshot.get(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY) != null && documentSnapshot.get(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY) instanceof Timestamp ? "" + documentSnapshot.getTimestamp(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY).toDate() : "Moment ago";
                    if (dateCreated.length() > 10) {
                        dateCreated = dateCreated.substring(0, 10);
                    }
                    String dateEdited = documentSnapshot.get(GlobalConfig.DATE_EDITED_TIME_STAMP_KEY) != null && documentSnapshot.get(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY) instanceof Timestamp ? "" + documentSnapshot.getTimestamp(GlobalConfig.DATE_EDITED_TIME_STAMP_KEY).toDate() : "Moment ago";
                    if (dateEdited.length() > 10) {
                        dateEdited = dateEdited.substring(0, 10);
                    }
                    if(!authorId.equals(GlobalConfig.getCurrentUserId())){
                    boolean isDiscussionLocked = documentSnapshot.get(GlobalConfig.IS_DISCUSSION_LOCKED_KEY) != null ? documentSnapshot.getBoolean(GlobalConfig.IS_DISCUSSION_LOCKED_KEY) : false;
                    if (isDiscussionLocked) {
                        lessonNoteEditText.setEnabled(false);
                        lessonNoteEditText.setHint("Locked for all students");
                    } else {
                        lessonNoteEditText.setEnabled(true);
                        lessonNoteEditText.setHint("Unlocked");
                    }

                    ArrayList permittedHandRaisersList = documentSnapshot.get(GlobalConfig.PERMITTED_HAND_RAISERS_LIST_KEY) != null && documentSnapshot.get(GlobalConfig.PERMITTED_HAND_RAISERS_LIST_KEY) instanceof ArrayList ? (ArrayList) documentSnapshot.get(GlobalConfig.PERMITTED_HAND_RAISERS_LIST_KEY) : new ArrayList();

                    if (permittedHandRaisersList.contains(GlobalConfig.getCurrentUserId())) {
                        lessonNoteEditText.setEnabled(true);
                        lessonNoteEditText.setHint("unlocked for you");
                    }

                }
                    renderClass(new ClassDataModel(
                            classId,
                            authorId,
                            classTitle,
                            classDescription,
                            (int)totalClassFeeCoins,
                            dateCreated,
                            dateEdited,
                            totalStudents,
                            totalViews,
                            isPublic,
                            isClosed,
                            isStarted,
                            startDateList,
                            endDateList,
                            studentsList
                    ));


                    renderHandRaisers(documentSnapshot);
                    analyseAndRenderLessonNotes(documentSnapshot);

                }
            }
        });
    }
    void renderStudentInfo(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        classStudentRCVAdapter = new ClassStudentRCVAdapter(classStudentDatamodels,this,classId,classDataModel,authorId,markClassAsClosedActionTextView,studentsName,studentsIconBitmaps);
        studentRecyclerView.setHasFixedSize(false);
        studentRecyclerView.setAdapter(classStudentRCVAdapter);
        studentRecyclerView.setLayoutManager(linearLayoutManager);

        getStudents();

    }

    public void createTabLayout(){

        viewTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               if(tab.getPosition()==0){
                   toggleInfoDetails();
               }
               else{
                   toggleLessonDetails();
               }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        TabLayout.Tab infoTabItem= viewTabLayout.newTab();
        infoTabItem.setText("Info");
        viewTabLayout.addTab(infoTabItem,0,true);


        TabLayout.Tab lessonTabItem= viewTabLayout.newTab();
        lessonTabItem.setText("Lesson");
        viewTabLayout.addTab(lessonTabItem,1);

    }

    void toggleLessonDetails(){
        lessonLinearLayout.setVisibility(View.VISIBLE);
        handRaisersLinearLayout.setVisibility(View.VISIBLE);
        composeMessageLinearLayout.setVisibility(View.VISIBLE);


        infoLinearLayout.setVisibility(View.GONE);
        markClassAsClosedActionTextView.setVisibility(View.GONE);
    }
    void toggleInfoDetails(){
        infoLinearLayout.setVisibility(View.VISIBLE);
        markClassAsClosedActionTextView.setVisibility(View.VISIBLE);


        lessonLinearLayout.setVisibility(View.GONE);
        handRaisersLinearLayout.setVisibility(View.GONE);
        composeMessageLinearLayout.setVisibility(View.GONE);

    }

    public void openGallery(){
//        findViewById(R.id.chooseImageLinearLayoutId).setVisibility(View.GONE);
//        isVisible = false;
        requestForPermission(GALLERY_PERMISSION_REQUEST_CODE);

    }
    public void openVideoGallery(){
//        findViewById(R.id.chooseImageLinearLayoutId).setVisibility(View.GONE);
//        isVisible = false;
        requestForPermission(VIDEO_PERMISSION_REQUEST_CODE);

    }
    public void openCamera(){
//        findViewById(R.id.chooseImageLinearLayoutId).setVisibility(View.GONE);
//        isVisible = false;
        requestForPermission(CAMERA_PERMISSION_REQUEST_CODE);
    }

    public void fireGalleryIntent(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
//        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        galleryIntent.setType("image/*");
        openGalleryLauncher.launch(galleryIntent);
    }
    public void fireVideoIntent(){
        Intent videoGalleryIntent = new Intent();
//        videoGalleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        videoGalleryIntent.setAction(Intent.ACTION_PICK);
        videoGalleryIntent.setType("video/*");
        openVideoGalleryLauncher.launch(videoGalleryIntent);
    }
    public void fireCameraIntent(){
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraLauncher.launch(cameraIntent);
    }
    public void handleCameraResult(){
        ContentResolver contentResolver=getApplicationContext().getContentResolver();
        String[] path =new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
        };
        Cursor cursor=contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,path,null,null,path[1]+" DESC");
        if(cursor.moveToFirst()) {
            String uriFromCamera = "content://media/external/images/media/" + cursor.getInt(0);
//            displayAndConfigureSendingImage(uriFromCamera,null);
        }
        cursor.close();
    }
    public void requestForPermission(int requestCode){
        if(getApplicationContext().checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED || getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},requestCode);
        }else{
            if(requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
                fireCameraIntent();
            }
            if(requestCode == GALLERY_PERMISSION_REQUEST_CODE) {
                fireGalleryIntent();
            } if(requestCode == VIDEO_PERMISSION_REQUEST_CODE) {
                fireVideoIntent();
            }
        }
    }

    void postLessonNote(String lessonNoteId,ArrayList<Object> lessonInfoList, GlobalConfig.ActionCallback actionCallback){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
        DocumentReference lessonReference1 = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).document(classId);
        HashMap<String,Object> lessonDetails = new HashMap<>();
        lessonDetails.put(lessonNoteId,lessonInfoList);
        lessonDetails.put(GlobalConfig.ALL_LESSON_ID_LIST_KEY, FieldValue.arrayUnion(lessonNoteId));
        lessonDetails.put(lessonNoteId+GlobalConfig.DATE_SENT_KEY, FieldValue.serverTimestamp());
        lessonDetails.put(GlobalConfig.TOTAL_LESSONS_KEY, FieldValue.increment(1L));
        writeBatch.set(lessonReference1,lessonDetails, SetOptions.merge());

        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(actionCallback!=null) {
                            actionCallback.onFailed(e.getMessage());
                        }
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        actionCallback.onSuccess();
                    }
                });
    }


    private void displayAndConfigureSendingImage(String imageDownloadUrl, Bitmap bitmap){
        Dialog messageImageViewDialog = new Dialog(this);
        messageImageViewDialog.setContentView(R.layout.lesson_note_sending_image_dialog_layout);
//        messageImageViewDialog.requestWindowFeature();

        ImageView messageImagePreview = messageImageViewDialog.findViewById(R.id.lessonNoteImagePreviewId);
        EditText imageCaptionMessageEditText = messageImageViewDialog.findViewById(R.id.imageCaptionLessonNoteEditTextId);
        Button cancelSendImageActionButton = messageImageViewDialog.findViewById(R.id.cancelSendImageActionButtonId);
        Button sendImageActionButton = messageImageViewDialog.findViewById(R.id.sendImageActionButton);
        if(bitmap == null) {
            messageImagePreview.setImageURI(Uri.parse(imageDownloadUrl));
        }else{

            messageImagePreview.setImageBitmap(bitmap);
        }


        cancelSendImageActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageImageViewDialog.cancel();
            }
        });

        sendImageActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lessonNoteId = GlobalConfig.getRandomString(60);
                String textMessage = imageCaptionMessageEditText.getText().toString();
                displayLessonNote(false,true,new ArrayList<>(),lessonNoteId,textMessage,Uri.parse(imageDownloadUrl), bitmap,"now");

                messageImageViewDialog.cancel();
            }
            });

        messageImageViewDialog.create();
        messageImageViewDialog.show();
    }

    /*
    static void displayLessonNote(Context context, ViewGroup viewGroup, DocumentReference allMessagesDocumentDirectoryReference, DocumentReference messageDocumentReference, String messageId, String textMessage, String imageDownloadUrl, String videoDownloadUrl, String currentSenderId, String senderDisplayName, String senderId, String recipientId, String dateSent, boolean isTextIncluded, boolean isImageIncluded, boolean isSendImage, ImageView  sendingImageView, boolean isVideoIncluded, boolean isCurrentUserTheSender, boolean isLoadFromSnapshot, boolean isLoadFromCache, boolean hasPendingWrites, boolean isDeliveredToRecipient, boolean isSeenByRecipient, boolean isDeliveredToCurrentUser, boolean isSeenByCurrentUser, OnMessageImageUploadListener onMessageImageUploadListener){
        if(sendingImageView != null){
            sendingImageView.setDrawingCacheEnabled(true);
        }
        String[] messageIdArray = new String[1];
        messageIdArray[0] = messageId;

        if(isSendImage){
            if(messageId == null){
                messageId = getRandomString(60);
                messageIdArray[0] = messageId;
                if(messageDocumentReference ==  null){
                    messageDocumentReference = allMessagesDocumentDirectoryReference.collection("MESSAGES").document(messageId);
                }
            }
        }

        if(messageDocumentReference ==  null){
            messageDocumentReference = allMessagesDocumentDirectoryReference.collection("MESSAGES").document(messageId);
        }

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View lessonNoteView =  layoutInflater.inflate(R.layout.plain_message_view_holder_layout,viewGroup,false);
        CardView bodyMessageMainCardView = lessonNoteView.findViewById(R.id.bodyMessageMainCardViewId);
        LinearLayout mainLinearLayout = lessonNoteView.findViewById(R.id.mainLinearLayoutId);
        LinearLayout bodyMessageMainLinearLayout = lessonNoteView.findViewById(R.id.bodyMessageMainLinearLayoutId);
        View leftDummyView = lessonNoteView.findViewById(R.id.leftDummyViewId);
        View rightDummyView = lessonNoteView.findViewById(R.id.rightDummyViewId);

        TextView messageSenderDisplayNameTextView = lessonNoteView.findViewById(R.id.messageSenderDisplayNameTextViewId);
        TextView textMessageTextView = lessonNoteView.findViewById(R.id.textMessageTextViewId);
        TextView dateSentTextView = lessonNoteView.findViewById(R.id.dateSentTextViewId);

        ImageView messageStateIndicatorImageView = lessonNoteView.findViewById(R.id.messageStateIndicatorImageViewId);

        TextView recipientIdHolderDummyTextView = lessonNoteView.findViewById(R.id.recipientIdHolderDummyTextViewId);
        TextView senderIdHolderDummyTextView = lessonNoteView.findViewById(R.id.senderIdHolderDummyTextViewId);
        TextView messageIdHolderDummyTextView = lessonNoteView.findViewById(R.id.messageIdHolderDummyTextViewId);


        RelativeLayout messageImageViewRelativeLayout = lessonNoteView.findViewById(R.id.messageImageViewRelativeLayoutId);
        ImageView imageMessageImageView = lessonNoteView.findViewById(R.id.imageMessageImageViewId);
        StyledPlayerView videoMessageStyledPlayerView = lessonNoteView.findViewById(R.id.videoMessageStyledPlayerViewId);



        FloatingActionButton startUploadImageView = lessonNoteView.findViewById(R.id.startUploadButtonId);
        FloatingActionButton successImageView = lessonNoteView.findViewById(R.id.successImageViewId);

        FloatingActionButton pauseUploadImageView = lessonNoteView.findViewById(R.id.pauseUploadButtonId);

        FloatingActionButton resumeUploadImageView = lessonNoteView.findViewById(R.id.resumeUploadButtonId);

        ProgressBar progressBar = lessonNoteView.findViewById(R.id.progressIndicatorId);

        lessonNoteView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(senderId.equals(currentSenderId)) {
                    createPopUpMenu(context, R.menu.message_menu_action, messageStateIndicatorImageView, new OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClicked(MenuItem item) {

                            if(!textMessage.equals("-MESSAGE DELETED-")) {
                                if (isConnectedOnline(context)) {
                                    if (item.getItemId() == R.id.deleteMessageId) {
                                        deleteMessage(allMessagesDocumentDirectoryReference, messageIdHolderDummyTextView.getText().toString(), isTextIncluded, isImageIncluded, isVideoIncluded);
                                        textMessageTextView.setText("-MESSAGE DELETED-");
                                        messageImageViewRelativeLayout.setVisibility(View.GONE);
                                        videoMessageStyledPlayerView.setVisibility(View.GONE);
                                        messageStateIndicatorImageView.setImageResource(R.drawable.ic_baseline_sending_12);

//                viewGroup.removeView(lessonNoteView);
//              viewGroup.indexOfChild(lessonNoteView);
                                    }
                                } else {
                                    Toast.makeText(context, "No network, please try and connect!", Toast.LENGTH_SHORT).show();
                                }
                            }
//            else{
//    Toast.makeText(context, "No network, please try and connect!", Toast.LENGTH_SHORT).show();
//}


                            return true;
                        }
                    });

                }
                return true;
            }
        });

        messageSenderDisplayNameTextView.setText(senderDisplayName);
        dateSentTextView.setText(getFormattedDate(dateSent));
        textMessageTextView.setText(textMessage);
        imageMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewImagePreview(context,imageMessageImageView, imageDownloadUrl);
            }
        });

        imageMessageImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                createPopUpMenu(context, R.menu.image_message_menu_action, imageMessageImageView, new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClicked(MenuItem item) {

                        if (item.getItemId() == R.id.saveImageId) {
                            saveImageToLocalStorage(context, imageMessageImageView.getDrawingCache(true), textMessage, new ActionCallback() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailed(String message) {

                                }
                            });

                        }


                        return true;
                    }
                });


                return true;
            }
        });

        if(isTextIncluded){
            textMessageTextView.setVisibility(View.VISIBLE);
        }

        if(isImageIncluded){
            messageImageViewRelativeLayout.setVisibility(View.VISIBLE);
            imageMessageImageView.setVisibility(View.VISIBLE);
            if(imageDownloadUrl != null  && !isSendImage) {
//                Glide.with(context).load(imageDownloadUrl).into(imageMessageImageView);

                Picasso.get().load(imageDownloadUrl).error(R.drawable.ic_baseline_image_24).into(imageMessageImageView);
            }
        }
        if(isSendImage){
            if(sendingImageView != null) {
                sendingImageView.setDrawingCacheEnabled(true);
                sendingImageView.buildDrawingCache();
                imageMessageImageView.setImageBitmap(((BitmapDrawable) sendingImageView.getDrawable()).getBitmap());
            }else {
                if (imageDownloadUrl != null) {
//                Glide.with(context).load(imageDownloadUrl).into(imageMessageImageView);
//                    imageMessageImageView.setImageURI(Uri.parse(imageDownloadUrl));
                }
            }
            String imageId = getRandomString(60);

            messageImageViewRelativeLayout.setVisibility(View.VISIBLE);
            imageMessageImageView.setVisibility(View.VISIBLE);
            pauseUploadImageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
//            if(sendingImageView != null) {
//                imageMessageImageView.setImageBitmap(sendingImageView.getDrawingCache());
//            }

            StorageReference sentMessageImageStorageReference = FirebaseStorage.getInstance().getReference().child("ALL_MESSAGES/"+currentSenderId+"/MESSAGE_IMAGES/"+imageId+".PNG");
            imageMessageImageView.setDrawingCacheEnabled(true);

            imageMessageImageView.setDrawingCacheEnabled(true);
            imageMessageImageView.buildDrawingCache();

            Bitmap messageImageBitmap = null;
            if(sendingImageView != null) {
                messageImageBitmap = ((BitmapDrawable) sendingImageView.getDrawable()).getBitmap();
            }else{
                messageImageBitmap = ((BitmapDrawable) imageMessageImageView.getDrawable()).getBitmap();

            }


//            Bitmap messageImageBitmap = imageMessageImageView.getDrawingCache();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            messageImageBitmap.compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();

            UploadTask messageImageUploadTask = sentMessageImageStorageReference.putBytes(bytes);

            pauseUploadImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageImageUploadTask.pause();
                    progressBar.setVisibility(View.GONE);
                    pauseUploadImageView.setVisibility(View.GONE);
                    resumeUploadImageView.setVisibility(View.VISIBLE);
                }
            });

            resumeUploadImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageImageUploadTask.resume();
                    progressBar.setVisibility(View.VISIBLE);
                    resumeUploadImageView.setVisibility(View.GONE);
                    pauseUploadImageView.setVisibility(View.VISIBLE);
                }
            });


            messageImageUploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "failed to upload", Toast.LENGTH_SHORT).show();
                            startUploadImageView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            pauseUploadImageView.setVisibility(View.GONE);
                            resumeUploadImageView.setVisibility(View.GONE);

                        }
                    });

                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                            deleteVideoButton.setVisibility(View.GONE);
                            double uploadSize = snapshot.getTotalByteCount();
                            double uploadedSize = snapshot.getBytesTransferred();
                            double remainingSize = uploadSize - uploadedSize;
                            int uploadProgress = (int) ((100 * uploadedSize) / uploadSize);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(uploadProgress);

                                }
                            });


                            // Toast.makeText(context, "progressing..." + uploadProgress, Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //Toast.makeText(context, "image uploaded", Toast.LENGTH_SHORT).show();

                            messageImageUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!(task.isSuccessful())) {
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(context, ""+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }

                                    return sentMessageImageStorageReference.getDownloadUrl();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(context, "failed to upload", Toast.LENGTH_SHORT).show();
                                                    startUploadImageView.setVisibility(View.VISIBLE);
                                                    progressBar.setVisibility(View.GONE);
                                                    pauseUploadImageView.setVisibility(View.GONE);
                                                    resumeUploadImageView.setVisibility(View.GONE);

                                                }
                                            });


                                        }
                                    })
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            // Toast.makeText(context, "Task completed", Toast.LENGTH_SHORT).show();


                                            String sentMessageImageDownloadUrl = String.valueOf(task.getResult());
                                            onMessageImageUploadListener.onUploadSuccess(messageIdArray[0] ,sentMessageImageDownloadUrl, sentMessageImageStorageReference.getPath() );
                                            startUploadImageView.setVisibility(View.GONE);
                                            progressBar.setVisibility(View.GONE);
                                            pauseUploadImageView.setVisibility(View.GONE);
                                            resumeUploadImageView.setVisibility(View.GONE);
                                            successImageView.setVisibility(View.VISIBLE);


                                        }
                                    });

                        }
                    });
        }

        if(isVideoIncluded){
            videoMessageStyledPlayerView.setVisibility(View.VISIBLE);
//            displayE
        }

        senderIdHolderDummyTextView.setText(senderId);
        recipientIdHolderDummyTextView.setText(recipientId);
        messageIdHolderDummyTextView.setText(messageId);

        if(isCurrentUserTheSender){
//            LinearLayout.LayoutParams bodyMainCardViewMessageLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//            bodyMainCardViewMessageLayoutParams.setMargins(100,10,10,10);
//            bodyMessageMainCardView.setLayoutParams(bodyMainCardViewMessageLayoutParams);
            leftDummyView.setVisibility(View.VISIBLE);
            messageStateIndicatorImageView.setVisibility(View.VISIBLE);
            rightDummyView.setVisibility(View.GONE);
            bodyMessageMainLinearLayout.setBackgroundColor(Color.RED);
//            LinearLayout.LayoutParams textMessageLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//            LinearLayout.LayoutParams imageMessageLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,170);
//            LinearLayout.LayoutParams videoMessageLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,200);
//
//            textMessageLayoutParams.setMarginStart(100);
//            imageMessageLayoutParams.setMarginStart(100);
//            videoMessageLayoutParams.setMarginStart(100);

            messageSenderDisplayNameTextView.setGravity(Gravity.END);
            messageSenderDisplayNameTextView.setTextColor(Color.BLUE);
            textMessageTextView.setForegroundGravity(Gravity.END);


//            textMessageTextView.setLayoutParams(textMessageLayoutParams);
//            imageMessageImageView.setLayoutParams(imageMessageLayoutParams);
//            videoMessageStyledPlayerView.setLayoutParams(videoMessageLayoutParams);

            dateSentTextView.setGravity(Gravity.END);
            dateSentTextView.setTextColor(Color.BLACK);
//            mainLinearLayout.setPadding(100,10,10,10);


            if(!isLoadFromSnapshot) {
                if (!hasPendingWrites) {
                    messageStateIndicatorImageView.setImageResource(R.drawable.ic_baseline_sent_12);


                    if (isDeliveredToRecipient) {
                        messageStateIndicatorImageView.setImageResource(R.drawable.ic_baseline_delivered_12);
                        if (isSeenByRecipient) {
                            messageStateIndicatorImageView.setImageResource(R.drawable.ic_baseline_seen_blue_eye_12);
                        }
                    }
                }
            }

            //Listen to when the message is delivered and or seen
            if(!isSeenByRecipient && !hasPendingWrites){
                messageDocumentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        boolean[] isDeliveredToRecipient = new boolean[1];
                        boolean[] isSeenByRecipient = new boolean[1];

                        isDeliveredToRecipient[0] = false;
                        isSeenByRecipient[0] = false;
                        if(value != null ) {
                            if(value.exists()) {

                                if (value.get("IS_DELIVERED_" +recipientId) != null) {
                                    isDeliveredToRecipient[0] = value.getBoolean("IS_DELIVERED_" + recipientId);
                                }
                                if (value.get("IS_SEEN_" +recipientId) != null) {
                                    isSeenByRecipient[0] = value.getBoolean("IS_SEEN_" + recipientId);
                                }

                                if(isDeliveredToRecipient[0]){
                                    messageStateIndicatorImageView.setImageResource(R.drawable.ic_baseline_delivered_12);
                                }
                                if(isSeenByRecipient[0]){
                                    messageStateIndicatorImageView.setImageResource(R.drawable.ic_baseline_seen_blue_eye_12);
                                }
                            }else{
                                messageStateIndicatorImageView.setImageResource(R.drawable.ic_baseline_error_12);

                            }}

                    }
                });
            }


        }
        else{
            //set the left separator visible to indicator that it was the current user who sent the message
            leftDummyView.setVisibility(View.GONE);
            rightDummyView.setVisibility(View.VISIBLE);
        }


        //the tag is not useful right now
        lessonNoteView.setTag(viewGroup.getChildCount());
        if(isLoadFromSnapshot || isSendImage) {
            viewGroup.addView(lessonNoteView);

            if(ChatRoomActivity.topScrollView != null){
                ChatRoomActivity.topScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        ChatRoomActivity.topScrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        }else{
            viewGroup.addView(lessonNoteView,0);

        }


        if(!isDeliveredToCurrentUser){
            //message has not delivered to current user and also it has not been seen by current user
            setIsAlreadyDeliveredAndReadFlag(context,allMessagesDocumentDirectoryReference,messageDocumentReference,currentSenderId);
        }
        else{
            if(!isSeenByCurrentUser){
                //message has delivered to current user but  it has not been seen by current user
                setIsAlreadySeenFlag(context,allMessagesDocumentDirectoryReference,messageDocumentReference,currentSenderId);
            }
        }

    }
*/


    void displayLessonNote(boolean isSendPlainText,boolean isSendImage, ArrayList<Object> lessonNoteInfo, String lessonNoteId, String lessonNotePlainText,@Nullable Uri localImageUriToSend,@Nullable Bitmap bitmapToSend, String dateSent ) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View lessonNoteView = layoutInflater.inflate(R.layout.lesson_note_item_layout, lessonLinearLayout, false);

        RoundedImageView senderIcon = lessonNoteView.findViewById(R.id.senderIcon);
        TextView messageSenderDisplayNameTextView = lessonNoteView.findViewById(R.id.senderNameId);
        TextView lessonNotePlainTextView = lessonNoteView.findViewById(R.id.lessonNotePlainTextViewId);
        TextView dateSentTextView = lessonNoteView.findViewById(R.id.dateSentViewId);


        RelativeLayout lessonNoteImageViewRelativeLayout = lessonNoteView.findViewById(R.id.lessonNoteImageViewRelativeLayoutId);
        ImageView imageLessonNoteImageView = lessonNoteView.findViewById(R.id.imageLessonNoteImageViewId);

        FloatingActionButton startUploadImageView = lessonNoteView.findViewById(R.id.startUploadButtonId);
        FloatingActionButton successImageView = lessonNoteView.findViewById(R.id.successImageViewId);

        FloatingActionButton pauseUploadImageView = lessonNoteView.findViewById(R.id.pauseUploadButtonId);

        FloatingActionButton resumeUploadImageView = lessonNoteView.findViewById(R.id.resumeUploadButtonId);

        ProgressBar progressBar = lessonNoteView.findViewById(R.id.progressIndicatorId);

        ImageView lessonNoteStateIndicatorImageView = lessonNoteView.findViewById(R.id.lessonNoteStateIndicatorImageViewId);

        TextView lessonNoteIdHolderDummyTextView = lessonNoteView.findViewById(R.id.lessonNoteIdHolderDummyTextViewId);

        if(isSendPlainText){
            lessonNoteStateIndicatorImageView.setVisibility(View.VISIBLE);
            lessonNotePlainTextView.setText(lessonNotePlainText);
            lessonNoteIdHolderDummyTextView.setText(lessonNoteId);

            dateSentTextView.setText("Now");

        }
        if (isSendImage) {
            lessonNoteStateIndicatorImageView.setVisibility(View.VISIBLE);
            String imageId = GlobalConfig.getRandomString(60);
            if (bitmapToSend != null) {
                imageLessonNoteImageView.setImageBitmap(bitmapToSend);
            } else {
                imageLessonNoteImageView.setImageURI(localImageUriToSend);

            }
            lessonNotePlainTextView.setText(lessonNotePlainText);
            lessonNoteIdHolderDummyTextView.setText(lessonNoteId);

            dateSentTextView.setText("Now");

            lessonNoteImageViewRelativeLayout.setVisibility(View.VISIBLE);
            imageLessonNoteImageView.setVisibility(View.VISIBLE);
            pauseUploadImageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            StorageReference sentMessageImageStorageReference = FirebaseStorage.getInstance().getReference().child(GlobalConfig.ALL_CLASS_KEY + "/" + GlobalConfig.getCurrentUserId() + "/" + GlobalConfig.LESSON_NOTE_IMAGES_KEY + "/" + imageId + ".PNG");
            imageLessonNoteImageView.setDrawingCacheEnabled(true);
            imageLessonNoteImageView.buildDrawingCache();

            Bitmap messageImageBitmap = ((BitmapDrawable) imageLessonNoteImageView.getDrawable()).getBitmap();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            messageImageBitmap.compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();

            UploadTask messageImageUploadTask = sentMessageImageStorageReference.putBytes(bytes);

            pauseUploadImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageImageUploadTask.pause();
                    progressBar.setVisibility(View.GONE);
                    pauseUploadImageView.setVisibility(View.GONE);
                    resumeUploadImageView.setVisibility(View.VISIBLE);
                }
            });

            resumeUploadImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageImageUploadTask.resume();
                    progressBar.setVisibility(View.VISIBLE);
                    resumeUploadImageView.setVisibility(View.GONE);
                    pauseUploadImageView.setVisibility(View.VISIBLE);
                }
            });


            messageImageUploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "failed to upload", Toast.LENGTH_SHORT).show();
                            startUploadImageView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            pauseUploadImageView.setVisibility(View.GONE);
                            resumeUploadImageView.setVisibility(View.GONE);

                        }
                    });

                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                            deleteVideoButton.setVisibility(View.GONE);
                            double uploadSize = snapshot.getTotalByteCount();
                            double uploadedSize = snapshot.getBytesTransferred();
                            double remainingSize = uploadSize - uploadedSize;
                            int uploadProgress = (int) ((100 * uploadedSize) / uploadSize);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(uploadProgress);

                                }
                            });


                            // Toast.makeText(context, "progressing..." + uploadProgress, Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //Toast.makeText(context, "image uploaded", Toast.LENGTH_SHORT).show();

                            messageImageUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!(task.isSuccessful())) {
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(ClassActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }

                                    return sentMessageImageStorageReference.getDownloadUrl();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(ClassActivity.this, "failed to upload", Toast.LENGTH_SHORT).show();
                                                    startUploadImageView.setVisibility(View.VISIBLE);
                                                    progressBar.setVisibility(View.GONE);
                                                    pauseUploadImageView.setVisibility(View.GONE);
                                                    resumeUploadImageView.setVisibility(View.GONE);

                                                }
                                            });


                                        }
                                    })
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            // Toast.makeText(context, "Task completed", Toast.LENGTH_SHORT).show();


                                            String sentMessageImageDownloadUrl = String.valueOf(task.getResult());
//                                    onMessageImageUploadListener.onUploadSuccess(messageIdArray[0], sentMessageImageDownloadUrl, sentMessageImageStorageReference.getPath());
                                            startUploadImageView.setVisibility(View.GONE);
                                            progressBar.setVisibility(View.GONE);
                                            pauseUploadImageView.setVisibility(View.GONE);
                                            resumeUploadImageView.setVisibility(View.GONE);
                                            successImageView.setVisibility(View.VISIBLE);


                                            ArrayList<Object> lessonInfoList = new ArrayList<>();

//the type of lesson whether it's plain,image,video, or other types.
                                            lessonInfoList.add(0, GlobalConfig.IS_IMAGE_LESSON_NOTE_TYPE_KEY);
//the lesson note id
                                            lessonInfoList.add(1, lessonNoteId);
//the lesson note itself
                                            lessonInfoList.add(2, lessonNotePlainText + GlobalConfig.MEDIA_URL_KEY+ sentMessageImageDownloadUrl);
//sender id
                                            lessonInfoList.add(3, GlobalConfig.getCurrentUserId());
////date sent
//                                    lessonInfoList.add(4,FieldValue.serverTimestamp());


                                            postLessonNote(lessonNoteId, lessonInfoList, new GlobalConfig.ActionCallback() {
                                                @Override
                                                public void onSuccess() {
//                                                    Toast.makeText(getApplicationContext(), "lesson note: " + lessonNotePlainText + " posted", Toast.LENGTH_SHORT).show();
                                                    lessonNoteStateIndicatorImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_sent_12,getTheme()));

                                                }

                                                @Override
                                                public void onFailed(String errorMessage) {

                                                }
                                            });


                                        }
                                    });
                        }
                    });

        }

        else {
            String lessonNoteType = lessonNoteInfo.get(0) + "";
            //the lesson note id
            String lessonNoteId1 = lessonNoteInfo.get(1) + "";
            //the lesson note itself
            String lessonNote = lessonNoteInfo.get(2) + "";
            //sender id
            String senderId = lessonNoteInfo.get(3) + "";
            if(dateSent.length()>=10){
                dateSent = dateSent.substring(10);
            }
            dateSentTextView.setText(dateSent);
            lessonNoteIdHolderDummyTextView.setText(lessonNoteId1);
            switch (lessonNoteType) {
                case GlobalConfig.IS_PLAIN_TEXT_LESSON_NOTE_TYPE_KEY:
                    lessonNotePlainTextView.setText(lessonNote);
                    break;
                case GlobalConfig.IS_IMAGE_LESSON_NOTE_TYPE_KEY:

                    lessonNoteImageViewRelativeLayout.setVisibility(View.VISIBLE);
                    String lessonNotePlainText1 = lessonNote.split(GlobalConfig.MEDIA_URL_KEY)[0];
                    lessonNotePlainTextView.setText(lessonNotePlainText1);

                    String lessonNoteImageDownloadUrl = lessonNote.split(GlobalConfig.MEDIA_URL_KEY)[1];
                    imageLessonNoteImageView.setVisibility(View.VISIBLE);
                    Glide.with(ClassActivity.this)
                            .load(lessonNoteImageDownloadUrl)
                            .centerCrop()
                            .placeholder(R.drawable.learn_era_logo)
                            .into(imageLessonNoteImageView);
                    break;
                case GlobalConfig.IS_VIDEO_LESSON_NOTE_TYPE_KEY:

                    break;
                case GlobalConfig.IS_AUDIO_LESSON_NOTE_TYPE_KEY:

                    break;
            }
        }

        //checks if the lesson note is already displayed. if it's displayed then ignore adding it to avoid duplicates, but if it's not displayed then display it
       if(!renderedLessonNoteIds.contains(lessonNoteId)){
           lessonLinearLayout.addView(lessonNoteView);
           renderedLessonNoteIds.add(lessonNoteId);

       }

    }

    void analyseAndRenderLessonNotes(DocumentSnapshot documentSnapshot){
        ArrayList<String> allLessonNoteIdList =  documentSnapshot.get(GlobalConfig.ALL_LESSON_ID_LIST_KEY) != null && documentSnapshot.get(GlobalConfig.ALL_LESSON_ID_LIST_KEY) instanceof ArrayList ? (ArrayList) documentSnapshot.get(GlobalConfig.ALL_LESSON_ID_LIST_KEY) : new ArrayList();
        for(String lessonNoteId : allLessonNoteIdList){

            ArrayList<Object> lessonNoteInfo =  documentSnapshot.get(lessonNoteId) != null && documentSnapshot.get(lessonNoteId) instanceof ArrayList ? (ArrayList) documentSnapshot.get(lessonNoteId) : new ArrayList();
            String dateSent =  documentSnapshot.get(lessonNoteId+GlobalConfig.DATE_SENT_KEY) != null && documentSnapshot.get(lessonNoteId+GlobalConfig.DATE_SENT_KEY) instanceof Timestamp ?  documentSnapshot.getTimestamp(lessonNoteId+GlobalConfig.DATE_SENT_KEY).toDate()+"" : "Undefined";
            displayLessonNote(false,false,lessonNoteInfo,lessonNoteId,"",null,null,dateSent);

//            Toast.makeText(ClassActivity.this, lessonNoteId, Toast.LENGTH_SHORT).show();

        }
        }

        void raiseHand(GlobalConfig.ActionCallback actionCallback){
           WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
           DocumentReference documentReference1 = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).document(classId);
           HashMap<String,Object> quizDetails = new HashMap<>();
           quizDetails.put(GlobalConfig.HAND_RAISERS_LIST_KEY,FieldValue.arrayUnion(GlobalConfig.getCurrentUserId()));


           writeBatch.set(documentReference1,quizDetails,SetOptions.merge());

           writeBatch.commit()
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           actionCallback.onFailed(e.getMessage());
                       }
                   })
                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void unused) {
                           actionCallback.onSuccess();
                       }
                   });
    }
        void permitRaisedHand(String handRaiserId, GlobalConfig.ActionCallback actionCallback){
               WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
               DocumentReference documentReference1 = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).document(classId);
               HashMap<String,Object> quizDetails = new HashMap<>();
               quizDetails.put(GlobalConfig.HAND_RAISERS_LIST_KEY,FieldValue.arrayRemove(handRaiserId));
               quizDetails.put(GlobalConfig.PERMITTED_HAND_RAISERS_LIST_KEY,FieldValue.arrayUnion(handRaiserId));


               writeBatch.set(documentReference1,quizDetails,SetOptions.merge());

               writeBatch.commit()
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               actionCallback.onFailed(e.getMessage());
                           }
                       })
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               actionCallback.onSuccess();
                           }
                       });
        }
        void removePermittedRaisedHand(String handRaiserId, GlobalConfig.ActionCallback actionCallback){
               WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
               DocumentReference documentReference1 = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).document(classId);
               HashMap<String,Object> quizDetails = new HashMap<>();
               quizDetails.put(GlobalConfig.HAND_RAISERS_LIST_KEY,FieldValue.arrayRemove(handRaiserId));
               quizDetails.put(GlobalConfig.PERMITTED_HAND_RAISERS_LIST_KEY,FieldValue.arrayRemove(handRaiserId));


               writeBatch.set(documentReference1,quizDetails,SetOptions.merge());

               writeBatch.commit()
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               actionCallback.onFailed(e.getMessage());
                           }
                       })
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               actionCallback.onSuccess();
                           }
                       });
        }
        void unlockDiscussion(GlobalConfig.ActionCallback actionCallback){
               WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
               DocumentReference documentReference1 = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).document(classId);
               HashMap<String,Object> quizDetails = new HashMap<>();
               quizDetails.put(GlobalConfig.IS_DISCUSSION_LOCKED_KEY,false);


               writeBatch.set(documentReference1,quizDetails,SetOptions.merge());

               writeBatch.commit()
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               actionCallback.onFailed(e.getMessage());
                           }
                       })
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               actionCallback.onSuccess();
                           }
                       });
        }
        void lockDiscussion(GlobalConfig.ActionCallback actionCallback){
               WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
               DocumentReference documentReference1 = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).document(classId);
               HashMap<String,Object> quizDetails = new HashMap<>();
               quizDetails.put(GlobalConfig.IS_DISCUSSION_LOCKED_KEY,false);


               writeBatch.set(documentReference1,quizDetails,SetOptions.merge());

               writeBatch.commit()
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               actionCallback.onFailed(e.getMessage());
                           }
                       })
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               actionCallback.onSuccess();
                           }
                       });
        }

        void notifyStudentsWhetherTeacherIsTyping(boolean isTeacherTyping){
           WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
           DocumentReference documentReference1 = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_CLASS_KEY).document(classId);
           HashMap<String,Object> quizDetails = new HashMap<>();
           quizDetails.put(GlobalConfig.IS_TEACHER_TYPING_KEY,isTeacherTyping);


           writeBatch.set(documentReference1,quizDetails,SetOptions.merge());

           writeBatch.commit();

    }

        void renderHandRaisers(DocumentSnapshot documentSnapshot){
            ArrayList<String> handRaisersIdList =  documentSnapshot.get(GlobalConfig.HAND_RAISERS_LIST_KEY) != null && documentSnapshot.get(GlobalConfig.HAND_RAISERS_LIST_KEY) instanceof ArrayList ? (ArrayList) documentSnapshot.get(GlobalConfig.HAND_RAISERS_LIST_KEY) : new ArrayList();
            for(String handRaiserId : handRaisersIdList){
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View handRaiserView = layoutInflater.inflate(R.layout.hand_raiser_item_layout, handRaisersLinearLayout, false);

                ImageButton handIcon = handRaiserView.findViewById(R.id.handIcon);
                ImageButton declineActionButton = handRaiserView.findViewById(R.id.declineActionButtonId);
                ImageButton permitActionButton = handRaiserView.findViewById(R.id.permitActionButtonId);
                TextView handRaiserNameTextView = handRaiserView.findViewById(R.id.handRaiserNameTextViewId);
                TextView handRaisingCaptionTextView = handRaiserView.findViewById(R.id.handRaisingCaptionTextViewId);
                if(studentsName.containsKey(handRaiserId)){
                    handRaiserNameTextView.setText(studentsName.get(handRaiserId));
                }
                if(!authorId.equals(GlobalConfig.getCurrentUserId())) {
                    //hide this button if the current user is not the author
                    permitActionButton.setVisibility(View.GONE);
                    if(!handRaiserId.equals(GlobalConfig.getCurrentUserId())) {
                        //hide this button if the current user is not the handraiser
                        declineActionButton.setVisibility(View.GONE);

                    }
                    }
                declineActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handRaisersLinearLayout.removeView(handRaiserView);
                        removePermittedRaisedHand(handRaiserId, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFailed(String errorMessage) {

                            }
                        });
                    }
                });
                permitActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permitActionButton.setVisibility(View.GONE);
                        permitRaisedHand(handRaiserId, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFailed(String errorMessage) {

                            }
                        });

                    }
                });
                if(!renderedHandRaisersIds.contains(handRaiserId)){
                    Toast.makeText(ClassActivity.this, "Hand raised", Toast.LENGTH_SHORT).show();
                    handRaisersLinearLayout.addView(handRaiserView);
                    renderedHandRaisersIds.add(handRaiserId);

                }
            }
        }



    }

