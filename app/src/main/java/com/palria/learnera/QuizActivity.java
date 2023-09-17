package com.palria.learnera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.palria.learnera.models.QuestionDataModel;
import com.palria.learnera.models.QuizDataModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    ImageButton backButton;

    Toolbar toolbar;
    String authorId;
    String quizId;
    TextView maxTimeTextView;
    TextView joinActionTextView;
    TextView statusTextView;
    TextView participantCountTextView;
    TextView viewCountTextView;
    TextView datePostedTextView;
    TextView questionCountTextView;
    TextView posterNameTextView;
    ImageView posterPhoto;
    FloatingActionButton submitActionButton;
    AlertDialog alertDialog;
    QuizDataModel quizDataModel;
    ArrayList questionList = new ArrayList();
    ArrayList<ArrayList<String>> answersList = new ArrayList();
    LinearLayout containerLinearLayout;
    long totalTimeLimit = 0;
    long timeRemain = 0;
    HashMap<Integer,Integer> timeRemainMap = new HashMap<>();
    CountDownTimer countDownTimer;
    int activePosition = -1;
    TextView activeQuestionTimeRemainTextView ;
    TextView activeQuestionTextView ;
    View activeAnswerInput;
    ArrayList<Integer> viewedPositions = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        fetchIntentData();
        initUI();
        submitActionButton.setOnClickListener(view ->{
            showQuizCompletionConfirmationDialog();
        });
        joinActionTextView.setOnClickListener(view ->{
           joinQuiz();
        });
        renderQuiz();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDownTimer.cancel();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        QuizActivity.super.onBackPressed();

        return true;
    }
    private void initUI(){

        //init programmatically

         activeQuestionTimeRemainTextView = new TextView(this);
         activeQuestionTextView  = new TextView(this);
         activeAnswerInput  = new View(this);

        backButton = findViewById(R.id.backButtonId);
        toolbar = findViewById(R.id.topBar);
        maxTimeTextView = findViewById(R.id.maxTimeTextViewId);
        joinActionTextView = findViewById(R.id.joinQuizActionTextViewId);
        statusTextView = findViewById(R.id.activeStatusTextViewId);
        participantCountTextView = findViewById(R.id.participantCountTextViewId);
        viewCountTextView = findViewById(R.id.viewCountTextViewId);
//        datePostedTextView = findViewById(R.id.dateCreatedTextViewId);
        questionCountTextView = findViewById(R.id.questionCountTextViewId);
        posterNameTextView = findViewById(R.id.posterNameTextViewId);
        posterPhoto = findViewById(R.id.posterProfilePhotoId);
        containerLinearLayout = findViewById(R.id.containerLinearLayoutId);
        submitActionButton = findViewById(R.id.submitActionButtonId);

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
    }
    private void showQuizCompletionConfirmationDialog(){

        AlertDialog confirmationDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you ready to submit?");
        builder.setMessage("You are about to submit your answers, please click submit button to confirm if you are done and ready to go");
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
        builder.setMessage("We will notify you when your answer is marked by the author. We encourage your participation, join other open quizzes to participate");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_baseline_error_outline_24);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            QuizActivity.super.onBackPressed();
            }
        });
        successDialog = builder.create();
        successDialog.show();

    }

    private void processAndSubmitAnswer(){
        toggleProgress(true);
        GlobalConfig.submitQuiz(this, quizId, answersList, new GlobalConfig.ActionCallback() {
            @Override
            public void onSuccess() {
                toggleProgress(false);
                showQuizSuccessDialog();
            }

            @Override
            public void onFailed(String errorMessage) {
                toggleProgress(false);
                GlobalConfig.createSnackBar2(QuizActivity.this, submitActionButton, "Sorry! your answer failed to submit, please try again", "Try again", Snackbar.LENGTH_INDEFINITE, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        processAndSubmitAnswer();
                    }
                });

            }
        });
    }

    private void updateQuizCountDownTimer(){
        countDownTimer = new CountDownTimer(9000000000000000000L,1000) {
            @Override
            public void onTick(long l) {

                //count down time for the quiz
                timeRemain = timeRemain-1;
                maxTimeTextView.setText("Time "+timeRemain+"s/"+totalTimeLimit+"s");
                if(timeRemain == 5){
                    maxTimeTextView.setTextColor(ResourcesCompat.getColor(getResources(),R.color.error_red,getTheme()));
                }
                if(timeRemain == 0){
                    processAndSubmitAnswer();
                }
                //count down time for updating single question
        for(int i=0; i<viewedPositions.size();i++){
            if(timeRemainMap.get(i) != 0) {
                timeRemainMap.put(i, (timeRemainMap.get(i) - 1));
            }
}

if(timeRemainMap.get(activePosition) == 0){
    activeQuestionTextView.setText("Time elapsed");
    activeQuestionTextView.setTextColor(ResourcesCompat.getColor(getResources(),R.color.error_red,getTheme()));
    activeAnswerInput.setEnabled(false);
    activeQuestionTimeRemainTextView.setText(timeRemainMap.get(activePosition)+"s");

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

    private void renderQuiz(){
        toolbar.setTitle(quizDataModel.getQuizTitle());
        if(quizDataModel.getParticipantsList().contains(GlobalConfig.getCurrentUserId())){
            joinActionTextView.setText("Joined");
        }
        else{
            joinActionTextView.setText("Join");
            if(quizDataModel.isClosed()){
                joinActionTextView.setEnabled(false);
            }else {
                joinActionTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }

        }

        if(quizDataModel.isClosed()){
            statusTextView.setText("Closed");
        }
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
//        datePostedTextView.setText(quizDataModel.getDateCreated()+"");
        questionCountTextView.setText("Total "+quizDataModel.getTotalQuestions()+"");

        questionList = quizDataModel.getQuestionList();
        //Init answers list to be updated in future
        for(int i=0; i<questionList.size();i++){
            //add two items
            ArrayList<String> answerItem = new ArrayList<>();
            //indicates the type of question
            answerItem.add("");
            //stores answer, using 0 as default value
            answerItem.add("0");
            answersList.add(answerItem);

            //init time remian
            timeRemainMap.put(i,i);
        }

        displayQuestion(0);
        updateQuizCountDownTimer();
    }

    private void displayQuestion(int questionNumber){
        ArrayList questionItem = (ArrayList) questionList.get(questionNumber);

        if( (questionItem.get(0)+"").equals(GlobalConfig.IS_THEORY_QUESTION_KEY)){
            displayTheoryQuestion(questionItem,questionNumber);
        }
        else if( (questionItem.get(0)+"").equals(GlobalConfig.IS_OBJECTIVE_QUESTION_KEY)){
            displayObjectiveQuestion(questionItem);
        }

        questionCountTextView.setText(questionNumber+1+"/"+(questionList.size()-1));
    }

    private void displayTheoryQuestion(ArrayList questionItem,int questionNumber){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View questionView  =  layoutInflater.inflate(R.layout.theory_question_item_preview,containerLinearLayout,false);

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

        TextInputEditText answerInput = questionView.findViewById(R.id.answerInputId);
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

            }
        });

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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        containerLinearLayout.removeAllViews();
        containerLinearLayout.addView(questionView);
        timeRemainMap.put(questionNumber,maxTime);
        activePosition = questionNumber;
        activeQuestionTextView = questionTextView;
        activeAnswerInput = answerInput;
        activeQuestionTimeRemainTextView = timeRemainTextView;

        if(!viewedPositions.contains(questionNumber)) {
            viewedPositions.add(questionNumber);
        }

        GlobalConfig.recordViewedQuiz(this,quizId);
    }
    private void joinQuiz(){
        GlobalConfig.joinQuiz(QuizActivity.this, quizId, new GlobalConfig.ActionCallback() {
            @Override
            public void onSuccess() {
                joinActionTextView.setOnClickListener(v->{

                });
                joinActionTextView.setText("Joined");
            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });
    }
    private void displayObjectiveQuestion(ArrayList questionItem){

    }

    interface CountDownCallback{
        void onCount();
    }
}
