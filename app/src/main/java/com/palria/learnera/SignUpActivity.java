package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private String userDisplayName;
    private String userCountryOfOrigin;
    private String email;
    private String password;
    private String genderType;
    private EditText userDisplayNameEditText;
    private EditText userCountryOfOriginEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signUpActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        signUpActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDisplayName = userDisplayNameEditText.getText().toString();
                userCountryOfOrigin = userCountryOfOriginEditText.getText().toString();
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                if (userDisplayName != null && !userDisplayName.isEmpty()) {
                    GlobalConfig.signUpUserWithEmailAndPassword(email, password, new GlobalConfig.SignUpListener() {
                        @Override
                        public void onSuccess(String email, String password) {
                            //user has successfully signed in
                            GlobalConfig.signInUserWithEmailAndPassword(email, password, new GlobalConfig.SignInListener() {
                                @Override
                                public void onSuccess(String email, String password) {
                                    //user has signed in so can now write to the database, now create his first profile
                                    createUserProfileInDatabase(new ProfileCreationListener() {
                                        @Override
                                        public void onSuccess(String userName) {
                                            FirebaseAuth.getInstance().signOut();
                                            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                            startActivity(intent);
                                            SignUpActivity.this.finish();
                                        }

                                        @Override
                                        public void onFailed(String errorMessage) {

                                        }
                                    });
                                }

                                @Override
                                public void onFailed(String errorMessage) {

                                }

                                @Override
                                public void onEmptyInput(boolean isEmailEmpty, boolean isPasswordEmpty) {

                                }
                            });

                        }

                        @Override
                        public void onFailed(String errorMessage) {
                            // account sign in failed
                        }

                        @Override
                        public void onEmptyInput(boolean isEmailEmpty, boolean isPasswordEmpty) {
                            // Either email or password is empty
                        }
                    });
                }
                else{
                    /*Take an action when a user has not filled in their user name
                    *It is mandatory to fill in their user name
                    */
                }
            }
        });
    }



    private void createUserProfileInDatabase(ProfileCreationListener profileCreationListener){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference userProfileDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.USER_PROFILE_KEY).document(GlobalConfig.getCurrentUserId());
        HashMap<String,Object>userProfileDetails = new HashMap<>();
        userProfileDetails.put(GlobalConfig.USER_DISPLAY_NAME_KEY,userDisplayName);
        userProfileDetails.put(GlobalConfig.USER_COUNTRY_OF_ORIGIN_KEY,userCountryOfOrigin);
        userProfileDetails.put(GlobalConfig.USER_GENDER_TYPE_KEY,genderType);
        userProfileDetails.put(GlobalConfig.USER_SEARCH_VERBATIM_KEYWORD_KEY,FieldValue.arrayUnion(GlobalConfig.generateSearchVerbatimKeyWords(userDisplayName)));
        userProfileDetails.put(GlobalConfig.USER_SEARCH_ANY_MATCH_KEYWORD_KEY,FieldValue.arrayUnion(GlobalConfig.generateSearchAnyMatchKeyWords(userDisplayName)));
        userProfileDetails.put(GlobalConfig.USER_PROFILE_DATE_CREATED_KEY,GlobalConfig.getDate());
        userProfileDetails.put(GlobalConfig.USER_PROFILE_DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        userProfileDetails.put(GlobalConfig.USER_TOKEN_ID_KEY,GlobalConfig.getCurrentUserTokenId());
        writeBatch.set(userProfileDocumentReference,userProfileDetails, SetOptions.merge());


        DocumentReference userDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId());
        HashMap<String,Object>userDetails = new HashMap<>();
        userDetails.put(GlobalConfig.USER_PROFILE_DATE_CREATED_KEY,GlobalConfig.getDate());
        userDetails.put(GlobalConfig.USER_PROFILE_DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        writeBatch.set(userDocumentReference,userDetails, SetOptions.merge());

        writeBatch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                     profileCreationListener.onSuccess(userDisplayName);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
           profileCreationListener.onFailed(e.getMessage());
            }
        });


    }


    private void initUI(){

    }

    interface ProfileCreationListener{
        void onSuccess(String userName);
        void onFailed(String errorMessage);
    }
}