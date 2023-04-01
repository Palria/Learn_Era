package com.palria.learnera.adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.GlobalHelpers;
import com.palria.learnera.LibraryActivity;
import com.palria.learnera.R;
import com.palria.learnera.models.AuthorDataModel;
import com.palria.learnera.models.LibraryDataModel;
import com.palria.learnera.widgets.LEBottomSheetDialog;

import java.io.Serializable;
import java.util.ArrayList;

public class AllLibraryFragmentRcvAdapter extends RecyclerView.Adapter<AllLibraryFragmentRcvAdapter.ViewHolder> implements Serializable {

    ArrayList<LibraryDataModel> libraryDataModels;
    Context context;

    public AllLibraryFragmentRcvAdapter(ArrayList<LibraryDataModel> libraryDataModels, Context context) {
        this.libraryDataModels = libraryDataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public AllLibraryFragmentRcvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.all_library_fragment_list_item_library, parent, false);
        AllLibraryFragmentRcvAdapter.ViewHolder viewHolder = new AllLibraryFragmentRcvAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllLibraryFragmentRcvAdapter.ViewHolder holder, int position) {

        LibraryDataModel libraryDataModel = libraryDataModels.get(position);
        if (libraryDataModel.isPublic() || (GlobalConfig.getCurrentUserId().equals(libraryDataModel.getAuthorUserId()+""))) {

        holder.libraryName.setText(libraryDataModel.getLibraryName());
        Glide.with(context)
                .load(libraryDataModel.getLibraryCoverPhotoDownloadUrl())
                .centerCrop()
                .placeholder(R.drawable.book_cover)
                .into(holder.cover);
        holder.libraryViewCount.setText(libraryDataModel.getTotalNumberOfLibraryViews()+"");
        holder.moreActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LEBottomSheetDialog leBottomSheetDialog = new LEBottomSheetDialog(context);
                leBottomSheetDialog.addOptionItem("Block Library", R.drawable.ic_baseline_error_outline_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        leBottomSheetDialog.hide();
                        Toast.makeText(context,"Blocking",Toast.LENGTH_SHORT).show();
                        int position = libraryDataModels.indexOf(libraryDataModel);
                        libraryDataModels.remove(libraryDataModel);
                        AllLibraryFragmentRcvAdapter.this.notifyItemRemoved(position);
                        GlobalConfig.block(GlobalConfig.ACTIVITY_LOG_USER_BLOCK_LIBRARY_TYPE_KEY, libraryDataModel.getAuthorUserId(), libraryDataModel.getLibraryId(), null, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFailed(String errorMessage) {

                            }
                        });
                    }
                }, 0);
                leBottomSheetDialog.addOptionItem("Report Library", R.drawable.ic_baseline_error_outline_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        leBottomSheetDialog.hide();
                        Toast.makeText(context,"reporting",Toast.LENGTH_SHORT).show();
                        int position = libraryDataModels.indexOf(libraryDataModel);
                        libraryDataModels.remove(libraryDataModel);
                        AllLibraryFragmentRcvAdapter.this.notifyItemRemoved(position);
                        GlobalConfig.report(GlobalConfig.ACTIVITY_LOG_USER_REPORT_LIBRARY_TYPE_KEY, libraryDataModel.getAuthorUserId(), libraryDataModel.getLibraryId(), null, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFailed(String errorMessage) {

                            }
                        });
                    }
                }, 0);
                leBottomSheetDialog.render().show();
            }
        });

        int[] ratings =new int[5];
        ratings[0]= (int) libraryDataModel.getTotalNumberOfOneStarRate();
        ratings[1]= (int) libraryDataModel.getTotalNumberOfTwoStarRate();
        ratings[2]= (int) libraryDataModel.getTotalNumberOfThreeStarRate();
        ratings[3]= (int) libraryDataModel.getTotalNumberOfFourStarRate();
        ratings[4]= (int) libraryDataModel.getTotalNumberOfFiveStarRate();
        String averageRating= GlobalHelpers.calculateAverageRating(ratings);

        holder.ratingCount.setText(averageRating);
        String categories = "N/A";
        ArrayList<String> cats = libraryDataModel.getLibraryCategoryArrayList();
                      if(cats!=null) {
                          categories = "";
                          int j=0;
                          for (String cat : cats) {
                              if(j==cats.size()-1){
                                  categories += cat;
                              }else{
                                  categories += cat+", ";
                              }
                              j++;
                          }
                      }

        holder.libraryDescription.setText(libraryDataModel.getLibraryDescription());
        holder.tutorialsCount.setText(libraryDataModel.getTotalNumberOfTutorials() +"");
try {
    GlobalConfig.getFirebaseFirestoreInstance()
            .collection(GlobalConfig.ALL_USERS_KEY)
            .document(libraryDataModel.getAuthorUserId())
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    String userDisplayName = "" + documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                    holder.authorName.setText(userDisplayName);
                }
            });
}catch(Exception e){}

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(GlobalConfig.LIBRARY_DOCUMENT_SNAPSHOT_KEY, libraryDataModel);


                Intent intent = new Intent(context, LibraryActivity.class);
                intent.putExtra(GlobalConfig.LIBRARY_ID_KEY, libraryDataModel.getLibraryId());
                intent.putExtra(GlobalConfig.LIBRARY_AUTHOR_ID_KEY, libraryDataModel.getAuthorUserId());
                intent.putExtra(GlobalConfig.LIBRARY_DATA_MODEL_KEY, libraryDataModel);
                intent.putExtra(GlobalConfig.IS_FIRST_VIEW_KEY, false);
//                intent.putExtra(GlobalConfig.LIBRARY_DOCUMENT_SNAPSHOT_KEY, libraryDataModel.getLibraryDocumentSnapshot());
                context.startActivity(intent);
            }
        });
    }else{
        holder.libraryName.setText("Library is private");
        holder.itemView.setEnabled(false);
    }
    }

    @Override
    public int getItemCount() {
        return libraryDataModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView cover;
        public TextView libraryName;
        public TextView authorName;
        public TextView libraryViewCount;
        public TextView  tutorialsCount;
        public TextView ratingCount;
        public TextView libraryDescription;
        public ImageButton moreActionButton;


        public ViewHolder(View itemView) {
            super(itemView);
            this.cover = (ImageView) itemView.findViewById(R.id.cover);
            this.libraryName = (TextView) itemView.findViewById(R.id.libraryName);
            authorName=itemView.findViewById(R.id.authorName);
            libraryViewCount=itemView.findViewById(R.id.libraryViewCount);
            tutorialsCount=itemView.findViewById(R.id.tutorialsCount);
            ratingCount=itemView.findViewById(R.id.ratingCount);
            this.moreActionButton = itemView.findViewById(R.id.moreActionButtonId);
            libraryDescription=itemView.findViewById(R.id.libraryDescription);


        }
    }

    public void setDataList(ArrayList<LibraryDataModel> data){
        libraryDataModels.clear();
        libraryDataModels.addAll(data);
        this.notifyDataSetChanged();

    }

}
