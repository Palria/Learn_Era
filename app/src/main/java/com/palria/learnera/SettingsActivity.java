package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    ImageButton backButton;
    Button submitActionButton;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        fetchIntentData();
        iniUI();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsActivity.super.onBackPressed();
            }
        });
        if(!GlobalConfig.isAccountSubmittedForVerification() && !GlobalConfig.isCurrentUserAccountVerified()) {
            submitActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submitAccountForVerification();
                }
            });
        }else{
            submitActionButton.setOnClickListener(null);
            submitActionButton.setText("Account verification in progress");
            if( GlobalConfig.isCurrentUserAccountVerified()){
                submitActionButton.setText("Account verified");
            }
        }

    }

    void fetchIntentData(){
        Intent intent = getIntent();

    }
    void iniUI(){
        backButton = findViewById(R.id.backButton);
        submitActionButton = findViewById(R.id.submitActionButtonId);

        //init progress.
        alertDialog = new AlertDialog.Builder(SettingsActivity.this)
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
    void submitAccountForVerification(){
        toggleProgress(true);
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
        DocumentReference userDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(GlobalConfig.getCurrentUserId());
        HashMap<String,Object> verifyDetails = new HashMap<>();
        verifyDetails.put(GlobalConfig.IS_SUBMITTED_FOR_VERIFICATION_KEY,true);
        verifyDetails.put(GlobalConfig.DATE_VERIFICATION_SUBMITTED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        writeBatch.update(userDocumentReference,verifyDetails);

        writeBatch.commit().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toggleProgress(false);
                GlobalConfig.createSnackBar(SettingsActivity.this,submitActionButton, "Account verification submission failed", Snackbar.LENGTH_INDEFINITE);
                submitActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        submitAccountForVerification();
                    }
                });


            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                toggleProgress(false);
                GlobalConfig.createSnackBar(SettingsActivity.this,submitActionButton, "Account verification submission succeeded", Snackbar.LENGTH_LONG);
                submitActionButton.setText("Account verification in progress");
                GlobalConfig.setIsAccountSubmittedForVerification(true);

                submitActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

            }
        });
    }


}