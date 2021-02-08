package com.example.music_quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.music_quiz.START";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button actionButton = (Button) findViewById(R.id.actionButton);
        actionButton.setVisibility(View.GONE);
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
                    spotifyLogin();
                }
                else if(err.equals("capabilities err")){
                    //Premium spotify needed
                }
            }
        });

    }
    private void qualifyPlayer(){
        Button startGameButton = (Button) findViewById(R.id.actionButton);
        startGameButton.setBackgroundColor(Color.parseColor("#31a744"));
        startGameButton.setText("Start Game");
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
        startGameButton.setVisibility(View.VISIBLE);
    }

    private void spotifyLogin(){
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.spotify.music");
        Button spotifyLoginButton = (Button) findViewById(R.id.actionButton);
        spotifyLoginButton.setBackgroundColor(Color.parseColor("#31a744"));
        spotifyLoginButton.setText("Spotify Login");
        spotifyLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        });
        spotifyLoginButton.setVisibility(View.VISIBLE);
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