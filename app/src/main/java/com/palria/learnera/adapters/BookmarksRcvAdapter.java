package com.palria.learnera.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.R;
import com.palria.learnera.TutorialFolderActivity;
import com.palria.learnera.models.BookmarkDataModel;
import com.palria.learnera.models.FolderDataModel;

import java.util.ArrayList;

public class BookmarksRcvAdapter extends RecyclerView.Adapter<BookmarksRcvAdapter.ViewHolder> {

    ArrayList<BookmarkDataModel> bookmarkDataModels;
    Context context;

    public BookmarksRcvAdapter(ArrayList<BookmarkDataModel> bookmarkDataModels, Context context) {
        this.bookmarkDataModels = bookmarkDataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public BookmarksRcvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.bookmarks_item_layout, parent, false);
        BookmarksRcvAdapter.ViewHolder viewHolder = new BookmarksRcvAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarksRcvAdapter.ViewHolder holder, int position) {

        BookmarkDataModel bookmarkDataModel = bookmarkDataModels.get(position);

        holder.title.setText(bookmarkDataModel.getTitle());
        holder.dateCreated.setText(bookmarkDataModel.getDate());
        holder.description.setText(bookmarkDataModel.getDescription());

        Glide.with(context)
                        .load(bookmarkDataModel.getIconDownloadUrl())
                                .placeholder(R.drawable.book_cover2)
                .centerCrop()
                                        .into(holder.icon);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext(),"click on item: "+folderDataModel.getFolderName(),Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(context, TutorialFolderActivity.class);
//                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return bookmarkDataModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView dateCreated;
        public TextView description;
        public RoundedImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title =  itemView.findViewById(R.id.title);
            this.dateCreated = (TextView) itemView.findViewById(R.id.dateCreated);
            description=itemView.findViewById(R.id.description);
            icon = itemView.findViewById(R.id.icon);

        }
    }

}

