package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private String userDisplayName;
    private String userCountryOfResidence;
    private String email;
    private String password;
    private String genderType;
    private EditText userDisplayNameEditText;
    private EditText userCountryOfResidenceEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText genderTypeEditText;
    private Button signUpActionButton;
    private Spinner genderTypeSpinner;
    private Spinner countrySpinner;

    /**
     * This is a flag indicating when the sign up process finishes
     * */
    boolean isInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initUI();
//

        signUpActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isInProgress) {
                    isInProgress = true;
                    userDisplayName = userDisplayNameEditText.getText().toString();
                    userCountryOfResidence = userCountryOfResidenceEditText.getText().toString();
                    email = emailEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    if (!userDisplayName.isEmpty() && !genderType.isEmpty()) {
                        GlobalConfig.signUpUserWithEmailAndPassword(SignUpActivity.this, email, password, new GlobalConfig.SignUpListener() {
                            @Override
                            public void onSuccess(String email, String password) {
                                //user has successfully signed up
                                GlobalConfig.signInUserWithEmailAndPassword(SignUpActivity.this, email, password, new GlobalConfig.SignInListener() {
                                    @Override
                                    public void onSuccess(String email, String password) {
                                        //user has signed in so can now write to the database, now create his first profile
                                        createUserProfileInDatabase(new ProfileCreationListener() {
                                            @Override
                                            public void onSuccess(String userName) {
                                                isInProgress = false;

                                                FirebaseAuth.getInstance().signOut();
                                                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                                startActivity(intent);
                                                SignUpActivity.this.finish();
                                            }

                                            @Override
                                            public void onFailed(String errorMessage) {
                                                isInProgress = false;

                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {
                                        isInProgress = false;

                                    }

                                    @Override
                                    public void onEmptyInput(boolean isEmailEmpty, boolean isPasswordEmpty) {
                                        isInProgress = false;

                                    }
                                });

                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                // account sign in failed
                                isInProgress = false;

                            }

                            @Override
                            public void onEmptyInput(boolean isEmailEmpty, boolean isPasswordEmpty) {
                                // Either email or password is empty
                                isInProgress = false;

                            }
                        });
                    } else {
                        /*Take an action when a user has not filled in their user name and gender
                         *It is mandatory to fill in their user name and gender
                         */
                    }
                }
                else{
                    //Sign up in progress...
                }
            }
        });
    }



    /**
     * Creates the user's profile in the database
     * @param profileCreationListener the callback triggered when the profile is created in the database or if the creation fails
     * */
    private void createUserProfileInDatabase(ProfileCreationListener profileCreationListener){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference userProfileDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.USER_PROFILE_KEY).document(GlobalConfig.getCurrentUserId());
        HashMap<String,Object>userProfileDetails = new HashMap<>();
        userProfileDetails.put(GlobalConfig.USER_DISPLAY_NAME_KEY,userDisplayName);
        userProfileDetails.put(GlobalConfig.USER_COUNTRY_OF_RESIDENCE_KEY,userCountryOfResidence);
        userProfileDetails.put(GlobalConfig.USER_GENDER_TYPE_KEY,genderType);
        userProfileDetails.put(GlobalConfig.USER_EMAIL_ADDRESS_KEY,email);
        userProfileDetails.put(GlobalConfig.IS_USER_BLOCKED_KEY,false);
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

    @Override
    public void onBackPressed() {

        if(!isInProgress){
            SignUpActivity.super.onBackPressed();
        }else{
            //process running...


        }

    }

    /**Initializes the activity's views*/
    private void initUI(){

    }

    /**
     * Initializes the gender spinner for selection
     * */
    private void initGenderSpinner(){
        String[] genderArray = {getResources().getString(R.string.male),getResources().getString(R.string.female)};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,genderArray);
        genderTypeSpinner.setAdapter(arrayAdapter);
        genderTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genderType = String.valueOf(genderTypeSpinner.getSelectedItem());
                genderTypeEditText.setText(genderType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Initializes the country spinner for selection
     * */
    private void initCountrySpinner(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,GlobalConfig.getCountryArrayList(new ArrayList<>()));
        countrySpinner.setAdapter(arrayAdapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userCountryOfResidence = String.valueOf(countrySpinner.getSelectedItem());
                userCountryOfResidenceEditText.setText(userCountryOfResidence);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * A callback of the user's profile creation
     * <p>{@link ProfileCreationListener#onSuccess(String)} triggered when the process finishes successfully</p>
     * <p>{@link ProfileCreationListener#onFailed(String)} triggered when the process fails </p>
     * */
    interface ProfileCreationListener{

        /**
         * triggered when the process finishes successfully
         * @param userName the user's name
         * */
        void onSuccess(String userName);

        /**
         * triggered when the process fails
         * @param errorMessage the error message stating the cause of the failure
         * */
        void onFailed(String errorMessage);
    }
}