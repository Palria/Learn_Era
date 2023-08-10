package com.palria.learnera.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.HashBiMap;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.WriteBatch;
import com.makeramen.roundedimageview.RoundedImageView;
import com.palria.learnera.DeclineUserAccountVerificationActivity;
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.LibraryActivity;
import com.palria.learnera.MainActivity;
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
import java.util.HashMap;

public class UsersRCVAdapter extends RecyclerView.Adapter<UsersRCVAdapter.ViewHolder> {

        ArrayList<UsersDataModel> userDataModelsList;
        Context context;
        boolean isVerification;

        public UsersRCVAdapter(ArrayList<UsersDataModel> userDataModelsList, Context context, boolean isVerification) {
            this.userDataModelsList = userDataModelsList;
            this.context = context;
            this.isVerification = isVerification;
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
            if(userDataModels.getUserId().equals(GlobalConfig.getCurrentUserId())){
                holder.followActionTextView.setVisibility(View.GONE);
            }
            if(GlobalConfig.isFollowing(context,userDataModels.getUserId())){
                holder.followActionTextView.setText("Following");

            }else{
                holder.followActionTextView.setText("Follow");
            }
            holder.followActionTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.followActionTextView.setEnabled(false);
                    if(GlobalConfig.isFollowing(context,userDataModels.getUserId())){
                        GlobalConfig.unFollowUser(context, userDataModels.getUserId(), new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {
                                holder.followActionTextView.setEnabled(true);
                                holder.followActionTextView.setText("Follow");

                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                holder.followActionTextView.setEnabled(true);

                            }
                        });
                    }else{
                        GlobalConfig.followUser(context, userDataModels.getUserId(), new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {
                                holder.followActionTextView.setEnabled(true);
                                holder.followActionTextView.setText("Following");

                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                holder.followActionTextView.setEnabled(true);

                            }
                        });
                    }
                }
            });
            Glide.with(context)
                    .load(userDataModels.getUserProfileImageDownloadUrl())
                    .placeholder(R.drawable.default_profile)
                    .centerCrop()
                    .into(holder.icon);
            if(!userDataModels.getUserId().equals(GlobalConfig.getCurrentUserId())) {
                holder.moreActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LEBottomSheetDialog leBottomSheetDialog = new LEBottomSheetDialog(context);
                        leBottomSheetDialog.addOptionItem("Block User", R.drawable.ic_baseline_error_outline_24, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                leBottomSheetDialog.hide();
                                Toast.makeText(context, "Blocking", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(context, "reporting", Toast.LENGTH_SHORT).show();
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
            }else{
                holder.moreActionButton.setVisibility(View.INVISIBLE);
            }

            if(isVerification && GlobalConfig.isLearnEraAccount() && !userDataModels.isAccountVerified()){
                holder.verifyActionButton.setVisibility(View.VISIBLE);
                holder.declineActionButton.setVisibility(View.VISIBLE);
            }
            if(userDataModels.isAccountVerified()){
                holder.verificationFlagImageView.setVisibility(View.VISIBLE);
            }
            holder.verifyActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   verifyUserAccount(userDataModels,holder.verifyActionButton);
                }
            });
            holder.declineActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DeclineUserAccountVerificationActivity.class);
                    i.putExtra(GlobalConfig.USER_ID_KEY,userDataModels.getUserId());
                    i.putExtra(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY,userDataModels.getUserProfileImageDownloadUrl());
                    i.putExtra(GlobalConfig.USER_DISPLAY_NAME_KEY,userDataModels.getUserName());
                    context.startActivity(i);
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
            public ImageView verificationFlagImageView;
            public Button verifyActionButton;
            public Button declineActionButton;
            public TextView followActionTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                this.userName =  itemView.findViewById(R.id.title);
                this.dateCreated = (TextView) itemView.findViewById(R.id.dateCreated);
                description=itemView.findViewById(R.id.description);
                this.moreActionButton = itemView.findViewById(R.id.moreActionButtonId);
                icon = itemView.findViewById(R.id.icon);
                verifyActionButton = itemView.findViewById(R.id.verifyAccountActionButtonId);
                declineActionButton = itemView.findViewById(R.id.declineActionButtonId);
                verificationFlagImageView = itemView.findViewById(R.id.verificationFlagImageViewId);
                followActionTextView = itemView.findViewById(R.id.followActionTextViewId);

            }
        }

        void verifyUserAccount(UsersDataModel userDataModels,TextView textView ){
            WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
            DocumentReference userDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_USERS_KEY)
                    .document(userDataModels.getUserId());
            HashMap<String,Object> verifyDetails = new HashMap<>();
            verifyDetails.put(GlobalConfig.IS_ACCOUNT_VERIFIED_KEY,true);
            verifyDetails.put(GlobalConfig.IS_ACCOUNT_VERIFICATION_SEEN_KEY,false);
            verifyDetails.put(GlobalConfig.IS_SUBMITTED_FOR_VERIFICATION_KEY,false);
            verifyDetails.put(GlobalConfig.IS_ACCOUNT_VERIFICATION_DECLINED_KEY,false);
            verifyDetails.put(GlobalConfig.DATE_VERIFIED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
            writeBatch.update(userDocumentReference,verifyDetails);

            textView.setEnabled(false);
            textView.setText("Verifying");
            writeBatch.commit().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    textView.setText("Retry");
                    textView.setEnabled(true);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           verifyUserAccount(userDataModels,textView);
                        }
                    });

                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    textView.setText("Verified");
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            userDataModels.setIsAccountVerified(true);
                        }
                    });

                }
            });
        }


}
