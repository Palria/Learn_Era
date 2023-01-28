package com.palria.learnera;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class CreateNewTutorialActivity extends AppCompatActivity {
    String tutorialName;
    String tutorialId;
    /**
     * The library ID that will contain this tutorial
     * The value will be initialized from {@link Intent}
     * */
    String libraryContainerId;
    /**The library category that will contain this tutorial.
     * The value will be initialized from {@link Intent}
     * */
    String libraryContainerCategory;
    String tutorialDescription;

    EditText tutorialNameEditText;
    EditText tutorialDescriptionEditText;
    ImageView coverPhotoImageView;
    /**
     * <p>Indicates whether the user is editing his tutorial or creating a new tutorial</p>
     * The {@link boolean} value is initialized from {@link Intent} data
     * */
    boolean isCreateNewTutorial = false;

    boolean isCameraImage = false;
    /**
     * Indicates if the user makes change in his tutorial's cover photo
     * */
    boolean isTutorialCoverPhotoChanged;

    /**
     * Indicates if the tutorial has a cover photo or the owner has selected from the tutorial
     * */
    boolean isTutorialCoverPhotoIncluded;

    int CAMERA_PERMISSION_REQUEST_CODE = 2002;
    int GALLERY_PERMISSION_REQUEST_CODE = 23;
    /**
     * This field will be initialized from the database if there has been a cover photo before trying to edit
     * It is the tutorial's photo url which will be converted to a {@link Uri} when retrieved from the database
     * When the user edits the library but did not make change on the tutorial's cover photo, this value is still retained for downloading his
     * profile photo else if the cover photo is changed then another value will fill it after changing the photo.
     * */
    String retrievedCoverPhotoDownloadUrl;
    /**
     * Initialized from the device camera's captured photo
     * */
    Bitmap cameraImageBitmap;

    /**
     * Initialized from the device photo gallery
     * */
    Uri galleryImageUri;

    /**
     * A {@link View} for performing the edition action
     * */
    Button createTutorialActionButton;

    /**
     * A {@link View} for performing the edition action
     * */
    Button pickImageActionButton;

    /**
     * A  variable for launching the gallery {@link Intent}
     * */
    ActivityResultLauncher<Intent> openGalleryLauncher;

    /**
     * A  variable for launching the camera {@link Intent}
     * */
    ActivityResultLauncher<Intent> openCameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_tutorial);
        initUI();
        fetchIntentData();
        if(isCreateNewTutorial){
            //User is creating new tutorial
            tutorialId = GlobalConfig.getRandomString(100);
        }else{
            //User is editing his tutorial
            initializeTutorialProfile(new ProfileInitializationListener() {
                @Override
                public void onSuccess(String tutorialName, String tutorialDescription, String retrievedCoverPhotoDownloadUrl, boolean isTutorialCoverPhotoIncluded) {

                    tutorialNameEditText.setText(tutorialName);
                    tutorialDescriptionEditText.setText(tutorialDescription);
                    CreateNewTutorialActivity.this.isTutorialCoverPhotoIncluded = isTutorialCoverPhotoIncluded;

                    //start implementations from here


                }

                @Override
                public void onFailed(String errorMessage) {
                    //Inform the user of the failure and suggest possible solutions or try reload else exit

                }
            });
        }

        openGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null) {
                    Intent data=result.getData();
                    galleryImageUri = data.getData();
                    Picasso.get().load(galleryImageUri).into(coverPhotoImageView);
                    isTutorialCoverPhotoIncluded = true;
                    isTutorialCoverPhotoChanged = true;


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
                            coverPhotoImageView.setImageBitmap(cameraImageBitmap);
                            isTutorialCoverPhotoIncluded = true;
                            isTutorialCoverPhotoChanged = true;
                        }

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No image captured!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        createTutorialActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tutorialName = tutorialNameEditText.getText().toString();
                tutorialDescription = tutorialDescriptionEditText.getText().toString();

                if(isTutorialCoverPhotoIncluded){
                    if(isCreateNewTutorial){
                        uploadTutorialCoverPhoto(new CoverPhotoUploadListener() {
                            @Override
                            public void onSuccess(String coverPhotoDownloadUrl) {
                                createNewTutorial(coverPhotoDownloadUrl);
                            }

                            @Override
                            public void onFailed(String errorMessage) {

                            }
                        });
                    }
                    else{
                        if(isTutorialCoverPhotoChanged){
                            uploadTutorialCoverPhoto(new CoverPhotoUploadListener() {
                                @Override
                                public void onSuccess(String coverPhotoDownloadUrl) {
                                    editTutorial(coverPhotoDownloadUrl);
                                }

                                @Override
                                public void onFailed(String errorMessage) {

                                }
                            });
                        }
                        else{
                            editTutorial(retrievedCoverPhotoDownloadUrl);
                        }
                    }
                }
                else{
                    if(isCreateNewTutorial){
                        createNewTutorial("");
                    }
                    else{
                        editTutorial("");
                    }
                }
            }
        });

        pickImageActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalConfig.createPopUpMenu(CreateNewTutorialActivity.this,R.menu.pick_image_menu , pickImageActionButton, new GlobalConfig.OnMenuItemClickListener() {
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

    /**
     * Initializes the views
     * */
    private void initUI(){

    }

    /**
     * Fetches the tutorial's profile data and initializes the global variables before edition
     * */
    private void fetchIntentData(){

        Intent intent = getIntent();
        isCreateNewTutorial = intent.getBooleanExtra(GlobalConfig.IS_CREATE_NEW_TUTORIAL_KEY,false);
        if(!isCreateNewTutorial){
            tutorialId = intent.getStringExtra(GlobalConfig.TUTORIAL_ID_KEY);
        }
        libraryContainerId = intent.getStringExtra(GlobalConfig.LIBRARY_CONTAINER_ID_KEY);
        libraryContainerCategory = intent.getStringExtra(GlobalConfig.LIBRARY_CONTAINER_CATEGORY_KEY);

    }

    //
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

    public void openGallery(){
        requestForPermissionAndPickImage(GALLERY_PERMISSION_REQUEST_CODE);

    }
    public void openCamera(){
        requestForPermissionAndPickImage(CAMERA_PERMISSION_REQUEST_CODE);
    }

    public void fireGalleryIntent(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
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

    private void uploadTutorialCoverPhoto(CoverPhotoUploadListener coverPhotoUploadListener){

        StorageReference coverPhotoStorageReference  = GlobalConfig.getFirebaseStorageInstance().getReference().child(GlobalConfig.ALL_USERS_KEY+"/"+GlobalConfig.getCurrentUserId()+"/"+GlobalConfig.ALL_LIBRARY_KEY+"/"+libraryContainerId+"/"+GlobalConfig.ALL_TUTORIAL_KEY+"/"+tutorialId+"/"+GlobalConfig.TUTORIAL_IMAGES_KEY+"/"+GlobalConfig.TUTORIAL_COVER_PHOTO_KEY+".PNG");
        coverPhotoImageView.setDrawingCacheEnabled(true);
        Bitmap coverPhotoBitmap = coverPhotoImageView.getDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        coverPhotoBitmap.compress(Bitmap.CompressFormat.PNG,20,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        UploadTask coverPhotoUploadTask = coverPhotoStorageReference.putBytes(bytes);

        coverPhotoUploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                coverPhotoUploadListener.onFailed(e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                coverPhotoUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        return coverPhotoStorageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String coverPhotoDownloadUrl = String.valueOf(task.getResult());
                        coverPhotoUploadListener.onSuccess(coverPhotoDownloadUrl);
                    }
                });
            }
        });

    }

    private void createNewTutorial(String tutorialCoverPhotoDownloadUrl){

        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference userTutorialDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryContainerId).collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId);
        HashMap<String,Object>userTutorialDetails  = new HashMap<>();
        userTutorialDetails.put(GlobalConfig.TUTORIAL_DATE_CREATED_KEY,GlobalConfig.getDate());
        userTutorialDetails.put(GlobalConfig.TUTORIAL_DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        userTutorialDetails.put(GlobalConfig.TUTORIAL_DATE_EDITED_KEY,GlobalConfig.getDate());
        userTutorialDetails.put(GlobalConfig.TUTORIAL_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        writeBatch.set(userTutorialDocumentReference,userTutorialDetails, SetOptions.merge());

        DocumentReference tutorialProfileDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryContainerId).collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.TUTORIAL_PROFILE_KEY).document(tutorialId);
        HashMap<String,Object>tutorialProfileDetails  = new HashMap<>();
        if(isTutorialCoverPhotoIncluded) {
            tutorialProfileDetails.put(GlobalConfig.TUTORIAL_COVER_PHOTO_DOWNLOAD_URL_KEY, tutorialCoverPhotoDownloadUrl);
            tutorialProfileDetails.put(GlobalConfig.IS_TUTORIAL_COVER_PHOTO_INCLUDED_KEY, true);

        }else{
            tutorialProfileDetails.put(GlobalConfig.IS_TUTORIAL_COVER_PHOTO_INCLUDED_KEY, false);

        }

        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DISPLAY_NAME_KEY,tutorialName);
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DESCRIPTION_KEY,tutorialDescription);
        tutorialProfileDetails.put(GlobalConfig.LIBRARY_CONTAINER_CATEGORY_KEY,libraryContainerCategory);
        tutorialProfileDetails.put(GlobalConfig.LIBRARY_CONTAINER_ID_KEY,libraryContainerId);
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY,GlobalConfig.getCurrentUserTokenId());
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DATE_CREATED_KEY,GlobalConfig.getDate());
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DATE_EDITED_KEY,GlobalConfig.getDate());
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        for(String searchKeyword: GlobalConfig.generateSearchVerbatimKeyWords(tutorialName)) {
            tutorialProfileDetails.put(GlobalConfig.TUTORIAL_SEARCH_VERBATIM_KEYWORD_KEY,FieldValue.arrayUnion(searchKeyword));
        }
        for(String searchKeyword: GlobalConfig.generateSearchAnyMatchKeyWords(tutorialName)) {
            tutorialProfileDetails.put(GlobalConfig.TUTORIAL_SEARCH_ANY_MATCH_KEYWORD_KEY,FieldValue.arrayUnion(searchKeyword));
        }
        writeBatch.set(tutorialProfileDocumentReference,tutorialProfileDetails, SetOptions.merge());


        DocumentReference allTutorialProfileDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId);
        HashMap<String,Object>allTutorialDetails  = new HashMap<>();
        allTutorialDetails.put(GlobalConfig.LIBRARY_CONTAINER_CATEGORY_KEY,libraryContainerCategory);
        allTutorialDetails.put(GlobalConfig.LIBRARY_CONTAINER_ID_KEY,libraryContainerId);
        allTutorialDetails.put(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
        allTutorialDetails.put(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY,GlobalConfig.getCurrentUserTokenId());
        allTutorialDetails.put(GlobalConfig.TUTORIAL_DATE_CREATED_KEY,GlobalConfig.getDate());
        allTutorialDetails.put(GlobalConfig.TUTORIAL_DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        allTutorialDetails.put(GlobalConfig.TUTORIAL_DATE_EDITED_KEY,GlobalConfig.getDate());
        allTutorialDetails.put(GlobalConfig.TUTORIAL_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());

        for(String searchKeyword: GlobalConfig.generateSearchVerbatimKeyWords(tutorialName)) {
            allTutorialDetails.put(GlobalConfig.TUTORIAL_SEARCH_VERBATIM_KEYWORD_KEY,FieldValue.arrayUnion(searchKeyword));
        }
        for(String searchKeyword: GlobalConfig.generateSearchAnyMatchKeyWords(tutorialName)) {
            allTutorialDetails.put(GlobalConfig.TUTORIAL_SEARCH_ANY_MATCH_KEYWORD_KEY,FieldValue.arrayUnion(searchKeyword));
        }
        writeBatch.set(allTutorialProfileDocumentReference,allTutorialDetails, SetOptions.merge());



        DocumentReference userDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryContainerId);
        HashMap<String,Object>userDetails  = new HashMap<>();
        userDetails.put(GlobalConfig.LAST_TUTORIAL_CREATED_ID_KEY,tutorialId);
        userDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY,FieldValue.increment(1L));
        userDetails.put(GlobalConfig.LAST_TUTORIAL_DATE_CREATED_KEY,GlobalConfig.getDate());
        userDetails.put(GlobalConfig.LAST_TUTORIAL_DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        writeBatch.set(userDocumentReference,userDetails, SetOptions.merge());


        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });



    }

    private void editTutorial(String tutorialCoverPhotoDownloadUrl){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference tutorialProfileDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryContainerId).collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.TUTORIAL_PROFILE_KEY).document(tutorialId);
        HashMap<String,Object>tutorialProfileDetails  = new HashMap<>();
        if(isTutorialCoverPhotoIncluded) {
            tutorialProfileDetails.put(GlobalConfig.TUTORIAL_COVER_PHOTO_DOWNLOAD_URL_KEY, tutorialCoverPhotoDownloadUrl);
            tutorialProfileDetails.put(GlobalConfig.IS_TUTORIAL_COVER_PHOTO_INCLUDED_KEY, true);

        }else{
            tutorialProfileDetails.put(GlobalConfig.IS_LIBRARY_COVER_PHOTO_INCLUDED_KEY, false);

        }

        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DISPLAY_NAME_KEY,tutorialName);
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DESCRIPTION_KEY,tutorialDescription);
        tutorialProfileDetails.put(GlobalConfig.LIBRARY_CONTAINER_CATEGORY_KEY,libraryContainerCategory);
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DATE_EDITED_KEY,GlobalConfig.getDate());
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        for(String searchKeyword: GlobalConfig.generateSearchVerbatimKeyWords(tutorialName)) {
            tutorialProfileDetails.put(GlobalConfig.TUTORIAL_SEARCH_VERBATIM_KEYWORD_KEY,FieldValue.arrayUnion(searchKeyword));
        }
        for(String searchKeyword: GlobalConfig.generateSearchAnyMatchKeyWords(tutorialName)) {
            tutorialProfileDetails.put(GlobalConfig.TUTORIAL_SEARCH_ANY_MATCH_KEYWORD_KEY,FieldValue.arrayUnion(searchKeyword));
        }
        writeBatch.set(tutorialProfileDocumentReference,tutorialProfileDetails, SetOptions.merge());


        DocumentReference allLibraryProfileDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId);
        HashMap<String,Object>allTutorialDetails  = new HashMap<>();
        allTutorialDetails.put(GlobalConfig.LIBRARY_CONTAINER_CATEGORY_KEY,libraryContainerCategory);
        allTutorialDetails.put(GlobalConfig.TUTORIAL_DATE_EDITED_KEY,GlobalConfig.getDate());
        allTutorialDetails.put(GlobalConfig.TUTORIAL_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        for(String searchKeyword: GlobalConfig.generateSearchVerbatimKeyWords(tutorialName)) {
            allTutorialDetails.put(GlobalConfig.TUTORIAL_SEARCH_VERBATIM_KEYWORD_KEY,FieldValue.arrayUnion(searchKeyword));
        }
        for(String searchKeyword: GlobalConfig.generateSearchAnyMatchKeyWords(tutorialName)) {
            allTutorialDetails.put(GlobalConfig.TUTORIAL_SEARCH_ANY_MATCH_KEYWORD_KEY,FieldValue.arrayUnion(searchKeyword));
        }
        writeBatch.set(allLibraryProfileDocumentReference,allTutorialDetails, SetOptions.merge());



        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }

    private void initializeTutorialProfile(ProfileInitializationListener profileInitializationListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(GlobalConfig.getCurrentUserId())
                .collection(GlobalConfig.ALL_LIBRARY_KEY)
                .document(tutorialId)
                .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                .document(tutorialId)
                .collection(GlobalConfig.TUTORIAL_PROFILE_KEY)
                .document(tutorialId)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        profileInitializationListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String tutorialName = documentSnapshot.getString(GlobalConfig.TUTORIAL_DISPLAY_NAME_KEY);
                        String tutorialDescription = documentSnapshot.getString(GlobalConfig.TUTORIAL_DESCRIPTION_KEY);
                        retrievedCoverPhotoDownloadUrl = documentSnapshot.getString(GlobalConfig.TUTORIAL_COVER_PHOTO_DOWNLOAD_URL_KEY);
                        boolean isTutorialCoverPhotoIncluded = false;
                        if(documentSnapshot.getBoolean(GlobalConfig.IS_TUTORIAL_COVER_PHOTO_INCLUDED_KEY)!= null){
                            isTutorialCoverPhotoIncluded = documentSnapshot.getBoolean(GlobalConfig.IS_TUTORIAL_COVER_PHOTO_INCLUDED_KEY);
                        }

                        profileInitializationListener.onSuccess( tutorialName, tutorialDescription  , retrievedCoverPhotoDownloadUrl,  isTutorialCoverPhotoIncluded);

                    }
                });

    }

    interface CoverPhotoUploadListener{
        void onSuccess(String coverPhotoDownloadUrl);
        void onFailed(String errorMessage);

    }
    interface ProfileInitializationListener{
        void onSuccess(String tutorialName,String tutorialDescription  ,String retrievedCoverPhotoDownloadUrl, boolean isTutorialCoverPhotoIncluded);
        void onFailed(String errorMessage);

    }
}