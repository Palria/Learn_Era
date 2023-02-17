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
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class CreateNewLibraryActivity extends AppCompatActivity {
String libraryName;
String libraryId;
String libraryCategory;
ArrayList<String> libraryCategoryArrayList;
String libraryDescription;

EditText libraryNameEditText;
TextView libraryCategoryEditText;
EditText libraryDescriptionEditText;
ImageView coverPhotoImageView;
Button cancelButton;
TextView chooseCategoryTextView;
boolean[] selectedCategories;
ArrayList<Integer> categoriesList = new ArrayList<>();


    /**
 * <p>Indicates whether the user is editing his library or creating a new library</p>
 * The {@link boolean} value is initialized from {@link Intent} data
 * */
boolean isCreateNewLibrary = true;

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
    AlertDialog addTutorialDialog;

    ArrayList<String> categoryList =  new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_library);
        initUI();
        fetchIntentData();



        if(isCreateNewLibrary){
            //User is creating new library
            libraryId = GlobalConfig.getRandomString(100);

            //helps to load the categories from server/firebase
            getDynamicCategories();



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
//                        Picasso.get().load(galleryImageUri)
//                                .centerCrop()
//                                .into(pickImageActionButton);
                    Glide.with(CreateNewLibraryActivity.this)
                            .load(galleryImageUri)
                            .centerCrop()
                            .into(pickImageActionButton);
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
                        //coverPhotoImageView.setImageBitmap(cameraImageBitmap);
                        Glide.with(CreateNewLibraryActivity.this)
                                .load(cameraImageBitmap)
                                .centerCrop()
                                .into(pickImageActionButton);
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

                boolean hasErrors = validateForm();
                if(hasErrors)return;
                //show alert
                toggleProgress(true);
                libraryName = libraryNameEditText.getText().toString();
                libraryCategory = chooseCategoryTextView.getText().toString();
                libraryDescription = libraryDescriptionEditText.getText().toString();
if(chooseCategoryTextView.getText().toString().split(",").length !=0) {
    categoryList.addAll(Arrays.asList(chooseCategoryTextView.getText().toString().split(",")));
}else{
    categoryList.add(chooseCategoryTextView.getText().toString());
}

                if(isLibraryCoverPhotoIncluded){
                    if(isCreateNewLibrary){
                        uploadLibraryCoverPhoto(new CoverPhotoUploadListener() {
                            @Override
                            public void onSuccess(String coverPhotoDownloadUrl,String coverPhotoStorageReference) {
                                createNewLibrary(categoryList,coverPhotoDownloadUrl, coverPhotoStorageReference,new OnLibraryEditionListener() {
                                    @Override
                                    public void onSuccess() {
                                        GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_LIBRARY_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryId, null, null, null, null,null, new GlobalConfig.ActionCallback() {
                                            @Override
                                            public void onSuccess() {
                                                toggleProgress(false);
                                                //showAddTutorialDialog();
                                                gotoNewlyCreatedLibrary();
//
//                                                GlobalHelpers.showAlertMessage("success",
//                                                        CreateNewLibraryActivity.this,
//                                                        "Library Created Successfully.",
//                                                        "You have successfully created your library,thanks and go ahead and contribute to Learn Era ");

                                            }

                                            @Override
                                            public void onFailed(String errorMessage) {
                                                toggleProgress(false);
                                                //showAddTutorialDialog();
                                                gotoNewlyCreatedLibrary();

//
//                                                GlobalHelpers.showAlertMessage("success",
//                                                        CreateNewLibraryActivity.this,
//                                                        "Library Created Successfully.",
//                                                        "You have successfully created your library,thanks and go ahead and contribute to Learn Era ");

                                            }
                                        });

                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {
                                        toggleProgress(false);

                                        GlobalHelpers.showAlertMessage("error",
                                                CreateNewLibraryActivity.this,
                                                "Library Creation Failed.",
                                                errorMessage);
                                    }
                                });
                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                toggleProgress(false);

                                GlobalHelpers.showAlertMessage("error",
                                        CreateNewLibraryActivity.this,
                                        "Library Creation Failed.",
                                        errorMessage);

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
                                            GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_EDIT_LIBRARY_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryId, null,  null, null, null, null, new GlobalConfig.ActionCallback() {
                                                @Override
                                                public void onSuccess() {

                                                    toggleProgress(false);

                                                    GlobalHelpers.showAlertMessage("success",
                                                            CreateNewLibraryActivity.this,
                                                            "Library Edited Successfully.",
                                                            "You have successfully edited your library,thanks go ahead and contribute to Learn Era ");
                                                }

                                                @Override
                                                public void onFailed(String errorMessage) {

                                                    toggleProgress(false);

                                                    GlobalHelpers.showAlertMessage("success",
                                                            CreateNewLibraryActivity.this,
                                                            "Library Edited Successfully.",
                                                            "You have successfully edited your library,thanks and go ahead and contribute to Learn Era ");
                                                }
                                            });


                                        }

                                        @Override
                                        public void onFailed(String errorMessage) {
                                            toggleProgress(false);

                                            GlobalHelpers.showAlertMessage("error",
                                                    CreateNewLibraryActivity.this,
                                                    "Library Edition Failed.",
                                                    errorMessage);


                                        }
                                    });
                                }

                                @Override
                                public void onFailed(String errorMessage) {
                                    toggleProgress(false);

                                    GlobalHelpers.showAlertMessage("error",
                                            CreateNewLibraryActivity.this,
                                            "Library Edition Failed.",
                                            errorMessage);


                                }
                            });
                        }
                        else{
                            editLibrary(libraryCategoryArrayList,retrievedCoverPhotoDownloadUrl,retrievedCoverPhotoStorageReference, new OnLibraryEditionListener() {
                                @Override
                                public void onSuccess() {
                                    GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_EDIT_LIBRARY_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryId, null,  null, null, null, null,  new GlobalConfig.ActionCallback() {
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

                                    GlobalHelpers.showAlertMessage("error",
                                            CreateNewLibraryActivity.this,
                                            "Library Edition Failed.",
                                            " Failed to edit library ");

                                }
                            });
                        }
                    }
                }
                else{
                    if(isCreateNewLibrary){
                        createNewLibrary(categoryList,"","", new OnLibraryEditionListener() {
                            @Override
                            public void onSuccess() {
                                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_LIBRARY_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryId, null,  null, null, null, null, new GlobalConfig.ActionCallback() {
                                    @Override
                                    public void onSuccess() {

                                        toggleProgress(false);
                                        //showAddTutorialDialog();
                                        gotoNewlyCreatedLibrary();

//
//                                        GlobalHelpers.showAlertMessage("success",
//                                                CreateNewLibraryActivity.this,
//                                                "Library Created Successfully.",
//                                                "You have successfully created your library,thanks and go ahead and contribute to Learn Era ");

                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {

                                        toggleProgress(false);
                                        //showAddTutorialDialog();
                                        gotoNewlyCreatedLibrary();

//
//                                        GlobalHelpers.showAlertMessage("success",
//                                                CreateNewLibraryActivity.this,
//                                                "Library Created Successfully.",
//                                                "You have successfully created your library,thanks and go ahead and contribute to Learn Era ");

                                    }
                                });
                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                toggleProgress(false);

                                GlobalHelpers.showAlertMessage("error",
                                        CreateNewLibraryActivity.this,
                                        "Library Edition Failed.",
                                        errorMessage);
                            }
                        });
                    }
                    else{
                        editLibrary(libraryCategoryArrayList,"","", new OnLibraryEditionListener() {
                            @Override
                            public void onSuccess() {
                                GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_EDIT_LIBRARY_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryId, null, null, null, null, null, new GlobalConfig.ActionCallback() {
                                    @Override
                                    public void onSuccess() {
toggleProgress(false);
                                        GlobalHelpers.showAlertMessage("success",
                                                CreateNewLibraryActivity.this,
                                                "Library Edited Successfully.",
                                                "You have successfully edited your library,thanks and go ahead and contribute to Learn Era ");

                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {
toggleProgress(false);
                                        GlobalHelpers.showAlertMessage("success",
                                                CreateNewLibraryActivity.this,
                                                "Library Edited Successfully.",
                                                "You have successfully edited your library,thanks and go ahead and contribute to Learn Era ");

                                    }
                                });

                            }

                            @Override
                            public void onFailed(String errorMessage) {

                                GlobalHelpers.showAlertMessage("error",
                                        CreateNewLibraryActivity.this,
                                        "Library Edition Failed.",
                                        errorMessage);

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

        chooseCategoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Initialize alert dialog
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(CreateNewLibraryActivity.this);

                // set title
                builder.setTitle("Select Categories for Library.");

                // set dialog non cancelable
                builder.setCancelable(true);

                String[] arr = new String[libraryCategoryArrayList.size()];
                for(int i=0; i<arr.length;i++){
                    arr[i]=libraryCategoryArrayList.get(i);
                }

                builder.setMultiChoiceItems(arr, selectedCategories, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                        if (isChecked) {
                            categoriesList.add(i);
                            Collections.sort(categoriesList);
                        } else {
                            categoriesList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < categoriesList.size(); j++) {
                            // concat array value
                            stringBuilder.append(libraryCategoryArrayList.get(categoriesList.get(j)));
                            // check condition
                            if (j != categoriesList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        if(categoriesList.size()>0) chooseCategoryTextView.setError(null);
                        // set text on textView
                        chooseCategoryTextView.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                // show dialog
                builder.show();

            }
        });
    }

    private void getDynamicCategories() {

        libraryCategoryArrayList=new ArrayList<>();
        libraryCategoryArrayList.add("Software Development");
        libraryCategoryArrayList.add("Web Development");
        libraryCategoryArrayList.add("Graphic Design");
        libraryCategoryArrayList.add("Ui Design");
        libraryCategoryArrayList.add("Ethical Hacking");
        libraryCategoryArrayList.add("Game Development");
        libraryCategoryArrayList.add("Prototyping");
        libraryCategoryArrayList.add("SEO");

        selectedCategories=new boolean[libraryCategoryArrayList.size()];
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

        if(TextUtils.isEmpty(libraryNameEditText.getText().toString().trim())){

        libraryNameEditText.setError("Please Enter a valid library name.");
        libraryNameEditText.requestFocus();
        return true;

        }

        if(TextUtils.isEmpty(libraryDescriptionEditText.getText().toString().trim())){
            libraryDescriptionEditText.setError("This field is required.");
            libraryDescriptionEditText.requestFocus();
            return true;
        }

        if(TextUtils.isEmpty(chooseCategoryTextView.getText().toString().trim())){
            chooseCategoryTextView.setError("Please choose at least one category.");
            chooseCategoryTextView.requestFocus();
            return true;
        }

        if(isCreateNewLibrary && !isLibraryCoverPhotoChanged){

            GlobalHelpers.showAlertMessage("error",CreateNewLibraryActivity.this,
                    "Oops error!","Please choose a cover for your library.");

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

        libraryNameEditText=findViewById(R.id.nameInput);
        libraryDescriptionEditText=findViewById(R.id.descriptionInput);
        createLibraryActionButton=findViewById(R.id.createActionButton);
        pickImageActionButton=findViewById(R.id.pickImageActionButton);
        chooseCategoryTextView =findViewById(R.id.chooseCategory);

        cancelButton = findViewById(R.id.cancelButton);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateNewLibraryActivity.this.onBackPressed();

            }
        });

        //init progress.
        alertDialog = new AlertDialog.Builder(CreateNewLibraryActivity.this)
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();

        addTutorialDialog = new AlertDialog.Builder(CreateNewLibraryActivity.this)
                .setCancelable(false)
                .setTitle("Congrats,"+libraryNameEditText.getText()+" library created!")
                .setMessage("Create first Tutorial under this library")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(CreateNewLibraryActivity.this, CreateNewTutorialActivity.class);
                        //creating new

                        intent.putExtra(GlobalConfig.IS_CREATE_NEW_TUTORIAL_KEY,true);
                        intent.putExtra(GlobalConfig.LIBRARY_CATEGORY_ARRAY_KEY,categoryList);
                        intent.putExtra(GlobalConfig.LIBRARY_CONTAINER_ID_KEY,libraryId);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Back",null)
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
    private void showAddTutorialDialog(){
        addTutorialDialog.show();
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

    private void gotoNewlyCreatedLibrary(){

        Intent intent  = new Intent(CreateNewLibraryActivity.this,LibraryActivity.class);
        intent.putExtra(GlobalConfig.LIBRARY_ID_KEY,libraryId);
        intent.putExtra(GlobalConfig.LIBRARY_AUTHOR_ID_KEY,GlobalConfig.getCurrentUserId());
        intent.putExtra(GlobalConfig.IS_FIRST_VIEW_KEY,true);
        startActivity(intent);
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