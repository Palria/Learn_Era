package com.palria.learnera;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

public class AddCategoryFragment extends Fragment {

    public AddCategoryFragment() {
        // Required empty public constructor
    }

    LinearLayout containerLinearLayout;
    MaterialButton saveCategoryActionButton;
    AlertDialog alertDialog;
    OnCategoryUpdateCallback onCategoryUpdateCallback;
    FloatingActionButton fab ;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView =  inflater.inflate(R.layout.fragment_add_category, container, false);
        initUI(parentView);
        fetchCategoryList();
        saveCategoryActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCategory();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayCategory(GlobalConfig.getCurrentUserId(),containerLinearLayout.getChildCount());
            }
        });
        onCategoryUpdateCallback = new OnCategoryUpdateCallback() {
            @Override
            public void onSuccess() {
                toggleProgress(false);
            }

            @Override
            public void onFailed(String errorMessage) {
                toggleProgress(false);

            }
        };
        return parentView;
    }

    private void initUI(View parentView){
        containerLinearLayout = parentView.findViewById(R.id.containerLinearLayoutId);
        fab = parentView.findViewById(R.id.fab);
        saveCategoryActionButton = parentView.findViewById(R.id.saveCategoryActionButtonId);
        alertDialog = new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();
    }

    private void fetchCategoryList(){
        toggleProgress(true);
        GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.PLATFORM_CONFIGURATION_FILE_KEY).document(GlobalConfig.PLATFORM_CONFIGURATION_FILE_KEY).get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                toggleProgress(false);
                ArrayList<String> allCategoryList = documentSnapshot.get(GlobalConfig.CATEGORY_LIST_KEY)!=null? (ArrayList<String>) documentSnapshot.get(GlobalConfig.CATEGORY_LIST_KEY):new ArrayList();
              if(allCategoryList.size()!=0) {
                  for (int i = 0; i < allCategoryList.size(); i++) {
                      String category = allCategoryList.get(i) + "";
                      int position = allCategoryList.indexOf(category);
                      displayCategory(category,position);

                  }
              }else{
                displayCategory("",containerLinearLayout.getChildCount());
              }
            }
        });
    }

    private void displayCategory(String title, int position){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View categoryItem = inflater.inflate(R.layout.category_item_layout,containerLinearLayout,false);
        EditText categoryTitleEditText = categoryItem.findViewById(R.id.categoryTitleEditTextId);
        EditText categoryPositionEditText = categoryItem.findViewById(R.id.categoryPositionEditTextId);
        ImageButton removeCategoryImageButton = categoryItem.findViewById(R.id.removeCategoryImageButtonId);
        removeCategoryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                containerLinearLayout.removeView(categoryItem);
            }
        });
        categoryTitleEditText.setText(title);
        categoryPositionEditText.setText(position+"");

        containerLinearLayout.addView(categoryItem,position);
    }

    private void saveCategory(){
        ArrayList<String> categoryList = new ArrayList<>();
        if(containerLinearLayout.getChildCount()>0){
            for(int i=0;i<containerLinearLayout.getChildCount();i++) {
                LinearLayout categoryItem = (LinearLayout) containerLinearLayout.getChildAt(i);
                EditText categoryTitleEditText = categoryItem.findViewById(R.id.categoryTitleEditTextId);
                EditText categoryPositionEditText = categoryItem.findViewById(R.id.categoryPositionEditTextId);
                ImageButton removeCategoryImageButton = categoryItem.findViewById(R.id.removeCategoryImageButtonId);
                String category = categoryTitleEditText.getText() +"";
                int newIndex =   Integer.parseInt(categoryPositionEditText.getText().toString().trim().equals("") ? "0" : categoryPositionEditText.getText() + "");
                if(category.isEmpty() || categoryPositionEditText.getText().toString().isEmpty()){
                    GlobalConfig.createSnackBar(getContext(), saveCategoryActionButton, "All category must have title and position, fill in the title and position areas and try again! ", Snackbar.LENGTH_INDEFINITE);
                    return;
                }
                if(newIndex<0){
//                    categoryPositionEditText.setError("It must be less than number of categories in the list and greater than or equal to zero");
                    newIndex = 0;
//                    return;
                }
                categoryList.add(newIndex,category);
            }

                WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
                DocumentReference documentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.PLATFORM_CONFIGURATION_FILE_KEY).document(GlobalConfig.PLATFORM_CONFIGURATION_FILE_KEY);


                HashMap<String, Object> categoryDetail = new HashMap<>();
                categoryDetail.put(GlobalConfig.CATEGORY_LIST_KEY, categoryList);
                writeBatch.set(documentReference, categoryDetail, SetOptions.merge());

            toggleProgress(true);

            writeBatch.commit().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    toggleProgress(false);

                    onCategoryUpdateCallback.onFailed(e.getMessage());
                    GlobalConfig.createSnackBar(getContext(), saveCategoryActionButton, "Category failed to update: " + e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    toggleProgress(false);
                    GlobalConfig.createSnackBar(getContext(), saveCategoryActionButton, "Category update succeeded! ", Snackbar.LENGTH_INDEFINITE);

                    onCategoryUpdateCallback.onSuccess();
                }
            });


        }else{
            GlobalConfig.createSnackBar(getContext(), containerLinearLayout, "Category must be added, add and try again", Snackbar.LENGTH_INDEFINITE);
        return;
        }
    }

    private void toggleProgress(boolean show) {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
    }
    public interface OnCategoryUpdateCallback{
        void onSuccess();
        void onFailed(String errorMessage);
    }

}