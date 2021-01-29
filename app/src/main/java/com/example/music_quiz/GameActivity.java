package com.example.music_quiz;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.PlayerApi;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.ListItem;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String CLIENT_ID = "afca0c6d0ea04e77b84465e4c5d9f2f3";
    private static final String REDIRECT_URI = "app://music.quiz";
    private static SpotifyAppRemote mSpotifyAppRemote;
    public PlayerState spotifyPlayerState;
    private String song;
    private String playlistUri;
    private ArrayList<String> answers = new ArrayList<String>();
    public int score = 0;
    private int incorrect = 0;
    private String checkedMark = "\u2713";
    private int buttonBlue = Color.parseColor("#2196F3");
    public static final String EXTRA_MESSAGE = "com.example.music_quiz.SCORE";
    private Button answerButton0;
    private Button answerButton1;
    private Button answerButton2;
    private Button answerButton3;
    private TextView incorrect0;
    private TextView incorrect1;
    private TextView incorrect2;
    private ProgressBar roundTime;
    private CountDownTimer roundTimer = new CountDownTimer(10000, 1000) {
        public void onTick(long millisUntilFinished) {
            int total = (int) ((float) millisUntilFinished);
            roundTime.setProgress(total);
            Log.d("Seconds Left", String.valueOf(total));
        }

        public void onFinish() {
            roundTime.setProgress(0);
            mSpotifyAppRemote.getPlayerApi().pause();
            incorrect();
            if(incorrect != 3) {
                mSpotifyAppRemote.getPlayerApi().skipNext()
                        .setResultCallback(response -> {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            startRound();
                        });
            }
        }
    };
    Connection connection = new Connection();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        answerButton0 = (Button)findViewById(R.id.answer0);
        answerButton0.setOnClickListener(this);
        answerButton0.setVisibility(View.GONE);
        answerButton0.setEnabled(false);


        roundTime = (ProgressBar)findViewById(R.id.roundTime);

        answerButton1 = (Button)findViewById(R.id.answer1);
        answerButton1.setOnClickListener(this);
        answerButton1.setVisibility(View.GONE);
        answerButton1.setEnabled(false);


        answerButton2 = (Button)findViewById(R.id.answer2);
        answerButton2.setOnClickListener(this);
        answerButton2.setVisibility(View.GONE);
        answerButton2.setEnabled(false);

        answerButton3 = (Button)findViewById(R.id.answer3);
        answerButton3.setOnClickListener(this);
        answerButton3.setVisibility(View.GONE);
        answerButton3.setEnabled(false);

        incorrect0 = (TextView) findViewById(R.id.incorrect0);
        incorrect1 = (TextView) findViewById(R.id.incorrect1);
        incorrect2 = (TextView) findViewById(R.id.incorrect2);

        Button end_game_button = (Button)findViewById(R.id.endGameButton);
        end_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endGame();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        playlistUri = getIntent().getStringExtra(PlaylistSelectionActivity.EXTRA_PLAYLIST_URI);

    }

    @Override
    protected void onResume(){
        super.onResume();
        // Set the connection parameters
        connection.connectSpotify(this, new ConnectionCallback() {
            @Override
            public void onSuccess() {
                Log.d("Quiz Connection:", "Successful");
                mSpotifyAppRemote = connection.getSpotifyRemote();
                connected();
            }

            @Override
            public void onError(String err) {
                Log.e("Quiz Connection:", "Failed");
                if(err.equals("connection err")){
                    //Spotify login
                }
                else if(err.equals("capabilities err")){
                    //Premium spotify needed
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSpotifyAppRemote.getPlayerApi().pause();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSpotifyAppRemote.getPlayerApi().pause();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected() {
        //Play on phone
        mSpotifyAppRemote.getConnectApi().connectSwitchToLocalDevice();
        //Shuffle playlist
        mSpotifyAppRemote.getPlayerApi().setShuffle(true);
        // Play
        mSpotifyAppRemote.getPlayerApi().play(playlistUri);
        startRound();

    }
    private void startRound(){
        answers.clear();
        roundTime.setMax(10000);
        roundTime.setProgress(10000);
        TextView scoreValue = (TextView) findViewById(R.id.score);
        scoreValue.setText(String.valueOf(score));
        mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    if (!answers.contains(playerState.track.name)) {
                        answers.add(playerState.track.name);
                        song = playerState.track.name;
                        if (answers.size() < 3) {
                            mSpotifyAppRemote.getPlayerApi().skipNext();
                        }
                        else if (answers.size() == 3) {
                            mSpotifyAppRemote.getPlayerApi().skipNext()
                            .setResultCallback(cb->{
                                Long startMs = nextLong(new Random(playerState.track.duration),((playerState.track.duration - 30000)));
                                mSpotifyAppRemote.getPlayerApi().seekToRelativePosition(startMs);
                                mSpotifyAppRemote.getPlayerApi().pause();

                            });
                        }
                        else{
                            setAnswers();
                    }
                    }
                });
}

    private void setAnswers(){

        Log.d("Answers", String.valueOf(answers));
        Collections.shuffle(answers);
        if(!answers.contains(this.song)){
        int randomAnswerIndex = new Random().nextInt(answers.size()+1);
        answers.set(randomAnswerIndex, this.song);
        }

        //Update answer button to song title
        answerButton0.setBackgroundColor(buttonBlue);
        answerButton0.setText(String.valueOf(this.answers.get(0)));

        answerButton1.setBackgroundColor(buttonBlue);
        answerButton1.setText(String.valueOf(this.answers.get(1)));

        answerButton2.setBackgroundColor(buttonBlue);
        answerButton2.setText(String.valueOf(this.answers.get(2)));

        answerButton3.setBackgroundColor(buttonBlue);
        answerButton3.setText(String.valueOf(this.answers.get(3)));
        fadeAnswersIn();
    }

    private void fadeAnswersIn(){
        int longAnimationDuration = getResources().getInteger(
        android.R.integer.config_longAnimTime);
        answerButton0.setAlpha(0f);
        answerButton0.setVisibility(View.VISIBLE);
        answerButton0.animate()
                .alpha(1f)
                .setDuration(longAnimationDuration)
                .setListener(new AnimatorListenerAdapter(){
                    @Override
                    public void onAnimationEnd(Animator animation){
                        answerButton1.setAlpha(0f);
                        answerButton1.setVisibility(View.VISIBLE);
                        answerButton1.animate()
                                .alpha(1f)
                                .setDuration(longAnimationDuration)
                                .setListener(new AnimatorListenerAdapter(){
                                    @Override
                                    public void onAnimationEnd(Animator animation){
                                        answerButton2.setAlpha(0f);
                                        answerButton2.setVisibility(View.VISIBLE);
                                        answerButton2.animate()
                                                .alpha(1f)
                                                .setDuration(longAnimationDuration)
                                                .setListener(new AnimatorListenerAdapter(){
                                                    @Override
                                                    public void onAnimationEnd(Animator animation){
                                                        answerButton3.setAlpha(0f);
                                                        answerButton3.setVisibility(View.VISIBLE);
                                                        answerButton3.animate()
                                                                .alpha(1f)
                                                                .setDuration(longAnimationDuration)
                                                                .setListener(new AnimatorListenerAdapter(){
                                                                    @Override
                                                                    public void onAnimationEnd(Animator animation){
                                                                        answerButton0.setEnabled(true);
                                                                        answerButton1.setEnabled(true);
                                                                        answerButton2.setEnabled(true);
                                                                        answerButton3.setEnabled(true);
                                                                       previewSong();
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                });
                    }
                });

    }
    private void previewSong(){
        mSpotifyAppRemote.getPlayerApi().resume();
        roundTime.setMax(10000);
        roundTimer.start();

    };
    public long nextLong(Random rng, long n) {
        // error checking and 2^x checking removed for simplicity.
        long bits, val;
        do {
            bits = (rng.nextLong() << 1) >>> 1;
            val = bits % n;
        } while (bits-val+(n-1) < 0L);
        return val;
    }

    private void fadeAnswersOut(){
        int shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        answerButton0.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter(){
                    @Override
                    public void onAnimationEnd(Animator animation){
                        answerButton0.setVisibility(View.GONE);
                    }
                });
        answerButton1.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter(){
                    @Override
                    public void onAnimationEnd(Animator animation){
                        answerButton1.setVisibility(View.GONE);
                    }
                });
        answerButton2.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter(){
                    @Override
                    public void onAnimationEnd(Animator animation){
                        answerButton2.setVisibility(View.GONE);
                    }
                });
        answerButton3.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter(){
                    @Override
                    public void onAnimationEnd(Animator animation){
                        answerButton3.setVisibility(View.GONE);
                    }
                });

    }

private void incorrect(){
    incorrect++;
    answerButton0.setEnabled(false);
    answerButton1.setEnabled(false);
    answerButton2.setEnabled(false);
    answerButton3.setEnabled(false);
    if(incorrect == 1){incorrect0.setTextColor(Color.RED);}
    else if(incorrect ==2){incorrect1.setTextColor(Color.RED);}
    else if(incorrect == 3){incorrect2.setTextColor(Color.RED);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        endGame();
    }
}

    public void endGame() {
        // Do something in response to button
        roundTimer.cancel();
        mSpotifyAppRemote.getPlayerApi().pause();
        Intent endGameActivity = new Intent(this, EndGameActivity.class);
        endGameActivity.putExtra(EXTRA_MESSAGE, String.valueOf(this.score));
        startActivity(endGameActivity);
    }
    @Override
    public void onClick(View v) {
        roundTimer.cancel();
        switch (v.getId()){
            case R.id.answer0:
                if(answerButton0.getText().equals(song)) {
                    score++;
                    answerButton0.setBackgroundColor(Color.GREEN);
                    answerButton0.setText(checkedMark);
                    Log.d("CORRECT! Your score", String.valueOf(score));
                }
                else{
                    Log.d("Wrong, it was..", String.valueOf(song));
                    answerButton0.setBackgroundColor(Color.RED);
                    answerButton0.setText("X");;
                    incorrect();
                }
                mSpotifyAppRemote.getPlayerApi().skipNext()
                        .setResultCallback(response->{
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            startRound();
                        });
                break;
            case R.id.answer1:
                if(answerButton1.getText().equals(song)) {
                    score++;
                    answerButton1.setBackgroundColor(Color.GREEN);
                    answerButton1.setText(checkedMark);
                    Log.d("CORRECT! Your score", String.valueOf(score));
                }
                else{
                    Log.d("Wrong, it was..", String.valueOf(song));
                    answerButton1.setBackgroundColor(Color.RED);
                    answerButton1.setText("X");
                    incorrect();
                }
                mSpotifyAppRemote.getPlayerApi().skipNext()
                        .setResultCallback(response->{
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            startRound();
                        });
                break;
            case R.id.answer2:
                if(answerButton2.getText() == song) {
                    score++;
                    answerButton2.setBackgroundColor(Color.GREEN);
                    answerButton2.setText(checkedMark);
                    Log.d("CORRECT! Your score", String.valueOf(score));
                }
                else{
                    Log.d("Wrong, it was..", String.valueOf(song));
                    answerButton2.setBackgroundColor(Color.RED);
                    answerButton2.setText("X");
                    incorrect();
                }
                mSpotifyAppRemote.getPlayerApi().skipNext()
                        .setResultCallback(response->{
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            startRound();
                        });
                break;
            case R.id.answer3:
                if(answerButton3.getText() == song) {
                    score++;
                    answerButton3.setBackgroundColor(Color.GREEN);
                    answerButton3.setText(checkedMark);
                    Log.d("CORRECT! Your score", String.valueOf(score));
                }
                else{
                    Log.d("Wrong, it was..", String.valueOf(song));
                    answerButton3.setBackgroundColor(Color.RED);
                    answerButton3.setText("X");
                    incorrect();
                }
                mSpotifyAppRemote.getPlayerApi().skipNext()
                        .setResultCallback(response->{
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            startRound();
                        });
                break;
            default:
                break;

        }
        fadeAnswersOut();
    }
}