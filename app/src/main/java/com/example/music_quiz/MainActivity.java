package com.example.music_quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.spotify.android.appremote.api.SpotifyAppRemote;


public class MainActivity extends AppCompatActivity {

    private SpotifyAppRemote mSpotifyAppRemote;
    public static final String EXTRA_MESSAGE = "com.example.music_quiz.START";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start_game_button = (Button) findViewById(R.id.startGame);
        start_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }

    public void startGame() {
        // Do something in response to button
        Intent gameActivity = new Intent(this, GameActivity.class);
        gameActivity.putExtra(EXTRA_MESSAGE, "start");
        startActivity(gameActivity);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}