package com.palria.learnera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.palria.learnera.models.AnswerDataModel;
import com.palria.learnera.models.QuestionDataModel;
import com.palria.learnera.widgets.BottomSheetFormBuilderWidget;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class SingleQuestionActivity extends AppCompatActivity {



    String authorId;
    String question;
    String questionId;
    String questionPhotoDownloadUrl = "";
    TextView questionBodyText;
    ImageView questionPhoto;
    TextView ansCountTextView;
    TextView viewCountTextView;
    TextView dateAskedTextView;
    TextView askerNameTextView;
    ImageButton backButton;
    ImageView askerPhoto;
    FloatingActionButton answerQuestionActionButton;
    AlertDialog alertDialog;
    QuestionDataModel questionDataModel;
    BottomSheetFormBuilderWidget bottomSheetCatalogFormBuilderWidget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_single_question);
        initUI();
        fetchIntentData();
        renderQuestion();
        initAnswerFragment();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            SingleQuestionActivity.super.onBackPressed();

            }
        });
        answerQuestionActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AllAnswersFragment.addAnswerListener!=null) {
                    AllAnswersFragment.addAnswerListener.onAddAnswerTriggered(null, "",false,true);
                }
//                bottomSheetCatalogFormBuilderWidget.show();
            }
        });
    }
//

    /**
     * Initializes the Activity's widgets
     * */
    private void initUI(){
        backButton= findViewById(R.id.backButtonId);
        questionBodyText= findViewById(R.id.questionBodyTextViewId);
        answerQuestionActionButton = findViewById(R.id.addAnswerFloatingButtonId);
        questionPhoto = findViewById(R.id.questionPhotoImageViewId);
        ansCountTextView = findViewById(R.id.ansCountTextViewId);
        viewCountTextView = findViewById(R.id.viewCountTextViewId);
        dateAskedTextView = findViewById(R.id.dateAskedTextViewId);
        askerNameTextView = findViewById(R.id.posterNameTextViewId);
        askerPhoto = findViewById(R.id.askerProfilePhotoId);
        alertDialog = new AlertDialog.Builder(SingleQuestionActivity.this)
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();

    }

    private void fetchIntentData(){
        Intent intent = getIntent();
        questionDataModel = (QuestionDataModel) intent.getSerializableExtra(GlobalConfig.QUESTION_DATA_MODEL_KEY);

    }


    private void toggleProgress(boolean show)
    {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.hide();
        }
    }
    private void renderQuestion(){


    authorId = questionDataModel.getAuthorId();
    question = questionDataModel.getQuestionBody();
    questionId = questionDataModel.getQuestionId();
    questionPhotoDownloadUrl = questionDataModel.getPhotoDownloadUrl();
    questionBodyText.setText(question);

        Glide.with(this)
                .load(questionPhotoDownloadUrl)
                .centerCrop()
                .into(questionPhoto);
        ansCountTextView.setText(questionDataModel.getNumOfAnswers()+" ans");
        viewCountTextView.setText(questionDataModel.getNumOfViews()+"");
        dateAskedTextView.setText(questionDataModel.getDateAsked()+"");

        GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY)
                .document(questionDataModel.getAuthorId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final String userName = ""+ documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                        final String userProfilePhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);

                        askerNameTextView.setText(userName);
                        try {
                            Glide.with(SingleQuestionActivity.this)
                                    .load(userProfilePhotoDownloadUrl)
                                    .centerCrop()
                                    .placeholder(R.drawable.default_profile)
                                    .into(askerPhoto);
                        } catch (Exception ignored) {
                        }
                    }
                });


    }

    private void initAnswerFragment(){
        AllAnswersFragment allAnswersFragment = new AllAnswersFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putBoolean(GlobalConfig.IS_FOR_PREVIEW_KEY,false);
        bundle.putBoolean(GlobalConfig.IS_FROM_QUESTION_CONTEXT_KEY,true);
        bundle.putBoolean(GlobalConfig.IS_VIEW_ALL_ANSWERS_FOR_SINGLE_QUESTION_KEY,true);
        bundle.putBoolean(GlobalConfig.IS_VIEW_SINGLE_ANSWER_REPLY_KEY,false);
        bundle.putString(GlobalConfig.AUTHOR_ID_KEY,authorId);
        bundle.putString(GlobalConfig.ANSWER_ID_KEY,"");
        bundle.putString(GlobalConfig.QUESTION_ID_KEY,questionId);
        allAnswersFragment.setArguments(bundle);
        try {
            fragmentManager.beginTransaction()
                    .replace(R.id.answersFrameLayoutId,allAnswersFragment )
                    .commit();
        }catch(Exception e){}

    }



    /**
     * A callback triggered either if the answer is successfully added or failed to add
     * */
    interface AnswerListener{
        /**
         * Triggered when the link is successfully sent to the given email address
         * */
        void onSuccess();
        /**
         * Triggered when the link fails to send to the email address
         * @param errorMessage the error message indicating the cause of the failure
         * */
        void onFailed(String errorMessage);
    }
    interface AnswerPhotoUploadListener{
        /**
         * Triggered when the answer photo is uploaded
         * */
        void onSuccess(String answer,boolean isPhotoIncluded,boolean isEdition,String answerPhotoDownloadUrl);
        /**
         * Triggered when the photo fails to upload
         * */
        void onFailed(String errorMessage);
    }



}