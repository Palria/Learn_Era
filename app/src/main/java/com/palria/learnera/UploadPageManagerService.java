
package com.palria.learnera;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class UploadPageManagerService extends Service implements OnPageUploadListener {
    public UploadPageManagerService() {
    }
    static FirebaseFirestore firebaseFirestore;
    static String currentUserId = "0";
    static FirebaseStorage firebaseStorage;
//    static OnPageUploadListener onPageUploadListener;
    static  ArrayList<String>allActivePagesIdArrayList = new ArrayList<>();

    static HashMap<String, Integer> totalNumberOfTimesImageFailed = new HashMap<>();
    static HashMap<String, Integer> totalNumberOfPartitionsWithImageHashMap = new HashMap<>();
    static HashMap<String, Integer> pageUploadProgressCounterHashMap = new HashMap<>();
    static HashMap<String, Integer> totalNumberOfPagePartitionsHashMap = new HashMap<>();
    static HashMap<String, Integer> totalNumberOfPagePartitionUploadSuccessCounterHashMap = new HashMap<>();
    static HashMap<String, Boolean> isTextIncludedHashMap = new HashMap<>();
    static HashMap<String, Boolean> isPageTextPartitionsUploaded = new HashMap<>();
    static HashMap<String, Boolean> isImageIncludedHashMap = new HashMap<>();
    static HashMap<String, Boolean> isPageImagesPartitionsUploaded = new HashMap<>();
    static HashMap<String, Boolean> isPageUploadedHashMap = new HashMap<>();
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


        String pageId = intent.getStringExtra(GlobalConfig.PAGE_ID_KEY);
        String libraryId = intent.getStringExtra(GlobalConfig.LIBRARY_ID_KEY);
        String tutorialId = intent.getStringExtra(GlobalConfig.TUTORIAL_ID_KEY);
        String folderId = intent.getStringExtra(GlobalConfig.FOLDER_ID_KEY);
        String pageTitle = intent.getStringExtra(GlobalConfig.PAGE_TITLE_KEY);
        String pageContent = intent.getStringExtra(GlobalConfig.PAGE_CONTENT_KEY);
        String coverPhotoDownloadUrl = intent.getStringExtra(GlobalConfig.PAGE_COVER_PHOTO_DOWNLOAD_URL_KEY);
        ArrayList<String> imageListToUpload = (ArrayList<String>) intent.getSerializableExtra(GlobalConfig.PAGE_MEDIA_URL_LIST_KEY);
        ArrayList<String>retrievedActivePageMediaUrlArrayList = (ArrayList<String>) intent.getSerializableExtra(GlobalConfig.ACTIVE_PAGE_MEDIA_URL_LIST_KEY);
        boolean isTutorialPage = intent.getBooleanExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,true);
        boolean isCreateNewPage = intent.getBooleanExtra(GlobalConfig.IS_CREATE_NEW_PAGE_KEY,true);
        boolean isPageCoverPhotoChanged = intent.getBooleanExtra(GlobalConfig.IS_PAGE_COVER_PHOTO_CHANGED_KEY,true);
        UploadPageManagerService.this.onNewPage( pageId,  folderId,  tutorialId,  libraryId,  isTutorialPage, isCreateNewPage, coverPhotoDownloadUrl,isPageCoverPhotoChanged,  pageTitle,retrievedActivePageMediaUrlArrayList,  pageContent,imageListToUpload);


        return Service.START_STICKY;
    }


    static void addUploadListeners(OnPageUploadListener onPageUploadListener){
//        UploadPageManagerService.onPageUploadListener = onPageUploadListener;
    }

    static void setInitialVariables(String pageId){
//        onPageUploadListener.onNewPage(pageId);
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

//                firebaseFirestore.collection("ALL_USERS").document(currentUserId).collection("ALL_LIBRARY").document(libraryId).collection("ALL_BOOKS").document(bookId).collection("ALL_PAGES").document(pageId).set(pageTextPartitionsDataDetailsHashMap, SetOptions.merge()).addOnFailureListener(new OnFailureListener() {
                firebaseFirestore.collection(GlobalConfig.ALL_TUTORIAL_KEY).document(bookId).collection(GlobalConfig.ALL_TUTORIAL_PAGES_KEY).document(pageId).set(pageTextPartitionsDataDetailsHashMap, SetOptions.merge()).addOnFailureListener(new OnFailureListener() {
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
//                            onPageUploadListener.onSuccess(pageId);
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
//                    onPageUploadListener.onProgress(pageId,progressCount);

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
                                            firebaseFirestore.collection(GlobalConfig.ALL_TUTORIAL_KEY).document(bookId).collection(GlobalConfig.ALL_TUTORIAL_PAGES_KEY).document(pageId).set(pageImagePartitionsDataDetails, SetOptions.merge()).addOnFailureListener(new OnFailureListener() {
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
//                                                            onPageUploadListener.onSuccess(pageId);
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


    static  void uploadTextDataToPage(String libraryId, String tutorialId, String folderId, String pageId, String pageTitle, ArrayList<ArrayList<String>> allPageTextDataDetailsArrayList, int totalNumberOfChildren, boolean isTutorialPage) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DocumentReference pageDocumentReference = null;
            if(isTutorialPage){
                pageDocumentReference = firebaseFirestore.collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_TUTORIAL_PAGES_KEY).document(pageId);
            }else{
                pageDocumentReference = firebaseFirestore.collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_FOLDERS_KEY).document(folderId).collection(GlobalConfig.ALL_FOLDER_PAGES_KEY).document(pageId);

            }

            HashMap<String, Object> pageTextPartitionsDataDetailsHashMap = new HashMap<>();
            pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.PAGE_TITLE_KEY, pageTitle);
            pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.LIBRARY_ID_KEY, libraryId);
            pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.TUTORIAL_ID_KEY, tutorialId);
            pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.FOLDER_ID_KEY, folderId);
            pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.PAGE_ID_KEY, pageId);
            pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.AUTHOR_ID_KEY, currentUserId);
            pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.TOTAL_NUMBER_OF_PAGE_DATA_KEY, totalNumberOfChildren);
            pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.DATE_TIME_STAMP_PAGE_CREATED_KEY, FieldValue.serverTimestamp());

//            if (!allPageTextDataDetailsArrayList.isEmpty()) {
                isTextIncludedHashMap.put(pageId,true);
                  for (int i = 0; i < allPageTextDataDetailsArrayList.size(); i++) {
                    ArrayList<String> pageTextDataTypeDetailsArrayList = allPageTextDataDetailsArrayList.get(i);
                    String position = pageTextDataTypeDetailsArrayList.get(1);
                    pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.DATA_ARRAY_KEY + position , pageTextDataTypeDetailsArrayList);
                }

//                firebaseFirestore.collection("ALL_USERS").document(currentUserId).collection("ALL_LIBRARY").document(libraryId).collection("ALL_BOOKS").document(bookId).collection("ALL_PAGES").document(pageId).set(pageTextPartitionsDataDetailsHashMap, SetOptions.merge()).addOnFailureListener(new OnFailureListener() {
                pageDocumentReference.set(pageTextPartitionsDataDetailsHashMap, SetOptions.merge()).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        uploadTextDataToPage(libraryId,  tutorialId,  folderId,  pageId,pageTitle,  allPageTextDataDetailsArrayList,  totalNumberOfChildren,isTutorialPage);

                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference documentReference = null;
                        if(isTutorialPage){
                            documentReference = firebaseFirestore.collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId);
                            GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_PAGE_TYPE_KEY, GlobalConfig.getCurrentUserId(), libraryId, tutorialId, folderId, pageId, null,  new GlobalConfig.ActionCallback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onFailed(String errorMessage) {

                                }
                            });

                        }else{
                            documentReference = firebaseFirestore.collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_FOLDERS_KEY).document(folderId);
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
                        documentReference.update(incrementPageNumberHashMap)
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                isPageTextPartitionsUploaded.put(pageId,false);
                                                if(!isImageIncludedHashMap.get(pageId) || isPageImagesPartitionsUploaded.get(pageId)){

                                                    //failed
//                                                    allActivePagesIdArrayList.remove(pageId);
                                                    isPageUploadedHashMap.put(pageId,false);
//                                                    onPageUploadListener.onFailed(pageId,e.getMessage());
                                                }
                                            }
                                        })
                                         .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        isPageTextPartitionsUploaded.put(pageId,true);
                                        if(!isImageIncludedHashMap.get(pageId) || isPageImagesPartitionsUploaded.get(pageId)){
                                            //succeeded
                                            allActivePagesIdArrayList.remove(pageId);
                                            isPageUploadedHashMap.put(pageId,true);
//                                            onPageUploadListener.onSuccess(pageId);
                                        }
                                    }
                                });


                    }
                });
//            }


        }
    }

    static  void uploadImageDataToPage(final String libraryId, final String tutorialId, final String folderId, final String pageId, final String pageTitle, final ArrayList<ArrayList<Object>> allImageArrayList,final int totalNumberOfImages,final int numOfChildrenData, final boolean isTutorialPage) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (!allImageArrayList.isEmpty()) {
                totalNumberOfPartitionsWithImageHashMap.put(pageId,totalNumberOfPartitionsWithImageHashMap.get(pageId)+1);
                int[] imagePartitionSuccessCounter = new int[1];
                isImageIncludedHashMap.put(pageId,true);

                int totalNumberOfPartitionsWithImage = totalNumberOfPartitionsWithImageHashMap.get(pageId);
//                HashMap<String, Object> pageImagePartitionsDataDetails = new HashMap<>();
//                pageImagePartitionsDataDetails.put("TOTAL_NUMBER_OF_PARTITIONS", totalNumberOfPartitions);
//                pageImagePartitionsDataDetails.put(imagePartitionArrayName, FieldValue.arrayUnion(partitionId));

                for (int i = 0; i < allImageArrayList.size(); i++) {

                    ArrayList<Object> imageDataList = allImageArrayList.get(i);
                    String position =""+ imageDataList.get(0);
                    byte[] imageByteArray =(byte[]) imageDataList.get(1);
                    uploadImageDataToPage( libraryId,  tutorialId,folderId,  pageId,  pageTitle, position,imageByteArray,  totalNumberOfImages,  numOfChildrenData,isTutorialPage);

                }


            }
        }
    }
    static  void uploadImageDataToPage(String libraryId, String tutorialId, String folderId, String pageId, String pageTitle,String position, byte[] imageUploadByteArray,final int totalNumberOfImages,final int numOfChildrenData, boolean isTutorialPage) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            int[] imagePartitionSuccessCounter = new int[1];
            isImageIncludedHashMap.put(pageId,true);
            HashMap<String, Object> pageImageDataDetails = new HashMap<>();
//
//            pageImageDataDetails.put(GlobalConfig.PAGE_TITLE_KEY, pageTitle);
//            pageImageDataDetails.put(GlobalConfig.TOTAL_NUMBER_OF_PAGE_DATA_KEY, numOfChildrenData);
            String imageId = GlobalConfig.getRandomString(15);
            StorageReference storageReference = null;
            if(isTutorialPage) {
                storageReference = firebaseStorage.getReference().child(GlobalConfig.ALL_USERS_KEY + "/" + currentUserId + "/" + GlobalConfig.ALL_LIBRARY_KEY + "/" + libraryId + "/" + GlobalConfig.ALL_TUTORIAL_KEY + "/" + tutorialId + "/" + GlobalConfig.ALL_TUTORIAL_PAGES_KEY + "/" + pageId + "/ALL_IMAGES/" + imageId + ".PNG");
            }else{
                storageReference = firebaseStorage.getReference().child(GlobalConfig.ALL_USERS_KEY + "/" + currentUserId + "/" + GlobalConfig.ALL_LIBRARY_KEY + "/" + libraryId + "/" + GlobalConfig.ALL_TUTORIAL_KEY + "/" + tutorialId + "/" + GlobalConfig.ALL_FOLDERS_KEY+"/"+folderId+"/"+GlobalConfig.ALL_FOLDER_PAGES_KEY + "/" + pageId + "/ALL_IMAGES/" + imageId + ".PNG");

            }
           final StorageReference finalStorageReference = storageReference;

            UploadTask uploadTask = storageReference.putBytes(imageUploadByteArray);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    long totalBytes = snapshot.getTotalByteCount();
                    long totalBytesTransferred = snapshot.getBytesTransferred();
                    int progressCount = (int) ( (totalBytesTransferred  * 100)/totalBytes) ;
                    pageUploadProgressCounterHashMap.put(pageId,progressCount);
//                    onPageUploadListener.onProgress(pageId,progressCount);


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
                                            ArrayList<String> dataImageList = new ArrayList<>();
                                            dataImageList.add(0,GlobalConfig.IMAGE_TYPE);
                                            dataImageList.add(1,position+"");
                                            dataImageList.add(2,imageDownloadUrl);
                                            dataImageList.add(3,finalStorageReference.getPath());
                                            pageImageDataDetails.put(GlobalConfig.DATA_ARRAY_KEY+position, dataImageList);

                                            DocumentReference documentReference = null;
                                            if(isTutorialPage) {
                                                documentReference = firebaseFirestore.collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_TUTORIAL_PAGES_KEY).document(pageId);
                                            }else{
                                                documentReference = firebaseFirestore.collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_FOLDERS_KEY).document(folderId).collection(GlobalConfig.ALL_FOLDER_PAGES_KEY).document(pageId);

                                            }

                                               documentReference.set(pageImageDataDetails, SetOptions.merge()).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
//                                                    onPageUploadListener.onFailed(pageId,e.getMessage());
                                                    }
                                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        //Succeeded in uploading image of a book page.
                                                        imagePartitionSuccessCounter[0]++;

                                                        if (imagePartitionSuccessCounter[0] == totalNumberOfImages) {
                                                            //all images succeeded
                                                            isPageImagesPartitionsUploaded.put(pageId, true);
                                                            if (!isTextIncludedHashMap.get(pageId) || isPageTextPartitionsUploaded.get(pageId)) {

                                                                //succeeded
                                                                allActivePagesIdArrayList.remove(pageId);
                                                                isPageUploadedHashMap.put(pageId, true);
//                                                                onPageUploadListener.onSuccess(pageId);
                                                            }
                                                        }
                                                    }
                                                });


                                        }
                                        else{
//                                            onPageUploadListener.onFailed(pageId,task.getException().getMessage());
                                        }
                                    }
                                });
                            }
                            else {
                                //failed to upload the image

                                if(totalNumberOfTimesImageFailed.containsKey(imageId)) {
                                    if(totalNumberOfTimesImageFailed.get(imageId) <=5){
                                        uploadImageDataToPage( libraryId,  tutorialId,folderId,  pageId,pageTitle, position,imageUploadByteArray,  totalNumberOfImages,  numOfChildrenData, isTutorialPage);
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

    static ArrayList<String> getAllActivePagesIdArrayList(){

        return allActivePagesIdArrayList;
    }









    /**new implementations begin*/
HashMap<String,Integer> numberOfMedia = new HashMap<>();
HashMap<String,Integer> numberOfMediaUploaded = new HashMap<>();
HashMap<String,Integer> numberOfMediaFailed = new HashMap<>();
HashMap<String,Integer> numberOfProgressingMedia = new HashMap<>();
HashMap<String,Long> totalBytesTransferred = new HashMap<>();
HashMap<String,Long> totalBytes = new HashMap<>();
HashMap<String,Integer> progressCount = new HashMap<>();
HashMap<String,RemoteViews> notificationLayout = new HashMap<>();
HashMap<String,NotificationCompat.Builder> builder = new HashMap<>();
HashMap<String,NotificationManagerCompat> notificationManager = new HashMap<>();
HashMap<String,Integer> notificationId = new HashMap<>();
HashMap<String,Boolean> isCreateNewPageMap = new HashMap<>();
HashMap<String,Boolean> isPageCoverPhotoChangedMap = new HashMap<>();
HashMap<String,String> coverPhotoDownloadUrlMap = new HashMap<>();
HashMap<String,ArrayList<String>> activePageMediaUrlArrayListMap = new HashMap<>();
HashMap<String,ArrayList<String>> retrievedActivePageMediaUrlArrayListMap = new HashMap<>();

//ends

    private void startUploadService(String libraryId, String tutorialId, String folderId, String pageId,String pageTitle, String pageContent, ArrayList<String> imagesListToUpload,boolean isTutorialPage) {
//        pageId = GlobalConfig.getRandomString(60);

        /**
         * implement upload service here and other here
         *
         */

        final String[] postContentHtml = {pageContent};
        //first upload all the images
        String[] coverImageDownloadUrl = new String[1];

        if(imagesListToUpload.size()==0){
            //upload page directly here no need to upload images

            //postTitle,postContentHtml[0]
            Log.e("pageContentInUploadService",postContentHtml[0]);
            writePageToDatabase( libraryId,  tutorialId,  folderId,  pageId,  pageTitle,  postContentHtml[0],"", isTutorialPage);
//            updatePageProgressNotification(pageId,notificationLayout.get(pageId),0,builder.get(pageId));

            return;
        }else {
            //store the number of media to be uploaded
            numberOfMedia.put(pageId,imagesListToUpload.size());

            for (String localUrl : imagesListToUpload) {
                uploadImageToServer( libraryId,  tutorialId,  folderId,  pageId,pageTitle, localUrl, isTutorialPage,imagesListToUpload, new ImageUploadListener() {
                    @Override
                    public void onComplete(String localUrl, String downloadUrl,ArrayList<String> imagesListToUpload) {
                        super.onComplete(localUrl, downloadUrl,imagesListToUpload);
                        //replace the url when completed.
                        postContentHtml[0] = postContentHtml[0].replaceAll(localUrl, downloadUrl);
                        //increment the number of successfully uploaded media from the Hashmap
                        numberOfMediaUploaded.put(pageId,numberOfMediaUploaded.get(pageId)+1);

                        if(imagesListToUpload.indexOf(localUrl) == 0){
                            coverImageDownloadUrl[0] = downloadUrl;
                        }
                        /*if number of media uploaded is equal to number of all the media supplied by author then all media has been uploaded;
                         OR
                          if number of media uploaded + number of media that failed are equal to number of all the media supplied by author then all media has been uploaded;
                          But if number of failures equals the number of all media, it means none of the media was uploaded;
                          In all situations, the texts must be uploaded
                        */
                        if (numberOfMediaUploaded.get(pageId) == imagesListToUpload.size() || (numberOfMediaUploaded.get(pageId) + numberOfMediaFailed.get(pageId)==imagesListToUpload.size()) ) {
                            //if all images uploaded successfully save page to database now.
                            //postTitle,postContentHtml[0]
                            Log.e("pageContentInUploadService_withImages_", postContentHtml[0]);

                            writePageToDatabase( libraryId,  tutorialId,  folderId,  pageId,  pageTitle,  postContentHtml[0],coverImageDownloadUrl[0], isTutorialPage);



                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        super.onFailed(throwable);
                        UploadPageManagerService.this.onFailed(pageId, throwable.getMessage());
                    }
                });


            }

            //post here (just title and page content )
            //postPage();
            //uploadPage();

        }
    }

    public void uploadImageToServer(String libraryId, String tutorialId, String folderId, String pageId,String pageTitle,String url,boolean isTutorialPage,ArrayList<String>imageUrlUploadList, ImageUploadListener listener){
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

            //increment the number of media that are already in progress
            numberOfProgressingMedia.put(pageId,numberOfProgressingMedia.get(pageId)+1);


            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    totalBytes.put(pageId,totalBytes.get(pageId)+snapshot.getTotalByteCount());
                    totalBytesTransferred.put(pageId,totalBytesTransferred.get(pageId)+snapshot.getBytesTransferred());

                    progressCount.put(pageId,(int) ((100.0 * totalBytesTransferred.get(pageId)  /totalBytes.get(pageId)) ));

                    Toast.makeText(UploadPageManagerService.this, ""+progressCount.get(pageId), Toast.LENGTH_SHORT).show();

                   // if((int)numberOfMedia.get(pageId) == (int) (numberOfProgressingMedia.get(pageId))) {
                        Toast.makeText(UploadPageManagerService.this, "was true  tt"+totalBytes.get(pageId)+" :" +progressCount.get(pageId)+" yes true ttr : "+totalBytesTransferred.get(pageId), Toast.LENGTH_SHORT).show();

                        UploadPageManagerService.this.onProgress(pageId, progressCount.get(pageId),folderId,  tutorialId,  isTutorialPage,  pageTitle);
                   /// }

                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                            if(imageUrlUploadList.indexOf(url) != 0) {
                                                //Do this when the url is not the cover photo url

                                                /**we need to keep record of all active download url to enable know when an author deletes an image from his page*/
                                                ArrayList<String> activePageMedialist = activePageMediaUrlArrayListMap.get(pageId);
                                                activePageMedialist.add(imageDownloadUrl);
                                                activePageMediaUrlArrayListMap.put(pageId, activePageMedialist);
                                            }
                                            listener.onComplete(url,imageDownloadUrl,imageUrlUploadList);

                                        }
                                        else{
                                            try {
                                                numberOfMediaFailed.put(pageId,numberOfMediaFailed.get(pageId)+1);
                                                listener.onFailed(new Throwable(task.getException().getMessage()));
                                            }catch(Exception e){  listener.onFailed(new Throwable("Error unknown!")); }

                                        }
                                    }
                                });
                            }
                            else {
                                //failed to upload the image
                              try{
                                  numberOfMediaFailed.put(pageId,numberOfMediaFailed.get(pageId)+1);
                                  listener.onFailed(new Throwable(task.getException().getMessage()));
                            }catch(Exception e){  listener.onFailed(new Throwable("Error unknown!")); }


                        }
                        }
                    });

        }

//        String downloadUrl = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_light_color_272x92dp.png";
//        listener.onComplete(url, downloadUrl);
    }

    /**This posts the page content to database*/
    private void writePageToDatabase(String libraryId, String tutorialId, String folderId, String pageId, String pageTitle, String pageContent,String coverImageDownloadUrl,boolean isTutorialPage) {
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
        DocumentReference pageDocumentReference = null;
        if(isTutorialPage){
            pageDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_TUTORIAL_PAGES_KEY).document(pageId);
        }else{
            pageDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_FOLDERS_KEY).document(folderId).collection(GlobalConfig.ALL_FOLDER_PAGES_KEY).document(pageId);

        }


        ArrayList<String> retrievedActivePageMediaUrlArrayList = retrievedActivePageMediaUrlArrayListMap.get(pageId);
        //delete media from  the retrieved active url list because the author has already deleted it from the page during edition
        for(int i=0; i<retrievedActivePageMediaUrlArrayList.size(); i++){
            if(!pageContent.contains(retrievedActivePageMediaUrlArrayList.get(i))){
                try {
                    FirebaseStorage.getInstance().getReferenceFromUrl(retrievedActivePageMediaUrlArrayList.get(i)).delete();
                    retrievedActivePageMediaUrlArrayList.remove(i);
                }catch(Exception ignored){}
            }
        }

        //merge new media urls with retrieved old ones from previous edition
        ArrayList<String> activePageMediaUrlList = activePageMediaUrlArrayListMap.get(pageId);
        activePageMediaUrlList.addAll(retrievedActivePageMediaUrlArrayList);
        activePageMediaUrlArrayListMap.put(pageId, activePageMediaUrlList);


        HashMap<String, Object> pageTextPartitionsDataDetailsHashMap = new HashMap<>();
        pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.PAGE_TITLE_KEY, pageTitle);
        pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.TUTORIAL_ID_KEY, tutorialId);
        pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.ACTIVE_PAGE_MEDIA_URL_LIST_KEY, activePageMediaUrlArrayListMap.get(pageId));
        if(isPageCoverPhotoChangedMap.get(pageId)){
            //new cover photo was added
            pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.PAGE_COVER_PHOTO_DOWNLOAD_URL_KEY, coverImageDownloadUrl);

        }else{
            //maintain the old cover photo url because it was not changed
            pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.PAGE_COVER_PHOTO_DOWNLOAD_URL_KEY, coverPhotoDownloadUrlMap.get(pageId));

        }
        pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.PAGE_CONTENT_KEY, pageContent);
        pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.PAGE_DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());

        if(isCreateNewPageMap.get(pageId)){
            pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.PAGE_DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
            pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.LIBRARY_ID_KEY, libraryId);
            pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.FOLDER_ID_KEY, folderId);
            pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.PAGE_ID_KEY, pageId);
            pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.AUTHOR_ID_KEY, GlobalConfig.getCurrentUserId());
            pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.ACTIVE_PAGE_MEDIA_URL_LIST_KEY, activePageMediaUrlArrayListMap.get(pageId));


        }

        writeBatch.set(pageDocumentReference,pageTextPartitionsDataDetailsHashMap, SetOptions.merge());

        DocumentReference documentReference = null;
        if(isTutorialPage){
            documentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId);
           String logType = GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_PAGE_TYPE_KEY;
            if(!isCreateNewPageMap.get(pageId)) {
                 logType = GlobalConfig.ACTIVITY_LOG_USER_EDIT_TUTORIAL_PAGE_TYPE_KEY;

            }
                GlobalConfig.updateActivityLog(logType, GlobalConfig.getCurrentUserId(), libraryId, tutorialId, folderId, pageId, null,  new GlobalConfig.ActionCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailed(String errorMessage) {

                }
            });

        }else{
            documentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_FOLDERS_KEY).document(folderId);
            String logType = GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_FOLDER_PAGE_TYPE_KEY;
            if(!isCreateNewPageMap.get(pageId)) {
                logType = GlobalConfig.ACTIVITY_LOG_USER_EDIT_FOLDER_PAGE_TYPE_KEY;
            }
            GlobalConfig.updateActivityLog(logType, GlobalConfig.getCurrentUserId(), libraryId, tutorialId, folderId, pageId, null,  new GlobalConfig.ActionCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailed(String errorMessage) {

                }
            });
        }
      if(isCreateNewPageMap.get(pageId)) {
          HashMap<String, Object> incrementPageNumberHashMap = new HashMap<>();
          incrementPageNumberHashMap.put(GlobalConfig.TOTAL_NUMBER_OF_PAGES_CREATED_KEY, FieldValue.increment(1L));
          writeBatch.update(documentReference, incrementPageNumberHashMap);
      }
        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        writePageToDatabase( libraryId,  tutorialId,  folderId,  pageId,  pageTitle,  pageContent,coverImageDownloadUrl, isTutorialPage);
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if(isTutorialPage){
                    String logType =GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_PAGE_TYPE_KEY;
                    if(!isCreateNewPageMap.get(pageId)) {
                        logType = GlobalConfig.ACTIVITY_LOG_USER_EDIT_TUTORIAL_PAGE_TYPE_KEY;
                    }
                    GlobalConfig.updateActivityLog(logType, GlobalConfig.getCurrentUserId(), libraryId, tutorialId, folderId, pageId, null,  new GlobalConfig.ActionCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailed(String errorMessage) {

                        }
                    });

                }else{
                    String logType = GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_FOLDER_PAGE_TYPE_KEY;
                    if(!isCreateNewPageMap.get(pageId)) {
                        logType = GlobalConfig.ACTIVITY_LOG_USER_EDIT_FOLDER_PAGE_TYPE_KEY;
                    }
                    GlobalConfig.updateActivityLog(logType, GlobalConfig.getCurrentUserId(), libraryId, tutorialId, folderId, pageId, null,  new GlobalConfig.ActionCallback() {
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
                        //delay the success for some seconds to allow some asynchronous and untracked processes to finish
                        UploadPageManagerService.this.onSuccess( pageId, folderId, tutorialId, isTutorialPage, pageTitle);

                    }
                },2000);
            }
        });
//            }


    }
    private void showPageProgressNotification(String pageId,String pageTitle){
        // Create a notification channel (required for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("my_channel_id", "My Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

// Create a custom layout for the notification
//        @SuppressLint("RemoteViewLayout")
        if(pageTitle.length()>20){
            pageTitle = pageTitle.substring(0,20);
        }
        notificationLayout.put(pageId,new RemoteViews(getPackageName(), R.layout.notification_layout));
        //set image cover icon here
        notificationLayout.get(pageId).setImageViewResource(R.id.icon, R.drawable.book_cover2);
        notificationLayout.get(pageId).setTextViewText(R.id.title, "\""+pageTitle+"\"" +( isCreateNewPageMap.get(pageId) ? " Page Uploading...":" Page Editing..."));

        if(numberOfMedia.get(pageId)>0) {
            notificationLayout.get(pageId).setProgressBar(R.id.progress_bar, 100, 2, false);
            notificationLayout.get(pageId).setTextViewText(R.id.percent, 0 + "% completed");
        }else{
            notificationLayout.get(pageId).setProgressBar(R.id.progress_bar, 0, 0, true);
        }

        // Create an explicit intent for launching the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        // Create a PendingIntent from the intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

//        notificationId = (int) System.currentTimeMillis(); // generate a unique ID
        builder.put(pageId,new NotificationCompat.Builder(UploadPageManagerService.this, "my_channel_id")
                .setSmallIcon(R.drawable.baseline_notifications_24) //notification icon
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCustomBigContentView(notificationLayout.get(pageId))
                .setCustomHeadsUpContentView(notificationLayout.get(pageId))
                .setContentIntent(pendingIntent) //intent to start when click notification
                .setOnlyAlertOnce(true) //only show once not when updating progress
                .setStyle(new NotificationCompat.BigTextStyle().bigText("\""+pageTitle+"\"" +( isCreateNewPageMap.get(pageId) ? " Page Uploading...":" Page Editing...")))
                .setOngoing(true) //set ongoing which is not cancelable by user
                .setAutoCancel(false) //auto cancel false
        );
        notificationManager.put(pageId,NotificationManagerCompat.from(this));
        notificationManager.get(pageId).notify(notificationId.get(pageId), builder.get(pageId).build());

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

    private void updatePageProgressNotification(String pageId,RemoteViews notificationLayout,int percentageCompleted, NotificationCompat.Builder builder){
        if(numberOfMedia.get(pageId)>0) {
            notificationLayout.setProgressBar(R.id.progress_bar, 100, percentageCompleted, false);
            notificationLayout.setTextViewText(R.id.percent, percentageCompleted + "% completed");
        }else{
            notificationLayout.setProgressBar(R.id.progress_bar, 0, 0, true);
        }
        NotificationManagerCompat.from(getApplicationContext()).notify(notificationId.get(pageId), builder.build());


    }

    public void showCompletedNotification(String pageId,String folderId,String tutorialId,boolean isTutorialPage,String pageTitle){
        @SuppressLint("RemoteViewLayout")
        //customize notification
        String pageTitle1 = pageTitle.length()>20 ? pageTitle.substring(0,20) :pageTitle;
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_layout);
        notificationLayout.setTextViewText(R.id.title, "\""+pageTitle1+"\"" +( isCreateNewPageMap.get(pageId) ? " Page Uploaded":" Page Edited"));
        notificationLayout.setProgressBar(R.id.progress_bar,100,100,false);
        notificationLayout.setTextViewText(R.id.percent, "100% completed");
        notificationLayout.setImageViewResource(R.id.icon,R.drawable.book_cover2);


        //intent where to redirect the user
        Intent intent = new Intent(UploadPageManagerService.this, PageActivity.class);
        intent.putExtra(GlobalConfig.PAGE_ID_KEY,pageId);
        intent.putExtra(GlobalConfig.FOLDER_ID_KEY,folderId);
        intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
        intent.putExtra(GlobalConfig.AUTHOR_ID_KEY,GlobalConfig.getCurrentUserId());
        intent.putExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,isTutorialPage);
        // Create a PendingIntent from the intent
        PendingIntent pendingIntent = PendingIntent.getActivity(UploadPageManagerService.this, 0, intent, 0);

        int notificationId = (int) System.currentTimeMillis(); // generate a unique ID
        NotificationCompat.Builder builder = new NotificationCompat.Builder(UploadPageManagerService.this, "my_channel_id")
                .setSmallIcon(R.drawable.baseline_notifications_24) //notification icon
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCustomBigContentView(notificationLayout)
                .setCustomHeadsUpContentView(notificationLayout)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("\""+pageTitle1+"\"" +( isCreateNewPageMap.get(pageId) ? " Page Uploaded":" Page Edited")))
                .setAutoCancel(false);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(UploadPageManagerService.this);
        notificationManager.notify(notificationId, builder.build());

    }


    @Override
    public void onNewPage(String pageId,String  folderId,String  tutorialId,String  libraryId, boolean isTutorialPage,boolean isCreateNewPage,String coverPhotoDownloadUrl,boolean isPageCoverPhotoChanged,String  pageTitle,ArrayList<String>retrievedActivePageMediaUrlArrayList,String  pageContent,ArrayList<String>imageListToUpload) {
//initialize the hashmaps to avoid null pointer exception
        numberOfMedia.put(pageId,imageListToUpload.size());
        numberOfMediaUploaded.put(pageId,0);
        numberOfMediaFailed.put(pageId,0);
        numberOfProgressingMedia.put(pageId,0);
        totalBytesTransferred.put(pageId,0L);
        totalBytes.put(pageId,0L);
        progressCount.put(pageId,0);
        notificationLayout.put(pageId,new RemoteViews(getPackageName(), R.layout.notification_layout));
        builder.put(pageId,new NotificationCompat.Builder(UploadPageManagerService.this, "my_channel_id"));
        notificationManager.put(pageId,NotificationManagerCompat.from(this));
        notificationId.put(pageId,(int)System.currentTimeMillis());
        isCreateNewPageMap.put(pageId,isCreateNewPage);
        isPageCoverPhotoChangedMap.put(pageId,isPageCoverPhotoChanged);
        coverPhotoDownloadUrlMap.put(pageId,coverPhotoDownloadUrl);
        activePageMediaUrlArrayListMap.put(pageId,new ArrayList<>());
        retrievedActivePageMediaUrlArrayListMap.put(pageId,retrievedActivePageMediaUrlArrayList);

        startUploadService(libraryId,tutorialId,folderId,pageId,pageTitle,pageContent,imageListToUpload,isTutorialPage);

        showPageProgressNotification(pageId,pageTitle);
    }

    @Override
    public void onFailed(String pageId, String errorMessage) {

    }

    @Override
    public void onProgress(String pageId, int progressCounter,String  folderId,String  tutorialId, boolean isTutorialPage,String  pageTitle) {

        if(progressCounter==100){
            notificationManager.get(pageId).cancelAll();
            showCompletedNotification( pageId, folderId, tutorialId, isTutorialPage, pageTitle);
        }else{
            updatePageProgressNotification(pageId,notificationLayout.get(pageId),progressCount.get(pageId),builder.get(pageId));
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
    public void onSuccess(String pageId,String  folderId,String  tutorialId, boolean isTutorialPage,String  pageTitle) {

        notificationManager.get(pageId).cancelAll();
        showCompletedNotification( pageId, folderId, tutorialId, isTutorialPage, pageTitle);


    }

    public class ImageUploadListener implements ImageUploadInterface {

        @Override
        public void onComplete(String localUrl, String downloadUrl, ArrayList<String> imagesListToUpload) {

        }

        @Override
        public void onFailed(Throwable throwable) {

        }
    }

    interface ImageUploadInterface{
        public void onComplete(String localUrl, String downloadUrl, ArrayList<String> imagesListToUpload);
        public void onFailed(Throwable throwable);
    }

}