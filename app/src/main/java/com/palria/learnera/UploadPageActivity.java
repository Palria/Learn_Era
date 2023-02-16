package com.palria.learnera;


import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class UploadPageActivity extends AppCompatActivity {


    String pageId;
    String libraryId;
    String tutorialId;
    String folderId;
    LinearLayout containerLinearLayout;
    ImageButton addImageActionButton ;
    ImageButton addTodoListActionButton ;
    ImageButton addTableActionButton ;
    Button btn ;
    EditText pageTitleEditText;
    String pageTitle;
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
    int validPosition = 1;
    ImageView receiverImage;
    LinearLayout receiverLinearLayout;
    View currentFocusView;
    /**
     * loading alert dialog
     *
     */
    AlertDialog alertDialog;

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
                   ImageView image = getImage();
                    Glide.with(UploadPageActivity.this)
                            .load(data.getData())
                            .centerCrop()
                            .into(image);
                    if(containerLinearLayout.getChildCount() == (containerLinearLayout.indexOfChild(currentFocusView)+1)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                             //   createPartition();

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

                            ImageView image = getImage();

                            Glide.with(UploadPageActivity.this)
                                    .load(bitmapFromCamera)
                                    .centerCrop()
                                    .into(image);
                            if(containerLinearLayout.getChildCount() == (containerLinearLayout.indexOfChild(receiverLinearLayout)+1)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                      //  createPartition();

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

        tutorialId  = "TEST_ID-3";
        folderId  = "TEST_ID-3";
        pageId  = "TEST_ID-3";

        startService(new Intent(getApplicationContext(),UploadPageManagerService.class));
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
//                uploadPage();
                pageTitle = pageTitleEditText.getText().toString();
                postPage();
            }
        });

    }
private void initUI(){

    pageTitleEditText = findViewById(R.id.pageTitleEditTextId);
    containerLinearLayout = findViewById(R.id.containerLinearLayoutId);
    addImageActionButton = findViewById(R.id.addImageActionButtonId);
    addTodoListActionButton = findViewById(R.id.addTodoListActionButtonId);
    addTableActionButton = findViewById(R.id.addTableActionButtonId);
    btn = findViewById(R.id.btn);
    addEditText(0);
    //createPartition();

    //init progress.
    alertDialog = new AlertDialog.Builder(UploadPageActivity.this)
            .setCancelable(false)
            .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
            .create();
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

    private void toggleProgress(boolean show)
    {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
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
                addTodoGroup();
            }
        });
        containerLinearLayout.addView(partitionView);
    }

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
            UploadPageManagerService.uploadPartitionImageDataToPage(libraryId,tutorialId,pageId,partitionId,imagePartitionByteArrayList,"IMAGE_PARTITION_ARRAY_"+i,containerLinearLayout.getChildCount());

        }
        //call the service method here for uploading text data partition at once
        UploadPageManagerService.uploadPartitionTextDataToPage(libraryId,tutorialId,pageId,allPageTextPartitionsDataDetailsArrayList,containerLinearLayout.getChildCount());

    }

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

void createTableColumn(LinearLayout tableLinearLayout){
        if(tableLinearLayout.getChildCount()!=0){
            for(int i=0; i<tableLinearLayout.getChildCount(); i++){
                LinearLayout rowContainerLinearLayout = (LinearLayout) tableLinearLayout.getChildAt(i);
                LinearLayout rowLinearLayout = (LinearLayout) rowContainerLinearLayout.getChildAt(0);
                addTableEditTextCell(rowLinearLayout);
            }
        }
}

void addTableEditTextCell(LinearLayout rowLinearLayout){
    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View tableCell  =  layoutInflater.inflate(R.layout.page_table_cell_edit_text,rowLinearLayout,false);
    EditText editTextCell =tableCell.findViewById(R.id.editTextCellId);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,40,1);
    layoutParams.setMargins(5,5,5,5);
//    editTextCell.setLayoutParams(layoutParams);
    editTextCell.setHint("?");
    editTextCell.requestFocus();
//    editTextCell.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
    ColorDrawable gray = new ColorDrawable();
    gray.setColor(getResources().getColor(R.color.gray));
//    editTextCell.setBackground(gray);

    rowLinearLayout.addView(tableCell);

}

void postPage(){
        toggleProgress(true);
preparePage();

    UploadPageManagerService.addUploadListeners(new UploadPageManagerService.OnPageUploadListener() {
        @Override
        public void onNewPage(String pageId) {
            Toast.makeText(getApplicationContext(), "New page id: "+ pageId, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed(String pageId) {
            Toast.makeText(getApplicationContext(), "page upload failed: "+ pageId, Toast.LENGTH_SHORT).show();
            toggleProgress(false);

        }

        @Override
        public void onProgress(String pageId, int progressCount) {
            Toast.makeText(getApplicationContext(), "New page uploading: "+ pageId, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onSuccess(String pageId) {
            Toast.makeText(getApplicationContext(), "New page upload succeeded: "+ pageId, Toast.LENGTH_SHORT).show();
            toggleProgress(false);
            GlobalHelpers.showAlertMessage("success",
                    UploadPageActivity.this,
                    "Page created successfully",
                    "You have successfully created your page, go ahead and contribute to Learn Era ");


        }
    });
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

    UploadPageManagerService.uploadImageDataToPage( libraryId,  tutorialId,  folderId,  pageId, pageTitle,allImageArrayList, totalNumberOfImages,numOfChildrenData);
    UploadPageManagerService.uploadTextDataToPage( libraryId,  tutorialId,  folderId,  pageId, pageTitle, allPageTextDataDetailsArrayList,  numOfChildrenData);

    }

void preparePage(){
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
}
