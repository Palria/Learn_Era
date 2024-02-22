package com.palria.learnera.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.makeramen.roundedimageview.RoundedImageView;
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.GlobalHelpers;
import com.palria.learnera.R;
import com.palria.learnera.models.ClassDataModel;
import com.palria.learnera.models.ClassStudentDatamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class ClassStudentRCVAdapter extends RecyclerView.Adapter<ClassStudentRCVAdapter.ViewHolder> {

    ArrayList<ClassStudentDatamodel> classStudentDataModels;
    Context context;
    String classId;
    String authorId;
    ClassDataModel classDataModel;

    MaterialButton markClassAsClosedActionTextView;
//    FloatingActionButton submitClassActionButton;

    HashMap<String,String> studentsName = new HashMap<>();
    HashMap<String, ImageView> studentsIconImages = new HashMap<>();
    HashMap<String, Boolean> studentsVerifiedFlagsMap = new HashMap<>();
    ArrayList<String> fetchedOwnerDetailsIdList = new ArrayList<>();

    public ClassStudentRCVAdapter(ArrayList<ClassStudentDatamodel> classStudentDataModels, Context context, String classId, ClassDataModel classDataModel, String authorId, MaterialButton markClassAsClosedActionTextView, HashMap<String,String> studentsName,HashMap<String, ImageView> studentsIconImages,HashMap<String, Boolean> studentsVerifiedFlagsMap, ArrayList<String> fetchedOwnerDetailsIdList ) {
        this.classStudentDataModels = classStudentDataModels;
        this.context = context;
        this.classId = classId;
        this.authorId = authorId;
        this.classDataModel = classDataModel;
        this.markClassAsClosedActionTextView = markClassAsClosedActionTextView;
//        this.submitClassActionButton = submitClassActionButton;
        this.studentsName = studentsName;
        this.studentsIconImages = studentsIconImages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.class_students_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        ClassStudentDatamodel classStudentDataModel = classStudentDataModels.get(position);

        boolean isEligibleToReward = false;

            markClassAsClosedActionTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    markClassAsClosedActionTextView.setEnabled(false);
                    markClassAsClosedActionTextView.setText("Closing...");
                    GlobalConfig.markClassAsClosed(context,classId,new GlobalConfig.ActionCallback() {
                        @Override
                        public void onSuccess() {
                            markClassAsClosedActionTextView.setOnClickListener(null);
                            markClassAsClosedActionTextView.setText("Closed");
//                            submitClassActionButton.setEnabled(false);
                            GlobalConfig.recentlyClosedClassList.add(classId);
                        }

                        @Override
                        public void onFailed(String errorMessage) {
                            markClassAsClosedActionTextView.setEnabled(true);
                            markClassAsClosedActionTextView.setText("Failed, Retry");

                        }
                    });
                }
            });

                GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(classStudentDataModel.getStudentId())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                String userProfilePhotoDownloadUrl = "" + documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
                                try {
                                    Glide.with(context)
                                            .load(userProfilePhotoDownloadUrl)
                                            .centerCrop()
                                            .placeholder(R.drawable.default_profile)
                                            .into(holder.studentIcon);

                                    ImageView icon = holder.studentIcon;
                                    icon.setDrawingCacheEnabled(true);
                                    icon.buildDrawingCache(true);
                                    studentsIconImages.put(classStudentDataModel.getStudentId(),(icon));

                                } catch (Exception ignored) {
                                }

                                String userDisplayName = "" + documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                                holder.studentName.setText(userDisplayName);
                                studentsName.put(classStudentDataModel.getStudentId(),userDisplayName);

                                boolean isVerified = documentSnapshot.get(GlobalConfig.IS_ACCOUNT_VERIFIED_KEY) != null ? documentSnapshot.getBoolean(GlobalConfig.IS_ACCOUNT_VERIFIED_KEY) : false;
                                if (isVerified) {
                                    holder.verificationFlagImageView.setVisibility(View.VISIBLE);
                                } else {
                                    holder.verificationFlagImageView.setVisibility(View.INVISIBLE);

                                }
                                studentsVerifiedFlagsMap.put(classStudentDataModel.getStudentId(),isVerified);
                                fetchedOwnerDetailsIdList.add(classStudentDataModel.getStudentId());
                            }
                        });

//checks if class is marked completed by author
            if((classDataModel.isClosed() || GlobalConfig.recentlyClosedClassList.contains(classId))) {

                markClassAsClosedActionTextView.setVisibility(View.GONE);

            }



holder.studentName.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        context.startActivity(GlobalConfig.getHostActivityIntent(context,null,GlobalConfig.USER_PROFILE_FRAGMENT_TYPE_KEY,classStudentDataModel.getStudentId()));

    }
});
holder.studentIcon.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        context.startActivity(GlobalConfig.getHostActivityIntent(context,null,GlobalConfig.USER_PROFILE_FRAGMENT_TYPE_KEY,classStudentDataModel.getStudentId()));

    }
});
    }

    @Override
    public int getItemCount() {
        return classStudentDataModels.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        public RoundedImageView studentIcon;
        public TextView studentName;
        public ImageView verificationFlagImageView;
        public TextView statusView;
        public Button saveResultButton;
        public ImageButton menuButton;


        public ViewHolder(View itemView) {
            super(itemView);

            this.studentIcon =  itemView.findViewById(R.id.studentIcon);
            this.studentName =  itemView.findViewById(R.id.studentNameId);
            this.studentName =  itemView.findViewById(R.id.studentNameId);
            this.verificationFlagImageView =  itemView.findViewById(R.id.verificationFlagImageViewId);

            this.saveResultButton =  itemView.findViewById(R.id.saveResultButtonId);
            this.menuButton =  itemView.findViewById(R.id.menuButtonId);

        }
    }

}

