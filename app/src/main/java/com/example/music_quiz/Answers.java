package com.example.music_quiz;

import androidx.annotation.VisibleForTesting;

import java.util.ArrayList;

public class Answers {
    @VisibleForTesting
    protected ArrayList answers = new ArrayList<String>();
    private static String ANSWER_EXCEPTION = "ANSWER_EXCEPTION";
    public void setAnswer(String track){
        if(answers.size() < 4) {
            answers.add(track);
        }
        else {
            answers.set(0, track);
        }
    }
    public ArrayList fetchAnswer() throws Exception {
        if(answers != null) {
            return answers;
        }
        else{
            throw new Exception(ANSWER_EXCEPTION);
        }
    }
    public void clearAnswer(){
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
