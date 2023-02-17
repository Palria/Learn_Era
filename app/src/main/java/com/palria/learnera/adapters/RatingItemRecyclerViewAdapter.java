package com.palria.learnera.adapters;

import static com.palria.learnera.GlobalConfig.LIBRARY_AUTHOR_ID_KEY;
import static com.palria.learnera.GlobalConfig.LIBRARY_ID_KEY;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.GlobalHelpers;
import com.palria.learnera.LibraryActivity;
import com.palria.learnera.R;
import com.palria.learnera.models.LibraryDataModel;
import com.palria.learnera.models.RatingDataModel;

import java.util.ArrayList;

public class RatingItemRecyclerViewAdapter extends RecyclerView.Adapter<RatingItemRecyclerViewAdapter.ViewHolder> {

    ArrayList<RatingDataModel> ratingDataModels;
    Context context;

    public RatingItemRecyclerViewAdapter(ArrayList<RatingDataModel> ratingDataModels, Context context) {
        this.ratingDataModels = ratingDataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RatingItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.single_rating_layout_item, parent, false);
        RatingItemRecyclerViewAdapter.ViewHolder viewHolder = new RatingItemRecyclerViewAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RatingItemRecyclerViewAdapter.ViewHolder holder, int position) {

        RatingDataModel ratingDataModel = ratingDataModels.get(position);

        holder.userName.setText(ratingDataModel.getUserName());
        Glide.with(context)
                .load(ratingDataModel.getUserProfileDownlaodUrl())
                .centerCrop()
                .placeholder(R.drawable.default_profile)
                .into(holder.cover);

        holder.message.setText(ratingDataModel.getMessage());
        holder.date.setText(ratingDataModel.getDate());

        //default progress for this now.
        holder.ratingBar.setProgress(ratingDataModel.getRating());


//        GlobalConfig.getFirebaseFirestoreInstance()
//                .collection(GlobalConfig.ALL_USERS_KEY)
//                .document(ratingDataModel.getUserId())
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                        String userDisplayName = "" + documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
//                        holder.authorName.setText(userDisplayName);
//                    }
//                });


    }

    @Override
    public int getItemCount() {
        return ratingDataModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView cover;
        public TextView userName;
        public RatingBar ratingBar;
        public TextView message;
        public TextView  date;



        public ViewHolder(View itemView) {
            super(itemView);
            this.cover = (ImageView) itemView.findViewById(R.id.profilePicture);
            this.userName = (TextView) itemView.findViewById(R.id.name);
            ratingBar=itemView.findViewById(R.id.ratingBar);
            message=itemView.findViewById(R.id.message);
            date=itemView.findViewById(R.id.date);


        }
    }


}