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
import com.palria.learnera.SingleQuestionActivity;
import com.palria.learnera.models.PageDataModel;
import com.palria.learnera.models.PageDiscussionDataModel;
import com.palria.learnera.models.QuestionDataModel;
import com.palria.learnera.widgets.BottomSheetFormBuilderWidget;

import java.util.ArrayList;
import java.util.HashMap;










public class QuestionRcvAdapter extends RecyclerView.Adapter<QuestionRcvAdapter.ViewHolder> {

    ArrayList<QuestionDataModel> questionDataModels;
    Context context;

    public QuestionRcvAdapter(ArrayList<QuestionDataModel> questionDataModels, Context context){
        this.questionDataModels = questionDataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public QuestionRcvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.question_item_layout, parent, false);
        QuestionRcvAdapter.ViewHolder viewHolder = new QuestionRcvAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionRcvAdapter.ViewHolder holder, int position) {
        QuestionDataModel questionDataModel = questionDataModels.get(position);

        if (questionDataModel.isPublic() || (GlobalConfig.getCurrentUserId().equals(questionDataModel.getAuthorId()+""))) {

            holder.dateAskedTextView.setText(questionDataModel.getDateAsked()+"");

            GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY)
                    .document(questionDataModel.getAuthorId())
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
                                        .into(holder.askerProfilePhoto);
                            } catch (Exception ignored) {
                            }
                        }
                    });

            holder.questionBodyTextView.setText(questionDataModel.getQuestionBody());
            holder.ansCountTextView.setText(questionDataModel.getNumOfAnswers() + " Ans");

            holder.moreActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GlobalConfig.createPopUpMenu(context,R.menu.question_action_menu , holder.moreActionButton, new GlobalConfig.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClicked(MenuItem item) {

                            if(item.getItemId() == R.id.deleteId){

                                int positionDeleted =questionDataModels.indexOf(questionDataModel);
                                questionDataModels.remove(questionDataModel);
                                notifyItemRemoved(positionDeleted);
                            }

                            return true;
                        }
                    });
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, SingleQuestionActivity.class);
                    intent.putExtra(GlobalConfig.QUESTION_ID_KEY,questionDataModel.getQuestionId());
                    intent.putExtra(GlobalConfig.QUESTION_DATA_MODEL_KEY,questionDataModel);
                    context.startActivity(intent);
                }
            });


        }
        else{
            holder.questionBodyTextView.setText("question is hidden");
            holder.itemView.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return questionDataModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public RoundedImageView askerProfilePhoto;
        public TextView posterNameTextView;
        public ImageView verificationFlagImageView;
        public TextView  questionBodyTextView;
        public TextView dateAskedTextView;
        public ImageButton moreActionButton;
        public TextView ansCountTextView;
        public TextView viewCountTextView;



        public ViewHolder(View itemView) {
            super(itemView);
            this.askerProfilePhoto = itemView.findViewById(R.id.askerProfilePhotoId);
            this.posterNameTextView =  itemView.findViewById(R.id.posterNameTextViewId);
            this.verificationFlagImageView = itemView.findViewById(R.id.verificationFlagImageViewId);
            questionBodyTextView=itemView.findViewById(R.id.questionBodyTextViewId);
            moreActionButton=itemView.findViewById(R.id.moreActionButtonId);
            dateAskedTextView=itemView.findViewById(R.id.dateAskedTextViewId);
            ansCountTextView=itemView.findViewById(R.id.ansCountTextViewId);
            viewCountTextView=itemView.findViewById(R.id.viewCountTextViewId);

        }
    }

}
