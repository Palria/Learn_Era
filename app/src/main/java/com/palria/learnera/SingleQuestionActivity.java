package com.palria.learnera;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class SingleQuestionActivity extends AppCompatActivity {
String email;
EditText emailEditText;
Button sendEmailActionButton;
AlertDialog alertDialog;
TextView mLoginLink;
private TextView errorMessageTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_change_password);
        initUI();

        sendEmailActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailEditText.getText().toString();
              //check email not empty and valid?
                if(email==null || email.trim().equals("")){


                    errorMessageTextView.setText("Enter a valid email to receive reset link!");
                    errorMessageTextView.setVisibility(View.VISIBLE);
                    return;
                }
                toggleProgress(true);
                sendEmailResetLink(new SendLinkListener() {
                    @Override
                    public void onSuccess() {
                        /*
                        *Email reset link sent to user's provided email
                        *address, inform him to open his inbox and click on the link
                        * to proceed resetting his password
                        */
                        toggleProgress(false);
                        //show success

                        GlobalHelpers.showAlertMessage("success",
                                SingleQuestionActivity.this,
                                "Password Reset Link Sent",
                                "we have successfully sent your password reset link. check your email now and click the link");



                    }

                    @Override
                    public void onFailed(String errorMessage) {
                        toggleProgress(false);
                        //Take an action when the password reset process fails
                        errorMessageTextView.setText(errorMessage + "  Please try again!");
                        errorMessageTextView.setVisibility(View.VISIBLE);
                    }


                });
            }
        });

        //login link click listener
        mLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SingleQuestionActivity.this, SignInActivity.class);
                startActivity(i);
                SingleQuestionActivity.super.onBackPressed();

            }
        });
    }
//

    /**
     * Initializes the Activity's widgets
     * */
    private void initUI(){

    emailEditText= findViewById(R.id.emailInput);
    sendEmailActionButton = findViewById(R.id.resetButton);
    mLoginLink = findViewById(R.id.login_link);
    errorMessageTextView = findViewById(R.id.errorMessage);

    alertDialog = new AlertDialog.Builder(SingleQuestionActivity.this)
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();

    }


    private void toggleProgress(boolean show)
    {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.hide();
        }
    }


    private void sendEmailResetLink(SendLinkListener sendLinkListener) {
        if (email != null && !email.isEmpty()) {
            String trimmedEmail = email.trim();
            FirebaseAuth.getInstance().sendPasswordResetEmail(trimmedEmail)
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
        }else{
            //Tell the user to input his email address
        }
    }

    /**
     * A callback triggered either if the link is successfully set or failed to send
     * */
    interface SendLinkListener{
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



}