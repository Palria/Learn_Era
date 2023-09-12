package com.palria.learnera.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.WriteBatch;
import com.makeramen.roundedimageview.RoundedImageView;
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.PageActivity;
import com.palria.learnera.R;
import com.palria.learnera.models.PageDataModel;
import com.palria.learnera.models.PageDiscussionDataModel;
import com.palria.learnera.widgets.BottomSheetFormBuilderWidget;

import java.util.ArrayList;
import java.util.HashMap;

public class PageDiscussionRcvAdapter extends RecyclerView.Adapter<PageDiscussionRcvAdapter.ViewHolder> {

    ArrayList<PageDiscussionDataModel> pageDiscussionDataModels;
    Context context;
    String authorId;
    boolean isTutorialPage;

    public PageDiscussionRcvAdapter(ArrayList<PageDiscussionDataModel> pageDiscussionDataModels, Context context,String authorId, boolean isTutorialPage){
        this.pageDiscussionDataModels = pageDiscussionDataModels;
        this.context = context;
        this.authorId = authorId;
        this.isTutorialPage = isTutorialPage;
    }

    @NonNull
    @Override
    public PageDiscussionRcvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.page_discussion_item_layout, parent, false);
        PageDiscussionRcvAdapter.ViewHolder viewHolder = new PageDiscussionRcvAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PageDiscussionRcvAdapter.ViewHolder holder, int position) {
        PageDiscussionDataModel pageDiscussionDataModel = pageDiscussionDataModels.get(position);

//        if (pageDiscussionDataModel.isHiddenByAuthor() || pageDiscussionDataModel.isHiddenByPoster() || (GlobalConfig.getCurrentUserId().equals(pageDiscussionDataModel.getDiscussionPosterId()+""))) {
        if (true) {

            holder.dateCreatedTextView.setText(pageDiscussionDataModel.getDateCreated());

            GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY)
                    .document(pageDiscussionDataModel.getDiscussionPosterId())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            final String userName = ""+ documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                            final String userProfilePhotoDownloadUrl = ""+ documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);

                            holder.posterNameTextView.setText(userName);
                            try {
                                Glide.with(context)
                                        .load(userProfilePhotoDownloadUrl)
                                        .centerCrop()
                                        .placeholder(R.drawable.placeholder)
                                        .into(holder.discussionPosterProfilePhoto);
                            } catch (Exception ignored) {
                            }
                        }
                    });

            holder.discussionDescriptionTextView.setText(pageDiscussionDataModel.getDescription());
            holder.discussionCountTextView.setText(pageDiscussionDataModel.getTotalReplies() + "");
            if(pageDiscussionDataModel.getTotalReplies()==1){
                holder.viewDiscussionsTextView.setText("See " + pageDiscussionDataModel.getTotalReplies() + " reply");
            }else {
                holder.viewDiscussionsTextView.setText("See " + pageDiscussionDataModel.getTotalReplies() + " replies");
            }
            holder.likeCountTextView.setText(pageDiscussionDataModel.getTotalLikes() + "");
/*
            DocumentReference likedDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.LIKED_DISCUSSIONS_KEY).document(pageDiscussionDataModel.getDiscussionId());
            GlobalConfig.checkIfDocumentExists(likedDocumentReference, new GlobalConfig.OnDocumentExistStatusCallback() {
                @Override
                public void onExist(DocumentSnapshot documentSnapshot) {
                    holder.likeActionButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_baseline_thumb_up_24,context.getTheme()));
                }

                @Override
                public void onNotExist() {
                    holder.likeActionButton.setImageResource(R.drawable.ic_outline_thumb_up_24);
                }

                @Override
                public void onFailed(@NonNull String errorMessage) {

                }
            });
            */
//            if(GlobalConfig.isPageDiscussionLiked(context,pageDiscussionDataModel.getDiscussionId())){
            if(pageDiscussionDataModel.getLikersIdList().contains(GlobalConfig.getCurrentUserId())){
                holder.likeActionButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_baseline_thumb_up_24,context.getTheme()));
            }else{
                holder.likeActionButton.setImageResource(R.drawable.ic_outline_thumb_up_24);
            }
//            holder.disLikeAccountActionButton.setText(pageDiscussionDataModel.getTotalDisLikes() + "");
            if(pageDiscussionDataModel.getTotalReplies() <=0){
                holder.viewDiscussionsTextView.setVisibility(View.GONE);
            }
            holder.moreActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GlobalConfig.createPopUpMenu(context,R.menu.page_discussion_action_menu , holder.moreActionButton, new GlobalConfig.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClicked(MenuItem item) {

                            if(item.getItemId() == R.id.deleteId){

                                GlobalConfig.deleteDiscussion(pageDiscussionDataModel.getDiscussionId(), pageDiscussionDataModel.getParentDiscussionId(), pageDiscussionDataModel.getPageId(), pageDiscussionDataModel.getTutorialId(), pageDiscussionDataModel.getFolderId(), pageDiscussionDataModel.getAuthorId(), pageDiscussionDataModel.isTutorialPage(), pageDiscussionDataModel.hasParentDiscussion(), new GlobalConfig.ActionCallback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onFailed(String errorMessage) {

                                            }
                                        });
                                int positionDeleted = pageDiscussionDataModels.indexOf(pageDiscussionDataModel);
                                pageDiscussionDataModels.remove(pageDiscussionDataModel);
                                notifyItemRemoved(positionDeleted);
                            }

                            return true;
                        }
                    });
                }
            });
            holder.viewDiscussionsTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent();
                    intent.putExtra(GlobalConfig.IS_FROM_PAGE_CONTEXT_KEY,false);
                    intent.putExtra(GlobalConfig.IS_VIEW_ALL_DISCUSSIONS_FOR_SINGLE_PAGE_KEY,false);
                    intent.putExtra(GlobalConfig.IS_VIEW_SINGLE_DISCUSSION_REPLY_KEY,true);
                    intent.putExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,isTutorialPage);
                    intent.putExtra(GlobalConfig.AUTHOR_ID_KEY,pageDiscussionDataModel.getAuthorId());
                    intent.putExtra(GlobalConfig.PARENT_DISCUSSION_ID_KEY,pageDiscussionDataModel.getDiscussionId());
                    intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY,pageDiscussionDataModel.getTutorialId());
                    intent.putExtra(GlobalConfig.FOLDER_ID_KEY,pageDiscussionDataModel.getFolderId());
                    intent.putExtra(GlobalConfig.PAGE_ID_KEY,pageDiscussionDataModel.getPageId());
                    context.startActivity(GlobalConfig.getHostActivityIntent(context,intent,GlobalConfig.DISCUSSION_FRAGMENT_TYPE_KEY,""));


                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent();
                    intent.putExtra(GlobalConfig.IS_FROM_PAGE_CONTEXT_KEY,false);
                    intent.putExtra(GlobalConfig.IS_VIEW_ALL_DISCUSSIONS_FOR_SINGLE_PAGE_KEY,false);
                    intent.putExtra(GlobalConfig.IS_VIEW_SINGLE_DISCUSSION_REPLY_KEY,true);
                    intent.putExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,isTutorialPage);
                    intent.putExtra(GlobalConfig.AUTHOR_ID_KEY,pageDiscussionDataModel.getAuthorId());
                    intent.putExtra(GlobalConfig.PARENT_DISCUSSION_ID_KEY,pageDiscussionDataModel.getDiscussionId());
                    intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY,pageDiscussionDataModel.getTutorialId());
                    intent.putExtra(GlobalConfig.FOLDER_ID_KEY,pageDiscussionDataModel.getFolderId());
                    intent.putExtra(GlobalConfig.PAGE_ID_KEY,pageDiscussionDataModel.getPageId());
                    context.startActivity(GlobalConfig.getHostActivityIntent(context,intent,GlobalConfig.DISCUSSION_FRAGMENT_TYPE_KEY,""));


                }
            });

            holder.likeActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int currentLikesCount = Integer.parseInt((holder.likeCountTextView.getText()+""));

                    holder.likeActionButton.setEnabled(false);
                    if(pageDiscussionDataModel.getLikersIdList().contains(GlobalConfig.getCurrentUserId())){
                        if(!(holder.likeCountTextView.getText()+"").equals("0")) {
                            holder.likeCountTextView.setText((currentLikesCount - 1) + "");
                        }


                        holder.likeActionButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_outline_thumb_up_24,context.getTheme()));
                        GlobalConfig.likePageDiscussion(context,pageDiscussionDataModel,pageDiscussionDataModel.getDiscussionId(), pageDiscussionDataModel.getPageId(), pageDiscussionDataModel.getTutorialId(), pageDiscussionDataModel.getFolderId(), pageDiscussionDataModel.getAuthorId(), isTutorialPage, false, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {
                                holder.likeActionButton.setEnabled(true);

                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                holder.likeActionButton.setEnabled(true);

                            }
                        });

                    }else{
                        holder.likeCountTextView.setText((currentLikesCount+1)+"");
                        holder.likeActionButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_baseline_thumb_up_24,context.getTheme()));
                        GlobalConfig.likePageDiscussion(context,pageDiscussionDataModel,pageDiscussionDataModel.getDiscussionId(),pageDiscussionDataModel.getPageId(), pageDiscussionDataModel.getTutorialId(), pageDiscussionDataModel.getFolderId(), pageDiscussionDataModel.getAuthorId(), isTutorialPage, true, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {
                                holder.likeActionButton.setEnabled(true);

                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                holder.likeActionButton.setEnabled(true);

                            }
                        });

                    }
//                    DocumentReference likedDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.LIKED_DISCUSSIONS_KEY).document(pageDiscussionDataModel.getDiscussionId());
//                    GlobalConfig.checkIfDocumentExists(likedDocumentReference, new GlobalConfig.OnDocumentExistStatusCallback() {
//                        @Override
//                        public void onExist(DocumentSnapshot documentSnapshot) {
//
//
//                        }
//
//                        @Override
//                        public void onNotExist() {
//                              }
//
//                        @Override
//                        public void onFailed(@NonNull String errorMessage) {
//                            holder.likeActionButton.setEnabled(true);
//
//                        }
//                    });



                }
            });

            holder.discussActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDiscussionForm(holder,pageDiscussionDataModel);
                }
            });



        }
        else{
            holder.discussionDescriptionTextView.setText("Discussion is hidden");
            holder.itemView.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return pageDiscussionDataModels.size();
    }
    void showDiscussionForm(ViewHolder holder, PageDiscussionDataModel pageDiscussionDataModel){
        new BottomSheetFormBuilderWidget(context)
                .setTitle("Join in discussion of this lesson by contributing your idea")
                .setPositiveTitle("Discuss")
                .addInputField(new BottomSheetFormBuilderWidget.EditTextInput(context)
                        .setHint("Enter your idea")
                        .autoFocus())
                .setOnSubmit(new BottomSheetFormBuilderWidget.OnSubmitHandler() {
                    @Override
                    public void onSubmit(String[] values) {
                        super.onSubmit(values);

//                         Toast.makeText(PageActivity.this, values[0], Toast.LENGTH_SHORT).show();
                        //values will be returned as array of strings as per input list position
                        //eg first added input has first value
                        String body = values[0];
                        if (body.trim().equals("")) {
                            //     leBottomSheetDialog.setTitle("Folder needs name, must enter name for the folder");

                            Toast.makeText(context, "Please enter your idea",Toast.LENGTH_SHORT).show();

                        } else {

                            String discussionId = GlobalConfig.getRandomString(80);
                            GlobalConfig.createSnackBar(context,holder.discussActionButton, "Adding discussion: "+body,Snackbar.LENGTH_INDEFINITE);

                            GlobalConfig.discuss(new PageDiscussionDataModel(discussionId,GlobalConfig.getCurrentUserId(),body,"",pageDiscussionDataModel.getPageId(),pageDiscussionDataModel.getAuthorId(),pageDiscussionDataModel.getDiscussionId(),pageDiscussionDataModel.getTutorialId(),pageDiscussionDataModel.getFolderId(),isTutorialPage,true,false,false,false,"",0L,0L,new ArrayList(),new ArrayList()),new GlobalConfig.ActionCallback(){
                                @Override
                                public void onFailed(String errorMessage){
                                    Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();

                                }
                                @Override
                                public void onSuccess(){
//                                     Toast.makeText(PageActivity.this, body, Toast.LENGTH_SHORT).show();
                                    GlobalConfig.createSnackBar(context,holder.discussActionButton, "Thanks discussion added: "+body,Snackbar.LENGTH_SHORT);
                                    int currentDiscussionCount = Integer.parseInt((holder.discussionCountTextView.getText()+""));
                                    holder.discussionCountTextView.setText((currentDiscussionCount+1)+"");

                                }
                            });
//                             createNewFolder(values[0],isPublic[0]);
//                                           leBottomSheetDialog.setTitle("Creating "+values[0]+" folder in progress...");
//                                           leBottomSheetDialog.hide();
                        }
                        //create folder process here
                    }
                })
                .render()
                .show();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public RoundedImageView discussionPosterProfilePhoto;
        public TextView posterNameTextView;
        public ImageView verificationFlagImageView;
        public TextView discussionDescriptionTextView;
        public TextView dateCreatedTextView;
        public ImageButton moreActionButton;
        public Button likeAccountActionButton;
        public Button disLikeAccountActionButton;
        public Button replyAccountActionButton;

        public ImageView likeActionButton;
        public ImageView discussActionButton;
        public TextView likeCountTextView;
        public TextView discussionCountTextView;
        public TextView viewDiscussionsTextView;



        public ViewHolder(View itemView) {
            super(itemView);
            this.discussionPosterProfilePhoto = itemView.findViewById(R.id.discussionPosterProfilePhotoId);
            this.posterNameTextView =  itemView.findViewById(R.id.posterNameTextViewId);
            this.verificationFlagImageView = itemView.findViewById(R.id.verificationFlagImageViewId);
            discussionDescriptionTextView=itemView.findViewById(R.id.discussionDescriptionTextViewId);
            moreActionButton=itemView.findViewById(R.id.moreActionButtonId);
            dateCreatedTextView=itemView.findViewById(R.id.dateCreatedTextViewId);
            likeAccountActionButton=itemView.findViewById(R.id.likeAccountActionButtonId);
            disLikeAccountActionButton=itemView.findViewById(R.id.disLikeAccountActionButtonId);
            replyAccountActionButton=itemView.findViewById(R.id.replyAccountActionButtonId);

            likeActionButton=itemView.findViewById(R.id.likeActionButtonId);
            likeCountTextView=itemView.findViewById(R.id.likeCountTextViewId);

            discussActionButton=itemView.findViewById(R.id.discussActionButtonId);
            discussionCountTextView=itemView.findViewById(R.id.discussionCountTextViewId);
            viewDiscussionsTextView=itemView.findViewById(R.id.viewDiscussionsTextViewId);



        }
    }

}
