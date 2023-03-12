package com.palria.learnera.adapters;

import static com.palria.learnera.GlobalConfig.IS_FIRST_VIEW_KEY;
import static com.palria.learnera.GlobalConfig.LIBRARY_AUTHOR_ID_KEY;
import static com.palria.learnera.GlobalConfig.LIBRARY_ID_KEY;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.palria.learnera.widgets.LEBottomSheetDialog;

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
       holder.moreActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LEBottomSheetDialog leBottomSheetDialog = new LEBottomSheetDialog(context);
                leBottomSheetDialog.addOptionItem("Block Library", R.drawable.ic_baseline_error_outline_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        leBottomSheetDialog.hide();
                        Toast.makeText(context,"Blocking",Toast.LENGTH_SHORT).show();
                        int position = libraryDataModels.indexOf(libraryDataModel);
                        libraryDataModels.remove(libraryDataModel);
                        HomeBooksRecyclerListViewAdapter.this.notifyItemRemoved(position);
                        GlobalConfig.block(GlobalConfig.ACTIVITY_LOG_USER_BLOCK_LIBRARY_TYPE_KEY, libraryDataModel.getAuthorUserId(), libraryDataModel.getLibraryId(), null, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFailed(String errorMessage) {

                            }
                        });
                    }
                }, 0);
                leBottomSheetDialog.addOptionItem("Report Library", R.drawable.ic_baseline_error_outline_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        leBottomSheetDialog.hide();
                        Toast.makeText(context,"reporting",Toast.LENGTH_SHORT).show();
                        int position = libraryDataModels.indexOf(libraryDataModel);
                        libraryDataModels.remove(libraryDataModel);
                        HomeBooksRecyclerListViewAdapter.this.notifyItemRemoved(position);
                        GlobalConfig.report(GlobalConfig.ACTIVITY_LOG_USER_REPORT_LIBRARY_TYPE_KEY, libraryDataModel.getAuthorUserId(), libraryDataModel.getLibraryId(), null, new GlobalConfig.ActionCallback() {
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
        public ImageButton moreActionButton;

        public ViewHolder(View itemView) {
            super(itemView);
            this.bookCover = (ImageView) itemView.findViewById(R.id.bookCover);
            this.bookName = (TextView) itemView.findViewById(R.id.bookName);
            this.bookAuthor = itemView.findViewById(R.id.bookAuthor);
            this.numOfViews = itemView.findViewById(R.id.viewCount);
            this.moreActionButton = itemView.findViewById(R.id.moreActionButtonId);
            linearLayout = itemView.findViewById(R.id.parentItem);
        }
    }

}
