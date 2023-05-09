package com.ecolehexagone.topquiz.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ecolehexagone.topquiz.R;
import com.ecolehexagone.topquiz.model.Question;
import com.ecolehexagone.topquiz.model.QuestionBank;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{


    TextView mTextView; // Questions
/* Choix possibles */
    Button mGameButton1;
    Button mGameButton2;
    Button mGameButton3;
    Button mGameButton4;
    /* Stocke info sur l'état de la partie */
    private final QuestionBank mQuestionBank = generateQuestionBank();

    // Creation et initialisation d'une liste de questions avec reponses
    Question mCurrentQuestion; // current q

    private int mRemainingQuestionCount; // nombre de q
    private int mScore; // score

    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    // stocke et recup final score
    public static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    //Stocke et recup current score
    public static final String BUNDLE_STATE_QUESTION = "BUNDLE_STATE_QUESTION";
    //stock et recup nombre de questions qui reste

    private boolean mEnableTouchEvents;
    // controle si touche toujours active

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION, mRemainingQuestionCount);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if(savedInstanceState != null){
            mRemainingQuestionCount = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
        }
        else {

        }
// Referencement
        mTextView = findViewById(R.id.game_activity_textview_question);

        mGameButton1 = findViewById(R.id.game_activity_button_1);
        mGameButton2 = findViewById(R.id.game_activity_button_2);
        mGameButton3 = findViewById(R.id.game_activity_button_3);
        mGameButton4 = findViewById(R.id.game_activity_button_4);

// Methode Onlick appelé à chaque clic
        mGameButton1.setOnClickListener(this);
        mGameButton2.setOnClickListener(this);
        mGameButton3.setOnClickListener(this);
        mGameButton4.setOnClickListener(this);

        mCurrentQuestion = mQuestionBank.getCurrentQuestion();

        displayQuestion(mCurrentQuestion);

        mEnableTouchEvents = true;

    }
    private void displayQuestion(final Question question) {
        mTextView.setText(question.getQuestion());
        mGameButton1.setText(question.getChoiceList().get(0));
        mGameButton2.setText(question.getChoiceList().get(1));
        mGameButton3.setText(question.getChoiceList().get(2));
        mGameButton4.setText(question.getChoiceList().get(3));
    } // permet MAJ l'affichage

    private QuestionBank generateQuestionBank() {
        Question question1 = new Question(
            "Qui est Lionel Messi ?",
            Arrays.asList(
                "Un peintre",
                "Un footballeur",
                "Un medecin",
                "Un ingénieur"
            ),
            1
        );

        Question question2 = new Question(
            "A quelle date l'homme a t'il marché sur la lune?",
            Arrays.asList(
                "1958",
                "1962",
                "1967",
                "1969"
            ),
            3
        );

        Question question3 = new Question(
            "Quel est le numéro de porte des Simpsons ?",
            Arrays.asList(
                "42",
                "101",
                "666",
                "742"
            ),
            3
        );

        return new QuestionBank(Arrays.asList(question1, question2, question3));
    }
// comparaison index de reponse choisie avec index bonne reponse
    @Override
    public void onClick(View view) {
        int index;

        if (view == mGameButton1) {
            index = 0;
        } else if (view == mGameButton2) {
            index = 1;
        } else if (view == mGameButton3) {
            index = 2;
        } else if (view == mGameButton4) {
            index = 3;
        } else {
            throw new IllegalStateException("Unknown clicked view : " + view);
        }

        if (index == mQuestionBank.getCurrentQuestion().getAnswerIndex()){
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            mScore ++;

        }
        else
        {
            Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
        }

        mEnableTouchEvents = false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRemainingQuestionCount--;

                if (mRemainingQuestionCount > 0) {
                    mCurrentQuestion = mQuestionBank.getNextQuestion();
                    displayQuestion(mCurrentQuestion);
                } else {
                    endGame();
                }
                mEnableTouchEvents= true;

            }
        }, 2000);


    }
    private void endGame(){ //affiche boite de dialogue avec score
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Bien !")
            .setMessage("Ton score est " + mScore)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent();
                    intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            })
            .create()
            .show();



    }
}
