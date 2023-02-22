package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class PageActivity extends AppCompatActivity {
boolean isTutorialPage = true;

String tutorialId = "";
String folderId = "";
String pageId = "";

LinearLayout containerLinearLayout;
TextView pageTitleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        initUI();
        fetchIntentData();
        fetchPageData();
    }

    void initUI(){
        containerLinearLayout = findViewById(R.id.containerLinearLayoutId);
        pageTitleTextView = findViewById(R.id.pageTitleTextViewId);
    }

    void fetchIntentData(){
        Intent intent = getIntent();

        isTutorialPage = intent.getBooleanExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,true);
        tutorialId = intent.getStringExtra(GlobalConfig.TUTORIAL_ID_KEY);
        folderId = intent.getStringExtra(GlobalConfig.FOLDER_ID_KEY);
        pageId = intent.getStringExtra(GlobalConfig.PAGE_ID_KEY);

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

                        String pageTitle = ""+ documentSnapshot.get(GlobalConfig.PAGE_TITLE_KEY);
                        pageTitleTextView.setText(pageTitle);
                        long totalNumberOfPageData =  documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_PAGE_DATA_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_PAGE_DATA_KEY) : 0L;
                        if(totalNumberOfPageData != 0L){
                            for(int i =0; i<totalNumberOfPageData; i++){

                                ArrayList<String> pageDataArrayList = documentSnapshot.get(GlobalConfig.DATA_ARRAY_KEY+i)!=null ? (ArrayList<String>) documentSnapshot.get(GlobalConfig.DATA_ARRAY_KEY+i) : new ArrayList<>();
                                if(pageDataArrayList!=null && pageDataArrayList.size() != 0){
                                    switch (pageDataArrayList.get(0)) {
                                        case GlobalConfig.TEXT_TYPE:
                                            renderPageTextData(pageDataArrayList);
                                            Toast.makeText(getApplicationContext(), "text", Toast.LENGTH_SHORT).show();
                                            break;
                                        case GlobalConfig.IMAGE_TYPE:
                                            renderPageImageData(pageDataArrayList);
                                            Toast.makeText(getApplicationContext(), "image", Toast.LENGTH_SHORT).show();

                                            break;
                                        case GlobalConfig.TABLE_TYPE:
                                            renderPageTableData(pageDataArrayList);
                                            Toast.makeText(getApplicationContext(), "table", Toast.LENGTH_SHORT).show();

                                            break;
                                        case GlobalConfig.TODO_TYPE:
                                            renderPageTodoData(pageDataArrayList);
                                            Toast.makeText(getApplicationContext(), "todo", Toast.LENGTH_SHORT).show();

                                            break;
                                        default:
                                            renderPageImageData(pageDataArrayList);
                                            Toast.makeText(getApplicationContext(), "image", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), "array empty", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }

                    }
                });

    }

    void renderPageTextData(ArrayList<String> textDetails){


//        .add(0,GlobalConfig.TEXT_TYPE);
//        .add(1,containerLinearLayout.indexOfChild(editText)+"");
//        .add(2,editText.getText().toString());

        int position =  Integer.parseInt(textDetails.get(1));
        String pageText = textDetails.get(2);


        View view = getLayoutInflater().inflate(R.layout.page_text_layout,containerLinearLayout,false);
        TextView pageTextDataTextView = view.findViewById(R.id.pageTextDataTextViewId);
        pageTextDataTextView.setText(pageText);
        containerLinearLayout.addView(view,position);
        Toast.makeText(getApplicationContext(), "text added", Toast.LENGTH_SHORT).show();

    }
   void renderPageImageData(ArrayList<String> imageDetails){
        int position = Integer.parseInt(imageDetails.get(0));
        String imageDownloadUrl = imageDetails.get(1);

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
       Toast.makeText(getApplicationContext(), "image added", Toast.LENGTH_SHORT).show();

   }

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
       Toast.makeText(getApplicationContext(), "todo added", Toast.LENGTH_SHORT).show();

    }

    void setTableRowCell(String[] cellArray, LinearLayout rowLinearLayout){
        for(int i=0; i<cellArray.length; i++){

            View tableCell  =  getLayoutInflater().inflate(R.layout.page_table_cell_text_view,rowLinearLayout,false);
            TextView textViewCell =tableCell.findViewById(R.id.textCellId);
            textViewCell.setText(cellArray[i]);
            rowLinearLayout.addView(tableCell);

        }
    }
}