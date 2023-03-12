package com.palria.learnera.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.LibraryActivity;
import com.palria.learnera.PageActivity;
import com.palria.learnera.R;
import com.palria.learnera.TutorialActivity;
import com.palria.learnera.TutorialFolderActivity;
import com.palria.learnera.models.BookmarkDataModel;
import com.palria.learnera.models.FolderDataModel;
import com.palria.learnera.models.LibraryDataModel;
import com.palria.learnera.models.TutorialDataModel;
import com.palria.learnera.models.UsersDataModel;
import com.palria.learnera.widgets.LEBottomSheetDialog;

import java.util.ArrayList;

    public class UsersRCVAdapter extends RecyclerView.Adapter<UsersRCVAdapter.ViewHolder> {

        ArrayList<UsersDataModel> userDataModelsList;
        Context context;

        public UsersRCVAdapter(ArrayList<UsersDataModel> userDataModelsList, Context context) {
            this.userDataModelsList = userDataModelsList;
            this.context = context;
        }

        @NonNull
        @Override
        public UsersRCVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.users_item_layout, parent, false);
            UsersRCVAdapter.ViewHolder viewHolder = new UsersRCVAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull UsersRCVAdapter.ViewHolder holder, int position) {

            UsersDataModel userDataModels = userDataModelsList.get(position);

            holder.userName.setText(userDataModels.getUserName());
            holder.dateCreated.setText(userDataModels.getDateRegistered());
//            holder.description.setText(userDataModels.getDescription());
            if(userDataModels.isAnAuthor()){
                holder.description.setText("Author");
            }else{
                holder.description.setText("User");
            }

            Glide.with(context)
                    .load(userDataModels.getUserProfileImageDownloadUrl())
                    .placeholder(R.drawable.default_profile)
                    .centerCrop()
                    .into(holder.icon);
            holder.moreActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LEBottomSheetDialog leBottomSheetDialog = new LEBottomSheetDialog(context);
                    leBottomSheetDialog.addOptionItem("Block User", R.drawable.ic_baseline_error_outline_24, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            leBottomSheetDialog.hide();
                            Toast.makeText(context,"Blocking",Toast.LENGTH_SHORT).show();
                            int position = userDataModelsList.indexOf(userDataModels);
                            userDataModelsList.remove(userDataModels);
                            UsersRCVAdapter.this.notifyItemRemoved(position);
                            GlobalConfig.block(GlobalConfig.ACTIVITY_LOG_USER_BLOCK_USER_TYPE_KEY, userDataModels.getUserId(), null, null, new GlobalConfig.ActionCallback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onFailed(String errorMessage) {

                                }
                            });
                        }
                    }, 0);
                    leBottomSheetDialog.addOptionItem("Report User", R.drawable.ic_baseline_error_outline_24, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            leBottomSheetDialog.hide();
                            Toast.makeText(context,"reporting",Toast.LENGTH_SHORT).show();
                            int position = userDataModelsList.indexOf(userDataModels);
                            userDataModelsList.remove(userDataModels);
                            UsersRCVAdapter.this.notifyItemRemoved(position);
                            GlobalConfig.report(GlobalConfig.ACTIVITY_LOG_USER_REPORT_USER_TYPE_KEY, userDataModels.getUserId(), null, null, new GlobalConfig.ActionCallback() {
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


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(GlobalConfig.getHostActivityIntent(context,null,GlobalConfig.USER_PROFILE_FRAGMENT_TYPE_KEY,userDataModels.getUserId()));

                }
            });

        }


        @Override
        public int getItemCount() {
            return userDataModelsList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            public TextView userName;
            public TextView dateCreated;
            public TextView description;
            public RoundedImageView icon;
            public ImageButton moreActionButton;

            public ViewHolder(View itemView) {
                super(itemView);
                this.userName =  itemView.findViewById(R.id.title);
                this.dateCreated = (TextView) itemView.findViewById(R.id.dateCreated);
                description=itemView.findViewById(R.id.description);
                this.moreActionButton = itemView.findViewById(R.id.moreActionButtonId);
                icon = itemView.findViewById(R.id.icon);

            }
        }


}
