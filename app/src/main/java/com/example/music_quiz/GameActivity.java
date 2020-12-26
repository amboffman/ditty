package com.example.music_quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.ListItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String CLIENT_ID = "afca0c6d0ea04e77b84465e4c5d9f2f3";
    private static final String REDIRECT_URI = "app://music.quiz";
    private SpotifyAppRemote mSpotifyAppRemote;
    private String song;
    private String playlistUri;
    private ArrayList<String> answers = new ArrayList<String>();
    public int score = 0;
    private String checkedMark = "\u2713";
    private int buttonBlue = Color.parseColor("#2196F3");
    public static final String EXTRA_MESSAGE = "com.example.music_quiz.SCORE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Button answer0_button = (Button)findViewById(R.id.answer0);
        answer0_button.setOnClickListener(this);

        Button answer1_button = (Button)findViewById(R.id.answer1);
        answer1_button.setOnClickListener(this);

        Button answer2_button = (Button)findViewById(R.id.answer2);
        answer2_button.setOnClickListener(this);

        Button answer3_button = (Button)findViewById(R.id.answer3);
        answer3_button.setOnClickListener(this);

        Button end_game_button = (Button)findViewById(R.id.endGameButton);
        end_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endGame();
            }
        });

    }
    public void endGame() {
        // Do something in response to button
        Intent endGameActivity = new Intent(this, EndGameActivity.class);
        endGameActivity.putExtra(EXTRA_MESSAGE, String.valueOf(this.score));
        startActivity(endGameActivity);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Set the connection parameters
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams, new Connector.ConnectionListener(){
            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote){
                mSpotifyAppRemote = spotifyAppRemote;
                Log.d("MainActivity", "Connected! Yay!");
                // Now you can start interacting with App Remote
                connected();
            }
            @Override
            public void onFailure(Throwable throwable) {
                Log.e("MainActivity", throwable.getMessage(), throwable);

                // Something went wrong when attempting to connect! Handle errors here
            }
        });
    }

    private void connected() {

        mSpotifyAppRemote.getContentApi().getRecommendedContentItems("DEFAULT")
                .setResultCallback(playlistRecommendations -> {
                    final ListItem recentlyPlayedPlaylists = playlistRecommendations.items[0];
                    mSpotifyAppRemote.getContentApi().getChildrenOfItem(recentlyPlayedPlaylists, 5, 0)
                            .setResultCallback(playlists->{
                                final ListItem lastPlaylist = playlists.items[0];
                                this.playlistUri = lastPlaylist.uri;
                            });
                });
        //Play on phone
        mSpotifyAppRemote.getConnectApi().connectSwitchToLocalDevice();
        //Shuffle playlist
        mSpotifyAppRemote.getPlayerApi().setShuffle(true);
        // Play
        mSpotifyAppRemote.getPlayerApi().play(this.playlistUri);
        startRound();

    }
    private void startRound(){
        answers.clear();
        TextView scoreValue = (TextView) findViewById(R.id.score);
        scoreValue.setText(String.valueOf(score));
//        mSpotifyAppRemote.getPlayerApi().subscribeToPlayerContext();
        mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    if (!answers.contains(playerState.track.name)) {
                        answers.add(playerState.track.name);
                        song = playerState.track.name;
                        if (answers.size() < 3) {
                            mSpotifyAppRemote.getPlayerApi().skipNext();
                        }
                        if (answers.size() == 3) {
                            mSpotifyAppRemote.getPlayerApi().skipNext()
                            .setResultCallback(cb->{
                                Long startMs = nextLong(new Random(playerState.track.duration),playerState.track.duration + 15000);
                                mSpotifyAppRemote.getPlayerApi().seekToRelativePosition(startMs);
                            });
                        }
                        if (answers.size() == 4) {
                            //do some code here
                            Collections.shuffle(answers);
                            setAnswers();

                    }
                    }
                });
}

    public long nextLong(Random rng, long n) {
        // error checking and 2^x checking removed for simplicity.
        long bits, val;
        do {
            bits = (rng.nextLong() << 1) >>> 1;
            val = bits % n;
        } while (bits-val+(n-1) < 0L);
        return val;
    }
    private void setAnswers(){
        Log.d("Answers", String.valueOf(answers));
        //Update answer button to song title
        Button answer0_button = (Button)findViewById(R.id.answer0);
        answer0_button.setBackgroundColor(buttonBlue);
        answer0_button.setText(String.valueOf(this.answers.get(0)));

        Button answer1_button = (Button)findViewById(R.id.answer1);
        answer1_button.setBackgroundColor(buttonBlue);
        answer1_button.setText(String.valueOf(this.answers.get(1)));

        Button answer2_button = (Button)findViewById(R.id.answer2);
        answer2_button.setBackgroundColor(buttonBlue);
        answer2_button.setText(String.valueOf(this.answers.get(2)));

        Button answer3_button = (Button)findViewById(R.id.answer3);
        answer3_button.setBackgroundColor(buttonBlue);
        answer3_button.setText(String.valueOf(this.answers.get(3)));
    }


    @Override
    protected void onStop() {
        super.onStop();
        mSpotifyAppRemote.getPlayerApi().pause();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.answer0:
                Button answer0_button = (Button)findViewById(R.id.answer0);
                if(answer0_button.getText() == song) {
                    score++;
                    answer0_button.setBackgroundColor(Color.GREEN);
                    answer0_button.setText(checkedMark);
                    Log.d("CORRECT! Your score", String.valueOf(score));
                }
                else{
                    Log.d("Wrong, it was..", String.valueOf(song));
                    answer0_button.setBackgroundColor(Color.RED);
                    answer0_button.setText("X");;
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
                Button answer1_button = (Button)findViewById(R.id.answer1);
                if(answer1_button.getText() == song) {
                    score++;
                    answer1_button.setBackgroundColor(Color.GREEN);
                    answer1_button.setText(checkedMark);
                    Log.d("CORRECT! Your score", String.valueOf(score));
                }
                else{
                    Log.d("Wrong, it was..", String.valueOf(song));
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
                Button answer2_button = (Button)findViewById(R.id.answer2);
                if(answer2_button.getText() == song) {
                    score++;
                    answer2_button.setBackgroundColor(Color.GREEN);
                    answer2_button.setText(checkedMark);
                    Log.d("CORRECT! Your score", String.valueOf(score));
                }
                else{
                    Log.d("Wrong, it was..", String.valueOf(song));
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
                Button answer3_button = (Button)findViewById(R.id.answer3);
                if(answer3_button.getText() == song) {
                    score++;
                    answer3_button.setBackgroundColor(Color.GREEN);
                    answer3_button.setText(checkedMark);
                    Log.d("CORRECT! Your score", String.valueOf(score));
                }
                else{
                    Log.d("Wrong, it was..", String.valueOf(song));
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
    }
}