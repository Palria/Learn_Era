
package com.palria.learnera;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
                                                    onPageUploadListener.onFailed(e.getMessage());
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
                                            onPageUploadListener.onSuccess(pageId);
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
                                                    onPageUploadListener.onFailed(e.getMessage());
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
                                                                onPageUploadListener.onSuccess(pageId);
                                                            }
                                                        }
                                                    }
                                                });


                                        }
                                        else{
                                            onPageUploadListener.onFailed(pageId);
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

    interface OnPageUploadListener{
        void onNewPage(String pageId);
        void onFailed(String pageId);
        void onProgress(String pageId, int progressCount);
        void onSuccess(String pageId);


    }


}