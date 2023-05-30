package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.WriteBatch;
import com.makeramen.roundedimageview.RoundedImageView;
import com.palria.learnera.models.UsersDataModel;

import java.util.ArrayList;
import java.util.HashMap;

public class DeclineUserAccountVerificationActivity extends AppCompatActivity {
    ImageButton backButton;
    MaterialButton declineActionButton;
    RoundedImageView profileImageView;
    TextView userNameTextView;
    String userId ="";
    String userName ="";
    String userIconDownloadUrl ="";


    CheckedTextView lessMonthReason;
    CheckedTextView emailAddressReason;
    CheckedTextView authorReason;
    CheckedTextView otherReasons;

    ArrayList<String> reasonList = new ArrayList<>();
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decline_user_account_verification);
        iniUI();
        fetchIntentData();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeclineUserAccountVerificationActivity.super.onBackPressed();
            }
        });

        declineActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                declineVerification();

            }
        });
            renderUser();

    }

    void fetchIntentData(){
        Intent intent = getIntent();
        userId = intent.getStringExtra(GlobalConfig.USER_ID_KEY);
        userIconDownloadUrl = intent.getStringExtra(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
        userName = intent.getStringExtra(GlobalConfig.USER_DISPLAY_NAME_KEY);
    }


    private void iniUI(){
        backButton = findViewById(R.id.backButton);
        declineActionButton = findViewById(R.id.declineActionButtonId);
        profileImageView = findViewById(R.id.profileImageViewId);
        userNameTextView = findViewById(R.id.userNameTextViewId);
        lessMonthReason = findViewById(R.id.lessMonthReasonId);
        authorReason = findViewById(R.id.notAuthorReasonId);
        emailAddressReason = findViewById(R.id.emailAddressReasonId);
        otherReasons = findViewById(R.id.otherReasonsId);


        //init progress.
        alertDialog = new AlertDialog.Builder(DeclineUserAccountVerificationActivity.this)
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();

    }
    private void toggleProgress(boolean show){
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
    }

    private void renderUser(){

        Glide.with(DeclineUserAccountVerificationActivity.this)
                .load(userIconDownloadUrl)
                .centerCrop()
                .into(profileImageView);
        userNameTextView.setText(userName);
    }

    private void declineVerification(){
        toggleProgress(true);
        if(lessMonthReason.isChecked()){
            reasonList.add(lessMonthReason.getText()+"");
        }
          if(authorReason.isChecked()){
              reasonList.add(authorReason.getText()+"");
          }
          if(emailAddressReason.isChecked()){
              reasonList.add(emailAddressReason.getText()+"");
          }
          if(otherReasons.isChecked()){
              reasonList.add(otherReasons.getText()+"");
          }

            WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
            DocumentReference userDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(userId);
            HashMap<String,Object> verifyDetails = new HashMap<>();
            verifyDetails.put(GlobalConfig.IS_ACCOUNT_VERIFICATION_DECLINED_KEY,true);
            verifyDetails.put(GlobalConfig.IS_ACCOUNT_VERIFIED_KEY,false);
            verifyDetails.put(GlobalConfig.IS_SUBMITTED_FOR_VERIFICATION_KEY,false);
            verifyDetails.put(GlobalConfig.IS_ACCOUNT_VERIFICATION_DECLINE_SEEN_KEY,false);
            verifyDetails.put(GlobalConfig.DATE_DECLINED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
            verifyDetails.put(GlobalConfig.ACCOUNT_VERIFICATION_DECLINE_REASONS_LIST_KEY, reasonList);
            writeBatch.update(userDocumentReference,verifyDetails);

            writeBatch.commit().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    toggleProgress(false);
                    GlobalConfig.createSnackBar(DeclineUserAccountVerificationActivity.this,declineActionButton, "User verification failed to decline", Snackbar.LENGTH_INDEFINITE);



                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    toggleProgress(false);
                    GlobalConfig.createSnackBar(DeclineUserAccountVerificationActivity.this,declineActionButton, "User verification declined", Snackbar.LENGTH_LONG);
                }
            });

    }
}