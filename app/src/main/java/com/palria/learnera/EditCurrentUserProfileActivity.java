package com.palria.learnera;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class EditCurrentUserProfileActivity extends AppCompatActivity {


    private String userDisplayName;
    private String description;
    private String userCountryOfResidence;
    private String contactEmail;
    private String contactPhoneNumber;
    private String webLink;
    private String genderType;
    private EditText userDisplayNameEditText;
    private EditText descriptionInput;
    private EditText contactEmailEditText;
    private EditText webLinkInput;
    private EditText genderTypeEditText;
    private EditText contactPhoneNumberEditText;
    private EditText userCountryOfResidenceEditText;

    private Spinner genderTypeSpinner;
    private Spinner countrySpinner;

    TextView display_name;
    TextView display_email;
    RoundedImageView profile_image_view;

    boolean isBirthDateSet = false;
    private String birthDate;
    private EditText birthDateInput;

    boolean isProfilePhotoChanged;
    boolean isProfilePhotoIncluded;
    boolean isUserBlocked = false;
    int CAMERA_PERMISSION_REQUEST_CODE = 2002;
    int GALLERY_PERMISSION_REQUEST_CODE = 23;

    ImageView profilePhotoImageView;
    /**
     * This field will be initialized from the database if there has been a profile photo before trying to edit
     * It is the user's profile photo url which will be converted to a {@link Uri} when retrieved from the database
     * When the user edits the profile but did not make change on his profile photo, this value is still retained for downloading his
     * profile photo else ifn he changes the photo then another value will fill it after changing his profile photo.
     * */
    String retrievedProfilePictureDownloadUrl;
    String retrievedProfilePhotoStorageReference;
    Bitmap cameraImageBitmap;
    Uri galleryImageUri;

    Button editProfileActionButton;
    Button cancelButton;
    ImageView pickImageActionButton;
    ActivityResultLauncher<Intent> openGalleryLauncher;
    ActivityResultLauncher<Intent> openCameraLauncher;
    AlertDialog alertDialog;
    DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_current_user_profile);
        initUI();
        toggleProgress(true);
   initUserProfileValuesBeforeEdition(new ProfileValueInitListener() {
       @Override
       public void onSuccess(String userDisplayName,String description,String birthDate, String userCountryOfResidence, String contactEmail,String webLink, String contactPhoneNumber, String genderType, String userProfilePhotoDownloadUrl, String profilePhotoStorageReference, boolean isUserBlocked, boolean isUserProfilePhotoIncluded) {

           userDisplayNameEditText.setText(userDisplayName);
           descriptionInput.setText(description);
           birthDateInput.setText(birthDate);
           contactEmailEditText.setText(contactEmail);
           webLinkInput.setText(webLink);
           contactPhoneNumberEditText.setText(contactPhoneNumber);
//           genderTypeEditText.setText(genderType);
           retrievedProfilePictureDownloadUrl = userProfilePhotoDownloadUrl;
           retrievedProfilePhotoStorageReference = profilePhotoStorageReference;
           EditCurrentUserProfileActivity.this.isUserBlocked = isUserBlocked;
           EditCurrentUserProfileActivity.this.isProfilePhotoIncluded = isUserProfilePhotoIncluded;

           display_name.setText(userDisplayName);
           display_email.setText(contactEmail);

           ///gender auto select after info loaded
           switch (genderType.toLowerCase()){
               case "male":
                   genderTypeSpinner.setSelection(0);
                   break;
               case "female":
                   genderTypeSpinner.setSelection(1);
                   break;
               case "other":
                   genderTypeSpinner.setSelection(2);
                   break;
               default:break;
           }


           //country auto select
            ArrayList<String> countries = GlobalConfig.getCountryArrayList(null);
           for(String country : countries){
               if(country.toLowerCase().equals(userCountryOfResidence.toLowerCase())){
                   countrySpinner.setSelection(countries.indexOf(country));
                   break;
               }
           }


           Log.w("success_tag",userCountryOfResidence);
           toggleProgress(false);


           Glide.with(EditCurrentUserProfileActivity.this)
                   .load(userProfilePhotoDownloadUrl)
                   .centerCrop()
                   .error(R.drawable.default_profile)
                   .into(profile_image_view);
       }

       @Override
       public void onFailed(String errorMessage) {
           toggleProgress(false);

       }
   });
        prepareDatePickerDialog();
        birthDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        openGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null) {
                    Intent data=result.getData();
                    galleryImageUri = data.getData();
                    Picasso.get().load(galleryImageUri).into(profile_image_view);
//                    Glide.with(EditCurrentUserProfileActivity.this)
//                            .load(data.toUri(Intent.URI_ALLOW_UNSAFE))
//                            .load(galleryImageUri)
//                            .centerCrop()
//                            .into(profile_image_view);
                    isProfilePhotoIncluded = true;
                    isProfilePhotoChanged = true;


                }
            }
        });
        openCameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK) {

                    if (result.getData() != null) {
                        Intent data = result.getData();
                        Bitmap  bitmapFromCamera =(Bitmap) data.getExtras().get("data");

                        if(bitmapFromCamera != null) {
                            cameraImageBitmap = bitmapFromCamera;
                            //profilePhotoImageView.setImageBitmap(cameraImageBitmap);
                            Glide.with(EditCurrentUserProfileActivity.this)
                                    .asBitmap()
                                    .load(cameraImageBitmap)
                                    .centerCrop()
                                    .into(profile_image_view);
                            isProfilePhotoIncluded = true;
                            isProfilePhotoChanged = true;
                        }

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No image captured!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditCurrentUserProfileActivity.this.onBackPressed();
            }
        });

        editProfileActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDisplayName = userDisplayNameEditText.getText()+"";
                description = descriptionInput.getText()+"";
                birthDate = birthDateInput.getText()+"";
                userCountryOfResidence = countrySpinner.getSelectedItem()+"";
                genderType = genderTypeSpinner.getSelectedItem()+"";
                contactEmail = contactEmailEditText.getText()+"";
                webLink = webLinkInput.getText()+"";
                contactPhoneNumber = contactPhoneNumberEditText.getText()+"";

                if(isBirthDateSet){
                if(!userDisplayName.isEmpty()){
                    toggleProgress(true);

                    if(isProfilePhotoIncluded){
                    if(isProfilePhotoChanged){
                       uploadUserProfilePhoto(new ProfilePhotoUploadListener() {
                           @Override
                           public void onSuccess(String profilePhotoDownloadUrl,String profilePhotoStorageReference) {
                               editUserProfile(profilePhotoDownloadUrl,profilePhotoStorageReference, new ProfileCreationListener() {
                                   @Override
                                   public void onSuccess(String userName) {
                                       //succeed in editing profile
                                       GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_EDIT_ACCOUNT_TYPE_KEY, GlobalConfig.getCurrentUserId(), null, null,  null, null, null,  new GlobalConfig.ActionCallback() {
                                           @Override
                                           public void onSuccess() {

                                               display_name.setText(userDisplayNameEditText.getText().toString());
                                               display_email.setText(contactEmailEditText.getText().toString());
                                               toggleProgress(false);

                                               GlobalHelpers.showAlertMessage("success",
                                                       EditCurrentUserProfileActivity.this,
                                                       "Profile Edited Successfully.",
                                                       "You have successfully edited your profile, Learn Era has more knowledge to offer you, go ahead and learn more.");
                                           }

                                           @Override
                                           public void onFailed(String errorMessage) {

                                               display_name.setText(userDisplayNameEditText.getText().toString());
                                               display_email.setText(contactEmailEditText.getText().toString());
                                               toggleProgress(false);

                                               GlobalHelpers.showAlertMessage("success",
                                                       EditCurrentUserProfileActivity.this,
                                                       "Profile Edited Successfully.",
                                                       "You have successfully edited your profile, Learn Era has more knowledge to offer you, go ahead and learn more.");
                                           }
                                       });



                                   }

                                   @Override
                                   public void onFailed(String errorMessage) {

                                       //failed to post new changes of user's profile
                                       toggleProgress(false);

                                   }
                               });
                           }

                           @Override
                           public void onFailed(String errorMessage) {
                               //failed to upload user's profile photo
                               toggleProgress(false);

                           }
                       });
                    }else{
                        editUserProfile(retrievedProfilePictureDownloadUrl,retrievedProfilePhotoStorageReference, new ProfileCreationListener() {
                            @Override
                            public void onSuccess(String userName) {
                                //succeed in editing profile
                                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_EDIT_ACCOUNT_TYPE_KEY, GlobalConfig.getCurrentUserId(), null, null, null, null, null, new GlobalConfig.ActionCallback() {
                                    @Override
                                    public void onSuccess() {
                                        toggleProgress(false);
                                        GlobalHelpers.showAlertMessage("success",
                                                EditCurrentUserProfileActivity.this,
                                                "Profile Edited Successfully.",
                                                "You have successfully edited your profile, Learn Era has more knowledge to offer you, go ahead and learn more.");
                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {
                                        toggleProgress(false);
                                        GlobalHelpers.showAlertMessage("success",
                                                EditCurrentUserProfileActivity.this,
                                                "Profile Edited Successfully.",
                                                "You have successfully edited your profile, Learn Era has more knowledge to offer you, go ahead and learn more.");
                                    }
                                });

                                display_name.setText(userDisplayNameEditText.getText().toString());
                                display_email.setText(contactEmailEditText.getText().toString());


                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                //failed to post new changes of user's profile
                                toggleProgress(false);

                            }
                        });
                    }
                }else {
                    editUserProfile("","", new ProfileCreationListener() {
                        @Override
                        public void onSuccess(String userName) {
                            //succeed in editing profile
                            GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_EDIT_ACCOUNT_TYPE_KEY, GlobalConfig.getCurrentUserId(), null, null,  null, null, null, new GlobalConfig.ActionCallback() {
                                @Override
                                public void onSuccess() {

                                    GlobalHelpers.showAlertMessage("success",
                                            EditCurrentUserProfileActivity.this,
                                            "Profile Edited Successfully.",
                                            "You have successfully edited your profile, Learn Era has more knowledge to offer you, go ahead and learn more.");

                                }

                                @Override
                                public void onFailed(String errorMessage) {

                                    GlobalHelpers.showAlertMessage("success",
                                            EditCurrentUserProfileActivity.this,
                                            "Profile Edited Successfully.",
                                            "You have successfully edited your profile, Learn Era has more knowledge to offer you, go ahead and learn more.");

                                }
                            });

                            toggleProgress(false);
                            display_name.setText(userDisplayNameEditText.getText().toString());
                            display_email.setText(contactEmailEditText.getText().toString());
                        }

                        @Override
                        public void onFailed(String errorMessage) {
                            //failed to post new changes of user's profile
                            toggleProgress(false);

                        }
                    });
                }
                }else{
                    Toast.makeText(getApplicationContext(), "Please enter your user name", Toast.LENGTH_SHORT).show();
                }
            }else{
                    Toast.makeText(getApplicationContext(), "Please set your date of birth", Toast.LENGTH_SHORT).show();

                    birthDateInput.performClick();
                }

            }
        });

        pickImageActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalConfig.createPopUpMenu(EditCurrentUserProfileActivity.this,R.menu.pick_image_menu , pickImageActionButton, new GlobalConfig.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClicked(MenuItem item) {

                        if(item.getItemId() == R.id.galleryId){
                            openGallery();
                        }
                        else if(item.getItemId() == R.id.cameraId){
                            openCamera();
                        }
                        return true;
                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed(){
        createConfirmExitDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }

private void initUI(){

    Toolbar actionBar = (Toolbar)  findViewById(R.id.topBar);
    setSupportActionBar(actionBar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    /**
    *  private EditText userDisplayNameEditText;
    private EditText contactEmailEditText;
    private EditText genderTypeEditText;
    private EditText contactPhoneNumberEditText;
    private EditText userCountryOfResidenceEditText;
    * */

    userDisplayNameEditText = findViewById(R.id.nameInput);
    descriptionInput = findViewById(R.id.descriptionInputId);
    birthDateInput = findViewById(R.id.birthDateInputId);
    contactEmailEditText = findViewById(R.id.emailInput);
    webLinkInput = findViewById(R.id.webLinkInputId);
    contactPhoneNumberEditText = findViewById(R.id.contactInput);

    //gender and country is spinner.
    genderTypeSpinner = findViewById(R.id.genderSpinner);
    countrySpinner = findViewById(R.id.countrySpinner);

    pickImageActionButton = findViewById(R.id.photoSelectorButton);
    editProfileActionButton = findViewById(R.id.update_button);
    display_name = findViewById(R.id.current_name);
    display_email = findViewById(R.id.current_email);
    profile_image_view = findViewById(R.id.imageView1);

    cancelButton=findViewById(R.id.cancelButton);


    initCountrySpinner();
    initGenderSpinner();


    //init progress.
    alertDialog = new AlertDialog.Builder(EditCurrentUserProfileActivity.this)
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



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(getApplicationContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

            if (requestCode == CAMERA_PERMISSION_REQUEST_CODE){
                fireCameraIntent();
            }

            if (requestCode == GALLERY_PERMISSION_REQUEST_CODE){
                fireGalleryIntent();
            }

        }
    }
    private void createConfirmExitDialog(){
        AlertDialog confirmExitDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Exit");
        builder.setMessage("Click exit button to exit the screen");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_baseline_error_outline_24);
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditCurrentUserProfileActivity.super.onBackPressed();
            }
        })
                .setNegativeButton("Stay back", null);
        confirmExitDialog = builder.create();
        confirmExitDialog.show();

    }

    public void openGallery(){
        requestForPermissionAndPickImage(GALLERY_PERMISSION_REQUEST_CODE);

    }
    public void openCamera(){
        requestForPermissionAndPickImage(CAMERA_PERMISSION_REQUEST_CODE);
    }

    public void fireGalleryIntent(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        openGalleryLauncher.launch(galleryIntent);
    }
    public void fireCameraIntent(){
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraLauncher.launch(cameraIntent);
    }
    public void requestForPermissionAndPickImage(int requestCode){
        if(getApplicationContext().checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED || getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},requestCode);
        }else{
            if(requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
                fireCameraIntent();
            }
            if(requestCode == GALLERY_PERMISSION_REQUEST_CODE) {
                fireGalleryIntent();
            }
        }



    }


    private void uploadUserProfilePhoto(ProfilePhotoUploadListener profilePhotoUploadListener){
        StorageReference profilePhotoStorageReference  = GlobalConfig.getFirebaseStorageInstance().getReference().child(GlobalConfig.ALL_USERS_KEY+"/"+GlobalConfig.getCurrentUserId()+"/"+GlobalConfig.USER_IMAGES_KEY+"/"+GlobalConfig.USER_PROFILE_PHOTO_KEY+".PNG");
        profile_image_view.setDrawingCacheEnabled(true);
        Bitmap profilePhotoBitmap = profile_image_view.getDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        profilePhotoBitmap.compress(Bitmap.CompressFormat.PNG,20,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        UploadTask profilePhotoUploadTask = profilePhotoStorageReference.putBytes(bytes);

        profilePhotoUploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                profilePhotoUploadListener.onFailed(e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profilePhotoUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        return profilePhotoStorageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String profilePhotoDownloadUrl = String.valueOf(task.getResult());
                        String profilePhotoStorageReference_2 = profilePhotoStorageReference.getPath();

                        profilePhotoUploadListener.onSuccess(profilePhotoDownloadUrl,profilePhotoStorageReference_2);
                    }
                });
            }
        });

    }

    private void editUserProfile(String userProfilePhotoDownloadUrl,String profilePhotoStorageReference, ProfileCreationListener profileCreationListener){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

//        DocumentReference userProfileDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.USER_PROFILE_KEY).document(GlobalConfig.getCurrentUserId());
        DocumentReference userProfileDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId());

        HashMap<String,Object>userProfileDetails = new HashMap<>();
        userProfileDetails.put(GlobalConfig.USER_DISPLAY_NAME_KEY,userDisplayName);
        userProfileDetails.put(GlobalConfig.USER_DESCRIPTION_KEY,description);
        userProfileDetails.put(GlobalConfig.USER_BIRTH_DATE_KEY,birthDate);
        userProfileDetails.put(GlobalConfig.USER_ID_KEY,GlobalConfig.getCurrentUserId());
        userProfileDetails.put(GlobalConfig.USER_COUNTRY_OF_RESIDENCE_KEY,userCountryOfResidence);
        userProfileDetails.put(GlobalConfig.USER_GENDER_TYPE_KEY,genderType);
        userProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_AUTHOR_REVIEWS_KEY,FieldValue.increment(0L));
        userProfileDetails.put(GlobalConfig.IS_USER_BLOCKED_KEY,isUserBlocked);
        userProfileDetails.put(GlobalConfig.USER_CONTACT_EMAIL_ADDRESS_KEY,contactEmail);
        userProfileDetails.put(GlobalConfig.USER_PERSONAL_WEBSITE_LINK_KEY,webLink);
        userProfileDetails.put(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY,userProfilePhotoDownloadUrl);
        userProfileDetails.put(GlobalConfig.USER_PROFILE_PHOTO_STORAGE_REFERENCE_KEY,profilePhotoStorageReference);
        userProfileDetails.put(GlobalConfig.IS_USER_PROFILE_PHOTO_INCLUDED_KEY,isProfilePhotoIncluded);
        userProfileDetails.put(GlobalConfig.USER_CONTACT_PHONE_NUMBER_KEY,contactPhoneNumber);
        userProfileDetails.put(GlobalConfig.USER_TOKEN_ID_KEY,GlobalConfig.getCurrentUserTokenId());
        userProfileDetails.put(GlobalConfig.USER_PROFILE_DATE_EDITED_KEY,GlobalConfig.getDate());
        userProfileDetails.put(GlobalConfig.USER_PROFILE_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
//        userProfileDetails.put(GlobalConfig.DOCUMENT_CREATED_KEY, "DOCUMENT_CREATED");


                    userProfileDetails.put(GlobalConfig.USER_SEARCH_VERBATIM_KEYWORD_KEY, GlobalConfig.generateSearchVerbatimKeyWords(userDisplayName));
                    userProfileDetails.put(GlobalConfig.USER_SEARCH_ANY_MATCH_KEYWORD_KEY,GlobalConfig.generateSearchAnyMatchKeyWords(userDisplayName));

        writeBatch.set(userProfileDocumentReference,userProfileDetails,SetOptions.merge());


//        DocumentReference userDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId());
//        HashMap<String,Object>userDetails = new HashMap<>();
//        userDetails.put(GlobalConfig.USER_PROFILE_DATE_EDITED_KEY,GlobalConfig.getDate());
//        userDetails.put(GlobalConfig.USER_PROFILE_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
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
    private void initUserProfileValuesBeforeEdition(ProfileValueInitListener profileValueInitListener){
        GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId())
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        profileValueInitListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String userDisplayName =""+ documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                        String description =""+ documentSnapshot.get(GlobalConfig.USER_DESCRIPTION_KEY);
                        String birthDate =""+ documentSnapshot.get(GlobalConfig.USER_BIRTH_DATE_KEY);
                        if(documentSnapshot.get(GlobalConfig.USER_BIRTH_DATE_KEY) != null){
                            isBirthDateSet = true;
                        }
                        String userCountryOfResidence =""+ documentSnapshot.get(GlobalConfig.USER_COUNTRY_OF_RESIDENCE_KEY);
                        String contactEmail =""+ documentSnapshot.get(GlobalConfig.USER_CONTACT_EMAIL_ADDRESS_KEY);
                        String contactPhoneNumber =""+ documentSnapshot.get(GlobalConfig.USER_CONTACT_PHONE_NUMBER_KEY);
                        String webLink =documentSnapshot.get(GlobalConfig.USER_PERSONAL_WEBSITE_LINK_KEY)!=null? ""+ documentSnapshot.get(GlobalConfig.USER_PERSONAL_WEBSITE_LINK_KEY):"";
                        String genderType =""+ documentSnapshot.get(GlobalConfig.USER_GENDER_TYPE_KEY);
                        String userProfilePhotoDownloadUrl =""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
                        String profilePhotoStorageReference =""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_STORAGE_REFERENCE_KEY);
                        boolean isUserBlocked = false;
                        boolean isUserProfilePhotoIncluded = false;
                        if(documentSnapshot.get(GlobalConfig.IS_USER_BLOCKED_KEY) != null){
                             isUserBlocked =documentSnapshot.getBoolean(GlobalConfig.IS_USER_BLOCKED_KEY);

                        }
                        if(documentSnapshot.get(GlobalConfig.IS_USER_PROFILE_PHOTO_INCLUDED_KEY) != null){
                             isUserProfilePhotoIncluded = documentSnapshot.getBoolean(GlobalConfig.IS_USER_PROFILE_PHOTO_INCLUDED_KEY);

                        }

                        profileValueInitListener.onSuccess( userDisplayName,description,birthDate, userCountryOfResidence, contactEmail,webLink, contactPhoneNumber, genderType, userProfilePhotoDownloadUrl,  profilePhotoStorageReference, isUserBlocked, isUserProfilePhotoIncluded);

                    }
                });
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

    interface ProfilePhotoUploadListener{
        void onSuccess(String profilePhotoDownloadUrl,String profilePhotoStorageReference);
        void onFailed(String errorMessage);

    }
    interface ProfileCreationListener{
        void onSuccess(String userName);
        void onFailed(String errorMessage);
    }
    interface ProfileValueInitListener{
        void onSuccess(String userDisplayName,String description,String birthDate,String userCountryOfResidence,String contactEmail,String webLink,String contactPhoneNumber,String genderType,String userProfilePhotoDownloadUrl,String profilePhotoStorageReference,boolean isUserBlocked,boolean isUserProfilePhotoIncluded);
        void onFailed(String errorMessage);
    }

}
