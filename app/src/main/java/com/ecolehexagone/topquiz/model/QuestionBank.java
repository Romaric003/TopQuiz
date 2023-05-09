package com.ecolehexagone.topquiz.model;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
// ensemble de question

public class QuestionBank implements Serializable {

    private final List<Question> mQuestionList;
    private int mQuestionIndex;

    public QuestionBank(List<Question> questionList) {
        mQuestionList = questionList;

        Collections.shuffle(mQuestionList);
    }

    public Question getCurrentQuestion() {
        return mQuestionList.get(mQuestionIndex);
    }

    public Question getNextQuestion() {
        mQuestionIndex++;
        return getCurrentQuestion();
    }
}

