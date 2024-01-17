package com.palria.learnera.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.makeramen.roundedimageview.RoundedImageView;
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.QuizActivity;
import com.palria.learnera.R;
import com.palria.learnera.TutorialFolderActivity;
import com.palria.learnera.models.FolderDataModel;
import com.palria.learnera.models.QuizDataModel;
import com.palria.learnera.models.QuizParticipantDatamodel;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizParticipantRCVAdapter extends RecyclerView.Adapter<QuizParticipantRCVAdapter.ViewHolder> {

    ArrayList<QuizParticipantDatamodel> quizParticipantDataModels;
    Context context;
    String quizId;
    String authorId;
    QuizDataModel quizDataModel;
//TODO: Let author post his own answer
    ArrayList<ArrayList<String>>authorAnswerList;

    public QuizParticipantRCVAdapter(ArrayList<QuizParticipantDatamodel> quizParticipantDataModels, Context context, ArrayList<ArrayList<String>>authorAnswerList,String quizId,QuizDataModel quizDataModel,String authorId) {
        this.quizParticipantDataModels = quizParticipantDataModels;
        this.context = context;
        this.quizId = quizId;
        this.authorId = authorId;
        this.quizDataModel = quizDataModel;
        this.authorAnswerList = authorAnswerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.quiz_participant_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        QuizParticipantDatamodel quizParticipantDataModel = quizParticipantDataModels.get(position);


            holder.timeSubmittedView.setText("Date answered: "+ quizParticipantDataModel.getTimeSubmitted());
            if(quizParticipantDataModel.isAuthor()) {
                holder.participantName.setText("Author's answer");
                holder.positionView.setVisibility(View.GONE);
            }
            else{
                holder.saveResultButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.saveResultButton.setEnabled(false);
                        holder.saveResultButton.setText("Saving result...");

                        markTheAnswer(quizParticipantDataModel, new GlobalConfig.ActionCallback() {
                            @Override
                            public void onSuccess() {
                                holder.saveResultButton.setText("Saved");
                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                holder.saveResultButton.setEnabled(true);
                                holder.saveResultButton.setText("Try again");

                                Toast.makeText(context, "Failed to save result", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(quizParticipantDataModel.getParticipantId())
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
                                            .into(holder.participantIcon);
                                } catch (Exception ignored) {
                                }

                                String userDisplayName = "" + documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                                holder.participantName.setText(userDisplayName);


                                boolean isVerified = documentSnapshot.get(GlobalConfig.IS_ACCOUNT_VERIFIED_KEY) != null ? documentSnapshot.getBoolean(GlobalConfig.IS_ACCOUNT_VERIFIED_KEY) : false;
                                if (isVerified) {
                                    holder.verificationFlagImageView.setVisibility(View.VISIBLE);
                                } else {
                                    holder.verificationFlagImageView.setVisibility(View.INVISIBLE);

                                }
                            }
                        });
            }

            if(quizParticipantDataModel.isSubmitted() || quizParticipantDataModel.isAuthor()) {
                addAnswerView(quizParticipantDataModel, holder);
            }

    }

    @Override
    public int getItemCount() {
        return quizParticipantDataModels.size();
    }


    void addAnswerView(QuizParticipantDatamodel quizParticipantDataModel,ViewHolder viewHolder){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i=0; i<quizParticipantDataModel.getAnswerList().size(); i++) {
            ArrayList<String> answerItem = quizParticipantDataModel.getAnswerList().get(i);
            if(!(answerItem.isEmpty())){
            if (answerItem.get(0).equals(GlobalConfig.IS_THEORY_QUESTION_KEY)) {
                View answerView = layoutInflater.inflate(R.layout.theory_answer_preview, viewHolder.answersScrollView, false);

                TextView statusView =  answerView.findViewById(R.id.statusViewId);

                TextView questionTextView = answerView.findViewById(R.id.questionTextViewId);
                String question = answerItem.size() >= 3 ? answerItem.get(2) : "Question Undefined";
                questionTextView.setText(question);

                TextView answerText = answerView.findViewById(R.id.answerTextViewId);
                String answer = answerItem.get(1);
                answerText.setText(answer);
                //todo test and delete
//                answerText.setText(answerItem.get(0)+answerItem.get(1)+answerItem.get(2));
//                answerText.setText(question);



                RadioButton passedButton = answerView.findViewById(R.id.passedButtonId);
                RadioButton failedButton = answerView.findViewById(R.id.failedButtonId);

                int finalI = i;
                passedButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            //REMOVE SOME ELEMENTS BECAUSE THEY SERVE THE SAME PURPOSE
                            answerItem.remove("UNMARKED");
                            answerItem.remove("PASSED");
                            answerItem.remove("FAILED");
                            answerItem.add("PASSED");

                            ArrayList<ArrayList<String>> answerList = quizParticipantDataModel.getAnswerList();
                            answerList.remove(finalI);
                            answerList.add(finalI, answerItem);
                            quizParticipantDataModel.setAnswerList(answerList);


                            viewHolder.saveResultButton.setEnabled(true);
                            viewHolder.saveResultButton.setVisibility(View.VISIBLE);

//                            passedButton.setEnabled(false);
//                            failedButton.setEnabled(false);

                        }
                    }
                });
                failedButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            //REMOVE SOME ELEMENTS BECAUSE THEY SERVE THE SAME PURPOSE
                            answerItem.remove("UNMARKED");
                            answerItem.remove("PASSED");
                            answerItem.remove("FAILED");
                            answerItem.add("FAILED");


                            ArrayList<ArrayList<String>> answerList = quizParticipantDataModel.getAnswerList();
                            answerList.remove(finalI);
                            answerList.add(finalI, answerItem);
                            quizParticipantDataModel.setAnswerList(answerList);

                            viewHolder.saveResultButton.setEnabled(true);
                            viewHolder.saveResultButton.setVisibility(View.VISIBLE);

//                            passedButton.setEnabled(false);
//                            failedButton.setEnabled(false);

                        }
                    }
                });

                //                checks if it is marked
                String status = answerItem.size() == 4 ? answerItem.get(3) : "Upgrade Error";
                if (status.equalsIgnoreCase("UNMARKED")) {
                    passedButton.setEnabled(true);
                    failedButton.setEnabled(true);
                    passedButton.setVisibility(View.VISIBLE);
                    failedButton.setVisibility(View.VISIBLE);
                    statusView.setText("Status: UNMARKED");
                } else if (status.equalsIgnoreCase("PASSED")) {
                    passedButton.setVisibility(View.INVISIBLE);
                    failedButton.setVisibility(View.INVISIBLE);
                    statusView.setText("Status: CORRECT");

                } else if (status.equalsIgnoreCase("FAILED")) {
                    passedButton.setVisibility(View.INVISIBLE);
                    failedButton.setVisibility(View.INVISIBLE);
                    statusView.setText("Status: WRONG");

                }
                viewHolder.answersScrollView.addView(answerView);

                if(authorId.equals(GlobalConfig.getCurrentUserId())){
                    passedButton.setVisibility(View.VISIBLE);
                    failedButton.setVisibility(View.VISIBLE);

                    if (!status.equalsIgnoreCase("UNMARKED")) {
                        passedButton.setVisibility(View.INVISIBLE);
                        failedButton.setVisibility(View.INVISIBLE);
                    }
                }else{
                    passedButton.setVisibility(View.INVISIBLE);
                    failedButton.setVisibility(View.INVISIBLE);
                }
                if(quizParticipantDataModel.isAuthor()){

                    statusView.setVisibility(View.INVISIBLE);
                    passedButton.setVisibility(View.INVISIBLE);
                    failedButton.setVisibility(View.INVISIBLE);
                }
            }
            else {
                View answerView = layoutInflater.inflate(R.layout.objective_answer_preview, viewHolder.answersScrollView, false);

                TextView statusView = answerView.findViewById(R.id.statusViewId);

                TextView questionTextView = answerView.findViewById(R.id.questionTextViewId);
                String question = answerItem.size() >= 3 ? answerItem.get(2) : "Question Undefined";
                questionTextView.setText(question);

                RadioButton answerOptionButton = answerView.findViewById(R.id.answerOptionButtonId);
                String answerInfo = answerItem.get(1);
                int answerPosition = Integer.parseInt(answerInfo.split("-")[0]);
                String answer = answerInfo.split("-").length > 1 ? answerInfo.split("-")[1] : "Undefined";
                answerOptionButton.setText(answer);


//                String status = answerItem.size() == 4 ? answerItem.get(2) : "Upgrade Error";
//                checks if it is marked
                String status = answerItem.size() == 4 ? answerItem.get(3) : "Upgrade Error";
                if(!authorAnswerList.isEmpty() && authorAnswerList.get(i).size()>0){
                    //Toast.makeText(context, "it's NOT empty", Toast.LENGTH_SHORT).show();
                    //compares participant answer to the author own answer
                    if (answerPosition == Integer.parseInt((authorAnswerList.get(i).get(1)).split("-")[0])) {
                    status = "Status: CORRECT";
                } else if (answerPosition != Integer.parseInt((authorAnswerList.get(i).get(1)).split("-")[0])) {
                    status = "Status: WRONG";
                }
            }
                statusView.setText(status);
                viewHolder.answersScrollView.addView(answerView);

                if(quizParticipantDataModel.isAuthor()){
                    statusView.setVisibility(View.INVISIBLE);
                }
            }

        }
            else {
            }


        }

    }
    void markTheAnswer(QuizParticipantDatamodel quizParticipantDatamodel, GlobalConfig.ActionCallback actionCallback){
            WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
            DocumentReference participantReference1 = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUIZ_KEY).document(quizId).collection(GlobalConfig.ALL_PARTICIPANTS_KEY).document(quizParticipantDatamodel.getParticipantId());
            HashMap<String, Object> participantDetails = new HashMap<>();
//            participantDetails.put(GlobalConfig.IS_ANSWER_SUBMITTED_KEY, true);
//            participantDetails.put(GlobalConfig.ANSWER_SUBMITTED_TIME_STAMP_KEY, FieldValue.serverTimestamp());

            for (int position = 0; position < quizParticipantDatamodel.getAnswerList().size(); position++) {
                participantDetails.put(GlobalConfig.ANSWER_LIST_KEY + "-" + position, quizParticipantDatamodel.getAnswerList().get(position));
            }

            writeBatch.set(participantReference1, participantDetails, SetOptions.merge());


            writeBatch.commit()
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            actionCallback.onFailed(e.getMessage());
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            String notificationId = GlobalConfig.getRandomString(100);
                            //contains ids to the participants who receive the notification
                            ArrayList<String> receiversIdList = new ArrayList<>();
                            receiversIdList.add(quizParticipantDatamodel.getParticipantId());
                            //carries the info about the quiz
                            ArrayList<String> modelInfo = new ArrayList<>();
                            modelInfo.add(quizId);

                            //fires out the notification
                            GlobalConfig.sendNotificationToUsers(GlobalConfig.NOTIFICATION_TYPE_QUIZ_KEY,notificationId,receiversIdList,modelInfo,quizDataModel.getQuizTitle(),"Author has marked your answer in a quiz you participated",null);


                            actionCallback.onSuccess();
                        }
                    });
        }

    class ViewHolder extends RecyclerView.ViewHolder{

        public RoundedImageView participantIcon;
        public TextView participantName;
        public ImageView verificationFlagImageView;
        public LinearLayout answersScrollView;
        public TextView timeSubmittedView;
        public TextView positionView;
        //indicates whether failed, passed or if already marked
        public TextView statusView;
        public Button saveResultButton;
        public ImageButton menuButton;


        public ViewHolder(View itemView) {
            super(itemView);

            this.participantIcon =  itemView.findViewById(R.id.participantIcon);
            this.participantName =  itemView.findViewById(R.id.participantNameId);
            this.participantName =  itemView.findViewById(R.id.participantNameId);
            this.verificationFlagImageView =  itemView.findViewById(R.id.verificationFlagImageViewId);
            this.answersScrollView =  itemView.findViewById(R.id.answersScrollViewId);
            this.timeSubmittedView =  itemView.findViewById(R.id.timeSubmittedViewId);
            this.positionView =  itemView.findViewById(R.id.positionViewId);

            this.saveResultButton =  itemView.findViewById(R.id.saveResultButtonId);
            this.menuButton =  itemView.findViewById(R.id.menuButtonId);

        }
    }

}

