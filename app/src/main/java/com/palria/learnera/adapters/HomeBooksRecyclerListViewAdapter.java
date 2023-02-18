package com.palria.learnera.adapters;

import static com.palria.learnera.GlobalConfig.IS_FIRST_VIEW_KEY;
import static com.palria.learnera.GlobalConfig.LIBRARY_AUTHOR_ID_KEY;
import static com.palria.learnera.GlobalConfig.LIBRARY_ID_KEY;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.LibraryActivity;
import com.palria.learnera.R;
import com.palria.learnera.models.AuthorDataModel;
import com.palria.learnera.models.LibraryDataModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HomeBooksRecyclerListViewAdapter extends RecyclerView.Adapter<HomeBooksRecyclerListViewAdapter.ViewHolder> {

    ArrayList<LibraryDataModel> libraryDataModels;
    Context context;

    public HomeBooksRecyclerListViewAdapter(ArrayList<LibraryDataModel> libraryDataModels, Context context) {
        this.libraryDataModels = libraryDataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeBooksRecyclerListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.home_single_library_item_layout, parent, false);
        HomeBooksRecyclerListViewAdapter.ViewHolder viewHolder = new HomeBooksRecyclerListViewAdapter.ViewHolder(listItem);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull HomeBooksRecyclerListViewAdapter.ViewHolder holder, int position) {

        LibraryDataModel libraryDataModel = libraryDataModels.get(position);

        holder.bookName.setText(libraryDataModel.getLibraryName());
        holder.numOfViews.setText(libraryDataModel.getTotalNumberOfLibraryViews()+"");

        Glide.with(context)
                .load(libraryDataModel.getLibraryCoverPhotoDownloadUrl())
                .centerCrop()
                .placeholder(R.drawable.book_cover)
                .into(holder.bookCover);

        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(libraryDataModel.getAuthorUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                          @Override
                                          public void onSuccess(DocumentSnapshot documentSnapshot) {

                                              String userDisplayName = "" + documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                                        holder.bookAuthor.setText(userDisplayName);
                                          }
                                      });


       holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LibraryActivity.class);
                intent.putExtra(LIBRARY_ID_KEY, libraryDataModel.getLibraryId());
                intent.putExtra(LIBRARY_AUTHOR_ID_KEY, libraryDataModel.getAuthorUserId());
                intent.putExtra(IS_FIRST_VIEW_KEY, false);
                intent.putExtra(GlobalConfig.LIBRARY_DATA_MODEL_KEY, libraryDataModel);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return libraryDataModels.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView bookCover;
        public TextView bookName;
        public TextView bookAuthor;
        public TextView numOfViews;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.bookCover = (ImageView) itemView.findViewById(R.id.bookCover);
            this.bookName = (TextView) itemView.findViewById(R.id.bookName);
            this.bookAuthor = itemView.findViewById(R.id.bookAuthor);
            this.numOfViews = itemView.findViewById(R.id.viewCount);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.parentItem);
        }
    }

}
