package com.palria.learnera.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.WriteBatch;
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.PageActivity;
import com.palria.learnera.R;
import com.palria.learnera.models.PageDataModel;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryRcvAdapter /*extends RecyclerView.Adapter<CategoryRcvAdapter.ViewHolder> */ {
//
//    ArrayList<PageDataModel> pageDataModels;
//    Context context;
//    Button paginateButton;
//    HashMap<String,Integer> pageNums = new HashMap<>();
//    ArrayList<String>pageIdList = new ArrayList<>();
//    ArrayList<String>categoryList = new ArrayList<>();
//    OnPaginationCallback onPaginationCallback;
//    AlertDialog alertDialog;
//
//    public CategoryRcvAdapter(ArrayList<PageDataModel> pageDataModels, Context context, Button paginateButton, boolean isTutorialPage, long numberOfPagesCreated, boolean isPagination, OnPaginationCallback onPaginationCallback) {
//        this.pageDataModels = pageDataModels;
//        this.context = context;
//        this.paginateButton = paginateButton;
//        this.isTutorialPage = isTutorialPage;
//        this.numberOfPagesCreated = numberOfPagesCreated;
//        this.isPagination = isPagination;
//        this.onPaginationCallback = onPaginationCallback;
//        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        alertDialog = new AlertDialog.Builder(context)
//                .setCancelable(false)
//                .setView(layoutInflater.inflate(R.layout.default_loading_layout,null))
//                .create();
//    }
//
//    @NonNull
//    @Override
//    public CategoryRcvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View listItem= layoutInflater.inflate(R.layout.all_page_item_layout, parent, false);
//        CategoryRcvAdapter.ViewHolder viewHolder = new CategoryRcvAdapter.ViewHolder(listItem);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CategoryRcvAdapter.ViewHolder holder, int position) {
//        PageDataModel pageDataModel = pageDataModels.get(position);
//
//            if (!pageIdList.contains(pageDataModel.getPageId())) {
//                pageIdList.add(pageDataModel.getPageId());
//                holder.categoryNumberEditText.setText(pageDataModel.getPageNumber() + "");
////                pageNums.put(pageDataModel.getPageId(), Integer.valueOf(pageDataModel.getPageNumber()));
//
//            }
//            holder.categoryTitle.setText(pageDataModel.getTitle());
//
//
//
//
//
//                holder.categoryNumberEditText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                        try {
////                            pageNums.put(pageDataModel.getPageId(), Integer.valueOf(holder.pageNumberEditText.getText().toString().trim().equals("") ? "0" : holder.pageNumberEditText.getText() + ""));
//
//                            categoryList.remove(Integer.valueOf(holder.categoryNumberEditText.getText().toString().trim().equals("") ? "0" : holder.categoryNumberEditText.getText() + ""));)
//                            categoryList.add(,holder.categoryTitle);
//
//                        } catch (Exception ignored) {
//                        }
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//
//                    }
//                });
//
//
//            paginateButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
//                    for (int i = 0; i < pageIdList.size(); i++) {
//                        DocumentReference documentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(pageDataModel.getTutorialId()).collection(GlobalConfig.ALL_TUTORIAL_PAGES_KEY).document(pageIdList.get(i));
//
//                        long pageNumber = pageNums.get(pageIdList.get(i));
//                        if (pageNumber <= 0) {
//                            GlobalConfig.createSnackBar(context, paginateButton, "Page number must be greater than 0,correct the page numbers and try again", Snackbar.LENGTH_INDEFINITE);
//                            paginateButton.setEnabled(true);
//                            return;
//                        }
//
//                        HashMap<String, Object> numberDetail = new HashMap<>();
//                        numberDetail.put(GlobalConfig.PAGE_NUMBER_KEY, pageNumber);
//                        writeBatch.update(documentReference, numberDetail);
//
//                    }
//                    toggleProgress(true);
//
//                    writeBatch.commit().addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            toggleProgress(false);
//
//                            onPaginationCallback.onFailed(e.getMessage());
//                            GlobalConfig.createSnackBar(context, paginateButton, "Your pagination failed: " + e.getMessage(), Snackbar.LENGTH_INDEFINITE);
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            toggleProgress(false);
//                            GlobalConfig.createSnackBar(context, paginateButton, "Your pagination succeeded! ", Snackbar.LENGTH_INDEFINITE);
//
//                            onPaginationCallback.onSuccess();
//                        }
//                    });
//
//                }
//            });
//
//
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return pageDataModels.size();
//    }
//
//    class ViewHolder extends RecyclerView.ViewHolder{
//
//        public TextView categoryTitle;
//        public EditText categoryNumberEditText;
//
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            this.categoryTitle =  itemView.findViewById(R.id.categoryTitleId);
//            this.categoryNumberEditText =  itemView.findViewById(R.id.categoryNumberEditTextId);
//
//
//        }
//    }
//    private void toggleProgress(boolean show) {
//        if(show){
//            alertDialog.show();
//        }else{
//            alertDialog.cancel();
//        }
//    }
//
//    public interface OnPaginationCallback{
//        void onSuccess();
//        void onFailed(String errorMessage);
//}

}
