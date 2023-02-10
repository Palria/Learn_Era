package com.palria.learnera;


import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class UploadPageActivity extends AppCompatActivity {


    String pageId;
    String libraryId;
    String bookId;
    LinearLayout containerLinearLayout;
    Button btn ;

    /**
     * A  variable for launching the gallery {@link Intent}
     * */
    ActivityResultLauncher<Intent> openGalleryLauncher;

    /**
     * A  variable for launching the camera {@link Intent}
     * */
    ActivityResultLauncher<Intent> openCameraLauncher;

    int CAMERA_PERMISSION_REQUEST_CODE = 2002;
    int GALLERY_PERMISSION_REQUEST_CODE = 23;
    int imageDisplayPosition = 0;
    ImageView receiverImage;
    LinearLayout receiverLinearLayout;
    View currentFocusView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page);

        initUI();

        openGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null) {
                    Intent data=result.getData();
////                    galleryImageUri = data.getData();
////                        Picasso.get().load(galleryImageUri)
////                                .centerCrop()
////                                .into(pickImageActionButton);
//                    ImageView partitionImage = new ImageView(getApplicationContext());
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(70,70);
//                    partitionImage.setLayoutParams(layoutParams);
//
                   ImageView image = getImage(receiverLinearLayout);
                    Glide.with(UploadPageActivity.this)
                            .load(data.getData())
                            .centerCrop()
                            .into(image);
                    if(containerLinearLayout.getChildCount() == (containerLinearLayout.indexOfChild(currentFocusView)+1)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                createPartition();

                            }
                        });
                    }

//                    isLibraryCoverPhotoIncluded = true;
//                    isLibraryCoverPhotoChanged = true;


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
//                            cameraImageBitmap = bitmapFromCamera;
                            //coverPhotoImageView.setImageBitmap(cameraImageBitmap);

//                            ImageView partitionImage = new ImageView(getApplicationContext());
//                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(70,70);
//                            partitionImage.setLayoutParams(layoutParams);
//                            receiverLinearLayout.addView(partitionImage);

                            ImageView image = getImage(receiverLinearLayout);

                            Glide.with(UploadPageActivity.this)
                                    .load(bitmapFromCamera)
                                    .centerCrop()
                                    .into(image);
                            if(containerLinearLayout.getChildCount() == (containerLinearLayout.indexOfChild(receiverLinearLayout)+1)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        createPartition();

                                    }
                                });
                            }
//                            isLibraryCoverPhotoIncluded = true;
//                            isLibraryCoverPhotoChanged = true;
                        }

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No image captured!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bookId  = "TEST_ID-2";
        pageId  = "TEST_ID-2";

        startService(new Intent(getApplicationContext(),UploadPageManagerService.class));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPage();
            }
        });

    }
private void initUI(){

    containerLinearLayout = findViewById(R.id.containerLinearLayoutId);
    btn = findViewById(R.id.btn);
createPartition();
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
            }
        });
        containerLinearLayout.addView(partitionView);
    }

    private ImageView getImage(ViewGroup viewGroup){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View imageView  =  layoutInflater.inflate(R.layout.page_image_layout,viewGroup,false);
        ImageView image = imageView.findViewById(R.id.imageViewId);
        ImageView removeImage = imageView.findViewById(R.id.removeImageId);

removeImage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        viewGroup.removeView(imageView);
    }
});
viewGroup.addView(imageView);
return image;
    }

    void uploadPage(){
        ArrayList<ArrayList<String>> allPageTextPartitionsDataDetailsArrayList = new ArrayList<>();

        UploadPageManagerService.addUploadListeners(new UploadPageManagerService.OnPageUploadListener() {
            @Override
            public void onNewPage(String pageId) {
                Toast.makeText(getApplicationContext(), "New page id: "+ pageId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String pageId) {
                Toast.makeText(getApplicationContext(), "page upload failed: "+ pageId, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onProgress(String pageId, int progressCount) {
                Toast.makeText(getApplicationContext(), "New page uploading: "+ pageId, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(String pageId) {
                Toast.makeText(getApplicationContext(), "New page upload succeeded: "+ pageId, Toast.LENGTH_SHORT).show();

            }
        });
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
            UploadPageManagerService.uploadPartitionImageDataToPage(libraryId,bookId,pageId,partitionId,imagePartitionByteArrayList,"IMAGE_PARTITION_ARRAY_"+i,containerLinearLayout.getChildCount());

        }
        //call the service method here for uploading text data partition at once
        UploadPageManagerService.uploadPartitionTextDataToPage(libraryId,bookId,pageId,allPageTextPartitionsDataDetailsArrayList,containerLinearLayout.getChildCount());

    }



}
