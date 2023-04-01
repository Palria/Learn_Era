package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;
public class SignInActivity extends AppCompatActivity {
private String email;
private String password;
private EditText emailEditText;
private EditText passwordEditText;
private Button signInActionButton;
//register and forget password link
private TextView register_link_view;
private TextView forget_password_link;
private TextView errorMessageTextView;

AlertDialog alertDialog;

    /**
     * This is a flag indicating when the sign in process finishes
     * */
boolean isInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_sign_in);
        //initializes this activity's views
        initUI();

        signInActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isInProgress) {
                    isInProgress = true;
                    //show logging in progress
                    toggleProgress(true);
                    email = emailEditText.getText().toString();
                    password = passwordEditText.getText().toString();
//                    Toast.makeText(getApplicationContext(), "Sign in progress...!", Toast.LENGTH_SHORT).show();
                    errorMessageTextView.setVisibility(View.VISIBLE);
                    errorMessageTextView.setText("Progress...");
                    GlobalConfig.signInUserWithEmailAndPassword(SignInActivity.this, email, password, new GlobalConfig.SignInListener() {
                        @Override
                        public void onSuccess(String email, String password) {
                            //user has successfully signed in
                            GlobalConfig.setCurrentUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_SIGN_IN_TYPE_KEY, GlobalConfig.getCurrentUserId(), null, null, null, null, null,   new GlobalConfig.ActionCallback() {
                                @Override
                                public void onSuccess() {

                                    isInProgress = false;
                                    //hide progress
                                    toggleProgress(false);
                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "You successfully signed in, go learn more, it is era of learning", Toast.LENGTH_LONG).show();
                                    SignInActivity.this.finish();
                                }

                                @Override
                                public void onFailed(String errorMessage) {

                                    isInProgress = false;
                                    //hide progress
                                    toggleProgress(false);
                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "You successfully signed in, go learn more, it is era of learning", Toast.LENGTH_LONG).show();
                                    SignInActivity.this.finish();
                                }
                            });
                        }

                        @Override
                        public void onFailed(String errorMessage) {
                            // account sign in failed//
//                        Toast.makeText(getApplicationContext(), "Sign in failed: "+errorMessage+" please try again!", Toast.LENGTH_SHORT).show();
                            isInProgress = false;
                            //hide progress
                            toggleProgress(false);
                            errorMessageTextView.setText(errorMessage + "  Please try again!");
                            errorMessageTextView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onEmptyInput(boolean isEmailEmpty, boolean isPasswordEmpty) {
                            // Either email or password is empty
//                        Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                            isInProgress = false;
                            //hide progress
                            toggleProgress(false);
                            errorMessageTextView.setText("All fields are required, fill the form and try again!");
                            errorMessageTextView.setVisibility(View.VISIBLE);
                        }
                    });
                }else{
//                    sign in is in progress
                    toggleProgress(true);
                }
            }
        });

        //register link button click listener
        register_link_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //register|sign up activity starts from here .
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        forget_password_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //forget password activity intent starts from here .
                Intent i = new Intent(SignInActivity.this, ChangePasswordActivity.class);
                startActivity(i);
            }
        });
      

    }

    @Override
    public void onBackPressed() {

    if(!isInProgress){
        SignInActivity.super.onBackPressed();
    }else{
        //process running...


    }

    }

    /**
     * Initializes the views
     * Has to be called first before any other method
     * */
    private void initUI(){


        emailEditText = (EditText) findViewById(R.id.emailInput);
        passwordEditText = (EditText) findViewById(R.id.passwordInput);
        errorMessageTextView = (TextView) findViewById(R.id.errorMessage);

        signInActionButton = (Button) findViewById(R.id.loginButton);

        register_link_view = (TextView) findViewById(R.id.register_link);

        forget_password_link = (TextView) findViewById(R.id.forget_password_link);

        //init progress.
    alertDialog = new AlertDialog.Builder(SignInActivity.this)
            .setCancelable(false)
            .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
            .create();



    }


    private void toggleProgress(boolean show)
    {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
    }

}