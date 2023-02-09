package com.palria.learnera.adapters;

import android.content.Context;
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
import com.palria.learnera.GlobalHelpers;
import com.palria.learnera.R;
import com.palria.learnera.models.AuthorDataModel;
import com.palria.learnera.models.LibraryDataModel;

import java.util.ArrayList;

public class AllLibraryFragmentRcvAdapter extends RecyclerView.Adapter<AllLibraryFragmentRcvAdapter.ViewHolder> {

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

        holder.libraryName.setText(libraryDataModel.getLibraryName());
        Glide.with(context)
                .load(libraryDataModel.getLibraryCoverPhotoDownloadUrl())
                .centerCrop()
                .placeholder(R.drawable.book_cover)
                .into(holder.cover);
        holder.libraryViewCount.setText(libraryDataModel.getTotalNumberOfLibraryViews()+"");

        int[] ratings =new int[5];
        ratings[0]= (int) libraryDataModel.getTotalNumberOfOneStarRate();
        ratings[1]= (int) libraryDataModel.getTotalNumberOfTwoStarRate();
        ratings[2]= (int) libraryDataModel.getTotalNumberOfThreeStarRate();
        ratings[3]= (int) libraryDataModel.getTotalNumberOfFourStarRate();
        ratings[4]= (int) libraryDataModel.getTotalNumberOfFiveStarRate();
        String averageRating= GlobalHelpers.calculateAverageRating(ratings);

        holder.ratingCount.setText(averageRating);


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
        public TextView  booksCount;
        public TextView ratingCount;


        public ViewHolder(View itemView) {
            super(itemView);
            this.cover = (ImageView) itemView.findViewById(R.id.cover);
            this.libraryName = (TextView) itemView.findViewById(R.id.libraryName);
            authorName=itemView.findViewById(R.id.authorName);
            libraryViewCount=itemView.findViewById(R.id.libraryViewCount);
            booksCount=itemView.findViewById(R.id.booksCount);
            ratingCount=itemView.findViewById(R.id.ratingCount);

        }
    }

    public void setDataList(ArrayList<LibraryDataModel> data){
        libraryDataModels.clear();
        libraryDataModels.addAll(data);
        this.notifyDataSetChanged();

    }

}
