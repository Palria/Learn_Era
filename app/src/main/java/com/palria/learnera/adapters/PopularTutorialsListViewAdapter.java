package com.palria.learnera.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.palria.learnera.R;
import com.palria.learnera.TutorialActivity;
import com.palria.learnera.models.AuthorDataModel;
import com.palria.learnera.models.LibraryDataModel;
import com.palria.learnera.models.TutorialDataModel;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

public class PopularTutorialsListViewAdapter extends RecyclerView.Adapter<PopularTutorialsListViewAdapter.ViewHolder> implements Serializable {

    ArrayList<TutorialDataModel> tutorialDataModels;
    Context context;

    public PopularTutorialsListViewAdapter(ArrayList<TutorialDataModel> tutorialDataModels, Context context) {
        this.tutorialDataModels = tutorialDataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public PopularTutorialsListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.single_tutorial_item_layout, parent, false);
        PopularTutorialsListViewAdapter.ViewHolder viewHolder = new PopularTutorialsListViewAdapter.ViewHolder(listItem);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull PopularTutorialsListViewAdapter.ViewHolder holder, int position) {

        TutorialDataModel tutorialDataModel = tutorialDataModels.get(position);

      holder.tutorialName.setText(tutorialDataModel.getTutorialName());
      if(tutorialDataModel.getDateCreated().length() >10) {
          holder.tutorialCreatedDate.setText(tutorialDataModel.getDateCreated().substring(0, 10));
      }else holder.tutorialCreatedDate.setText(tutorialDataModel.getDateCreated());

        Glide.with(context)
                .load(tutorialDataModel.getTutorialCoverPhotoDownloadUrl())
                .centerCrop()
                .placeholder(R.drawable.book_cover)
                .into(holder.cover);

        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(tutorialDataModel.getAuthorId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String userDisplayName = "" + documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                        holder.authorName.setText(userDisplayName);
                        String authorPhotoDownloadUrl = "" + documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);

try {
    Glide.with(context)
            .load(authorPhotoDownloadUrl)
            .placeholder(R.drawable.default_profile)
            .centerCrop()
            .into(holder.authorPicture);
}catch(Exception e){}
                    }
                });


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TutorialActivity.class);
                intent.putExtra(GlobalConfig.TUTORIAL_ID_KEY, tutorialDataModel.getTutorialId());
                intent.putExtra(GlobalConfig.LIBRARY_CONTAINER_ID_KEY, tutorialDataModel.getLibraryId());
                intent.putExtra(GlobalConfig.TUTORIAL_AUTHOR_ID_KEY, tutorialDataModel.getAuthorId());
                intent.putExtra(GlobalConfig.IS_FIRST_VIEW_KEY,false);
                intent.putExtra(GlobalConfig.TUTORIAL_DATA_MODEL_KEY,tutorialDataModel);
                context.startActivity(intent);
                  }
        });

    }

    @Override
    public int getItemCount() {
        return tutorialDataModels.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView cover;
        public ImageView authorPicture;
        public TextView tutorialName;
        public TextView authorName;
        public TextView tutorialCreatedDate;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.cover = (ImageView) itemView.findViewById(R.id.cover);
            this.authorPicture = itemView.findViewById(R.id.authorPicture);
            this.tutorialName = itemView.findViewById(R.id.title);
            this.authorName = itemView.findViewById(R.id.authorName);
            tutorialCreatedDate = itemView.findViewById(R.id.dateCreated);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.parentItem);
        }
    }

}
