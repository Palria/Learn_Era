package com.palria.learnera;


import static com.google.android.exoplayer2.ExoPlayerLibraryInfo.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.palria.learnera.lib.rcheditor.Utils;
import com.palria.learnera.lib.rcheditor.WYSIWYG;
import com.palria.learnera.widgets.BottomSheetFormBuilderWidget;
import com.palria.learnera.widgets.LEBottomSheetDialog;
import com.skydoves.colorpickerview.ActionMode;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.squareup.picasso.Picasso;

import org.apache.commons.text.StringEscapeUtils;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



public class UploadPageActivity extends AppCompatActivity {


    String pageId;
    String libraryId;
    String tutorialId;
    String folderId;
    Switch visibilitySwitch;
    LinearLayout containerLinearLayout;
    ImageButton addImageActionButton ;
    ImageButton addTodoListActionButton ;
    ImageButton addTableActionButton ;
    FloatingActionButton btn ;
    EditText pageTitleEditText;
    String pageTitle;
    String pageContent;
    AlertDialog confirmationDialog;
    AlertDialog initDialog;

    boolean isForcedExit = false;

    boolean isPublic = true;
    boolean isTutorialPage = true;
    boolean isCreateNewPage = true;
    boolean isCoverImageIncluded = false;
    boolean isCoverImageChanged = false;
    int pageNumber = 0;
    String coverImageUrl = "";
    String retrievedCoverPhotoDownloadUrl = "";
    ArrayList<String>retrievedActivePageMediaUrlArrayList = new ArrayList<>();


    Snackbar pageUploadSnackBar;
    OnPageUploadListener onPageUploadListener;
    RemoteViews notificationLayout;
    NotificationCompat.Builder builder;
    NotificationManagerCompat notificationManager;
    int notificationId;
    int numberOfProgressingMedia= 0;
    int numberOfMedia = 0;
    int progressCount = 0;
    long totalBytes = 0;
    long totalBytesTransferred = 0;

    /**
     * A  variable for launching the gallery {@link Intent}
     * */
    ActivityResultLauncher<Intent> openGalleryLauncher;

    /**
     * A  variable for launching the camera {@link Intent}
     * */
    ActivityResultLauncher<Intent> openCameraLauncher;
    ActivityResultLauncher<Intent> openVideoLauncher;
    ActivityResultLauncher<Intent> openCoverImageLauncher;
    int COVER_IMAGE_GALLERY_REQUEST_CODE=123012;
    int CAMERA_PERMISSION_REQUEST_CODE = 2002;
    int VIDEO_PERMISSION_REQUEST_CODE = 21;
    int GALLERY_PERMISSION_REQUEST_CODE = 23;

    int noOfImagesLoading = 0;
    int noOfImagesCompleted = 0;

    ImageProcessingListener imageProcessingListener;
    boolean isImageProcessingStarted = false;
    boolean isImageProcessingCompleted = false;
    TextView imageProcessingIndicatorView;

    int imageDisplayPosition = 0;
    int validPosition = 1;
    ImageView receiverImage;
    LinearLayout receiverLinearLayout;
    View currentFocusView;
    /**
     * loading alert dialog
     *
     */
    AlertDialog alertDialog;
//editor views

    WYSIWYG wysiwygEditor;
    ImageView action_undo;
    ImageView action_redo;
    ImageView action_bold;
    ImageView action_italic;
    ImageView action_subscript;
    ImageView action_superscript;
    ImageView action_strikethrough;
    ImageView action_underline;
    ImageView action_heading1;
      ImageView action_heading2;
        ImageView action_heading3;
        ImageView action_heading4;
        ImageView action_heading5;
        ImageView action_heading6;
         ImageView action_txt_color;
          ImageView  action_bg_color;
          ImageView  action_indent;
        ImageView action_align_left;
    ImageView action_align_center;
    ImageView action_align_right;
    ImageView action_align_justify;
    ImageView action_blockquote;
    ImageView  action_insert_bullets;
    ImageView action_insert_numbers;
     ImageView action_insert_image;
        ImageView action_insert_link;
       ImageView action_insert_checkbox;
        boolean visible = false;
        ImageView preview;
        ImageView insert_latex;
        ImageView insert_code;
       ImageView action_change_font_type;
       HorizontalScrollView latex_editor;
       Button submit_latex;
       EditText latex_equation;
       ImageView action_insert_video;
    RoundedImageView coverImageView;
    ImageView coverUploadButton;

    ImageView action_insert_table;

    LEBottomSheetDialog choosePhotoPickerModal;
    LEBottomSheetDialog chooseVideoPickerModal;

    ArrayList<String> localImagesUriList = new ArrayList<>();
    ArrayList<String> localVideosUriList = new ArrayList<>();

    AlertDialog confirmExitDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page);

        initUI();
        fetchIntentData();
        if(isCreateNewPage) {
            //user is creating new page, then generate new page id
            pageId = GlobalConfig.getRandomString(60);
        }else{
            //is editing page
            //main the old page id
            startInitializationForEdition();
        }
        openCoverImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null) {
//                    toggleProgress(true);
                    Intent data = result.getData();
                    Bitmap bitmap = null;
                    try {
                        noOfImagesLoading++;
                        imageProcessingListener.onStart();

                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                        final Bitmap bitmap1 = bitmap;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    bitmap1.compress(Bitmap.CompressFormat.PNG, 10, new ByteArrayOutputStream());
                                    coverImageUrl = GlobalHelpers.getImageUri(UploadPageActivity.this, bitmap1).toString();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Glide.with(UploadPageActivity.this)
                                                    .load(coverImageUrl)
                                                    .into(coverImageView);
                                        }
                                    });
                                    isCoverImageIncluded = true;
                                    isCoverImageChanged = true;
                                    toggleProgress(false);
                                    noOfImagesCompleted++;
                                    if (noOfImagesLoading == noOfImagesCompleted) {
                                        imageProcessingListener.onComplete();
                                    }

                                }catch(Exception e){
                                    coverImageUrl = ""+ data.getData();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Glide.with(UploadPageActivity.this)
                                                    .load(coverImageUrl)
                                                    .into(coverImageView);
                                        }
                                    });
                                    isCoverImageIncluded = true;
                                    isCoverImageChanged = true;
                                    toggleProgress(false);
                                    noOfImagesCompleted++;
                                    if (noOfImagesLoading == noOfImagesCompleted) {
                                        imageProcessingListener.onComplete();
                                    }

                                }
                            }
                        }).start();

                    } catch (IOException e) {
//                        throw new RuntimeException(e);
                        toggleProgress(false);

                    }


                }
            }
        });

        openGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null) {
//                    toggleProgress(true);

                    Intent data=result.getData();
                    noOfImagesLoading++;
                    imageProcessingListener.onStart();

                    //upload this image when uploading the page as index
                    //insert image to
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                                    String imageUriString = "";
                                try {
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 10, new ByteArrayOutputStream());
                                    imageUriString = GlobalHelpers.getImageUri(UploadPageActivity.this, bitmap).toString();
                                    final String finalImageUriString = imageUriString;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            localImagesUriList.add(finalImageUriString);
                                            wysiwygEditor.insertImage(finalImageUriString, "-");
                                            toggleProgress(false);
                                            noOfImagesCompleted++;
                                            if(noOfImagesLoading == noOfImagesCompleted) {
                                                imageProcessingListener.onComplete();
                                            }
                                        }
                                    });
                                }catch (Exception e){
                                    imageUriString = ""+ data.getData();
                                    final String finalImageUriString = imageUriString;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            localImagesUriList.add(finalImageUriString);
                                            wysiwygEditor.insertImage(finalImageUriString, "-");
                                            toggleProgress(false);
                                            noOfImagesCompleted++;
                                            if(noOfImagesLoading == noOfImagesCompleted) {
                                                imageProcessingListener.onComplete();
                                            }
                                        }
                                    });

//                                    GlobalConfig.createSnackBar(UploadPageActivity.this,imageProcessingIndicatorView,e.getMessage(),Snackbar.LENGTH_LONG);
                                }
                                //insert image also upload image in uploading page with indexing.

                            } catch (IOException ioException) {
                                    toggleProgress(false);
                                    noOfImagesCompleted++;
                                    if(noOfImagesLoading == noOfImagesCompleted) {
                                        imageProcessingListener.onComplete();
                                    };
                                    ioException.printStackTrace();

                            }
                            }
                        }).start();
//                        uploadedImagesList.add(data.getData().toString());
//                        wysiwygEditor.insertImage(data.getData().toString(),"-");



                    return;

////                    galleryImageUri = data.getData();
////                        Picasso.get().load(galleryImageUri)
////                                .centerCrop()
////                                .into(pickImageActionButton);
//                    ImageView partitionImage = new ImageView(getApplicationContext());
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(70,70);
//                    partitionImage.setLayoutParams(layoutParams);
//
//                   ImageView image = getImage();
//                    Glide.with(UploadPageActivity.this)
//                            .load(data.getData())
//                            .centerCrop()
//                            .into(image);
//                    if(containerLinearLayout.getChildCount() == (containerLinearLayout.indexOfChild(currentFocusView)+1)) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                             //   createPartition();
//
//                            }
//                        });
                    }

//                    isLibraryCoverPhotoIncluded = true;
//                    isLibraryCoverPhotoChanged = true;



            }
        });
        openCameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK) {

                    if (result.getData() != null) {
//                        toggleProgress(true);
                        noOfImagesLoading++;
                        imageProcessingListener.onStart();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Intent data = result.getData();
                                Bitmap bitmapFromCamera = (Bitmap) data.getExtras().get("data");
                                bitmapFromCamera.compress(Bitmap.CompressFormat.PNG, 10, new ByteArrayOutputStream());

                                if (bitmapFromCamera != null) {
                                    String imageUriString = GlobalHelpers.getImageUri(UploadPageActivity.this, bitmapFromCamera).toString();
                                    //insert image also upload image in uploading page with indexing.
                                    runOnUiThread(new Runnable() {
                                                      @Override
                                                      public void run() {

                                                          localImagesUriList.add(imageUriString);
                                                          wysiwygEditor.insertImage(imageUriString, "-");
                                                          toggleProgress(false);
                                                          noOfImagesCompleted++;
                                                          if(noOfImagesLoading == noOfImagesCompleted) {
                                                              imageProcessingListener.onComplete();
                                                          }
                                                      }
                                                  });


//                            cameraImageBitmap = bitmapFromCamera;
                                    //coverPhotoImageView.setImageBitmap(cameraImageBitmap);

//                            ImageView partitionImage = new ImageView(getApplicationContext());
//                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(70,70);
//                            partitionImage.setLayoutParams(layoutParams);
//                            receiverLinearLayout.addView(partitionImage);
//
//                            ImageView image = getImage();
//
//                            Glide.with(UploadPageActivity.this)
//                                    .load(bitmapFromCamera)
//                                    .centerCrop()
//                                    .into(image);
//                            if(containerLinearLayout.getChildCount() == (containerLinearLayout.indexOfChild(receiverLinearLayout)+1)) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                      //  createPartition();
//
//                                    }
//                                });
//                            }
//                            isLibraryCoverPhotoIncluded = true;
//                            isLibraryCoverPhotoChanged = true;
                                }
                            }
                        }).start();

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No image captured!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        openVideoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK) {

                    if (result.getData() != null) {
                        toggleProgress(true);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Intent data = result.getData();
                                String videoUri =""+ data.getData();
                                  runOnUiThread(new Runnable() {
                                                      @Override
                                                      public void run() {

                                                          localVideosUriList.add(videoUri);
                                                          wysiwygEditor.insertVideo(videoUri);
                                                          toggleProgress(false);

                                                      }
                                                  });


//                            cameraImageBitmap = bitmapFromCamera;
                                    //coverPhotoImageView.setImageBitmap(cameraImageBitmap);

//                            ImageView partitionImage = new ImageView(getApplicationContext());
//                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(70,70);
//                            partitionImage.setLayoutParams(layoutParams);
//                            receiverLinearLayout.addView(partitionImage);
//
//                            ImageView image = getImage();
//
//                            Glide.with(UploadPageActivity.this)
//                                    .load(bitmapFromCamera)
//                                    .centerCrop()
//                                    .into(image);
//                            if(containerLinearLayout.getChildCount() == (containerLinearLayout.indexOfChild(receiverLinearLayout)+1)) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                      //  createPartition();
//
//                                    }
//                                });
//                            }
//                            isLibraryCoverPhotoIncluded = true;
//                            isLibraryCoverPhotoChanged = true;
                            }
                        }).start();

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No video selected!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageProcessingListener = new ImageProcessingListener() {
            @Override
            public void onComplete() {
                isImageProcessingCompleted = true;
                imageProcessingIndicatorView.setVisibility(View.GONE);
            }

            @Override
            public void onStart() {
                imageProcessingIndicatorView.setVisibility(View.VISIBLE);
                isImageProcessingStarted =true;
                isImageProcessingCompleted = false;

            }
        };

        onPageUploadListener = new OnPageUploadListener() {
            @Override
            public void onNewPage(String pageId) {
            pageUploadSnackBar =  GlobalConfig.createSnackBar(UploadPageActivity.this,wysiwygEditor, pageTitle + " is uploading...", Snackbar.LENGTH_INDEFINITE);
//            showPageProgressNotification();
            }

            @Override
            public void onFailed(String pageId, String errorMessage) {
                pageUploadSnackBar.dismiss();
                GlobalConfig.createSnackBar(UploadPageActivity.this,wysiwygEditor, "Page failed "+pageTitle + " please try again", Snackbar.LENGTH_SHORT);

            }

            @Override
            public void onProgress(String pageId, int progressCount) {
                pageUploadSnackBar.dismiss();
                pageUploadSnackBar =  GlobalConfig.createSnackBar(UploadPageActivity.this,wysiwygEditor, "Progress: "+ progressCount, Snackbar.LENGTH_INDEFINITE);

                if(progressCount==100){
                    notificationManager.cancelAll();
                    showCompletedNotification( pageId, folderId, tutorialId, isTutorialPage, pageTitle);
                }else{
                    updatePageProgressNotification(notificationLayout,progressCount,builder);
                    // Update the  with the current time
//                    percentageCompleted++;
//
//                    notificationLayout.setProgressBar(R.id.progress_bar,100,0,false);
//                    notificationLayout.setTextViewText(R.id.percent,percentageCompleted+"% completed");

                    // Update the notification
//                    NotificationManagerCompat.from(getApplicationContext()).notify(notificationId, builder.build());
                    // Schedule the next update in 1 second
                    //handler.postDelayed(this, 100);//make it 1000
                }
            }

            @Override
            public void onSuccess(String pageId) {
                pageUploadSnackBar.dismiss();
                GlobalHelpers.showAlertMessage("success",
                        UploadPageActivity.this,
                        "Page created successfully",
                        "You have successfully created your page, go ahead and contribute to Learn Era ");
                notificationManager.cancelAll();
                showCompletedNotification( pageId, folderId, tutorialId, isTutorialPage, pageTitle);


            }
        };

//
//        tutorialId  = "TEST_ID-3";
//        folderId  = "TEST_ID-3";
//        pageId  = "TEST_ID-3";


        addImageActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalConfig.createPopUpMenu(getApplicationContext(), R.menu.pick_image_menu, addImageActionButton, new GlobalConfig.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClicked(MenuItem item) {
                        if(item.getItemId() == R.id.galleryId)openGallery();
                        else if(item.getItemId() == R.id.cameraId)openCamera();

                        return true;
                    }
                });

            }
        });
        addTodoListActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTodoGroup();
            }
        });

        addTableActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            createTable();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the datas first to valid them
                wysiwygEditor.evaluateJavascript(
                        "(function() { return document.getElementById('editor').innerHTML; })();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String html) {
//                                pageContent = html.substring(1,html.length()-1);//substring use for remove double quotes.
                                pageContent = wysiwygEditor.getHtml();
                                pageTitle = pageTitleEditText.getText().toString();

//                                pageTitleEditText.setText(wysiwygEditor.getHtml());
//                                wysiwygEditor.setHtml(wysiwygEditor.getHtml());

                                if(validateForm()){
                                    //if validate form reeturns error/false
                                    return;
                                }



//                                showPageProgressNotification();
//
//                                //if form is valid now get the image to upload to store
                                //Configure images to be added to the database
                                ArrayList<String> mediaFinalListToUpload = new ArrayList<>();
                                if(isCoverImageIncluded){
                                    mediaFinalListToUpload.add(0,coverImageUrl);
                                }
                                for(String localUrl : localImagesUriList){
                                    if(pageContent.contains(localUrl)){
                                        //remove duplicate upload multiple times
                                       if(!mediaFinalListToUpload.contains(localUrl)){
                                           mediaFinalListToUpload.add(localUrl);
                                       }
                                    }
                                }

                                //configure videos to be added to the database
                                for(String videoUri: localVideosUriList){
                                    if(pageContent.contains(videoUri)){
                                        if(!mediaFinalListToUpload.contains(videoUri)){
                                            mediaFinalListToUpload.add(videoUri);
                                        }
                                    }
                                }

//                               startPageUploadService(imagesFinalListToUpload);
                                //Let the author confirm his action before uploading the page
                            showPageUploadConfirmationDialog(mediaFinalListToUpload);
                            }
                        });



            }
        });

        coverUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestForPermissionAndPickImage(COVER_IMAGE_GALLERY_REQUEST_CODE);
            }
        });
        coverImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestForPermissionAndPickImage(COVER_IMAGE_GALLERY_REQUEST_CODE);
            }
        });
        visibilitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isPublic = b;
            }
        });
    }



    @Override
     public void onBackPressed(){
        if(isForcedExit) {
            super.onBackPressed();
        }else{
            createConfirmExitDialog();
        }
    }

    //uploaded images count tracker all complete or not
    int  uploaded = 0;
    private void startUploadService(String postTitle, String postContent, ArrayList<String> imagesListToUpload) {
        pageId = GlobalConfig.getRandomString(60);

        onPageUploadListener.onNewPage(pageId);
        /**
         * implement upload service here and other here
         *
         */

        final String[] postContentHtml = {postContent};
        //first upload all the images

        if(imagesListToUpload.size()==0){
            //upload page directly here no need to upload images

            //postTitle,postContentHtml[0]
            Log.e("pageContentInUploadService",postContentHtml[0]);
                writePageToDatabase(postTitle,postContent);
            return;
        }else {
            numberOfMedia =imagesListToUpload.size();

            for (String localUrl : imagesListToUpload) {
                uploadImageToServer(localUrl, new ImageUploadListener() {
                    @Override
                    public void onComplete(String localUrl, String downloadUrl) {
                        super.onComplete(localUrl, downloadUrl);
                        //replace the url when completed.
                        postContentHtml[0] = postContentHtml[0].replaceAll(localUrl, downloadUrl);
                        uploaded++;
                        if (uploaded == imagesListToUpload.size()) {
                            //if all images uploaded successfully save page to database now.
                            //postTitle,postContentHtml[0]
                            Log.e("pageContentInUploadService_withImages_", postContentHtml[0]);

                            writePageToDatabase(postTitle, postContentHtml[0]);

                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        super.onFailed(throwable);
                        onPageUploadListener.onFailed(pageId, throwable.getMessage());
                    }
                });


            }

            //post here (just title and page content )
            //postPage();
            //uploadPage();

        }
    }

    public void uploadImageToServer(String url, ImageUploadListener listener){
        //upload the image here

        //call this on success (test listener like this)
        //imageUploader.uploadOnServer(new Listener(){
        // public void onSuccess(String downloadUrl){
        // listener.onComplete(url, downloadUrl);
        // }
        // })
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String imageId = GlobalConfig.getRandomString(15);
            StorageReference storageReference = null;
            if(isTutorialPage) {
                storageReference = GlobalConfig.getFirebaseStorageInstance().getReference().child(GlobalConfig.ALL_USERS_KEY + "/" + GlobalConfig.getCurrentUserId() + "/" + GlobalConfig.ALL_LIBRARY_KEY + "/" + libraryId + "/" + GlobalConfig.ALL_TUTORIAL_KEY + "/" + tutorialId + "/" + GlobalConfig.ALL_TUTORIAL_PAGES_KEY + "/" + pageId + "/ALL_IMAGES/" + imageId + ".PNG");
            }else{
                storageReference = GlobalConfig.getFirebaseStorageInstance().getReference().child(GlobalConfig.ALL_USERS_KEY + "/" + GlobalConfig.getCurrentUserId() + "/" + GlobalConfig.ALL_LIBRARY_KEY + "/" + libraryId + "/" + GlobalConfig.ALL_TUTORIAL_KEY + "/" + tutorialId + "/" + GlobalConfig.ALL_FOLDERS_KEY+"/"+folderId+"/"+GlobalConfig.ALL_FOLDER_PAGES_KEY + "/" + pageId + "/ALL_IMAGES/" + imageId + ".PNG");

            }
            final StorageReference finalStorageReference = storageReference;

            UploadTask uploadTask = storageReference.putFile(Uri.parse(url));
            numberOfProgressingMedia++;

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                     totalBytes =totalBytes+ snapshot.getTotalByteCount();
                     totalBytesTransferred =totalBytesTransferred+ snapshot.getBytesTransferred();
                     progressCount = (int) ((totalBytesTransferred  /totalBytes) *100.0);
                     if(numberOfMedia == numberOfProgressingMedia) {
                         onPageUploadListener.onProgress(pageId, progressCount);
                     }

                }
            })
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {


                                        return finalStorageReference.getDownloadUrl();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            String imageDownloadUrl = String.valueOf(task.getResult());

                                            listener.onComplete(url,imageDownloadUrl);

                                        }
                                        else{
                                       listener.onFailed(new Throwable(task.getException().getMessage()));

                                        }
                                    }
                                });
                            }
                            else {
                                //failed to upload the image
                                listener.onFailed(new Throwable(task.getException().getMessage()));


                            }
                        }
                    });

        }

//        String downloadUrl = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_light_color_272x92dp.png";
//        listener.onComplete(url, downloadUrl);
    }

    public class ImageUploadListener implements  ImageUploadInterface{

        @Override
        public void onComplete(String localUrl, String downloadUrl) {

        }

        @Override
        public void onFailed(Throwable throwable) {

        }
    }

    interface ImageUploadInterface{
        public void onComplete(String localUrl, String downloadUrl);
        public void onFailed(Throwable throwable);
    }

    //this is the method that triggers the page upload from the UploadPageManagerService class
    private void startPageUploadService(ArrayList<String> mediaFinalListToUpload){
        Intent intent = new Intent(getApplicationContext(),UploadPageManagerService.class);
        intent.putExtra(GlobalConfig.PAGE_ID_KEY,pageId);
        intent.putExtra(GlobalConfig.LIBRARY_ID_KEY,libraryId);
        intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
        intent.putExtra(GlobalConfig.FOLDER_ID_KEY,folderId);
        intent.putExtra(GlobalConfig.PAGE_TITLE_KEY,pageTitle);
        intent.putExtra(GlobalConfig.IS_PUBLIC_KEY,isPublic);
        intent.putExtra(GlobalConfig.PAGE_CONTENT_KEY,pageContent);
        intent.putExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,isTutorialPage);
        intent.putExtra(GlobalConfig.IS_CREATE_NEW_PAGE_KEY,isCreateNewPage);
        intent.putExtra(GlobalConfig.IS_PAGE_COVER_PHOTO_CHANGED_KEY,isCoverImageChanged);
        intent.putExtra(GlobalConfig.PAGE_COVER_PHOTO_DOWNLOAD_URL_KEY,retrievedCoverPhotoDownloadUrl);
        intent.putExtra(GlobalConfig.PAGE_MEDIA_URL_LIST_KEY,mediaFinalListToUpload);
        intent.putExtra(GlobalConfig.ACTIVE_PAGE_MEDIA_URL_LIST_KEY,retrievedActivePageMediaUrlArrayList);
        intent.putExtra(GlobalConfig.PAGE_NUMBER_KEY,pageNumber);
        startService(intent);

        isForcedExit = true;
        this.onBackPressed();

    }
    private boolean validateForm() {

        if(pageTitleEditText.getText().toString().trim().equals("")){
            pageTitleEditText.setError("Please enter a title for a page.");
            pageTitleEditText.requestFocus();
            return true;
        }

//Allow the user to either add page cover or not
//        if(coverImageUrl == null || coverImageUrl.equals("")){
//            Toast.makeText(UploadPageActivity.this, "Please choose a cover image for page.",Toast.LENGTH_SHORT).show();
//            return true;
//        }

        if(pageContent == null || pageContent.equals("")){
           Toast.makeText(UploadPageActivity.this, "Please enter a description.",Toast.LENGTH_SHORT).show();
           return true;
        }
        return false;
    }

    private void initUI(){

    pageTitleEditText = findViewById(R.id.pageTitleEditTextId);
        coverImageView =findViewById(R.id.coverImage);
        visibilitySwitch = findViewById(R.id.visibilitySwitchId);
    //containerLinearLayout = findViewById(R.id.containerLinearLayoutId);
    addImageActionButton = findViewById(R.id.addImageActionButtonId);
    addTodoListActionButton = findViewById(R.id.addTodoListActionButtonId);
    addTableActionButton = findViewById(R.id.addTableActionButtonId);
        imageProcessingIndicatorView = findViewById(R.id.imageProcessingIndicatorViewId);
    btn = findViewById(R.id.btn);
        coverUploadButton=findViewById(R.id.coverImagePicker);
    //addEditText(0);
    //createPartition();


/**load the content editor for the user.
 change any in this method here
 */
    loadContentEditor();

    //init progress.
    alertDialog = new AlertDialog.Builder(UploadPageActivity.this)
            .setCancelable(false)
            .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
            .create();
}

 private void createConfirmExitDialog(){
      AlertDialog.Builder builder = new AlertDialog.Builder(this);

     builder.setTitle("Exit");
     builder.setMessage("Click exit button to exit the screen");
     builder.setCancelable(false);
     builder.setIcon(R.drawable.ic_baseline_error_outline_24);
     builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
            UploadPageActivity.super.onBackPressed();
         }
     })
             .setNegativeButton("Stay back", null);
     confirmExitDialog = builder.create();
     confirmExitDialog.show();

 }

    private void loadContentEditor() {
        //load the content editor here and render there that make it easy to handle without any external codes.
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;

        wysiwygEditor=findViewById(R.id.editor);
        wysiwygEditor.setEditorHeight(screenHeight-844);
        wysiwygEditor.setEditorFontSize(16);
        wysiwygEditor.setPadding(10, 10, 10, 30);
        wysiwygEditor.setPlaceholder("Insert your content here...");
        wysiwygEditor.performClick();



         action_undo=findViewById(R.id.action_undo);
         action_redo=findViewById(R.id.action_redo);
         action_bold=findViewById(R.id.action_bold);
         action_italic=findViewById(R.id.action_italic);
         action_subscript=findViewById(R.id.action_subscript);
         action_superscript=findViewById(R.id.action_superscript);
         action_strikethrough=findViewById(R.id.action_strikethrough);
         action_underline=findViewById(R.id.action_underline);
         action_heading1=findViewById(R.id.action_heading1);
         action_heading2=findViewById(R.id.action_heading2);
         action_heading3=findViewById(R.id.action_heading3);
         action_heading4=findViewById(R.id.action_heading4);
         action_heading5=findViewById(R.id.action_heading5);
         action_heading6=findViewById(R.id.action_heading6);
         action_txt_color=findViewById(R.id.action_txt_color);
          action_bg_color=findViewById(R.id.action_bg_color);
          action_indent=findViewById(R.id.action_indent);
         action_align_left=findViewById(R.id.action_align_left);
         action_align_center=findViewById(R.id.action_align_center);
         action_align_right=findViewById(R.id.action_align_right);
         action_align_justify=findViewById(R.id.action_align_justify);
         action_blockquote=findViewById(R.id.action_blockquote);
          action_insert_bullets=findViewById(R.id.action_insert_bullets);
         action_insert_numbers=findViewById(R.id.action_insert_numbers);
         action_insert_image=findViewById(R.id.action_insert_image);
         action_insert_link=findViewById(R.id.action_insert_link);
         action_insert_checkbox=findViewById(R.id.action_insert_checkbox);
         preview=findViewById(R.id.preview);
         insert_latex=findViewById(R.id.insert_latex);
         insert_code=findViewById(R.id.insert_code);
         action_change_font_type=findViewById(R.id.action_change_font_type);
         latex_editor=findViewById(R.id.latext_editor);
         submit_latex=findViewById(R.id.submit_latex);
         latex_equation=findViewById(R.id.latex_equation);
         action_insert_table = findViewById(R.id.action_table);
        action_insert_video=findViewById(R.id.action_insert_video);
        Glide.with(this)
                .load("https://via.placeholder.com/640x360?text=Cover%20Image")
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(coverImageView);


        choosePhotoPickerModal= new LEBottomSheetDialog(UploadPageActivity.this)
                .addOptionItem("Camera", R.drawable.ic_baseline_photo_camera_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                choosePhotoPickerModal.hide();
                openCamera();
                    }
                },0)
                .addOptionItem("Gallery", R.drawable.baseline_insert_photo_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        choosePhotoPickerModal.hide();
                        openGallery();
                    }
                },0)
                .addOptionItem("Insert image URL", R.drawable.baseline_link_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        choosePhotoPickerModal.hide();

                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.single_edit_text_layout, null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadPageActivity.this);
                        builder.setView(dialogView);
                        builder.setTitle("Insert Photo from url:")
                                .setMessage("Enter or paste photo url to insert");

                        builder.setPositiveButton("Insert", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // handle OK button click
                                EditText editText = dialogView.findViewById(R.id.edit_text);
                                String inputText = editText.getText().toString();
                                // do something with the input text
                                if(GlobalHelpers.isValidUrl(inputText)){
                                    wysiwygEditor.insertImage(inputText,"IMG");
                                }

                            }
                        });

                        builder.setNegativeButton("Cancel", null);

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                },0)
                .render();

        chooseVideoPickerModal= new LEBottomSheetDialog(UploadPageActivity.this)
                .addOptionItem("Video Gallery", R.drawable.baseline_insert_photo_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chooseVideoPickerModal.hide();
                        openVideoGallery();
                    }
                },0)
                .addOptionItem("Insert video URL", R.drawable.baseline_link_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chooseVideoPickerModal.hide();

                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.single_edit_text_layout, null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadPageActivity.this);
                        builder.setView(dialogView);
                        builder.setTitle("Insert video url:")
                                .setMessage("enter or paste video url to insert");

                        builder.setPositiveButton("Insert", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // handle OK button click
                                EditText editText = dialogView.findViewById(R.id.edit_text);
                                String inputText = editText.getText().toString();
                                // do something with the input text
                                if(GlobalHelpers.isValidUrl(inputText)){
                                    wysiwygEditor.insertVideo(inputText);
                                }

                            }
                        });

                        builder.setNegativeButton("Cancel", null);

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                },0)
                .render();



        action_insert_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show modal here to enter rows,cols and hasheading

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.table_dialog_layout, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(UploadPageActivity.this);
                builder.setView(dialogView);
                builder.setTitle("Insert Table")
                        .setMessage("-Check the box if you need your table with headings \n-Enter number of rows and columns you want to create in your table");

                builder.setPositiveButton("Insert", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // handle OK button click
                        CheckBox headingCheckbox = dialogView.findViewById(R.id.headingCheckboxId);
                        EditText rowEditText = dialogView.findViewById(R.id.row_numbers_edit_text);
                        EditText columnEditText = dialogView.findViewById(R.id.column_numbers_edit_text);
                        // do something with the input text
                        int rows = 0;
                        int columns = 0;
                        if(rowEditText.getText()!=null && !((rowEditText.getText()+"").isEmpty()) ){
                            rows = Integer.parseInt(rowEditText.getText().toString());
                        }
                        if(columnEditText.getText()!=null && !((columnEditText.getText()+"").isEmpty()) ){
                            columns = Integer.parseInt(columnEditText.getText().toString());
                        }
                        wysiwygEditor.insertTable(rows,columns,headingCheckbox.isChecked());

                    }
                });

                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        action_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.undo();
            }
        });

        action_redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.redo();
            }
        });

        action_bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setBold();
            }
        });

        action_italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setItalic();
            }
        });

        action_subscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setSubscript();
            }
        });

        action_superscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setSuperscript();
            }
        });

        action_strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setStrikeThrough();
            }
        });

        action_underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setUnderline();
            }
        });

        action_heading1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setHeading(1);
            }
        });


        action_heading2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setHeading(2);
            }
        });
        action_heading3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setHeading(3);
            }
        });

        action_heading4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setHeading(4);
            }
        });

        action_heading5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setHeading(5);
            }
        });

        action_heading6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setHeading(6);
            }
        });

        action_txt_color.setOnClickListener(new View.OnClickListener() {
            boolean isChanged = false;
            @Override
            public void onClick(View view) {
                // Java Code
                ColorPickerView colorPickerView = new ColorPickerView.Builder(UploadPageActivity.this)
                        .setColorListener(new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                wysiwygEditor.setTextColor(envelope.getColor());
                            }
                        })
                        .setPreferenceName("MyColorPicker")
                        .setActionMode(ActionMode.LAST)
                        .setPaletteDrawable(ContextCompat.getDrawable(UploadPageActivity.this, R.drawable.palette))
                        .build();
                colorPickerView.setPadding(20,20,20,20);

                new AlertDialog.Builder(UploadPageActivity.this)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setTitle("Pick text Color")
                        .setView(colorPickerView)
                        .show();

                //wysiwygEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
//                isChanged = !isChanged
            }
        });



        action_bg_color.setOnClickListener(new View.OnClickListener() {
            private boolean isChanged = false;
            @Override
            public void onClick(View view) {
                ColorPickerView colorPickerView = new ColorPickerView.Builder(UploadPageActivity.this)
                        .setColorListener(new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                wysiwygEditor.setTextBackgroundColor(envelope.getColor());
                            }
                        })
                        .setPreferenceName("MyColorPicker")
                        .setActionMode(ActionMode.LAST)
                        .setPaletteDrawable(ContextCompat.getDrawable(UploadPageActivity.this, R.drawable.palette))
                        .build();
                colorPickerView.setPadding(20,20,20,20);

                new AlertDialog.Builder(UploadPageActivity.this)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setTitle("Pick Background Color")
                        .setView(colorPickerView)
                        .show();
            }
        });


        action_indent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setIndent();
            }
        });


        action_align_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setAlignLeft();
            }
        });

        action_align_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setAlignCenter();
            }
        });

        action_align_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setAlignRight();
            }
        });

        action_align_justify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setAlignJustifyFull();
            }
        });

        action_blockquote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setBlockquote();
            }
        });

        action_insert_bullets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setBullets();
            }
        });

        action_insert_numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setNumbers();
            }
        });

        action_insert_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhotoPickerModal.show();
//                wysiwygEditor.insertImage(
//                        "https://i.postimg.cc/JzL891Fm/maxresdefault.jpg",
//                        "Night Sky"
//                );
            }
        });

        action_insert_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.insertLink(
                        "https://github.com/onecode369",
                        "One Code"
                );
            }
        });

        action_insert_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.insertTodo();
            }
        });


        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!visible){
                    wysiwygEditor.setInputEnabled(false);
                    preview.setImageResource(R.drawable.visibility_off);
                }else{
                    wysiwygEditor.setInputEnabled(true);
                    preview.setImageResource(R.drawable.visibility);
                }
                visible = !visible;
            }
        });


        action_insert_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseVideoPickerModal.show();
//                wysiwygEditor.insertVideo("https://www.youtube.com/watch?v=ZChsMjm5-mk");
               // wysiwygEditor.insertVideo("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4");
//                LayoutInflater inflater = getLayoutInflater();
//                View dialogView = inflater.inflate(R.layout.single_edit_text_layout, null);
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(UploadPageActivity.this);
//                builder.setView(dialogView);
//                builder.setTitle("Insert video url:")
//                                .setMessage("enter or paste video url to insert");
//
//                builder.setPositiveButton("Insert", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // handle OK button click
//                        EditText editText = dialogView.findViewById(R.id.edit_text);
//                        String inputText = editText.getText().toString();
//                        // do something with the input text
//                        if(GlobalHelpers.isValidUrl(inputText)){
//                            wysiwygEditor.insertVideo(inputText);
//                        }
//
//                    }
//                });
//
//                builder.setNegativeButton("Cancel", null);
//
//                AlertDialog dialog = builder.create();
//                dialog.show();

            }
        });



        insert_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setCode();
            }
        });

        insert_latex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(latex_editor.getVisibility() == View.GONE) {
                    latex_editor.setVisibility(View.VISIBLE);
                    submit_latex.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            wysiwygEditor.insertLatex(latex_equation.toString());
                        }
                    });
                }else{
                    latex_editor.setVisibility(View.GONE);
                }
            }
        });

        action_change_font_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setFontType("Times New Roman");

            }
        });


    }
    private void showPageUploadConfirmationDialog(ArrayList<String> mediaFinalListToUpload){

       AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm your action");
        builder.setMessage("You are about to upload your page, please confirm if you are done editing your page");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_baseline_error_outline_24);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!String.valueOf(pageTitleEditText.getText()).trim().equals("") ) {
                    if(isImageProcessingCompleted || !(imageProcessingIndicatorView.getVisibility()==View.VISIBLE)) {
                        startPageUploadService(mediaFinalListToUpload);
                    }else{
                        GlobalConfig.createSnackBar(UploadPageActivity.this,imageProcessingIndicatorView,"Please wait images are still processing...When it's done processing you can continue",Snackbar.LENGTH_INDEFINITE);
                    }
                }else{
                    pageTitleEditText.requestFocus();
                    pageTitleEditText.setError("You must enter page title");

                }

            }
        })
                .setNegativeButton("Edit more", null);
        confirmationDialog = builder.create();
        confirmationDialog.show();

        // successDialog.show();

    }


    private void startInitializationForEdition(){
        toggleProgress(true);
            DocumentReference documentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_TUTORIAL_PAGES_KEY).document(pageId);
            if(!isTutorialPage){
                documentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_FOLDERS_KEY).document(folderId).collection(GlobalConfig.ALL_FOLDER_PAGES_KEY).document(pageId);

            }
            documentReference.get()
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toggleProgress(false);

                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            //THIS IS THE TITLE OF THE PAGE
                            String pageTitle = ""+ documentSnapshot.get(GlobalConfig.PAGE_TITLE_KEY);
                            isPublic = documentSnapshot.get(GlobalConfig.IS_PUBLIC_KEY)!=null ? documentSnapshot.getBoolean(GlobalConfig.IS_PUBLIC_KEY) :true;
                            visibilitySwitch.setChecked(isPublic);
                            //USE THIS URL TO DOWNLOAD PAGE'S COVER IMAGE
                            retrievedActivePageMediaUrlArrayList =  documentSnapshot.get(GlobalConfig.ACTIVE_PAGE_MEDIA_URL_LIST_KEY)!=null ? (ArrayList<String>) documentSnapshot.get(GlobalConfig.ACTIVE_PAGE_MEDIA_URL_LIST_KEY) :new ArrayList<>();
                            retrievedCoverPhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.PAGE_COVER_PHOTO_DOWNLOAD_URL_KEY);
                            Glide.with(UploadPageActivity.this)
                                    .load(retrievedCoverPhotoDownloadUrl)
                                    .into(coverImageView);

                            //THIS IS THE PAGE CONTENT IN HTML FORMAT , USE IT AND RENDER TO READABLE TEXT
                            String html = ""+ documentSnapshot.get(GlobalConfig.PAGE_CONTENT_KEY);
                            pageTitleEditText.setText(pageTitle);
                            wysiwygEditor.setHtml(html);

                            toggleProgress(false);

                        }
                    });

        }





    /**This posts the page content to database*/
    private void writePageToDatabase(String pageTitle, String pageContent) {
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
        DocumentReference pageDocumentReference = null;
        if(isTutorialPage){
            pageDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_TUTORIAL_PAGES_KEY).document(pageId);
        }else{
            pageDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_FOLDERS_KEY).document(folderId).collection(GlobalConfig.ALL_FOLDER_PAGES_KEY).document(pageId);

        }

        HashMap<String, Object> pageTextPartitionsDataDetailsHashMap = new HashMap<>();
        pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.PAGE_TITLE_KEY, pageTitle);
        pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.LIBRARY_ID_KEY, libraryId);
        pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.TUTORIAL_ID_KEY, tutorialId);
        pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.FOLDER_ID_KEY, folderId);
        pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.PAGE_ID_KEY, pageId);
        pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.AUTHOR_ID_KEY, GlobalConfig.getCurrentUserId());
        pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.PAGE_CONTENT_KEY, pageContent);
        pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.DATE_TIME_STAMP_PAGE_CREATED_KEY, FieldValue.serverTimestamp());

        writeBatch.set(pageDocumentReference,pageTextPartitionsDataDetailsHashMap, SetOptions.merge());

        DocumentReference documentReference = null;
        if(isTutorialPage){
            documentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId);
            GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_PAGE_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryId, tutorialId, folderId, pageId, null,  new GlobalConfig.ActionCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailed(String errorMessage) {

                }
            });

        }else{
            documentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_FOLDERS_KEY).document(folderId);
            GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_FOLDER_PAGE_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryId, tutorialId, folderId, pageId, null,  new GlobalConfig.ActionCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailed(String errorMessage) {

                }
            });

        }
        HashMap<String, Object> incrementPageNumberHashMap = new HashMap<>();
        incrementPageNumberHashMap.put(GlobalConfig.TOTAL_NUMBER_OF_PAGES_CREATED_KEY, FieldValue.increment(1L));
        writeBatch.update(documentReference,incrementPageNumberHashMap);
        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        writePageToDatabase( pageTitle, pageContent);
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if(isTutorialPage){
                    GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_PAGE_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryId, tutorialId, folderId, pageId, null,  new GlobalConfig.ActionCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailed(String errorMessage) {

                        }
                    });

                }else{
                    GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_FOLDER_PAGE_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryId, tutorialId, folderId, pageId, null,  new GlobalConfig.ActionCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailed(String errorMessage) {

                        }
                    });

                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //delay the success for some seconds to allow some asynchronous processes to finish
                        onPageUploadListener.onSuccess(pageId);

                    }
                },2000);
            }
        });
//            }


    }

    //interface for page upload callbacks
    interface OnPageUploadListener{
        void onNewPage(String pageId);
        void onFailed(String pageId, String errorMessage);
        void onProgress(String pageId, int progressCount);
        void onSuccess(String pageId);
    }



    private void fetchIntentData(){
        Intent intent = getIntent();
        isTutorialPage = intent.getBooleanExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,true);
        tutorialId = intent.getStringExtra(GlobalConfig.TUTORIAL_ID_KEY);
        libraryId = intent.getStringExtra(GlobalConfig.LIBRARY_ID_KEY);
        folderId = intent.getStringExtra(GlobalConfig.FOLDER_ID_KEY);
        pageNumber = intent.getIntExtra(GlobalConfig.PAGE_NUMBER_KEY,0);
        isCreateNewPage = intent.getBooleanExtra(GlobalConfig.IS_CREATE_NEW_PAGE_KEY,true);
        if(!isCreateNewPage){
            pageId = intent.getStringExtra(GlobalConfig.PAGE_ID_KEY);
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
            if (requestCode == COVER_IMAGE_GALLERY_REQUEST_CODE){
                fireCoverImageGalleryIntent();
            }

        }
    }

    public void openGallery(){
        requestForPermissionAndPickImage(GALLERY_PERMISSION_REQUEST_CODE);

    }
    public void openCamera(){
        requestForPermissionAndPickImage(CAMERA_PERMISSION_REQUEST_CODE);
    }
    public void openVideoGallery(){
        requestForPermissionAndPickImage(VIDEO_PERMISSION_REQUEST_CODE);
    }

    public void fireGalleryIntent(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        openGalleryLauncher.launch(galleryIntent);
    }
    public void fireVideoGalleryIntent(){
        Intent videoGalleryIntent = new Intent();
        videoGalleryIntent.setAction(Intent.ACTION_PICK);
        videoGalleryIntent.setType("video/*");
        openVideoLauncher.launch(videoGalleryIntent);
    }
    public void fireCoverImageGalleryIntent(){
        Intent galleryCoverIntent = new Intent();
        galleryCoverIntent.setAction(Intent.ACTION_PICK);
        galleryCoverIntent.setType("image/*");
        openCoverImageLauncher.launch(galleryCoverIntent);
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
            if(requestCode == VIDEO_PERMISSION_REQUEST_CODE) {
                fireVideoGalleryIntent();
            }
            if(requestCode== COVER_IMAGE_GALLERY_REQUEST_CODE){
                fireCoverImageGalleryIntent();
            }
        }



    }

    private void toggleProgress(boolean show)
    {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
    }

    private void showPageProgressNotification(){
        // Create a notification channel (required for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("my_channel_id", "My Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

// Create a custom layout for the notification
//        @SuppressLint("RemoteViewLayout")
        notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_layout);
        //set image cover icon here
        notificationLayout.setImageViewResource(R.id.icon, R.drawable.placeholder);
        // Create an explicit intent for launching the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        // Create a PendingIntent from the intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

         notificationId = (int) System.currentTimeMillis(); // generate a unique ID
                 builder = new NotificationCompat.Builder(UploadPageActivity.this, "my_channel_id")
                .setSmallIcon(R.drawable.baseline_notifications_24) //notification icon
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCustomBigContentView(notificationLayout)
                .setCustomHeadsUpContentView(notificationLayout)
                .setContentIntent(pendingIntent) //intent to start when click notification
                .setOnlyAlertOnce(true) //only show once not when updating progress
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Expanded content"))
                .setOngoing(true) //set ongoing with is not cancelable by user
                .setAutoCancel(false); //auto cancel false

        notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());

        // Create a Handler to update the notification loader and percent every second
//        Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//            //uplod percentage count .
////            int percentageCompleted = 0;
//            @Override
//            public void run() {
//                if(percentageCompleted==100){
//                    handler.removeCallbacks(this);
//                    //cancel all or single notification
//                    notificationManager.cancelAll();
//                    showCompletedNotification( pageId, folderId, tutorialId, isTutorialPage, pageTitle);
//                }else{
//                    updatePageProgressNotification(notificationLayout,percentageCompleted,builder);
//                    // Update the  with the current time
////                    percentageCompleted++;
////
////                    notificationLayout.setProgressBar(R.id.progress_bar,100,0,false);
////                    notificationLayout.setTextViewText(R.id.percent,percentageCompleted+"% completed");
//
//                    // Update the notification
////                    NotificationManagerCompat.from(getApplicationContext()).notify(notificationId, builder.build());
//                    // Schedule the next update in 1 second
//                    //handler.postDelayed(this, 100);//make it 1000
//                }
//            }
//        };
//// Start the periodic updates
//        handler.postDelayed(runnable, 100);//make it 1000

    }

    private void updatePageProgressNotification(RemoteViews notificationLayout,int percentageCompleted, NotificationCompat.Builder builder){

        notificationLayout.setProgressBar(R.id.progress_bar,100,percentageCompleted,false);
        notificationLayout.setTextViewText(R.id.percent,percentageCompleted+"% completed");
        NotificationManagerCompat.from(getApplicationContext()).notify(notificationId, builder.build());

    }

    public void showCompletedNotification(String pageId,String folderId,String tutorialId,boolean isTutorialPage,String pageTitle){
        @SuppressLint("RemoteViewLayout")
        //customize notification
                String pageTitle1 = pageTitle.length()>10 ? pageTitle.substring(0,10) :pageTitle;
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_layout);
        notificationLayout.setTextViewText(R.id.title, pageTitle1 + " page Upload Completed");
        notificationLayout.setProgressBar(R.id.progress_bar,100,100,false);
        notificationLayout.setTextViewText(R.id.percent, "100% completed");
        notificationLayout.setImageViewResource(R.id.icon,R.drawable.placeholder);


        //intent where to redirect the user
        Intent intent = new Intent(UploadPageActivity.this, PageActivity.class);
        intent.putExtra(GlobalConfig.PAGE_ID_KEY,pageId);
        intent.putExtra(GlobalConfig.FOLDER_ID_KEY,folderId);
        intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
        intent.putExtra(GlobalConfig.AUTHOR_ID_KEY,GlobalConfig.getCurrentUserId());
        intent.putExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,isTutorialPage);
        // Create a PendingIntent from the intent
        PendingIntent pendingIntent = PendingIntent.getActivity(UploadPageActivity.this, 0, intent, 0);

        int notificationId = (int) System.currentTimeMillis(); // generate a unique ID
        NotificationCompat.Builder builder = new NotificationCompat.Builder(UploadPageActivity.this, "my_channel_id")
                .setSmallIcon(R.drawable.baseline_notifications_24) //notification icon
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCustomBigContentView(notificationLayout)
                .setCustomHeadsUpContentView(notificationLayout)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Expanded content"))
                .setAutoCancel(false);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(UploadPageActivity.this);
        notificationManager.notify(notificationId, builder.build());

    }

    @Deprecated
    void createPartition(){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View partitionView  =  layoutInflater.inflate(R.layout.page_partition_layout,containerLinearLayout,false);
        EditText bodyEditText = partitionView.findViewById(R.id.bodyEditTextId);

        LinearLayout imageLinearLayout = partitionView.findViewById(R.id.imageLinearLayoutId);
        ImageButton addImageActionButton = partitionView.findViewById(R.id.addImageActionButtonId);

        addImageActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiverLinearLayout = imageLinearLayout;
                GlobalConfig.createPopUpMenu(getApplicationContext(), R.menu.pick_image_menu, addImageActionButton, new GlobalConfig.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClicked(MenuItem item) {
                        if(item.getItemId() == R.id.galleryId)openGallery();
                        else if(item.getItemId() == R.id.cameraId)openCamera();
                        currentFocusView = partitionView;

                        return true;
                    }
                });
                addTodoGroup();
            }
        });
        containerLinearLayout.addView(partitionView);
    }
    @Deprecated
    void addEditText(int validPosition){

                    EditText editText = new EditText(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    editText.setLayoutParams(layoutParams);
                    ColorDrawable white = new ColorDrawable();
                    white.setColor(getResources().getColor(R.color.white));
                    editText.setBackground(white);
                  editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                      @Override
                      public void onFocusChange(View view, boolean b) {
                          UploadPageActivity.this.validPosition = containerLinearLayout.indexOfChild(editText) + 1;

                      }
                  });
//                    editText.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            UploadPageActivity.this.validPosition = containerLinearLayout.indexOfChild(editText) + 1;
//                        }
//                    });
                    containerLinearLayout.addView(editText,validPosition);
//        UploadPageActivity.this.validPosition = containerLinearLayout.indexOfChild(editText) + 1;


    }
    @Deprecated
    private ImageView getImage(){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View imageView  =  layoutInflater.inflate(R.layout.page_image_layout,containerLinearLayout,false);
        ImageView image = imageView.findViewById(R.id.imageViewId);
        ImageView removeImage = imageView.findViewById(R.id.removeImageId);

        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText textBefore = (EditText) containerLinearLayout.getChildAt(containerLinearLayout.indexOfChild(imageView)-1);
                EditText textAfter = (EditText) containerLinearLayout.getChildAt(containerLinearLayout.indexOfChild(imageView)+1);

                textBefore.append(textAfter.getText().toString());
                containerLinearLayout.removeView(textAfter);
                containerLinearLayout.removeView(imageView);
            }
        });
        containerLinearLayout.addView(imageView,validPosition);
        addEditText(containerLinearLayout.indexOfChild(imageView)+1);
        return image;
    }
    @Deprecated
    void uploadPage(){
        ArrayList<ArrayList<String>> allPageTextPartitionsDataDetailsArrayList = new ArrayList<>();

//        UploadPageManagerService.addUploadListeners(new UploadPageManagerService.OnPageUploadListener() {
//            @Override
//            public void onNewPage(String pageId) {
//                Toast.makeText(getApplicationContext(), "New page id: "+ pageId, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailed(String pageId) {
//                Toast.makeText(getApplicationContext(), "page upload failed: "+ pageId, Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onProgress(String pageId, int progressCount) {
//                Toast.makeText(getApplicationContext(), "New page uploading: "+ pageId, Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onSuccess(String pageId) {
//                Toast.makeText(getApplicationContext(), "New page upload succeeded: "+ pageId, Toast.LENGTH_SHORT).show();
//
//            }
//        });
        UploadPageManagerService.setInitialVariables(pageId);

        for(int i=0; i<containerLinearLayout.getChildCount(); i++){
            String partitionId = GlobalConfig.getRandomString(10) + GlobalConfig._IS_PARTITION_ID_IS_FOR_IDENTIFYING_PARTITIONS_KEY;

            LinearLayout partitionLinearLayout = (LinearLayout) containerLinearLayout.getChildAt(i);
            TextView textDataPartitionTextView =(TextView) partitionLinearLayout.getChildAt(0);
            String textDataPartition = textDataPartitionTextView.getText().toString();
            ArrayList<String>textPartitionsDataDetailsArrayList = new ArrayList<>();
            if(!textDataPartition.isEmpty()){
                textPartitionsDataDetailsArrayList.add(textDataPartition);

            }
            if(!textPartitionsDataDetailsArrayList.contains(partitionId)){
                textPartitionsDataDetailsArrayList.add(partitionId);
            }
            //add text partition array to the general page partitions array
            allPageTextPartitionsDataDetailsArrayList.add(textPartitionsDataDetailsArrayList);

            LinearLayout imageLinearLayout =(LinearLayout) partitionLinearLayout.getChildAt(1);
            ArrayList<byte[]> imagePartitionByteArrayList = new ArrayList<>();

            if(imageLinearLayout.getChildCount() > 0) {
                for (int j = 0; j < imageLinearLayout.getChildCount(); j++) {
                    ConstraintLayout constraintLayout = (ConstraintLayout) imageLinearLayout.getChildAt(1);
                    ImageView imagePartitionImageView = (ImageView) constraintLayout.findViewById(R.id.imageViewId);
                    imagePartitionImageView.setDrawingCacheEnabled(true);
                    Bitmap bitmap = imagePartitionImageView.getDrawingCache();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    imagePartitionByteArrayList.add(bytes);
                }
            }
            //call the service method for uploading partition images.
            UploadPageManagerService.uploadPartitionImageDataToPage(libraryId,tutorialId,pageId,partitionId,imagePartitionByteArrayList,"IMAGE_PARTITION_ARRAY_"+i,containerLinearLayout.getChildCount());

        }
        //call the service method here for uploading text data partition at once
        UploadPageManagerService.uploadPartitionTextDataToPage(libraryId,tutorialId,pageId,allPageTextPartitionsDataDetailsArrayList,containerLinearLayout.getChildCount());

    }
    @Deprecated
    void addTodoItem(View todoGroupView,LinearLayout linearLayout){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View todoItemView  =  layoutInflater.inflate(R.layout.page_to_do_item_layout,linearLayout,false);
        ImageView removeItem  =  todoItemView.findViewById(R.id.removeTodoItemActionImageId);
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(linearLayout.getChildCount()==1){

                    EditText textBefore = (EditText) containerLinearLayout.getChildAt(containerLinearLayout.indexOfChild(todoGroupView)-1);
                    EditText textAfter = (EditText) containerLinearLayout.getChildAt(containerLinearLayout.indexOfChild(todoGroupView)+1);

                    textBefore.append(textAfter.getText().toString());
                    containerLinearLayout.removeView(textAfter);
                    containerLinearLayout.removeView(todoGroupView);
                }else{
                    linearLayout.removeView(todoItemView);
                }

            }
        });
        linearLayout.addView(todoItemView);
    }
    @Deprecated
    void addTodoGroup(){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View todoGroupView  =  layoutInflater.inflate(R.layout.page_to_do_group_layout,containerLinearLayout,false);
        LinearLayout todoLinearLayout = todoGroupView.findViewById(R.id.todoItemLinearLayoutId);
        ImageView addMoreItemActionButton = todoGroupView.findViewById(R.id.addMoreItemActionButtonId);
     addTodoItem(todoGroupView,todoLinearLayout);

     addMoreItemActionButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             addTodoItem(todoGroupView,todoLinearLayout);
         }
     });

        containerLinearLayout.addView(todoGroupView,validPosition);

     addEditText(containerLinearLayout.indexOfChild(todoGroupView)+1);

 }
    @Deprecated
    void createTable(){
    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View tableView  =  layoutInflater.inflate(R.layout.page_table_layout,containerLinearLayout,false);
    LinearLayout tableLinearLayout = tableView.findViewById(R.id.tableLinearLayoutId);
    ImageView addMoreColumnActionImageView = tableView.findViewById(R.id.addMoreColumnActionImageViewId);
    ImageView addMoreRowActionImageView = tableView.findViewById(R.id.addMoreRowActionImageViewId);
    createTableRow(tableView,tableLinearLayout);
    createTableColumn(tableLinearLayout);
    createTableColumn(tableLinearLayout);
    addMoreRowActionImageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            createTableRow(tableView,tableLinearLayout);
        }
    });

    addMoreColumnActionImageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            createTableColumn(tableLinearLayout);
        }
    });
    containerLinearLayout.addView(tableView,validPosition);
    addEditText(containerLinearLayout.indexOfChild(tableView)+1);

}
    @Deprecated
    void createTableRow(View tableView,LinearLayout tableLinearLayout){
    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View tableRowView  =  layoutInflater.inflate(R.layout.page_table_row_layout,containerLinearLayout,false);
    ImageView removeRowActionImageView = tableRowView.findViewById(R.id.removeRowActionImageViewId);
    LinearLayout tableRowLinearLayout = tableRowView.findViewById(R.id.tableRowLinearLayoutId);
    if(tableLinearLayout.getChildCount()!=0){
        LinearLayout firstRow = (LinearLayout) tableLinearLayout.getChildAt(0);
        LinearLayout rowLinearLayout = (LinearLayout) firstRow.getChildAt(0);
        int numberOfColumns = rowLinearLayout.getChildCount();
        for(int i=0; i<numberOfColumns; i++){
        addTableEditTextCell(tableRowLinearLayout);
        }
    }

    removeRowActionImageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(tableLinearLayout.getChildCount() == 1){
                EditText textBefore = (EditText) containerLinearLayout.getChildAt(containerLinearLayout.indexOfChild(tableView)-1);
                EditText textAfter = (EditText) containerLinearLayout.getChildAt(containerLinearLayout.indexOfChild(tableView)+1);

                textBefore.append(textAfter.getText().toString());
                containerLinearLayout.removeView(textAfter);
                containerLinearLayout.removeView(tableView);
            }else {
                tableLinearLayout.removeView(tableRowView);
            }

        }
    });
    tableLinearLayout.addView(tableRowView);
}
    @Deprecated
    void createTableColumn(LinearLayout tableLinearLayout){
        if(tableLinearLayout.getChildCount()!=0){
            for(int i=0; i<tableLinearLayout.getChildCount(); i++){
                LinearLayout rowContainerLinearLayout = (LinearLayout) tableLinearLayout.getChildAt(i);
                LinearLayout rowLinearLayout = (LinearLayout) rowContainerLinearLayout.getChildAt(0);
                addTableEditTextCell(rowLinearLayout);
            }
        }
}
    @Deprecated
    void addTableEditTextCell(LinearLayout rowLinearLayout){
    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View tableCell  =  layoutInflater.inflate(R.layout.page_table_cell_edit_text,rowLinearLayout,false);
    EditText editTextCell =tableCell.findViewById(R.id.editTextCellId);
//    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,40,1);
//    layoutParams.setMargins(5,5,5,5);
//    editTextCell.setLayoutParams(layoutParams);
    editTextCell.setHint("?");
//    editTextCell.requestFocus();
//    editTextCell.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
//    ColorDrawable gray = new ColorDrawable();
//    gray.setColor(getResources().getColor(R.color.gray));
//    editTextCell.setBackground(gray);

    rowLinearLayout.addView(tableCell);

}

    @Deprecated
    void postPage(){
    pageId = GlobalConfig.getRandomString(60);
GlobalConfig.createSnackBar(this,containerLinearLayout,"Creating "+pageTitleEditText.getText()+" page", Snackbar.LENGTH_INDEFINITE);

//        toggleProgress(true);
preparePage();
//
//    UploadPageManagerService.addUploadListeners(new UploadPageManagerService.OnPageUploadListener() {
//        @Override
//        public void onNewPage(String pageId) {
//            Toast.makeText(getApplicationContext(), "New page id: "+ pageId, Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onFailed(String pageId) {
//            Toast.makeText(getApplicationContext(), "page upload failed: "+ pageId, Toast.LENGTH_SHORT).show();
//            toggleProgress(false);
//
//        }
//
//        @Override
//        public void onProgress(String pageId, int progressCount) {
//            Toast.makeText(getApplicationContext(), progressCount+" page progressing: "+ pageId, Toast.LENGTH_SHORT).show();
//
//        }
//
//        @Override
//        public void onSuccess(String pageId) {
//            Toast.makeText(getApplicationContext(), "New page upload succeeded: "+ pageId, Toast.LENGTH_SHORT).show();
//            toggleProgress(false);
//            GlobalHelpers.showAlertMessage("success",
//                    UploadPageActivity.this,
//                    "Page created successfully",
//                    "You have successfully created your page, go ahead and contribute to Learn Era ");
//
//
//        }
//    });
    UploadPageManagerService.setInitialVariables(pageId);

    ArrayList<ArrayList<String>> allPageTextDataDetailsArrayList = new ArrayList<>();

    Toast.makeText(getApplicationContext(), containerLinearLayout.getChildCount()+"", Toast.LENGTH_SHORT).show();
    int numOfChildrenData = containerLinearLayout.getChildCount();
    int totalNumberOfImages = 0;
    ArrayList<ArrayList<Object>> allImageArrayList = new ArrayList<>();

    for(int i=0; i<containerLinearLayout.getChildCount(); i++){

            if(containerLinearLayout.getChildAt(i) instanceof  EditText){
                //A plain text
                EditText editText = (EditText) containerLinearLayout.getChildAt(i);
                ArrayList<String> pageTextDataTypeDetailsArrayList = new ArrayList<>();

                pageTextDataTypeDetailsArrayList.add(0,GlobalConfig.TEXT_TYPE);
                pageTextDataTypeDetailsArrayList.add(1,containerLinearLayout.indexOfChild(editText)+"");
                pageTextDataTypeDetailsArrayList.add(2,editText.getText().toString());
                allPageTextDataDetailsArrayList.add(pageTextDataTypeDetailsArrayList);

                       // for(int r =0; r<pageTextDataTypeDetailsArrayList.size(); r++){
                        EditText editText1 =  (EditText) containerLinearLayout.getChildAt(0);
//                        editText1.append(pageTextDataTypeDetailsArrayList.get(0)+"-"+pageTextDataTypeDetailsArrayList.get(1)+"-"+pageTextDataTypeDetailsArrayList.get(2)+"_"+containerLinearLayout.indexOfChild(editText)+"_");
                      //  }

//                Toast.makeText(getApplicationContext(), "it is edittext", Toast.LENGTH_SHORT).show();
                if(editText.getText().toString().isEmpty()){
//                    Toast.makeText(getApplicationContext(), editText.getText()+" is removed", Toast.LENGTH_SHORT).show();
//                    containerLinearLayout.removeView(editText);

                }
            }
            else if(containerLinearLayout.getChildAt(i).getId() == R.id.imageConstraintLayoutId){
//                Toast.makeText(getApplicationContext(), "it is image", Toast.LENGTH_SHORT).show();
                totalNumberOfImages++;
                ImageView imageView = containerLinearLayout.getChildAt(i).findViewById(R.id.imageViewId);

                ArrayList<Object> imageArrayList = new ArrayList<>();
                imageArrayList.add(0,containerLinearLayout.indexOfChild(containerLinearLayout.getChildAt(i)));

                imageView.setDrawingCacheEnabled(true);
                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                imageArrayList.add(1,bytes);
                allImageArrayList.add(imageArrayList);
//
//                EditText editText = (EditText) containerLinearLayout.getChildAt(0);
//                for(int i1 = 0; i<200; i++) {
//
//                    editText.append(bytes[i] + "");
//                }

            }

            else if(containerLinearLayout.getChildAt(i).getId() == R.id.tableConstraintLinearLayoutId){
//                Toast.makeText(getApplicationContext(), "it is table", Toast.LENGTH_SHORT).show();

                LinearLayout tableLinearLayout = containerLinearLayout.getChildAt(i).findViewById(R.id.tableLinearLayoutId);
                int numberOfRows = tableLinearLayout.getChildCount();

                LinearLayout tableRowHorizontalLinearLayout = tableLinearLayout.getChildAt(0).findViewById(R.id.tableRowLinearLayoutId);
                int numberOfColumns = tableRowHorizontalLinearLayout.getChildCount();

                ArrayList<String> pageTableTextDataTypeDetailsArrayList = new ArrayList<>();

                pageTableTextDataTypeDetailsArrayList.add(0,GlobalConfig.TABLE_TYPE);
                pageTableTextDataTypeDetailsArrayList.add(1,containerLinearLayout.indexOfChild(containerLinearLayout.getChildAt(i)) +"");
                pageTableTextDataTypeDetailsArrayList.add(2,numberOfRows+"");
                pageTableTextDataTypeDetailsArrayList.add(3,numberOfColumns+"");

                StringBuilder tableItems = new StringBuilder();

                for(int i1 = 0; i1<numberOfRows; i1++){
                    LinearLayout tableRowHorizontalLinearLayout2 = tableLinearLayout.getChildAt(i1).findViewById(R.id.tableRowLinearLayoutId);
                    int numberOfColumns2 = tableRowHorizontalLinearLayout2.getChildCount();
                    for(int i2 = 0; i2<numberOfColumns2; i2++){
                        EditText cell = tableRowHorizontalLinearLayout2.getChildAt(i2).findViewById(R.id.editTextCellId);
                        String text = cell.getText()+"";
                        if(i2 != numberOfColumns2-1) {
                            tableItems.append(text).append(",");
                        }else{
                            tableItems.append(text);

                        }
//                        Toast.makeText(getApplicationContext(), cell.getText(), Toast.LENGTH_SHORT).show();
                    }
                    if(i1 != numberOfRows-1) {
                        tableItems.append("_");
                    }


                }
                pageTableTextDataTypeDetailsArrayList.add(4,tableItems+"");
                allPageTextDataDetailsArrayList.add(pageTableTextDataTypeDetailsArrayList);

//                for(int r =0; r<pageTableTextDataTypeDetailsArrayList.size(); r++){
                    EditText editText1 =  (EditText) containerLinearLayout.getChildAt(0);
//                    editText1.append(pageTableTextDataTypeDetailsArrayList.get(0)+"-"+pageTableTextDataTypeDetailsArrayList.get(1)+"-"+pageTableTextDataTypeDetailsArrayList.get(3)+"_"+pageTableTextDataTypeDetailsArrayList.get(4)+"_"+containerLinearLayout.indexOfChild(containerLinearLayout.getChildAt(i))+"_");
//                }



            }

            else if(containerLinearLayout.getChildAt(i).getId() == R.id.todoGroupLinearLayoutId){
                Toast.makeText(getApplicationContext(), "it is todo list", Toast.LENGTH_SHORT).show();
                LinearLayout todoItemLinearLayout = containerLinearLayout.getChildAt(i).findViewById(R.id.todoItemLinearLayoutId);
                int numberOfItems = todoItemLinearLayout.getChildCount();

                ArrayList<String> pageTodoTextDataTypeDetailsArrayList = new ArrayList<>();

                pageTodoTextDataTypeDetailsArrayList.add(0,GlobalConfig.TODO_TYPE);
                pageTodoTextDataTypeDetailsArrayList.add(1,""+containerLinearLayout.indexOfChild(containerLinearLayout.getChildAt(i)));
                pageTodoTextDataTypeDetailsArrayList.add(2,numberOfItems+"");
                StringBuilder todoItems = new StringBuilder();

                for(int i1 = 0; i1<numberOfItems; i1++){
                    EditText todoEditText = todoItemLinearLayout.getChildAt(i1).findViewById(R.id.todoEditTextId);
                    String item = todoEditText.getText().toString();
//                    Toast.makeText(getApplicationContext(), todoEditText.getText(), Toast.LENGTH_SHORT).show();

                    if(i1  != numberOfItems-1){
                        todoItems.append(item).append(",");
                    }else{
                        todoItems.append(item);
                    }

                }
                pageTodoTextDataTypeDetailsArrayList.add(3,todoItems+"");
                allPageTextDataDetailsArrayList.add(pageTodoTextDataTypeDetailsArrayList);

//                for(int r =0; r<pageTodoTextDataTypeDetailsArrayList.size(); r++){
                    EditText editText1 =  (EditText) containerLinearLayout.getChildAt(0);
//                    editText1.append(pageTodoTextDataTypeDetailsArrayList.get(0)+"-"+pageTodoTextDataTypeDetailsArrayList.get(1)+"-"+pageTodoTextDataTypeDetailsArrayList.get(2)+"_"+pageTodoTextDataTypeDetailsArrayList.get(3)+"_"+containerLinearLayout.indexOfChild(containerLinearLayout.getChildAt(i))+"_");
//                }
            }

        }

    UploadPageManagerService.uploadImageDataToPage( libraryId,  tutorialId,  folderId,  pageId, pageTitle,allImageArrayList, totalNumberOfImages,numOfChildrenData,isTutorialPage);
    UploadPageManagerService.uploadTextDataToPage( libraryId,  tutorialId,  folderId,  pageId, pageTitle, allPageTextDataDetailsArrayList,  numOfChildrenData,isTutorialPage);

    }
    @Deprecated
    private void recordTextStyles(){



    }

    @Deprecated
    void preparePage(){


    //now save the html and title to the database here .
    //pageContent;

    Toast.makeText(getApplicationContext(), containerLinearLayout.getChildCount()+"", Toast.LENGTH_SHORT).show();

    for(int i=0; i<containerLinearLayout.getChildCount(); i++){

            if(containerLinearLayout.getChildAt(i) instanceof  EditText){
                //A plain text
//                Toast.makeText(getApplicationContext(), "it is edittext", Toast.LENGTH_SHORT).show();
                EditText editText = (EditText) containerLinearLayout.getChildAt(i);
                if(editText.getText().toString().isEmpty()){
//                    Toast.makeText(getApplicationContext(), editText.getText()+" is removed", Toast.LENGTH_SHORT).show();
                    containerLinearLayout.removeView(editText);

                }
            }
            /*
            else if(containerLinearLayout.getChildAt(i).getId() == R.id.imageConstraintLayoutId){
                Toast.makeText(getApplicationContext(), "it is image", Toast.LENGTH_SHORT).show();

                ImageView imageView = containerLinearLayout.getChildAt(i).findViewById(R.id.imageViewId);

            }

            else if(containerLinearLayout.getChildAt(i).getId() == R.id.tableConstraintLinearLayoutId){
                Toast.makeText(getApplicationContext(), "it is table", Toast.LENGTH_SHORT).show();

                LinearLayout tableLinearLayout = containerLinearLayout.getChildAt(i).findViewById(R.id.tableLinearLayoutId);
                int numberOfRows = tableLinearLayout.getChildCount();
                boolean isAllTableEmpty = true;
                for(int i1 = 0; i1<numberOfRows; i1++){
                    LinearLayout tableRowHorizontalLinearLayout = tableLinearLayout.getChildAt(i1).findViewById(R.id.tableRowLinearLayoutId);
                    int numberOfColumns = tableRowHorizontalLinearLayout.getChildCount();
                    boolean isAllRowEmpty = true;
                    for(int i2 = 0; i2<numberOfColumns; i2++){
                        EditText cell = tableRowHorizontalLinearLayout.getChildAt(i2).findViewById(R.id.editTextCellId);
                        Toast.makeText(getApplicationContext(), cell.getText(), Toast.LENGTH_SHORT).show();
                        if (!cell.getText().toString().isEmpty()){
                            isAllTableEmpty=false;
                            isAllRowEmpty = false;
                        }
                        if(isAllRowEmpty){
                            //remove this row if they are all empty (if there is need to do that)
//                            tableLinearLayout.removeView(tableRowHorizontalLinearLayout);
                        }
                    }

                }
                if(isAllTableEmpty){
                    //remove this table if they are all empty (if there is need to do that)

//                    containerLinearLayout.removeView(tableLinearLayout);
                }



            }

            else if(containerLinearLayout.getChildAt(i).getId() == R.id.todoGroupLinearLayoutId){
                Toast.makeText(getApplicationContext(), "it is todo list", Toast.LENGTH_SHORT).show();
                LinearLayout todoItemLinearLayout = containerLinearLayout.getChildAt(i).findViewById(R.id.todoItemLinearLayoutId);
                int numberOfItems = todoItemLinearLayout.getChildCount();
                for(int i1 = 0; i1<numberOfItems; i1++){
                    EditText todoEditText = todoItemLinearLayout.getChildAt(i1).findViewById(R.id.todoEditTextId);
                    Toast.makeText(getApplicationContext(), todoEditText.getText(), Toast.LENGTH_SHORT).show();

                }
            }
*/
        }
}

    private interface ImageProcessingListener{
        void onComplete();
        void onStart();

    }

}
