package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.makeramen.roundedimageview.RoundedImageView;
import com.palria.learnera.lib.rcheditor.WYSIWYG;
import com.palria.learnera.widgets.BottomSheetFormBuilderWidget;
import com.palria.learnera.widgets.LEBottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;

public class PageActivity extends AppCompatActivity {
boolean isTutorialPage = true;

String authorId = "";
String libraryId = "";
String tutorialId = "";
String folderId = "";
String pageId = "";

LinearLayout containerLinearLayout;
TextView pageTitleTextView;
TextView viewCount;
TextView dateCreatedTextView;
TextView bookmarkCountTextView;
ImageButton morePageActionButton;
    RoundedImageView coverImageView;
    WYSIWYG pageContentViewer;
    LEBottomSheetDialog leBottomSheetDialog;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        initUI();
        fetchIntentData();
        if(!(GlobalConfig.getBlockedItemsList().contains(authorId+"")) &&!(GlobalConfig.getBlockedItemsList().contains(libraryId+"")) && !(GlobalConfig.getBlockedItemsList().contains(tutorialId+"")))  {

            fetchPageData();
//        if(!authorId.equals(GlobalConfig.getCurrentUserId())){
//            morePageActionButton.setVisibility(View.GONE);
//        }
            if(GlobalConfig.getCurrentUserId().equals(authorId+"")){

                leBottomSheetDialog.addOptionItem("Edit Page", R.drawable.ic_baseline_edit_24,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                leBottomSheetDialog.hide();
                                Intent intent = new Intent(PageActivity.this,UploadPageActivity.class);
                                intent.putExtra(GlobalConfig.PAGE_ID_KEY,pageId);
                                intent.putExtra(GlobalConfig.FOLDER_ID_KEY,folderId);
                                intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY,tutorialId);
                                intent.putExtra(GlobalConfig.LIBRARY_ID_KEY,libraryId);
//                intent.putExtra(GlobalConfig.PAGE_CONTENT_KEY,pageContentViewer.getHtml());
//                intent.putExtra(GlobalConfig.PAGE_TITLE_KEY,pageTitleTextView.getText());
                                intent.putExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,isTutorialPage);
                                intent.putExtra(GlobalConfig.IS_CREATE_NEW_PAGE_KEY,false);
                                startActivity(intent);
                            }
                        }, 0);
                leBottomSheetDialog.addOptionItem("Delete Page", R.drawable.ic_baseline_error_outline_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new AlertDialog.Builder(PageActivity.this)
                                .setCancelable(true)
                                .setTitle("Delete Your Page!")
                                .setMessage("Action cannot be undone, are you sure you want to delete your Page?")
                                .setPositiveButton("Yes,delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        toggleProgress(true);
                                        Toast.makeText(getApplicationContext(), "Deleting", Toast.LENGTH_SHORT).show();

                                        leBottomSheetDialog.hide();
                                        GlobalConfig.deletePage(libraryId, tutorialId,folderId,pageId,isTutorialPage, new GlobalConfig.ActionCallback() {
                                            @Override
                                            public void onSuccess() {
                                                toggleProgress(false);
                                                Toast.makeText(getApplicationContext(), "Delete Page success", Toast.LENGTH_SHORT).show();

                                                PageActivity.super.onBackPressed();
                                            }

                                            @Override
                                            public void onFailed(String errorMessage) {
                                                toggleProgress(false);
                                                GlobalHelpers.showAlertMessage("error",getApplicationContext(), "Unable to delete Page",errorMessage);
                                                Toast.makeText(getApplicationContext(), "Unable to deleted Page!  please try again", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                        PageActivity.super.onBackPressed();

                                    }
                                })
                                .setNegativeButton("No", null)
                                .create().show();


                    }
                }, 0);

            }
            leBottomSheetDialog.addOptionItem("Bookmark", R.drawable.ic_baseline_bookmarks_24,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Snackbar saveSnackBar = GlobalConfig.createSnackBar(getApplicationContext(),coverImageView,"Initializing bookmark ...", Snackbar.LENGTH_INDEFINITE);



                            //CHECK IF THE CURRENT USER HAS ALREADY SAVED THIS FOLDER, IF SO DO STH
                            DocumentReference bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                                    .collection(GlobalConfig.ALL_USERS_KEY)
                                    .document(GlobalConfig.getCurrentUserId())
                                    .collection(GlobalConfig.BOOK_MARKS_KEY).document(pageId);
                            GlobalConfig.checkIfDocumentExists(bookMarkOwnerReference, new GlobalConfig.OnDocumentExistStatusCallback() {
                                @Override
                                public void onExist() {
                                    saveSnackBar.dismiss();

                                    new AlertDialog.Builder(PageActivity.this)
                                            .setTitle("Remove this Page from bookmark?")
                                            .setMessage("You have already added this folder to your bookmarks, do you want to remove it?")
                                            .setCancelable(false)
                                            .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Snackbar snackBar = GlobalConfig.createSnackBar(getApplicationContext(),coverImageView,"Removing from bookmark...", Snackbar.LENGTH_INDEFINITE);
                                                    String actionType = "NONE";
                                                    if(isTutorialPage){
                                                        actionType = GlobalConfig.TUTORIAL_PAGE_TYPE_KEY;
                                                    }else{
                                                        actionType = GlobalConfig.FOLDER_PAGE_TYPE_KEY;

                                                    }
                                                    GlobalConfig.removeBookmark(authorId, libraryId, tutorialId, folderId,pageId,actionType, new GlobalConfig.ActionCallback() {
                                                        @Override
                                                        public void onSuccess() {
//                                                Toast.makeText(TutorialActivity.this, "bookmark removed", Toast.LENGTH_SHORT).show();
                                                            snackBar.dismiss();
                                                            GlobalConfig.createSnackBar(getApplicationContext(),coverImageView,"Bookmark removed!", Snackbar.LENGTH_SHORT);
                                                        }

                                                        @Override
                                                        public void onFailed(String errorMessage) {
//                                                Toast.makeText(TutorialActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                                            snackBar.dismiss();
                                                            GlobalConfig.createSnackBar(getApplicationContext(),coverImageView,"Failed to remove from bookmark please try again!", Snackbar.LENGTH_SHORT);
                                                        }
                                                    });
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Toast.makeText(PageActivity.this, "undo remove bookmark.", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .show();
                                }

                                @Override
                                public void onNotExist() {
                                    saveSnackBar.dismiss();

                                    new AlertDialog.Builder(PageActivity.this)
                                            .setTitle("Add this Page to bookmark?")
                                            .setMessage("when you save to bookmark you are able to view it in your bookmarked tab")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Snackbar snackBar = GlobalConfig.createSnackBar(getApplicationContext(),coverImageView,"Adding to bookmark...", Snackbar.LENGTH_INDEFINITE);

                                                    String actionType = "NONE";
                                                    if(isTutorialPage){
                                                        actionType = GlobalConfig.TUTORIAL_PAGE_TYPE_KEY;
                                                    }else{
                                                        actionType = GlobalConfig.FOLDER_PAGE_TYPE_KEY;

                                                    }
                                                    GlobalConfig.addToBookmark(authorId, libraryId, tutorialId, folderId,pageId,actionType, new GlobalConfig.ActionCallback() {
                                                        @Override
                                                        public void onSuccess() {
//                                                Toast.makeText(LibraryActivity.this, "bookmark added", Toast.LENGTH_SHORT).show();
                                                            snackBar.dismiss();
                                                            GlobalConfig.createSnackBar(getApplicationContext(),coverImageView,"Bookmark added!", Snackbar.LENGTH_SHORT);

                                                        }

                                                        @Override
                                                        public void onFailed(String errorMessage) {
//                                                Toast.makeText(TutorialActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                                            snackBar.dismiss();
                                                            GlobalConfig.createSnackBar(getApplicationContext(),coverImageView,"Failed to add to bookmark please try again!", Snackbar.LENGTH_SHORT);
                                                        }
                                                    });

                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Toast.makeText(PageActivity.this, "cancelled bookmark.", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .show();
                                }

                                @Override
                                public void onFailed(@NonNull String errorMessage) {
                                    saveSnackBar.dismiss();
                                    GlobalConfig.createSnackBar(getApplicationContext(),coverImageView,"Failed to Fetch bookmark details please try again", Snackbar.LENGTH_SHORT);

                                }
                            });


                            leBottomSheetDialog.hide();

                        }
                    }, 0);
            leBottomSheetDialog.render();

            morePageActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               leBottomSheetDialog.show();
            }
        });
        }else{

            Toast.makeText(this, "Page Blocked! Unblock to explore the Page", Toast.LENGTH_SHORT).show();

            super.onBackPressed();
        }
    }

    void initUI(){
//        containerLinearLayout = findViewById(R.id.containerLinearLayoutId);
        pageTitleTextView = findViewById(R.id.pageTitle);
        coverImageView =findViewById(R.id.pageCover);
        viewCount =findViewById(R.id.viewCount);
        dateCreatedTextView =findViewById(R.id.dateCreatedTextViewId);
        bookmarkCountTextView =findViewById(R.id.bookmarkCountTextViewId);
        pageContentViewer=findViewById(R.id.pageContentViewer);
        morePageActionButton=findViewById(R.id.morePageActionButtonId);

        pageContentViewer.setPadding(10,10,10,10);
        pageContentViewer.setEnabled(false);//disable editing.
        pageContentViewer.setEditorFontSize(16);
        alertDialog = new AlertDialog.Builder(PageActivity.this)
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();
        leBottomSheetDialog=new LEBottomSheetDialog(this);

    }

    void fetchIntentData(){
        Intent intent = getIntent();

        isTutorialPage = intent.getBooleanExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,true);
        tutorialId = intent.getStringExtra(GlobalConfig.TUTORIAL_ID_KEY);
        authorId = intent.getStringExtra(GlobalConfig.AUTHOR_ID_KEY);
        folderId = intent.getStringExtra(GlobalConfig.FOLDER_ID_KEY);
        pageId = intent.getStringExtra(GlobalConfig.PAGE_ID_KEY);

    }

    private void toggleProgress(boolean show) {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
    }
    void fetchPageData(){
        DocumentReference documentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_TUTORIAL_PAGES_KEY).document(pageId);
        if(!isTutorialPage){
            documentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_FOLDERS_KEY).document(folderId).collection(GlobalConfig.ALL_FOLDER_PAGES_KEY).document(pageId);

        }
        documentReference.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                        HashMap<String, Object> pageTextPartitionsDataDetailsHashMap = new HashMap<>();
//                        pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.PAGE_TITLE_KEY, pageTitle);
//                        pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.LIBRARY_ID_KEY, libraryId);
//                        pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.TOTAL_NUMBER_OF_PAGE_DATA_KEY, totalNumberOfChildren);
//                        pageTextPartitionsDataDetailsHashMap.put(GlobalConfig.DATE_TIME_STAMP_PAGE_CREATED_KEY, FieldValue.serverTimestamp());
                        //THIS IS THE TITLE OF THE PAGE
                        String pageTitle = ""+ documentSnapshot.get(GlobalConfig.PAGE_TITLE_KEY);
                        libraryId = ""+ documentSnapshot.get(GlobalConfig.LIBRARY_ID_KEY);

                        //USE THIS URL TO DOWNLOAD PAGE'S COVER IMAGE
                        String pageCoverImageDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.PAGE_COVER_PHOTO_DOWNLOAD_URL_KEY);
                        String dateCreated =  documentSnapshot.get(GlobalConfig.PAGE_DATE_CREATED_TIME_STAMP_KEY)!=null?  documentSnapshot.getTimestamp(GlobalConfig.PAGE_DATE_CREATED_TIME_STAMP_KEY).toDate() +"" :"Undefined";
                        if(dateCreated.length()>10){
                            dateCreated = dateCreated.substring(0,10);
                        }
                        String viewCount1 = ""+ documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_PAGE_VISITOR_KEY);
                        String bookmarkCount1 =  documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_BOOK_MARKS_KEY)!=null ?documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_BOOK_MARKS_KEY)+"":"0";
                        Toast.makeText(getApplicationContext(), pageCoverImageDownloadUrl, Toast.LENGTH_LONG).show();
                        Glide.with(PageActivity.this)
                                .load(pageCoverImageDownloadUrl)
                                .into(coverImageView);
                        viewCount.setText(viewCount1);
                        bookmarkCountTextView.setText(bookmarkCount1);
                        dateCreatedTextView.setText(dateCreated);
                        //THIS IS THE PAGE CONTENT IN HTML FORMAT , USE IT AND RENDER TO READABLE TEXT
                        String html = ""+ documentSnapshot.get(GlobalConfig.PAGE_CONTENT_KEY);
                        pageTitleTextView.setText(pageTitle);
//                        long totalNumberOfPageData =  documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_PAGE_DATA_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_PAGE_DATA_KEY) : 0L;

                        renderHtmlFromDatabase(html);
                        GlobalConfig.incrementNumberOfVisitors(authorId,null,tutorialId,folderId,pageId,false,false,false,false,isTutorialPage,!isTutorialPage);

//                        for(int i=0; i<totalNumberOfPageData; i++){
//                            View view = new View(getApplicationContext());
//                            containerLinearLayout.addView(view);
//                        }
//
//                        if(totalNumberOfPageData != 0L){
//                            for(int i =0; i<totalNumberOfPageData; i++){
//
//                                ArrayList<String> pageDataArrayList = documentSnapshot.get(GlobalConfig.DATA_ARRAY_KEY+i)!=null ? (ArrayList<String>) documentSnapshot.get(GlobalConfig.DATA_ARRAY_KEY+i) : new ArrayList<>();
//                                if(pageDataArrayList!=null && pageDataArrayList.size() != 0){
//                                    switch (pageDataArrayList.get(0)) {
//                                        case GlobalConfig.TEXT_TYPE:
//                                            renderPageTextData(pageDataArrayList);
//                                            Toast.makeText(getApplicationContext(), "text", Toast.LENGTH_SHORT).show();
//                                            break;
//                                        case GlobalConfig.IMAGE_TYPE:
//                                            renderPageImageData(pageDataArrayList);
//                                            Toast.makeText(getApplicationContext(), "image", Toast.LENGTH_SHORT).show();
//
//                                            break;
//                                        case GlobalConfig.TABLE_TYPE:
//                                            renderPageTableData(pageDataArrayList);
//                                            Toast.makeText(getApplicationContext(), "table", Toast.LENGTH_SHORT).show();
//
//                                            break;
//                                        case GlobalConfig.TODO_TYPE:
//                                            renderPageTodoData(pageDataArrayList);
//                                            Toast.makeText(getApplicationContext(), "todo", Toast.LENGTH_SHORT).show();
//
//                                            break;
//
//                                    }
//                                }else{
//                                    Toast.makeText(getApplicationContext(), "array empty", Toast.LENGTH_SHORT).show();
//
//                                }
//                            }
//                        }

                    }
                });

    }

    @Deprecated
    void renderPageTextData(ArrayList<String> textDetails){


//        .add(0,GlobalConfig.TEXT_TYPE);
//        .add(1,containerLinearLayout.indexOfChild(editText)+"");
//        .add(2,editText.getText().toString());

        int position =  Integer.parseInt(textDetails.get(1));
        String pageText = textDetails.get(2);



       // View view = getLayoutInflater().inflate(R.layout.page_text_layout,containerLinearLayout,false);
        //TextView pageTextDataTextView = view.findViewById(R.id.pageTextDataTextViewId);
//        SpannableStringBuilder spannableStringBuilder =  GlobalConfig.interpretStyles(this,new StringBuilder(pageText));
//        pageTextDataTextView.setText(spannableStringBuilder);

      //  GlobalConfig.setHtmlText(this,pageTextDataTextView, pageText);
       // containerLinearLayout.addView(view,position);
//        Toast.makeText(getApplicationContext(), position, Toast.LENGTH_SHORT).show();

    }
    @Deprecated
    void renderPageImageData(ArrayList<String> imageDetails){
        int position = Integer.parseInt(imageDetails.get(1));
        String imageDownloadUrl = imageDetails.get(2);

           LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           View view  =  layoutInflater.inflate(R.layout.page_image_layout,containerLinearLayout,false);
           ImageView imageView = view.findViewById(R.id.imageViewId);
           ImageView removeImage = view.findViewById(R.id.removeImageId);
try {
    Glide.with(PageActivity.this)
            .load(imageDownloadUrl)
            .into(imageView);
}catch(Exception ignored){}

           removeImage.setVisibility(View.GONE);
           containerLinearLayout.addView(view,position);
//       Toast.makeText(getApplicationContext(), "image added", Toast.LENGTH_SHORT).show();

   }
    @Deprecated
    void renderPageTableData(ArrayList<String> tableDetails){
//
//       pageTableTextDataTypeDetailsArrayList.add(1,containerLinearLayout.indexOfChild(containerLinearLayout.getChildAt(i)) +"");
//       pageTableTextDataTypeDetailsArrayList.add(2,numberOfRows+"");
//       pageTableTextDataTypeDetailsArrayList.add(3,numberOfColumns+"");
//       pageTableTextDataTypeDetailsArrayList.add(4,tableItems+"");

       int position = Integer.parseInt(tableDetails.get(1));
       int numberOfRows = Integer.parseInt(tableDetails.get(2));
       int numberOfColumns = Integer.parseInt(tableDetails.get(3));
       String tableItems = tableDetails.get(4);
       String[] rowItems = tableItems.split("_");


       LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View tableView  =  layoutInflater.inflate(R.layout.page_table_layout,containerLinearLayout,false);
       LinearLayout tableLinearLayout = tableView.findViewById(R.id.tableLinearLayoutId);
       ImageView addMoreColumnActionImageView = tableView.findViewById(R.id.addMoreColumnActionImageViewId);
       ImageView addMoreRowActionImageView = tableView.findViewById(R.id.addMoreRowActionImageViewId);
       addMoreColumnActionImageView.setVisibility(View.GONE);
       addMoreRowActionImageView.setVisibility(View.GONE);

       for(int i = 0; i<numberOfRows; i++){
           View tableRowView  =  getLayoutInflater().inflate(R.layout.page_table_row_layout,containerLinearLayout,false);
           ImageView removeRowActionImageView = tableRowView.findViewById(R.id.removeRowActionImageViewId);
           removeRowActionImageView.setVisibility(View.GONE);
           LinearLayout tableRowLinearLayout = tableRowView.findViewById(R.id.tableRowLinearLayoutId);
           String[] rowCellArray = rowItems[i].split(",");
           setTableRowCell(rowCellArray,tableRowLinearLayout);
           tableLinearLayout.addView(tableRowView);
       }

       containerLinearLayout.addView(tableView,position);
       Toast.makeText(getApplicationContext(), "table added", Toast.LENGTH_SHORT).show();

    }
    @Deprecated
    void renderPageTodoData(ArrayList<String> todoDetails){
//
//       pageTodoTextDataTypeDetailsArrayList.add(1,""+containerLinearLayout.indexOfChild(containerLinearLayout.getChildAt(i)));
//       pageTodoTextDataTypeDetailsArrayList.add(2,numberOfItems+"");
//       pageTodoTextDataTypeDetailsArrayList.add(3,todoItems+"");

       int position = Integer.parseInt(todoDetails.get(1));
       int numberOfItems =Integer.parseInt(todoDetails.get(2));
       String todoItems = todoDetails.get(3);
       String[] todoItemsArray = todoItems.split(",");

       View todoGroupView  =  getLayoutInflater().inflate(R.layout.page_to_do_group_layout,containerLinearLayout,false);
       LinearLayout todoLinearLayout = todoGroupView.findViewById(R.id.todoItemLinearLayoutId);
       ImageView addMoreItemActionButton = todoGroupView.findViewById(R.id.addMoreItemActionButtonId);
       addMoreItemActionButton.setVisibility(View.GONE);

       for(int i = 0; i<todoItemsArray.length; i++){

           View todoItemView  =  getLayoutInflater().inflate(R.layout.page_to_do_item_layout,todoLinearLayout,false);
           ImageView removeItem  =  todoItemView.findViewById(R.id.removeTodoItemActionImageId);
           EditText todoEditText = todoItemView.findViewById(R.id.todoEditTextId);
           todoEditText.setLongClickable(false);
           todoEditText.setFocusable(false);
           todoEditText.setText(todoItemsArray[i]);
           removeItem.setVisibility(View.GONE);


           todoLinearLayout.addView(todoItemView);
       }

       containerLinearLayout.addView(todoGroupView,position);
//       Toast.makeText(getApplicationContext(), "todo added", Toast.LENGTH_SHORT).show();

    }
    @Deprecated
    void setTableRowCell(String[] cellArray, LinearLayout rowLinearLayout){
        for(int i=0; i<cellArray.length; i++){

            View tableCell  =  getLayoutInflater().inflate(R.layout.page_table_cell_text_view,rowLinearLayout,false);
            TextView textViewCell =tableCell.findViewById(R.id.textCellId);
            textViewCell.setText(cellArray[i]);
            rowLinearLayout.addView(tableCell);

        }
    }

    /**this is used to test the html from the database
     * i used it for only test purposes
     * */
    private void renderHtmlFromDatabase(String html){
        pageContentViewer.setHtml(html);
        //View view = getLayoutInflater().inflate(R.layout.page_text_layout,containerLinearLayout,false);
        //TextView pageTextDataTextView = view.findViewById(R.id.pageTextDataTextViewId);
       // pageTextDataTextView.setTextIsSelectable(true);
        //GlobalConfig.setHtmlText(this,pageTextDataTextView, html);
       // containerLinearLayout.addView(view);
    }

}