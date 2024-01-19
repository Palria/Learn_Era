package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;
import com.palria.learnera.adapters.QuizParticipantRCVAdapter;
import com.palria.learnera.models.QuestionDataModel;
import com.palria.learnera.models.QuizDataModel;
import com.palria.learnera.models.QuizParticipantDatamodel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizActivity extends AppCompatActivity {

    ImageButton backButton;

    Toolbar toolbar;
    String authorId;
    String quizId;
    TextView maxTimeTextView;
    TextView joinActionTextView;
    TextView statusTextView;
    TextView participantCountTextView;
    TextView viewCountTextView;
    TextView descriptionTextView;
    TextView datePostedTextView;
    TextView questionCountTextView;
    TextView posterNameTextView;
    ImageView posterPhoto,verificationFlagImageView;
    FloatingActionButton submitActionButton;
    AlertDialog alertDialog;
    QuizDataModel quizDataModel;
    ArrayList questionList = new ArrayList();
    ArrayList<ArrayList<String>> answersList = new ArrayList();
//    ArrayList<ArrayList<String>> authorSavedAnswersList = new ArrayList();
    LinearLayout containerLinearLayout;
    long totalTimeLimit = 0;
    long timeRemain = 0;
    HashMap<Integer,Integer> timeRemainMap = new HashMap<>();
    CountDownTimer countDownTimer;
    int activePosition = -1;
    TextView activeQuestionTimeRemainTextView ;
    TextView activeQuestionTextView ;
    View activeAnswerInput;
    RadioButton activeAnswerRadioOption1;
    RadioButton activeAnswerRadioOption2;
    RadioButton activeAnswerRadioOption3;
    RadioButton activeAnswerRadioOption4;
    ArrayList<Integer> viewedPositions = new ArrayList<>();
    boolean isSubmitted = false;
    boolean isJoined = false;
    boolean isAuthor = false;
    boolean isLoadFromOnline = false;

    RecyclerView participantRecyclerView;
    QuizParticipantRCVAdapter quizParticipantRCVAdapter;
    ArrayList<QuizParticipantDatamodel> quizParticipantDatamodels = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        fetchIntentData();
        initUI();
        setSupportActionBar(toolbar);
        if(GlobalConfig.recentlydeletedQuizList.contains(quizId)){
            Toast.makeText(getApplicationContext(), "Error occurred: Quiz deleted", Toast.LENGTH_SHORT).show();
            super.onBackPressed();

        }else {
            submitActionButton.setOnClickListener(view -> {
                showQuizCompletionConfirmationDialog();
            });
            if (authorId.equalsIgnoreCase(GlobalConfig.getCurrentUserId())) {
                isAuthor = true;
            } else {

            }
            getAuthorInfo();
            if(isLoadFromOnline){
                renderQuizFromOnline();
            }else {
                renderQuiz(quizDataModel);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(countDownTimer!=null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isAuthor) {
            getMenuInflater().inflate(R.menu.quiz_action_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.editId){
            if(quizDataModel.getParticipantsList().size()<1) {
                Intent intent = new Intent(QuizActivity.this, CreateQuizActivity.class);
                intent.putExtra(GlobalConfig.IS_EDITION_KEY, true);
                intent.putExtra(GlobalConfig.QUIZ_ID_KEY, quizId);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "Can't take action: A user has already joined and can't be edited", Toast.LENGTH_SHORT).show();
            }
        }else if(item.getItemId()==R.id.deleteId){
            if(quizDataModel.getParticipantsList().size()<1) {
                toggleProgress(true);
                GlobalConfig.deleteQuiz(getApplicationContext(), quizId, new GlobalConfig.ActionCallback() {
                    @Override
                    public void onSuccess() {
                        toggleProgress(false);
                        GlobalConfig.recentlydeletedQuizList.add(quizId);
                        QuizActivity.super.onBackPressed();

                    }

                    @Override
                    public void onFailed(String errorMessage) {
                        toggleProgress(false);

                        Toast.makeText(getApplicationContext(), "Error occurred: Can't delete", Toast.LENGTH_SHORT).show();

                    }
                });
            } else{
                Toast.makeText(getApplicationContext(), "Can't take action: A user has already joined and can't be deleted", Toast.LENGTH_SHORT).show();
            }
        }else if(item.getItemId()==R.id.home){
            super.onBackPressed();

        }
        return true;
    }

    private void initUI(){

        //init programmatically

         activeQuestionTimeRemainTextView = new TextView(this);
         activeQuestionTextView  = new TextView(this);
         activeAnswerInput  = new View(this);
        activeAnswerRadioOption1  = new RadioButton(this);
        activeAnswerRadioOption2  = new RadioButton(this);
        activeAnswerRadioOption3  = new RadioButton(this);
        activeAnswerRadioOption4  = new RadioButton(this);

        backButton = findViewById(R.id.backButtonId);
        toolbar = findViewById(R.id.topBar);
        maxTimeTextView = findViewById(R.id.maxTimeTextViewId);
        joinActionTextView = findViewById(R.id.joinQuizActionTextViewId);
        statusTextView = findViewById(R.id.activeStatusTextViewId);
        participantCountTextView = findViewById(R.id.participantCountTextViewId);
        descriptionTextView = findViewById(R.id.descriptionTextViewId);
        viewCountTextView = findViewById(R.id.viewCountTextViewId);
        datePostedTextView = findViewById(R.id.datePostedTextViewId);
        questionCountTextView = findViewById(R.id.questionCountTextViewId);
        posterNameTextView = findViewById(R.id.posterNameTextViewId);
        posterPhoto = findViewById(R.id.posterProfilePhotoId);
        verificationFlagImageView = findViewById(R.id.verificationFlagImageViewId);
        containerLinearLayout = findViewById(R.id.containerLinearLayoutId);
        submitActionButton = findViewById(R.id.submitActionButtonId);
        participantRecyclerView = findViewById(R.id.participantRecyclerViewId);

        alertDialog = new AlertDialog.Builder(QuizActivity.this)
                .setCancelable(false)
                .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
                .create();
    }

    private  void fetchIntentData(){
        Intent intent = getIntent();
        quizDataModel = (QuizDataModel) intent.getSerializableExtra(GlobalConfig.QUIZ_DATA_MODEL_KEY);
        authorId = intent.getStringExtra(GlobalConfig.AUTHOR_ID_KEY);
        quizId = intent.getStringExtra(GlobalConfig.QUIZ_ID_KEY);
        isLoadFromOnline =intent.getBooleanExtra(GlobalConfig.IS_LOAD_FROM_ONLINE_KEY,false);
    }
    private void showQuizCompletionConfirmationDialog(){

        AlertDialog confirmationDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you ready to submit?");
        String message = "You are about to submit your answers, please click submit button to confirm if you are done and ready to go";
        if(isAuthor){
            message = "Confirm to save the correct answers. Your answer as the quiz creator enables participants identify their errors or the correct answers";
        }
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_baseline_error_outline_24);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                processAndSubmitAnswer();

            }
        })
                .setNegativeButton("Cancel", null);
        confirmationDialog = builder.create();
        confirmationDialog.show();

    }

    private void toggleProgress(boolean show)
    {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
    }
    private void showQuizSuccessDialog(){

        AlertDialog successDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Congrats, quiz submitted successfully");
        String message = "We will notify you when your answer is marked by the author. We encourage your participation, join other open quizzes to participate";
        if(isAuthor){
            message = "Answer is saved and ready to view";
        }
        builder.setMessage(message);
                builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_baseline_error_outline_24);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!isAuthor) {
                    QuizActivity.super.onBackPressed();
                }
            }
        });
        successDialog = builder.create();
        successDialog.show();

    }

    private void processAndSubmitAnswer() {
        toggleProgress(true);
        isSubmitted = true;
        if(GlobalConfig.getCurrentUserId().equalsIgnoreCase(authorId)){
        GlobalConfig.saveAuthorQuizAnswer(this, quizId, answersList, new GlobalConfig.ActionCallback() {
            @Override
            public void onSuccess() {

                String notificationId = GlobalConfig.getRandomString(100);
                //contains ids to the participants who receive the notification
                ArrayList<String> receiversIdList = new ArrayList<>();
                receiversIdList.addAll(quizDataModel.getParticipantsList());
                //carries the info about the quiz
                 ArrayList<String> modelInfo = new ArrayList<>();
                 modelInfo.add(quizId);

                //fires out the notification
                GlobalConfig.sendNotificationToUsers(GlobalConfig.NOTIFICATION_TYPE_QUIZ_KEY,notificationId,receiversIdList,modelInfo,quizDataModel.getQuizTitle(),"Author has posted correct answers to the quiz you participated",null);


                toggleProgress(false);
                showQuizSuccessDialog();
                isSubmitted = true;
                GlobalConfig.authorRecentlySavedQuizAnswerIdList.add(quizId);
                GlobalConfig.authorRecentlySavedAnswersListMap.put(quizId,answersList);
            }

            @Override
            public void onFailed(String errorMessage) {
                toggleProgress(false);
                isSubmitted = false;
                GlobalConfig.createSnackBar2(QuizActivity.this, submitActionButton, "Sorry! your answer failed to submit, please try again", "Retry", Snackbar.LENGTH_INDEFINITE, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        processAndSubmitAnswer();
                    }
                });

            }
        });
    }else{
        GlobalConfig.submitQuiz(this, quizId, answersList, new GlobalConfig.ActionCallback() {
            @Override
            public void onSuccess() {
                toggleProgress(false);
                showQuizSuccessDialog();
                isSubmitted = true;
            }

            @Override
            public void onFailed(String errorMessage) {
                toggleProgress(false);
                isSubmitted = false;
                GlobalConfig.createSnackBar2(QuizActivity.this, submitActionButton, "Sorry! your answer failed to submit, please try again", "Retry", Snackbar.LENGTH_INDEFINITE, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        processAndSubmitAnswer();
                    }
                });

            }
        });
    }
    }

    private void updateQuizCountDownTimer(){
        countDownTimer = new CountDownTimer(9000000000000000000L,1000) {
            @Override
            public void onTick(long l) {

                //count down time for the quiz
                timeRemain = timeRemain-1;
                maxTimeTextView.setText("Time "+timeRemain+"s/"+totalTimeLimit+"s");
                if(timeRemain == 5){
                    maxTimeTextView.setTextColor(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
                }
                if(timeRemain == 0 && !isSubmitted && !isAuthor){
                    processAndSubmitAnswer();
                }
                //count down time for updating single question
        for(int i=0; i<viewedPositions.size();i++){
            if(timeRemainMap.get(i) != 0) {
                timeRemainMap.put(i, (timeRemainMap.get(i) - 1));
            }
}

if(timeRemainMap.get(activePosition) == 0) {
    if(!isAuthor){
    //question time elapsed, do something to round up for this particular question either closing it or disabling it
    activeQuestionTextView.setText("Time elapsed");
    activeQuestionTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.error_red, getTheme()));
    activeAnswerInput.setEnabled(false);
    activeAnswerRadioOption1.setEnabled(false);
    activeAnswerRadioOption2.setEnabled(false);
    activeAnswerRadioOption3.setEnabled(false);
    activeAnswerRadioOption4.setEnabled(false);

    activeQuestionTimeRemainTextView.setText(timeRemainMap.get(activePosition) + "s");
}
}else{
    activeQuestionTimeRemainTextView.setText(timeRemainMap.get(activePosition)+"s");

    if(timeRemainMap.get(activePosition) == 5){
        activeQuestionTimeRemainTextView.setTextColor(ResourcesCompat.getColor(getResources(),R.color.error_red,getTheme()));
    }
}
            }

            @Override
            public void onFinish() {

            //may never finish

            }
        }.start();

    }

    private void renderQuiz(QuizDataModel quizDataModel){
        this.quizDataModel = quizDataModel;
        toolbar.setTitle(quizDataModel.getQuizTitle());
        descriptionTextView.setText(quizDataModel.getQuizDescription());
        if(isAuthor){
            joinActionTextView.setText("You are the author");
        }else {
            if (quizDataModel.getParticipantsList().contains(GlobalConfig.getCurrentUserId()) || GlobalConfig.newlyJoinedQuizList.contains(quizId)) {
                joinActionTextView.setText("Joined");
                isJoined = true;
            } else {
                joinActionTextView.setText("Join");
                if (quizDataModel.isClosed()) {
                    joinActionTextView.setEnabled(false);
                    statusTextView.setText("Closed");
                } else {
                    joinActionTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            joinQuiz();
                        }
                    });
                }

            }
        }

        if(quizDataModel.isClosed()){
            statusTextView.setText("Closed");
        }
         isSubmitted = GlobalConfig.isQuizSubmitted(QuizActivity.this,quizId);
        totalTimeLimit = quizDataModel.getTotalTimeLimit();
        timeRemain = totalTimeLimit;

        maxTimeTextView.setText("Time "+timeRemain+"s/"+totalTimeLimit+"s");
//        CountDownTimer countDownTimer = new CountDownTimer(quizDataModel.getTotalTimeLimit()*1000,1000) {
//            @Override
//            public void onTick(long l) {
//                timeRemain = timeRemain-1;
//                maxTimeTextView.setText("Time "+timeRemain+"s/"+totalTimeLimit+"s");
//
//                if(timeRemain == 5){
//                    maxTimeTextView.setTextColor(ResourcesCompat.getColor(getResources(),R.color.error_red,getTheme()));
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                processAndSubmitAnswer();
//            }
//        }.start();

        participantCountTextView.setText(quizDataModel.getTotalParticipants()+"");
        viewCountTextView.setText(quizDataModel.getTotalViews()+"");
        datePostedTextView.setText(quizDataModel.getDateCreated()+"");
        questionCountTextView.setText("Total "+quizDataModel.getTotalQuestions()+"");

        questionList = quizDataModel.getQuestionList();
        //Init answers list to be updated in future
        for(int i=0; i<questionList.size();i++){
            //add two items
            ArrayList<String> answerItem = new ArrayList<>();
            //indicates the type of question, either theory or objective
            answerItem.add("");
            //stores answer, using 0 as default value
            answerItem.add("0");
            //saves the question text
            answerItem.add(questionList.size()>=4?questionList.get(3)+"":"Unknown error");
            //flags the answer as unmarked
            answerItem.add("UNMARKED");
            answersList.add(answerItem);

            //init time remain
            timeRemainMap.put(i,i);
        }
        if(isAuthor){
            displayQuestion(0);
            renderParticipantInfo();
            updateQuizCountDownTimer();

        }
        else {
//        if user has joined then display the question, else hide
            if (isJoined) {
                displayQuestion(0);

                if (isSubmitted) {

                    submitActionButton.setVisibility(View.GONE);
                    statusTextView.setText("Submitted");
                    renderParticipantInfo();

                } else {
                    updateQuizCountDownTimer();
                    submitActionButton.setVisibility(View.VISIBLE);
                }
            } else {
                submitActionButton.setVisibility(View.GONE);
            }
        }
    }

    private void displayQuestion(int questionNumber){
        ArrayList questionItem = (ArrayList) questionList.get(questionNumber);

        if( (questionItem.get(0)+"").equals(GlobalConfig.IS_THEORY_QUESTION_KEY)){
            displayTheoryQuestion(questionItem,questionNumber);
        }
        else if( (questionItem.get(0)+"").equals(GlobalConfig.IS_OBJECTIVE_QUESTION_KEY)){
            displayObjectiveQuestion(questionItem,questionNumber);
        }

        questionCountTextView.setText(questionNumber+1+"/"+(questionList.size()));
    }

    private void displayTheoryQuestion(ArrayList questionItem,int questionNumber) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View questionView = layoutInflater.inflate(R.layout.theory_question_item_preview, containerLinearLayout, false);

        Button prevButton = questionView.findViewById(R.id.previousButtonId);
        Button nextButton = questionView.findViewById(R.id.nextButtonId);
        Button submitButton = questionView.findViewById(R.id.submitButtonId);

        int maxTime = Integer.parseInt(questionItem.get(2) + "");
        int[] elapsedTime = new int[1];
        elapsedTime[0] = maxTime;

        TextView maxTimeTextView = questionView.findViewById(R.id.maxTimeTextViewId);
        maxTimeTextView.setText(maxTime + "s");

        TextView questionTextView = questionView.findViewById(R.id.questionTextViewId);
        questionTextView.setText(questionItem.get(3) + "");

        TextInputEditText answerInput = questionView.findViewById(R.id.answerInputId);
//        String answer = answerInput.getText()+"";


        answerInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String answer = answerInput.getText()+"";
                ArrayList<String> answerItem = answersList.get(questionNumber);
                answerItem.clear();
                answerItem.add(GlobalConfig.IS_THEORY_QUESTION_KEY);
                answerItem.add(answer);
                answerItem.add(questionTextView.getText()+"");
                answerItem.add("UNMARKED");

            }
        });
        //retrieve and set the answer
        ArrayList<String> answerItem = answersList.get(questionNumber);
        if(!answerItem.get(1).equalsIgnoreCase("0")){
            answerInput.setText(answerItem.get(1));
        }

        TextView completionStatusView = questionView.findViewById(R.id.completionStatusViewId);

        TextView timeRemainTextView = questionView.findViewById(R.id.remianTimeTextViewId);
//        CountDownTimer countDownTimer = new CountDownTimer(maxTime*1000,1000) {
//            @Override
//            public void onTick(long l) {
//                elapsedTime[0] = elapsedTime[0]-1;
//                elapsedTimeTextView.setText(elapsedTime[0]+"s");
//
//                if(elapsedTime[0] == 5){
//                    elapsedTimeTextView.setTextColor(ResourcesCompat.getColor(getResources(),R.color.error_red,getTheme()));
//                }
//            }
//
//            @Override
//            public void onFinish() {
////                questionTextView.setText("Time elapsed");
////                questionTextView.setTextColor(ResourcesCompat.getColor(getResources(),R.color.error_red,getTheme()));
////                answerInput.setEnabled(false);
////                completionStatusView.setVisibility(View.VISIBLE);
//            }
//        }.start();

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(questionNumber>0){
                    displayQuestion(questionNumber-1);
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((questionList.size()-1)>questionNumber){
                    displayQuestion(questionNumber+1);
                }
            }
        });

//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        containerLinearLayout.removeAllViews();
        containerLinearLayout.addView(questionView);
        activePosition = questionNumber;
        activeQuestionTextView = questionTextView;
        activeAnswerInput = answerInput;
        activeQuestionTimeRemainTextView = timeRemainTextView;

        if(!viewedPositions.contains(questionNumber)) {
            viewedPositions.add(questionNumber);
            timeRemainMap.put(questionNumber,maxTime);

        }
        if(!isAuthor){

            if(timeRemainMap.get(activePosition) == 0) {
                //question time elapsed, do something to round up for this particular question either closing it or disabling it
                activeQuestionTextView.setText("Time elapsed");
                activeQuestionTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.error_red, getTheme()));
                activeAnswerInput.setEnabled(false);

                activeQuestionTimeRemainTextView.setText(timeRemainMap.get(activePosition) + "s");
            }
        }
        GlobalConfig.recordViewedQuiz(this,quizId);
    }
    private void displayObjectiveQuestion(ArrayList questionItem,int questionNumber){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View questionView  =  layoutInflater.inflate(R.layout.objective_question_item_preview,containerLinearLayout,false);

        Button prevButton = questionView.findViewById(R.id.previousButtonId);
        Button nextButton = questionView.findViewById(R.id.nextButtonId);
        Button submitButton = questionView.findViewById(R.id.submitButtonId);

        int maxTime = Integer.parseInt(questionItem.get(2)+"");
        int[] elapsedTime= new int[1];
        elapsedTime[0]= maxTime;

        TextView maxTimeTextView = questionView.findViewById(R.id.maxTimeTextViewId);
        maxTimeTextView.setText(maxTime+"s");

        TextView questionTextView = questionView.findViewById(R.id.questionTextViewId);
        questionTextView.setText(questionItem.get(3)+"");

        RadioButton radioOption1 =  questionView.findViewById(R.id.radioOption1);
        radioOption1.setText(questionItem.get(4)+"");
        radioOption1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ArrayList<String> answerItem = answersList.get(questionNumber);
                    answerItem.clear();
                    answerItem.add(GlobalConfig.IS_OBJECTIVE_QUESTION_KEY);
                    answerItem.add("1-"+radioOption1.getText());
                    answerItem.add(questionTextView.getText()+"");
                    answerItem.add("UNMARKED");

                }
            }
        });
        RadioButton radioOption2 =  questionView.findViewById(R.id.radioOption2);
        radioOption2.setText(questionItem.get(5)+"");
        radioOption2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ArrayList<String> answerItem = answersList.get(questionNumber);
                    answerItem.clear();
                    answerItem.add(GlobalConfig.IS_OBJECTIVE_QUESTION_KEY);
                    answerItem.add("2-"+radioOption2.getText());
                    answerItem.add(questionTextView.getText()+"");
                    answerItem.add("UNMARKED");

                }
            }
        });
        RadioButton radioOption3 =  questionView.findViewById(R.id.radioOption3);
        radioOption3.setText(questionItem.get(6)+"");
        radioOption3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ArrayList<String> answerItem = answersList.get(questionNumber);
                    answerItem.clear();
                    answerItem.add(GlobalConfig.IS_OBJECTIVE_QUESTION_KEY);
                    answerItem.add("3-"+radioOption3.getText());
                    answerItem.add(questionTextView.getText()+"");
                    answerItem.add("UNMARKED");

                }
            }
        });
        RadioButton radioOption4 =  questionView.findViewById(R.id.radioOption4);
        radioOption4.setText(questionItem.get(7)+"");
        radioOption4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ArrayList<String> answerItem = answersList.get(questionNumber);
                    answerItem.clear();
                    answerItem.add(GlobalConfig.IS_OBJECTIVE_QUESTION_KEY);
                    answerItem.add("4-"+radioOption4.getText());
                    answerItem.add(questionTextView.getText()+"");
                    answerItem.add("UNMARKED");

                }
            }
        });
        //update the option chosen by particuipant if he has chosen already
        ArrayList<String> answerItem = answersList.get(questionNumber);
//     todo   answerItem.add(GlobalConfig.IS_OBJECTIVE_QUESTION_KEY);
        int optionPosition = Integer.parseInt(answerItem.get(1).split("-")[0]);
        switch(optionPosition){
            case 1:radioOption1.setChecked(true);break;
            case 2:radioOption2.setChecked(true);break;
            case 3:radioOption3.setChecked(true);break;
            case 4:radioOption4.setChecked(true);break;
            default:
                answerItem.clear();
                answerItem.add(GlobalConfig.IS_OBJECTIVE_QUESTION_KEY);
                answerItem.add("1-"+radioOption1.getText());
                answerItem.add(questionTextView.getText()+"");
                answerItem.add("UNMARKED");

        }

        TextView completionStatusView = questionView.findViewById(R.id.completionStatusViewId);

        TextView timeRemainTextView = questionView.findViewById(R.id.remianTimeTextViewId);
//        CountDownTimer countDownTimer = new CountDownTimer(maxTime*1000,1000) {
//            @Override
//            public void onTick(long l) {
//                elapsedTime[0] = elapsedTime[0]-1;
//                elapsedTimeTextView.setText(elapsedTime[0]+"s");
//
//                if(elapsedTime[0] == 5){
//                    elapsedTimeTextView.setTextColor(ResourcesCompat.getColor(getResources(),R.color.error_red,getTheme()));
//                }
//            }
//
//            @Override
//            public void onFinish() {
////                questionTextView.setText("Time elapsed");
////                questionTextView.setTextColor(ResourcesCompat.getColor(getResources(),R.color.error_red,getTheme()));
////                answerInput.setEnabled(false);
////                completionStatusView.setVisibility(View.VISIBLE);
//            }
//        }.start();

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(questionNumber>0){
                    displayQuestion(questionNumber-1);
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((questionList.size()-1)>questionNumber){
                    displayQuestion(questionNumber+1);
                }
            }
        });
//
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        containerLinearLayout.removeAllViews();
        containerLinearLayout.addView(questionView);
        activePosition = questionNumber;
        activeQuestionTextView = questionTextView;
        activeAnswerRadioOption1 = radioOption1;
        activeAnswerRadioOption2 = radioOption2;
        activeAnswerRadioOption3 = radioOption3;
        activeAnswerRadioOption4 = radioOption4;
        activeQuestionTimeRemainTextView = timeRemainTextView;

        if(!viewedPositions.contains(questionNumber)) {
            viewedPositions.add(questionNumber);
            timeRemainMap.put(questionNumber,maxTime);

        }
        if(!isAuthor) {

            if (timeRemainMap.get(activePosition) == 0) {
                //question time elapsed, do something to round up for this particular question either closing it or disabling it
                activeQuestionTextView.setText("Time elapsed");
                activeQuestionTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.error_red, getTheme()));
                activeAnswerRadioOption1.setEnabled(false);
                activeAnswerRadioOption2.setEnabled(false);
                activeAnswerRadioOption3.setEnabled(false);
                activeAnswerRadioOption4.setEnabled(false);

                activeQuestionTimeRemainTextView.setText(timeRemainMap.get(activePosition) + "s");

            }
        }

        GlobalConfig.incrementQuizViews(this,quizId);
        GlobalConfig.recordViewedQuiz(this,quizId);
    }
    private void joinQuiz(){
        joinActionTextView.setText("Joining...");
        //set the onclicklistener to do nothing
        joinActionTextView.setOnClickListener(v->{

        });
        GlobalConfig.joinQuiz(QuizActivity.this, quizId, new GlobalConfig.ActionCallback() {
            @Override
            public void onSuccess() {
                //set the onclicklistener to do nothing after it had succeeded
                joinActionTextView.setOnClickListener(v->{

                });
                joinActionTextView.setText("Joined");

                GlobalConfig.newlyJoinedQuizList.add(quizId);
//               ArrayList<String> list = quizDataModel.getParticipantsList();
//                list.add(GlobalConfig.getCurrentUserId());
//                quizDataModel.setParticipantsList(list);

//                display question after joining
                displayQuestion(0);
                renderParticipantInfo();
                if(!isSubmitted) {
//                    check if he has submitted, so if he has not submitted then start countdowntimer
                    updateQuizCountDownTimer();
                    submitActionButton.setVisibility(View.VISIBLE);

                }else{
                    submitActionButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                joinActionTextView.setText("Try again");
                //set the onclicklistener to trigger joining quiz again after it had failed
                joinActionTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        joinQuiz();
                    }
                });
            }
        });
    }

    void getAuthorInfo(){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(authorId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String userProfilePhotoDownloadUrl = "" + documentSnapshot.get(GlobalConfig.USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY);
                        try {
                            Glide.with(QuizActivity.this)
                                    .load(userProfilePhotoDownloadUrl)
                                    .centerCrop()
                                    .placeholder(R.drawable.default_profile)
                                    .into(posterPhoto);
                        } catch (Exception ignored) {
                        }

                        String userDisplayName = "" + documentSnapshot.get(GlobalConfig.USER_DISPLAY_NAME_KEY);
                        posterNameTextView.setText(userDisplayName);

                        boolean isVerified = documentSnapshot.get(GlobalConfig.IS_ACCOUNT_VERIFIED_KEY)!=null?documentSnapshot.getBoolean(GlobalConfig.IS_ACCOUNT_VERIFIED_KEY):false;
                        if(isVerified){
                            verificationFlagImageView.setVisibility(View.VISIBLE);
                        }else{
                            verificationFlagImageView.setVisibility(View.INVISIBLE);

                        }
                    }
                });

    }
    void getParticipants(){
        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_QUIZ_KEY).document(quizId)
                .collection(GlobalConfig.ALL_PARTICIPANTS_KEY)
                .get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots) {

//                    String participantId = "" + documentSnapshot.get(GlobalConfig.PARTICIPANT_ID_KEY);
                    String participantId = "" + documentSnapshot.getId();
                    String dateJoined = "" + documentSnapshot.get(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY);
                    boolean isSubmitted = documentSnapshot.get(GlobalConfig.IS_ANSWER_SUBMITTED_KEY) != null ? documentSnapshot.getBoolean(GlobalConfig.IS_ANSWER_SUBMITTED_KEY) : false;

                    if((participantId+"").equalsIgnoreCase(GlobalConfig.getCurrentUserId())){
                        QuizActivity.this.isSubmitted=isSubmitted;
                        GlobalConfig.recordSubmittedQuiz(QuizActivity.this,quizId);
                        if(isSubmitted){
                            submitActionButton.setVisibility(View.GONE);
                            statusTextView.setText("Submitted");

                            if(countDownTimer!=null){
                                countDownTimer.cancel();
                            }
                        }
                    }
                    ArrayList<ArrayList<String>> answerList = new ArrayList<>();
                    for(int i=0; i<questionList.size(); i++) {
                        //list of participant answer
                        ArrayList<String> answerItem = documentSnapshot.get(GlobalConfig.ANSWER_LIST_KEY + "-" + i) != null ? (ArrayList<String>) documentSnapshot.get(GlobalConfig.ANSWER_LIST_KEY + "-" + i) : new ArrayList<>();
                        answerList.add(answerItem);


                    }
                    String timeSubmitted = documentSnapshot.get(GlobalConfig.ANSWER_SUBMITTED_TIME_STAMP_KEY)!=null? documentSnapshot.getTimestamp(GlobalConfig.ANSWER_SUBMITTED_TIME_STAMP_KEY).toDate()+"" :"Undefined";

                    QuizParticipantDatamodel quizParticipantDatamodel = new QuizParticipantDatamodel(false,participantId,dateJoined,isSubmitted,timeSubmitted,answerList);
                    quizParticipantDatamodels.add(quizParticipantDatamodel);
                    quizParticipantRCVAdapter.notifyItemChanged(quizParticipantDatamodels.size());
                }
            }
        });

    }

    void renderQuizFromOnline(){
        GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUIZ_KEY).document(quizId).get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuizActivity.this, "Error: Failed to load quiz", Toast.LENGTH_SHORT).show();
                QuizActivity.super.onBackPressed();
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String quizId = ""+ documentSnapshot.getId();
                    String authorId = ""+ documentSnapshot.get(GlobalConfig.AUTHOR_ID_KEY);
                    String quizTitle = ""+ documentSnapshot.get(GlobalConfig.QUIZ_TITLE_KEY);
                    String quizDescription = ""+ documentSnapshot.get(GlobalConfig.QUIZ_DESCRIPTION_KEY);
                    String quizFeeDescription = ""+ documentSnapshot.get(GlobalConfig.QUIZ_FEE_DESCRIPTION_KEY);
                    String quizRewardDescription = ""+ documentSnapshot.get(GlobalConfig.QUIZ_REWARD_DESCRIPTION_KEY);
                    long totalQuestions =  documentSnapshot.get(GlobalConfig.TOTAL_QUESTIONS_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_QUESTIONS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_QUESTIONS_KEY) : 0L;
                    long totalTimeLimit =  documentSnapshot.get(GlobalConfig.TOTAL_TIME_LIMIT_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_TIME_LIMIT_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_TIME_LIMIT_KEY) : 0L;
                    long totalParticipants =  documentSnapshot.get(GlobalConfig.TOTAL_PARTICIPANTS_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_PARTICIPANTS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_PARTICIPANTS_KEY) : 0L;
                    long totalViews =  documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_VIEWS_KEY) != null && documentSnapshot.get(GlobalConfig.TOTAL_NUMBER_OF_VIEWS_KEY) instanceof Long ? documentSnapshot.getLong(GlobalConfig.TOTAL_NUMBER_OF_VIEWS_KEY) : 0L;
                    boolean isPublic =  documentSnapshot.get(GlobalConfig.IS_PUBLIC_KEY) != null && documentSnapshot.get(GlobalConfig.IS_PUBLIC_KEY) instanceof Boolean ? documentSnapshot.getBoolean(GlobalConfig.IS_PUBLIC_KEY) : true;
                    boolean isClosed =  documentSnapshot.get(GlobalConfig.IS_CLOSED_KEY) != null && documentSnapshot.get(GlobalConfig.IS_CLOSED_KEY) instanceof Boolean ? documentSnapshot.getBoolean(GlobalConfig.IS_CLOSED_KEY) : false;
                    ArrayList<ArrayList> questionList = new ArrayList();
                    for(int i=0;i<totalQuestions;i++) {
                        ArrayList questionList1 = documentSnapshot.get(GlobalConfig.QUESTION_LIST_KEY+"-" + i) != null && documentSnapshot.get(GlobalConfig.QUESTION_LIST_KEY+"-"+ i) instanceof ArrayList ? (ArrayList) documentSnapshot.get(GlobalConfig.QUESTION_LIST_KEY+"-"+ i) : new ArrayList();
                        questionList.add(questionList1);
                    }

                    ArrayList dateList =  documentSnapshot.get(GlobalConfig.QUIZ_DATE_LIST_KEY) != null && documentSnapshot.get(GlobalConfig.QUIZ_DATE_LIST_KEY) instanceof ArrayList ? (ArrayList) documentSnapshot.get(GlobalConfig.QUIZ_DATE_LIST_KEY) : new ArrayList();
                    ArrayList participantsList =  documentSnapshot.get(GlobalConfig.PARTICIPANTS_LIST_KEY) != null && documentSnapshot.get(GlobalConfig.QUIZ_DATE_LIST_KEY) instanceof ArrayList ? (ArrayList) documentSnapshot.get(GlobalConfig.PARTICIPANTS_LIST_KEY) : new ArrayList();
                    String dateCreated = documentSnapshot.get(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY) != null && documentSnapshot.get(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY) instanceof Timestamp ? "" + documentSnapshot.getTimestamp(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY).toDate() : "Moment ago";
                    if (dateCreated.length() > 10) {
                        dateCreated = dateCreated.substring(0, 10);
                    }
                    String dateEdited = documentSnapshot.get(GlobalConfig.DATE_EDITED_TIME_STAMP_KEY) != null && documentSnapshot.get(GlobalConfig.LIBRARY_DATE_CREATED_TIME_STAMP_KEY) instanceof Timestamp ? "" + documentSnapshot.getTimestamp(GlobalConfig.DATE_EDITED_TIME_STAMP_KEY).toDate() : "Moment ago";
                    if (dateEdited.length() > 10) {
                        dateEdited = dateEdited.substring(0, 10);
                    }
                    String timeAnswerSubmitted = documentSnapshot.get(GlobalConfig.ANSWER_SUBMITTED_TIME_STAMP_KEY)!=null? documentSnapshot.getTimestamp(GlobalConfig.ANSWER_SUBMITTED_TIME_STAMP_KEY).toDate()+"" :"Undefined";
                    boolean isAnswerSaved = documentSnapshot.get(GlobalConfig.IS_ANSWER_SUBMITTED_KEY)!=null? documentSnapshot.getBoolean(GlobalConfig.IS_ANSWER_SUBMITTED_KEY) :false;

                    ArrayList<ArrayList<String>> authorSavedAnswersList = new ArrayList<>();
                    for(int i=0; i<questionList.size(); i++) {
                        //list of participant answer
                        ArrayList<String> answerItem = documentSnapshot.get(GlobalConfig.ANSWER_LIST_KEY + "-" + i) != null ? (ArrayList<String>) documentSnapshot.get(GlobalConfig.ANSWER_LIST_KEY + "-" + i) : new ArrayList<>();
                        authorSavedAnswersList.add(answerItem);

                    }
                   renderQuiz(new QuizDataModel(
                            quizId,
                            authorId,
                            quizTitle,
                            quizDescription,
                            quizFeeDescription,
                            quizRewardDescription,
                            dateCreated,
                            dateEdited,
                            totalQuestions,
                            totalTimeLimit,
                            totalParticipants,
                            totalViews,
                            isPublic,
                            isClosed,
                            questionList,
                            dateList,
                            participantsList,
                            isAnswerSaved,
                            timeAnswerSubmitted,
                            authorSavedAnswersList
                    ));




            }
        });
    }
    void renderParticipantInfo(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        quizParticipantRCVAdapter = new QuizParticipantRCVAdapter(quizParticipantDatamodels,this,quizDataModel.getAuthorSavedAnswersList(),quizId,quizDataModel,authorId);
        participantRecyclerView.setHasFixedSize(false);
        participantRecyclerView.setAdapter(quizParticipantRCVAdapter);
        participantRecyclerView.setLayoutManager(linearLayoutManager);

        renderAuthorAnswer();
        getParticipants();
    }
    void renderAuthorAnswer(){
        QuizParticipantDatamodel quizParticipantDatamodel = new QuizParticipantDatamodel(true,authorId,"Undefined", !quizDataModel.isAnswerSaved() ?GlobalConfig.authorRecentlySavedQuizAnswerIdList.contains(quizId):true, quizDataModel.getTimeAnswerSubmitted(), !GlobalConfig.authorRecentlySavedQuizAnswerIdList.contains(quizId)?quizDataModel.getAuthorSavedAnswersList():GlobalConfig.authorRecentlySavedAnswersListMap.get(quizId));
        quizParticipantDatamodels.add(quizParticipantDatamodel);
        quizParticipantRCVAdapter.notifyItemChanged(quizParticipantDatamodels.size());

    }
    interface CountDownCallback{
        void onCount();
    }
}
