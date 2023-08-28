package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.palria.learnera.network.CountryApiEndpoints;
import com.palria.learnera.network.CountryNetworkApi;
import com.palria.learnera.network.CountryResponseDataModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private String userDisplayName;
    private String birthDate;
    private String description;
    private String userCountryOfResidence;
    private String phoneNumber;
    private String email;
    private String webLink;
    private String password;
    private String confirmPassword;
    private String genderType;
    private EditText userDisplayNameEditText;
    private EditText birthDateInput;
    private EditText descriptionInput;
    private EditText userCountryOfResidenceEditText;
    private EditText phoneNumberEditText;
    private EditText emailEditText;
    private EditText webLinkEditText;
    private EditText passwordEditText;
    private EditText genderTypeEditText;
    private Button signUpActionButton;
    private Spinner genderTypeSpinner;
    private Spinner countrySpinner;
    private EditText passwordConfirmEditText;
    AlertDialog alertDialog;
    //register and forget password link
    private TextView login_link_view;
    private TextView forget_password_link;
    private TextView errorMessageTextView;
    boolean isBirthDateSet = false;
    TextInputLayout passwordInputLayout;
    /**
     * This is a flag indicating when the sign up process finishes
     * */
    boolean isInProgress = false;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);
        initUI();


        loadCurrentVisitorCountry();
        prepareDatePickerDialog();

        signUpActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDisplayName = userDisplayNameEditText.getText()+"";
                birthDate = birthDateInput.getText()+"";
                description = descriptionInput.getText()+"";
                //userCountryOfResidence = userCountryOfResidenceEditText.getText().toString();
                phoneNumber = phoneNumberEditText.getText().toString();
                email = emailEditText.getText().toString();
                webLink = webLinkEditText.getText().toString();
                password = passwordEditText.getText().toString();
                confirmPassword = passwordConfirmEditText.getText().toString();

                Log.w("message", userDisplayName + " : " + email + " : " + genderType + " : " + userCountryOfResidence);

                //validate confirm password
                if(!isBirthDateSet){
                    Toast.makeText(SignUpActivity.this, "Please set birth date", Toast.LENGTH_SHORT).show();
                    errorMessageTextView.setText("Please set your birth date!");
                    birthDateInput.setError("Please set your birth date!");
                    errorMessageTextView.setVisibility(View.VISIBLE);

                    birthDateInput.performClick();
                    return;
                }
                if(!password.equals(confirmPassword)){
//                    Toast.makeText(SignUpActivity.this, password+"="+confirmPassword, Toast.LENGTH_SHORT).show();
                    errorMessageTextView.setText("Confirm password does not match the password!");
                    errorMessageTextView.setVisibility(View.VISIBLE);
                    return;
                }
                if(!isInProgress) {
                    isInProgress = true;
                   toggleProgress(true);
                    errorMessageTextView.setText("Progress...");
                    errorMessageTextView.setVisibility(View.VISIBLE);
                    if (!userDisplayName.isEmpty()) {
                        GlobalConfig.signUpUserWithEmailAndPassword(SignUpActivity.this, email, password, new GlobalConfig.SignUpListener() {
                            @Override
                            public void onSuccess(String email, String password) {
                                //user has successfully signed up
//                                toggleProgress(false);
//
//                                GlobalHelpers.showAlertMessage("success",
//                                        SignUpActivity.this,
//                                        "Account Created Successfully.",
//                                        "we have successfully sent a confirmation email. please check your email now and verify before login.");
//
//                                emailEditText.setText("");
//                                passwordEditText.setText("");
//                                passwordConfirmEditText.setText("");
//                                userDisplayNameEditText.setText("");
//                                isInProgress=false;
//                                errorMessageTextView.setText("Success");

                                //--do not login user directly we need to confirm email before login--//.
//                                Toast.makeText(SignUpActivity.this, "up sign success", Toast.LENGTH_SHORT).show();

                                /**Sign in user to enable us create his database in the server. When we are done creating the database we log hi out instantly*/
                                GlobalConfig.signInUserWithEmailAndPassword(SignUpActivity.this, email, password, new GlobalConfig.SignInListener() {
                                    @Override
                                    public void onSuccess(String email, String password) {
                                        String userId = FirebaseAuth.getInstance().getCurrentUser()!=null?FirebaseAuth.getInstance().getCurrentUser().getUid():"0";
                                        //user has signed in so can now write to the database, now create his first profile
//                                        Toast.makeText(SignUpActivity.this, "sign in success", Toast.LENGTH_SHORT).show();
                                        createUserProfileInDatabase(userId,new ProfileCreationListener() {
                                            @Override
                                            public void onSuccess(String userName) {
//                                                Toast.makeText(SignUpActivity.this, "profile success", Toast.LENGTH_SHORT).show();

                                                isInProgress = false;
                                                toggleProgress(false);
                                                FirebaseAuth.getInstance().signOut();
                                                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                                startActivity(intent);
                                                SignUpActivity.super.onBackPressed();
                                            }

                                            @Override
                                            public void onFailed(String errorMessage) { isInProgress = false;
                                                toggleProgress(false);
                                                FirebaseAuth.getInstance().signOut();
                                                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                                startActivity(intent);
                                                SignUpActivity.super.onBackPressed();
                                            }
                                        });

                                        GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_SIGN_UP_TYPE_KEY, userId, null, null, null, null, null, new GlobalConfig.ActionCallback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onFailed(String errorMessage) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {
                                        isInProgress = false;
                                        toggleProgress(false);
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                        startActivity(intent);
                                        SignUpActivity.super.onBackPressed();
                                    }

                                    @Override
                                    public void onEmptyInput(boolean isEmailEmpty, boolean isPasswordEmpty) {
                                        isInProgress = false;
                                        toggleProgress(false);

                                    }
                                });



                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                // account sign in failed
                                isInProgress = false;
                                toggleProgress(false);
                                errorMessageTextView.setText(errorMessage + "  Please try again!");
                                errorMessageTextView.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onEmptyInput(boolean isEmailEmpty, boolean isPasswordEmpty) {
                                // Either email or password is empty
                                isInProgress = false;
                                toggleProgress(false);
                                errorMessageTextView.setText("Please  enter your user name");
                                errorMessageTextView.setVisibility(View.VISIBLE);

                            }
                        });
                    } else {
                        /*Take an action when a user has not filled in their user name and gender
                         *It is mandatory to fill in their user name and gender
                         */
                        isInProgress=false;
                        toggleProgress(false);
                        errorMessageTextView.setText("Please enter your user name");
                        errorMessageTextView.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    //Sign up in progress...
                }
            }
        });


        //register link button click listener
        login_link_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //register|sign up activity starts from here .
                Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(i);
                SignUpActivity.super.onBackPressed();

            }
        });
        birthDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        forget_password_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //forget password activity intent starts from here .
                Intent i = new Intent(SignUpActivity.this, ChangePasswordActivity.class);
                startActivity(i);
                SignUpActivity.super.onBackPressed();

            }
        });
    }

    private void loadCurrentVisitorCountry() {

        /*Create handle for the RetrofitInstance interface*/
        CountryApiEndpoints service = CountryNetworkApi.getRetrofitInstance().create(CountryApiEndpoints.class);
        Call<CountryResponseDataModel> call = service.getCurrentCountry();


        
        call.enqueue(new Callback<CountryResponseDataModel>() {
            @Override
            public void onResponse(@NonNull Call<CountryResponseDataModel> call, Response<CountryResponseDataModel> response) {
//                Toast.makeText(SignUpActivity.this, "hey there", Toast.LENGTH_SHORT).show();
                Log.e("countryResponse", response.toString());


                //choose current country from spinner.

                //country auto select
                ArrayList<String> countries = GlobalConfig.getCountryArrayList(null);
                for(String country : countries){

                    if(country.toLowerCase().equals(response.body().getCountry().toLowerCase())){
                        countrySpinner.setSelection(countries.indexOf(country));
                        break;
                    }

                }

            }

            @Override
            public void onFailure(Call<CountryResponseDataModel> call, Throwable t) {
                Log.e("err","err:"+t.getMessage(),t);
//                Toast.makeText(SignUpActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
              }
        });

    }


    /**
     * Creates the user's profile in the database
     * @param profileCreationListener the callback triggered when the profile is created in the database or if the creation fails
     * */
    private void createUserProfileInDatabase(String userId,ProfileCreationListener profileCreationListener){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference userProfileDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(userId);
        HashMap<String,Object>userProfileDetails = new HashMap<>();
        userProfileDetails.put(GlobalConfig.USER_DISPLAY_NAME_KEY,userDisplayName);
        userProfileDetails.put(GlobalConfig.USER_BIRTH_DATE_KEY,birthDate);
        userProfileDetails.put(GlobalConfig.USER_DESCRIPTION_KEY,description);
        userProfileDetails.put(GlobalConfig.USER_COUNTRY_OF_RESIDENCE_KEY,userCountryOfResidence);
        userProfileDetails.put(GlobalConfig.USER_GENDER_TYPE_KEY,genderType);
        userProfileDetails.put(GlobalConfig.USER_CONTACT_PHONE_NUMBER_KEY,phoneNumber);
        userProfileDetails.put(GlobalConfig.USER_CONTACT_EMAIL_ADDRESS_KEY,email);
        userProfileDetails.put(GlobalConfig.USER_PERSONAL_WEBSITE_LINK_KEY,webLink);
        userProfileDetails.put(GlobalConfig.IS_USER_BLOCKED_KEY,false);
        userProfileDetails.put(GlobalConfig.USER_PROFILE_DATE_CREATED_KEY,GlobalConfig.getDate());
        userProfileDetails.put(GlobalConfig.USER_PROFILE_DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        userProfileDetails.put(GlobalConfig.USER_PROFILE_DATE_EDITED_KEY,GlobalConfig.getDate());
        userProfileDetails.put(GlobalConfig.USER_PROFILE_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        userProfileDetails.put(GlobalConfig.USER_TOKEN_ID_KEY,GlobalConfig.getCurrentUserTokenId());

        for(String searchKeyword: GlobalConfig.generateSearchVerbatimKeyWords(userDisplayName)) {
            userProfileDetails.put(GlobalConfig.USER_SEARCH_VERBATIM_KEYWORD_KEY, FieldValue.arrayUnion(searchKeyword));
        }

        for(String searchKeyword: GlobalConfig.generateSearchAnyMatchKeyWords(userDisplayName)) {
            userProfileDetails.put(GlobalConfig.USER_SEARCH_ANY_MATCH_KEYWORD_KEY,FieldValue.arrayUnion(searchKeyword));
        }

        writeBatch.set(userProfileDocumentReference,userProfileDetails, SetOptions.merge());

//
//        DocumentReference userDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId());
//        HashMap<String,Object>userDetails = new HashMap<>();
//        userDetails.put(GlobalConfig.USER_PROFILE_DATE_CREATED_KEY,GlobalConfig.getDate());
//        userDetails.put(GlobalConfig.USER_PROFILE_DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
//        writeBatch.set(userDocumentReference,userDetails, SetOptions.merge());

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
        userDisplayNameEditText = findViewById(R.id.nameInput);
        descriptionInput = findViewById(R.id.descriptionInputId);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberViewId);
        emailEditText = (EditText) findViewById(R.id.emailInput);
        webLinkEditText = (EditText) findViewById(R.id.webLinkInputId);
        passwordEditText = (EditText) findViewById(R.id.passwordInput);
        passwordConfirmEditText = findViewById(R.id.passwordConfirmInput);
        errorMessageTextView = (TextView) findViewById(R.id.errorMessage);
        signUpActionButton = (Button) findViewById(R.id.startButton);
        login_link_view = (TextView) findViewById(R.id.login_link);
        forget_password_link = (TextView) findViewById(R.id.forget_password_link);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);

        genderTypeSpinner = findViewById(R.id.genderSpinner);
        birthDateInput = findViewById(R.id.birthDateInputId);
        countrySpinner = findViewById(R.id.countrySpinner);
        //init progress.
        alertDialog = new AlertDialog.Builder(SignUpActivity.this)
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();
    initGenderSpinner();
    initCountrySpinner();
    }


    private void toggleProgress(boolean show) {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
    }
    void prepareDatePickerDialog(){

        Calendar dateCalendar = Calendar.getInstance();
        final int YEAR = dateCalendar.get(Calendar.YEAR);
        final int MONTH = dateCalendar.get(Calendar.MONTH);
        final int DAY_OF_MONTH = dateCalendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int YEAR, int MONTH, int DAY_OF_MONTH) {
                MONTH += 1 ;
                birthDateInput.setText(DAY_OF_MONTH  + "/" + MONTH + "/" + YEAR);
                isBirthDateSet = true;
            }
        }, YEAR, MONTH, DAY_OF_MONTH);
        datePickerDialog.setCancelable(false);

    }

    /**
     * Initializes the gender spinner for selection
     * */
    private void initGenderSpinner(){
        String[] genderArray = {getResources().getString(R.string.male),getResources().getString(R.string.female),getResources().getString(R.string.other) };
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,genderArray);
        genderTypeSpinner.setAdapter(arrayAdapter);
        genderTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genderType = String.valueOf(genderTypeSpinner.getSelectedItem());
                //genderTypeEditText.setText(genderType);
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
                //userCountryOfResidenceEditText.setText(userCountryOfResidence);
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