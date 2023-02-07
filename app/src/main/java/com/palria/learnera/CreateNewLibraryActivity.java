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
import java.util.ArrayList;
import java.util.HashMap;

public class CreateNewLibraryActivity extends AppCompatActivity {
String libraryName;
String libraryId;
ArrayList<String> libraryCategoryArrayList;
String libraryDescription;

EditText libraryNameEditText;
EditText libraryCategoryEditText;
EditText libraryDescriptionEditText;
ImageView coverPhotoImageView;
/**
 * <p>Indicates whether the user is editing his library or creating a new library</p>
 * The {@link boolean} value is initialized from {@link Intent} data
 * */
boolean isCreateNewLibrary = false;

boolean isCameraImage = false;
/**
 * Indicates if the user makes change in his library's cover photo
 * */
boolean isLibraryCoverPhotoChanged;

    /**
     * Indicates if the library has a cover photo or the owner has selected from the library
     * */
boolean isLibraryCoverPhotoIncluded;

int CAMERA_PERMISSION_REQUEST_CODE = 2002;
int GALLERY_PERMISSION_REQUEST_CODE = 23;
    /**
     * This field will be initialized from the database if there has been a cover photo before trying to edit
     * It is the library's photo url which will be converted to a {@link Uri} when retrieved from the database
     * When the user edits the library but did not make change on the library's cover photo, this value is still retained for downloading his
     * profile photo else if the cover photo is changed then another value will fill it after changing the photo.
     * */
    String retrievedCoverPhotoDownloadUrl;
    String retrievedCoverPhotoStorageReference;
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
    Button createLibraryActionButton;

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
        setContentView(R.layout.activity_create_new_library);
        initUI();
        fetchIntentData();
        if(isCreateNewLibrary){
            //User is creating new library
            libraryId = GlobalConfig.getRandomString(100);
        }else{
            //User is editing his library
            initializeLibraryProfile(new ProfileInitializationListener() {
                @Override
                public void onSuccess(String tutorialName, String tutorialDescription, ArrayList<String> libraryCategoryArray, String retrievedCoverPhotoDownloadUrl, String retrievedCoverPhotoStorageReference, boolean isLibraryCoverPhotoIncluded) {

                    libraryNameEditText.setText(libraryName);
                    libraryDescriptionEditText.setText(libraryDescription);
//                    libraryCategoryEditText.setText(libraryCategory);
                    libraryCategoryArrayList = libraryCategoryArray;
                    CreateNewLibraryActivity.this.isLibraryCoverPhotoIncluded = isLibraryCoverPhotoIncluded;
                    //Initialization succeeds, then start implementation

                }

                @Override
                public void onFailed(String errorMessage) {

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
                        isLibraryCoverPhotoIncluded = true;
                        isLibraryCoverPhotoChanged = true;


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
                        isLibraryCoverPhotoIncluded = true;
                        isLibraryCoverPhotoChanged = true;
                        }

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No image captured!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        createLibraryActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                libraryName = libraryNameEditText.getText().toString();
//                libraryCategory = libraryCategoryEditText.getText().toString();
                libraryDescription = libraryDescriptionEditText.getText().toString();

                if(isLibraryCoverPhotoIncluded){
                    if(isCreateNewLibrary){
                        uploadLibraryCoverPhoto(new CoverPhotoUploadListener() {
                            @Override
                            public void onSuccess(String coverPhotoDownloadUrl,String coverPhotoStorageReference) {
                                createNewLibrary(libraryCategoryArrayList,coverPhotoDownloadUrl, coverPhotoStorageReference,new OnLibraryEditionListener() {
                                    @Override
                                    public void onSuccess() {
                                        GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_LIBRARY_TYPE_KEY,GlobalConfig.getCurrentUserId(),libraryId,null,false,true,false,null,null,null,false,false,false);

                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {

                                    }
                                });
                            }

                            @Override
                            public void onFailed(String errorMessage) {

                            }
                        });
                    }
                    else{
                        if(isLibraryCoverPhotoChanged){
                            uploadLibraryCoverPhoto(new CoverPhotoUploadListener() {
                                @Override
                                public void onSuccess(String coverPhotoDownloadUrl,String coverPhotoStorageReference) {
                                    editLibrary(libraryCategoryArrayList,coverPhotoDownloadUrl, coverPhotoStorageReference,new OnLibraryEditionListener() {
                                        @Override
                                        public void onSuccess() {
                                            GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_EDIT_LIBRARY_TYPE_KEY,GlobalConfig.getCurrentUserId(),libraryId,null,false,true,false,null,null,null,false,false,false);

                                        }

                                        @Override
                                        public void onFailed(String errorMessage) {

                                        }
                                    });
                                }

                                @Override
                                public void onFailed(String errorMessage) {

                                }
                            });
                        }
                        else{
                            editLibrary(libraryCategoryArrayList,retrievedCoverPhotoDownloadUrl,retrievedCoverPhotoStorageReference, new OnLibraryEditionListener() {
                                @Override
                                public void onSuccess() {
                                    GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_EDIT_LIBRARY_TYPE_KEY,GlobalConfig.getCurrentUserId(),libraryId,null,false,true,false,null,null,null,false,false,false);

                                }

                                @Override
                                public void onFailed(String errorMessage) {

                                }
                            });
                        }
                    }
                }
                else{
                    if(isCreateNewLibrary){
                        createNewLibrary(libraryCategoryArrayList,"","", new OnLibraryEditionListener() {
                            @Override
                            public void onSuccess() {
                                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_LIBRARY_TYPE_KEY,GlobalConfig.getCurrentUserId(),libraryId,null,false,true,false,null,null,null,false,false,false);

                            }

                            @Override
                            public void onFailed(String errorMessage) {

                            }
                        });
                    }
                    else{
                        editLibrary(libraryCategoryArrayList,"","", new OnLibraryEditionListener() {
                            @Override
                            public void onSuccess() {
                                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_EDIT_LIBRARY_TYPE_KEY,GlobalConfig.getCurrentUserId(),libraryId,null,false,true,false,null,null,null,false,false,false);

                            }

                            @Override
                            public void onFailed(String errorMessage) {

                            }
                        });
                    }
                }
            }
        });

        pickImageActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalConfig.createPopUpMenu(CreateNewLibraryActivity.this,R.menu.pick_image_menu , pickImageActionButton, new GlobalConfig.OnMenuItemClickListener() {
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
     * Fetches the library's profile data and initializes the global variables before edition
     * */
    private void fetchIntentData(){

        Intent intent = getIntent();
        isCreateNewLibrary = intent.getBooleanExtra(GlobalConfig.IS_CREATE_NEW_LIBRARY_KEY,false);
        if(!isCreateNewLibrary){
            libraryId = intent.getStringExtra(GlobalConfig.LIBRARY_ID_KEY);
        }


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

    private void uploadLibraryCoverPhoto(CoverPhotoUploadListener coverPhotoUploadListener){

        StorageReference coverPhotoStorageReference  = GlobalConfig.getFirebaseStorageInstance().getReference().child(GlobalConfig.ALL_USERS_KEY+"/"+GlobalConfig.getCurrentUserId()+"/"+GlobalConfig.ALL_LIBRARY_KEY+"/"+libraryId+"/"+GlobalConfig.LIBRARY_IMAGES_KEY+"/"+GlobalConfig.LIBRARY_COVER_PHOTO_KEY+".PNG");
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
                        String coverPhotoStorageReference_2 = coverPhotoStorageReference.getPath();
                        coverPhotoUploadListener.onSuccess(coverPhotoDownloadUrl,coverPhotoStorageReference_2);
                    }
                });
            }
        });

    }

    private void createNewLibrary(ArrayList<String>libraryCategoryArray, String libraryCoverPhotoDownloadUrl, String libraryCoverPhotoStorageReference, OnLibraryEditionListener onLibraryEditionListener){

        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference libraryProfileDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId);

        HashMap<String,Object>libraryProfileDetails  = new HashMap<>();
        if(isLibraryCoverPhotoIncluded) {
            libraryProfileDetails.put(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY, libraryCoverPhotoDownloadUrl);
            libraryProfileDetails.put(GlobalConfig.LIBRARY_COVER_PHOTO_STORAGE_REFERENCE_KEY, libraryCoverPhotoStorageReference);
            libraryProfileDetails.put(GlobalConfig.IS_LIBRARY_COVER_PHOTO_INCLUDED_KEY, true);

        }else{

            libraryProfileDetails.put(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY, "");
            libraryProfileDetails.put(GlobalConfig.LIBRARY_COVER_PHOTO_STORAGE_REFERENCE_KEY, "");
            libraryProfileDetails.put(GlobalConfig.IS_LIBRARY_COVER_PHOTO_INCLUDED_KEY, false);

        }

        libraryProfileDetails.put(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY,libraryName);
        libraryProfileDetails.put(GlobalConfig.LIBRARY_DESCRIPTION_KEY,libraryDescription);
        libraryProfileDetails.put(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY,libraryCategoryArray);
        libraryProfileDetails.put(GlobalConfig.LIBRARY_ID_KEY,libraryId);
        libraryProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY,0L);
        libraryProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VIEWS_KEY,0L);
        libraryProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_VISITOR_KEY,0L);
        libraryProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REVIEWS_KEY,0L);
        libraryProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY,0L);
        libraryProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY,0L);
        libraryProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY,0L);
        libraryProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY,0L);
        libraryProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY,0L);
        libraryProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY,0L);
        libraryProfileDetails.put(GlobalConfig.LIBRARY_AUTHOR_ID_KEY,GlobalConfig.getCurrentUserId());
//        libraryProfileDetails.put(GlobalConfig.LIBRARY_DATE_CREATED_KEY,GlobalConfig.getDate());
        libraryProfileDetails.put(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
//        libraryProfileDetails.put(GlobalConfig.LIBRARY_DATE_EDITED_KEY,GlobalConfig.getDate());
        libraryProfileDetails.put(GlobalConfig.LIBRARY_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        libraryProfileDetails.put(GlobalConfig.LIBRARY_SEARCH_VERBATIM_KEYWORD_KEY,GlobalConfig.generateSearchVerbatimKeyWords(libraryName));
        libraryProfileDetails.put(GlobalConfig.LIBRARY_SEARCH_ANY_MATCH_KEYWORD_KEY,GlobalConfig.generateSearchAnyMatchKeyWords(libraryName));

        writeBatch.set(libraryProfileDocumentReference,libraryProfileDetails, SetOptions.merge());
//



        DocumentReference userDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId());
        HashMap<String,Object>userDetails  = new HashMap<>();
        userDetails.put(GlobalConfig.LAST_LIBRARY_CREATED_ID_KEY,libraryId);
        userDetails.put(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY,FieldValue.increment(1L));
        userDetails.put(GlobalConfig.LAST_LIBRARY_DATE_CREATED_KEY,GlobalConfig.getDate());
        userDetails.put(GlobalConfig.LAST_LIBRARY_DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        writeBatch.set(userDocumentReference,userDetails, SetOptions.merge());


        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onLibraryEditionListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onLibraryEditionListener.onSuccess();
                    }
                });



    }

    private void editLibrary(ArrayList<String>libraryCategoryArray,String libraryCoverPhotoDownloadUrl,String libraryCoverPhotoStorageReference, OnLibraryEditionListener onLibraryEditionListener){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference libraryProfileDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId);

        HashMap<String,Object>libraryProfileDetails  = new HashMap<>();
        if(isLibraryCoverPhotoIncluded) {
            libraryProfileDetails.put(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY, libraryCoverPhotoDownloadUrl);
            libraryProfileDetails.put(GlobalConfig.LIBRARY_COVER_PHOTO_STORAGE_REFERENCE_KEY, libraryCoverPhotoStorageReference);
            libraryProfileDetails.put(GlobalConfig.IS_LIBRARY_COVER_PHOTO_INCLUDED_KEY, true);

        }else{

            libraryProfileDetails.put(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY, "");
            libraryProfileDetails.put(GlobalConfig.LIBRARY_COVER_PHOTO_STORAGE_REFERENCE_KEY, "");
            libraryProfileDetails.put(GlobalConfig.IS_LIBRARY_COVER_PHOTO_INCLUDED_KEY, false);

        }

        libraryProfileDetails.put(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY,libraryName);
        libraryProfileDetails.put(GlobalConfig.LIBRARY_DESCRIPTION_KEY,libraryDescription);
        libraryProfileDetails.put(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY,libraryCategoryArray);
//        libraryProfileDetails.put(GlobalConfig.LIBRARY_DATE_EDITED_KEY,GlobalConfig.getDate());
        libraryProfileDetails.put(GlobalConfig.LIBRARY_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        libraryProfileDetails.put(GlobalConfig.LIBRARY_SEARCH_VERBATIM_KEYWORD_KEY,GlobalConfig.generateSearchVerbatimKeyWords(libraryName));
        libraryProfileDetails.put(GlobalConfig.LIBRARY_SEARCH_ANY_MATCH_KEYWORD_KEY,GlobalConfig.generateSearchAnyMatchKeyWords(libraryName));

        writeBatch.set(libraryProfileDocumentReference,libraryProfileDetails, SetOptions.merge());



        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onLibraryEditionListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onLibraryEditionListener.onSuccess();
                    }
                });
    }

    private void initializeLibraryProfile(ProfileInitializationListener profileInitializationListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_LIBRARY_KEY)
                .document(libraryId)
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
                        String libraryName = documentSnapshot.getString(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
                        String libraryDescription = documentSnapshot.getString(GlobalConfig.LIBRARY_DESCRIPTION_KEY);
                        ArrayList<String> libraryCategoryArrayList = (ArrayList<String>) documentSnapshot.get(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY);
                        retrievedCoverPhotoDownloadUrl = documentSnapshot.getString(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY);
                        retrievedCoverPhotoStorageReference = documentSnapshot.getString(GlobalConfig.LIBRARY_COVER_PHOTO_STORAGE_REFERENCE_KEY);
                        boolean isLibraryCoverPhotoIncluded = false;

                        if(documentSnapshot.getBoolean(GlobalConfig.IS_LIBRARY_COVER_PHOTO_INCLUDED_KEY)!= null){
                            isLibraryCoverPhotoIncluded = documentSnapshot.getBoolean(GlobalConfig.IS_LIBRARY_COVER_PHOTO_INCLUDED_KEY);
                        }
                        profileInitializationListener.onSuccess(libraryName, libraryDescription,libraryCategoryArrayList  , retrievedCoverPhotoDownloadUrl,retrievedCoverPhotoStorageReference, isLibraryCoverPhotoIncluded);

                    }
                });

    }

    interface CoverPhotoUploadListener{
     void onSuccess(String coverPhotoDownloadUrl,String coverPhotoStorageReference);
     void onFailed(String errorMessage);

    }


    interface ProfileInitializationListener{
        void onSuccess(String tutorialName,String tutorialDescription,ArrayList<String> libraryCategoryArrayList  ,String retrievedCoverPhotoDownloadUrl,String retrievedCoverPhotoStorageReference, boolean isLibraryCoverPhotoIncluded);
        void onFailed(String errorMessage);

    }
    interface OnLibraryEditionListener{
        void onSuccess();
        void onFailed(String errorMessage);

    }
}