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
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
import java.util.Collections;
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
    String tutorialCategory;
    String tutorialDescription;

    EditText tutorialNameEditText;
    EditText tutorialDescriptionEditText;
    ImageView coverPhotoImageView;
    Button cancelButton;
    TextView chooseCategoryTextView;
    TextView libraryNameView;
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
    Button createTutorialActionButton;

    /**
     * A {@link View} for performing the edition action
     * */
    RoundedImageView pickImageActionButton;

    /**
     * A  variable for launching the gallery {@link Intent}
     * */
    ActivityResultLauncher<Intent> openGalleryLauncher;

    /**
     * A  variable for launching the camera {@link Intent}
     * */
    ActivityResultLauncher<Intent> openCameraLauncher;

    /**
     * loading alert dialog
     *
     */
    AlertDialog alertDialog;

    /**
     * categories list containing only string values.
     */
    ArrayList<String> categoryArrayList = new ArrayList<>();
    final int[] checkedCategory = {-1};
    private String selectedLibraryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_tutorial);
        initUI();
        fetchIntentData();
        if(isCreateNewTutorial){
            //User is creating new tutorial
            tutorialId = GlobalConfig.getRandomString(100);

            //load the dynamic categories from server/firebase
            getDynamicCategories();



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
                   // Picasso.get().load(galleryImageUri).into(pickImageActionButton);
                    Glide.with(CreateNewTutorialActivity.this)
                            .load(galleryImageUri)
                            .centerCrop()
                            .into(pickImageActionButton);
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
                            //coverPhotoImageView.setImageBitmap(cameraImageBitmap);
                            Glide.with(CreateNewTutorialActivity.this)
                                    .load(cameraImageBitmap)
                                    .centerCrop()
                                    .into(pickImageActionButton);
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

                boolean hasErrors = validateForm();
                if(hasErrors)return;
                //show alert
                toggleProgress(true);

                tutorialName = tutorialNameEditText.getText().toString();
                tutorialCategory = chooseCategoryTextView.getText().toString();
                tutorialDescription = tutorialDescriptionEditText.getText().toString();

                if(isTutorialCoverPhotoIncluded){
                    if(isCreateNewTutorial){
                        uploadTutorialCoverPhoto(new CoverPhotoUploadListener() {
                            @Override
                            public void onSuccess(String coverPhotoDownloadUrl,String coverPhotoStorageReference) {
                                createNewTutorial(coverPhotoDownloadUrl,coverPhotoStorageReference, new OnTutorialEditionListener() {
                                    @Override
                                    public void onSuccess() {
                                        GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryContainerId, tutorialId, null, null, null, null, new GlobalConfig.ActionCallback() {
                                            @Override
                                            public void onSuccess() {

                                                toggleProgress(false);
//                                                GlobalHelpers.showAlertMessage("success",
//                                                        CreateNewTutorialActivity.this,
//                                                        "Tutorial created successfully",
//                                                        "You have successfully created your tutorial,thanks go ahead and contribute to Learn Era ");

                                            }

                                            @Override
                                            public void onFailed(String errorMessage) {

                                                toggleProgress(false);
                                                GlobalHelpers.showAlertMessage("success",
                                                        CreateNewTutorialActivity.this,
                                                        "Tutorial created successfully",
                                                        "You have successfully created your tutorial,thanks go ahead and contribute to Learn Era ");

                                            }
                                        });


                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {
                                        toggleProgress(false);
                                        GlobalHelpers.showAlertMessage("error",
                                                CreateNewTutorialActivity.this,
                                                "Error!",
                                                errorMessage);
                                    }
                                });
                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                toggleProgress(false);
                                GlobalHelpers.showAlertMessage("error",
                                        CreateNewTutorialActivity.this,
                                        "Something went wrong. please try again.",
                                        errorMessage);
                            }
                        });
                    }
                    else{
                        if(isTutorialCoverPhotoChanged){
                            uploadTutorialCoverPhoto(new CoverPhotoUploadListener() {
                                @Override
                                public void onSuccess(String coverPhotoDownloadUrl,String coverPhotoStorageReference) {
                                    editTutorial(coverPhotoDownloadUrl,coverPhotoStorageReference, new OnTutorialEditionListener() {
                                        @Override
                                        public void onSuccess() {
                                            GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_EDIT_TUTORIAL_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryContainerId, tutorialId,  null, null, null, null, new GlobalConfig.ActionCallback() {
                                                @Override
                                                public void onSuccess() {
                                                    toggleProgress(false);
                                                    GlobalHelpers.showAlertMessage("success",
                                                            CreateNewTutorialActivity.this,
                                                            "Tutorial edited successfully",
                                                            "You have successfully edited your tutorial,thanks go ahead and contribute to Learn Era ");
                                                }

                                                @Override
                                                public void onFailed(String errorMessage) {
toggleProgress(false);
                                                    GlobalHelpers.showAlertMessage("success",
                                                            CreateNewTutorialActivity.this,
                                                            "Tutorial edited successfully",
                                                            "You have successfully edited your tutorial,thanks go ahead and contribute to Learn Era ");
                                                }
                                            });

                                        }

                                        @Override
                                        public void onFailed(String errorMessage) {
toggleProgress(false);
                                        }
                                    });
                                }

                                @Override
                                public void onFailed(String errorMessage) {
toggleProgress(false);
                                }
                            });
                        }
                        else{
                            editTutorial(retrievedCoverPhotoDownloadUrl,retrievedCoverPhotoStorageReference, new OnTutorialEditionListener() {
                                @Override
                                public void onSuccess() {
                                    GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_EDIT_TUTORIAL_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryContainerId, tutorialId,  null, null, null, null, new GlobalConfig.ActionCallback() {
                                        @Override
                                        public void onSuccess() {

                                            toggleProgress(false);
                                            GlobalHelpers.showAlertMessage("success",
                                                    CreateNewTutorialActivity.this,
                                                    "Tutorial edited successfully",
                                                    "You have successfully edited your tutorial,thanks go ahead and contribute to Learn Era ");
                                        }

                                        @Override
                                        public void onFailed(String errorMessage) {

                                            toggleProgress(false);
                                            GlobalHelpers.showAlertMessage("success",
                                                    CreateNewTutorialActivity.this,
                                                    "Tutorial edited successfully",
                                                    "You have successfully edited your tutorial,thanks go ahead and contribute to Learn Era ");
                                        }
                                    });

                                }

                                @Override
                                public void onFailed(String errorMessage) {
toggleProgress(false);
                                }
                            });
                        }
                    }
                }
                else{
                    if(isCreateNewTutorial){
                        createNewTutorial("", "",new OnTutorialEditionListener() {
                            @Override
                            public void onSuccess() {
                                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryContainerId, tutorialId,  null, null, null, null, new GlobalConfig.ActionCallback() {
                                    @Override
                                    public void onSuccess() {


                                        toggleProgress(false);
                                        GlobalHelpers.showAlertMessage("success",
                                                CreateNewTutorialActivity.this,
                                                "Tutorial created successfully",
                                                "You have successfully created your tutorial,thanks go ahead and contribute to Learn Era ");
                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {


                                        toggleProgress(false);
                                        GlobalHelpers.showAlertMessage("success",
                                                CreateNewTutorialActivity.this,
                                                "Tutorial created successfully",
                                                "You have successfully created your tutorial,thanks go ahead and contribute to Learn Era ");
                                    }
                                });

                            }

                            @Override
                            public void onFailed(String errorMessage) {

                            }
                        });
                    }
                    else{
                        editTutorial("","" ,new OnTutorialEditionListener() {
                            @Override
                            public void onSuccess() {
                                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_EDIT_TUTORIAL_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryContainerId, tutorialId, null, null, null, null, new GlobalConfig.ActionCallback() {
                                    @Override
                                    public void onSuccess() {


                                        toggleProgress(false);
                                        GlobalHelpers.showAlertMessage("success",
                                                CreateNewTutorialActivity.this,
                                                "Tutorial edited successfully",
                                                "You have successfully edited your tutorial,thanks go ahead and contribute to Learn Era ");
                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {


                                        toggleProgress(false);
                                        GlobalHelpers.showAlertMessage("success",
                                                CreateNewTutorialActivity.this,
                                                "Tutorial edited successfully",
                                                "You have successfully edited your tutorial,thanks go ahead and contribute to Learn Era ");
                                    }
                                });

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

        // choose new category listener.
        chooseCategoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Initialize alert dialog
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(CreateNewTutorialActivity.this);

                // set title
                builder.setTitle("Select categories for tutorial.");

                // set dialog non cancelable
                builder.setCancelable(true);

                String[] arr = new String[categoryArrayList.size()];
                for(int i=0; i<arr.length;i++){
                    arr[i]=categoryArrayList.get(i);
                }

                builder.setSingleChoiceItems(arr, checkedCategory[0], (dialog, which) -> {
                    // update the selected item which is selected by the user so that it should be selected
                    // when user opens the dialog next time and pass the instance to setSingleChoiceItems method
                    checkedCategory[0] = which;

                    // now also update the TextView which previews the selected item
                    chooseCategoryTextView.setText(categoryArrayList.get(which));
                    //remove errors if that exists already.

                    chooseCategoryTextView.setError(null);
                    // when selected an item the dialog should be closed with the dismiss method
                    dialog.dismiss();
                });

                // show dialog
                builder.show();

            }
        });
    }

    private void getDynamicCategories() {
//
//        categoryArrayList=new ArrayList<>();
//        categoryArrayList.add("Software Development");
//        categoryArrayList.add("Web Development");
//        categoryArrayList.add("Graphic Design");
//        categoryArrayList.add("Ui Design");
//        categoryArrayList.add("Ethical Hacking");
//        categoryArrayList.add("Game Development");
//        categoryArrayList.add("Prototyping");
//        categoryArrayList.add("SEO");

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean validateForm() {

        if(TextUtils.isEmpty(tutorialNameEditText.getText().toString().trim())){

            tutorialNameEditText.setError("Please Enter a title for your tutorial.");
            tutorialNameEditText.requestFocus();
            return true;

        }

        if(TextUtils.isEmpty(tutorialDescriptionEditText.getText().toString().trim())){
            tutorialDescriptionEditText.setError("This field is required.");
            tutorialDescriptionEditText.requestFocus();
            return true;
        }

        if(TextUtils.isEmpty(chooseCategoryTextView.getText().toString().trim())){
            chooseCategoryTextView.setError("Please choose at least one category.");
            chooseCategoryTextView.requestFocus();
            return true;
        }

        if(isCreateNewTutorial && !isTutorialCoverPhotoChanged){

            GlobalHelpers.showAlertMessage("error",CreateNewTutorialActivity.this,
                    "Oops rejected!","Please choose a cover for your tutorial.");

            return true;
        }

        return false;

    }


    /**
     * Initializes the views
     * */
    private void initUI(){

        Toolbar actionBar = (Toolbar)  findViewById(R.id.topBar);
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        tutorialNameEditText=findViewById(R.id.nameInput);
        tutorialDescriptionEditText=findViewById(R.id.descriptionInput);
        createTutorialActionButton=findViewById(R.id.createActionButton);
        pickImageActionButton=findViewById(R.id.pickImageActionButton);
        chooseCategoryTextView =findViewById(R.id.chooseCategory);

        cancelButton = findViewById(R.id.cancelButton);

        libraryNameView=findViewById(R.id.libraryNameView);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateNewTutorialActivity.this.onBackPressed();

            }
        });

        //init progress.
        alertDialog = new AlertDialog.Builder(CreateNewTutorialActivity.this)
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();




    }

    /**
     * Fetches the tutorial's profile data and initializes the global variables before edition
     * */
    private void fetchIntentData(){

        Intent intent = getIntent();
        isCreateNewTutorial = intent.getBooleanExtra(GlobalConfig.IS_CREATE_NEW_TUTORIAL_KEY,false);
        if(!isCreateNewTutorial){
            tutorialId = intent.getStringExtra(GlobalConfig.TUTORIAL_ID_KEY);

        }else{
            if(intent.getSerializableExtra(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY) != null) {
                categoryArrayList = (ArrayList<String>) intent.getSerializableExtra(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY);
            }
        }

        selectedLibraryName=intent.getStringExtra(GlobalConfig.LIBRARY_DISPLAY_NAME_KEY);
        libraryNameView.setText("Creating new tutorial in : "+ Html.fromHtml("<b>"+selectedLibraryName+" </b>"));
        libraryContainerId = intent.getStringExtra(GlobalConfig.LIBRARY_CONTAINER_ID_KEY);
//        tutorialCategory = intent.getStringExtra(GlobalConfig.TUTORIAL_CATEGORY_KEY);
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

    private void uploadTutorialCoverPhoto(CoverPhotoUploadListener coverPhotoUploadListener){

        StorageReference coverPhotoStorageReference  = GlobalConfig.getFirebaseStorageInstance().getReference().child(GlobalConfig.ALL_USERS_KEY+"/"+GlobalConfig.getCurrentUserId()+"/"+GlobalConfig.ALL_LIBRARY_KEY+"/"+libraryContainerId+"/"+GlobalConfig.ALL_TUTORIAL_KEY+"/"+tutorialId+"/"+GlobalConfig.TUTORIAL_IMAGES_KEY+"/"+GlobalConfig.TUTORIAL_COVER_PHOTO_KEY+".PNG");
        pickImageActionButton.setDrawingCacheEnabled(true);
        Bitmap coverPhotoBitmap = pickImageActionButton.getDrawingCache();
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
                        String tutorialCoverPhotoStorageReference = coverPhotoStorageReference.getPath();
                        coverPhotoUploadListener.onSuccess(coverPhotoDownloadUrl,tutorialCoverPhotoStorageReference);
                    }
                });
            }
        });

    }

    private void createNewTutorial(String tutorialCoverPhotoDownloadUrl,String tutorialCoverPhotoStorageReference,OnTutorialEditionListener onTutorialEditionListener){

        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference allTutorialProfileDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId);
        HashMap<String,Object>tutorialProfileDetails  = new HashMap<>();
        if(isTutorialCoverPhotoIncluded) {
            tutorialProfileDetails.put(GlobalConfig.TUTORIAL_COVER_PHOTO_DOWNLOAD_URL_KEY, tutorialCoverPhotoDownloadUrl);
            tutorialProfileDetails.put(GlobalConfig.TUTORIAL_COVER_PHOTO_STORAGE_REFERENCE_KEY, tutorialCoverPhotoStorageReference);
            tutorialProfileDetails.put(GlobalConfig.IS_TUTORIAL_COVER_PHOTO_INCLUDED_KEY, true);

        }else{
            tutorialProfileDetails.put(GlobalConfig.IS_TUTORIAL_COVER_PHOTO_INCLUDED_KEY, false);

        }

        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DISPLAY_NAME_KEY,tutorialName);
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DESCRIPTION_KEY,tutorialDescription);
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_CATEGORY_KEY,tutorialCategory);
        tutorialProfileDetails.put(GlobalConfig.LIBRARY_CONTAINER_ID_KEY,libraryContainerId);
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
        tutorialProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_VISITOR_KEY,0L);
        tutorialProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_REVIEWS_KEY,0L);
        tutorialProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_REACH_KEY,0L);
        tutorialProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY,0L);
        tutorialProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY,0L);
        tutorialProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY,0L);
        tutorialProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY,0L);
        tutorialProfileDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY,0L);
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY,GlobalConfig.getCurrentUserId());
//        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DATE_CREATED_KEY,GlobalConfig.getDate());
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
//        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DATE_EDITED_KEY,GlobalConfig.getDate());
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_SEARCH_VERBATIM_KEYWORD_KEY,GlobalConfig.generateSearchVerbatimKeyWords(tutorialName));
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_SEARCH_ANY_MATCH_KEYWORD_KEY,GlobalConfig.generateSearchAnyMatchKeyWords(tutorialName));

        writeBatch.set(allTutorialProfileDocumentReference,tutorialProfileDetails, SetOptions.merge());


        DocumentReference userDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId());
        HashMap<String,Object>userDetails  = new HashMap<>();
        userDetails.put(GlobalConfig.LAST_TUTORIAL_CREATED_ID_KEY,tutorialId);
        userDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY,FieldValue.increment(1L));
//        userDetails.put(GlobalConfig.LAST_TUTORIAL_DATE_CREATED_KEY,GlobalConfig.getDate());
        userDetails.put(GlobalConfig.LAST_TUTORIAL_DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        writeBatch.set(userDocumentReference,userDetails, SetOptions.merge());


        DocumentReference libraryDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryContainerId);
        HashMap<String,Object>libraryDetails  = new HashMap<>();
        libraryDetails.put(GlobalConfig.LAST_TUTORIAL_CREATED_ID_KEY,tutorialId);
        libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY,FieldValue.increment(1L));
//        userDetails.put(GlobalConfig.LAST_TUTORIAL_DATE_CREATED_KEY,GlobalConfig.getDate());
        libraryDetails.put(GlobalConfig.LAST_TUTORIAL_DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        writeBatch.set(libraryDocumentReference,libraryDetails, SetOptions.merge());


        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onTutorialEditionListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onTutorialEditionListener.onSuccess();
                    }
                });



    }

    private void editTutorial(String tutorialCoverPhotoDownloadUrl,String tutorialCoverPhotoStorageReference,OnTutorialEditionListener onTutorialEditionListener){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference allLibraryProfileDocumentReference =  GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId);

        HashMap<String,Object>tutorialProfileDetails  = new HashMap<>();
        if(isTutorialCoverPhotoIncluded) {
            tutorialProfileDetails.put(GlobalConfig.TUTORIAL_COVER_PHOTO_DOWNLOAD_URL_KEY, tutorialCoverPhotoDownloadUrl);
            tutorialProfileDetails.put(GlobalConfig.TUTORIAL_COVER_PHOTO_STORAGE_REFERENCE_KEY, tutorialCoverPhotoStorageReference);
            tutorialProfileDetails.put(GlobalConfig.IS_TUTORIAL_COVER_PHOTO_INCLUDED_KEY, true);

        }else{
            tutorialProfileDetails.put(GlobalConfig.TUTORIAL_COVER_PHOTO_DOWNLOAD_URL_KEY, "");
            tutorialProfileDetails.put(GlobalConfig.TUTORIAL_COVER_PHOTO_STORAGE_REFERENCE_KEY, "");
            tutorialProfileDetails.put(GlobalConfig.IS_LIBRARY_COVER_PHOTO_INCLUDED_KEY, false);

        }

        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DISPLAY_NAME_KEY,tutorialName);
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DESCRIPTION_KEY,tutorialDescription);
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_CATEGORY_KEY,tutorialCategory);
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DATE_EDITED_KEY,GlobalConfig.getDate());
        tutorialProfileDetails.put(GlobalConfig.TUTORIAL_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        for(String searchKeyword: GlobalConfig.generateSearchVerbatimKeyWords(tutorialName)) {
            tutorialProfileDetails.put(GlobalConfig.TUTORIAL_SEARCH_VERBATIM_KEYWORD_KEY,FieldValue.arrayUnion(searchKeyword));
        }
        for(String searchKeyword: GlobalConfig.generateSearchAnyMatchKeyWords(tutorialName)) {
            tutorialProfileDetails.put(GlobalConfig.TUTORIAL_SEARCH_ANY_MATCH_KEYWORD_KEY,FieldValue.arrayUnion(searchKeyword));
        }
        writeBatch.update(allLibraryProfileDocumentReference,tutorialProfileDetails);




        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onTutorialEditionListener.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onTutorialEditionListener.onSuccess();
                    }
                });
    }

    private void initializeTutorialProfile(ProfileInitializationListener profileInitializationListener){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_TUTORIAL_KEY)
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
                        retrievedCoverPhotoStorageReference = documentSnapshot.getString(GlobalConfig.TUTORIAL_COVER_PHOTO_STORAGE_REFERENCE_KEY);
                        boolean isTutorialCoverPhotoIncluded = false;
                        if(documentSnapshot.getBoolean(GlobalConfig.IS_TUTORIAL_COVER_PHOTO_INCLUDED_KEY)!= null){
                            isTutorialCoverPhotoIncluded = documentSnapshot.getBoolean(GlobalConfig.IS_TUTORIAL_COVER_PHOTO_INCLUDED_KEY);
                        }

                        profileInitializationListener.onSuccess( tutorialName, tutorialDescription  , retrievedCoverPhotoDownloadUrl,  isTutorialCoverPhotoIncluded);

                    }
                });

    }

    private void gotoNewlyCreatedTutorial(){

        Intent intent  = new Intent(CreateNewTutorialActivity.this,TutorialActivity.class);
        intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
        intent.putExtra(GlobalConfig.LIBRARY_CONTAINER_ID_KEY,libraryContainerId);
        intent.putExtra(GlobalConfig.IS_FIRST_VIEW_KEY,true);
        startActivity(intent);
    }

    interface CoverPhotoUploadListener{
        void onSuccess(String coverPhotoDownloadUrl,String coverPhotoStorageReference);
        void onFailed(String errorMessage);

    }
    interface ProfileInitializationListener{
        void onSuccess(String tutorialName,String tutorialDescription  ,String retrievedCoverPhotoDownloadUrl, boolean isTutorialCoverPhotoIncluded);
        void onFailed(String errorMessage);

    }

    interface OnTutorialEditionListener{
        void onSuccess();
        void onFailed(String errorMessage);

    }
}