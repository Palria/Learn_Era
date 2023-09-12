package com.palria.learnera.adapters;

import static com.palria.learnera.GlobalConfig.LIBRARY_AUTHOR_ID_KEY;
import static com.palria.learnera.GlobalConfig.LIBRARY_ID_KEY;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.GlobalHelpers;
import com.palria.learnera.LibraryActivity;
import com.palria.learnera.PageActivity;
import com.palria.learnera.R;
import com.palria.learnera.models.LibraryDataModel;
import com.palria.learnera.models.PageDataModel;
import com.palria.learnera.models.PageDiscussionDataModel;
import com.palria.learnera.widgets.BottomSheetFormBuilderWidget;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class PagesRcvAdapter extends RecyclerView.Adapter<PagesRcvAdapter.ViewHolder> {

    ArrayList<PageDataModel> pageDataModels;
    Context context;
    Button paginateButton;
    boolean isTutorialPage;
    long numberOfPagesCreated;
    boolean isPagination;
    HashMap<String,Integer> pageNums = new HashMap<>();
    ArrayList<String>pageIdList = new ArrayList<>();
    OnPaginationCallback onPaginationCallback;
    AlertDialog alertDialog;

    public PagesRcvAdapter(ArrayList<PageDataModel> pageDataModels, Context context, Button paginateButton,boolean isTutorialPage,long numberOfPagesCreated,boolean isPagination,OnPaginationCallback onPaginationCallback) {
        this.pageDataModels = pageDataModels;
        this.context = context;
        this.paginateButton = paginateButton;
        this.isTutorialPage = isTutorialPage;
        this.numberOfPagesCreated = numberOfPagesCreated;
        this.isPagination = isPagination;
        this.onPaginationCallback = onPaginationCallback;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        alertDialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(layoutInflater.inflate(R.layout.default_loading_layout,null))
                .create();
    }

    @NonNull
    @Override
    public PagesRcvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.all_page_item_layout, parent, false);
        PagesRcvAdapter.ViewHolder viewHolder = new PagesRcvAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PagesRcvAdapter.ViewHolder holder, int position) {
        PageDataModel pageDataModel = pageDataModels.get(position);

        if (pageDataModel.isPublic() || (GlobalConfig.getCurrentUserId().equals(pageDataModel.getAuthorId()+""))) {
            if (!pageIdList.contains(pageDataModel.getPageId())) {
                pageIdList.add(pageDataModel.getPageId());
                holder.pageNumberEditText.setText(pageDataModel.getPageNumber() + "");
                pageNums.put(pageDataModel.getPageId(), Integer.valueOf(pageDataModel.getPageNumber()));

            }
//        holder.tutorialsContainer.setVisibility(View.GONE);
            holder.dateContainer.setVisibility(View.VISIBLE);
            holder.dateCreated.setText(pageDataModel.getDateCreated());//view counts here

            holder.pageTitle.setText(pageDataModel.getTitle());
            holder.likeCountTextView.setText(pageDataModel.getLikeCount()+"");

            if(pageDataModel.getDiscussionCount()>0) {
                if (pageDataModel.getDiscussionCount() == 1) {
                    holder.viewDiscussionsTextView.setText("See " + pageDataModel.getDiscussionCount() + " Discussion");
                } else {
                    holder.viewDiscussionsTextView.setText("See " + pageDataModel.getDiscussionCount() + " Discussions");
                }
            }
            holder.discussionCountTextView.setText(pageDataModel.getDiscussionCount()+"");
/*
            DocumentReference likedDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.LIKED_PAGES_KEY).document(pageDataModel.getPageId());
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

            if(GlobalConfig.isPageLiked(context,pageDataModel.getPageId())){
                holder.likeActionButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_baseline_thumb_up_24,context.getTheme()));
            }else{
                holder.likeActionButton.setImageResource(R.drawable.ic_outline_thumb_up_24);
            }

            try {
                Glide.with(context)
                        .load(pageDataModel.getCoverDownloadUrl())
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .into(holder.cover);
            } catch (Exception ignored) {
            }


            holder.pageCaptionTextView.setText(pageDataModel.getDescription());
            holder.pageViewCount.setText(pageDataModel.getPageViews() + "");
            ColorDrawable white = new ColorDrawable();
            white.setColor(context.getResources().getColor(R.color.white, context.getTheme()));
//        GlobalConfig.getFirebaseFirestoreInstance()
//                .collection(GlobalConfig.ALL_USERS_KEY)
//                .document(pageDataModel.getAuthorUserId())
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                        String userDisplayName = "" + documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
//                        holder.authorName.setText(userDisplayName);
//                    }
//                });


            if (isPagination) {
                //    holder.pageNumberEditText.setVisibility(View.VISIBLE);
                holder.pageNumberEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        try {
                            pageNums.put(pageDataModel.getPageId(), Integer.valueOf(holder.pageNumberEditText.getText().toString().trim().equals("") ? "0" : holder.pageNumberEditText.getText() + ""));
                        } catch (Exception ignored) {
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

            } else {
                holder.pageNumberEditText.setEnabled(false);
                holder.pageNumberEditText.setLongClickable(false);
                holder.pageNumberEditText.setClickable(true);
//                holder.pageNumberEditText.setBackground(white);


            }
            paginateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
                    for (int i = 0; i < pageIdList.size(); i++) {
                        DocumentReference documentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(pageDataModel.getTutorialId()).collection(GlobalConfig.ALL_TUTORIAL_PAGES_KEY).document(pageIdList.get(i));
                        if (!isTutorialPage) {
                            documentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(pageDataModel.getTutorialId()).collection(GlobalConfig.ALL_FOLDERS_KEY).document(pageDataModel.getFolderId()).collection(GlobalConfig.ALL_FOLDER_PAGES_KEY).document(pageIdList.get(i));

                        }
                        long pageNumber = pageNums.get(pageIdList.get(i));
                        if (pageNumber <= 0) {
                            GlobalConfig.createSnackBar(context, paginateButton, "Page number must be greater than 0,correct the page numbers and try again", Snackbar.LENGTH_INDEFINITE);
                            paginateButton.setEnabled(true);
                            return;
                        }

                        HashMap<String, Object> numberDetail = new HashMap<>();
                        numberDetail.put(GlobalConfig.PAGE_NUMBER_KEY, pageNumber);
                        writeBatch.update(documentReference, numberDetail);

                    }
                    toggleProgress(true);

                    writeBatch.commit().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toggleProgress(false);

                            onPaginationCallback.onFailed(e.getMessage());
                            GlobalConfig.createSnackBar(context, paginateButton, "Your pagination failed: " + e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            toggleProgress(false);
                            GlobalConfig.createSnackBar(context, paginateButton, "Your pagination succeeded! ", Snackbar.LENGTH_INDEFINITE);

                            onPaginationCallback.onSuccess();
                        }
                    });

                }
            });



            holder.viewDiscussionsTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent();
                    intent.putExtra(GlobalConfig.IS_FROM_PAGE_CONTEXT_KEY,false);
                    intent.putExtra(GlobalConfig.IS_VIEW_ALL_DISCUSSIONS_FOR_SINGLE_PAGE_KEY,true);
                    intent.putExtra(GlobalConfig.IS_VIEW_SINGLE_DISCUSSION_REPLY_KEY,false);
                    intent.putExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,isTutorialPage);
                    intent.putExtra(GlobalConfig.AUTHOR_ID_KEY,pageDataModel.getAuthorId());
                    intent.putExtra(GlobalConfig.PARENT_DISCUSSION_ID_KEY,"");
                    intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY,pageDataModel.getTutorialId());
                    intent.putExtra(GlobalConfig.FOLDER_ID_KEY,pageDataModel.getFolderId());
                    intent.putExtra(GlobalConfig.PAGE_ID_KEY,pageDataModel.getPageId());
                    context.startActivity(GlobalConfig.getHostActivityIntent(context,intent,GlobalConfig.DISCUSSION_FRAGMENT_TYPE_KEY,""));

                }
            });
            holder.likeActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                            int currentLikesCount = Integer.parseInt((holder.likeCountTextView.getText()+""));

                            holder.likeActionButton.setEnabled(false);
                            DocumentReference likedDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.LIKED_PAGES_KEY).document(pageDataModel.getPageId());
                            GlobalConfig.checkIfDocumentExists(likedDocumentReference, new GlobalConfig.OnDocumentExistStatusCallback() {
                                @Override
                                public void onExist(DocumentSnapshot documentSnapshot) {
                                    if(!(holder.likeCountTextView.getText()+"").equals("0")) {
                                        holder.likeCountTextView.setText((currentLikesCount - 1) + "");
                                    }
                                    holder.likeActionButton.setImageResource(R.drawable.ic_outline_thumb_up_24);
                                    GlobalConfig.likePage(context,pageDataModel,pageDataModel.getPageId(), pageDataModel.getTutorialId(), pageDataModel.getFolderId(), pageDataModel.getAuthorId(), isTutorialPage, false, new GlobalConfig.ActionCallback() {
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

                                @Override
                                public void onNotExist() {
                                    holder.likeCountTextView.setText((currentLikesCount+1)+"");
                                    holder.likeActionButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_baseline_thumb_up_24,context.getTheme()));
                                    GlobalConfig.likePage(context,pageDataModel,pageDataModel.getPageId(), pageDataModel.getTutorialId(), pageDataModel.getFolderId(), pageDataModel.getAuthorId(), isTutorialPage, true, new GlobalConfig.ActionCallback() {
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

                                @Override
                                public void onFailed(@NonNull String errorMessage) {
                                    holder.likeActionButton.setEnabled(true);

                                }
                            });



                }
            });

            holder.discussActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDiscussionForm(holder,pageDataModel);
                }
            });



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//
                    Intent intent = new Intent(context, PageActivity.class);
                    intent.putExtra(GlobalConfig.PAGE_ID_KEY, pageDataModel.getPageId());
                    intent.putExtra(GlobalConfig.FOLDER_ID_KEY, pageDataModel.getFolderId());
                    intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY, pageDataModel.getTutorialId());
                    intent.putExtra(GlobalConfig.AUTHOR_ID_KEY, pageDataModel.getAuthorId());
                    intent.putExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY, pageDataModel.isTutorialPage());
                    intent.putExtra(GlobalConfig.TOTAL_NUMBER_OF_PAGES_CREATED_KEY, numberOfPagesCreated);
                    context.startActivity(intent);

                }
            });


        }
        else{
            holder.pageTitle.setText("Page is private");
            holder.itemView.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return pageDataModels.size();
    }
    void showDiscussionForm(ViewHolder holder,PageDataModel pageDataModel){
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

                            GlobalConfig.discuss(new PageDiscussionDataModel(discussionId,GlobalConfig.getCurrentUserId(),body,"",pageDataModel.getPageId(),pageDataModel.getAuthorId(),"",pageDataModel.getTutorialId(),pageDataModel.getFolderId(),isTutorialPage,false,false,false,false,"",0L,0L,new ArrayList(),new ArrayList()),new GlobalConfig.ActionCallback(){
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

        public ImageView cover;
        public TextView pageTitle;
//        public TextView authorName;
        public TextView pageViewCount;
//        public TextView ratingCount;
        public TextView pageCaptionTextView;

        public LinearLayout dateContainer;
        public LinearLayout tutorialsContainer;
        public TextView dateCreated;
        public EditText pageNumberEditText;

        public ImageView likeActionButton;
        public ImageView discussActionButton;
        public TextView likeCountTextView;
        public TextView discussionCountTextView;
        public TextView viewDiscussionsTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            this.cover = (ImageView) itemView.findViewById(R.id.cover);
            this.pageTitle = (TextView) itemView.findViewById(R.id.pageName);
//            authorName=itemView.findViewById(R.id.authorName);
            pageViewCount=itemView.findViewById(R.id.pageViewCount);
//            ratingCount=itemView.findViewById(R.id.ratingCount);
            pageCaptionTextView=itemView.findViewById(R.id.pageCaptionTextViewId);
//            tutorialsContainer=itemView.findViewById(R.id.tutorialsContainer);
            dateContainer=itemView.findViewById(R.id.dateContainer);
            dateCreated=itemView.findViewById(R.id.dateCreated);
            pageNumberEditText=itemView.findViewById(R.id.pageNumberEditTextId);

            likeActionButton=itemView.findViewById(R.id.likeActionButtonId);
            likeCountTextView=itemView.findViewById(R.id.likeCountTextViewId);

            discussActionButton=itemView.findViewById(R.id.discussActionButtonId);
            discussionCountTextView=itemView.findViewById(R.id.discussionCountTextViewId);
            viewDiscussionsTextView=itemView.findViewById(R.id.viewDiscussionsTextViewId);

        }
    }
    private void toggleProgress(boolean show) {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
    }

    public interface OnPaginationCallback{
        void onSuccess();
        void onFailed(String errorMessage);
}

}
