package com.palria.learnera;


import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class UploadPageActivity extends AppCompatActivity {


    String pageId;
    String libraryId;
    String bookId;
    LinearLayout containerLinearLayout;
    Button btn ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page);
        initUI();
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

}
    void uploadPage(){
        ArrayList<ArrayList<String>> allPageTextPartitionsDataDetailsArrayList = new ArrayList<>();

        for(int i=0; i<containerLinearLayout.getChildCount(); i++){
            String partitionId = GlobalConfig.getRandomString(10) + GlobalConfig._IS_PARTITION_ID_IS_FOR_IDENTIFYING_PARTITIONS_KEY;
            UploadPageManagerService.setInitialVariables(pageId);
            UploadPageManagerService.addUploadListeners(new UploadPageManagerService.OnPageUploadListener() {
                @Override
                public void onNewPage(String pageId) {

                }

                @Override
                public void onFailed(String pageId) {

                }

                @Override
                public void onProgress(String pageId, int progressCount) {

                }

                @Override
                public void onSuccess(String pageId) {

                }
            });
            LinearLayout partitionLinearLayout = (LinearLayout) containerLinearLayout.getChildAt(i);
            EditText textDataPartitionTextView =(EditText) partitionLinearLayout.getChildAt(0);
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
                    ImageView imagePartitionImageView = (ImageView) imageLinearLayout.getChildAt(j);
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
