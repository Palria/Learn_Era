package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ChangePasswordActivity extends AppCompatActivity {
String email;
EditText emailEditText;
Button sendEmailActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initUI();
        email = emailEditText.getText().toString();
        sendEmailActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmailResetLink(new SendLinkListener() {
                    @Override
                    public void onSuccess() {
                        /*
                        *Email reset link sent to user's provided email
                        *address, inform him to open his inbox and click on the link
                        * to proceed resetting his password
                        */

                    }

                    @Override
                    public void onFailed(String errorMessage) {

                        //Take an action when the password reset process fails
                    }
                });
            }
        });
    }
//

    private void initUI(){

    }


    private void sendEmailResetLink(SendLinkListener sendLinkListener){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sendLinkListener.onFailed(e.getMessage());
            }
        })
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                sendLinkListener.onSuccess();
            }
        });
    }

    interface SendLinkListener{
        void onSuccess();
        void onFailed(String errorMessage);
    }
}