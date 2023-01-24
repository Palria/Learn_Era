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

public class CreateNewLibraryActivity extends AppCompatActivity {
String libraryName;
String libraryId;
String libraryCategory;
String libraryDescription;

EditText libraryNameEditText;
EditText libraryCategoryEditText;
EditText libraryDescriptionEditText;
ImageView coverPhotoImageView;
boolean isCreateNewLibrary = false;
boolean isCameraImage = false;
boolean isLibraryCoverPhotoChanged;
boolean isLibraryCoverPhotoIncluded;
int CAMERA_PERMISSION_REQUEST_CODE = 2002;
int GALLERY_PERMISSION_REQUEST_CODE = 23;

    String retrievedCoverPhotoDownloadUrl;
    Bitmap cameraImageBitmap;
    Uri galleryImageUri;

Button createLibraryActionButton;
Button pickImageActionButton;
    ActivityResultLauncher<Intent> openGalleryLauncher;
    ActivityResultLauncher<Intent> openCameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_library);
        initUI();
        fetchIntentData();
        if(isCreateNewLibrary){
            libraryId = GlobalConfig.getRandomString(100);
        }else{
            initializeLibraryProfile();
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
                libraryCategory = libraryCategoryEditText.getText().toString();
                libraryDescription = libraryDescriptionEditText.getText().toString();

                if(isLibraryCoverPhotoIncluded){
                    if(isCreateNewLibrary){
                        uploadLibraryCoverPhoto(new CoverPhotoUploadListener() {
                            @Override
                            public void onSuccess(String coverPhotoDownloadUrl) {
                                createNewLibrary(coverPhotoDownloadUrl);
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
                                public void onSuccess(String coverPhotoDownloadUrl) {
                                    editLibrary(coverPhotoDownloadUrl);
                                }

                                @Override
                                public void onFailed(String errorMessage) {

                                }
                            });
                        }
                        else{
                            editLibrary(retrievedCoverPhotoDownloadUrl);
                        }
                    }
                }
                else{
                    if(isCreateNewLibrary){
                        createNewLibrary("");
                    }
                    else{
                        editLibrary("");
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
    private void initUI(){

    }
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
                        coverPhotoUploadListener.onSuccess(coverPhotoDownloadUrl);
                    }
                });
            }
        });

    }

    private void createNewLibrary(String libraryCoverPhotoDownloadUrl){

        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference userLibraryDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId);
        HashMap<String,Object>userLibraryDetails  = new HashMap<>();
        userLibraryDetails.put(GlobalConfig.LIBRARY_DATE_CREATED_KEY,GlobalConfig.getDate());
        userLibraryDetails.put(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        userLibraryDetails.put(GlobalConfig.LIBRARY_DATE_EDITED_KEY,GlobalConfig.getDate());
        userLibraryDetails.put(GlobalConfig.LIBRARY_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        writeBatch.set(userLibraryDocumentReference,userLibraryDetails, SetOptions.merge());

        DocumentReference libraryProfileDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId).collection(GlobalConfig.LIBRARY_PROFILE_KEY).document(libraryId);
        HashMap<String,Object>libraryProfileDetails  = new HashMap<>();
        if(isLibraryCoverPhotoIncluded) {
            libraryProfileDetails.put(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY, libraryCoverPhotoDownloadUrl);
            libraryProfileDetails.put(GlobalConfig.IS_LIBRARY_COVER_PHOTO_INCLUDED_KEY, true);

        }else{
            libraryProfileDetails.put(GlobalConfig.IS_LIBRARY_COVER_PHOTO_INCLUDED_KEY, false);

        }

        libraryProfileDetails.put(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY,libraryName);
        libraryProfileDetails.put(GlobalConfig.LIBRARY_DESCRIPTION_KEY,libraryDescription);
        libraryProfileDetails.put(GlobalConfig.LIBRARY_CATEGORY_KEY,libraryCategory);
        libraryProfileDetails.put(GlobalConfig.LIBRARY_ID_KEY,libraryId);
        libraryProfileDetails.put(GlobalConfig.LIBRARY_OWNER_ID_KEY,GlobalConfig.getCurrentUserTokenId());
        libraryProfileDetails.put(GlobalConfig.LIBRARY_DATE_CREATED_KEY,GlobalConfig.getDate());
        libraryProfileDetails.put(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        libraryProfileDetails.put(GlobalConfig.LIBRARY_DATE_EDITED_KEY,GlobalConfig.getDate());
        libraryProfileDetails.put(GlobalConfig.LIBRARY_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        libraryProfileDetails.put(GlobalConfig.LIBRARY_SEARCH_VERBATIM_KEYWORD_KEY,GlobalConfig.generateSearchVerbatimKeyWords(libraryName));
        libraryProfileDetails.put(GlobalConfig.LIBRARY_SEARCH_ANY_MATCH_KEYWORD_KEY,GlobalConfig.generateSearchAnyMatchKeyWords(libraryName));
        writeBatch.set(libraryProfileDocumentReference,libraryProfileDetails, SetOptions.merge());


        DocumentReference allLibraryProfileDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId);
        HashMap<String,Object>allLibraryDetails  = new HashMap<>();
        allLibraryDetails.put(GlobalConfig.LIBRARY_CATEGORY_KEY,libraryCategory);
        allLibraryDetails.put(GlobalConfig.LIBRARY_ID_KEY,libraryId);
        allLibraryDetails.put(GlobalConfig.LIBRARY_OWNER_ID_KEY,GlobalConfig.getCurrentUserTokenId());
        allLibraryDetails.put(GlobalConfig.LIBRARY_DATE_CREATED_KEY,GlobalConfig.getDate());
        allLibraryDetails.put(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        allLibraryDetails.put(GlobalConfig.LIBRARY_DATE_EDITED_KEY,GlobalConfig.getDate());
        allLibraryDetails.put(GlobalConfig.LIBRARY_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        writeBatch.set(allLibraryProfileDocumentReference,allLibraryDetails, SetOptions.merge());



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

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });



    }

    private void editLibrary(String libraryCoverPhotoDownloadUrl){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference libraryProfileDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId).collection(GlobalConfig.LIBRARY_PROFILE_KEY).document(libraryId);
        HashMap<String,Object>libraryProfileDetails  = new HashMap<>();
        if(isLibraryCoverPhotoIncluded) {
            libraryProfileDetails.put(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY, libraryCoverPhotoDownloadUrl);
            libraryProfileDetails.put(GlobalConfig.IS_LIBRARY_COVER_PHOTO_INCLUDED_KEY, true);

        }else{
            libraryProfileDetails.put(GlobalConfig.IS_LIBRARY_COVER_PHOTO_INCLUDED_KEY, false);

        }

        libraryProfileDetails.put(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY,libraryName);
        libraryProfileDetails.put(GlobalConfig.LIBRARY_DESCRIPTION_KEY,libraryDescription);
        libraryProfileDetails.put(GlobalConfig.LIBRARY_CATEGORY_KEY,libraryCategory);
        libraryProfileDetails.put(GlobalConfig.LIBRARY_DATE_EDITED_KEY,GlobalConfig.getDate());
        libraryProfileDetails.put(GlobalConfig.LIBRARY_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        libraryProfileDetails.put(GlobalConfig.LIBRARY_SEARCH_VERBATIM_KEYWORD_KEY,GlobalConfig.generateSearchVerbatimKeyWords(libraryName));
        libraryProfileDetails.put(GlobalConfig.LIBRARY_SEARCH_ANY_MATCH_KEYWORD_KEY,GlobalConfig.generateSearchAnyMatchKeyWords(libraryName));
        writeBatch.set(libraryProfileDocumentReference,libraryProfileDetails, SetOptions.merge());


        DocumentReference allLibraryProfileDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId);
        HashMap<String,Object>allLibraryDetails  = new HashMap<>();
        allLibraryDetails.put(GlobalConfig.LIBRARY_CATEGORY_KEY,libraryCategory);
        allLibraryDetails.put(GlobalConfig.LIBRARY_DATE_EDITED_KEY,GlobalConfig.getDate());
        allLibraryDetails.put(GlobalConfig.LIBRARY_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        writeBatch.set(allLibraryProfileDocumentReference,allLibraryDetails, SetOptions.merge());



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

    private void initializeLibraryProfile(){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(GlobalConfig.getCurrentUserId())
                .collection(GlobalConfig.ALL_LIBRARY_KEY)
                .document(libraryId)
                .collection(GlobalConfig.LIBRARY_PROFILE_KEY)
                .document(libraryId)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String libraryName = documentSnapshot.getString(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
                        String libraryDescription = documentSnapshot.getString(GlobalConfig.LIBRARY_DESCRIPTION_KEY);
                        String libraryCategory = documentSnapshot.getString(GlobalConfig.LIBRARY_CATEGORY_KEY);
                        retrievedCoverPhotoDownloadUrl = documentSnapshot.getString(GlobalConfig.LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY);

                        libraryNameEditText.setText(libraryName);
                        libraryDescriptionEditText.setText(libraryDescription);
                        libraryCategoryEditText.setText(libraryCategory);

                        if(documentSnapshot.getBoolean(GlobalConfig.IS_LIBRARY_COVER_PHOTO_INCLUDED_KEY)!= null){
                            isLibraryCoverPhotoIncluded = documentSnapshot.getBoolean(GlobalConfig.IS_LIBRARY_COVER_PHOTO_INCLUDED_KEY);
                        }
                    }
                });

    }

    interface CoverPhotoUploadListener{
     void onSuccess(String coverPhotoDownloadUrl);
     void onFailed(String errorMessage);

    }
}