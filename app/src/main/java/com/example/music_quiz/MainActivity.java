package com.example.music_quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.spotify.android.appremote.api.SpotifyAppRemote;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.music_quiz.START";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start_game_button = (Button) findViewById(R.id.startGame);
        start_game_button.setVisibility(View.GONE);
        start_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }

    private void startGame() {
        // Do something in response to button
        Intent playlistSelectionActivity = new Intent(this, PlaylistSelectionActivity.class);
        playlistSelectionActivity.putExtra(EXTRA_MESSAGE, "start");
        startActivity(playlistSelectionActivity);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Connection connection = new Connection();
        connection.connectSpotify(this, new ConnectionCallback() {
            @Override
            public void onSuccess() {
                Log.d("Startup Connection:", "Successful");
                qualifyPlayer();
                connection.spotifyRemote.disconnect(connection.spotifyRemote);
            }

            @Override
            public void onError(String err) {
                Log.e("Startup Connection:", "Failed");
                if(err.equals("connection err")){
                    //Spotify login
                }
                else if(err.equals("capabilities err")){
                    //Premium spotify needed
                }
            }
        });

    }
    private void qualifyPlayer(){
        Button start_game_button = (Button) findViewById(R.id.startGame);
        start_game_button.setBackgroundColor(Color.parseColor("#31a744"));
        start_game_button.setVisibility(View.VISIBLE);
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