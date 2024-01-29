package com.palria.learnera.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.palria.learnera.CreateQuizActivity;
import com.palria.learnera.GlobalConfig;
import com.palria.learnera.GlobalHelpers;
import com.palria.learnera.QuizActivity;
import com.palria.learnera.R;
import com.palria.learnera.TutorialFolderActivity;
import com.palria.learnera.models.FolderDataModel;
import com.palria.learnera.models.QuizDataModel;
import com.palria.learnera.models.QuizParticipantDatamodel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class QuizParticipantRCVAdapter extends RecyclerView.Adapter<QuizParticipantRCVAdapter.ViewHolder> {

    ArrayList<QuizParticipantDatamodel> quizParticipantDataModels;
    Context context;
    String quizId;
    String authorId;
    QuizDataModel quizDataModel;
//TODO: Let author post his own answer
    ArrayList<ArrayList<String>>authorAnswerList;
    //stores the scores and ids of the participant locally and not from database
    ArrayList<String> participantScoresList = new ArrayList<>();
    //stores the ids scores and ids of the participant from database online

    MaterialButton markQuizAsCompletedActionTextView;
    FloatingActionButton submitQuizActionButton;
    HashMap<String,Integer> totalScoresMap = new HashMap<>();
//    boolean isQuizMarkedCompleted;
    public QuizParticipantRCVAdapter(ArrayList<QuizParticipantDatamodel> quizParticipantDataModels, Context context, ArrayList<ArrayList<String>>authorAnswerList,String quizId,QuizDataModel quizDataModel,String authorId,MaterialButton markQuizAsCompletedActionTextView, FloatingActionButton submitQuizActionButton) {
        this.quizParticipantDataModels = quizParticipantDataModels;
        this.context = context;
        this.quizId = quizId;
        this.authorId = authorId;
        this.quizDataModel = quizDataModel;
        this.authorAnswerList = authorAnswerList;
        this.markQuizAsCompletedActionTextView = markQuizAsCompletedActionTextView;
        this.submitQuizActionButton = submitQuizActionButton;
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
        totalScoresMap.put(quizParticipantDataModel.getParticipantId(), quizParticipantDataModel.getTotalScores());
        //check if it already has this item, if so ignore the adding
//        if(!participantScoresList.contains(quizParticipantDataModel.getTotalScores() + "-ID-" + quizParticipantDataModel.getParticipantId())) {
//            participantScoresList.add(quizParticipantDataModel.getTotalScores() + "-ID-" + quizParticipantDataModel.getParticipantId());
//        }
//        Toast.makeText(context, quizParticipantDataModel.getTotalScores()+"", Toast.LENGTH_SHORT).show();

        boolean isEligibleToReward = false;
        if(quizParticipantDataModel.isSubmitted()){

//            holder.timeSubmittedView.setText("Answered : "+ GlobalHelpers.getTimeString(quizParticipantDataModel.getTimeSubmitted()));
            holder.timeSubmittedView.setText("Answered : "+  GlobalHelpers.getTimeString(quizParticipantDataModel.getTimeSubmitted()));

        }else{
            holder.timeSubmittedView.setText("Answered : Not submitted");

        }
        if(quizParticipantDataModel.isAuthorAnswer()) {
                holder.participantName.setText("Author's answer");
                holder.positionView.setVisibility(View.GONE);
                holder.awardNoticeTextView.setVisibility(View.GONE);
                holder.claimAwardActionTextView.setVisibility(View.GONE);
            }
            else{
                holder.saveResultButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.saveResultButton.setEnabled(false);
                        holder.saveResultButton.setText("Saving result...");

                        markTheAnswer(quizParticipantDataModel,totalScoresMap.get(quizParticipantDataModel.getParticipantId()), new GlobalConfig.ActionCallback() {
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
            markQuizAsCompletedActionTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    markQuizAsCompletedActionTextView.setEnabled(false);
                    markQuizAsCompletedActionTextView.setText("Completing...");
                    markQuizAsCompleted(new GlobalConfig.ActionCallback() {
                        @Override
                        public void onSuccess() {
                            markQuizAsCompletedActionTextView.setOnClickListener(null);
                            markQuizAsCompletedActionTextView.setText("Completed");
                            submitQuizActionButton.setEnabled(false);
                            GlobalConfig.recentlyMarkedCompletedQuizList.add(quizId);
                        }

                        @Override
                        public void onFailed(String errorMessage) {
                            markQuizAsCompletedActionTextView.setEnabled(true);
                            markQuizAsCompletedActionTextView.setText("Failed, Retry");

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
//checks if quiz is marked completed by author
            if((quizDataModel.isQuizMarkedCompleted() || GlobalConfig.recentlyMarkedCompletedQuizList.contains(quizId)) && !quizParticipantDataModel.isAuthorAnswer()  && quizParticipantDataModel.isAnswerMarkedByAuthor()) {

                holder.positionView.setVisibility(View.VISIBLE);
                markQuizAsCompletedActionTextView.setVisibility(View.GONE);


                int resultPosition1 = 0;
                for(int j=0; j<quizDataModel.getSavedParticipantScoresList().size(); j++){
                    String scoreAndIDString = quizDataModel.getSavedParticipantScoresList().get(j);
                    //check if the score is for this particular participant update positions in the arraylist
                    String score = scoreAndIDString.split("-ID-")[0];
                    //this tells the position of this participant. the resultPosition1 is the index so we have to add 1 to determine the position correctly
                    resultPosition1 = ++j;
                    holder.positionView.setText("Position : " + resultPosition1 + "      Total score : "+quizParticipantDataModel.getTotalScores()+"/"+quizDataModel.getTotalQuizScore());
//                    Toast.makeText(context, scoreAndIDString, Toast.LENGTH_SHORT).show();

                    if(scoreAndIDString.split("-ID-")[1].equals(quizParticipantDataModel.getParticipantId())) {
                         //check if position is less than 3
                        if(resultPosition1<=3){
                            isEligibleToReward = true;
                        }
                    }
                }

                //check if eligible to claim
                if(isEligibleToReward && quizParticipantDataModel.getParticipantId().equals(GlobalConfig.getCurrentUserId()) && !quizParticipantDataModel.isAuthorAnswer()) {
                    holder.awardNoticeTextView.setVisibility(View.VISIBLE);
                    holder.claimAwardActionTextView.setVisibility(View.VISIBLE);

                    int finalResultPosition1 = resultPosition1;

                    int[] numberOfCoins = new int[1];
                    numberOfCoins[0] = 0;
                    String positionDesc = "-";
                    switch(finalResultPosition1){
                        case 1:
                            numberOfCoins[0]=quizDataModel.getTotalQuizRewardCoins();
                            positionDesc = "1st";
                            break;
                        case 2:
                            numberOfCoins[0]=quizDataModel.getTotalQuizRewardCoins()/2;
                            positionDesc = "2nd";
                            break;
                        case 3:
                            numberOfCoins[0]=quizDataModel.getTotalQuizRewardCoins()/3;
                            positionDesc = "3rd";
                            break;
                    }
                    holder.awardNoticeTextView.setText("Claim award : "+numberOfCoins[0]+" Coins for winning "+positionDesc+" position");

                    if (!quizParticipantDataModel.isRewardClaimed()) {

                        holder.claimAwardActionTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.claimAwardActionTextView.setText("Receiving...");
                                holder.claimAwardActionTextView.setEnabled(false);

                                claimReward(numberOfCoins[0], "rewarded "+numberOfCoins[0]+" for winning position "+finalResultPosition1+" in quiz", new GlobalConfig.ActionCallback() {
                                    @Override
                                    public void onSuccess() {
                                        holder.claimAwardActionTextView.setText("Received "+numberOfCoins[0]+" Coin");
                                        holder.claimAwardActionTextView.setEnabled(false);
                                    }

                                    @Override
                                    public void onFailed(String errorMessage) {
                                        claimReward(numberOfCoins[0], "rewarded "+numberOfCoins[0]+" for winning position "+finalResultPosition1+" in quiz", null);
                                    }
                                });
                            }
                        });
                    }
                    else {
                        holder.claimAwardActionTextView.setText("Claimed");
                        holder.claimAwardActionTextView.setEnabled(false);
                    }


                }
                else{

                    holder.awardNoticeTextView.setText("Try win either 1st, 2nd, or 3rd position to get rewarded");
                    holder.claimAwardActionTextView.setText("Ineligible");
                }

            }
            else{
                holder.claimAwardActionTextView.setVisibility(View.GONE);
                holder.awardNoticeTextView.setVisibility(View.GONE);
                holder.positionView.setVisibility(View.GONE);
            }

//            if(quizParticipantDataModel.isSubmitted() ) {
//            if(quizParticipantDataModel.isAuthor() || quizParticipantDataModel.isAuthorAnswer()) {
                addAnswerView(quizParticipantDataModel, holder);
//            }
//            }
holder.participantName.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        context.startActivity(GlobalConfig.getHostActivityIntent(context,null,GlobalConfig.USER_PROFILE_FRAGMENT_TYPE_KEY,quizParticipantDataModel.getParticipantId()));

    }
});
holder.participantIcon.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        context.startActivity(GlobalConfig.getHostActivityIntent(context,null,GlobalConfig.USER_PROFILE_FRAGMENT_TYPE_KEY,quizParticipantDataModel.getParticipantId()));

    }
});
    }

    @Override
    public int getItemCount() {
        return quizParticipantDataModels.size();
    }


    void addAnswerView(QuizParticipantDatamodel quizParticipantDataModel,ViewHolder viewHolder){

        //CHECK IF QUIZ IS not COMPLETED ELSECHECK OTHER CONDITIONS
        if(quizDataModel.isQuizMarkedCompleted()) {

        }else{
            //check if the current user is the same as the participant or if they are the creator.
            if (!(GlobalConfig.getCurrentUserId().equals(quizParticipantDataModel.getParticipantId())) && !(GlobalConfig.getCurrentUserId().equals(authorId)) && !quizParticipantDataModel.isAuthorAnswer()) {
                return;
            }
            if (!quizParticipantDataModel.isSubmitted() && !(GlobalConfig.getCurrentUserId().equals(authorId))) {
                return;
            }
        }

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

                TextView scoresTextView = answerView.findViewById(R.id.scoresTextViewId);
                if(answerItem.size()>=5){
                    scoresTextView.setText("Score : " + answerItem.get(4));
//                     totalScores += Integer.parseInt(answerItem.get(4));

                }
                Spinner scoresSpinner = answerView.findViewById(R.id.scoresSpinnerId);
                Long[] scores = new Long[]{10L,9L,8L,7L,6L,5L,4L,3L,2L,1L,0L};
                ArrayAdapter<Long> arrayAdapter = new ArrayAdapter<>(context,R.layout.support_simple_spinner_dropdown_item,scores);
                scoresSpinner.setAdapter(arrayAdapter);
                int[] recentlySelected = new int[1];
                scoresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if(answerItem.get(3).equals("UNMARKED")) {
                            //if it's not marked then update the item
                            if (answerItem.size() >= 5) {
                                answerItem.set(4, scoresSpinner.getSelectedItem() + "");
                            }

                            scoresTextView.setText("Score : " + scoresSpinner.getSelectedItem());
                        }
                        int newScore = Integer.parseInt(scoresSpinner.getSelectedItem()+"");
                        //first remove the recently saved score to accommodate a new one
                        totalScoresMap.put(quizParticipantDataModel.getParticipantId(), (totalScoresMap.get(quizParticipantDataModel.getParticipantId()))-recentlySelected[0]);
                        // then add the new selected score
                        totalScoresMap.put(quizParticipantDataModel.getParticipantId(), (totalScoresMap.get(quizParticipantDataModel.getParticipantId()) + newScore));

                         recentlySelected[0] = newScore;
//
//                         for(int j=0; j<participantScoresList.size(); j++){
//                             String scoreAndIDString = participantScoresList.get(j);
//                             //check if the score is for this particular participant and then update positions in the arraylist
//                             if(scoreAndIDString.split("-ID-")[1].equals(quizParticipantDataModel.getParticipantId())){
//                                 participantScoresList.set(j,totalScoresMap.get(quizParticipantDataModel.getParticipantId())+"-ID-"+quizParticipantDataModel.getParticipantId());
//                             }
//                         }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                RadioButton passedButton = answerView.findViewById(R.id.passedButtonId);
                RadioButton failedButton = answerView.findViewById(R.id.failedButtonId);

                int finalI = i;
                passedButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            //REMOVE SOME ELEMENTS BECAUSE THEY SERVE THE SAME PURPOSE
//                            answerItem.remove("UNMARKED");
//                            answerItem.remove("PASSED");
//                            answerItem.remove("FAILED");
                            answerItem.set(3,"PASSED");

                            ArrayList<ArrayList<String>> answerList = quizParticipantDataModel.getAnswerList();
//                            answerList.remove(finalI);
                            answerList.set(finalI, answerItem);
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
//                            answerItem.remove("UNMARKED");
//                            answerItem.remove("PASSED");
//                            answerItem.remove("FAILED");
                            answerItem.set(3,"FAILED");


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
                String status = answerItem.size() >= 4 ? answerItem.get(3) : "Upgrade Error";
                if (status.equalsIgnoreCase("UNMARKED")) {
                    passedButton.setEnabled(true);
                    failedButton.setEnabled(true);
                    passedButton.setVisibility(View.VISIBLE);
                    failedButton.setVisibility(View.VISIBLE);
                    scoresSpinner.setVisibility(View.VISIBLE);
                    statusView.setText("Status: UNMARKED");
                } else if (status.equalsIgnoreCase("PASSED")) {
                    passedButton.setVisibility(View.INVISIBLE);
                    failedButton.setVisibility(View.INVISIBLE);
                    scoresSpinner.setVisibility(View.INVISIBLE);
                    statusView.setText("Status: CORRECT");
                    //add 1 score to the mark when the objective answer is correct
//                    totalScores++;

                } else if (status.equalsIgnoreCase("FAILED")) {
                    passedButton.setVisibility(View.INVISIBLE);
                    failedButton.setVisibility(View.INVISIBLE);
                    scoresSpinner.setVisibility(View.INVISIBLE);
                    statusView.setText("Status: WRONG");

                }
                viewHolder.answersScrollView.addView(answerView);

                if(authorId.equals(GlobalConfig.getCurrentUserId())){
                    passedButton.setVisibility(View.VISIBLE);
                    failedButton.setVisibility(View.VISIBLE);
                    scoresSpinner.setVisibility(View.VISIBLE);

                    if (!status.equalsIgnoreCase("UNMARKED")) {
                        passedButton.setVisibility(View.INVISIBLE);
                        failedButton.setVisibility(View.INVISIBLE);
                        scoresSpinner.setVisibility(View.INVISIBLE);
                    }
                }else{
                    passedButton.setVisibility(View.INVISIBLE);
                    failedButton.setVisibility(View.INVISIBLE);
                    scoresSpinner.setVisibility(View.INVISIBLE);
                }
                if(quizParticipantDataModel.isAuthorAnswer()){

                    statusView.setVisibility(View.INVISIBLE);
                    passedButton.setVisibility(View.INVISIBLE);
                    failedButton.setVisibility(View.INVISIBLE);
                    scoresSpinner.setVisibility(View.INVISIBLE);
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

                ArrayList<String> realPositions = new ArrayList<>();
                realPositions.addAll(Arrays.asList(new String[]{"1", "2", "3", "4"}));

                    if (!authorAnswerList.isEmpty() && authorAnswerList.get(i).size() > 0) {
                        //Toast.makeText(context, "it's NOT empty", Toast.LENGTH_SHORT).show();
                        if(realPositions.contains((authorAnswerList.get(i).get(1)).split("-")[0])) {

                        //compares participant answer to the author own answer
                        if (answerPosition == Integer.parseInt((authorAnswerList.get(i).get(1)).split("-")[0])) {
                            status = "Status: CORRECT";
                            totalScoresMap.put(quizParticipantDataModel.getParticipantId(), (totalScoresMap.get(quizParticipantDataModel.getParticipantId()) + 1));

                        } else if (answerPosition != Integer.parseInt((authorAnswerList.get(i).get(1)).split("-")[0])) {
                            status = "Status: WRONG";
                        }
                    }
                }
                statusView.setText(status);
                viewHolder.answersScrollView.addView(answerView);

                if(quizParticipantDataModel.isAuthorAnswer()){
                    statusView.setVisibility(View.INVISIBLE);
                }
            }

        }
            else {
            }


        }

    }
    void markTheAnswer(QuizParticipantDatamodel quizParticipantDatamodel,int totalScores, GlobalConfig.ActionCallback actionCallback){
            WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
            DocumentReference participantReference1 = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUIZ_KEY).document(quizId).collection(GlobalConfig.ALL_PARTICIPANTS_KEY).document(quizParticipantDatamodel.getParticipantId());
            HashMap<String, Object> participantDetails = new HashMap<>();
            participantDetails.put(GlobalConfig.IS_ANSWER_MARKED_BY_AUTHOR_KEY, true);
            participantDetails.put(GlobalConfig.DATE_MARKED_BY_AUTHOR_TIME_STAMP_KEY, FieldValue.serverTimestamp());
            participantDetails.put(GlobalConfig.TOTAL_SCORE_KEY, totalScoresMap.get(quizParticipantDatamodel.getParticipantId()));

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
    void claimReward(int numberOfCoins,String earningDescription, GlobalConfig.ActionCallback actionCallback){

            WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
            DocumentReference walletReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.USER_WALLET_KEY).document(GlobalConfig.USER_WALLET_KEY);
            HashMap<String, Object> walletDetails = new HashMap<>();
        walletDetails.put(GlobalConfig.WITHDRAWABLE_COIN_BALANCE_KEY,FieldValue.increment(numberOfCoins));
        walletDetails.put(GlobalConfig.TOTAL_COINS_EARNED_KEY,FieldValue.increment(numberOfCoins));
        walletDetails.put(GlobalConfig.TOTAL_QUIZ_REWARD_COINS_EARNED_KEY,FieldValue.increment(numberOfCoins));
        walletDetails.put(GlobalConfig.QUIZ_EARNINGS_HISTORY_LIST_KEY,FieldValue.arrayUnion("COIN-"+numberOfCoins+"-DESC-"+earningDescription+"-DATE-"+GlobalConfig.getDate()));

            writeBatch.set(walletReference, walletDetails, SetOptions.merge());

        DocumentReference rewardReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUIZ_KEY).document(quizId).collection(GlobalConfig.ALL_PARTICIPANTS_KEY).document(GlobalConfig.getCurrentUserId());
        HashMap<String, Object> rewardDetails = new HashMap<>();
        rewardDetails.put(GlobalConfig.IS_REWARD_CLAIMED_KEY,true);
        rewardDetails.put(GlobalConfig.TOTAL_COINS_EARNED_KEY, numberOfCoins);
        writeBatch.set(rewardReference, rewardDetails, SetOptions.merge());


        writeBatch.commit()
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(actionCallback!=null) {
                                actionCallback.onFailed(e.getMessage());
                            }
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            if(actionCallback!=null) {
                                actionCallback.onSuccess();
                            }else{
                                claimReward(numberOfCoins, earningDescription,null);
                        }
                        }
                    });
        }
    void markQuizAsCompleted(GlobalConfig.ActionCallback actionCallback){
        participantScoresList.clear();
        for(int i=0; i<quizParticipantDataModels.size();i++){
            participantScoresList.add(totalScoresMap.get(quizParticipantDataModels.get(i).getParticipantId()) +"-ID-"+quizParticipantDataModels.get(i).getParticipantId());

        }
        participantScoresList.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2)*-1;
            }
        });

        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
        DocumentReference walletReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUIZ_KEY).document(quizId);
        HashMap<String, Object> walletDetails = new HashMap<>();
        walletDetails.put(GlobalConfig.IS_QUIZ_MARKED_COMPLETED_KEY,true);
        walletDetails.put(GlobalConfig.DATE_QUIZ_MARKED_COMPLETED_TIME_STAMP_KEY,FieldValue.serverTimestamp());
        walletDetails.put(GlobalConfig.PARTICIPANT_SCORES_LIST_KEY,participantScoresList);

        writeBatch.set(walletReference, walletDetails, SetOptions.merge());

        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(actionCallback!=null) {
                            actionCallback.onFailed(e.getMessage());
                        }

 }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if(actionCallback!=null) {
                            actionCallback.onSuccess();
                        }
                        GlobalConfig.recentlyMarkedCompletedQuizList.add(quizId);

                        String notificationId = GlobalConfig.getRandomString(100);
                        //carries the info about the quiz
                        ArrayList<String> modelInfo = new ArrayList<>();
                        modelInfo.add(quizId);

                        //fires out the notification
                        GlobalConfig.sendNotificationToUsers(GlobalConfig.NOTIFICATION_TYPE_QUIZ_COMPLETED_KEY,notificationId,quizDataModel.getParticipantsList(),modelInfo,quizDataModel.getQuizTitle(),"Author has marked your quiz as completed",null);

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
        public TextView awardNoticeTextView;
        public TextView claimAwardActionTextView;
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
            this.awardNoticeTextView =  itemView.findViewById(R.id.awardNoticeTextViewId);
            this.claimAwardActionTextView =  itemView.findViewById(R.id.claimAwardActionTextViewId);

            this.saveResultButton =  itemView.findViewById(R.id.saveResultButtonId);
            this.menuButton =  itemView.findViewById(R.id.menuButtonId);

        }
    }

}

