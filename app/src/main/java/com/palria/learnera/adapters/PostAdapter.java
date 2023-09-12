package com.palria.learnera.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.palria.learnera.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//
//import com.bumptech.glide.Glide;
//import com.govance.businessprojectconfiguration.ProductDataModel;
//import com.makeramen.roundedimageview.RoundedImageView;
//import com.palria.learnera.GlobalConfig;
//import com.palria.learnera.LibraryActivity;
//import com.palria.learnera.PageActivity;
//import com.palria.learnera.R;
//import com.palria.learnera.TutorialActivity;
//import com.palria.learnera.TutorialFolderActivity;
//import com.palria.learnera.models.BookmarkDataModel;
//import com.palria.learnera.models.FolderDataModel;
//import com.palria.learnera.models.LibraryDataModel;
//import com.palria.learnera.models.TutorialDataModel;

public class PostAdapter {
//    extends RecyclerView.Adapter<UpdateAdapter.ViewHolder> {
//
//    ArrayList<UpdateDataModel> updateDataModels;
//    Context context;
//    ArrayList<UpdateDataModel> updateDataModelBackupArrayList = new ArrayList<>();
//
//    public PostAdapter(ArrayList<UpdateDataModel> updateDataModels, Context context) {
//        this.updateDataModels = updateDataModels;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public UpdateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View listItem= layoutInflater.inflate(R.layout.update_item_layout, parent, false);
//        UpdateAdapter.ViewHolder viewHolder = new UpdateAdapter.ViewHolder(listItem);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull UpdateAdapter.ViewHolder holder, int position) {
//        UpdateDataModel updateDataModel = updateDataModels.get(position);
//
//
////        if (!productDataModelBackupArrayList.contains(productDataModel)) {
//
//
//            holder.title.setText(updateDataModel.getTitle());
////        holder.datePosted.setText(productDataModel.getDatePosted());
////        holder.description.setText(productDataModel.getProductDescription());
//            holder.description.setText(updateDataModel.getDescription());
//            holder.viewCountTextView.setText(""+updateDataModel.getNumOfViews());
//            holder.datePosted.setText(""+ updateDataModel.getDatePosted());
//
//            if(updateDataModel.getImageUrlList().size() == 0){
//                holder.icon.setVisibility(View.GONE);
//            }else {
//                Picasso.get()
//                        .load(updateDataModel.getImageUrlList().get(0))
//                        .placeholder(R.drawable.agg_logo)
//                        .into(holder.icon);
//            }
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, SingleUpdateActivity.class);
//                    intent.putExtra(GlobalValue.UPDATE_ID, updateDataModel.getUpdateId());
//                    intent.putExtra(GlobalValue.UPDATE_DATA_MODEL, updateDataModel);
//                    intent.putExtra(GlobalValue.IS_EDITION, false);
//                    context.startActivity(intent);
//
//                }
//            });
//
//            if(GlobalValue.isBusinessOwner()){
//                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View view) {
//
//
//                        GlobalValue.createPopUpMenu(context, R.menu.update_menu, holder.itemView, new GlobalValue.OnMenuItemClickListener() {
//                            @Override
//                            public boolean onMenuItemClicked(MenuItem item) {
//                                if(item.getItemId() == R.id.deleteId){
//                                    GlobalValue.getFirebaseFirestoreInstance().collection(GlobalValue.PLATFORM_UPDATES).document(updateDataModel.getUpdateId()).delete();
//                                    int positionDeleted = updateDataModels.indexOf(updateDataModel);
//                                    updateDataModels.remove(updateDataModel);
//                                    notifyItemChanged(positionDeleted);
//
//                                    for(int i=0; i<updateDataModel.getImageUrlList().size(); i++){
//                                        FirebaseStorage.getInstance().getReferenceFromUrl(updateDataModel.getImageUrlList().get(i)).delete();
//                                    }
//
//                                }
//                                else  if(item.getItemId() == R.id.editId){
////not yet implemented for edition
//                                    Intent intent = new Intent(context, PostNewUpdateActivity.class);
//                                    intent.putExtra(GlobalValue.UPDATE_ID, updateDataModel.getUpdateId());
//                                    intent.putExtra(GlobalValue.UPDATE_DATA_MODEL, updateDataModel);
//                                    intent.putExtra(GlobalValue.IS_EDITION, true);
//                                    context.startActivity(intent);
//                                }
//
//
//                                return true;
//                            }
//                        });
//
//                        return false;
//                    }
//                });
//
//            }
//
//
//
////        else {
////            Toast.makeText(context, "Already bound!", Toast.LENGTH_SHORT).show();
////        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return updateDataModels.size();
//    }
//
//    class ViewHolder extends RecyclerView.ViewHolder{
//
//        public TextView title;
//        public TextView datePosted;
//        public TextView description;
//        public TextView viewCountTextView;
//        public ImageView icon;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            this.title =  itemView.findViewById(R.id.updateTitleTextViewId);
//            this.description =  itemView.findViewById(R.id.descriptionTextViewId);
//            this.viewCountTextView =  itemView.findViewById(R.id.viewCountTextViewId);
//            this.datePosted =  itemView.findViewById(R.id.datePostedTextViewId);
////            this.datePosted = (TextView) itemView.findViewById(R.id.datePosted);
////            description=itemView.findViewById(R.id.description);
//            icon = itemView.findViewById(R.id.updateImageViewId);
//
//        }
//    }

}

