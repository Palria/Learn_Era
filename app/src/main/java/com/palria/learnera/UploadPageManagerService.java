
package com.palria.learnera;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class UploadPageManagerService extends Service {
    public UploadPageManagerService() {
    }
    static FirebaseFirestore firebaseFirestore;
    static String currentUserId = "0";
    static FirebaseStorage firebaseStorage;
    static OnPageUploadListener onPageUploadListener;
    static  ArrayList<String>allActivePagesIdArrayList = new ArrayList<>();

    static HashMap<String, Integer>totalNumberOfTimesImageFailed = new HashMap<>();
    static HashMap<String, Integer>totalNumberOfPartitionsWithImageHashMap = new HashMap<>();
    static HashMap<String, Integer>pageUploadProgressCounterHashMap = new HashMap<>();
    static HashMap<String, Integer>totalNumberOfPagePartitionsHashMap = new HashMap<>();
    static HashMap<String, Integer>totalNumberOfPagePartitionUploadSuccessCounterHashMap = new HashMap<>();
    static HashMap<String, Boolean>isTextIncludedHashMap = new HashMap<>();
    static HashMap<String, Boolean>isPageTextPartitionsUploaded = new HashMap<>();
    static HashMap<String, Boolean>isImageIncludedHashMap = new HashMap<>();
    static HashMap<String, Boolean>isPageImagesPartitionsUploaded = new HashMap<>();
    static HashMap<String, Boolean>isPageUploadedHashMap = new HashMap<>();
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }


        return Service.START_STICKY;
    }


    static void addUploadListeners(OnPageUploadListener onPageUploadListener){
        UploadPageManagerService.onPageUploadListener = onPageUploadListener;
    }

    static void setInitialVariables(String pageId){
        onPageUploadListener.onNewPage(pageId);
        if(!allActivePagesIdArrayList.contains(pageId)){
            allActivePagesIdArrayList.add(pageId);
        }
        isTextIncludedHashMap.put(pageId,false);
        isPageTextPartitionsUploaded.put(pageId,false);
        isImageIncludedHashMap.put(pageId,false);
        isPageImagesPartitionsUploaded.put(pageId,false);
        isPageUploadedHashMap.put(pageId,false);
        totalNumberOfPagePartitionsHashMap.put(pageId,0);
        totalNumberOfTimesImageFailed.put(pageId,0);
        pageUploadProgressCounterHashMap.put(pageId,0);
        totalNumberOfPagePartitionUploadSuccessCounterHashMap.put(pageId,0);
        totalNumberOfPartitionsWithImageHashMap.put(pageId,0);


    }

    static  void uploadPartitionTextDataToPage(String libraryId, String bookId, String pageId, ArrayList<ArrayList<String>> allPageTextPartitionsDataDetailsArrayList, int totalNumberOfPartitions) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (!allPageTextPartitionsDataDetailsArrayList.isEmpty()) {
                isTextIncludedHashMap.put(pageId,true);
                HashMap<String, Object> pageTextPartitionsDataDetailsHashMap = new HashMap<>();
                pageTextPartitionsDataDetailsHashMap.put("TOTAL_NUMBER_OF_PARTITIONS", totalNumberOfPartitions);
                for (int i = 0; i < allPageTextPartitionsDataDetailsArrayList.size(); i++) {
                    pageTextPartitionsDataDetailsHashMap.put("TEXT_PARTITION_ARRAY_" + i, allPageTextPartitionsDataDetailsArrayList.get(i));
                }

                firebaseFirestore.collection("ALL_USERS").document(currentUserId).collection("ALL_LIBRARY").document(libraryId).collection("ALL_BOOKS").document(bookId).collection("ALL_PAGES").document(pageId).set(pageTextPartitionsDataDetailsHashMap, SetOptions.merge()).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        uploadPartitionTextDataToPage( libraryId,  bookId,  pageId,  allPageTextPartitionsDataDetailsArrayList,  totalNumberOfPartitions);

                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        isPageTextPartitionsUploaded.put(pageId,true);
                        if(!isImageIncludedHashMap.get(pageId) || isPageImagesPartitionsUploaded.get(pageId)){

                            //succeeded
                            allActivePagesIdArrayList.remove(pageId);
                            isPageUploadedHashMap.put(pageId,true);
                            onPageUploadListener.onSuccess(pageId);
                        }
                    }
                });
            }


        }
    }

    static  void uploadPartitionImageDataToPage(String libraryId, String bookId, String pageId,String partitionId, ArrayList<byte[]> imageUploadByteArrayPartitionArrayList, String imagePartitionArrayName, int totalNumberOfPartitions) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (!imageUploadByteArrayPartitionArrayList.isEmpty()) {
                totalNumberOfPartitionsWithImageHashMap.put(pageId,totalNumberOfPartitionsWithImageHashMap.get(pageId)+1);
                int[] imagePartitionSuccessCounter = new int[1];
                isImageIncludedHashMap.put(pageId,true);

                int totalNumberOfPartitionsWithImage = totalNumberOfPartitionsWithImageHashMap.get(pageId);
                HashMap<String, Object> pageImagePartitionsDataDetails = new HashMap<>();
                pageImagePartitionsDataDetails.put("TOTAL_NUMBER_OF_PARTITIONS", totalNumberOfPartitions);
                pageImagePartitionsDataDetails.put(imagePartitionArrayName, FieldValue.arrayUnion(partitionId));

                for (int i = 0; i < imageUploadByteArrayPartitionArrayList.size(); i++) {
                    uploadPartitionImageDataToPage( libraryId,  bookId,  pageId, partitionId,  imageUploadByteArrayPartitionArrayList.get(i),  imagePartitionArrayName,  totalNumberOfPartitions,totalNumberOfPartitionsWithImage,pageImagePartitionsDataDetails);

                }


            }
        }
    }
    static  void uploadPartitionImageDataToPage(String libraryId, String bookId, String pageId,String partitionId, byte[] imageUploadByteArrayPartition, String imagePartitionArrayName, final int totalNumberOfPartitions, final int totalNumberOfPartitionsWithImage,final HashMap<String, Object> pageImagePartitionsDataDetails) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            int[] imagePartitionSuccessCounter = new int[1];
            isImageIncludedHashMap.put(pageId,true);

            pageImagePartitionsDataDetails.put("TOTAL_NUMBER_OF_PARTITIONS", totalNumberOfPartitions);
            String imageId = GlobalConfig.getRandomString(10);
            StorageReference storageReference = firebaseStorage.getReference().child("ALL_USERS/" + currentUserId + "/ALL_LIBRARY/" + libraryId + "/ALL_BOOKS/" + bookId + "/ALL_PAGES/" + pageId + "/ALL_IMAGES/" + imageId + ".PNG");
            UploadTask uploadTask = storageReference.putBytes(imageUploadByteArrayPartition);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    long totalBytes = snapshot.getTotalByteCount();
                    long totalBytesTransferred = snapshot.getBytesTransferred();
                    int progressCount = (int) ( (totalBytesTransferred /totalBytes) * 100 );
                    pageUploadProgressCounterHashMap.put(pageId,progressCount);
                    onPageUploadListener.onProgress(pageId,progressCount);

                }
            })
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {


                                        return storageReference.getDownloadUrl();
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
                                            pageImagePartitionsDataDetails.put(imagePartitionArrayName, FieldValue.arrayUnion(imageDownloadUrl));
                                            pageImagePartitionsDataDetails.put(imagePartitionArrayName + "_STORAGE_REFERENCE", FieldValue.arrayUnion(storageReference.getPath()));
                                            firebaseFirestore.collection("ALL_USERS").document(currentUserId).collection("ALL_LIBRARY").document(libraryId).collection("ALL_BOOKS").document(bookId).collection("ALL_PAGES").document(pageId).set(pageImagePartitionsDataDetails, SetOptions.merge()).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    //Succeeded in uploading image of a book page.
                                                    imagePartitionSuccessCounter[0]++;

                                                    if(imagePartitionSuccessCounter[0] == totalNumberOfPartitionsWithImage){
                                                        //all images succeeded
                                                        isPageImagesPartitionsUploaded.put(pageId,true);
                                                        if(!isTextIncludedHashMap.get(pageId) || isPageTextPartitionsUploaded.get(pageId)){

                                                            //succeeded
                                                            allActivePagesIdArrayList.remove(pageId);
                                                            isPageUploadedHashMap.put(pageId,true);
                                                            onPageUploadListener.onSuccess(pageId);
                                                        }
                                                    }
                                                }
                                            });

                                        }
                                    }
                                });
                            }
                            else {
                                //failed to upload the image

                                if(totalNumberOfTimesImageFailed.containsKey(imageId)) {
                                    if(totalNumberOfTimesImageFailed.get(imageId) <=5){
                                        uploadPartitionImageDataToPage( libraryId,  bookId,  pageId, partitionId,  imageUploadByteArrayPartition,  imagePartitionArrayName,  totalNumberOfPartitions,totalNumberOfPartitionsWithImage,pageImagePartitionsDataDetails);
                                    }
                                    totalNumberOfTimesImageFailed.put(imageId, totalNumberOfTimesImageFailed.get(imageId) + 1);
                                }else{
                                    totalNumberOfTimesImageFailed.put(imageId,1);
                                }
                            }
                        }
                    });

        }




    }

    static int getTotalNumberOfPagePartitions(String pageId){
        return  totalNumberOfPagePartitionsHashMap.get(pageId);
    }

    static int getTotalNumberOfPagePartitionsUploadedSuccessCounter(String pageId){
        return  totalNumberOfPagePartitionUploadSuccessCounterHashMap.get(pageId);
    }

    static int getPageUploadedProgress(String pageId){
        return  pageUploadProgressCounterHashMap.get(pageId);
    }
    static boolean isPageUploaded(String pageId){
        return  isPageUploadedHashMap.get(pageId);
    }

    interface OnPageUploadListener{
        void onNewPage(String pageId);
        void onFailed(String pageId);
        void onProgress(String pageId, int progressCount);
        void onSuccess(String pageId);


    }


}