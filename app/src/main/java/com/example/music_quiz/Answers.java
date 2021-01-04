package com.example.music_quiz;

import androidx.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collections;

public class Answers {
    @VisibleForTesting
    protected ArrayList answers = new ArrayList<String>();
    private static String ANSWER_EXCEPTION = "ANSWER_EXCEPTION";
    public void addAnswers(String track){
        if(answers.size() < 4) {
            answers.add(track);
        }
        else {
            answers.set(0, track);
        }
    }
    public ArrayList fetchAnswers() throws Exception {
        if(answers != null) {
            return answers;
        }
        else{
            throw new Exception(ANSWER_EXCEPTION);
        }
    }
    public void clearAnswers(){
      answers.clear();
    };
    public Boolean verifyAnswer(String title, int index){
        if(answers.get(index).equals(title)){
            return true;
        }
        else{
            return false;
        }
    };


}
