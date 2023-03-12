package com.palria.learnera;

import java.util.ArrayList;

public interface OnPageUploadListener {
    void onNewPage(String pageId, String  folderId, String  tutorialId, String  libraryId, boolean isTutorialPage,boolean isCreateNewPage,String coverPhotoDownloadUrl,boolean isPageCoverPhotoChanged, String  pageTitle,ArrayList<String>retrievedActivePageMediaUrlArrayList, String  pageContent, ArrayList<String> imageListToUpload);
    void onFailed(String pageId,String errorMessage);
    void onProgress(String pageId, int progressCount,String  folderId,String  tutorialId, boolean isTutorialPage,String  pageTitle);
    void onSuccess(String pageId,String  folderId,String  tutorialId, boolean isTutorialPage,String  pageTitle);

}
