package com.palria.learnera;


import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class UploadPageActivity extends AppCompatActivity {




    String pageId;
    String libraryId;
    String bookId;

    void uploadPage(LinearLayout containerLinearLayout){
        ArrayList<ArrayList<String>> allPageTextPartitionsDataDetailsArrayList = new ArrayList<>();

        for(int i=0; i<containerLinearLayout.getChildCount(); i++){
            String partitionId = GlobalConfig.getRandomString(10) + "_IS_PARTITION_ID_IS_PARTITION_ID_";
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
